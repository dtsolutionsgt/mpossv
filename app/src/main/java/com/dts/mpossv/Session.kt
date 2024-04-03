package com.dts.mpossv

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses
import com.dts.base.d_menuitem
import com.dts.classes.RecyclerItemClickListener
import com.dts.classes.extListDlg
import com.dts.classes.extWaitDlg
import com.dts.ladapter.LA_MenuAdapter

class Session : PBase() {

    var waitdlg: extWaitDlg? = null

    var menuview: RecyclerView? = null

    var adapter: LA_MenuAdapter? = null

    val menus = ArrayList<d_menuitem>()

    var saveselidx:Int=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_session)

            super.InitBase(savedInstanceState)

            menuview = findViewById<View>(R.id.recview) as RecyclerView
            menuview?.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

            gl?.emp=44
            gl?.licid=1
            gl?.curr= app?.getLocalCurrencySymbol().toString()

            listItems()

            /*
            waitdlg = extWaitDlg()
            waitdlg!!.buildDialog(this, "Espere por favor . . .", "Ocultar")
            waitdlg!!.show()
            */

            setHandlers()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //region Events

    fun doSettings(view: View?) {
        showMenuConfig()

        //val customDialog = CustomDialog(this)
        //customDialog.show()
    }

    private fun setHandlers() {
        try {
            menuview?.addOnItemTouchListener(RecyclerItemClickListener(this, menuview!!,
                object : RecyclerItemClickListener.OnItemClickListener {

                    override fun onItemClick(view: View, position: Int) {
                        saveselidx=position
                        processMenu(menus.get(position).mid)
                    }

                    override fun onItemLongClick(view: View?, position: Int) {
                        //toast("long click")
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
        try {

            menus.clear()

            /*
            addMenuCat(0, "Sucursales")
            addMenuCat(1, "Informes")
            addMenuCat(2, "Inventario")
            addMenuCat(3, "Recepciónes / Ajustes")
            addMenuCat(4, "Depósitos")
            addMenuCat(5, "Gastos")
            addMenuCat(6, "Recursos humanos")
            */

            addMenuCat(100, "Prospectos")
            addMenuCat(101, "Prospectos por vendedor")

            adapter = LA_MenuAdapter(menus)
            menuview?.adapter = adapter

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    private fun processMenu(idmenu: Int) {
        if (app?.sinInternet()!!) return;

        try {
            when (idmenu) {
                0 -> {startActivity(Intent(this, Sucursales::class.java))}
                1 -> {}

                100 -> {}
                101 -> {startActivity(Intent(this, ProspVend::class.java))}

            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }


    //endregion

    //region Configuration

    fun showMenuConfig() {
        try {
            val listdlg = extListDlg()

            listdlg.buildDialog(this@Session, "Configuración")
            listdlg.setLines(3)
            listdlg.setWidth(800)
            listdlg.setTopRightPosition()

            listdlg.addData("Actualizar sucursales")
            listdlg.addData("Registrar")
            listdlg.addData("Borrar registración")

            /*
            listdlg.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                try {
                    when (position) {
                        0 -> {

                        }
                    }
                    listdlg.dismiss()
                } catch (e: Exception) {
                }
            }
             */

            listdlg.setOnLeftClick { v: View? -> listdlg.dismiss() }
            listdlg.show()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

    //region Aux

    private fun addMenuCat(mid: Int,ss: String) {
        menus.add(d_menuitem(mid, ss))
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

    class CustomDialog(context: Context) : Dialog(context) {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)

            setContentView(R.layout.extwaitdlg)

            val params = window?.attributes
            params?.width = 600
            params?.height = 800
            window?.attributes = params as WindowManager.LayoutParams
        }

        // Utility method to convert dp to pixels
        private fun dpToPx(dp: Int): Int {
            val scale = context.resources.displayMetrics.density
            return (dp * scale + 0.5f).toInt()
        }
    }

    //endregion

    //region Activity Events

    override fun onResume() {
        try {
            super.onResume()
            gl?.dialogr = Runnable { dialogswitch() }

            adapter?.setSelectedItem(saveselidx)
            //listItems()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

}