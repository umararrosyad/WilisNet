package com.example.wilisnet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wilisnet.R
import com.example.wilisnet.activity.admin.DashboardAdmActivity
import java.text.DecimalFormat
import kotlin.collections.HashMap


class AdapterDailyAdm(val dataMenu: List<HashMap<String,String>>, val mainActivity: DashboardAdmActivity) :
    RecyclerView.Adapter<AdapterDailyAdm.HolderDataMhs>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1:Int): HolderDataMhs {

        val v = LayoutInflater.from(p0.context).inflate(R.layout.item_daily,p0,false)
        return HolderDataMhs(v)
    }

    override fun getItemCount(): Int {
        return  dataMenu.size
    }


    override fun onBindViewHolder(p0: HolderDataMhs, p1: Int) {
        val data = dataMenu.get(p1)
        p0.txnama.setText(data.get("nama"))
        p0.txkategori.setText(data.get("kategori"))
        var harga = data.get("jumlah_bayar")
        val decimalFormat = DecimalFormat("#,###")
        val formattedNumber = decimalFormat.format(harga?.toInt())
        p0.txnominal.setText("Rp. $formattedNumber")


    }

    class HolderDataMhs(v : View) : RecyclerView.ViewHolder(v){
        val txnama = v.findViewById<TextView>(R.id.txUserD)
        val txkategori = v.findViewById<TextView>(R.id.txKatD)
        val txnominal = v.findViewById<TextView>(R.id.txNominalD)
    }

}