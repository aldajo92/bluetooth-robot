package com.example.algomez.bluetoothrobot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

public class MainActivity extends AppCompatActivity {

  @Override protected void onStart() {
    super.onStart();
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    BluetoothSPP bt = new BluetoothSPP(getBaseContext());

    if(!bt.isBluetoothAvailable()) {
      // any command for bluetooth is not available
      Toast.makeText(this, "No Available", Toast.LENGTH_LONG).show();
    }else{
      if(!bt.isBluetoothEnabled()) {
        // Do somthing if bluetooth is disable
      } else {
        // Do something if bluetooth is already enable
        bt.startService(BluetoothState.DEVICE_OTHER);
      }
    }
  }
}
