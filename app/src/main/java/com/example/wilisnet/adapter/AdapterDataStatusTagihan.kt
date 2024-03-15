package com.example.wilisnet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.wilisnet.R
import com.example.wilisnet.activity.admin.DashboardAdmActivity
import com.example.wilisnet.activity.admin.StatusTagihanActivity
import com.squareup.picasso.Picasso
import java.text.DecimalFormat

class AdapterDataStatusTagihan(val dataMenu: List<HashMap<String,String>>, val mainActivity: StatusTagihanActivity) :
    RecyclerView.Adapter<AdapterDataStatusTagihan.HolderDataMhs>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1:Int): HolderDataMhs {

        val v = LayoutInflater.from(p0.context).inflate(R.layout.item_status_tagihan,p0,false)
        return HolderDataMhs(v)
    }

    override fun getItemCount(): Int {
        return  dataMenu.size
    }

    override fun onBindViewHolder(p0: HolderDataMhs, p1: Int) {
        val data = dataMenu.get(p1)
        p0.txnama.setText(data.get("nama"))
        p0.txpaket.setText(data.get("nama_paket"))

        val jumlah = data.get("jumlah_belum_lunas")?.toInt()
        if(jumlah != null){
            if(jumlah == 1){
                p0.txStatus.setText("Belum Bayar")
                p0.card.setCardBackgroundColor(ContextCompat.getColor(mainActivity, R.color.blue))
            }else if(jumlah >= 2){
                p0.txStatus.setText("Lewat Tenggat")
                p0.card.setCardBackgroundColor(ContextCompat.getColor(mainActivity, R.color.red))
            }else{
                p0.txStatus.setText("Lunas")
                p0.card.setCardBackgroundColor(ContextCompat.getColor(mainActivity, R.color.green))
            }
        }

        val foto =data.get("foto")
        if (foto== "null"){
            Picasso.get()
                .load(R.drawable.userr)
                .into(p0.img)
        }else{
            Picasso.get().load(data.get("foto")).into(p0.img);
        }

    }

    class HolderDataMhs(v : View) : RecyclerView.ViewHolder(v){
        val txnama = v.findViewById<TextView>(R.id.txNamaST)
        val txpaket = v.findViewById<TextView>(R.id.txPaketST)
        val txStatus = v.findViewById<TextView>(R.id.txStatusST)
        val img = v.findViewById<ImageView>(R.id.imgProfilST)
        val card = v.findViewById<CardView>(R.id.cardST)
    }

}