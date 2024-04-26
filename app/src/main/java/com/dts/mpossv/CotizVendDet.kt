package com.dts.mpossv

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses
import com.dts.ladapter.LA_CotizVend
import com.dts.ladapter.LA_ProspVend
import com.dts.webapi.GetCotVendedor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException

class CotizVendDet : PBase() {

    var rview: RecyclerView? = null
    var lblsuc: TextView? =null
    var lbltot: TextView? =null
    var pbar: ProgressBar? = null

    var adapter: LA_CotizVend? = null

    val pitems =  ArrayList<clsClasses.clsVendedorCotizacion>()

    var codigo:Int=0
    var nombre:String=""

    //var idmes:  Int=0;var idanio: Int=0
    var idmes1: Int=0;var idanio1: Int=0
    var idmes2: Int=0;var idanio2: Int=0
    var idmes3: Int=0;var idanio3: Int=0

    var cant1:Int=0;var monto1:Double=0.0
    var cant2:Int=0;var monto2:Double=0.0
    var cant3:Int=0;var monto3:Double=0.0

    var idle:Boolean=false;

    override fun onCreate(savedInstanceState: Bundle?) {

        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_cotiz_vend_det)

            super.InitBase(savedInstanceState)

            rview = findViewById(R.id.recview)
            rview?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
            lblsuc = findViewById(R.id.textView16);
            lbltot = findViewById(R.id.textView18);lbltot?.setText("")
            pbar = findViewById(R.id.progressBar2)

            codigo=gl?.gint!!
            nombre=gl?.gstr!!;lblsuc?.setText(nombre)

            inicializaTiempos()

            pbar?.visibility= View.VISIBLE;
            idle=false

