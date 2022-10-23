package com.example.magnetometerdatacollection

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aware.Accelerometer
//import com.aware.Accelerometer
import com.aware.Aware
import com.aware.Aware_Preferences
import com.aware.providers.Accelerometer_Provider
//import com.aware.providers.Accelerometer_Provider
import com.bumptech.glide.Glide


@Suppress("UNREACHABLE_CODE")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Aware.startAWARE(this) //initialise core AWARE service
        //sampling frequency in microseconds
        Aware.setSetting(this, Aware_Preferences.FREQUENCY_ACCELEROMETER,
            200000)
        // intensity threshold to report the reading
        Aware.setSetting(this, Aware_Preferences.THRESHOLD_ACCELEROMETER,
            0.02f)

        val imageView: ImageView = findViewById(R.id.imageView)
//        Glide.with(this).load(R.drawable.gears2).into(imageView)
//        Glide.with(this).asBitmap().load(R.drawable.gears2).into(imageView)

        Glide.with(this).asGif().load(R.drawable.gears2).into(imageView)
        imageView.setVisibility(View.INVISIBLE)

        Accelerometer.setSensorObserver {
        val x = it.getAsDouble(Accelerometer_Provider.Accelerometer_Data.VALUES_0)
        val y = it.getAsDouble(Accelerometer_Provider.Accelerometer_Data.VALUES_1)
        val z = it.getAsDouble(Accelerometer_Provider.Accelerometer_Data.VALUES_2)
        runOnUiThread(java.lang.Runnable {
            Log.i(TAG, "X:"+x.toString()+" Y:"+ y.toString()+" Z:" + z.toString())
        })
    }
    }

    override fun onResume() {
        super.onResume()
        Aware.startAccelerometer(this)
        val startBtn: Button = findViewById(R.id.button) as Button
        val textView2: TextView = findViewById(R.id.textView2)
        startBtn.setOnClickListener{
            val imageView = this.findViewById<ImageView>(R.id.imageView)
            imageView . setVisibility (View.VISIBLE)
            textView2.text = "Collecting..."
        }
//        try {
//            Accelerometer.getSensorObserver()
//            Toast.makeText(this, "Pass", Toast.LENGTH_SHORT).show()
//        }
//        catch (e: Exception)
//        {
//            Toast.makeText(this, "Crash", Toast.LENGTH_SHORT).show()
//        }
    }

    override fun onPause() {
        super.onPause()
        Aware.stopAccelerometer(this)
    }

//    fun collecting(){
//
//    }
}


