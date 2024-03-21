package com.dts.mpossv

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.widget.Toast
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    private var complete = false

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            val mtimer = Handler()
            val mrunner = Runnable { grantPermissions() }
            mtimer.postDelayed(mrunner, 50)
        } catch (e: Exception) {
            toastlong(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    private fun grantPermissions() {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    startApp()
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        1
                    )
                }
            } else {
                startApp()
            }
        } catch (e: Exception) {
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        try {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                startApp()
            } else {
                startApp()
                //Toast.makeText(this, "Permission not granted.", Toast.LENGTH_LONG).show();super.finish();
            }
        } catch (e: Exception) {
        }
    }

    private fun startApp() {
        try {
            try {
                val directory = externalMediaDirs[0]
                directory.mkdirs()
            } catch (e: Exception) {
            }
            val mtimer = Handler()
            val mrunner = Runnable {
                val intent = Intent(this@MainActivity, Session::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
            mtimer.postDelayed(mrunner, 50)

        } catch (e: Exception) {
            toastlong(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun toastlong(msg: String?) {
        val toast = Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    override fun onResume() {
        super.onResume()
        if (complete) finish() else complete = true
    }

}