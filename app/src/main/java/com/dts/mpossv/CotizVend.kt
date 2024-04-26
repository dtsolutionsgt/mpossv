package com.dts.mpossv

import android.content.Intent
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
import com.dts.classes.RecyclerItemClickListener
import com.dts.classes.extListDlg
import com.dts.ladapter.LA_CotizVend
import com.dts.webapi.GetCotVendedor
import com.dts.webapi.GetSucursalesByEmpresa
import com.dts.webapi.GetVendedorEmpresa
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.Collections
import kotlin.math.sign

class CotizVend : PBase() {

    var rview: RecyclerView? = null
    var lblsuc: TextView? =null
    var lblper: TextView? =null
    var lbltot: TextView? =null
    var pbar: ProgressBar? = null

    var adapter: LA_CotizVend? = null

    val vitems =  ArrayList<clsClasses.clsListaVendedor>()
    val sitems =  ArrayList<clsClasses.clsListaSucursal>()
    val pvitems = ArrayList<clsClasses.clsVendedorCotizacion>()
    val pitems =  ArrayList<clsClasses.clsVendedorCotizacion>()
    val pditems =  ArrayList<clsClasses.clsVendedorCotizacionDatos>()

    var idsucursal=0
    var sortord=0

    var idmes=0;var idanio=0
    var idmes1=0;var idanio1=0
    var idmes2=0;var idanio2=0
    var idmes3=0;var idanio3=0

    var idle=false;

    override fun onCreate(savedInstanceState: Bundle?) {

        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_cotiz_vend)

            super.InitBase(savedInstanceState)

            rview = findViewById(R.id.recview)
            rview?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
            lblsuc = findViewById(R.id.textView16)
            lblper = findViewById(R.id.textView17)
            lbltot = findViewById(R.id.textView18);lbltot?.setText("")
            pbar = findViewById(R.id.progressBar2)

            sortord=0
            inicializaTiempos()

            if (app?.sinInternet()!!) return;

            listaSucursales()
            fillData()
            idle=true;

