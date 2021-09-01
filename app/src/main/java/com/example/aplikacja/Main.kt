package com.example.aplikacja

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class Main : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonConfig.setOnClickListener {
            configuration()
        }

        buttonControl.setOnClickListener {
            control()
        }

        buttonPreview.setOnClickListener {
            dataPreview()
        }
    }

    fun configuration() {
        val intent = Intent(this, Config::class.java)
        startActivity(intent)
    }

    fun control() {
        val intent = Intent(this, Control::class.java)
        startActivity(intent)
    }

    fun dataPreview() {
        val intent = Intent(this, DataPreview::class.java)
        startActivity(intent)
    }
}