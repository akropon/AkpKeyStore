package com.akropon.akpkeystore;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class UserKeyStorage {

    private final DBHelper dbHelper;
    private final int userId;
    private final String password;


    UserKeyStorage(DBHelper dbHelper, int userId, String password) {
        this.dbHelper = dbHelper;
        this.userId = userId;
        this.password = password;
    }

    List<Bundle> getImageOfListOfBundles() throws GeneralSecurityException {

        List<Bundle> listOfBundles = new ArrayList<>();

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from bundles where userId="+String.valueOf(userId), null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                byte[] bundleNameEncrypted = cursor.getBlob(cursor.getColumnIndex("name"));
                byte[] bundleDescriptionEncrypted = cursor.getBlob(cursor.getColumnIndex("description"));
                byte[] bundleLoginEncrypted = cursor.getBlob(cursor.getColumnIndex("login"));
                byte[] bundlePasswordEncrypted = cursor.getBlob(cursor.getColumnIndex("password"));

                Bundle bundle = new Bundle(id,
                        CryptographyUtils.decode(bundleNameEncrypted, password),
                        CryptographyUtils.decode(bundleDescriptionEncrypted, password),
                        CryptographyUtils.decode(bundleLoginEncrypted, password),
                        CryptographyUtils.decode(bundlePasswordEncrypted, password));

                listOfBundles.add(bundle);

            } while (cursor.moveToNext());
        }

        cursor.close();
        dbHelper.close();

        return listOfBundles;
    }

    void addNewBundle(Bundle bundle) throws GeneralSecurityException {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", userId);
        contentValues.put("name", CryptographyUtils.encode(bundle.getName(), password));
        contentValues.put("description", CryptographyUtils.encode(bundle.getDescription(), password));
        contentValues.put("login", CryptographyUtils.encode(bundle.getLogin(), password));
        contentValues.put("password", CryptographyUtils.encode(bundle.getPassword(), password));
        long newRowId = database.insert("bundles", null, contentValues);

        if (newRowId == -1) {
            Log.e("akropon", "error adding new bundle");
        } else {
            Log.i("akropon" ,"successfully added new bundle with rowId=" + newRowId);
        }

        dbHelper.close();
    }

    void changeExistingBundle(Bundle changedBundle) throws GeneralSecurityException {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", userId);
        contentValues.put("name", CryptographyUtils.encode(changedBundle.getName(), password));
        contentValues.put("description", CryptographyUtils.encode(changedBundle.getDescription(), password));
        contentValues.put("login", CryptographyUtils.encode(changedBundle.getLogin(), password));
        contentValues.put("password", CryptographyUtils.encode(changedBundle.getPassword(), password));

        long changedAmount = database.update("bundles", contentValues,
                "id = "+String.valueOf(changedBundle.getId()), null);

        if (changedAmount != 1) {
            Log.e("akropon", "error changing bundle. Changed amount = "+changedAmount);
        }

        dbHelper.close();
    }


    void deleteBundle(Bundle bundle) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        int deletedAmount = database.delete(
                "bundles", "id = " + String.valueOf(bundle.getId()), null);

        if (deletedAmount != 1) {
            Log.e("akropon", "error deleting bundle. Deleted amount = "+deletedAmount);
        }

        dbHelper.close();
    }
}
