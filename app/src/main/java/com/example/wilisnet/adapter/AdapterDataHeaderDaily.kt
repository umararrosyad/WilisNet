package com.example.wilisnet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wilisnet.R
import com.example.wilisnet.activity.admin.DashboardAdmActivity
import java.text.DecimalFormat

class AdapterDataHeaderDaily(val dataMenu: List<HashMap<String,String>>, val mainActivity: DashboardAdmActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val headerView = LayoutInflater.from(parent.context).inflate(R.layout.item_header_daily, parent, false)
                HeaderViewHolder(headerView)
            }
            VIEW_TYPE_ITEM -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_daily, parent, false)
                DataViewHolder(itemView)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            // Lakukan sesuatu dengan ViewHolder header
        } else if (holder is DataViewHolder) {
            val data = dataMenu[position - 1] // Kurangi 1 karena posisi 0 adalah header
            holder.txnama.setText(data.get("nama"))
            holder.txkategori.setText(data.get("kategori"))
            var harga = data.get("jumlah_bayar")
            val decimalFormat = DecimalFormat("#,###")
            val formattedNumber = decimalFormat.format(harga?.toInt())
            holder.txnominal.setText("Rp. $formattedNumber")
        }
    }

    override fun getItemCount(): Int {
        // Jumlah total item, termasuk header
        return dataMenu.size + 1
    }

    // Implementasikan kelas ViewHolder sesuai dengan kebutuhan Anda

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Deklarasikan elemen tampilan header di sini
    }

    class DataViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val txnama = v.findViewById<TextView>(R.id.txUserD)
        val txkategori = v.findViewById<TextView>(R.id.txKatD)
        val txnominal = v.findViewById<TextView>(R.id.txNominalD)
    }
}