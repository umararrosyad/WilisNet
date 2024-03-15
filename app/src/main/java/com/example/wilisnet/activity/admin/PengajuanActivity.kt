package com.example.wilisnet.activity.admin

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wilisnet.Global
import com.example.wilisnet.R
import com.example.wilisnet.activity.user.ProfileActivity
import com.example.wilisnet.adapter.AdapterDataPengajuan
import com.example.wilisnet.adapter.AdapterDataStatusTagihan
import com.example.wilisnet.databinding.ActivityPengajuanBinding
import com.example.wilisnet.databinding.ActivityStatusTagihanBinding
import com.example.wilisnet.databinding.ActivityTagihanBinding
import org.json.JSONArray
import org.json.JSONObject

class PengajuanActivity : AppCompatActivity() {
    lateinit var b : ActivityPengajuanBinding

    val url = "http://${Global.ip}/wilisnet/show_pengajuan.php"
    val url2 = "http://${Global.ip}/wilisnet/update_pengajuan.php"
    val url3 = "http://${Global.ip}/wilisnet/tagihan_pemasangan.php"


    var id  = ""
    var status = ""

    var ss = "Diajukan"

    var email = ""
    var id_paket = ""

    lateinit var transAdapter: AdapterDataPengajuan
    val daftarTrans = mutableListOf<HashMap<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityPengajuanBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)
        b.loading.visibility = View.VISIBLE
        transAdapter = AdapterDataPengajuan(daftarTrans,this)
        b.rePengajuan.layoutManager = LinearLayoutManager(this)
        b.rePengajuan.adapter = transAdapter
        showDataPengajuan("Diajukan")

        b.imageView38.setOnClickListener {
            finish()
        }

        b.cbulan.setOnClickListener {
            b.cbulan.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue))
            b.csemua.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
            b.charian.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))

            b.txBulanan.setTextColor(ContextCompat.getColor(this, R.color.white))
            b.txHarian.setTextColor(ContextCompat.getColor(this, R.color.black))
            b.txSemua.setTextColor(ContextCompat.getColor(this, R.color.black))
            b.loading.visibility = View.VISIBLE
            showDataPengajuan("Dikerjakan")

            ss = "Dikerjakan"

        }
        b.charian.setOnClickListener {
            b.cbulan.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
            b.csemua.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
            b.charian.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue))

            b.txBulanan.setTextColor(ContextCompat.getColor(this, R.color.black))
            b.txHarian.setTextColor(ContextCompat.getColor(this, R.color.white))
            b.txSemua.setTextColor(ContextCompat.getColor(this, R.color.black))
            b.loading.visibility = View.VISIBLE

            showDataPengajuan("Disetujui")

            ss= "Disetujui"


        }
        b.csemua.setOnClickListener {
            b.cbulan.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
            b.csemua.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue))
            b.charian.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))

            b.txBulanan.setTextColor(ContextCompat.getColor(this, R.color.black))
            b.txHarian.setTextColor(ContextCompat.getColor(this, R.color.black))
            b.txSemua.setTextColor(ContextCompat.getColor(this, R.color.white))
            b.loading.visibility = View.VISIBLE

            showDataPengajuan("Diajukan")

            ss = "Diajukan"

        }

        b.btnTidakPE.setOnClickListener {
            b.lCormfirm.visibility = View.GONE
        }
        b.btnIyaPE.setOnClickListener {
            b.lCormfirm.visibility = View.GONE
            queryUpdate(status,id)
        }


    }

    fun showDataPengajuan (status : String) {
        val request = @RequiresApi(Build.VERSION_CODES.O)
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                daftarTrans.clear()
                if (response == ""){
                    b.loading.visibility = View.GONE
                    b.rePengajuan.visibility = View.GONE
                }
                else{
                    b.loading.visibility = View.GONE
                    b.rePengajuan.visibility = View.VISIBLE
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        var mhs = HashMap<String, String>()
                        mhs.put("id_pengajuan", jsonObject.getString("id_pengajuan"))
                        mhs.put("nama", jsonObject.getString("nama"))
                        mhs.put("email", jsonObject.getString("email"))
                        mhs.put("id_paket", jsonObject.getString("id_paket"))
                        mhs.put("nama_paket", jsonObject.getString("nama_paket"))
                        mhs.put("kategori", jsonObject.getString("kategori"))
                        mhs.put("foto", jsonObject.getString("url"))
                        mhs.put("status", jsonObject.getString("status"))
                        daftarTrans.add(mhs)

                    }
                    transAdapter.notifyDataSetChanged()
                }

            },
            Response.ErrorListener { _ ->
                Toast.makeText(this, "Terjadi kesalahan koneksi ke server", Toast.LENGTH_LONG)
                    .show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("status", status)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }


    fun queryUpdate(status : String, id:String){
        val request = object : StringRequest(
            Method.POST,url2,
            Response.Listener{ response ->
                showDataPengajuan("Diajukan")
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")

                if(error.equals("000")){
                    showDataPengajuan(ss)
                    if(status == "Disetujui"){
                        kirimtagihan()
                    }

                }else{
                    Toast.makeText(this,"Operasi Gagal", Toast.LENGTH_LONG).show()
                }


            },
            Response.ErrorListener { _ ->
                Toast.makeText(this,"Tidak dapat terhubung ke server CRUD", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                hm.put("mode","insert")
                hm.put("id_pengajuan",id)
                hm.put("status",status)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }

    fun kirimtagihan(){
        val request = object : StringRequest(
            Method.POST,url3,
            Response.Listener{ response ->
                Toast.makeText(this,response, Toast.LENGTH_LONG).show()
//                val jsonObject = JSONObject(response)
//                val error = jsonObject.getString("kode")
//                if(error.equals("000")){
//                }else{
//                    Toast.makeText(this,"Operasi Gagal", Toast.LENGTH_LONG).show()
//                }
            },
            Response.ErrorListener { _ ->
                Toast.makeText(this,"Tidak dapat terhubung ke server CRUD", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                hm.put("mode","insert")
                hm.put("email",email)
                hm.put("status","Belum Bayar")
                hm.put("id_paket",id_paket)
                hm.put("periode","Pemasangan")
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }



    fun setter(tid:String, tstatus:String, temail:String , tid_email:String){
        id = tid
        status = tstatus
        email = temail
        id_paket = tid_email
    }
}