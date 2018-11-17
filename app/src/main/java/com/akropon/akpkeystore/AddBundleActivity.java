package com.akropon.akpkeystore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddBundleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bundle);

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
        setResult(RESULT_CANCELED);
        finish();
    }

    private void onClickButtonOk() {
        String name = ((EditText) findViewById(R.id.textedit_name)).getText().toString();
        String description = ((EditText) findViewById(R.id.textedit_description)).getText().toString();
        String login = ((EditText) findViewById(R.id.textedit_login)).getText().toString();
        String password = ((EditText) findViewById(R.id.textedit_password)).getText().toString();

        boolean nameIsNormal = isNameNormal(name);
        boolean descriptionIsNormal = isDescriptionNormal(description);

        if (!nameIsNormal) {
            Toast.makeText(this, "Invalid name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!descriptionIsNormal) {
            Toast.makeText(this, "Invalid description", Toast.LENGTH_SHORT).show();
            return;
        }

        Bundle bundle = new Bundle(name, description, login, password);
        Intent intent = new Intent();
        intent.putExtra("bundle", bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean isNameNormal(String name) {
        // TODO: 16.11.18 impl
        return true;
    }

    private boolean isDescriptionNormal(String description) {
        // TODO: 16.11.18 impl
        return true;
    }
}
