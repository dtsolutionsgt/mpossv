package com.dts.mpossv

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsSucursalId
import com.dts.base.d_sucursalestado
import com.dts.ladapter.LA_SucEstado
import com.dts.webapi.GetVentaSucursalesByIds
import com.dts.webapi.SucursalesByID
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException

class Sucursales : PBase() {

    var rview: RecyclerView? = null
    var lblpend: TextView? =null
    var lblabr: TextView? =null
    var lblcerr: TextView? =null
    var lblventa: TextView? =null
    var lblcant: TextView? =null
    var pbar: ProgressBar? = null

    var adapter: LA_SucEstado? = null
    val items = ArrayList<d_sucursalestado>()
    var ids: MutableList<Int> = java.util.ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_sucursales)

            super.InitBase(savedInstanceState)

            rview = findViewById(R.id.recview)
            rview?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

            lblpend = findViewById(R.id.textView10)
            lblabr = findViewById(R.id.textView11)
            lblcerr = findViewById(R.id.textView12)
            lblventa = findViewById(R.id.textView)
            lblcant = findViewById(R.id.textView13)
            pbar = findViewById(R.id.progressBar2)

            llenaDatos()

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //region Events

    //endregion

    //region Main

    fun listItems() {
        var pend: Int=0;var abr: Int=0;var cer: Int=0;var cnt: Int=0
        var tot: Double=0.0

        try {
            adapter = LA_SucEstado(items,this, gl?.curr ?:"")
            rview?.adapter = adapter

            for (i in items) {
                when (i.estado) {
                   -1 -> {pend++}
                    0 -> {abr++}
                    1 -> {cer++}
                }

                tot+=i.totalVentas
                cnt+=i.cantVentas
            }

            lblpend?.setText("Pendiente: "+pend)
            lblabr?.setText("Abierto: "+abr)
            lblcerr?.setText("Cerrado: "+cer)
            lblventa?.setText("Ventas: "+gl?.curr+mu?.frmintth(tot))
            lblcant?.setText("Facturas: "+mu?.frmintth(cnt+0.0))

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }

        pbar?.visibility=View.INVISIBLE
     }

    private fun llenaDatos() {
        try {
            ids.clear()
            ids.add(102)
            ids.add(152)

            pbar?.visibility=View.VISIBLE

            listaSucursales()
        } catch (e: java.lang.Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }

    }

    private fun listaSucursales() {
        try {
            val sucid = retrofit!!.CrearServicio(SucursalesByID::class.java, gl!!.urlbase)
            val call = sucid.getSucursalId(ids)

            call!!.enqueue(object : Callback<List<clsSucursalId?>?> {
                override fun onResponse( call: Call<List<clsSucursalId?>?>,
                    response: Response<List<clsSucursalId?>?> ) {
                    var item: clsClasses.clsSucursalEstado

                    items.clear()
                    if (response.isSuccessful) {
                        val lista = response.body()

                        if (lista != null && lista.size > 0) {
                            for (litem in lista) {

                                item = clsCls.clsSucursalEstado()

                                item.idsucursal= litem?.CODIGO_SUCURSAL!!
                                item.nombre=litem?.DESCRIPCION+""
                                item.estado=-1
                                item.estadoDesc=estadoTexto(item.estado)
                                item.totalCierre=0.0
                                item.diferCierre=0.0
                                item.totalVentas=0.0
                                item.cantVentas=0

                                agregaSucursal(item)

                            }
                        }

                        valoresVenta()
                    } else {
                        mostrarError(response, call, object : Any() {}.javaClass.enclosingMethod.name)
                    }
                }

                override fun onFailure(call: Call<List<clsSucursalId?>?>, t: Throwable) {
                    if (t is SocketTimeoutException) {
                        msgbox("¡Connection Timeout!")
                    } else if (t is ConnectException) {
                        msgbox("¡Problemas de conexión!Inténtelo de nuevo")
                    }
                    cancelarPeticion(call)
                }
            })

        } catch (e: java.lang.Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun valoresVenta() {
        try {
            val sucventa = retrofit!!.CrearServicio(GetVentaSucursalesByIds::class.java, gl!!.urlbase)
            val call = sucventa.GetVentaSucursalesByIds(ids,"2024-01-18","2024-01-18")

            call!!.enqueue(object : Callback<List<clsClasses.clsSucursalVenta?>?> {
                override fun onResponse(call: Call<List<clsClasses.clsSucursalVenta?>?>,
                                        response: Response<List<clsClasses.clsSucursalVenta?>?> ) {
                    var item: clsClasses.clsSucursalEstado

                    if (response.isSuccessful) {
                        val lista = response.body()

                        if (lista != null && lista.size > 0) {
                            for (venta in lista) {
                                if (venta != null) {
                                    asignaVentas(venta)
                                }
                            }
                        }

                        valoresEstados();

                    } else {
                        mostrarError(response, call, object : Any() {}.javaClass.enclosingMethod.name)
                    }
                }

                override fun onFailure(call: Call<List<clsClasses.clsSucursalVenta?>?>, t: Throwable) {
                    if (t is SocketTimeoutException) {
                        msgbox("¡Connection Timeout!")
                    } else if (t is ConnectException) {
                        msgbox("¡Problemas de conexión!Inténtelo de nuevo")
                    }
                    cancelarPeticion(call)
                }
            })

        } catch (e: java.lang.Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun valoresEstados() {
        try {

            for (item in items) {

            }
            listItems()

        } catch (e: java.lang.Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

    //region WebAPI Common

    private fun cancelarPeticion(call: Call<*>) {
        call.cancel()
        pbar?.visibility=View.VISIBLE
        toast("Cancelado")
    }

    private fun mostrarError(response: Response<*>, call: Call<*>, metodo: String) {
        val errorResponse: String = setError(response)
        msgbox(
            """
            $metodo
            $errorResponse
            """.trimIndent()
        )
        cancelarPeticion(call)
    }

    fun setError(response: Response<*>): String {
        return try {
            response.code().toString() + " - " + response.message()
        } catch (e: java.lang.Exception) {
            " Desconocido "
        }
    }

    //endregion

    //region Aux

    fun asignaVentas(venta: clsClasses.clsSucursalVenta) {
        try {
            for (item in items) {
                if (item.idsucursal==venta?.SUCURSAL) {
                    item.totalVentas=venta?.MONTO!!
                    item.cantVentas=venta?.CANT!!
                    return
                }
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun agregaSucursal(item: clsClasses.clsSucursalEstado) {
        var ditem: d_sucursalestado
        try {
            ditem=d_sucursalestado(
                item.idsucursal,
                item.nombre,
                item.estado,
                item.estadoDesc,
                item.totalCierre,
                item.diferCierre,
                item.totalVentas,
                item.cantVentas
            )

            items.add(ditem)
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun estadoTexto(idest: Int) : String {
        var nom:String=""

        when (idest) {
            -1 -> {nom="Pendiente"}
            0 -> {nom="Abierto"}
            1 -> {nom="Cerrado"}
        }
        return nom
    }

    //endregion

    //region Dialogs


    //endregion

    //region Activity Events


    //endregion

}