            setHandlers()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }

    }

    //region Events

    fun doOrder(view: View?) {
        if (idle) showMenuOrder();else toast("Cargando datos, espere . . .");
    }

    fun doSucursal(view: View?) {
        if (idle) showMenuSucursal();else toast("Cargando datos, espere . . .");
    }

    fun doPeriod(view: View?) {
        if (idle) showMenuPeriodo();else toast("Cargando datos, espere . . .");
    }

    fun doExit(view: View?) {
        if (idle) finish();else toast("Cargando datos, espere . . .");
    }

    private fun setHandlers() {
        try {

            rview?.addOnItemTouchListener(
                RecyclerItemClickListener(this, rview!!, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        gl?.gint=pitems.get(position).codigo
                        gl?.gstr=pitems.get(position).nombre
                        gl?.anio=idanio;gl?.mes=idmes

                        if (app?.sinInternet()!!) return;

                        startActivity(Intent(this@CotizVend, CotizVendList::class.java))
                    }
                    override fun onItemLongClick(view: View?, position: Int) {
                        gl?.gint=pitems.get(position).codigo
                        gl?.gstr=pitems.get(position).nombre
                        if (app?.sinInternet()!!) return;

                        startActivity(Intent(this@CotizVend, CotizVendDet::class.java))
                    }
                })
            )

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //endregion

    //region Main

    private fun listItems() {
        var mv=0.0
        var av=0.0

        try {
            when (sortord) {
                0 -> {
                    Collections.sort(pitems, ItemVendNameComparator())}
                1 -> {
                    Collections.sort(pitems, ItemVendCantAscComparator())}
                2 -> {
                    Collections.sort(pitems, ItemVendCantDescComparator())}
                3 -> {
                    Collections.sort(pitems, ItemVendMontoAscComparator())}
                4 -> {
                    Collections.sort(pitems, ItemVendMontoDescComparator())}
            }

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

            lbltot?.setText("Registros: "+pitems.size)
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message);lbltot?.setText("")
        }
    }

    private fun fillData() {
        try {

            pbar?.visibility=View.VISIBLE;idle=false

            pitems.clear()
            adapter = LA_CotizVend(pitems,this)
            rview?.adapter = adapter
            lbltot?.setText("Registros: "+pitems.size)


            listaEmpleados()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
            pbar?.visibility=View.INVISIBLE;idle=true
        }
    }

    private fun listaEmpleados() {
        var item: clsClasses.clsListaVendedor
        var pitem: clsClasses.clsVendedorProspecto

        try {

            val vendlist = retrofit!!.CrearServicio(GetVendedorEmpresa::class.java, gl!!.urlbase)
            val call = vendlist.GetVendedorEmpresa(gl!!.emp)

            call!!.enqueue(object : Callback<List<clsClasses.clsAPIVendedor?>?> {
                override fun onResponse(call: Call<List<clsClasses.clsAPIVendedor?>?>,
                                        response: Response<List<clsClasses.clsAPIVendedor?>?>
                ) {
                    var item: clsClasses.clsAPIVendedor

                    vitems.clear();pvitems.clear();

                    if (response.isSuccessful) {
                        val lista = response.body()

                        if (lista != null && lista.size > 0) {
                            for (litem in lista) {
                                agregaVendedor(litem?.CODIGO_VENDEDOR!!,litem?.NOMBRE!!,litem?.SUCURSAL!!)
                            }
                        }

                        datosCotizaciones()
                        //filtroSucursal()
                    } else {
                        mostrarError(response, call, object : Any() {}.javaClass.enclosingMethod.name)
                    }
                }

                override fun onFailure(call: Call<List<clsClasses.clsAPIVendedor?>?>, t: Throwable) {
                    if (t is SocketTimeoutException) {
                        msgbox("¡Connection Timeout!")
                    } else if (t is ConnectException) {
                        msgbox("¡Problemas de conexión! Inténtelo de nuevo")
                    }
                    cancelarPeticion(call)
                }
            })

        } catch (e: java.lang.Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
            pbar?.visibility=View.INVISIBLE;idle=true
        }
    }

    private fun datosCotizaciones() {
        var ditem: clsClasses.clsVendedorCotizacionDatos
        var mt:Double=0.0;var tc:Double
        var mes:Int=0;var anio:Int=0

        pbar?.visibility=View.VISIBLE
        try {
            pditems.clear()
            var mi:Long= du!!.getBeginMonth(idanio,idmes)
            var mf:Long= du!!.getEndMonth(idanio,idmes)
            val body= clsClasses.clsAPIVendedorCotizacionBody()

            body.empresa= gl?.emp!!
            body.fdel = du!!.fechaapi(mi)
            body.fal = du!!.fechaapif(mf)

            val vendcotizlist = retrofit!!.CrearServicio(GetCotVendedor::class.java, gl!!.urlbase)
            val call = vendcotizlist.getCotizVendedor(body)

            call!!.enqueue(object : Callback<List<clsClasses.clsAPIVendedorCotizacion?>?> {
                override fun onResponse(call: Call<List<clsClasses.clsAPIVendedorCotizacion?>?>,
                                        response: Response<List<clsClasses.clsAPIVendedorCotizacion?>?>
                ) {

                    pditems.clear()

                    if (response.isSuccessful) {
                        val lista = response.body()

                        if (lista != null && lista.size > 0) {
                            for (litem in lista) {

                                mes=litem?.MES!!;anio=litem?.ANIO!!

                                if (anio==idanio && mes==idmes) {
                                    mt = litem?.MONTO!!
                                    tc = litem?.TASA_CAMBIO!!
                                    if (tc!=0.0) mt=mt/tc else mt=0.0

                                    mt=mt+0.0
                                    ditem = clsCls.clsVendedorCotizacionDatos(
                                        litem?.CODIGO_VENDEDOR!!, litem?.CANTIDAD!!, 0, mt)
                                    pditems.add(ditem)
                                }

                            }
                        }

                        llenaDatosCotizaciones()
                        filtroSucursal()

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

    private fun llenaDatosCotizaciones() {
        var cc:Int;var cn:Int;var mt:Double

        try {
            for (pv in pvitems) {
                cn=0;mt=0.0
                cc=pv.codigo;
                for (pd in pditems) {
                    if (pd.codigo==cc) {
                        cn+=pd.cant;mt+=pd.monto
                    }
                }

                pv.cant=cn
                pv.monto=mt
            }



        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
            pbar?.visibility=View.INVISIBLE;idle=true
        }
    }

    private fun filtroSucursal() {
        try {
            pitems.clear()

            for (pv in pvitems) {
                if (idsucursal>0) {
                    if (pv.idsucursal==idsucursal)
                        pitems.add(pv)
                } else {
                    pitems.add(pv)
                }
            }

            listItems()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }

        pbar?.visibility=View.INVISIBLE;idle=true
    }

    private fun listaSucursales() {
        try {

            val suclist = retrofit!!.CrearServicio(GetSucursalesByEmpresa::class.java, gl!!.urlbase)
            val call = suclist.GetSucursalesByEmpresa(gl!!.emp)

            call!!.enqueue(object : Callback<List<clsClasses.clsAPISucursal?>?> {
                override fun onResponse(call: Call<List<clsClasses.clsAPISucursal?>?>,
                                        response: Response<List<clsClasses.clsAPISucursal?>?>
                ) {
                    inicializaSucursales()

                    if (response.isSuccessful) {
                        val lista = response.body()

                        if (lista != null && lista.size > 0) {
                            for (litem in lista) {
                                agregaSucursal(litem?.CODIGO_SUCURSAL!!,litem?.DESCRIPCION+"")
                            }
                            Collections.sort(sitems, ItemSucNameComparator())
                        }
                    } else {
                        inicializaSucursales()
                        mostrarError(response, call, object : Any() {}.javaClass.enclosingMethod.name)
                    }
                }

                override fun onFailure(call: Call<List<clsClasses.clsAPISucursal?>?>, t: Throwable) {
                    if (t is SocketTimeoutException) {
                        msgbox("¡Connection Timeout!")
                    } else if (t is ConnectException) {
                        msgbox("¡Problemas de conexión!Inténtelo de nuevo")
                    }
                    inicializaSucursales()
                    cancelarPeticion(call)
                }
            })

        } catch (e: java.lang.Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
            inicializaSucursales()
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

    fun agregaSucursal(cs: Int, ns: String) {
        try {
            var ditem: clsClasses.clsListaSucursal = clsCls.clsListaSucursal(cs,ns)
            sitems.add(ditem)
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun agregaVendedor(cv:Int,nv:String,cs:Int) {
        try {
            var ditem : clsClasses.clsListaVendedor = clsCls.clsListaVendedor(cv,nv,cs)
            vitems.add(ditem)

            var pitem: clsClasses.clsVendedorCotizacion = clsCls.clsVendedorCotizacion(cv,nv,cs)
            pvitems.add(pitem)
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun inicializaSucursales() {
        try {
            sitems.clear()
            agregaSucursal(0," Todos los sucursales")
            idsucursal=0
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun inicializaTiempos():Boolean {
        try {
            var adate:Long=du!!.actDate

            idmes =du!!.getmonth(adate)
            idanio=du!!.getyear(adate)
            lblper?.setText(du!!.nombremes(idmes)+" "+idanio)

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

    class ItemVendNameComparator : Comparator<clsClasses.clsVendedorCotizacion?> {
        override fun compare(
            left: clsClasses.clsVendedorCotizacion?,
            right: clsClasses.clsVendedorCotizacion?
        ): Int {
            return left!!.nombre.compareTo(right!!.nombre);
        }
    }

    class ItemVendCantAscComparator : Comparator<clsClasses.clsVendedorCotizacion?> {
        override fun compare(
            left: clsClasses.clsVendedorCotizacion?,
            right: clsClasses.clsVendedorCotizacion?
        ): Int {
            var fval:Double= sign(left!!.cant.toDouble()-right!!.cant)
            var ival=fval.toInt()
            if (ival==0) {
                return left!!.nombre.compareTo(right!!.nombre);
            } else {
                return ival
            }
        }
    }

    class ItemVendCantDescComparator : Comparator<clsClasses.clsVendedorCotizacion?> {
        override fun compare(
            left: clsClasses.clsVendedorCotizacion?,
            right: clsClasses.clsVendedorCotizacion?
        ): Int {
            var fval:Double= -sign(left!!.cant.toDouble()-right!!.cant)
            var ival=fval.toInt()
            if (ival==0) {
                return left!!.nombre.compareTo(right!!.nombre);
            } else {
                return ival
            }
        }
    }

    class ItemVendMontoAscComparator : Comparator<clsClasses.clsVendedorCotizacion?> {
        override fun compare(
            left: clsClasses.clsVendedorCotizacion?,
            right: clsClasses.clsVendedorCotizacion?
        ): Int {
            var fval:Double= sign(left!!.monto-right!!.monto)
            var ival=fval.toInt()
            if (ival==0) {
                return left!!.nombre.compareTo(right!!.nombre);
            } else {
                return ival
            }
        }
    }

    class ItemVendMontoDescComparator : Comparator<clsClasses.clsVendedorCotizacion?> {
        override fun compare(
            left: clsClasses.clsVendedorCotizacion?,
            right: clsClasses.clsVendedorCotizacion?
        ): Int {
            var fval:Double= -sign(left!!.monto-right!!.monto)
            var ival=fval.toInt()
            if (ival==0) {
                return left!!.nombre.compareTo(right!!.nombre);
            } else {
                return ival
            }
        }
    }

    class ItemSucNameComparator : Comparator<clsClasses.clsListaSucursal?> {
        override fun compare(
            left: clsClasses.clsListaSucursal?,
            right: clsClasses.clsListaSucursal?
        ): Int {
            return left!!.nombre.compareTo(right!!.nombre);
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

            listdlg.buildDialog(this@CotizVend, "Orden")
            listdlg.setLines(5)
            listdlg.setWidth(700)
            listdlg.setTopRightPosition()

            listdlg.addData(0,"Por nombre")
            listdlg.addData(1,"Ascendente cantidad")
            listdlg.addData(2,"Descendente cantidad")
            listdlg.addData(3,"Ascendente monto")
            listdlg.addData(4,"Descendente monto")

            listdlg.clickListener= Runnable { processMenuOrder() }

            listdlg.setOnLeftClick { v: View? -> listdlg.dismiss() }
            listdlg.show()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun showMenuPeriodo() {
        try {
            val listdlg = extListDlg()

            listdlg.ggl=gl

            listdlg.buildDialog(this@CotizVend, "Periodo")
            listdlg.setLines(3)
            listdlg.setWidth(800)
            listdlg.setTopCenterPosition()

            listdlg.addData(du!!.nombremes(idmes1)+" "+idanio1)
            listdlg.addData(du!!.nombremes(idmes2)+" "+idanio2)
            listdlg.addData(du!!.nombremes(idmes3)+" "+idanio3)

            listdlg.clickListener= Runnable { processMenuPeriodo() }

            listdlg.setOnLeftClick { v: View? -> listdlg.dismiss() }
            listdlg.show()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun showMenuSucursal() {
        try {
            val listdlg = extListDlg()

            listdlg.ggl=gl

            listdlg.buildDialog(this@CotizVend, "Sucursal")
            listdlg.setLines(7)
            listdlg.setWidth(-1)
            listdlg.setTopCenterPosition()

            for (si in sitems) {
                listdlg.addData(si.nombre)
            }

            listdlg.clickListener= Runnable { processMenuSucursal() }

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

    fun processMenuPeriodo() {
        try {
            when (gl!!.dlgClickIndex) {
                0 -> {
                    idmes=idmes1;idanio=idanio1
                }
                1 -> {
                    idmes=idmes2;idanio=idanio2
                }
                2 -> {
                    idmes=idmes3;idanio=idanio3
                }
            }

            lblper?.setText(du!!.nombremes(idmes)+" "+idanio)

            fillData()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun processMenuSucursal() {
        try {
            idsucursal=sitems.get(gl!!.dlgClickIndex).codigo
            lblsuc?.setText(sitems.get(gl!!.dlgClickIndex).nombre)

            filtroSucursal()
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

            //listItems()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    override fun onBackPressed() {
        if (idle) {
            super.onBackPressed()
        } else {
            toast("Cargando datos, espere . . .");
        }
    }

    //endregion


}

