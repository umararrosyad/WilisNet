package com.example.wilisnet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.wilisnet.R
import com.example.wilisnet.activity.admin.NotifikasiAdminActivity
import com.example.wilisnet.activity.user.NotifikasiActivity


class AdapterDataNotifikasi2(val dataMenu: List<HashMap<String,String>>, val mainActivity: NotifikasiAdminActivity) :
    RecyclerView.Adapter<AdapterDataNotifikasi2.HolderDataMhs>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1:Int): HolderDataMhs {

        val v = LayoutInflater.from(p0.context).inflate(R.layout.item_notifikasi2,p0,false)
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

    }

    class HolderDataMhs(v : View) : RecyclerView.ViewHolder(v){
        val txSubjek = v.findViewById<TextView>(R.id.txSubjekNotif)
        val txIsi = v.findViewById<TextView>(R.id.txIsiNotif)
        val txwaktu = v.findViewById<TextView>(R.id.txTanggalNotif)
        val Ljudul = v.findViewById<ConstraintLayout>(R.id.Ljudul)
    }

}