            datosCotizaciones1()
            listItems()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }

    }

    //region Events

    fun doExit(view: View?) {
        if (idle) finish();else toast("Cargando datos, espere . . .");
    }

    //endregion

    //region Main

    private fun listItems() {
        var pitem: clsClasses.clsVendedorCotizacion
        var mv=0.0;var av=0.0

        try {

            pitems.clear()

            pitem = clsCls.clsVendedorCotizacion(1,du!!.nombremes(idmes1)+" "+idanio1,0)
            pitem.cant=cant1;pitem.monto=monto1
            pitems.add(pitem)

            pitem = clsCls.clsVendedorCotizacion(2,du!!.nombremes(idmes2)+" "+idanio3,0)
            pitem.cant=cant2;pitem.monto=monto2
            pitems.add(pitem)

            pitem = clsCls.clsVendedorCotizacion(3,du!!.nombremes(idmes3)+" "+idanio3,0)
            pitem.cant=cant3;pitem.monto=monto3
            pitems.add(pitem)

            for (pv in pitems) {
                if (pv.monto>mv) mv=pv.monto
            }
            if (mv==0.0) mv=1.0

            for (pv in pitems) {
                if (pv.monto==mv) av=0.01 else av=0.0
                pv.bm=drawGraphItem2(pv.monto,av,mv)
            }

            adapter = LA_CotizVend(pitems,this)
            rview?.adapter = adapter

            lbltot?.setText("")
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message);lbltot?.setText("")
        }

        pbar?.visibility=View.INVISIBLE;idle=true
    }

    private fun datosCotizaciones1() {
        var mt=0.0;var tc=0.0
        var smt=0.0;var stc=0.0

        pbar?.visibility=View.VISIBLE
        try {

            var mi:Long= du!!.getBeginMonth(idanio1,idmes1)
            var mf:Long= du!!.getEndMonth(idanio1,idmes1)
            val body= clsClasses.clsAPIVendedorCotizacionBody()

            body.empresa= gl?.emp!!
            body.vendedor=codigo
            body.fdel = du!!.fechaapi(mi)
            body.fal = du!!.fechaapif(mf)

            val vendcotizlist = retrofit!!.CrearServicio(GetCotVendedor::class.java, gl!!.urlbase)
            val call = vendcotizlist.getCotizVendedor(body)

            call!!.enqueue(object : Callback<List<clsClasses.clsAPIVendedorCotizacion?>?> {
                override fun onResponse(call: Call<List<clsClasses.clsAPIVendedorCotizacion?>?>,
                                        response: Response<List<clsClasses.clsAPIVendedorCotizacion?>?>
                ) {

                    if (response.isSuccessful) {
                        val lista = response.body()

                        if (lista != null && lista.size > 0) {
                            for (litem in lista) {

                                mt = litem?.MONTO!!;tc = litem?.TASA_CAMBIO!!
                                if (tc!=0.0) mt=mt/tc else mt=0.0
                                smt+=mt;stc+=litem?.CANTIDAD!!
                            }
                        }

                        cant1=stc.toInt();monto1=smt

                        datosCotizaciones2()
                    } else {
                        mostrarError(response, call, object : Any() {}.javaClass.enclosingMethod.name)
                    }
                }

                override fun onFailure(call: Call<List<clsClasses.clsAPIVendedorCotizacion?>?>, t: Throwable) {
                    if (t is SocketTimeoutException) {
                        msgbox("¡Connection Timeout!")
                    } else if (t is ConnectException) {
                        msgbox("¡Problemas de conexión! Inténtelo de nuevo")
                    }
                    cancelarPeticion(call)
                }
            })

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
            pbar?.visibility=View.INVISIBLE;idle=true
        }
    }

    private fun datosCotizaciones2() {
        var mt=0.0;var tc=0.0
        var smt=0.0;var stc=0.0

        pbar?.visibility=View.VISIBLE
        try {

            var mi:Long= du!!.getBeginMonth(idanio2,idmes2)
            var mf:Long= du!!.getEndMonth(idanio2,idmes2)
            val body= clsClasses.clsAPIVendedorCotizacionBody()

            body.empresa= gl?.emp!!
            body.vendedor=codigo
            body.fdel = du!!.fechaapi(mi)
            body.fal = du!!.fechaapif(mf)

            val vendcotizlist = retrofit!!.CrearServicio(GetCotVendedor::class.java, gl!!.urlbase)
            val call = vendcotizlist.getCotizVendedor(body)

            call!!.enqueue(object : Callback<List<clsClasses.clsAPIVendedorCotizacion?>?> {
                override fun onResponse(call: Call<List<clsClasses.clsAPIVendedorCotizacion?>?>,
                                        response: Response<List<clsClasses.clsAPIVendedorCotizacion?>?>
                ) {

                    if (response.isSuccessful) {
                        val lista = response.body()

                        if (lista != null && lista.size > 0) {
                            for (litem in lista) {

                                mt = litem?.MONTO!!;tc = litem?.TASA_CAMBIO!!
                                if (tc!=0.0) mt=mt/tc else mt=0.0
                                smt+=mt;stc+=litem?.CANTIDAD!!
                            }
                        }

                        cant2=stc.toInt();monto2=smt

                        datosCotizaciones3()
                    } else {
                        mostrarError(response, call, object : Any() {}.javaClass.enclosingMethod.name)
                    }
                }

                override fun onFailure(call: Call<List<clsClasses.clsAPIVendedorCotizacion?>?>, t: Throwable) {
                    if (t is SocketTimeoutException) {
                        msgbox("¡Connection Timeout!")
                    } else if (t is ConnectException) {
                        msgbox("¡Problemas de conexión! Inténtelo de nuevo")
                    }
                    cancelarPeticion(call)
                }
            })

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
            pbar?.visibility=View.INVISIBLE;idle=true
        }
    }

    private fun datosCotizaciones3() {
        var mt=0.0;var tc=0.0
        var smt=0.0;var stc=0.0

        pbar?.visibility=View.VISIBLE
        try {

            var mi:Long= du!!.getBeginMonth(idanio3,idmes3)
            var mf:Long= du!!.getEndMonth(idanio3,idmes3)
            val body= clsClasses.clsAPIVendedorCotizacionBody()

            body.empresa= gl?.emp!!
            body.vendedor=codigo
            body.fdel = du!!.fechaapi(mi)
            body.fal = du!!.fechaapif(mf)

            val vendcotizlist = retrofit!!.CrearServicio(GetCotVendedor::class.java, gl!!.urlbase)
            val call = vendcotizlist.getCotizVendedor(body)

            call!!.enqueue(object : Callback<List<clsClasses.clsAPIVendedorCotizacion?>?> {
                override fun onResponse(call: Call<List<clsClasses.clsAPIVendedorCotizacion?>?>,
                                        response: Response<List<clsClasses.clsAPIVendedorCotizacion?>?>
                ) {

                    if (response.isSuccessful) {
                        val lista = response.body()

                        if (lista != null && lista.size > 0) {
                            for (litem in lista) {
                                mt = litem?.MONTO!!;tc = litem?.TASA_CAMBIO!!
                                if (tc!=0.0) mt=mt/tc else mt=0.0
                                smt+=mt;stc+=litem?.CANTIDAD!!
                            }
                        }

                        cant3=stc.toInt();monto3=smt
                        listItems()
                    } else {
                        mostrarError(response, call, object : Any() {}.javaClass.enclosingMethod.name)
                    }
                }

                override fun onFailure(call: Call<List<clsClasses.clsAPIVendedorCotizacion?>?>, t: Throwable) {
                    if (t is SocketTimeoutException) {
                        msgbox("¡Connection Timeout!")
                    } else if (t is ConnectException) {
                        msgbox("¡Problemas de conexión! Inténtelo de nuevo")
                    }
                    cancelarPeticion(call)
                }
            })

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
            pbar?.visibility=View.INVISIBLE;idle=true
        }
    }

    //endregion

    //region WebAPI Common

    private fun cancelarPeticion(call: Call<*>) {
        pbar?.visibility=View.INVISIBLE
        idle=true
        call.cancel()
    }

    private fun mostrarError(response: Response<*>, call: Call<*>, metodo: String) {
        val errorResponse: String = setError(response)
        msgbox(metodo+"\n"+errorResponse)
        cancelarPeticion(call)
    }

    fun setError(response: Response<*>): String {
        return try {
            response.code().toString() + " - " + response.toString()
        } catch (e: java.lang.Exception) {
            " Desconocido "
        }
    }

    //endregion

    //region Aux

    fun inicializaTiempos():Boolean {
        var idmes =0
        var idanio=0

        try {
            var adate:Long=du!!.actDate

            idmes =du!!.getmonth(adate)
            idanio=du!!.getyear(adate)

            idmes1=idmes;idanio1=idanio
            idmes2=idmes-1;idanio2=idanio
            idmes3=idmes-2;idanio3=idanio

            if (idmes1==1) {
                idmes2=12;idanio2=idanio-1
                idmes3=11;idanio3=idanio-1
            } else if (idmes1==2) {
                idmes2=1;idanio2=idanio
                idmes3=12;idanio3=idanio-1
            }

            return true
        } catch (e: Exception) {
            idmes1=idmes;idanio1=idanio
            idmes2=idmes;idanio2=idanio
            idmes3=idmes;idanio3=idanio
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message);
            return false
        }
    }

    fun drawGraphItem2(nv:Double, av:Double, tv:Double): Bitmap? {
        try {
            var bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
            var lim: Double;var lima: Double
            var vn:Double=nv*1.0;var va:Double=av*1.0;var vt:Double=tv*1.0
            var ival:Int;var ivala:Int

            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.parseColor("#FDFFFF"))

            val paint = Paint();paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#9CD0F4");paint.isAntiAlias = true
            val paintad = Paint();paintad.style = Paint.Style.FILL
            paintad.color = Color.parseColor("#020EBC");paintad.isAntiAlias = true

            lim=100*(vn)/vt;ival=lim.toInt()
            val rectm = Rect(0,0,ival,100)

            if (av>0) {
                canvas.drawRect(rectm, paintad)
            } else {
                canvas.drawRect(rectm, paint)
            }

            return bitmap
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
            return null
        }
    }

    //endregion

    //region Dialogs


    //endregion

    //region Activity Events

    override fun onBackPressed() {
        if (idle) {
            super.onBackPressed()
        } else {
            toast("Cargando datos, espere . . .");
        }
    }

    //endregion

}