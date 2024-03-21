package com.dts.base

import android.app.AlertDialog
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.dts.mpossv.R

class BaseDatosScript(private val vcontext: Context) {
    fun scriptDatabase(database: SQLiteDatabase): Int {
        return try {
            if (scriptTablas(database) == 0) 0 else 1
        } catch (e: SQLiteException) {
            msgbox(e.message)
            0
        }
    }

    private fun scriptTablas(db: SQLiteDatabase): Int {
        var sql: String
        return try {
            sql = "CREATE TABLE [Sucursal] (" +
                    "CODIGO_SUCURSAL INTEGER NOT NULL," +
                    "EMPRESA INTEGER NOT NULL," +
                    "NOMBRE TEXT NOT NULL," +
                    "MODO_INVENTARIO INTEGER NOT NULL," +
                    "PRIMARY KEY ([CODIGO_SUCURSAL])" + ");"
            db.execSQL(sql)
            sql = "CREATE INDEX Sucursal_idx1 ON Sucursal(CODIGO_SUCURSAL)"
            db.execSQL(sql)


            //-------------------------------------------
            sql = "CREATE TABLE [Params] (" +
                    "ID integer NOT NULL," +
                    "dbver INTEGER  NOT NULL," +
                    "param1 TEXT  NOT NULL," +
                    "param2 TEXT  NOT NULL," +
                    "param3 INTEGER  NOT NULL," +  // EntityID
                    "param4 INTEGER  NOT NULL," +  //
                    "lic1 TEXT  NOT NULL," +  //
                    "lic2 INTEGER  NOT NULL," +  // i
                    "PRIMARY KEY ([ID])" + ");"
            db.execSQL(sql)
            sql = "CREATE TABLE [ParamLic] (" +
                    "ID integer NOT NULL," +
                    "param1 TEXT  NOT NULL," +
                    "param2 INTEGER  NOT NULL," +
                    "PRIMARY KEY ([ID])" + ");"
            db.execSQL(sql)
            sql = "CREATE TABLE [Paramsext] (" +
                    "ID INTEGER NOT NULL," +
                    "Nombre TEXT NOT NULL," +
                    "Valor TEXT NOT NULL," +
                    "Tipo TEXT NOT NULL," +
                    "PRIMARY KEY ([ID])" + ");"
            db.execSQL(sql)
            1
        } catch (e: SQLiteException) {
            msgbox(e.message)
            0
        }
    }

    fun scriptData(db: SQLiteDatabase): Int {
        return try {
            db.execSQL("INSERT INTO Params VALUES (1,1,'','',0,0,'',0);")
            1
        } catch (e: SQLiteException) {
            msgbox(e.message)
            0
        }
    }

    private fun msgbox(msg: String?) {
        val dialog = AlertDialog.Builder(vcontext)
        dialog.setTitle(R.string.app_name)
        dialog.setMessage(msg)
        dialog.setNeutralButton("OK") { dialog, which -> }
        dialog.show()
    }
}