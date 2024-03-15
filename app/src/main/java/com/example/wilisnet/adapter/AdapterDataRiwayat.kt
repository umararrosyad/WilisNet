package com.example.wilisnet.adapter


import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.wilisnet.Global
import com.example.wilisnet.R
import com.example.wilisnet.activity.user.DashboardActivity
import com.example.wilisnet.activity.user.DetailTagihanActivity
import com.example.wilisnet.activity.user.NotifikasiActivity
import com.example.wilisnet.activity.user.RiwayatPaketActivity
import com.squareup.picasso.Picasso
import java.text.DecimalFormat

class AdapterDataRiwayat(val dataMenu: List<HashMap<String,String>>, val mainActivity: RiwayatPaketActivity) :
    RecyclerView.Adapter<AdapterDataRiwayat.HolderDataMhs>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1:Int): HolderDataMhs {

        val v = LayoutInflater.from(p0.context).inflate(R.layout.item_riwayat,p0,false)
        return HolderDataMhs(v)
    }



    override fun getItemCount(): Int {
        return  dataMenu.size
    }


    override fun onBindViewHolder(p0: HolderDataMhs, p1: Int) {
        val data = dataMenu.get(p1)
        p0.txnama.setText(data.get("nama_paket"))
        p0.txket.setText(data.get("keterangan"))
        var keterangan = data.get("keterangan")
        p0.txwaktu.setText(data.get("waktu"))

        if(keterangan == "paket awal"){

        }else if(keterangan == "upgrade"){
            p0.img.setImageResource(R.drawable.upgrade)
        }else if(keterangan == "downgrade"){
            p0.img.setImageResource(R.drawable.downgrade)
        }




    }

    class HolderDataMhs(v : View) : RecyclerView.ViewHolder(v){
        val txnama = v.findViewById<TextView>(R.id.txNamaPaketRp)
        val txket = v.findViewById<TextView>(R.id.txKeterananRp)
        val txwaktu = v.findViewById<TextView>(R.id.txTanggalRp)
        val img = v.findViewById<ImageView>(R.id.imageView10)
    }

}