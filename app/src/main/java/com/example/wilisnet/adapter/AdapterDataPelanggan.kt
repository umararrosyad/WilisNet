package com.example.wilisnet.adapter

import android.content.Intent
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
import com.example.wilisnet.activity.admin.DetailPelangganActivity
import com.example.wilisnet.activity.admin.PelangganActivity
import com.example.wilisnet.activity.admin.StatusTagihanActivity
import com.example.wilisnet.activity.user.DetailTagihanActivity
import com.squareup.picasso.Picasso
import java.text.DecimalFormat

class AdapterDataPelanggan(val dataMenu: List<HashMap<String,String>>, val mainActivity: PelangganActivity) :
    RecyclerView.Adapter<AdapterDataPelanggan.HolderDataMhs>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1:Int): HolderDataMhs {

        val v = LayoutInflater.from(p0.context).inflate(R.layout.item_pelanggan,p0,false)
        return HolderDataMhs(v)
    }

    override fun getItemCount(): Int {
        return  dataMenu.size
    }

    override fun onBindViewHolder(p0: HolderDataMhs, p1: Int) {
        val data = dataMenu.get(p1)
        p0.txnama.setText(data.get("nama"))

        val jumlah = data.get("langganan")
        if(jumlah != null){
            if(jumlah == "non langganan"){
                p0.txStatus.setText("non-langganan")
                p0.card.setCardBackgroundColor(ContextCompat.getColor(mainActivity, R.color.blue))
            }else{
                p0.txStatus.setText("langganan")
                p0.card.setCardBackgroundColor(ContextCompat.getColor(mainActivity, R.color.green))
            }
        }

        val foto =data.get("url")
        if (foto== "null"){
            Picasso.get()
                .load(R.drawable.userr)
                .into(p0.img)
        }else{
            Picasso.get().load(data.get("url")).into(p0.img);
        }


        p0.card2.setOnClickListener {
            val intent = Intent(mainActivity, DetailPelangganActivity::class.java)
            intent.putExtra("email",data.get("email") )
            mainActivity.startActivity(intent)
        }

    }

    class HolderDataMhs(v : View) : RecyclerView.ViewHolder(v){
        val txnama = v.findViewById<TextView>(R.id.txNamaST)
        val txStatus = v.findViewById<TextView>(R.id.txStatusST)
        val img = v.findViewById<ImageView>(R.id.imgProfilST)
        val card = v.findViewById<CardView>(R.id.cardST)
        val card2 = v.findViewById<CardView>(R.id.cardPelanggan)
    }

}