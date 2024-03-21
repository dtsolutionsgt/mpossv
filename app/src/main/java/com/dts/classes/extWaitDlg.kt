package com.dts.classes

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.dts.mpossv.R

class extWaitDlg {
    @JvmField
    var wdhandle: Dialog? = null

    var mTitleLabel: TextView? = null;
    var mBtnLeft: TextView? = null
    var mBtnMid: TextView? = null
    var mBtnRight: TextView? = null

    var mRel: RelativeLayout? = null
    var mRelTop: RelativeLayout? = null
    var mRelBot: RelativeLayout? = null

    var mbuttons: LinearLayout? = null

    var dialog: Dialog? = null
    var cont: Context? = null
    var buttonCount = 0

    val bwidth = 620; val bheight = 450
    var mwidth = 0; var mheight = 0; var mlines = 6; var mminlines = 1

    //region Public methods
    private fun buildDialogbase(activity: Activity,titletext: String,butleft: String,
        butmid: String,butright: String)
    {
        dialog = Dialog(activity)
        wdhandle = dialog
        cont = dialog!!.context

        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.setContentView(R.layout.extwaitdlg)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        mRel = dialog!!.findViewById(R.id.extlistdialogrel)
        mRelTop = dialog!!.findViewById(R.id.xdlgreltop)
        mRelBot = dialog!!.findViewById(R.id.xdlgrelbut)

        mbuttons = dialog!!.findViewById(R.id.linbuttons)
        mTitleLabel = dialog!!.findViewById(R.id.lbltitulo)
        mTitleLabel?.setText(titletext)

        mBtnLeft = dialog!!.findViewById(R.id.btnexit)
        mBtnLeft?.setText(butleft)
        mBtnLeft?.setOnClickListener(View.OnClickListener {
            dialog?.dismiss()
        })

        mBtnMid = dialog!!.findViewById(R.id.btndel)
        mBtnMid?.setText(butmid)
        mBtnMid?.setOnClickListener(View.OnClickListener { })

        mBtnRight = dialog!!.findViewById(R.id.btnadd)
        mBtnRight?.setText(butright)
        mBtnRight?.setOnClickListener(View.OnClickListener { })

        when (buttonCount) {
            1 -> {
                mBtnMid?.setVisibility(View.GONE)
                mBtnRight?.setVisibility(View.GONE)
                mbuttons?.setWeightSum(1f)
            }

            2 -> {
                mBtnRight?.setVisibility(View.GONE)
                mbuttons?.setWeightSum(2f)
            }

            3 -> mbuttons?.setWeightSum(3f)
        }
        mwidth = 0
        mheight = 0
        mlines = 0
    }

    fun buildDialog(activity: Activity, titletext: String) {
        buttonCount = 1
        buildDialogbase(activity, titletext, "Salir", "", "")
    }

    fun buildDialog(activity: Activity, titletext: String, butleft: String) {
        buttonCount = 1
        buildDialogbase(activity, titletext, butleft, "", "")
    }

    fun buildDialog(activity: Activity, titletext: String, butleft: String, butmid: String) {
        buttonCount = 2
        buildDialogbase(activity, titletext, butleft, butmid, "")
    }

    fun buildDialog(activity: Activity,titletext: String,butleft: String,butmid:
        String, butright: String )
    {
        buttonCount = 3
        buildDialogbase(activity, titletext, butleft, butmid, butright)
    }

    fun dismiss() {
        dialog!!.dismiss()
    }

    fun setOnLeftClick(l: View.OnClickListener?) {
        mBtnLeft!!.setOnClickListener(l)
    }

    fun setOnMiddleClick(l: View.OnClickListener?) {
        mBtnMid!!.setOnClickListener(l)
    }

    fun setOnRightClick(l: View.OnClickListener?) {
        mBtnRight!!.setOnClickListener(l)
    }

    fun setWidth(pWidth: Int) {
        mwidth = pWidth
        if (mwidth < 100) mwidth = 0
    }

    fun setHeight(pHeight: Int) {
        mheight = pHeight
        if (mheight < 100) mheight = 0
    }

    fun setLines(pLines: Int) {
        mlines = pLines
        if (mlines < 1) mlines = 0
        if (mlines > 0) mheight = 0
    }

    fun setMinLines(pLines: Int) {
        mminlines = pLines
        if (mminlines < 1) mminlines = 1
    }

    fun show() {
        var fwidth: Int;var fheight: Int; var rlcount: Int
        var itemHeight: Int; var headerHeight: Int; var footerHeight: Int

        fwidth = bwidth;fheight = bheight

        val displayMetrics = cont!!.resources.displayMetrics
        fwidth = displayMetrics.widthPixels
        fwidth = (0.8 * fwidth).toInt()
        if (fwidth > bwidth) fwidth = bwidth

        mRel!!.layoutParams.width = fwidth
        mRel!!.layoutParams.height = fheight
        dialog!!.window!!.setLayout(fwidth, fheight)
        dialog!!.show()
    }
    //endregion

}