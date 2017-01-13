package com.example.algomez.bluetoothrobot;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements BluetoothSPP.OnDataReceivedListener {

  //@BindView(R.id.show_image) Button connectCta;
  @BindView(R.id.image_viewer) ImageView imageViewer;
  @BindView(R.id.text_to_show) TextView textToShow;
  private BluetoothSPP bt;
  boolean flag = true;
  StringBuilder split_image;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    bt = new BluetoothSPP(getBaseContext());
    bt.setOnDataReceivedListener(this);

    split_image = new StringBuilder();

    if (!bt.isBluetoothAvailable()) {
      // any command for bluetooth is not available
      Toast.makeText(this, "No Available", Toast.LENGTH_LONG).show();
    } else {
      if (!bt.isBluetoothEnabled()) {
        // Do somthing if bluetooth is disable
      } else {
        // Do something if bluetooth is already enable
        bt.setupService();
        bt.startService(BluetoothState.DEVICE_OTHER);
      }
    }
  }

  @OnClick(R.id.show_image) public void showImage() {
    String received = split_image.toString();
    decodeImage(received);
  }

  @OnClick(R.id.connect_cta) public void connect() {
    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
  }

  @OnClick(R.id.send_data_cta) public void sendData() {
    if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
      bt.send((flag ? "led-1-1\n" : "led-1-0\n"), false);
      flag = !flag;
    }
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
      if (resultCode == Activity.RESULT_OK) bt.connect(data);
    } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
      if (resultCode == Activity.RESULT_OK) {
        bt.setupService();
        bt.startService(BluetoothState.DEVICE_OTHER);
        //setup();
      } else {
        // Do something if user doesn't choose any device (Pressed back)
      }
    }
  }

  @Override public void onDataReceived(byte[] data, String message) {
    split_image.append(message);
    //textToShow.setText(message);
  }

  public void decodeImage(String encodedImage) {
    byte[] decodedString = Base64.decode(encodedImage, Base64.URL_SAFE);
    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    imageViewer.setImageBitmap(decodedByte);
  }
}
