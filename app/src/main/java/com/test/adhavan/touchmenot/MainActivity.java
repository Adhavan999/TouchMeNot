package com.test.adhavan.touchmenot;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;

import java.lang.reflect.Array;


public class MainActivity extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<String> {
    Button sos_contacts, settings, pair_device;
    public TextView bluetooth_status;
    public String SOS_contact_name;
    public static final int MY_PERMISSIONS_REQUEST = 90;
    public String[] needed_permissions = {Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};
    public String[] permissions_to_ask = {};
    Boolean permissions_granted = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initstuff();
        comingbackformcontactpage();
        settingbuttonlisteners();
        checkpermissions();
        getSupportLoaderManager().initLoader(0, null,this);
    }

    void initstuff(){
        sos_contacts = findViewById(R.id.SOS_contacts_button);
        settings = findViewById(R.id.settings_button);
        pair_device = findViewById(R.id.pair_device_button);
        bluetooth_status = findViewById(R.id.pair_status);
    }

    void checkpermissions(){
        for (String needed_permission : needed_permissions) {
            if (ContextCompat.checkSelfPermission(this, needed_permission) != PackageManager.PERMISSION_GRANTED) {
                permissions_granted = false;
                permissions_to_ask = ArrayUtils.appendToArray(permissions_to_ask, needed_permission);
                Log.d("PERMISSIONS:", "THE " + needed_permission + " ISN'T AVAILABLE");
            }
        }
        if(permissions_granted){
            Log.d("PERMISSIONS:", "THE PERMISSIONS ARE AVAILABLE");
        }
        else{
            Log.d("PERMISSIONS:", "REQUESTING FOR PERMISSIONS");
            ActivityCompat.requestPermissions(this,permissions_to_ask,MY_PERMISSIONS_REQUEST);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissions_granted = true;
        for (String needed_permission : needed_permissions) {
            if (ContextCompat.checkSelfPermission(this, needed_permission) != PackageManager.PERMISSION_GRANTED) {
                permissions_granted = false;
                Log.d("PERMISSIONS:", "THE " + needed_permission + " WASN'T GRANTED");
            }
        }
        if(!permissions_granted){
            Toast.makeText(this,"The app needs those permission for running properly. Please enable them.",Toast.LENGTH_LONG).show();
        }
    }

    void comingbackformcontactpage(){
        if(getIntent().getExtras() != null) {
            if (getIntent().getExtras().getBoolean("made_contact")) {
                SharedPreferences sharedPreferences = getSharedPreferences("contacts_preferences", 0);
                SOS_contact_name = sharedPreferences.getString(getString(R.string.contact1_name), "contact");
                Toast.makeText(getApplicationContext(), SOS_contact_name + " is now your SOS contact", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Couldn't make contact. Try again.", Toast.LENGTH_LONG).show();
            }
        }
    }

    void settingbuttonlisteners(){
        //-----------------------------
        //SOS contact button
        sos_contacts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContactPage.class);
                startActivity(intent);
            }
        });
        //-----------------------------
        //Pair device button
        pair_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //OLDBluetoothconnectionservice OLDBluetoothconnectionservice = new OLDBluetoothconnectionservice(MainActivity.this);
                Intent intent = new Intent(getApplicationContext(), Alert_Activity.class);
                startActivity(intent);
            }
        });
    }


    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d("Loader:", " Loader has started");
        return new Bluetoothconnectionloader(this);
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<String> loader, String data) {
        Log.d("Loader:", " Loader has Finished (data has been recieved)");
        Log.d("Loader data:",data);
        if(data == "triggered") {
            Intent intent = new Intent(getApplicationContext(), Alert_Activity.class);
            startActivity(intent);
        }
        else{
            getSupportLoaderManager().restartLoader(0, null,this);
        }

    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<String> loader) {
        Log.d("Loader:", " Loader has Reset");
    }
}




