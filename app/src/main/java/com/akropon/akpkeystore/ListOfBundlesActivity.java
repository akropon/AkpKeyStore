package com.akropon.akpkeystore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.security.GeneralSecurityException;
import java.util.List;

public class ListOfBundlesActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_BUNDLE = 1;

    private UserKeyStorage userKeyStorage;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_bundles);
        Log.i("akropon", "onCreate() invoked");

        String login = getIntent().getStringExtra("login");
        String password = getIntent().getStringExtra("password");

        try {
            UsersStorageManager usersStorageManager = UsersStorageManager.getInstance(this);
            userKeyStorage = usersStorageManager.getUserKeyStorage(login, password);

            // TODO: 17.11.18 check userKeyStorage is not null

            ListView listView = findViewById(R.id.listview_bundles);
            updateListView(listView, userKeyStorage);
        } catch (GeneralSecurityException e) {
            Log.e("akropon", "error", e);
            finish();
            return;
        }

        findViewById(R.id.button_add_bundle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButtonAddBundle();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ADD_BUNDLE:
                onResultAddBundleActivity(resultCode, data);
        }
    }

    private void onResultAddBundleActivity(int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        Bundle newBundle = ((Bundle) data.getSerializableExtra("bundle"));


        try {
            userKeyStorage.addNewBundle(newBundle);
        } catch (GeneralSecurityException e) {
            Log.e("akropon", "error adding new bundle", e);
            return;
        }

        try {
            ListView listView = findViewById(R.id.listview_bundles);
            updateListView(listView, userKeyStorage);
        } catch (GeneralSecurityException e) {
            Log.e("akropon", "error updating listView of bundles", e);
        }
    }

    private void updateListView(ListView listView, UserKeyStorage userKeyStorage) throws GeneralSecurityException {
        List<Bundle> listOfBundles = userKeyStorage.getImageOfListOfBundles();
        ListAdapter listAdapter = new BundlesListAdapter(this, listOfBundles);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickItemInListOfBundles(position);
            }
        });
    }

    private void onClickButtonAddBundle() {
        Intent intent = new Intent(this, AddBundleActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_BUNDLE);
    }

    private void onClickItemInListOfBundles(int position) {
        ListView listView = findViewById(R.id.listview_bundles);
        Bundle bundle = (Bundle) listView.getItemAtPosition(position);
        Intent intent = new Intent(this, ShowBundleActivity.class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

}
