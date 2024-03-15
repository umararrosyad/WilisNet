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
import androidx.recyclerview.widget.RecyclerView
import com.example.wilisnet.Global
import com.example.wilisnet.R
import com.example.wilisnet.activity.user.DashboardActivity
import com.example.wilisnet.activity.user.DetailLanggananActivity
import com.example.wilisnet.activity.user.DetailTagihanActivity
import com.example.wilisnet.activity.user.DetailVoucherActivity
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import kotlin.collections.HashMap


class AdapterDataMenu(val dataMenu: List<HashMap<String,String>>, val mainActivity: DashboardActivity) :
    RecyclerView.Adapter<AdapterDataMenu.HolderDataMhs>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1:Int): HolderDataMhs {

        val v = LayoutInflater.from(p0.context).inflate(R.layout.item_paket,p0,false)
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

        if(id == Global.id_paket ){
            p0.img.visibility = View.VISIBLE
            p0.cLayout.setOnClickListener{
                val intent = Intent(mainActivity, DetailLanggananActivity::class.java)
                intent.putExtra("nama", nama)
                intent.putExtra("id", id)
                intent.putExtra("kecepatan", kec)
                intent.putExtra("harga", harga)
                intent.putExtra("url", url)
                mainActivity.startActivity(intent)
            }
        }else{
            p0.img.visibility = View.GONE
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
    }

    class HolderDataMhs(v : View) : RecyclerView.ViewHolder(v){
        val tx_nama = v.findViewById<TextView>(R.id.txNamaPaket)
        val tx_harga = v.findViewById<TextView>(R.id.txHarga)
        val photo = v.findViewById<ImageView>(R.id.imageView15)
        val cLayout = v.findViewById<CardView>(R.id.cardView)
        val img = v.findViewById<ImageView>(R.id.img_terpakai)
    }

}