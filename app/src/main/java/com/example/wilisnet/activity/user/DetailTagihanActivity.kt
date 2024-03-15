package com.example.wilisnet.activity.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wilisnet.Global
import com.example.wilisnet.R
import com.example.wilisnet.databinding.ActivityDetailTagihanBinding
import com.example.wilisnet.databinding.ActivityDetailVoucherBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailTagihanActivity : AppCompatActivity() {

    private lateinit var b : ActivityDetailTagihanBinding
    val url = "http://${Global.ip}/wilisnet/insert_pengajuan.php"
    var id_paket = ""
    var nama =""
    var kecepatan = ""
    var harga = ""
    var kategori = ""
    var pemasangan = ""
    var keterangan =""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDetailTagihanBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)
        val intent = intent
        id_paket = intent.getStringExtra("id").toString()
        nama = intent.getStringExtra("nama").toString()
        kecepatan = intent.getStringExtra("kecepatan").toString()
        harga = intent.getStringExtra("harga").toString()
        val url = intent.getStringExtra("url").toString()

        var nn = 0
        if(Global.id_paket == "kosong"){
             nn = 200000
             b.btnDetail2.visibility = View.VISIBLE
             kategori = "pasang baru"
             pemasangan = "1"
            keterangan = kategori
        }else{
             kategori = "ganti paket"
             pemasangan = "0"
            val a = Global.kecepatan
            val b = kecepatan

            val numericValueA = a.filter { it.isDigit() }.toInt()
            val numericValueB = b.filter { it.isDigit() }.toInt()

            keterangan = when {
                numericValueA > numericValueB -> "downgrate"
                numericValueB > numericValueA -> "upgrade"
                else -> "Kedua nilai sama"
            }
        }



        val decimalFormat = DecimalFormat("#,###")
        val formattedNumber = decimalFormat.format(harga.toInt())
        Picasso.get().load(url).into(b.imageView28);

        var n = harga.toInt()+nn
        val n2 = decimalFormat.format(n)
        b.txNamaD.text = nama
        b.txHARGA.text = "Rp. $formattedNumber"
        b.txHargaD.text ="Rp. $n2"
        b.txMasaD.text = kecepatan




        b.btnDetail2.setOnClickListener {

            if(!detail){
                val rotateAnimation = RotateAnimation(0f, 90f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
                rotateAnimation.duration = 50 // Durasi animasi dalam milidetik
                rotateAnimation.fillAfter = true // Menetapkan posisi ImageView setelah animasi selesai

                b.btnDetail.startAnimation(rotateAnimation)
                b.detailTagihan.visibility = View.VISIBLE

                detail = true
            }
            else if(detail){
                val rotateAnimation = RotateAnimation(90f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
                rotateAnimation.duration = 50 // Durasi animasi dalam milidetik
                rotateAnimation.fillAfter = true // Menetapkan posisi ImageView setelah animasi selesai

                b.btnDetail.startAnimation(rotateAnimation)
                b.detailTagihan.visibility = View.GONE
                detail = false
            }
        }
        b.txDialog.text = "Apakah kamu ingin mengajukan $keterangan pemasangan untuk $nama ?"

        b.btnIya.setOnClickListener {
            queryPemasangan("insert")
        }
        b.btnTidak.setOnClickListener {
            b.dialog.visibility = View.GONE
        }
        b.dialog.setOnClickListener{
            b.dialog.visibility = View.GONE
        }

        b.btnBayar.setOnClickListener {
            b.dialog.visibility = View.VISIBLE

        }
    }

    var detail = false


    fun queryPemasangan(mode : String){
        val request = object : StringRequest(
            Method.POST,url,
            Response.Listener{ response ->
//                    Toast.makeText(this,response, Toast.LENGTH_LONG).show()
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if(error.equals("000")){
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent).also { finish() }
                }else{
                    Toast.makeText(this,error, Toast.LENGTH_LONG).show()
                }


            },
            Response.ErrorListener { error ->
                Toast.makeText(this,error.toString(), Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "insert"->{
                        hm.put("mode","insert")
                        hm.put("email", Global.email)
                        hm.put("kategori",kategori)
                        hm.put("id_paket",id_paket)
                        hm.put("pemasangan",pemasangan)
                        hm.put("id_paket_lama",Global.id_paket)
                        hm.put("keterangan",keterangan)

                    }
                    "update"->{
                        hm.put("mode","Update")

                    }
                    "delete"->{
                        hm.put("mode","delete")
                        hm.put("email", Global.email)

                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}