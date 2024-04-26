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
import com.dts.base.clsClasses
import com.dts.mpossv.PBase
import com.dts.mpossv.R

class LA_CotizVendList(val itemList: ArrayList<clsClasses.clsVendedorCotizacionDoc>, val owner: PBase) : RecyclerView.Adapter<LA_CotizVendList.ViewHolder>() {

    private var mu: MiscUtils? = null
    private var du: DateUtils? = null
    private var app: AppMethods? = null

    var selectedItemPosition: Int = -1
    lateinit var lay: LinearLayout

    fun Init() {
        mu = owner.mu
        du = owner.du
        app = owner.app
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LA_CotizVendList.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lv_cotizvendlist, parent, false)
        Init()
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: LA_CotizVendList.ViewHolder, position: Int) {
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

        fun bindItems(item: clsClasses.clsVendedorCotizacionDoc) {

            val txtname = itemView.findViewById(R.id.txtnombre) as TextView
            val txtmontoconv = itemView.findViewById(R.id.textView5) as TextView
            val txtmonto = itemView.findViewById(R.id.textView19) as TextView
            val txtfecha = itemView.findViewById(R.id.textView21) as TextView
            val txtest = itemView.findViewById(R.id.textView22) as TextView
            val imgest=itemView.findViewById(R.id.imageView10) as ImageView
            lay=itemView.findViewById(R.id.relitem) as LinearLayout

            txtname.text = item.nombre
            txtfecha.text=item.sfecha
            txtest.text=item.sestado
            txtmonto.text="Monto: "+item.moneda+mu?.frmintth(item.monto)
            txtmontoconv.text="$"+mu?.frmintth(item.monto_convertido)

            if (item.estado==1) {
                imgest.setImageResource(R.drawable.color_green_grad)
            } else {
                imgest.setImageResource(R.drawable.blank)
            }

        }

        fun bind(mitem: clsClasses.clsVendedorCotizacionDoc, isSelected: Boolean) {
            lay.setBackgroundResource(if (isSelected)
                R.drawable.frame_round_sel else R.drawable.frame_round)
        }

        override fun onClick(p0: View?) {}

    }

}
