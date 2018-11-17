package com.akropon.akpkeystore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.security.GeneralSecurityException;
import java.util.List;

public class ListOfBundlesActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CREATE_BUNDLE = 1;
    private static final int REQUEST_CODE_CHANGE_BUNDLE = 2;

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

            if (userKeyStorage == null) {
                Log.e("akropon", "Error: userKeyStorage is null.");
                finish();
                return;
            }

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
                launchCreateBundleActivity();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_CREATE_BUNDLE:
                onResultAddBundleActivity(resultCode, data);
            case REQUEST_CODE_CHANGE_BUNDLE:
                onResultChangeBundleActivity(resultCode, data);
        }
    }

    private void updateListView(ListView listView, UserKeyStorage userKeyStorage) throws GeneralSecurityException {
        List<Bundle> listOfBundles = userKeyStorage.getImageOfListOfBundles();
        ListAdapter listAdapter = new BundlesListAdapter(this, listOfBundles);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchShowBundleActivity(position);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                onLongClickItemInListOfBounds(view, position);
                return true;
            }
        });
    }

    private void onLongClickItemInListOfBounds(View itemView, final int bundlePositionInList) {
        PopupMenu popupMenu = new PopupMenu(this, itemView);
        popupMenu.inflate(R.menu.menu_for_bundle_in_list);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.watch:
                        launchShowBundleActivity(bundlePositionInList);
                        return true;
                    case R.id.change:
                        launchChangeBundleActivity(bundlePositionInList);
                        return true;
                    case R.id.delete:
                        deleteBundle(bundlePositionInList);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    private void deleteBundle(int bundlePositionInList) {
        ListView listView = findViewById(R.id.listview_bundles);
        Bundle bundle = (Bundle) listView.getItemAtPosition(bundlePositionInList);
        userKeyStorage.deleteBundle(bundle);

        try {
            updateListView(listView, userKeyStorage);
        } catch (GeneralSecurityException e) {
            Log.e("akropon", "error", e);
            finish();
        }
    }


    private void launchCreateBundleActivity() {
        Intent intent = new Intent(this, CreateBundleActivity.class);
        intent.putExtra("mode", "create");
        startActivityForResult(intent, REQUEST_CODE_CREATE_BUNDLE);
    }

    private void launchShowBundleActivity(int bundlePositionInList) {
        ListView listView = findViewById(R.id.listview_bundles);
        Bundle bundle = (Bundle) listView.getItemAtPosition(bundlePositionInList);
        Intent intent = new Intent(this, ShowBundleActivity.class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    private void launchChangeBundleActivity(int bundlePositionInList) {
        ListView listView = findViewById(R.id.listview_bundles);
        Bundle bundle = (Bundle) listView.getItemAtPosition(bundlePositionInList);
        Intent intent = new Intent(this, CreateBundleActivity.class);
        intent.putExtra("mode", "change");
        intent.putExtra("bundle", bundle);
        startActivityForResult(intent, REQUEST_CODE_CHANGE_BUNDLE);
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

    private void onResultChangeBundleActivity(int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        Bundle changedBundle = ((Bundle) data.getSerializableExtra("bundle"));

        try {
            userKeyStorage.changeExistingBundle(changedBundle);
        } catch (GeneralSecurityException e) {
            Log.e("akropon", "error changing existing bundle", e);
            return;
        }

        try {
            ListView listView = findViewById(R.id.listview_bundles);
            updateListView(listView, userKeyStorage);
        } catch (GeneralSecurityException e) {
            Log.e("akropon", "error updating listView of bundles", e);
        }
    }




}
