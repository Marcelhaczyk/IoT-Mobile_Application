package com.example.aplikacja

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class SectionsPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    val tabsList : ArrayList<Fragment> = ArrayList()
    val tabsNamesList : ArrayList<String> = ArrayList()

    fun addTab(f: Fragment, title: String) {
        tabsList.add(f)
        tabsNamesList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabsNamesList[position]
    }

    override fun getItem(position: Int): Fragment {
        return tabsList[position]
    }

    override fun getCount(): Int {
        return tabsList.size
    }

}