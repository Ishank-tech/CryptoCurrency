package com.example.cryptocurrency.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs{
    companion object{
        lateinit var prefs: SharedPreferences
        fun init(context: Context) {
            prefs = context.getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
        }
    }

    fun saveTime(time : String){
        val editor = prefs.edit()
        editor.putString("refreshtime", time)
        editor.apply()
    }

    fun getTime() : String? {
        return prefs.getString("refreshtime", null)
    }
}