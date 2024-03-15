package com.example.wilisnet.adapter

import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.wilisnet.Global
import com.example.wilisnet.R
import com.example.wilisnet.activity.user.DashboardActivity
import com.example.wilisnet.activity.user.DetailTagihanActivity
import com.example.wilisnet.activity.user.NotifikasiActivity


class AdapterDataNotifikasi(val dataMenu: List<HashMap<String,String>>, val mainActivity: NotifikasiActivity) :
    RecyclerView.Adapter<AdapterDataNotifikasi.HolderDataMhs>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1:Int): HolderDataMhs {

        val v = LayoutInflater.from(p0.context).inflate(R.layout.item_notifikasi,p0,false)
        return HolderDataMhs(v)
    }



    override fun getItemCount(): Int {
        return  dataMenu.size
    }


    override fun onBindViewHolder(p0: HolderDataMhs, p1: Int) {
        val data = dataMenu.get(p1)
        p0.txSubjek.setText(data.get("subjek"))
        p0.txIsi.setText(data.get("isi"))
        p0.txwaktu.setText(data.get("waktu"))


        if (data.get("baca")== "tidak"){
            p0.Ljudul.setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.blue))
            p0.txSubjek.setTextColor(ContextCompat.getColor(mainActivity, R.color.white))

            p0.txwaktu.setTextColor(ContextCompat.getColor(mainActivity, R.color.white))
        }else if  (data.get("baca")== "iya"){
            p0.Ljudul.setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.white))
            p0.txSubjek.setTextColor(ContextCompat.getColor(mainActivity, R.color.black))

            p0.txwaktu.setTextColor(ContextCompat.getColor(mainActivity, R.color.black))
        }




    }

    class HolderDataMhs(v : View) : RecyclerView.ViewHolder(v){
        val txSubjek = v.findViewById<TextView>(R.id.txSubjekNotif)
        val txIsi = v.findViewById<TextView>(R.id.txIsiNotif)
        val txwaktu = v.findViewById<TextView>(R.id.txTanggalNotif)
        val Ljudul = v.findViewById<ConstraintLayout>(R.id.Ljudul)
    }

}