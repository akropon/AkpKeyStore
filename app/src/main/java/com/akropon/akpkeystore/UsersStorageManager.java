package com.akropon.akpkeystore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class UsersStorageManager {
    private static UsersStorageManager ourInstance;

    private DBHelper dbHelper;

    public static UsersStorageManager getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new UsersStorageManager(context);
        return ourInstance;
    }

    private UsersStorageManager(Context context) {
        dbHelper = new DBHelper(context);
    }

    Boolean isCorrectLoginPassword(String login, String password) throws NoSuchAlgorithmException {
        boolean result = false;

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from users", null);

        if (cursor.moveToFirst()) {
            do {
                String loginInDB = cursor.getString(cursor.getColumnIndex("login"));
                byte[] userPasswordHash = cursor.getBlob(cursor.getColumnIndex("password"));
                if (loginInDB.equals(login)) {
                    result = (Arrays.equals(userPasswordHash, CryptographyUtils.getHash(password)));
                    break;
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        dbHelper.close();

        return result;
    }

    public boolean isPasswordNormal(String password) {
        // TODO: 16.11.18 impl
        return password.length() > 0;
    }

    public boolean canLoginBeCreated(String login) {
        // TODO: 16.11.18 impl
        return login.length() > 0;
    }

    public boolean canPasswordBeCreated(String password) {
        // TODO: 16.11.18 impl
        return true;
    }

    public void addUser(String login, String password) throws NoSuchAlgorithmException {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("login", login);
        contentValues.put("password", CryptographyUtils.getHash(password));
        long newRowId = database.insert("users", null, contentValues);

        if (newRowId == -1) {
            Log.e("akropon", "error adding new user");
        } else {
            Log.i("akropon" ,"successfully added new user with rowId=" + newRowId);
        }

        dbHelper.close();
    }

    public UserKeyStorage getUserKeyStorage(String login, String password) throws NoSuchAlgorithmException {
        int userId = 0;
        boolean userFound = false;

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from users", null);

        if (cursor.moveToFirst()) {
            do {
                String loginInDB = cursor.getString(cursor.getColumnIndex("login"));
                byte[] userPasswordHash = cursor.getBlob(cursor.getColumnIndex("password"));
                if (loginInDB.equals(login)
                        && Arrays.equals(userPasswordHash, CryptographyUtils.getHash(password))) {

                    userId = cursor.getInt(cursor.getColumnIndex("id"));
                    userFound = true;
                    break;
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        dbHelper.close();

        if (!userFound) {
            Log.e("akropon", "there is no such user in db");
            return null;
        }

        return new UserKeyStorage(dbHelper, userId, password);
    }

    void deleteDatabase(Context context) {
        dbHelper.close();
        boolean result = context.deleteDatabase(dbHelper.getDatabaseName());
        Log.i("akropon", "deleting db. isSuccess = "+result);
    }
}
