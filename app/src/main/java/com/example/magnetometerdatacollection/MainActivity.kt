package com.example.magnetometerdatacollection

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
import androidx.core.view.isVisible
import com.aware.Aware
import com.aware.Aware_Preferences
import com.aware.Magnetometer
import com.aware.providers.Magnetometer_Provider
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.gson.GsonBuilder
import kotlinx.coroutines.NonCancellable.cancel
import kotlinx.coroutines.delay
import java.io.File
import java.io.FileInputStream
import java.util.*
import kotlin.concurrent.schedule
import kotlin.properties.Delegates


@Suppress("UNREACHABLE_CODE")
class MainActivity : AppCompatActivity(), SensorEventListener {
    // Sensors & SensorManager
    lateinit var magnetometer: Sensor
    lateinit var mSensorManager: SensorManager
    // Storage for Sensor readings
    lateinit var mGeomagnetic:  ArrayList<Float>

    var con by Delegates.notNull<Int>()
    lateinit var startBtn: Button
    lateinit var  textView2: TextView
    lateinit var imageView: ImageView
    lateinit var UserIDfield: EditText
    lateinit var folderField: EditText

    lateinit var deviceID: String
    lateinit var UserID: String


//    var SET_of_STAGES = setOf<Int> (1,2,3,4,5,6)
//    var Firebase_location = "files/"

    var SET_of_STAGES = setOf<Int> (5,6)  //two_stages_only ALL ON/ ALL OFF//////
    var Firebase_location = "files_for_only_two_stages/"
    var SoS_size = SET_of_STAGES.size

    var done: Boolean = false
//    data class DataCollected (
//        val id: Int,
//        val username: String,
//        val device_model: String,
//        val stage: Int,
//        val mtx_xyzt: List<List<Any>>
//            )
//    lateinit var instanceOfList: MutableMap<String,Any>
    lateinit var id: String
    lateinit var list_X: ArrayList<Any>
    lateinit var list_Y: ArrayList<Any>
    lateinit var list_Z: ArrayList<Any>
    lateinit var new_list_X: ArrayList<Any>
    lateinit var new_list_Y: ArrayList<Any>
    lateinit var new_list_Z: ArrayList<Any>
    lateinit var new_b_list_X: ArrayList<Any>
    lateinit var new_b_list_Y: ArrayList<Any>
    lateinit var new_b_list_Z: ArrayList<Any>
    lateinit var new_b_list_T: ArrayList<Any>
    lateinit var con_list: ArrayList<Any>
    lateinit var list_timeStamp: ArrayList<Any>
    lateinit var phoneDir: String
    lateinit var ListOfFiles: ArrayList<String>
    var sent: Int = 0

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

        startActivity(Intent(this@MainActivity, PhaseActivity::class.java))

//        SensorManager.SENSOR_DELAY_FASTEST
//        SensorManager.SENSOR_DELAY_NORMAL

        // Get a reference to the SensorManager
        mSensorManager = applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // Get a reference to the magnetometer
        magnetometer = mSensorManager!!
            .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED)
        id = magnetometer.id.toString()
        // Exit unless sensor are available
        con = 0

        startBtn = findViewById(R.id.button)
        textView2 = findViewById(R.id.textView2)
        imageView = findViewById(R.id.imageView)
        UserIDfield = findViewById(R.id.UserIDfield)
        folderField = findViewById(R.id.folderField)

        init_lists_2_empty()
        ListOfFiles = ArrayList<String>()
        sent = 0

//        Aware.setSetting(this, Aware_Preferences.FREQUENCY_MAGNETOMETER,200000) //that is default
//        Magnetometer.setSensorObserver {
//            id = it.getAsString(Magnetometer_Provider.Magnetometer_Data.DEVICE_ID)
//            val x = it.getAsDouble(Magnetometer_Provider.Magnetometer_Data.VALUES_0)
//            val y = it.getAsDouble(Magnetometer_Provider.Magnetometer_Data.VALUES_1)
//            val z = it.getAsDouble(Magnetometer_Provider.Magnetometer_Data.VALUES_2)
//            val t = it.getAsDouble(Magnetometer_Provider.Magnetometer_Data.TIMESTAMP)
////            println("x = $x y = $y, z = $z, timestamp = $t")
//            list_X.add(x)
//            list_Y.add(y)
//            list_Z.add(z)
//            list_timeStamp.add(t)
//        }
//        Glide.with(this).load(R.drawable.gears2).into(imageView)
//        Glide.with(this).asBitmap().load(R.drawable.gears2).into(imageView)

