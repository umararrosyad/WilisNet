package com.example.wilisnet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.wilisnet.R
import com.example.wilisnet.activity.admin.DashboardAdmActivity
import com.example.wilisnet.activity.admin.RekapActivity
import com.squareup.picasso.Picasso
import java.text.DateFormatSymbols
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AdapterTransSemua(val dataMenu: List<HashMap<String,String>>, val mainActivity: RekapActivity) :
    RecyclerView.Adapter<AdapterTransSemua.HolderDataMhs>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1:Int): HolderDataMhs {

        val v = LayoutInflater.from(p0.context).inflate(R.layout.item_recap,p0,false)
        return HolderDataMhs(v)
    }

    override fun getItemCount(): Int {
        return  dataMenu.size
    }


    override fun onBindViewHolder(p0: HolderDataMhs, p1: Int) {
        val data = dataMenu.get(p1)
        p0.txnama.setText(data.get("waktu"))
        var harga = data.get("jumlah")
        val decimalFormat = DecimalFormat("#,###")
        val formattedNumber = decimalFormat.format(harga?.toInt())
        p0.txnominal.setText("Rp. $formattedNumber")


        p0.card.setOnClickListener {
            mainActivity.b.LCetak.visibility = View.VISIBLE
            mainActivity.waktu = data.get("waktu").toString()
        }


    }

    class HolderDataMhs(v : View) : RecyclerView.ViewHolder(v){
        val txnama = v.findViewById<TextView>(R.id.txwaktuRecap)
        val txnominal = v.findViewById<TextView>(R.id.txTotalRecap)
        val card = v.findViewById<CardView>(R.id.CIRekap)




    }

}