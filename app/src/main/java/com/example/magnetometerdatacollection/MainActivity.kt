package com.example.magnetometerdatacollection

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
<<<<<<< Updated upstream
=======
import android.hardware.SensorEvent
>>>>>>> Stashed changes
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.aware.Aware
import com.aware.Aware_Preferences
import com.aware.Magnetometer
import com.aware.providers.Magnetometer_Provider
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import java.io.File
import java.util.*
import kotlin.concurrent.schedule
<<<<<<< Updated upstream
import android.hardware.SensorEvent
import android.util.Log


@Suppress("UNREACHABLE_CODE")
abstract class MainActivity : AppCompatActivity(), SensorEventListener {

    // Sensors & SensorManager
//    private lateinit var magnetometer: Sensor
//    private lateinit var mSensorManager: SensorManager
//    // Storage for Sensor readings
//    private lateinit var mGeomagnetic: FloatArray
=======
import kotlin.properties.Delegates

@Suppress("UNREACHABLE_CODE")
class MainActivity : AppCompatActivity(), SensorEventListener {
    // Sensors & SensorManager
    private lateinit var magnetometer: Sensor
    private lateinit var mSensorManager: SensorManager
    // Storage for Sensor readings
    private lateinit var mGeomagnetic:  ArrayList<Float>
>>>>>>> Stashed changes

    var con by Delegates.notNull<Int>()
    lateinit var startBtn: Button
    lateinit var  textView2: TextView
    lateinit var imageView: ImageView
    lateinit var UserIDfield: EditText

    lateinit var deviceID: String
    lateinit var UserID: String


    var SET_of_STAGES = setOf<Int> (1,2,3,4,5,6)

    var done: Boolean = false
    data class DataCollected (
        val id: Int,
        val username: String,
        val device_model: String,
        val stage: Int,
        val mtx_xyzt: List<List<Any>>
            )
    lateinit var instanceOfList: MutableMap<String,Any>
    lateinit var id: String
    lateinit var list_X: ArrayList<Any>
    lateinit var list_Y: ArrayList<Any>
    lateinit var list_Z: ArrayList<Any>
    lateinit var list_timeStamp: ArrayList<Any>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        // Get a reference to the SensorManager
//        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        // Get a reference to the magnetometer
//        magnetometer = mSensorManager!!
//            .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
//        // Exit unless sensor are available
//        if (null == magnetometer)
//            finish();

        val sharedPref: SharedPreferences = getSharedPreferences("SharedVal", MODE_PRIVATE)
//        SET_of_STAGES = SET_of_STAGES.minusElement(sharedPref.getInt("STAGE", -1))
        changeStage(SET_of_STAGES.random())
//        println(SET_of_STAGES + "aaaaaaaaaaaaaaaaaaa")

        startActivity(Intent(this@MainActivity, PhaseActivity::class.java))

        // Get a reference to the SensorManager
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // Get a reference to the magnetometer
        magnetometer = mSensorManager!!
            .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        // Exit unless sensor are available
        if (null == magnetometer)
            finish();
        con = 0

        startBtn = findViewById(R.id.button)
        textView2 = findViewById(R.id.textView2)
        imageView = findViewById(R.id.imageView)
        UserIDfield = findViewById(R.id.UserIDfield)

        list_X = ArrayList<Any>()
        list_Y = ArrayList<Any>()    
        list_Z = ArrayList<Any>()
        list_timeStamp = ArrayList<Any>()
//        Aware.startAWARE(applicationContext)
        Aware.setSetting(this, Aware_Preferences.FREQUENCY_MAGNETOMETER,200000) //that is default
        Magnetometer.setSensorObserver {
            id = it.getAsString(Magnetometer_Provider.Magnetometer_Data.DEVICE_ID)  
            val x = it.getAsDouble(Magnetometer_Provider.Magnetometer_Data.VALUES_0)
            val y = it.getAsDouble(Magnetometer_Provider.Magnetometer_Data.VALUES_1)
            val z = it.getAsDouble(Magnetometer_Provider.Magnetometer_Data.VALUES_2)
            val t = it.getAsDouble(Magnetometer_Provider.Magnetometer_Data.TIMESTAMP)
//            println("x = $x y = $y, z = $z, timestamp = $t")
            list_X.add(x)
            list_Y.add(y)
            list_Z.add(z)
            list_timeStamp.add(t)
        }
//        Glide.with(this).load(R.drawable.gears2).into(imageView)
//        Glide.with(this).asBitmap().load(R.drawable.gears2).into(imageView)

//        Glide.with(this).asGif().load(R.drawable.gears2).into(imageView)
//        imageView.visibility = View.INVISIBLE

    }
    override fun onSensorChanged(event: SensorEvent?) {

        // Acquire magnetometer event data
//        if (mGeomagnetic != null) {
//            Log.d(TAG, "mx : "+mGeomagnetic[0]+" my : "+mGeomagnetic[1]+" mz : "+mGeomagnetic[2]);
//        }
//        else if (event != null) {
//            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
//                if (event != null) {
//                    System.arraycopy(event.values, 0, mGeomagnetic, 0, 3)
//                };
//
//            }
//        }

        // If we have readings from both sensors then
        // use the readings to compute the device's orientation
        // and then update the display.



    }
    fun changeStage(stage_num: Int){
        val sharedPref: SharedPreferences = getSharedPreferences("SharedVal", MODE_PRIVATE)

        val prefEditor = sharedPref.edit()

        try {
//            if (!sharedPref.contains("STAGE")){
                //Stage cariable set to stage_num
            println(stage_num)
                prefEditor.putInt("STAGE", stage_num)
                prefEditor.commit()

//                println("add config file success "+ sharedPref.getInt("STAGE",-1))
//            }
//            else{
//                prefEditor.
//            }
        }
        catch (e: Exception){
//            println("add config file fail")
        }
    }
    override fun onResume() {
        super.onResume()
<<<<<<< Updated upstream
        // Register for sensor updates
//        mSensorManager.registerListener(this, magnetometer,
//            SensorManager.SENSOR_DELAY_NORMAL);
=======

        // Register for sensor updates
        mSensorManager.registerListener(this, magnetometer,
            SensorManager.SENSOR_DELAY_NORMAL);
>>>>>>> Stashed changes

        Aware.startAWARE(applicationContext)
        collecting()
    }

