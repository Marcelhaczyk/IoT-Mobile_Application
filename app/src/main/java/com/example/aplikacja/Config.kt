package com.example.aplikacja

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_config.*

class Config : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        //retrieve from internal memory of the app server addres and port
        val shared = this.getSharedPreferences("com.example.aplikacja.config",0)
        editTextConfigServerAdress.setText(shared.getString("server", ""))
        editTextConfigServerPort.setText(shared.getString("port",""))
        editTextConfigDataPeriod.setText(shared.getString("speriod",""))
        editTextConfigDataMaxNo.setText(shared.getString("maxNo",""))

        buttonConfigAccept.setOnClickListener {
            accept()
        }
    }

    fun accept() {
        val shared = this.getSharedPreferences("com.example.aplikacja.config",0)
        val editor = shared!!.edit()
        editor.putString("server", editTextConfigServerAdress.getText().toString())
        editor.putString("port", editTextConfigServerPort.getText().toString())
        editor.putString("speriod", editTextConfigDataPeriod.getText().toString())
        editor.putString("maxNo", editTextConfigDataMaxNo.getText().toString())
        editor.apply()

        //close this window (return to the previous)
        finish()
    }
}
