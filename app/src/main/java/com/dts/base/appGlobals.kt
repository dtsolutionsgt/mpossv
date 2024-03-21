package com.dts.base

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast

class appGlobals : Application() {
   	var context: Context? = null

  	var licid = 0
    var it_fecha: Long = 0
    var curr: String=""
    var urlbase: String=""

    var dialogr: Runnable? = null
    var dialogid = 0


    override fun onCreate() {
        super.onCreate()
    }

    fun saveInstance(savedInstanceState: Bundle) {
        try {
            savedInstanceState.putInt("licid", licid)
            savedInstanceState.putString("curr", curr)
            savedInstanceState.putString(" urlbase", urlbase)
        } catch (e: Exception) {
        }
    }

    fun restoreInstance(savedInstanceState: Bundle) {
        try {
            licid = savedInstanceState.getInt("licid")
            curr = savedInstanceState.getString("curr").toString()
            urlbase = savedInstanceState.getString("urlbase").toString()
        } catch (e: Exception) {
        }
    }

    private fun toastlong(msg: String) {
        val toast = Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}