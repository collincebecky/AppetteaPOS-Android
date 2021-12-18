package com.appettea.appetteaPOS;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.IOException;
import java.util.Locale;


public class MainActivity extends AppCompatActivity

        implements NavigationDrawerFragment.NavigationDrawerCallbacks ,AddItemDialogue.DialogListener,EditItem.DialogListener {


    static {
       AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
   }

    /**
     * used to keep track of last fragment showing
     */
    public static final String KEY_LAST_FRAGMENT = "last_fragment";

    public static final int GALLERY_REQUEST =1;


    public static final int IMAGE_CAPTURE = 100;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private ImageView imageView;

    private SQLiteDatabase db;

    private SQLiteOpenHelper openHelper;

    /**
     * current fragment
     */
    private Fragment mContentFragment = null;
    private     ReceiveSms receiveSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        //super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openHelper = new DatabaseHelper(getApplicationContext());

        db = openHelper.getWritableDatabase();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getBsName());
        actionBar.setDisplayShowHomeEnabled(true);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Check Permission fails on Android OSes below Marshmallow, so the below if sentence is necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                        100);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 1000);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 1000);
        }


        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);


       // if(isAccountEmpty(db)) creatAccount();
        //else startLogin();




        startService(new Intent(MainActivity.this, ReceiveSms.class));
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }
    private String getBsName(){

        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.ACCOUNT, null);

        String bsname="";

        if (c.moveToFirst()) {

            do {

                bsname = c.getString(0);

                break;

            } while (c.moveToNext());

        }

        return bsname.toUpperCase(Locale.ROOT);

    }

    private void startLogin(){

        //Toast.makeText(this,"trying to login",Toast.LENGTH_SHORT).show();

       // DialogFragment newFragment = Login.newInstance(db);
       // newFragment.show(getSupportFragmentManager(), "dialog");

        Login login = new   Login(db);

        login.show(getSupportFragmentManager(),"login");

    }

    private void creatAccount(){

        // Create the fragment and show it as a dialog.
        CreateAccount createAccount  = new CreateAccount(db);

        createAccount.show(getSupportFragmentManager(),"account");

    }


    private boolean isAccountEmpty(SQLiteDatabase db){

            long NoOfRows = DatabaseUtils.queryNumEntries(db,DatabaseHelper.ACCOUNT);

            if (NoOfRows == 0){
                return true;
            } else {
                return false;
            }

    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state members from saved instance
        //mContentFragment = getSupportFragmentManager().getFragment(savedInstanceState, KEY_LAST_FRAGMENT);

    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (mContentFragment != null && mContentFragment.isAdded() && getSupportFragmentManager() != null) {
            getSupportFragmentManager().putFragment(savedInstanceState,
                    KEY_LAST_FRAGMENT, mContentFragment);
        }
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // make sure we always have some default
        if (mContentFragment == null) {
            mContentFragment = new FirstFragment();
        }
        // no backstack here
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mContentFragment)
                .commit();



    }

    @Override
    public void onNavigationDrawerItemSelected(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // use default here
        if (fragment != null) {
            // update the main content by replacing fragments
            switchContent(fragment);
        }
    }

    /**
     * helper to switch content with backstack
     *
     * @param fragment
     */
    public void switchContent(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                        // add to backstack
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
        mContentFragment = fragment;
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) {
           FirstFragment sf = new FirstFragment();
            switchContent(sf);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void startCamera(ImageView imageView) {



        this.imageView = imageView;
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);


    }

    @Override
    public void startCameraView(ImageView imageView) {

        this.imageView = imageView;

        Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,100);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {

                case GALLERY_REQUEST:

                    Uri selectedImage = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(MainActivity.this.getContentResolver(), selectedImage);
                        imageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;

                case IMAGE_CAPTURE:

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                    imageView.setImageBitmap(bitmap);

                    break;


            }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);


        if (requestCode == 1000) {
            if(grantResults.length==0)return;;
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                Toast.makeText(getApplicationContext(), "permission granted", Toast.LENGTH_SHORT).show();
            else{

                Toast.makeText(getApplicationContext(), "permission denied", Toast.LENGTH_SHORT).show();

                finish();
            }

        }

    }

}

//0727933990 # ilham Taisir