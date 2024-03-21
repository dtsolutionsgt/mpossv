package com.dts.ladapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.AppMethods
import com.dts.base.DateUtils
import com.dts.base.MiscUtils
import com.dts.base.d_sucursalestado
import com.dts.mpossv.PBase
import com.dts.mpossv.R

class LA_SucEstado(val itemList: ArrayList<d_sucursalestado>,val owner: PBase) : RecyclerView.Adapter<LA_SucEstado.ViewHolder>() {

    var Curr: String=""

    private var mu: MiscUtils? = null
    private var du: DateUtils? = null
    private var app: AppMethods? = null

    constructor(itemList: ArrayList<d_sucursalestado>,owner: PBase,CurrSymbol: String) : this(itemList,owner) {
        this.Curr=CurrSymbol
    }

    var selectedItemPosition: Int = -1
    lateinit var lay: LinearLayout

    fun Init() {
        mu = owner.mu
        du = owner.du
        app = owner.app
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LA_SucEstado.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lv_sucitem, parent, false)
        Init()
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: LA_SucEstado.ViewHolder, position: Int) {
        val item = itemList[position]
        val isSelected = position == selectedItemPosition

        holder.bindItems(itemList[position])

        holder.bind(item, isSelected)

        holder.itemView.setOnClickListener {
            val previousSelectedPosition = selectedItemPosition
            selectedItemPosition = position

            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(position)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        fun bindItems(item: d_sucursalestado) {

            val txtname = itemView.findViewById(R.id.txtnombre) as TextView
            val txtest = itemView.findViewById(R.id.textView4) as TextView
            val txtdif = itemView.findViewById(R.id.textView5) as TextView
            val txtmonto = itemView.findViewById(R.id.textView8) as TextView
            val txtcant = itemView.findViewById(R.id.textView9) as TextView
            val img1=itemView.findViewById(R.id.imageView7) as ImageView
            val img2=itemView.findViewById(R.id.imageView5) as ImageView
            lay=itemView.findViewById(R.id.relitem) as LinearLayout

            txtname.text = item.nombre
            txtest.text = item.estadoDesc
            txtmonto.text = Curr+mu?.frmintth(item.totalVentas)
            txtcant.text = "Fact.: "+item.cantVentas

            var dif=item.diferCierre
            if (dif==0.0) {
                txtdif.text = ""
                img2.visibility=View.INVISIBLE
            } else {
                if (dif<1)  dif= 1.0
                if (dif<-1) dif=-1.0
                txtdif.text = Curr+mu?.frmintth(item.diferCierre)
                img2.visibility=View.VISIBLE
            }

            when (item.estado) {
               -1 -> {img1.setImageResource(R.drawable.blank)}
                0 -> {img1.setImageResource(R.drawable.sucest0)}
                1 -> {img1.setImageResource(R.drawable.sucest1)}
            }

        }

        fun bind(mitem: d_sucursalestado, isSelected: Boolean) {
            lay.setBackgroundResource(if (isSelected)
                R.drawable.frame_round_flatb_sel else R.drawable.frame_round_flatb)
        }

        override fun onClick(p0: View?) {}

    }

}