    private fun writeCollectedData() {

        //creating data file for collection in new folder
//        val contextWrapper = ContextWrapper(applicationContext)
//        val path = contextWrapper.data
        val instanceOfList = mutableMapOf<String, Any>()
        val sharedPref = getSharedPreferences("SharedVal", MODE_PRIVATE)
        instanceOfList["DEVICE_ID"] = id
        instanceOfList["stage"] = sharedPref.getInt("STAGE",-1)
        instanceOfList["X"] = list_X
        instanceOfList["Y"] = list_Y
        instanceOfList["Z"] = list_Z
        instanceOfList["time"] = list_timeStamp
        val path = applicationContext.getExternalFilesDir(null)
        val collDataDir = File(path, "collectedData")
        collDataDir.mkdirs()
        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val jsonTutsListPretty: String = gsonPretty.toJson(listOf(instanceOfList))
//        println(collDataDir.toPath().toString())
//        println("Data"+UserID+".json")
//        println(jsonTutsListPretty)
        File(collDataDir,"/Data_"+id+"_"+sharedPref.getInt("STAGE",-1)+"_"+UserID+".json").writeText(jsonTutsListPretty)
        println("printed"+sharedPref.getInt("STAGE",-1))
//        println(File(collDataDir,"Data"+UserID+".json").exists())
//        println(File(collDataDir,"Data"+UserID+".json").path)
    }

    override fun onPause() {
        super.onPause()
<<<<<<< Updated upstream
        // Unregister all sensors
//        mSensorManager.unregisterListener(this);
=======

        // Unregister all sensors
        mSensorManager.unregisterListener(this);
>>>>>>> Stashed changes

        Aware.stopMagnetometer(this)
    }

    private fun collecting(){
        val sharedPref = getSharedPreferences("SharedVal", MODE_PRIVATE)

        Glide.with(this).asBitmap().load(R.drawable.gears2).into(imageView)
        imageView.visibility = View.VISIBLE
        textView2.text = "Click Start button to begin collecting."
        startBtn.setOnClickListener{
            if (UserIDfield.text.length != 0){
                UserIDfield.isEnabled = false
                Glide.with(this).asGif().load(R.drawable.gears2).into(imageView)
                imageView.visibility = View.VISIBLE
                textView2.text = "Collecting..."
                UserID = UserIDfield.text.toString()
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
                    writeCollectedData()
                    println("stage to be removed is : "+sharedPref.getInt("STAGE",-1))
                    println(SET_of_STAGES.toString() + " cccccccccccccc " + sharedPref.getInt("STAGE",-1))
                    SET_of_STAGES = SET_of_STAGES.minusElement(sharedPref.getInt("STAGE",-1))
                    if (!SET_of_STAGES.isEmpty()) {
//                        println("prije  "+SET_of_STAGES)
                        println("medju "+SET_of_STAGES.toString())
                        changeStage(SET_of_STAGES.random())
                        println(SET_of_STAGES.toString() + "bbbbbbbbbbbbbbbbbbbb")
                        startActivity(Intent(this@MainActivity, PhaseActivity::class.java))
                    }
                    else {
                        runOnUiThread(java.lang.Runnable {
                            Glide.with(applicationContext).asBitmap().load(R.drawable.done_icon).into(imageView)
                            //                    imageView.visibility = View.INVISIBLE
                            startBtn.isEnabled = false
                            textView2.text = "All stages are completed and all data needed is collected. Thank you!"
                        })
                    }
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

    override fun onSensorChanged(p0: SensorEvent?) {
        if (con == 1) {
//            if (mGeomagnetic != null) {
//                Log.d(TAG, "mx : "+ mGeomagnetic!![0]+" my : "+ mGeomagnetic!![1]+" mz : "+ mGeomagnetic!![2]);
//            }
//            if (p0 != null) {
//                if (p0.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
//                    if (p0 != null) {
//                        System.arraycopy(p0.values, 0, mGeomagnetic, 0, 3)
//                    };
//
//                }
//            }
//        }
//            if (p0 != null) {
////                mGeomagnetic?.let { System.arraycopy(p0.values, 0, it, 0, 3) }
//                mGeomagnetic.add(p0.values[0])
//            }
            if (p0 != null) {
                println("mx : " + p0.values[1])
            };
            println("mx : x ");
        }
    }
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        if (p0 === magnetometer) {
            when (p1) {
                0 -> {
                    println("Unreliable")
                    con = 0
                }
                1 -> {
                    println("Low Accuracy")
                    con = 0
                }
                2 -> {
                    println("Medium Accuracy")
                    con = 0
                }
                3 -> {
                    println("High Accuracy")
                    con = 1
                }
            }
        }
    }
}


