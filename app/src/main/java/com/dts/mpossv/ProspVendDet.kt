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
import com.dts.ladapter.LA_ProspVend
import com.dts.webapi.GetProspectoVendedor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException

class ProspVendDet : PBase() {

    var rview: RecyclerView? = null
    var lblsuc: TextView? =null
    var lbltot: TextView? =null
    var pbar: ProgressBar? = null

    var adapter: LA_ProspVend? = null

    val pitems =  ArrayList<clsClasses.clsVendedorProspecto>()

    var codigo:Int=0
    var nombre:String=""

    var idsemana: Int=0;var idanio: Int=0
    var idsemana1: Int=0;var idanio1: Int=0
    var idsemana2: Int=0;var idanio2: Int=0
    var idsemana3: Int=0;var idanio3: Int=0

    var cant1:Int=0;var meta1:Int=0
    var cant2:Int=0;var meta2:Int=0
    var cant3:Int=0;var meta3:Int=0

    var idle:Boolean=false;

    override fun onCreate(savedInstanceState: Bundle?) {

        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_prosp_vend_det)

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

            datosProspectos1()

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
        var pitem: clsClasses.clsVendedorProspecto
        var mv:Int=0
        var av:Int

        try {

            pitems.clear()

            pitem = clsCls.clsVendedorProspecto(1,"Semana actual",0)
            pitem.cant=cant1;pitem.meta=meta1
            pitems.add(pitem)

            pitem = clsCls.clsVendedorProspecto(2,"Semana pasada",0)
            pitem.cant=cant2;pitem.meta=meta2
            pitems.add(pitem)

            pitem = clsCls.clsVendedorProspecto(3,"Semana antepasada",0)
            pitem.cant=cant3;pitem.meta=meta3
            pitems.add(pitem)

            for (pv in pitems) {
                if (pv.cant>mv) mv=pv.cant
            }
            if (mv==0) mv=15

            for (pv in pitems) {
                if(pv.cant>pv.meta) {
                    av=pv.cant-pv.meta
                };else av=0
                pv.bm=drawGraphItem(pv.cant,av,mv)
            }

            adapter = LA_ProspVend(pitems,this)
            rview?.adapter = adapter

            lbltot?.setText("")
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message);lbltot?.setText("")
        }

        pbar?.visibility=View.INVISIBLE;idle=true
    }

    private fun datosProspectos1() {

        try {

            val vendproslist = retrofit!!.CrearServicio(GetProspectoVendedor::class.java, gl!!.urlbase)
            val call = vendproslist.GetProspectoVendedor(gl!!.emp,idanio1,idsemana1)

            call!!.enqueue(object : Callback<List<clsClasses.clsAPIVendedorProspecto?>?> {
                override fun onResponse(call: Call<List<clsClasses.clsAPIVendedorProspecto?>?>,
                                        response: Response<List<clsClasses.clsAPIVendedorProspecto?>?>
                ) {

                    if (response.isSuccessful) {
                        val lista = response.body()

                        if (lista != null && lista.size > 0) {
                            for (litem in lista) {
                                if (litem?.CODIGO_VENDEDOR==codigo) {
                                    cant1 = litem?.CANTIDAD_REALIZADA!!
                                    meta1 = litem?.OBJETIVO_CANTIDAD!!
                                    break
                                }
                            }
                        }

                        datosProspectos2()
                    } else {
                        mostrarError(response, call, object : Any() {}.javaClass.enclosingMethod.name)
                    }
                }

                override fun onFailure(call: Call<List<clsClasses.clsAPIVendedorProspecto?>?>, t: Throwable) {
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

    private fun datosProspectos2() {

        try {

            val vendproslist = retrofit!!.CrearServicio(GetProspectoVendedor::class.java, gl!!.urlbase)
            val call = vendproslist.GetProspectoVendedor(gl!!.emp,idanio2,idsemana2)

            call!!.enqueue(object : Callback<List<clsClasses.clsAPIVendedorProspecto?>?> {
                override fun onResponse(call: Call<List<clsClasses.clsAPIVendedorProspecto?>?>,
                                        response: Response<List<clsClasses.clsAPIVendedorProspecto?>?>
                ) {

                    if (response.isSuccessful) {
                        val lista = response.body()

                        if (lista != null && lista.size > 0) {
                            for (litem in lista) {
                                if (litem?.CODIGO_VENDEDOR==codigo) {
                                    cant2 = litem?.CANTIDAD_REALIZADA!!
                                    meta2 = litem?.OBJETIVO_CANTIDAD!!
                                    break
                                }
                            }
                        }

                        datosProspectos3()
                    } else {
                        mostrarError(response, call, object : Any() {}.javaClass.enclosingMethod.name)
                    }
                }

                override fun onFailure(call: Call<List<clsClasses.clsAPIVendedorProspecto?>?>, t: Throwable) {
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

    private fun datosProspectos3() {

        try {

            val vendproslist = retrofit!!.CrearServicio(GetProspectoVendedor::class.java, gl!!.urlbase)
            val call = vendproslist.GetProspectoVendedor(gl!!.emp,idanio3,idsemana3)

            call!!.enqueue(object : Callback<List<clsClasses.clsAPIVendedorProspecto?>?> {
                override fun onResponse(call: Call<List<clsClasses.clsAPIVendedorProspecto?>?>,
                                        response: Response<List<clsClasses.clsAPIVendedorProspecto?>?>
                ) {

                    if (response.isSuccessful) {
                        val lista = response.body()

                        if (lista != null && lista.size > 0) {
                            for (litem in lista) {
                                if (litem?.CODIGO_VENDEDOR==codigo) {
                                    cant3 = litem?.CANTIDAD_REALIZADA!!
                                    meta3 = litem?.OBJETIVO_CANTIDAD!!
                                    break
                                }
                            }
                        }

                        listItems()
                    } else {
                        mostrarError(response, call, object : Any() {}.javaClass.enclosingMethod.name)
                    }
                }

                override fun onFailure(call: Call<List<clsClasses.clsAPIVendedorProspecto?>?>, t: Throwable) {
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
        try {
            var adate:Long=du!!.actDate

            idsemana=du!!.getweek(adate)
            idanio=du!!.getyear(adate)

            idsemana1=idsemana;idanio1=idanio
            idsemana2=idsemana-1;idanio2=idanio
            idsemana3=idsemana-2;idanio3=idanio

            if (idsemana1==1) {
                idsemana2=52;idanio2=idanio-1
                idsemana3=51;idanio3=idanio-1
            } else if (idsemana==2) {
                idsemana2=idsemana-1;idanio2=idanio
                idsemana3=52;idanio3=idanio-1
            }

            return true
        } catch (e: Exception) {
            idsemana1=idsemana;idanio1=idanio
            idsemana2=idsemana;idanio2=idanio
            idsemana3=idsemana;idanio3=idanio
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message);
            return false
        }
    }

    fun drawGraphItem(nv:Int,av:Int,tv:Int): Bitmap? {
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

            lim=100*(vn-va)/vt;ival=lim.toInt()
            val rectm = Rect(0,0,ival,100)
            canvas.drawRect(rectm, paint)

            if (av>0) {
                lima=100*va/vt;
                lima=lima+lim;ivala=lima.toInt()
                val recta = Rect(ival,0,+ivala,100)
                canvas.drawRect(recta, paintad)
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