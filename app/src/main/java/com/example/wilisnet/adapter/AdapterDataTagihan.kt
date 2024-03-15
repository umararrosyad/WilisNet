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
import com.example.wilisnet.activity.user.DashboardActivity
import com.example.wilisnet.activity.user.TagihanActivity
import java.text.DecimalFormat
import kotlin.collections.HashMap


class AdapterDataTagihan(val dataMenu: List<HashMap<String,String>>, val mainActivity: DashboardActivity) :
    RecyclerView.Adapter<AdapterDataTagihan.HolderDataMhs>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1:Int): HolderDataMhs {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.item_tagihan,p0,false)
        return HolderDataMhs(v)
    }

    override fun getItemCount(): Int {
        return  dataMenu.size
    }

    override fun onBindViewHolder(p0: HolderDataMhs, p1: Int) {
        val data = dataMenu.get(p1)

        val shortMonths = arrayOf("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli"
            ,"Agustus","September","Oktober","November","Desember")

        val periode = data.get("periode")
        p0.txStatus.setText(data.get("status"))
        var harga = data.get("harga")
        val decimalFormat = DecimalFormat("#,###")
        val formattedNumber = decimalFormat.format(harga?.toInt())
        p0.tx_harga.setText("Rp. $formattedNumber")

        if(periode == "pemasangan"){
            p0.tx_periode.setText("Pemasangan")
            harga = "200000"
            val decimalFormat = DecimalFormat("#,###")
            val formattedNumber = decimalFormat.format(harga?.toInt())
            p0.tx_harga.setText("Rp. $formattedNumber")
        }else{
            val t = periode?.split("-")
            var tanggal = t?.get(1)?.toInt()
            if (tanggal != null) {
                tanggal = tanggal - 1
            }
            val bulan = t?.get(0).toString()
            p0.tx_periode.setText("${shortMonths[tanggal!!]} $bulan")
        }




        if (p0.txStatus.text.toString() == "Belum Bayar"){
            p0.lsStatus.setImageResource(R.drawable.bulat_merah);
        }else{
            p0.lsStatus.setImageResource(R.drawable.bulat_hijau);
        }



        p0.cLayout.setOnClickListener{
            val intent = Intent(mainActivity, TagihanActivity::class.java)
            intent.putExtra("id", data.get("id"))
            intent.putExtra("id_transaksi", data.get("id_transaksi"))
            intent.putExtra("status", data.get("status"))
            intent.putExtra("periode", data.get("periode"))
            intent.putExtra("status", data.get("status"))
            intent.putExtra("harga", harga)
            intent.putExtra("nama", data.get("nama"))
            intent.putExtra("kecepatan", data.get("kecepatan"))
            mainActivity.startActivity(intent)
        }
    }

    class HolderDataMhs(v : View) : RecyclerView.ViewHolder(v){
        val tx_periode = v.findViewById<TextView>(R.id.txJudulTransaksi)
        val txStatus = v.findViewById<TextView>(R.id.txMethod)
        val tx_harga = v.findViewById<TextView>(R.id.txHargaT)
        val cLayout = v.findViewById<ConstraintLayout>(R.id.clayoutT)
        val lsStatus = v.findViewById<ImageView>(R.id.imageView26)
    }

}