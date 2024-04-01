package com.dts.mpossv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ProspVend : PBase() {

    override fun onCreate(savedInstanceState: Bundle?) {

        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_prosp_vend)

            super.InitBase(savedInstanceState)
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //region Events


    //endregion

    //region Main


    //endregion

    //region Aux


    //endregion

    //region Dialogs


    //endregion

    //region Activity Events


    //endregion


}