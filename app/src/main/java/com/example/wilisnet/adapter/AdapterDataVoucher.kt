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
import androidx.recyclerview.widget.RecyclerView
import com.example.wilisnet.R
import com.example.wilisnet.activity.user.DashboardActivity
import com.example.wilisnet.activity.user.DetailVoucherActivity
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import kotlin.collections.HashMap


class AdapterDataVoucher(val dataMenu: List<HashMap<String,String>>, val mainActivity: DashboardActivity) :
    RecyclerView.Adapter<AdapterDataVoucher.HolderDataMhs>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1:Int): HolderDataMhs {

        val v = LayoutInflater.from(p0.context).inflate(R.layout.item_voucher,p0,false)
        return HolderDataMhs(v)
    }



    override fun getItemCount(): Int {
        return  dataMenu.size
    }


    override fun onBindViewHolder(p0: HolderDataMhs, p1: Int) {
        val data = dataMenu.get(p1)
        val durasi =(data.get("durasi"))
        val besar_paket =(data.get("besar_paket"))
        val nama = (data.get("nama_voucher"))
        p0.txnama.setText("$nama $besar_paket /$durasi ")
        var harga = data.get("harga")
        val decimalFormat = DecimalFormat("#,###")
        val formattedNumber = decimalFormat.format(harga?.toInt())
        p0.txharga.setText("Rp. $formattedNumber")

        val url = data.get("url")
        Picasso.get().load(data.get("url")).into(p0.poto);

        //memberikan perbedaan warna antara baris genap dan ganjil

        p0.cLayout.setOnClickListener{
            val intent = Intent(mainActivity, DetailVoucherActivity::class.java)
            intent.putExtra("nama", nama)
            intent.putExtra("durasi", durasi)
            intent.putExtra("besar", besar_paket)
            intent.putExtra("harga", harga)
            intent.putExtra("pengguna", data.get("pengguna"))
            intent.putExtra("id_voucher", data.get("id_voucher"))
            intent.putExtra("url", url)
            mainActivity.startActivity(intent)
        }

    }

    class HolderDataMhs(v : View) : RecyclerView.ViewHolder(v){
        val txnama = v.findViewById<TextView>(R.id.txNamaVoucher)
        val txharga = v.findViewById<TextView>(R.id.txHargaVoucher)
        val poto = v.findViewById<ImageView>(R.id.imageView13)
        val cLayout = v.findViewById<ConstraintLayout>(R.id.clayoutVoucher)
    }

}