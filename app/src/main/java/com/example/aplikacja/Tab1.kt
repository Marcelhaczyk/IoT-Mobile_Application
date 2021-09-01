package com.example .aplikacja

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class Tab1 : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tab1, container, false)
    }
/*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var lineChart1 : LineChart? = view.findViewById(R.id.DataPreviewLineChart1)

        lineChart1?.isDragEnabled = true
        lineChart1?.setScaleEnabled(false)

        var yValues : ArrayList<Entry> = ArrayList()
        yValues.add(Entry(0.toFloat(),60.toFloat()))
        yValues.add(Entry(1.toFloat(),50.toFloat()))
        yValues.add(Entry(2.toFloat(),70.toFloat()))
        yValues.add(Entry(3.toFloat(),30.toFloat()))
        yValues.add(Entry(4.toFloat(),50.toFloat()))
        yValues.add(Entry(5.toFloat(),60.toFloat()))
        yValues.add(Entry(6.toFloat(),65.toFloat()))

        var set1 : LineDataSet = LineDataSet(yValues, "Zbior danych 1")
        set1.fillAlpha = 100
        set1.setColor(Color.rgb(0,200,0)) //green chart color
        set1.lineWidth = 2f //width of line (must be float)
        set1.valueTextSize = 11f //font size of points

        var leftAxis = lineChart1?.axisLeft
        leftAxis?.axisMinimum = 0f //set minimum value to 0

        lineChart1?.axisRight?.isEnabled = false //remove right side Y axis
        var xAxis = lineChart1?.xAxis
        xAxis?.position = XAxis.XAxisPosition.BOTTOM


        var allDataSets: ArrayList<ILineDataSet> = ArrayList()
        allDataSets.add(set1)

        var data : LineData = LineData(allDataSets)
        lineChart1?.data = data


    }*/
}