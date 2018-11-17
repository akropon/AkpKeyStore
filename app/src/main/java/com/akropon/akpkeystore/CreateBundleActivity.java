package com.akropon.akpkeystore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateBundleActivity extends AppCompatActivity {

    private Bundle bundleToChange = null; // not null in "change mode", null in "create mode"

    private EditText editTextName;
    private EditText editTextDescription;
    private EditText editTextLogin;
    private EditText editTextPassword;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bundle);

        editTextName = findViewById(R.id.textedit_name);
        editTextDescription = findViewById(R.id.textedit_description);
        editTextLogin = findViewById(R.id.textedit_login);
        editTextPassword = findViewById(R.id.textedit_password);

        String mode = getIntent().getStringExtra("mode");
        if (mode.equalsIgnoreCase("change")) {
            bundleToChange = (Bundle) getIntent().getSerializableExtra("bundle");
            editTextName.setText(bundleToChange.getName());
            editTextDescription.setText(bundleToChange.getDescription());
            editTextLogin.setText(bundleToChange.getLogin());
            editTextPassword.setText(bundleToChange.getPassword());
        } else {
            editTextName.setText("");
            editTextDescription.setText("");
            editTextLogin.setText("");
            editTextPassword.setText("");
        }

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
        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        String login = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();

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

        Bundle bundle = new Bundle(bundleToChange == null ? Bundle.UNKNOWN_ID : bundleToChange.getId(),
                name, description, login, password);
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
