package com.example.aplikacja

import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.tab1.*
import org.json.JSONArray
import java.net.URL
import org.json.JSONObject
import java.lang.Exception


class DataPreview : AppCompatActivity() {

    var samplingPeriod: Int? = 500
    var maxNoOfSavedPoints: Int? = 1000
    val handler = Handler()
    var count = 0

    var measuresList : ArrayList<DataPreviewMeasure> = ArrayList()
    var pressureChartData : ArrayList<Entry> = ArrayList()
    var temperatureChartData : ArrayList<Entry> = ArrayList()
    var humidityChartData : ArrayList<Entry> = ArrayList()

    lateinit var adapter: DataPreviewKeyValueAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datapreview)


        //Log.d("DataPreview", "OnCreate: Starting")



        //downloadLatestData()


        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        val viewPager: ViewPager = findViewById(R.id.view_pager)
        sectionsPagerAdapter.addTab(Tab1(), "Charts")
        sectionsPagerAdapter.addTab(Tab2(), " Measured Data")
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        loop()
    }


    private fun setupListAdaper() {
        if (!this::adapter.isInitialized) {
            var listViewKeys: ListView = findViewById(R.id.ListViewDataPreview)
            adapter = DataPreviewKeyValueAdapter(
                applicationContext,
                R.layout.tab2_adapter_view_layout,
                measuresList
            )
            listViewKeys.adapter = adapter
        }else{

           // adapter.clear()
           // adapter.addAll(measuresList)
            adapter.notifyDataSetChanged()
        }
    }

    fun loop(){
        val numberOfLoop = 100
        val runnable: Runnable = object : Runnable {
            override fun run() {

                if (count++ < numberOfLoop) { // to liczba pobran danych
                    downloadLatestData()
                    //Toast.makeText(this@DataPreview, count.toString(), Toast.LENGTH_LONG).show()
                    handler.postDelayed(this, 7000)
                }else{
                    Toast.makeText(this@DataPreview, "done 5 times", Toast.LENGTH_LONG).show()
                }
            }
        }
        // trigger first time
        handler.post(runnable)
    }
    fun downloadLatestData() {
        val shared = this.getSharedPreferences("com.example.aplikacja.config",0)
        val serv = shared.getString("server", "")
        val port = shared.getString("port","")


        samplingPeriod = shared.getString("speriod","").toIntOrNull()
        if(samplingPeriod == null || samplingPeriod!! <= 0) samplingPeriod = 500
        maxNoOfSavedPoints = shared.getString("maxNo","").toIntOrNull()
        if(maxNoOfSavedPoints == null || maxNoOfSavedPoints!! <= 0) maxNoOfSavedPoints = 1000

        if(serv.isNotEmpty()) {
            class receive() : AsyncTask<Void, Void, String>() {
                override fun doInBackground(vararg params: Void?): String? {
                    val url ="http://192.168.56.101/alldata.json"
                    return URL(url).readText()
                }

                override fun onPostExecute(result: String?) {
                    super.onPostExecute(result)
                    onDataDownloaded(result)
                }
            }
            receive().execute()
        }
    }

    fun onDataDownloaded(result: String?) {
        measuresList.clear()
        var jsonArray: JSONArray = JSONArray(result)
        for(i in 0 until jsonArray.length()) {
            Log.d("ELEMENT = null ? ->:" , jsonArray.get(i).equals(null).toString())
            var jsonObject: JSONObject = jsonArray.get(i) as JSONObject
            var klucze = jsonObject.keys()
            while(klucze.hasNext()) {
                val klucz = klucze.next() as String

                if(klucz != "Joystick" && klucz !="Acceleration" && klucz != "Magnetic") {
                    if(klucz != "Unit") {
                        measuresList.add(DataPreviewMeasure(klucz, jsonObject.getString(klucz) + " [" + jsonObject.getString("Unit") + "]"))
                    }
                } else {
                    try {
                        Log.d("TEN OBIEKT => ",jsonObject.toString() )
                        Log.d("jego wartosci => ",jsonObject.getString(klucz) )
                        var jA: JSONArray = JSONArray(jsonObject.getString(klucz))
                        for(j in 0 until jA.length()) {
                            var jO : JSONObject = jA.get(j) as JSONObject
                            if(jO == null) {
                                Log.d("ZNALAZLEM NULLA!", jO)
                            }
                            var kle = jO.keys()
                            while(kle.hasNext()) {
                                val kl = kle.next() as String
                                measuresList.add(DataPreviewMeasure(kl, jO.getString(kl) + " [" + jsonObject.getString("Unit") + "]"))
                            }
                            Log.d("Nowy OBIEKT => ",jO.toString() )
                        }
                    } catch(e: Exception) {
                        Log.d("ERROR: ", e.message)
                    }
                }

                if(klucz == "Pressure") {
                    pressureChartData.add(Entry(samplingPeriod!!*pressureChartData.size.toFloat(), jsonObject.getString(klucz).toFloat()))
                } else if(klucz == "Temperature") {
                    temperatureChartData.add(Entry(samplingPeriod!!*temperatureChartData.size.toFloat(), jsonObject.getString(klucz).toFloat()))
                } else if(klucz == "Humidity") {
                    humidityChartData.add(Entry(samplingPeriod!!*humidityChartData.size.toFloat(), jsonObject.getString(klucz).toFloat()))
                }

            }
        }

        showData()
    }

    fun showData() {
        //remove additional elements according to configuration Max number of saved points
        if(pressureChartData.size > maxNoOfSavedPoints!!) {
            pressureChartData.subList(maxNoOfSavedPoints!! - 1, pressureChartData.size-1).clear()
        }
        if(temperatureChartData.size > maxNoOfSavedPoints!!) {
            temperatureChartData.subList(maxNoOfSavedPoints!! - 1, temperatureChartData.size-1).clear()
        }
        if(humidityChartData.size > maxNoOfSavedPoints!!) {
            humidityChartData.subList(maxNoOfSavedPoints!! - 1, humidityChartData.size-1).clear()
        }
        prepareChartPressure(samplingPeriod!!.toFloat(), pressureChartData, "Pressure [hPa]")
        prepareChartTemperature(samplingPeriod!!.toFloat(), temperatureChartData, "Temperature [Celsius Degrees]")
        prepareChartHumidity(samplingPeriod!!.toFloat(), humidityChartData, "Humidity [%]")
        setupListAdaper()
    }

    fun prepareChartPressure(samplingPeriod: Float, pressureChartData: ArrayList<Entry> ,whatHasBeenMeasured: String) {
        textViewChart1Yaxis.setText(whatHasBeenMeasured)

        var lineChart1 : LineChart? = findViewById(R.id.DataPreviewLineChart1)

        lineChart1?.isDragEnabled = true
        lineChart1?.setScaleEnabled(false)

        var set1 : LineDataSet = LineDataSet(pressureChartData, "Data set - " + whatHasBeenMeasured)
        set1.fillAlpha = 100
        set1.setColor(Color.rgb(0,200,0)) //green chart color
        set1.lineWidth = 2f //width of line (must be float)
        set1.valueTextSize = 11f //font size of points

        var leftAxis = lineChart1?.axisLeft
        leftAxis?.axisMinimum = 0f //set minimum value to 0

        lineChart1?.axisRight?.isEnabled = false //remove right side Y axis
        var xAxis = lineChart1?.xAxis
        xAxis?.position = XAxis.XAxisPosition.BOTTOM
        xAxis?.isGranularityEnabled = true
        xAxis?.granularity = samplingPeriod

        var allDataSets: ArrayList<ILineDataSet> = ArrayList()
        allDataSets.add(set1)

        var data : LineData = LineData(allDataSets)
        lineChart1?.data = data
        lineChart1?.notifyDataSetChanged()
        lineChart1?.invalidate()

    }

    fun prepareChartTemperature(samplingPeriod: Float, temperatureChartData: ArrayList<Entry> ,whatHasBeenMeasured: String) {
        textViewChart2Yaxis.setText(whatHasBeenMeasured)
        var lineChart2 : LineChart? = findViewById(R.id.DataPreviewLineChart2)

        lineChart2?.isDragEnabled = true
        lineChart2?.setScaleEnabled(false)


        var set1 : LineDataSet = LineDataSet(temperatureChartData, "Data set - " + whatHasBeenMeasured)
        set1.fillAlpha = 100
        set1.setColor(Color.rgb(12,249,241)) //green chart color
        set1.lineWidth = 2f //width of line (must be float)
        set1.valueTextSize = 11f //font size of points

        var leftAxis = lineChart2?.axisLeft
        leftAxis?.axisMinimum = -50f //set minimum value to 0

        lineChart2?.axisRight?.isEnabled = false //remove right side Y axis
        var xAxis = lineChart2?.xAxis
        xAxis?.position = XAxis.XAxisPosition.BOTTOM
        xAxis?.isGranularityEnabled = true
        xAxis?.granularity = samplingPeriod

        var allDataSets: ArrayList<ILineDataSet> = ArrayList()
        allDataSets.add(set1)

        var data : LineData = LineData(allDataSets)
        lineChart2?.data = data
        lineChart2?.notifyDataSetChanged()
        lineChart2?.invalidate()

    }

    fun prepareChartHumidity(samplingPeriod: Float, humidityChartData: ArrayList<Entry> ,whatHasBeenMeasured: String) {
        textViewChart3Yaxis.setText(whatHasBeenMeasured)
        var lineChart3 : LineChart? = findViewById(R.id.DataPreviewLineChart3)

        lineChart3?.isDragEnabled = true
        lineChart3?.setScaleEnabled(false)


        var set1 : LineDataSet = LineDataSet(humidityChartData, "Data set - " + whatHasBeenMeasured)
        set1.fillAlpha = 100
        set1.setColor(Color.rgb(200,0,0)) //green chart color
        set1.lineWidth = 2f //width of line (must be float)
        set1.valueTextSize = 11f //font size of points

        var leftAxis = lineChart3?.axisLeft
        leftAxis?.axisMinimum = 0f //set minimum value to 0

        lineChart3?.axisRight?.isEnabled = false //remove right side Y axis
        var xAxis = lineChart3?.xAxis
        xAxis?.position = XAxis.XAxisPosition.BOTTOM
        xAxis?.isGranularityEnabled = true
        xAxis?.granularity = samplingPeriod

        var allDataSets: ArrayList<ILineDataSet> = ArrayList()
        allDataSets.add(set1)

        var data : LineData = LineData(allDataSets)
        lineChart3?.data = data
        lineChart3?.notifyDataSetChanged()
        lineChart3?.invalidate()
    }

}