//        Glide.with(this).asGif().load(R.drawable.gears2).into(imageView)
//        imageView.visibility = View.INVISIBLE
        collecting()
    }
    private fun haveConnection(): Boolean {
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw      = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
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
//        if (SET_of_STAGES.size == ListOfFiles.size)
//        {
//            Glide.with(applicationContext).asBitmap().load(R.drawable.done_icon).into(imageView)
//            imageView.visibility = View.VISIBLE
//            textView2.text = "Data collected!"
//        }
//        else {
//            Glide.with(this).asBitmap().load(R.drawable.gears2).into(imageView)
//            imageView.visibility = View.VISIBLE
//            textView2.text = "Click Start button to begin collecting."
//        }
        // Register for sensor updates
//        mSensorManager.registerListener(this, magnetometer,
//            SensorManager.SENSOR_DELAY_NORMAL);

//        Aware.startAWARE(applicationContext)
//        collecting()
    }

    private fun writeCollectedData() {

        //creating data file for collection in new folder
        val instanceOfList = mutableMapOf<String, Any>()
        val sharedPref = getSharedPreferences("SharedVal", MODE_PRIVATE)
        instanceOfList["stage"] = sharedPref.getInt("STAGE",-1)
//        instanceOfList["X_AWARE"] = list_X
//        instanceOfList["Y_AWARE"] = list_Y
//        instanceOfList["Z_AWARE"] = list_Z
//        instanceOfList["time_AWARE"] = list_timeStamp

        id = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        instanceOfList["DEVICE_ID"] = id

        instanceOfList["X_UnCal"] = new_list_X
        instanceOfList["Y_UnCal"] = new_list_Y
        instanceOfList["Z_UnCal"] = new_list_Z
        instanceOfList["X_Bias"] = new_b_list_X
        instanceOfList["Y_Bias"] = new_b_list_Y
        instanceOfList["Z_Bias"] = new_b_list_Z
        instanceOfList["time_UnCal"] = new_b_list_T
        instanceOfList["Accuracy"] = con_list
        init_lists_2_empty()
        val path = applicationContext.getExternalFilesDir(null)
        val collDataDir = File(path, "collectedData")
        collDataDir.mkdirs()
        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val jsonTutsListPretty: String = gsonPretty.toJson(listOf(instanceOfList))
        println(collDataDir.toPath().toString())
        File(collDataDir,"/Data_"+id+"_"+sharedPref.getInt("STAGE",-1)+"_"+UserID+".json").writeText(jsonTutsListPretty)
        //Firebase upload file------------------------------------------------------------
        phoneDir = collDataDir.path
        ListOfFiles.add("Data_"+id+"_"+sharedPref.getInt("STAGE",-1)+"_"+UserID+".json")
        println("printed"+sharedPref.getInt("STAGE",-1))
    }
    fun init_lists_2_empty(){
        list_X = ArrayList<Any>()
        list_Y = ArrayList<Any>()
        list_Z = ArrayList<Any>()
        list_timeStamp = ArrayList<Any>()
        new_list_X = ArrayList<Any>()
        new_list_Y = ArrayList<Any>()
        new_list_Z = ArrayList<Any>()
        new_b_list_X = ArrayList<Any>()
        new_b_list_Y = ArrayList<Any>()
        new_b_list_Z = ArrayList<Any>()
        new_b_list_T = ArrayList<Any>()
        con_list = ArrayList<Any>()
    }
    override fun onPause() {
        super.onPause()

//        Aware.stopMagnetometer(this)
        // Unregister all sensors
//        mSensorManager.unregisterListener(this);
    }

    override fun onDestroy() {
        super.onDestroy()
        mSensorManager.unregisterListener(this);
    }

    override fun onStart() {
        super.onStart()

    }

    private fun collecting(){
        val sharedPref = getSharedPreferences("SharedVal", MODE_PRIVATE)

        Glide.with(this).asBitmap().load(R.drawable.gears2).into(imageView)
        imageView.visibility = ImageView.VISIBLE
        textView2.text = "Click Start button to begin collecting."
        startBtn.setOnClickListener{
            if (UserIDfield.text.length != 0){
                UserIDfield.isEnabled = false
                folderField.isEnabled = false
                Glide.with(this).asGif().load(R.drawable.gears2).into(imageView)
                imageView.visibility = View.VISIBLE
                textView2.text = "Collecting..."
                UserID = UserIDfield.text.toString()
                startBtn.isEnabled = false;
                //connect to magnetometer through AWARE and normal
                //-------------------------------------------------
//                Aware.startMagnetometer(this)
                mSensorManager.registerListener(this, magnetometer,
                    SensorManager.SENSOR_DELAY_FASTEST);
                //-------------------------------------------------
                Timer().schedule(60000) {
                    //-------------------------------------------------
//                    Aware.stopMagnetometer(applicationContext)
                    mSensorManager.unregisterListener(this@MainActivity);
                    println("Readings stopped!")
                    //-------------------------------------------------
                    runOnUiThread(java.lang.Runnable {
                        Glide.with(applicationContext).asBitmap().load(R.drawable.gears2).into(imageView)
                        textView2.text = "Click Start button to begin collecting."

//                        textView2.text = "Data collected!"
                        startBtn.isEnabled = true;
                        done = true
                    })
                    writeCollectedData()
                    SET_of_STAGES = SET_of_STAGES.minusElement(sharedPref.getInt("STAGE",-1))
                    if (!SET_of_STAGES.isEmpty()) {
//                    if (SET_of_STAGES.size<5) {
                        changeStage(SET_of_STAGES.random())

                        startActivity(Intent(this@MainActivity, PhaseActivity::class.java))
                    }
                    else {
                        runOnUiThread(java.lang.Runnable {
                            Glide.with(applicationContext).asGif().load(R.drawable.hug).into(imageView)
//                            if (!haveConnection()){
//                            }
//                            while (!haveConnection()){
//                                Timer().schedule(10000) {}
//                            }
                            //--------------------------------------------------------------------------------------
//                            val builder = AlertDialog.Builder(applicationContext)
//                            builder.setMessage(resources.getString(R.string.messageConnection))
//                            builder.setTitle(resources.getString(R.string.internetConn))
//                            builder.setCancelable(false)
//                            builder.setPositiveButton(
//                                resources.getString(R.string.done),
//                                        DialogInterface.OnClickListener { dialog, id ->
//                                    if (haveConnection()) {
//                                        dialog.dismiss()
//                                    }
//                                    else{
//                                        Toast.makeText(applicationContext, "Please connect phone to the internet!", Toast.LENGTH_LONG).show()
//                                    }
//                                })
//
//                            val alert = builder.create()
//                            alert.show()
                            //--------------------------------------------------------------------------------------
                            startBtn.isVisible = false
                            textView2.text = "Sending files..."+" Please connect to the internet so we can send files to server!"
                            while (ListOfFiles.isNotEmpty()) {
                                val elem = ListOfFiles.first()
                                sendFileToFirebase(elem as String)
                            }
                            Timer().schedule(10000) {}
                        })
//                        runOnUiThread(java.lang.Runnable {
//                        Glide.with(applicationContext).asBitmap().load(R.drawable.done_icon).into(imageView)
//                        textView2.text = "All stages are completed and all data needed is collected. Thank you!"})
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

//        if (con == 1) {
            if (p0 != null) {
                new_list_X.add(p0.values[0])
                new_list_Y.add(p0.values[1])
                new_list_Z.add(p0.values[2])
                new_b_list_X.add(p0.values[3])
                new_b_list_Y.add(p0.values[4])
                new_b_list_Z.add(p0.values[5])

                new_b_list_T.add(p0.timestamp)
                con_list.add(con)
                println("mx   : " + p0.values[0])
                println("my   : " + p0.values[1])
                println("mz   : " + p0.values[2])
                println("mx_b : " + p0.values[3])
                println("my_b : " + p0.values[4])
                println("mz_b : " + p0.values[5])
                println("con_ : " + con)
            }
//        }
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
    fun sendFileToFirebase (FileName:String){
        println("Početak slanja")
        val storageRef: StorageReference = Firebase.storage.reference.child(
            (if(folderField.text.equals("")) Firebase_location else
                         folderField.text.toString()+"/")+FileName)
        val stream = FileInputStream(File(phoneDir+"/"+FileName))
        val uploadTask = storageRef.putStream(stream)
        println("Završetak slanja")
        uploadTask.addOnSuccessListener {
            sent += 1
            Log.e("Firebase", "File Upload passed")
            println("Uspješno slanje")
//            Toast.makeText(this, sent , Toast.LENGTH_SHORT).show()
            if (sent == SoS_size){
                Glide.with(applicationContext).asBitmap().load(R.drawable.done_icon).into(imageView)
                textView2.text = "All stages are completed and all data needed is collected. Thank you!"
            }
        }.addOnFailureListener {
            Log.e("Firebase", "File Upload fail")
            println("Neuspješno slanje")
        }
        ListOfFiles.remove(FileName)

    }
}



