package com.example.wilisnet.activity.admin

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wilisnet.Global
import com.example.wilisnet.adapter.AdapterDataNotifikasi2
import com.example.wilisnet.adapter.AdapterDataPengajuan
import com.example.wilisnet.databinding.ActivityNotifikasiAdminBinding
import com.example.wilisnet.databinding.ActivityPengajuanBinding
import org.json.JSONArray
import java.text.DecimalFormat

class NotifikasiAdminActivity : AppCompatActivity() {

    private lateinit var b : ActivityNotifikasiAdminBinding

    val url = "http://${Global.ip}/wilisnet/show_notifikasi2.php"
    val url2 = "http://${Global.ip}/notifikasi/tambah_android/"
    lateinit var transAdapter: AdapterDataNotifikasi2
    val daftarTrans = mutableListOf<HashMap<String, String>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityNotifikasiAdminBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        transAdapter = AdapterDataNotifikasi2(daftarTrans,this)
        b.reNotifAdmin.layoutManager = LinearLayoutManager(this)
        b.reNotifAdmin.adapter = transAdapter
        showDataBulanan("Diajukan")



        b.btnKirimTagihan.setOnClickListener {
            b.notifikasi.visibility = View.VISIBLE

            b.btnKembaliNotif.setOnClickListener {
                b.notifikasi.visibility = View.GONE
            }
            b.btnKirimNotif.setOnClickListener {
                var subjek =b.edSubjek.text.toString()
                var isi  =b.edIsi.text.toString()
                b.loading.visibility = View.VISIBLE
                b.notifikasi.visibility = View.GONE
                notif(subjek,isi)
            }
        }

    }

    fun showDataBulanan(tanggal: String) {
        val request = @RequiresApi(Build.VERSION_CODES.O)
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                //mengambil data dari dqatabse melalui web service
                if (response == ""){
                    b.loading.visibility = View.GONE
                }
                else{
                    b.loading.visibility = View.GONE
                    var jum = 0
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        var mhs = HashMap<String, String>()
                        mhs.put("kode", jsonObject.getString("kode"))
                        mhs.put("subjek", jsonObject.getString("subjek"))
                        mhs.put("isi", jsonObject.getString("isi"))
                        mhs.put("waktu", jsonObject.getString("waktu"))

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
                //file yang dikirim ke web service
                hm.put("tanggal", tanggal)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }

    fun notif(subjek: String, isi:String) {
        val request = @RequiresApi(Build.VERSION_CODES.O)
        object : StringRequest(
            Request.Method.POST, url2,
            Response.Listener { response ->

                if (response == ""){
                    b.loading.visibility = View.GONE
                    showDataBulanan("")
                    Toast.makeText(this, "Notifikasi Berhasil Dibuat", Toast.LENGTH_LONG)
                        .show()
                }
                else{
                    b.loading.visibility = View.GONE

                }

            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG)
                    .show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                //file yang dikirim ke web service
                hm.put("subjek", subjek)

                hm.put("isi", isi)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }



}