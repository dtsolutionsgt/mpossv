package com.dts.base

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.widget.Toast
import com.dts.mpossv.R
import java.io.File
import java.text.DecimalFormat
import java.util.Locale

class MiscUtils {

    var color: ColorDef

    private var cont: Context
    private var gl: appGlobals? = null
    private var thnumformat: DecimalFormat
    private var thdecformat: DecimalFormat
    private var plainnumformat: DecimalFormat
    private var decformat: DecimalFormat
    private var ffrmdec1: DecimalFormat
    private var ffrmdec2: DecimalFormat
    private var ffrmdec3: DecimalFormat
    private var ffrmdec4: DecimalFormat
    private var slang = "EN"

    constructor(context: Context, global: appGlobals?) {
        cont = context
        gl = global
        color = ColorDef(cont)
        thnumformat = DecimalFormat("#,###")
        thdecformat = DecimalFormat("#,###.##")
        decformat = DecimalFormat("#0.00")
        plainnumformat = DecimalFormat("#0")
        ffrmdec1 = DecimalFormat("###0.##")
        ffrmdec2 = DecimalFormat("#,##0.##")
        ffrmdec3 = DecimalFormat("#,##0.00")
        ffrmdec4 = DecimalFormat("#,##0.0")
    }

    constructor(context: Context) {
        cont = context
        color = ColorDef(cont)
        thnumformat = DecimalFormat("#,###")
        thdecformat = DecimalFormat("#,###.##")
        decformat = DecimalFormat("#0.00")
        plainnumformat = DecimalFormat("#0")
        ffrmdec1 = DecimalFormat("#,##0.#")
        ffrmdec2 = DecimalFormat("#,##0.##")
        ffrmdec3 = DecimalFormat("#,##0.00")
        ffrmdec4 = DecimalFormat("#,##0.0")
    }

    fun decfrm(`val`: Double): String {
        return decformat.format(`val`)
    }

    fun frmintth(`val`: Double): String {
        return thnumformat.format(`val`)
    }

    fun frmdblth(`val`: Double): String {
        return thdecformat.format(`val`)
    }

    fun frmintnum(`val`: Double): String {
        return plainnumformat.format(`val`)
    }

    fun frmonedec(`val`: Double): String {
        return ffrmdec4.format(`val`)
    }

    fun frmdecno(`val`: Double): String {
        //return ffrmdec2.format(val);
        return ffrmdec1.format(`val`)
    }

    fun frmmonto(`val`: Double): String {
        return ffrmdec3.format(`val`)
    }

    fun frmdecno2(`val`: Double): String {
        return ffrmdec2.format(`val`)
    }

    fun frmmin(min: Int): String {
        var ss = "" + min
        if (min < 10) ss = "0$min"
        return ss
    }

    fun CInt(s: String): Int {
        return s.toInt()
    }

    fun CDbl(s: String): Double {
        return s.toDouble()
    }

    fun CStr(v: Int): String {
        return v.toString()
    }

    fun CStr(v: Double): String {
        return v.toString()
    }

    fun emptystr(s: String?): Boolean {
        return if (s == null || s.isEmpty()) {
            true
        } else {
            false
        }
    }

    fun setlang() {
        slang = Locale.getDefault().language
        slang = slang.substring(0, 2)
    }

    fun lstr(s1: String, s2: String): String {
        return if (slang.equals("ES", ignoreCase = true)) {
            s2
        } else {
            s1
        }
    }

    fun msgbox(msg: String?) {
        if (msg == null || msg.isEmpty()) {
            return
        }
        val dialog = AlertDialog.Builder(cont)
        dialog.setTitle(R.string.app_name)
        dialog.setMessage(msg)
        //dialog.setIcon(R.drawable.info48);
        dialog.setNeutralButton("OK") { dialog, which ->
            //Toast.makeText(getApplicationContext(), "Yes button pressed",Toast.LENGTH_SHORT).show();
        }
        dialog.show()
    }

    fun msgbox(v: Int) {
        val dialog = AlertDialog.Builder(cont)
        dialog.setTitle(R.string.app_name)
        dialog.setMessage(v.toString())
        dialog.setNeutralButton("OK") { dialog, which -> }
        dialog.show()
    }

    fun msgask(dialogid: Int, msg: String?) {
        if (msg == null || msg.isEmpty()) return
        gl!!.dialogid = dialogid
        val dialog = AlertDialog.Builder(cont)
        dialog.setTitle(R.string.app_name)
        dialog.setMessage(msg)
        dialog.setPositiveButton("Yes") { dialog, which ->
            if (gl!!.dialogr != null) {
                gl!!.dialogr!!.run()
            }
        }
        dialog.setNegativeButton("No") { dialog, which -> }
        dialog.show()
    }

    fun toast(msg: String?) {
        Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show()
    }

    fun toast(msg: Double) {
        Toast.makeText(cont, "" + msg, Toast.LENGTH_SHORT).show()
    }

    fun intdiv(`val`: Double, div: Double): Int {
        var `val` = `val`
        if (div == 0.0) return 0
        `val` = `val` / div
        return `val`.toInt()
    }

    fun trunc(`val`: Double, div: Double): Int {
        var `val` = `val`
        if (div == 0.0) return 0
        `val` = `val` / div
        `val` = Math.floor(`val`)
        return `val`.toInt()
    }

    fun truncup(cc: Double): Int {
        val cct: Int
        val cnt: Int
        cct = Math.floor(cc).toInt()
        cnt = if (cct.toDouble() != cc) (cct + 1) else cc.toInt()
        return cnt
    }

    fun roundup10(cc: Double): Int {
        var cc = cc
        val cct: Int
        var cnt: Int
        cc = cc / 10
        cct = Math.floor(cc).toInt()
        cnt = if (cct.toDouble() != cc) (cct + 1) else cc.toInt()
        cnt = cnt * 10
        return cnt
    }

    inner class ColorDef(private val cont: Context) {
        var neonyellow: Int
        var neongreen: Int
        var neonorange: Int
        var neonpink: Int
        var neonblue: Int

        init {
            neonyellow = Color.rgb(243, 243, 21)
            neongreen = Color.rgb(193, 253, 51)
            neonorange = Color.rgb(255, 153, 51)
            neonpink = Color.rgb(252, 90, 184)
            neonblue = Color.rgb(13, 213, 252)
        }
    }

    fun fileexists(fname: String?): Boolean {
        return try {
            val file = File(fname)
            file.exists()
        } catch (e: Exception) {
            false
        }
    }

    fun toPounds(w: Double): String {
        var w = w
        val v1: Double
        var v2: Double
        w = w * 2.205
        v1 = Math.floor(w)
        v2 = (w - v1) * 1000 / 28.35
        v2 = Math.round(v2).toDouble()
        return frmintnum(v1) + " lb " + frmintnum(v2) + " oz"
    }

    fun toFeet(h: Double): String {
        var v1: Double
        var v2: Double
        v1 = h / 30.5
        v1 = Math.floor(v1)
        v2 = h - v1 * 30.5
        v2 = v2 / 2.54
        v2 = Math.round(v2).toDouble()
        return frmintnum(v1) + "' " + frmintnum(v2) + " ''"
    }
}