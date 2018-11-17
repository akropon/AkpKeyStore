package com.akropon.akpkeystore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

public class AddUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButtonOk();
            }
        });
        findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButtonCancel();
            }
        });
    }

    private void onClickButtonCancel() {
        this.finish();
    }

    private void onClickButtonOk() {
        String login = ((EditText) findViewById(R.id.textedit_login)).getText().toString();
        String password = ((EditText) findViewById(R.id.textedit_password)).getText().toString();

        UsersStorageManager usersStorageManager = UsersStorageManager.getInstance(this);

        boolean passwordIsNormal = usersStorageManager.isPasswordNormal(password);
        boolean loginCanBeCreated = usersStorageManager.canLoginBeCreated(login);
        boolean passwordCanBeCreated = usersStorageManager.canPasswordBeCreated(password);

        if (!passwordIsNormal) {
            Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!loginCanBeCreated) {
            Toast.makeText(this, "This login can't be created.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!passwordCanBeCreated) {
            Toast.makeText(this, "This password can't be created.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            usersStorageManager.addUser(login, password);
            Toast.makeText(this, "New user created.", Toast.LENGTH_SHORT).show();
            finish();
        } catch (NoSuchAlgorithmException e) {
            Log.e("akropon", "error adding new user.", e);
        }
    }


}
