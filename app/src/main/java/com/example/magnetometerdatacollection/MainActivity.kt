package com.example.magnetometerdatacollection

import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.aware.Aware
import com.aware.Magnetometer
import com.aware.providers.Magnetometer_Provider
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import java.io.File
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

    var SET_of_STAGES = setOf<Int> (1,2,3,4,5,6)

    var done: Boolean = false
    data class DataCollected (
        val id: Int,
        val username: String,
        val device_model: String,
        val stage: Int,
        val mtx_xyzt: List<List<Any>>
            )
    lateinit var instanceOfList: List<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        changeStage(SET_of_STAGES.random())

        startActivity(Intent(this@MainActivity, PhaseActivity::class.java))

        startBtn = findViewById(R.id.button)
        textView2 = findViewById(R.id.textView2)
        imageView = findViewById(R.id.imageView)
        UserIDfield = findViewById(R.id.UserIDfield)

        instanceOfList = listOf()
        Aware.startAWARE(applicationContext)

        Magnetometer.setSensorObserver {
            val x = it.getAsDouble(Magnetometer_Provider.Magnetometer_Data.VALUES_0)
            val y = it.getAsDouble(Magnetometer_Provider.Magnetometer_Data.VALUES_1)
            val z = it.getAsDouble(Magnetometer_Provider.Magnetometer_Data.VALUES_2)
            val t = it.getAsDouble(Magnetometer_Provider.Magnetometer_Data.TIMESTAMP)
            println("x = $x y = $y, z = $z, timestamp = $t")
            instanceOfList = listOf(x,y,z,t)
        }
//        Glide.with(this).load(R.drawable.gears2).into(imageView)
//        Glide.with(this).asBitmap().load(R.drawable.gears2).into(imageView)

//        Glide.with(this).asGif().load(R.drawable.gears2).into(imageView)
//        imageView.visibility = View.INVISIBLE

    }

    fun changeStage(stage_num: Int){

        val sharedPref = getSharedPreferences("SharedVal", MODE_PRIVATE)
        val prefEditor = sharedPref.edit()

        try {
            if (!sharedPref.contains("STAGE")){
                //Stage cariable set to stage_num
                prefEditor.putInt("STAGE", stage_num)
                prefEditor.commit()

                println("add config file success "+ sharedPref.getInt("STAGE",-1))
            }
        }
        catch (e: Exception){
            println("add config file fail")
        }
    }
    override fun onResume() {
        super.onResume()
        collecting()
    }

    private fun writeCollectedData() {

        //creating data file for collection in new folder
//        val contextWrapper = ContextWrapper(applicationContext)
//        val path = contextWrapper.data
        val path = applicationContext.getExternalFilesDir(null)
        val collDataDir = File(path, "collectedData")
        collDataDir.mkdirs()
        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val jsonTutsListPretty: String = gsonPretty.toJson(listOf(instanceOfList))
        println(collDataDir.toPath().toString())
        println("Data"+UserID+".json")
        println(jsonTutsListPretty)
        File(collDataDir,"/Data"+UserID+".json").writeText(jsonTutsListPretty)
        println(File(collDataDir,"Data"+UserID+".json").exists())
        println(File(collDataDir,"Data"+UserID+".json").path)
    }

    override fun onPause() {
        super.onPause()
        Aware.stopMagnetometer(this)
    }

    fun collecting(){
        val sharedPref = getSharedPreferences("SharedVal", MODE_PRIVATE)
        startBtn.setOnClickListener{
            if (UserIDfield.text.length != 0){
                UserID = UserIDfield.text.toString()
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
                    writeCollectedData()
                    SET_of_STAGES.minusElement(sharedPref.getInt("STAGE",-1))
                    changeStage(SET_of_STAGES.random())
                    startActivity(Intent(this@MainActivity, PhaseActivity::class.java))
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


