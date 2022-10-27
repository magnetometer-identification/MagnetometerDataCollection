package com.example.magnetometerdatacollection

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.DialogInterface
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
        checkStage(false)
        button.setOnClickListener {
            checkStage(true)
        }

    }

    @SuppressLint("SetTextI18n")
    fun checkStage(inListener: Boolean){
        val sharedPref = getSharedPreferences("SharedVal", MODE_PRIVATE)
        val wifi: WifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
        val tm = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        val myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val AirPlainMode =
            Settings.System.getInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) == 1

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println("No READ_PHONE_STATE permission!")
            ActivityCompat.requestPermissions(this, Array<String>(1){Manifest.permission.READ_PHONE_STATE},PackageManager.PERMISSION_GRANTED)
        }

        val tmEnabled =tm.isDataEnabled

        if (sharedPref.getInt("STAGE",-1) == 1){
            textView.text = "CURRENT STAGE: STAGE 1\n" +
                    "For this stage make sure that:\n"+
                    "WiFi is OFF\n" +
                    "Bluetooth is OFF\n" +
                    "Mobile Data is OFF\n" +
                    "Airplane mode is OFF\n" +
                    "Then please click the button below to continue!"
            val PASS_STAGE = !(wifi.isWifiEnabled) and !(tmEnabled) and !(myBluetoothAdapter.isEnabled) and !(AirPlainMode);
            if (PASS_STAGE and inListener){
                println("Stage 1 PASSED!")
                finish()
            }
        }
        else if (sharedPref.getInt("STAGE",-1) == 2){
            textView.text = "CURRENT STAGE: STAGE 2\n" +
                    "For this stage make sure that:\n"+
                    "WiFi is ON\n" +
                    "Bluetooth is OFF\n" +
                    "Mobile Data is OFF\n" +
                    "Airplane mode is OFF\n" +
                    "Then please click the button below to continue!"
            val PASS_STAGE = (wifi.isWifiEnabled) and !(tmEnabled) and !(myBluetoothAdapter.isEnabled) and !(AirPlainMode);
            if (PASS_STAGE and inListener){
                println("Stage 2 PASSED!")
                finish()
            }
        }

        else if (sharedPref.getInt("STAGE",-1) == 3){
            textView.text = "CURRENT STAGE: STAGE 3\n" +
                    "For this stage make sure that:\n"+
                    "WiFi is OFF\n" +
                    "Bluetooth is OFF\n" +
                    "Mobile Data is ON\n" +
                    "Airplane mode is OFF\n" +
                    "Then please click the button below to continue!"
            val PASS_STAGE = !(wifi.isWifiEnabled) and (tmEnabled) and !(myBluetoothAdapter.isEnabled) and !(AirPlainMode);
            if (PASS_STAGE and inListener){
                println("Stage 3 PASSED!")
                finish()
            }
        }
        else if (sharedPref.getInt("STAGE",-1) == 4){
            textView.text = "CURRENT STAGE: STAGE 4\n" +
                    "For this stage make sure that:\n"+
                    "WiFi is OFF\n" +
                    "Bluetooth is ON\n" +
                    "Mobile Data is OFF\n" +
                    "Airplane mode is OFF\n" +
                    "Then please click the button below to continue!"
            val PASS_STAGE = !(wifi.isWifiEnabled) and !(tmEnabled) and (myBluetoothAdapter.isEnabled) and !(AirPlainMode);
            if (PASS_STAGE and inListener){
                println("Stage 4 PASSED!")
                finish()
            }
        }

        else if (sharedPref.getInt("STAGE",-1) == 5){
            textView.text = "CURRENT STAGE: STAGE \n" +
                    "For this stage make sure that:\n"+
                    "WiFi is OFF\n" +
                    "Bluetooth is OFF\n" +
                    "Mobile Data is OFF\n" +
                    "Airplane mode is ON\n" +
                    "Then please click the button below to continue!"
            val PASS_STAGE = !(wifi.isWifiEnabled) and !(tmEnabled) and !(myBluetoothAdapter.isEnabled) and (AirPlainMode);
            if (PASS_STAGE and inListener){
                println("Stage 5 PASSED!")
                finish()
            }
        }else if (sharedPref.getInt("STAGE",-1) == 6){
            textView.text = "CURRENT STAGE: STAGE 6\n" +
                    "For this stage make sure that:\n"+
                    "WiFi is ON\n" +
                    "Bluetooth is ON\n" +
                    "Mobile Data is ON\n" +
                    "Airplane mode is ON\n" +
                    "Then please click the button below to continue!"
            val PASS_STAGE = (wifi.isWifiEnabled) and (tmEnabled) and (myBluetoothAdapter.isEnabled) and (AirPlainMode);
            if (PASS_STAGE and inListener){
                println("Stage 6 PASSED!")
                finish()
            }
        }
    }

}