package com.dts.ladapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.d_menuitem
import com.dts.mpossv.R

class LA_MenuAdapter(val itemList: ArrayList<d_menuitem>) : RecyclerView.Adapter<LA_MenuAdapter.ViewHolder>() {

    var selectedItemPosition: Int = -1
    lateinit var lay: LinearLayout

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LA_MenuAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lv_menuitem, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: LA_MenuAdapter.ViewHolder, position: Int) {
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

        fun bindItems(mitem: d_menuitem) {
            val textViewName = itemView.findViewById(R.id.textViewUsername) as TextView
            val img1=itemView.findViewById(R.id.imageView7) as ImageView
            lay=itemView.findViewById(R.id.relitem) as LinearLayout

            textViewName.text = mitem.nombre

            when (mitem.mid) {
                0 -> {img1.setImageResource(R.drawable.picon5)}
                1 -> {img1.setImageResource(R.drawable.picon2)}
                2 -> {img1.setImageResource(R.drawable.picon3)}
                3 -> {img1.setImageResource(R.drawable.picon4)}
                4 -> {img1.setImageResource(R.drawable.picon6)}
                5 -> {img1.setImageResource(R.drawable.picon5)}
                6 -> {img1.setImageResource(R.drawable.picon1)}
            }

        }

        fun bind(mitem: d_menuitem, isSelected: Boolean) {
            lay.setBackgroundResource(if (isSelected)
                R.drawable.frame_round_flatb_sel else R.drawable.frame_round_flatb)
        }

        override fun onClick(p0: View?) {}

    }

}
