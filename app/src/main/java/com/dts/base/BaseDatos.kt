package com.dts.base

import android.app.AlertDialog
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.dts.mpossv.R

class BaseDatos(context: Context, dbname: String?) : SQLiteOpenHelper(context, dbname, null, 1) {
    @JvmField
	var vDatabase: SQLiteDatabase? = null
    var vcontext: Context
    var Created: Int
    @JvmField
	var Ins: Insert
    @JvmField
	var Upd: Update
    private val DBScript: BaseDatosScript

    init {
        Ins = Insert()
        Upd = Update()
        DBScript = BaseDatosScript(context)
        Created = 0
        vcontext = context
    }

    override fun onCreate(database: SQLiteDatabase) {
        Created = 1
        if (scriptDatabase(database) == 0) {
            Created = -1
            return
        }
        if (scriptData(database) == 0) {
            Created = -1
            return
        }

        //dbCreated();
    }

    private fun scriptDatabase(database: SQLiteDatabase): Int {
        val Rslt: Int
        Rslt = DBScript.scriptDatabase(database)
        return Rslt
    }

    private fun scriptData(database: SQLiteDatabase): Int {
        val Rslt: Int
        Rslt = DBScript.scriptData(database)
        return Rslt
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Toast.makeText(vcontext, "UPDATE DB", Toast.LENGTH_SHORT).show()
    }

    fun OpenDT(pSQL: String?): Cursor {
        return vDatabase!!.rawQuery(pSQL, null)
    }

    // Public class Insert
    inner class Insert {
        private val clFList: MutableList<String> = ArrayList()
        private val clVList: MutableList<String> = ArrayList()
        private var clTable: String

        init {
            clFList.clear()
            clVList.clear()
            clTable = ""
        }

        fun init(TableName: String) {
            clFList.clear()
            clVList.clear()
            clTable = TableName
        }

        fun add(pField: String, pValue: String, pTipo: String) {
            var pValue = pValue
            var SV: String
            try {
                if (pField === "") return
                if (pTipo === "") return
                pValue = pValue.replace("'", "")
                SV = "'$pValue'"
                if (pTipo === "S") SV = "'$pValue'"
                if (pTipo === "N") SV = pValue
                clFList.add(pField)
                clVList.add(SV)
            } catch (e: Exception) {
            }
        }

        fun add(pField: String, pValue: String) {
            var pValue = pValue
            val SV: String
            try {
                if (pField === "") return
                pValue = pValue.replace("'", "")
                SV = "'$pValue'"
                clFList.add(pField)
                clVList.add(SV)
            } catch (e: Exception) {
            }
        }

        fun add(pField: String, pValue: Int) {
            val SV: String
            try {
                if (pField === "") {
                    return
                }
                SV = pValue.toString()
                clFList.add(pField)
                clVList.add(SV)
            } catch (e: Exception) {
            }
        }

        fun add(pField: String, pValue: Double) {
            val SV: String
            try {
                if (pField === "") {
                    return
                }
                SV = pValue.toString()
                clFList.add(pField)
                clVList.add(SV)
            } catch (e: Exception) {
            }
        }

        fun sql(): String {
            var sVal: String
            var S: String
            var SF: String
            var SV: String
            if (clTable === "") {
                return ""
            }
            return if (clFList.isEmpty()) {
                ""
            } else try {
                SV = ""
                SF = ""
                S = "INSERT INTO $clTable ("
                var I = 0
                while (I < clFList.size) {
                    sVal = clFList[I]
                    SF = SF + sVal
                    if (I < clFList.size - 1) {
                        SF = "$SF,"
                    }
                    sVal = clVList[I]
                    SV = SV + sVal
                    if (I < clFList.size - 1) {
                        SV = "$SV,"
                    }
                    I = I + 1
                }
                S = "$S$SF) VALUES ($SV)"
                S
            } catch (e: Exception) {
                ""
            }
        }
    }

    // Public class Update
    inner class Update {
        private val clFList: MutableList<String> = ArrayList()
        private var clTable: String
        private var vWhere: String? = null

        init {
            clFList.clear()
            clTable = ""
        }

        fun init(TableName: String) {
            clFList.clear()
            clTable = "UPDATE $TableName SET "
        }

        fun add(pField: String, pValue: String, pTipo: String) {
            var pValue = pValue
            var SV: String
            try {
                if (pField === "") return
                if (pTipo === "") return
                pValue = pValue.replace("'", "")
                SV = "'$pValue'"
                if (pTipo === "S") SV = "'$pValue'"
                if (pTipo === "N") SV = pValue
                clFList.add("$pField = $SV")
            } catch (e: Exception) {
            }
        }

        fun add(pField: String, pValue: String) {
            var pValue = pValue
            val SV: String
            try {
                if (pField === "") return
                pValue = pValue.replace("'", "")
                SV = "'$pValue'"
                clFList.add("$pField = $SV")
            } catch (e: Exception) {
            }
        }

        fun add(pField: String, pValue: Int) {
            val SV: String
            try {
                if (pField === "") return
                SV = pValue.toString()
                clFList.add("$pField = $SV")
            } catch (e: Exception) {
            }
        }

        fun add(pField: String, pValue: Double) {
            val SV: String
            try {
                if (pField === "") return
                SV = pValue.toString()
                clFList.add("$pField = $SV")
            } catch (e: Exception) {
            }
        }

        fun Where(pWhere: String) {
            vWhere = " WHERE $pWhere"
        }

        fun sql(): String {
            var sVal: String
            var vUpDate: String
            if (clTable === "") return ""
            return if (clFList.isEmpty()) "" else try {
                vUpDate = clTable
                var I = 0
                while (I < clFList.size) {
                    sVal = clFList[I]
                    vUpDate = vUpDate + sVal
                    if (I < clFList.size - 1) {
                        vUpDate = "$vUpDate,"
                    }
                    I = I + 1
                }
                vUpDate = vUpDate + vWhere
                vUpDate
            } catch (e: Exception) {
                ""
            }
        }
    }

    private fun msgbox(msg: String) {
        val dialog = AlertDialog.Builder(vcontext)
        dialog.setTitle(R.string.app_name)
        dialog.setMessage(msg)
        dialog.setNeutralButton("OK") { dialog, which ->
            //Toast.makeText(getApplicationContext(), "Yes button pressed",Toast.LENGTH_SHORT).show();
        }
        dialog.show()
    }

    private fun dbCreated() {
        val dialog = AlertDialog.Builder(vcontext)
        dialog.setTitle(R.string.app_name)
        dialog.setMessage("La base de datos ha sido creada.")
        dialog.setNeutralButton("OK") { dialog, which -> }
        dialog.show()
    }
}