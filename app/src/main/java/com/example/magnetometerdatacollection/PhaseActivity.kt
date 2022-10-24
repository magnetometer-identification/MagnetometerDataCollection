package com.example.magnetometerdatacollection

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


@Suppress("DEPRECATION")
class PhaseActivity : AppCompatActivity() {
    lateinit var  textView: TextView
    lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phase)
        textView = findViewById(R.id.textView)
        button = findViewById(R.id.button2)
        textView.text = "Data collection is done in 5 stages. First you need to disable WiFi, Bluetooth, Mobile Data and make sure that your phone is not in Airpain mode." +
                "Then please click the button below!"
        button.setOnClickListener{
            val wifi : WifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
            val tm = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            val myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            val AirPlainMode = Settings.System.getInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) == 1

            println("test button AirPlainMode == " + AirPlainMode)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return@setOnClickListener
            }
            if (wifi.isWifiEnabled and myBluetoothAdapter.isEnabled and tm.isDataEnabled) {
                //TODO: Code to execute if wifi is enabled.
                println("test button")
            }
            println("test button MOB DATA " + tm.isDataEnabled)
        }

    }
}