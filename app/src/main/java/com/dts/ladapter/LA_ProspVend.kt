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
import com.dts.base.d_sucursalestado
import com.dts.mpossv.PBase
import com.dts.mpossv.R

class LA_ProspVend(val itemList: ArrayList<clsClasses.clsVendedorProspecto>, val owner: PBase) : RecyclerView.Adapter<LA_ProspVend.ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LA_ProspVend.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lv_prospvend, parent, false)
        Init()
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: LA_ProspVend.ViewHolder, position: Int) {
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

        fun bindItems(item: clsClasses.clsVendedorProspecto) {

            val txtname = itemView.findViewById(R.id.txtnombre) as TextView
            val txtdif = itemView.findViewById(R.id.textView5) as TextView
            val imggr=itemView.findViewById(R.id.imageView7) as ImageView
            val imgest=itemView.findViewById(R.id.imageView10) as ImageView

            lay=itemView.findViewById(R.id.relitem) as LinearLayout

            txtname.text = item.nombre
            txtdif.text=""+item.cant+" / "+item.meta

            imggr.setImageResource(R.drawable.blank)

            if (item.cant>0) {
                if (item.cant<item.meta) {
                    imgest.setImageResource(R.drawable.color_ocra_grad)
                } else {
                    imgest.setImageResource(R.drawable.color_green_grad)
                }
            } else {
                imgest.setImageResource(R.drawable.color_red_grad)
            }
        }

        fun bind(mitem: clsClasses.clsVendedorProspecto, isSelected: Boolean) {
            lay.setBackgroundResource(if (isSelected)
                R.drawable.frame_round_sel else R.drawable.frame_round)
        }

        override fun onClick(p0: View?) {}

    }

}
