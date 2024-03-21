package com.dts.base

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Patterns
import android.view.Gravity
import android.widget.Toast
import com.dts.base.BaseDatos.Update
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.Currency
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class AppMethods(
    private val cont: Context,
    private val gl: appGlobals,
    private var Con: BaseDatos,
    private var db: SQLiteDatabase
) {
    var flicsup: Long = 0
    var flicbck: Long = 0
    private var ins: BaseDatos.Insert
    private var upd: Update
    var clsCls = clsClasses()

    init {
        ins = Con.Ins
        upd = Con.Upd
    }

    fun reconnect(dbconnection: BaseDatos, database: SQLiteDatabase) {
        Con = dbconnection
        db = database
        ins = Con.Ins
        upd = Con.Upd
    }

    //region Public

    fun getLocalCurrencySymbol(): String {
        val locale = Locale.getDefault()
        val currency = Currency.getInstance(locale)
        return currency.symbol as String
    }

    //endregion

    //region Common

    fun copyToClipboard(text: String?) {
        try {
            val clipboard = cont.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(text, text)
            clipboard.setPrimaryClip(clip)
        } catch (e: Exception) {
            toast(e.message)
        }
    }

    @Throws(IOException::class)
    fun zip(file: String?, zipFile: String?) {
        var origin: BufferedInputStream? = null
        val out = ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile)))
        val BUFFER_SIZE = 6 * 1024
        try {
            val files = arrayOfNulls<String>(1)
            files[0] = file
            val data = ByteArray(BUFFER_SIZE)
            for (i in files.indices) {
                val fi = FileInputStream(files[i])
                origin = BufferedInputStream(fi, BUFFER_SIZE)
                try {
                    val entry = ZipEntry(
                        files[i]!!.substring(files[i]!!.lastIndexOf("/") + 1)
                    )
                    out.putNextEntry(entry)
                    var count: Int
                    while (origin.read(data, 0, BUFFER_SIZE).also { count = it } != -1) {
                        out.write(data, 0, count)
                    }
                } finally {
                    origin.close()
                }
            }
        } finally {
            out.close()
        }
    }

    fun unzip(zipFile: File?, targetDirectory: File?, dname: String?) {
        val DefaultBufferSize = 65536
        try {
            FileInputStream(zipFile).use { fis ->
                BufferedInputStream(fis).use { bis ->
                    ZipInputStream(bis).use { zis ->
                        var ze: ZipEntry
                        var count: Int
                        val buffer = ByteArray(DefaultBufferSize)
                        while (zis.nextEntry.also { ze = it } != null) {
                            //File file = new File(targetDirectory, ze.getName());
                            val file = File(targetDirectory, dname)
                            val dir = if (ze.isDirectory) file else file.parentFile
                            if (!dir.isDirectory && !dir.mkdirs()) throw FileNotFoundException("Failed to ensure directory: " + dir.absolutePath)
                            if (ze.isDirectory) continue
                            FileOutputStream(file).use { fout ->
                                while (zis.read(buffer)
                                        .also { count = it } != -1
                                ) fout.write(buffer, 0, count)
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            //handle exception
        }
    }

    fun invalidEmail(email: CharSequence?): Boolean {
        return !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    //endregion

    //region Private

    protected fun toast(msg: String?) {
        val toast = Toast.makeText(cont, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }

    protected fun toastlong(msg: String?) {
        val toast = Toast.makeText(cont, msg, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }

    //endregion

}