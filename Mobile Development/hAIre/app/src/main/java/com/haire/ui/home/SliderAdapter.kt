package com.haire.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.haire.R

class SliderAdapter(private val drawableList: List<Int>) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    private val indicatorList = mutableListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slider, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val drawableId = drawableList[position]
        holder.itemView.findViewById<ImageView>(R.id.iv_banner).setImageResource(drawableId)

        for (i in indicatorList.indices) {
            val indicatorView = holder.itemView.findViewById<ImageView>(indicatorList[i])
            indicatorView.setImageResource(if (i == position) R.drawable.indicator_selected else R.drawable.indicator_unselected)
        }
    }

    override fun getItemCount(): Int {
        return drawableList.size
    }

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun addIndicatorView(indicatorId: Int) {
        indicatorList.add(indicatorId)
    }
}