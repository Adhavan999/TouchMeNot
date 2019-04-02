package com.test.adhavan.touchmenot;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Bluetoothconnectionloader extends android.support.v4.content.AsyncTaskLoader<String> {


    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket btsocket;
    private Boolean BTisconnected;
    private static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private String mAddress = "00:13:04:83:89:E9";

    public Bluetoothconnectionloader(@NonNull Context context) {
        super(context);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BTisconnected = false;
    }

    @Nullable
    @Override
    public String loadInBackground() {
        boolean triggered = false;

        byte[] buffer = new byte[1024];
        int begin = 0;
        int bytes = 0;

        try{
            if (btsocket == null || !BTisconnected) {
                do {
                    BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(mAddress);//connects to the device's mAddress and checks if it's available
                    btsocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    bluetoothAdapter.cancelDiscovery();
                    btsocket.connect();//start connection
                }
                while (!btsocket.isConnected());
                Log.d("BLUETOOTH:", " HAS CONNECTED");

                //Recieving data from arduino
                while(!triggered){
                    try{
                        bytes += btsocket.getInputStream().read(buffer, bytes, buffer.length - bytes);
                        for(int i = begin; i < bytes; i++){
                            if(buffer[i] == "~".getBytes()[0]){
                                Log.d("TRIGGER:"," HAS BEEN TRIGGERED!");
                                triggered = true;
                                break;
                            }
                        }

                    }
                    catch (IOException e){
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        if(triggered)
            return "triggered";
        else
            return "not triggered";
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

}

