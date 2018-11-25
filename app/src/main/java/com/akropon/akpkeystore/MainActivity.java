package com.akropon.akpkeystore;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBtnOk();
            }
        });
        findViewById(R.id.button_add_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBtnAddUser();
            }
        });
        findViewById(R.id.button_delete_database).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBtnDeleteDatabase();
            }
        });
    }

    private void onClickBtnAddUser() {
        Intent intent = new Intent(this, AddUserActivity.class);
        startActivity(intent);
    }

    private void onClickBtnOk() {
        String login = ((EditText) findViewById(R.id.textedit_login)).getText().toString();
        String password = ((EditText) findViewById(R.id.textedit_password)).getText().toString();

        UsersStorageManager usersStorageManager = UsersStorageManager.getInstance(this);
        boolean canPass = false;
        try {
            canPass = usersStorageManager.isCorrectLoginPassword(login, password);
        } catch (NoSuchAlgorithmException e) {
            Log.e("akropon", "error while checking login-password", e);
            Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
            return;
        }

        if (canPass) {
            Intent intent = new Intent(this, ListOfBundlesActivity.class);
            intent.putExtra("login", login);
            intent.putExtra("password", password);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Wrong login or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void onClickBtnDeleteDatabase() {

        final Context context = this;
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UsersStorageManager.getInstance(context).deleteDatabase(context);
                Toast.makeText(context, "All data was deleted.", Toast.LENGTH_SHORT).show();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener).
                setNegativeButton("No", null).show();
    }
}
