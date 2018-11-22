package com.akropon.akpkeystore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

    private void onClickBtnDeleteDatabase() {
        UsersStorageManager.getInstance(this).deleteDatabase(this);
        Toast.makeText(this, "All data was deleted.", Toast.LENGTH_SHORT).show();
    }

    private void onClickBtnAddUser() {
        Intent intent = new Intent(this, AddUserActivity.class);
        startActivity(intent);
    }

    private void onClickBtnOk() {
        String login = ((EditText) findViewById(R.id.textedit_login)).getText().toString();
        String password = ((EditText) findViewById(R.id.textedit_password)).getText().toString();

        UsersStorageManager usersStorageManager = UsersStorageManager.getInstance(this);
        boolean canPass = usersStorageManager.isCorrectLoginPassword(login, password);

        if (canPass) {
            Intent intent = new Intent(this, ListOfBundlesActivity.class);
            intent.putExtra("login", login);
            intent.putExtra("password", password);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Wrong login or password", Toast.LENGTH_SHORT).show();
        }
    }
}
