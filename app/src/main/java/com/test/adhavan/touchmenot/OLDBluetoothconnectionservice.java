package com.test.adhavan.touchmenot;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class OLDBluetoothconnectionservice {
    BluetoothAdapter bluetoothAdapter;
    Context context;

    private final BroadcastReceiver mBroadcastRecieveronoff = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d("Please work","!!!!");
            String action = intent.getAction();
            Log.d("Broadcast reiveronoff","action is:" + action);
            if (action.equals((BluetoothAdapter.ACTION_STATE_CHANGED))) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d("BTstate", "STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d("BTstate", "STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d("BTstate", "STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d("BTstate", "STATE TURNING OFF");
                        break;
                }
            }
        }
    };
    private final BroadcastReceiver mBroadcastRecieverdiscoverable = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);
                switch(mode){
                    //Device is in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d("discoverable","Discoverability enabled");
                        break;
                    // Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d("discoverable","Discoverability enabled, able to recieve connection" );
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d("discoverable","Discoverability enabled,not able to recieve connection" );
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d("discoverable","connecting" );
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d("discoverable","connected" );
                        break;
                }
            }
        }
    };
    /*private final BroadcastReceiver mBroadcastRecieverpair = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = new intent.getAction();
            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case: if bonded already
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                Log.d("pairingstate", "BroadcastReceiver: BOND_BONDED");
                }
                //case2: creating a bond
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDING){
                    Log.d("pairingstate", "BroadcastReceiver: BOND_BONDING");

                }
                //case3: breaking bond
                if(mDevice.getBondState() == BluetoothDevice.BOND_NONE){
                    Log.d("pairingstate", "BroadcastReceiver: BOND_NONE");

                }
            }
        }
    };
    */


    public OLDBluetoothconnectionservice(Context context){
        this.context = context;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        startupBT();
        makedevicediscoverable();
        findtouchmenotdevice();
        setupbluetoothpair();
    }




    // TO ENABLE BLUETOOTH ON THE DEVICE (CALLED 1st)
    void startupBT(){
        if(bluetoothAdapter == null){
            Log.d("bluetooth", "does not have BT capablities");
        }
        else {
            if (!bluetoothAdapter.isEnabled()) {
                Log.d("enableDisableBT:", "enabling BT");
                Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                context.startActivity(enableBTIntent);
                Log.d("has started", "enabled bluetooth");
                IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                context.registerReceiver(mBroadcastRecieveronoff, BTIntent);
                Log.d("enableDisableBT:", "readied the broadcast filter");

            }
            else{
                /*Log.d("enableDisableBT:", "bluetooth is already enabled");
                IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                Log.d("enableDisableBT:", "made the intent filter");
                registerReceiver(mBroadcastRecieveronoff, BTIntent);
                Log.d("enableDisableBT:", "readied the broadcast filter");
                */
            }
        }
    }

    //TO ENABLE DISCOVERABILITY ON DEVICE (CALLED 2nd)
    void makedevicediscoverable(){
        Log.d("discoverable", "making device discoverable for 100 secs");
        Intent discoverableintent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableintent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 100);
        context.startActivity(discoverableintent);
        Log.d("discoverable", "has made discoverable");
        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        context.registerReceiver(mBroadcastRecieverdiscoverable, intentFilter);
    }

    //TO FIND THE WEARBABLE DEVICE FROM A LIST OF CONNECTABLE DEVICES (CALLED 3rd)
    void findtouchmenotdevice(){

    }

    //TO PAIR TO THE TOUCHMENOT DEVICE (CALLED 4th)
    void setupbluetoothpair(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //Pairing
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        //registerReceiver(mBroadcastRecieverpair);
    }
}

