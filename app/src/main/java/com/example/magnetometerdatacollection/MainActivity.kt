package com.example.magnetometerdatacollection

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable

//import pl.droidsonroids.gif.GifImageView

class MainActivity : AppCompatActivity() {
//    val gifImageView = GifImageView(this)
//    val StatImg : GifImageView = findViewById(R.id.gifImageView)
//    val startBtn: Button = findViewById(R.id.button) as Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val imageView: ImageView = findViewById(R.id.imageView)
//        Glide.with(this).load(R.drawable.gears2).into(imageView)
//        Glide.with(this).asBitmap().load(R.drawable.gears2).into(imageView)
        Glide.with(this).asGif().load(R.drawable.gears2).into(imageView)
        imageView.setVisibility(View.INVISIBLE);
    }

    override fun onResume() {
        super.onResume()
        val startBtn: Button = findViewById(R.id.button) as Button
        val textView2: TextView = findViewById(R.id.textView2)
        startBtn.setOnClickListener{
            val imageView = this.findViewById<ImageView>(R.id.imageView)
            imageView . setVisibility (View.VISIBLE)
            textView2.text = "Collecting..."
        }
    }
}


