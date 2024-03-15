package com.example.wilisnet.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.wilisnet.R
import com.example.wilisnet.activity.user.TagihanActivity
import com.example.wilisnet.activity.user.TransaksiActivity
import com.squareup.picasso.Picasso
import java.text.DateFormatSymbols
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class AdapterDataTransaksi(val dataMenu: List<HashMap<String,String>>, val mainActivity: TransaksiActivity) :
    RecyclerView.Adapter<AdapterDataTransaksi.HolderDataMhs>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1:Int): HolderDataMhs {

        val v = LayoutInflater.from(p0.context).inflate(R.layout.item_transaksi,p0,false)
        return HolderDataMhs(v)
    }



    override fun getItemCount(): Int {
        return  dataMenu.size
    }


    override fun onBindViewHolder(p0: HolderDataMhs, p1: Int) {
        val data = dataMenu.get(p1)

        val nama = data.get("kategori")
        p0.tx_judul.setText("Pembayaran $nama")
        p0.txmethod.setText(data.get("methode"))

        val date = data.get("tanggal_pembayaran").toString()
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm 'WIB' dd MMM yyyy", Locale.getDefault())
        outputFormat.dateFormatSymbols = DateFormatSymbols().apply {
            shortMonths = arrayOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des")
        }

        try {
            val date = inputFormat.parse(date)
            val formattedDate = outputFormat.format(date)
            p0.txtanggal.setText(formattedDate) // Output: 22:01 WIB 14 Nov 2023
        } catch (e: Exception) {
            e.printStackTrace()
        }





        var harga = data.get("jumlah_bayar")
        val decimalFormat = DecimalFormat("#,###")
        val formattedNumber = decimalFormat.format(harga?.toInt())
        p0.tx_harga.setText("Rp. $formattedNumber")



        //memberikan perbedaan warna antara baris genap dan ganjil


    }

    class HolderDataMhs(v : View) : RecyclerView.ViewHolder(v){
        val tx_judul = v.findViewById<TextView>(R.id.txJudulTransaksi)
        val txmethod = v.findViewById<TextView>(R.id.txMethod)
        val txtanggal = v.findViewById<TextView>(R.id.txTanggal)
        val tx_harga = v.findViewById<TextView>(R.id.txHargaT)
        val cLayout = v.findViewById<ConstraintLayout>(R.id.clayoutT)

    }

}