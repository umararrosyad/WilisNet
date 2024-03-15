package com.example.wilisnet.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.wilisnet.R
import com.example.wilisnet.activity.nonuser.OtploginActivity
import com.example.wilisnet.activity.user.DashboardActivity
import com.example.wilisnet.activity.user.DetailVoucherActivity
import com.example.wilisnet.activity.user.GenerateActivity
import com.example.wilisnet.activity.user.VoucherActivity
import java.text.DecimalFormat
import kotlin.collections.HashMap


class AdapterDataVoucher3(val dataMenu: List<HashMap<String,String>>, val mainActivity: DashboardActivity) :
    RecyclerView.Adapter<AdapterDataVoucher3.HolderDataMhs>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1:Int): HolderDataMhs {

        val v = LayoutInflater.from(p0.context).inflate(R.layout.item_voucher3,p0,false)
        return HolderDataMhs(v)
    }



    override fun getItemCount(): Int {
        return  dataMenu.size
    }


    override fun onBindViewHolder(p0: HolderDataMhs, p1: Int) {
        val data = dataMenu.get(p1)
        p0.txnama.text =(data.get("nama_voucher")).toString()
        p0.durasi.text =(data.get("durasi")).toString()
        p0.besar.text =(data.get("besar_paket")).toString()
//        val nama = (data.get("besar"))
//        p0.txnama.setText("$nama $besar_paket /$durasi ")
//        var harga = data.get("harga")
//        val decimalFormat = DecimalFormat("#,###")
//        val formattedNumber = decimalFormat.format(harga?.toInt())
//        p0.txharga.setText("Rp. $formattedNumber")

        val nama = (data.get("nama_voucher")).toString()
        val durasi = (data.get("durasi")).toString()
        val besar = (data.get("besar_paket")).toString()

        //memberikan perbedaan warna antara baris genap dan ganjil

        p0.cLayout.setOnClickListener{
            val intent = Intent(mainActivity, GenerateActivity::class.java)
            intent.putExtra("id",data.get("id"))
            intent.putExtra("nama_voucher",nama)
            intent.putExtra("durasi",durasi)
            intent.putExtra("besar",besar)
            intent.putExtra("user",data.get("user"))
            intent.putExtra("status",data.get("status_penggunaan"))
            mainActivity.startActivity(intent)
        }

    }

    class HolderDataMhs(v : View) : RecyclerView.ViewHolder(v){
        val txnama = v.findViewById<TextView>(R.id.txnamaMy)
        val besar = v.findViewById<TextView>(R.id.txbesarmy)
        val durasi = v.findViewById<TextView>(R.id.txwaktumy)
        val cLayout = v.findViewById<ConstraintLayout>(R.id.cardvoucher)
    }

}