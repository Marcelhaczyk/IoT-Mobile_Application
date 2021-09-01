package com.example.aplikacja

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class DataPreviewKeyValueAdapter(context: Context, resource: Int, objects: ArrayList<DataPreviewMeasure> ) : ArrayAdapter<DataPreviewMeasure>(context, resource, objects) {
    var mContext: Context = context
    var mResource: Int = resource

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var returnedView = convertView
        val name = getItem(position).measureName
        val value = getItem(position).measureValue

        var inflater : LayoutInflater = LayoutInflater.from(mContext)
        returnedView = inflater.inflate(mResource, parent, false)

        var tvName : TextView = returnedView.findViewById(R.id.textViewDataPreviewName)
        var tvValue : TextView = returnedView.findViewById(R.id.textViewDataPreviewValue)

        tvName.setText(name)
        tvValue.setText(value)

        return returnedView
    }
}