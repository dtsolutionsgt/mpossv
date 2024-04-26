package com.dts.base

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast

class appGlobals : Application() {
   	var context: Context? = null

  	var licid=0
    var curr="";var urlbase="";var gstr=""
    var emp=0;var gint=0;var anio=0;var mes=0

    var dialogr: Runnable? = null
    var dialogid = 0

    var dlgClickIndex=-1;var dlgClickCod=-1

    override fun onCreate() {
        super.onCreate()
    }

    fun saveInstance(savedInstanceState: Bundle) {
        try {
            savedInstanceState.putInt("emp", emp)
            savedInstanceState.putInt("licid", licid)
            savedInstanceState.putInt("dlgClickIndex", dlgClickIndex)
            savedInstanceState.putInt("dlgClickCod", dlgClickCod)
            savedInstanceState.putInt("gint", gint)
            savedInstanceState.putInt("anio", anio)
            savedInstanceState.putInt("mes", mes)

            savedInstanceState.putString("curr", curr)
            savedInstanceState.putString("urlbase", urlbase)
            savedInstanceState.putString("gstr", gstr)
        } catch (e: Exception) {
        }
    }

    fun restoreInstance(savedInstanceState: Bundle) {
        try {
            emp = savedInstanceState.getInt("emp")
            licid = savedInstanceState.getInt("licid")
            dlgClickIndex = savedInstanceState.getInt("dlgClickIndex")
            dlgClickCod = savedInstanceState.getInt("dlgClickCod")
            gint = savedInstanceState.getInt("gint")
            anio = savedInstanceState.getInt("anio")
            mes = savedInstanceState.getInt("mes")

            curr = savedInstanceState.getString("curr").toString()
            urlbase = savedInstanceState.getString("urlbase").toString()
            gstr = savedInstanceState.getString("gstr").toString()

        } catch (e: Exception) {
        }
    }

    private fun toastlong(msg: String) {
        val toast = Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}