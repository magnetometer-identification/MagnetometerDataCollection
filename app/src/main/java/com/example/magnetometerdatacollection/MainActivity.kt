package com.example.magnetometerdatacollection

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.aware.Aware
import com.aware.Magnetometer
import com.aware.providers.Magnetometer_Provider
import com.bumptech.glide.Glide
import java.util.*
import kotlin.concurrent.schedule


@Suppress("UNREACHABLE_CODE")
class MainActivity : AppCompatActivity() {

    lateinit var startBtn: Button
    lateinit var  textView2: TextView
    lateinit var imageView: ImageView
    lateinit var UserIDfield: EditText

    lateinit var deviceID: String
    lateinit var UserID: String


    var done: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startActivity(Intent(this@MainActivity, PhaseActivity::class.java))

        startBtn = findViewById(R.id.button)
        textView2 = findViewById(R.id.textView2)
        imageView = findViewById(R.id.imageView)
        UserIDfield = findViewById(R.id.UserIDfield)


        Aware.startAWARE(applicationContext)

        Magnetometer.setSensorObserver {
            val x = it.getAsDouble(Magnetometer_Provider.Magnetometer_Data.VALUES_0)
            val y = it.getAsDouble(Magnetometer_Provider.Magnetometer_Data.VALUES_1)
            val z = it.getAsDouble(Magnetometer_Provider.Magnetometer_Data.VALUES_2)
            println("x = $x y = $y, z = $z")
        }
//        Glide.with(this).load(R.drawable.gears2).into(imageView)
//        Glide.with(this).asBitmap().load(R.drawable.gears2).into(imageView)

//        Glide.with(this).asGif().load(R.drawable.gears2).into(imageView)
//        imageView.visibility = View.INVISIBLE

    }

    override fun onResume() {
        super.onResume()
        collecting()
    }

    override fun onPause() {
        super.onPause()
        Aware.stopMagnetometer(this)
    }

    fun collecting(){
        startBtn.setOnClickListener{
            if ((UserIDfield.isFocused) and (UserIDfield.text.length != 0)){
                Glide.with(this).asGif().load(R.drawable.gears2).into(imageView)
                imageView.visibility = View.VISIBLE
                textView2.text = "Collecting..."
                startBtn.isEnabled = false;

                Aware.startMagnetometer(this)

                Timer().schedule(10000) {
                    Aware.stopMagnetometer(applicationContext)
                    println("Readings stopped!")
                    runOnUiThread(java.lang.Runnable {
                        Glide.with(applicationContext).asBitmap().load(R.drawable.done_icon).into(imageView)
    //                    imageView.visibility = View.INVISIBLE
                        textView2.text = "Data collected!"
                        startBtn.isEnabled = true;
                        done = true
                    })
                }

                try {
                    Magnetometer.getSensorObserver()
                    Toast.makeText(this, "Pass", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this, "Crash", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Please input some username first!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


