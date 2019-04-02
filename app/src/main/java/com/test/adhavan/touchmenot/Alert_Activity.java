package com.test.adhavan.touchmenot;

import android.Manifest;
import android.content.SharedPreferences;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InvalidObjectException;

public class Alert_Activity extends AppCompatActivity {

    public String phno, name, location_string = "...not sure...", TAG = "ALERT_ACTIVITY";
    TextView sos_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_);
        sos_name = findViewById(R.id.ALERT_contact);
        Log.d(TAG, " has been created");
        getlocationcoordinates();
    }

    void getlocationcoordinates(){
        try {
            FusedLocationProviderClient fusedLocationClient;
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            Log.d("LOCATION","fused location client made");
            if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.LOCATION) != PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            Log.d("LOCATION:", "UNABLE TO GET LOCATION");
                        } else {
                            Log.d("LOCATION:", "LOCATION IS :" + location.toString());
                            location_string = "http://maps.google.com/?q=<" + location.getLongitude() + ">,<" + location.getLatitude() + ">";
                            Log.d("LOCATION:", "LOCATION STRING:" + location_string);
                            sendsms();
                        }
                    }
                });
            }
            else {
                Toast.makeText(this, "SHOULD HAVE GIVEN LOCATION PERMISSIONS ¯\\_(ツ)_/¯", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    void sendsms(){
        SmsManager smsManager = SmsManager.getDefault();
        Log.d(TAG, "SMSmanager made");
        SharedPreferences sharedPreferences = getSharedPreferences("contacts_preferences",0);
        name = sharedPreferences.getString(getString(R.string.contact1_name),"default emergency contact");
        Log.d(TAG, " name" + name);
        sos_name.setText(String.format("ALERTING:%s", name.toUpperCase()));
        phno = sharedPreferences.getString(getString(R.string.contact1_no), "default phno");
        Log.d(TAG, "ph:" + phno);
        smsManager.sendTextMessage(phno, null,"SOS: I NEED HELP!" + "..find me at:" + location_string,null,null);
        Log.d("SMS:", "SMS HAS BEEN SENT");
    }
}
