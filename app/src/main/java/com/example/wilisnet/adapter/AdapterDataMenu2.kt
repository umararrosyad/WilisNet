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
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.wilisnet.R
import com.example.wilisnet.activity.user.DashboardActivity
import com.example.wilisnet.activity.user.DetailTagihanActivity
import com.example.wilisnet.activity.user.PaketActivity
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import kotlin.collections.HashMap


class AdapterDataMenu2(val dataMenu: List<HashMap<String,String>>, val mainActivity: PaketActivity) :
    RecyclerView.Adapter<AdapterDataMenu2.HolderDataMhs>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1:Int): HolderDataMhs {

        val v = LayoutInflater.from(p0.context).inflate(R.layout.item_paket2,p0,false)
        return HolderDataMhs(v)
    }



    override fun getItemCount(): Int {
        return  dataMenu.size
    }


    override fun onBindViewHolder(p0: HolderDataMhs, p1: Int) {
        val data = dataMenu.get(p1)
        val id = data.get("id_paket")
        val kec =(data.get("kecepatan"))
        val nama = data.get("nama_paket")
        p0.tx_nama.setText(nama+" "+kec)

        var harga = data.get("harga")
        val decimalFormat = DecimalFormat("#,###")
        val formattedNumber = decimalFormat.format(harga?.toInt())
        p0.tx_harga.setText("Rp. $formattedNumber")
        val url = data.get("url")
        Picasso.get().load(data.get("url")).into(p0.photo);

//        //memberikan perbedaan warna antara baris genap dan ganjil
//        if(p1.rem(2)==0) p0.cLayout.setBackgroundColor(Color.rgb(230,245,240))
//        else p0.cLayout.setBackgroundColor(Color.rgb(255,255,245))

        p0.cLayout.setOnClickListener{
            val intent = Intent(mainActivity, DetailTagihanActivity::class.java)
            intent.putExtra("nama", nama)
            intent.putExtra("id", id)
            intent.putExtra("kecepatan", kec)
            intent.putExtra("harga", harga)
            intent.putExtra("url", url)
            mainActivity.startActivity(intent)
        }

    }

    class HolderDataMhs(v : View) : RecyclerView.ViewHolder(v){
        val tx_nama = v.findViewById<TextView>(R.id.txNamaPaket)
        val photo = v.findViewById<ImageView>(R.id.imageView15)
        val tx_harga = v.findViewById<TextView>(R.id.txHarga)
        val cLayout = v.findViewById<CardView>(R.id.cardView)
    }

}