package com.example.cryptocurrency

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptocurrency.models.Currency

class CurrencyAdapter(val context: Context): RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {
    var currencies: List<Currency> = emptyList()
    class CurrencyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var icon = itemView.findViewById<ImageView>(R.id.icon)
        var full_name = itemView.findViewById<TextView>(R.id.full_name)
        var exchange_rate = itemView.findViewById<TextView>(R.id.exchange_rate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return CurrencyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return currencies.size
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        var cur = currencies[position]
        holder.full_name.text = cur.fullName
        holder.exchange_rate.text = String.format("%.6f", cur.exchangeRates)
        Glide.with(context).load(cur.iconUrl).into(holder.icon)
    }
}