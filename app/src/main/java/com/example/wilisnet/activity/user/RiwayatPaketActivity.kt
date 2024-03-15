package com.example.wilisnet.activity.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wilisnet.Global
import com.example.wilisnet.R
import com.example.wilisnet.adapter.AdapterDataMenu2
import com.example.wilisnet.adapter.AdapterDataNotifikasi
import com.example.wilisnet.adapter.AdapterDataRiwayat
import com.example.wilisnet.databinding.ActivityNotifikasiBinding
import com.example.wilisnet.databinding.ActivityRiwayatPaketBinding
import org.json.JSONArray

class RiwayatPaketActivity : AppCompatActivity() {

    val url4 = "http://${Global.ip}/wilisnet/show_pengajuan_user.php"
    val url = "http://${Global.ip}/wilisnet/show_riwayat_paket.php"
    private lateinit var b: ActivityRiwayatPaketBinding
    lateinit var menuAdapter: AdapterDataRiwayat
    val daftarMenu = mutableListOf<HashMap<String, String>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityRiwayatPaketBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        getPengajuan()

        menuAdapter = AdapterDataRiwayat(daftarMenu,this)
        b.reRiwayat.layoutManager = LinearLayoutManager(this)
        b.reRiwayat.adapter = menuAdapter
        showRiwayat()
    }

    fun getPengajuan() {
        val request = object : StringRequest(
            Request.Method.POST, url4,
            Response.Listener { response ->
                if (response == ""){

                }
                else{
//                    Toast.makeText(thisParent, response, Toast.LENGTH_LONG)
//                        .show()
                    val jsonArray = JSONArray(response)

                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        var nama_paket = jsonObject.getString("nama_paket")
                        var kategori = jsonObject.getString("kategori")
                        var status  = jsonObject.getString("status")
                        var alasan  = jsonObject.getString("alasan")
                        var keterangan  = jsonObject.getString("keterangan").toUpperCase()
                        var nama_paket_lama  = jsonObject.getString("nama_paket_lama").toUpperCase()
                        var ketegoria = kategori.toUpperCase()
                        var nama_paketa = nama_paket.toUpperCase()
                        if(kategori == "Berhenti Langganan"){
                            b.txKeterangan.text = "$ketegoria $keterangan $nama_paket_lama"
                        }else{
                            b.txKeterangan.text = "$ketegoria $keterangan KE $nama_paketa"
                        }
                        Log.d("status", status)


                    }
                }

            },
            Response.ErrorListener { _ ->
                Toast.makeText(this, "Terjadi kesalahan koneksi ke server", Toast.LENGTH_LONG)
                    .show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                //file yang dikirim ke web service
                hm.put("email", Global.email)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }


    fun showRiwayat() {
        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                daftarMenu.clear()
                var kecepatan = ""
                var sebelum= ""
                if (response == ""){

                }
                else{
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        var mhs = HashMap<String, String>()
                        mhs.put("id", jsonObject.getString("id"))
                        mhs.put("nama_paket", jsonObject.getString("nama_paket"))
                        mhs.put("waktu", jsonObject.getString("waktu"))
                        mhs.put("kecepatan", jsonObject.getString("kecepatan"))
                        kecepatan = jsonObject.getString("kecepatan")

                        if(sebelum == ""){
                            mhs.put("keterangan","Paket Awal")
                        }else{
                            val a = kecepatan
                            val b = sebelum

                            val numericValueA = a.filter { it.isDigit() }.toInt()
                            val numericValueB = b.filter { it.isDigit() }.toInt()

                            val result = when {
                                numericValueA > numericValueB -> "upgrade"
                                numericValueB > numericValueA -> "downgrade"
                                else -> "Kedua nilai sama"
                            }

                            mhs.put("keterangan",result)


                        }
                        Log.d("sebelum", sebelum)
                        Log.d("sebelum", kecepatan)
                        sebelum = kecepatan


                        daftarMenu.add(mhs)
                    }
                    menuAdapter.notifyDataSetChanged()
                }

            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG)
                    .show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                //file yang dikirim ke web service
                hm.put("email", Global.email)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }
}