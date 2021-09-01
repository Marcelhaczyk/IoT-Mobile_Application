package com.example.aplikacja

import android.graphics.Color
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Button
import kotlinx.android.synthetic.main.activity_control.*
import org.json.JSONArray
import org.json.JSONObject
import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.io.*


class Control : AppCompatActivity() {

    var coZostaloKlikniete : Int = 0 //1 - Reset One, 3 - Select Color
    var mDefaultColor: Int = 0
    var liczbaPol = 8
    var matrycaLED = arrayOf<Array<Button>>()
    var kolory = arrayOf<Array<Int>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)

        mDefaultColor = ContextCompat.getColor(applicationContext, R.color.colorPrimary)

        buttonResetOne.setOnClickListener {
            coZostaloKlikniete = 1
        }

        buttonResetAll.setOnClickListener {
            clearAll()
            postData()
        }

        buttonSelectColor.setOnClickListener {
            coZostaloKlikniete = 3
            openColorPicker()
        }

        for (i in 0 until liczbaPol) {
            var tab = arrayOf<Button>()
            var kol = arrayOf<Int>()
            for (j in 0 until liczbaPol) {
                var przyciskString: String = "button" + i + j
                var przyciskID = resources.getIdentifier(przyciskString, "id", packageName)
                var przycisk = findViewById<Button>(przyciskID)
                przycisk.setBackgroundColor(Color.WHITE)
                tab += przycisk
                kol += Color.WHITE
            }
            matrycaLED += tab
            kolory += kol
        }

        for(i in 0 until liczbaPol) {
            for(j in 0 until liczbaPol) {
                matrycaLED[i][j].setOnClickListener {
                    if(coZostaloKlikniete == 3) {
                        matrycaLED[i][j].setBackgroundColor(mDefaultColor)
                        kolory[i][j] = mDefaultColor
                        postData()

                    } else if(coZostaloKlikniete == 1) {
                        matrycaLED[i][j].setBackgroundColor(Color.WHITE)
                        kolory[i][j] = Color.WHITE
                        postData()
                    }
                }
            }
        }

    }

    fun clearAll() {
        for(i in 0 until liczbaPol) {
            for(j in 0 until liczbaPol) {
                matrycaLED[i][j].setBackgroundColor(Color.WHITE)
                kolory[i][j] = Color.WHITE
            }
        }
    }

    fun openColorPicker() {
        var colorPicker: AmbilWarnaDialog =
            AmbilWarnaDialog(this, mDefaultColor, object : OnAmbilWarnaListener {
                override fun onCancel(dialog: AmbilWarnaDialog?) {
                }

                override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                    mDefaultColor = color
                }
            })
        colorPicker.show()
    }

    fun postData() {
        val shared = this.getSharedPreferences("com.example.aplikacja.config",0)
        val serv = shared.getString("server", "")
        val port = shared.getString("port","")

        class send() : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg params: Void?): String? {
                try {

                    var url : URL = URL("http://192.168.56.101/reader.php")
                    var connection : HttpURLConnection = url.openConnection() as HttpURLConnection

                    connection.requestMethod = "POST"
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                    connection.doOutput = true

                    var jsonArray: JSONArray = JSONArray()
                    for(i in 0 until liczbaPol) {
                        for(j in 0 until liczbaPol) {
                            val r = kolory[i][j] shr 16 and 0xFF
                            val g = kolory[i][j] shr 8 and 0xFF
                            val b = kolory[i][j] and 0xFF

                            var jsonObject: JSONObject = JSONObject()
                            jsonObject.put("r", r)
                            jsonObject.put("g", g)
                            jsonObject.put("b", b)
                            jsonObject.put("x", i)
                            jsonObject.put("y", j)

                            jsonArray.put(jsonObject)
                        }
                    }

                    var POST_PARAMS: String = "data="+ jsonArray.toString()

                    var osw : OutputStreamWriter = OutputStreamWriter(connection.outputStream)
                    osw.write(POST_PARAMS)
                    osw.flush()
                    osw.close()


                } catch(e: Exception) {
                    Log.d("Something went wrong: ", e.message)
                }

                return null
            }

            override fun onPreExecute() {
                super.onPreExecute()
                // ...
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                // ...
            }
        }
        send().execute().get()
    }
}