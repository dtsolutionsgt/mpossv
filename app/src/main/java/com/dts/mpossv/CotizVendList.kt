package com.dts.mpossv

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses
import com.dts.classes.extListDlg
import com.dts.ladapter.LA_CotizVend
import com.dts.ladapter.LA_CotizVendList
import com.dts.webapi.GetCotVendedor
import com.dts.webapi.GetCotVendedorDet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Collections
import kotlin.math.sign

class CotizVendList : PBase() {

    var rview: RecyclerView? = null
    var lbluser: TextView? =null
    var lblsuc: TextView? =null
    var lblper: TextView? =null
    var lbltot: TextView? =null
    var pbar: ProgressBar? = null

    var adapter: LA_CotizVendList? = null

    val pitems = ArrayList<clsClasses.clsVendedorCotizacionDoc>()
    val pvitems = ArrayList<clsClasses.clsVendedorCotizacionDoc>()

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    var codigo=0;var idanio=0;var idmes=0;var sortord=0

    var nombre=""

    var idle:Boolean=false;

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_cotiz_vend_list)

            super.InitBase(savedInstanceState)

            rview = findViewById(R.id.recview)
            rview?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
            lbluser = findViewById(R.id.textView2)
            lblsuc = findViewById(R.id.textView16)
            lblper = findViewById(R.id.textView17)
            lbltot = findViewById(R.id.textView18);lbltot?.setText("")
            pbar = findViewById(R.id.progressBar2)

            codigo=gl?.gint!!
            nombre=gl?.gstr!!;lbluser?.setText(nombre)
            idanio=gl?.anio!!;
            idmes=gl?.mes!!

            fillData()

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //region Events

    fun doOrder(view: View?) {
        if (idle) showMenuOrder();else toast("Cargando datos, espere . . .");
    }

    fun doSucursal(view: View?) {
        //if (idle) showMenuSucursal();else toast("Cargando datos, espere . . .");
    }

    fun doPeriod(view: View?) {
        //if (idle) showMenuPeriodo();else toast("Cargando datos, espere . . .");
    }

    fun doHistory(view: View?) {
        var ii=0
        ii++

        if (app?.sinInternet()!!) return;

        if (idle) {
            startActivity(Intent(this@CotizVendList, CotizVendDet::class.java))
        } else {
            toast("Cargando datos, espere . . .")
        };
    }

    fun doExit(view: View?) {
        if (idle) finish();else toast("Cargando datos, espere . . .");
    }

    //endregion

    //region Main

    private fun listItems() {
        try {
            pbar?.visibility=View.VISIBLE;idle=false
            adapter = LA_CotizVendList(pitems,this)
            rview?.adapter = adapter

            when (sortord) {
                0 -> {
                    Collections.sort(pitems, ItemVendNameComparator())}
                1 -> {
                    Collections.sort(pitems, ItemVendFechaAscComparator())}
                2 -> {
                    Collections.sort(pitems, ItemVendFechaDescComparator())}
                3 -> {
                    Collections.sort(pitems, ItemVendMontoAscComparator())}
                4 -> {
                    Collections.sort(pitems, ItemVendMontoDescComparator())}
                5 -> {
                    Collections.sort(pitems, ItemVendEstadoAscComparator())}
                6 -> {
                    Collections.sort(pitems, ItemVendEstadoDescComparator())}
            }


            lbltot?.setText("Registros: "+pitems.size)
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message);lbltot?.setText("")
        }

        pbar?.visibility=View.INVISIBLE;idle=true
    }

    private fun fillData() {
        try {
            pbar?.visibility=View.VISIBLE;idle=false
            datosCotizaciones()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
            pbar?.visibility=View.INVISIBLE;idle=true
        }
    }

    private fun datosCotizaciones() {
        var citem : clsClasses.clsVendedorCotizacionDoc
        var ff=0L

        try {

            var mi:Long= du!!.getBeginMonth(idanio,idmes)
            var mf:Long= du!!.getEndMonth(idanio,idmes)
            val body= clsClasses.clsAPIVendedorCotizacionBody()

            body.empresa= gl?.emp!!
            body.vendedor=codigo
            body.fdel = du!!.fechaapi(mi)
            body.fal = du!!.fechaapif(mf)

            val vendcotizdocs = retrofit!!.CrearServicio(GetCotVendedorDet::class.java, gl!!.urlbase)
            val call = vendcotizdocs.getCotizVendedorDocs(body)

            call!!.enqueue(object : Callback<List<clsClasses.clsAPIVendedorCotizacionDet?>?> {
                override fun onResponse(call: Call<List<clsClasses.clsAPIVendedorCotizacionDet?>?>,
                                        response: Response<List<clsClasses.clsAPIVendedorCotizacionDet?>?>
                ) {

                    pitems.clear();pvitems.clear()

                    if (response.isSuccessful) {
                        val lista = response.body()

                        if (lista != null && lista.size > 0) {
                            for (litem in lista) {

                                ff=convFecha(litem!!.FECHA_AGR)

                                citem = clsCls.clsVendedorCotizacionDoc()

                                citem.codigo = ""
                                citem.nombre = litem!!.LINEA
                                citem.sestado = "pend"
                                citem.estado = 0
                                citem.monto = litem!!.MONTO
                                citem.moneda = litem!!.MONEDA
                                citem.tasa = litem!!.TASA_CAMBIO
                                citem.monto_convertido = litem!!.MONTO_CONVERTIDO
                                citem.fecha = ff
                                citem.sfecha = du!!.sfechalocal(citem.fecha)

                                pitems.add(citem);pvitems.add(citem)
                            }
                        }

                        listItems()
                    } else {
                        mostrarError(response, call, object : Any() {}.javaClass.enclosingMethod.name)
                    }
                }

                override fun onFailure(call: Call<List<clsClasses.clsAPIVendedorCotizacionDet?>?>, t: Throwable) {
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
        //toast("Cancelado")
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

    fun convFecha(sf:String):Long {
        try {
            val dateTime = LocalDateTime.parse(sf, formatter)
            val y=dateTime.year
            val m=dateTime.month.value
            val d=dateTime.dayOfMonth

            val fc:Long = du?.cfecha(y,m,d)!!
            return fc
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message);return 0
        }
    }

    class ItemVendNameComparator : Comparator<clsClasses.clsVendedorCotizacionDoc?> {
        override fun compare(
            left: clsClasses.clsVendedorCotizacionDoc?,
            right: clsClasses.clsVendedorCotizacionDoc?
        ): Int {
            return left!!.nombre.compareTo(right!!.nombre);
        }
    }

    class ItemVendFechaAscComparator : Comparator<clsClasses.clsVendedorCotizacionDoc?> {
        override fun compare(
            left: clsClasses.clsVendedorCotizacionDoc?,
            right: clsClasses.clsVendedorCotizacionDoc?
        ): Int {
            var fval:Double= sign(left!!.fecha.toDouble()-right!!.fecha.toDouble())
            var ival=fval.toInt()
            if (ival==0) {
                return left!!.nombre.compareTo(right!!.nombre);
            } else {
                return ival
            }
        }
    }

    class ItemVendFechaDescComparator : Comparator<clsClasses.clsVendedorCotizacionDoc?> {
        override fun compare(
            left: clsClasses.clsVendedorCotizacionDoc?,
            right: clsClasses.clsVendedorCotizacionDoc?
        ): Int {
            var fval:Double= -sign(left!!.fecha.toDouble()-right!!.fecha.toDouble())
            var ival=fval.toInt()
            if (ival==0) {
                return left!!.nombre.compareTo(right!!.nombre);
            } else {
                return ival
            }
        }
    }

    class ItemVendMontoAscComparator : Comparator<clsClasses.clsVendedorCotizacionDoc?> {
        override fun compare(
            left: clsClasses.clsVendedorCotizacionDoc?,
            right: clsClasses.clsVendedorCotizacionDoc?
        ): Int {
            var fval:Double= sign(left!!.monto_convertido-right!!.monto_convertido)
            var ival=fval.toInt()
            if (ival==0) {
                return left!!.nombre.compareTo(right!!.nombre);
            } else {
                return ival
            }
        }
    }

    class ItemVendMontoDescComparator : Comparator<clsClasses.clsVendedorCotizacionDoc?> {
        override fun compare(
            left: clsClasses.clsVendedorCotizacionDoc?,
            right: clsClasses.clsVendedorCotizacionDoc?
        ): Int {
            var fval:Double= -sign(left!!.monto_convertido-right!!.monto_convertido)
            var ival=fval.toInt()
            if (ival==0) {
                return left!!.nombre.compareTo(right!!.nombre);
            } else {
                return ival
            }
        }
    }

    class ItemVendEstadoAscComparator : Comparator<clsClasses.clsVendedorCotizacionDoc?> {
        override fun compare(
            left: clsClasses.clsVendedorCotizacionDoc?,
            right: clsClasses.clsVendedorCotizacionDoc?
        ): Int {
            var fval:Double= sign(left!!.estado.toDouble()-right!!.estado.toDouble())
            var ival=fval.toInt()
            if (ival==0) {
                return left!!.nombre.compareTo(right!!.nombre);
            } else {
                return ival
            }
        }
    }

    class ItemVendEstadoDescComparator : Comparator<clsClasses.clsVendedorCotizacionDoc?> {
        override fun compare(
            left: clsClasses.clsVendedorCotizacionDoc?,
            right: clsClasses.clsVendedorCotizacionDoc?
        ): Int {
            var fval:Double= -sign(left!!.estado.toDouble()-right!!.estado.toDouble())
            var ival=fval.toInt()
            if (ival==0) {
                return left!!.nombre.compareTo(right!!.nombre);
            } else {
                return ival
            }
        }
    }

    //endregion

    //region Dialogs

    fun dialogswitch() {
        //msgask(3,"Are you sure ?");
        try {
            when (gl?.dialogid) {
                0 -> {}
                1 -> {}
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun showMenuOrder() {
        try {
            val listdlg = extListDlg()

            listdlg.ggl=gl

            listdlg.buildDialog(this@CotizVendList, "Orden")
            listdlg.setLines(7)
            listdlg.setWidth(700)
            listdlg.setTopRightPosition()

            listdlg.addData(0,"Por nombre")
            listdlg.addData(1,"Ascendente fecha")
            listdlg.addData(2,"Descendente fecha")
            listdlg.addData(3,"Ascendente monto")
            listdlg.addData(4,"Descendente monto")
            listdlg.addData(5,"Ascendente estado")
            listdlg.addData(6,"Descendente estado")

            listdlg.clickListener= Runnable { processMenuOrder() }

            listdlg.setOnLeftClick { v: View? -> listdlg.dismiss() }
            listdlg.show()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun processMenuOrder() {
        try {
            sortord=gl!!.dlgClickIndex
            listItems()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

    //region Activity Events

    override fun onResume() {
        try {
            super.onResume()
            gl?.dialogr = Runnable { dialogswitch() }

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

}