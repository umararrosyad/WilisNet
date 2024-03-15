package com.example.wilisnet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.wilisnet.R
import com.example.wilisnet.activity.admin.PengajuanActivity
import com.squareup.picasso.Picasso


class AdapterDataPengajuan(val dataMenu: List<HashMap<String,String>>, val mainActivity: PengajuanActivity) :
    RecyclerView.Adapter<AdapterDataPengajuan.HolderDataMhs>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1:Int): HolderDataMhs {

        val v = LayoutInflater.from(p0.context).inflate(R.layout.item_pengajuan,p0,false)
        return HolderDataMhs(v)
    }

    override fun getItemCount(): Int {
        return  dataMenu.size
    }


    override fun onBindViewHolder(p0: HolderDataMhs, p1: Int) {
        val data = dataMenu.get(p1)
        p0.txnama.setText(data.get("nama"))
        p0.txpaket.setText(data.get("nama_paket"))
        p0.txKategori.setText(data.get("kategori"))

        val foto =data.get("foto")
        if (foto== "null"){
            Picasso.get()
                .load(R.drawable.userr) // Ganti dengan ID Drawable gambar yang diinginkan
                .into(p0.img)
        }else{
            Picasso.get().load(data.get("foto")).into(p0.img);
        }

        var status= data.get("status").toString()
        var tstatus = ""
        if(status == "Diajukan"){
            tstatus = "Disetujui"
        }else if (status == "Disetujui"){
            tstatus = "Dikerjakan"
        }else if (status == "Dikerjakan"){
            tstatus = "Selesai"
        }
        val id = data.get("id_pengajuan").toString()
        val email = data.get("email").toString()
        val id_paket = data.get("id_paket").toString()



        p0.card.setOnClickListener {
            mainActivity.b.lCormfirm.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(mainActivity, R.anim.keatas)
            mainActivity.b.CDialog.startAnimation(animation)
            mainActivity.setter(id,tstatus, email,id_paket)
        }

    }

    class HolderDataMhs(v : View) : RecyclerView.ViewHolder(v){
        val txnama = v.findViewById<TextView>(R.id.txNamaPe)
        val txpaket = v.findViewById<TextView>(R.id.txPaketP)
        val txKategori = v.findViewById<TextView>(R.id.txpasangP)
        val img = v.findViewById<ImageView>(R.id.imgProfilST)
        val card = v.findViewById<CardView>(R.id.cardPE)
    }

}