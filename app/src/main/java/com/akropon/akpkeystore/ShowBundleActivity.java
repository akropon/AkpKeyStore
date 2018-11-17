package com.akropon.akpkeystore;

import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class ShowBundleActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextDescription;
    private EditText editTextLogin;
    private EditText editTextPassword;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bundle);

        editTextName = findViewById(R.id.textedit_name);
        editTextDescription = findViewById(R.id.textedit_description);
        editTextLogin = findViewById(R.id.textedit_login);
        editTextPassword = findViewById(R.id.textedit_password);

        Bundle bundle = ((Bundle) getIntent().getSerializableExtra("bundle"));
        editTextName.setText(bundle.getName());
        editTextDescription.setText(bundle.getDescription());
        editTextLogin.setText(bundle.getLogin());
        editTextPassword.setText(bundle.getPassword());

        EditText[] editTexts = {editTextName, editTextDescription, editTextLogin, editTextPassword};
        for (EditText editText : editTexts) {
            editText.setOnKeyListener(null);
            editText.setFocusable(true);
            editText.setTextIsSelectable(true);
            editText.setCursorVisible(false);
            editText.addTextChangedListener(new TextChangesBlocker(editText.getText().toString()));
        }



        findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButtonBack();
            }
        });
    }

    private void onClickButtonBack() {
        finish();
    }


    private class TextChangesBlocker implements TextWatcher {

        private final String staticText;
        private boolean enabled;

        public TextChangesBlocker(String staticText) {
            this.staticText = staticText;
            enabled = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (enabled) {
                enabled = false;
                s.clear();
                s.append(staticText);
                enabled = true;
            }
        }
    }
}
