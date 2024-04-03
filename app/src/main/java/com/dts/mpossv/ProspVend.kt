package com.dts.mpossv

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses
import com.dts.classes.RecyclerItemClickListener
import com.dts.classes.extListDlg
import com.dts.ladapter.LA_ProspVend
import com.dts.webapi.GetProspectoVendedor
import com.dts.webapi.GetSucursalesByEmpresa
import com.dts.webapi.GetVendedorEmpresa
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.Collections
import kotlin.math.sign

class ProspVend : PBase() {

    var rview: RecyclerView? = null
    var lblsuc: TextView? =null
    var lblper: TextView? =null
    var pbar: ProgressBar? = null

    var adapter: LA_ProspVend? = null

    val vitems =  ArrayList<clsClasses.clsListaVendedor>()
    val sitems =  ArrayList<clsClasses.clsListaSucursal>()
    val pvitems = ArrayList<clsClasses.clsVendedorProspecto>()
    val pitems =  ArrayList<clsClasses.clsVendedorProspecto>()
    val pditems =  ArrayList<clsClasses.clsVendedorProspectoDatos>()

    var idsucursal:Int=0
    var sortord:Int=0

    var idsemana: Int=0;var idanio: Int=0
    var idsemana1: Int=0;var idanio1: Int=0
    var idsemana2: Int=0;var idanio2: Int=0
    var idsemana3: Int=0;var idanio3: Int=0

    var idle:Boolean=false;

    override fun onCreate(savedInstanceState: Bundle?) {

        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_prosp_vend)

            super.InitBase(savedInstanceState)

            rview = findViewById(R.id.recview)
            rview?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
            lblsuc = findViewById(R.id.textView16)
            lblper = findViewById(R.id.textView17)
            pbar = findViewById(R.id.progressBar2)

            sortord=0
            inicializaTiempos()

            if (app?.sinInternet()!!) return;

            listaSucursales()
            fillData()

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

    private fun setHandlers() {
        try {
            rview?.addOnItemTouchListener(RecyclerItemClickListener(this, rview!!,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {  }
                    override fun onItemLongClick(view: View?, position: Int) { }
                })
            )

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //endregion

    //region Main

    private fun listItems() {

        try {
            when (sortord) {
                0 -> {
                    Collections.sort(pitems, ItemVendNameComparator())}
                1 -> {
                    Collections.sort(pitems, ItemVendAscComparator())}
                2 -> {
                    Collections.sort(pitems, ItemVendDescComparator())}
            }

            adapter = LA_ProspVend(pitems,this)
            rview?.adapter = adapter
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    private fun fillData() {
        try {
            pbar?.visibility=View.VISIBLE;idle=false
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

                        datosProspectos()
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

    private fun datosProspectos() {
        var ditem: clsClasses.clsVendedorProspectoDatos

        pbar?.visibility=View.VISIBLE
        try {
            pditems.clear()

            val vendproslist = retrofit!!.CrearServicio(GetProspectoVendedor::class.java, gl!!.urlbase)
            val call = vendproslist.GetProspectoVendedor(gl!!.emp,idanio,idsemana)

            call!!.enqueue(object : Callback<List<clsClasses.clsAPIVendedorProspecto?>?> {
                override fun onResponse(call: Call<List<clsClasses.clsAPIVendedorProspecto?>?>,
                                        response: Response<List<clsClasses.clsAPIVendedorProspecto?>?>
                ) {
                    var item: clsClasses.clsAPIVendedorProspecto

                    pditems.clear()

                    if (response.isSuccessful) {
                        val lista = response.body()

                        if (lista != null && lista.size > 0) {
                            for (litem in lista) {
                                 ditem = clsCls.clsVendedorProspectoDatos(
                                    litem?.CODIGO_VENDEDOR!!,litem?.CANTIDAD_REALIZADA!!,
                                    litem?.OBJETIVO_CANTIDAD!!,litem?.ESTADO_OBJETIVO!!
                                )
                                pditems.add(ditem)
                            }
                        }

                        llenaDatosProspectos()
                        filtroSucursal()
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

    private fun llenaDatosProspectos() {
        var cv:Int;

        try {
            for (pd in pditems) {
                cv=pd.codigo
                for (pv in pvitems) {
                    if (pv.codigo==cv) {
                        pv.meta=pd.meta
                        pv.cant=pd.cant
                        break
                    }
                }
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

            var pitem: clsClasses.clsVendedorProspecto = clsCls.clsVendedorProspecto(cv,nv,cs)
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

    class ItemVendNameComparator : Comparator<clsClasses.clsVendedorProspecto?> {
        override fun compare(
            left: clsClasses.clsVendedorProspecto?,
            right: clsClasses.clsVendedorProspecto?
        ): Int {
            return left!!.nombre.compareTo(right!!.nombre);
        }
    }

    class ItemVendAscComparator : Comparator<clsClasses.clsVendedorProspecto?> {
        override fun compare(
            left: clsClasses.clsVendedorProspecto?,
            right: clsClasses.clsVendedorProspecto?
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

    class ItemVendDescComparator : Comparator<clsClasses.clsVendedorProspecto?> {
        override fun compare(
            left: clsClasses.clsVendedorProspecto?,
            right: clsClasses.clsVendedorProspecto?
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

    class ItemSucNameComparator : Comparator<clsClasses.clsListaSucursal?> {
        override fun compare(
            left: clsClasses.clsListaSucursal?,
            right: clsClasses.clsListaSucursal?
        ): Int {
            return left!!.nombre.compareTo(right!!.nombre);
        }
    }

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

            listdlg.buildDialog(this@ProspVend, "Orden")
            listdlg.setLines(3)
            listdlg.setWidth(500)
            listdlg.setTopRightPosition()

            listdlg.addData("Por nombre")
            listdlg.addData("Ascendente")
            listdlg.addData("Descendente")

            listdlg.clickListener= Runnable { processMenuOrder() }

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

            listdlg.buildDialog(this@ProspVend, "Sucursal")
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

    fun showMenuPeriodo() {
        try {
            val listdlg = extListDlg()

            listdlg.ggl=gl

            listdlg.buildDialog(this@ProspVend, "Periodo")
            listdlg.setLines(3)
            listdlg.setWidth(800)
            listdlg.setTopCenterPosition()

            listdlg.addData("Semana actual")
            listdlg.addData("Semana pasada")
            listdlg.addData("Semana antepasada")

            listdlg.clickListener= Runnable { processMenuPeriodo() }

            listdlg.setOnLeftClick { v: View? -> listdlg.dismiss() }
            listdlg.show()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    private fun processMenuOrder() {
        try {
            sortord=gl!!.dlgClickIndex
            listItems()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    private fun processMenuSucursal() {
        try {
            idsucursal=sitems.get(gl!!.dlgClickIndex).codigo
            lblsuc?.setText(sitems.get(gl!!.dlgClickIndex).nombre)

            filtroSucursal()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    private fun processMenuPeriodo() {
        var pn:String=""

        try {
            when (gl!!.dlgClickIndex) {
                0 -> {
                    idsemana=idsemana1;idanio=idanio1
                    pn="Semana actual"
                }
                1 -> {
                    idsemana=idsemana2;idanio=idanio2
                    pn="Semana pasada"
                }
                2 -> {
                    idsemana=idsemana3;idanio=idanio3
                    pn="Semana antepasada"
                }
            }

            lblper?.setText(pn)

            fillData()
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