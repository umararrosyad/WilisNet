package com.example.wilisnet.activity.user

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wilisnet.Global
import com.example.wilisnet.R
import com.example.wilisnet.adapter.AdapterDataTransaksi
import com.example.wilisnet.databinding.ActivityTransaksiBinding
import org.json.JSONArray
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class TransaksiActivity : AppCompatActivity() {

    private lateinit var b : ActivityTransaksiBinding

    val url = "http://${Global.ip}/wilisnet/show_transaksi.php"

    lateinit var transAdapter: AdapterDataTransaksi
    val daftarTrans = mutableListOf<HashMap<String, String>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityTransaksiBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)


        transAdapter = AdapterDataTransaksi(daftarTrans,this)
        b.reTransaksi.layoutManager = LinearLayoutManager(this)
        b.reTransaksi.adapter = transAdapter
        showDataMenu("Settlement")

        b.btnSelesai.setOnClickListener {
            b.btnSelesai.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue))
            b.btnBerlangsung.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
            b.btnGagalT.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))

            b.txTersedia.setTextColor(ContextCompat.getColor(this, R.color.white))
            b.txTerpakai.setTextColor(ContextCompat.getColor(this, R.color.black))
            b.txPending.setTextColor(ContextCompat.getColor(this, R.color.black))

            showDataMenu("Settlement")
        }
        b.btnBerlangsung.setOnClickListener {
            b.btnSelesai.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
            b.btnBerlangsung.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue))
            b.btnGagalT.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))

            b.txTersedia.setTextColor(ContextCompat.getColor(this, R.color.black))
            b.txTerpakai.setTextColor(ContextCompat.getColor(this, R.color.white))
            b.txPending.setTextColor(ContextCompat.getColor(this, R.color.black))
            showDataMenu("Pending")
        }
        b.btnGagalT.setOnClickListener {
            b.btnSelesai.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
            b.btnBerlangsung.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
            b.btnGagalT.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue))

            b.txTersedia.setTextColor(ContextCompat.getColor(this, R.color.black))
            b.txTerpakai.setTextColor(ContextCompat.getColor(this, R.color.black))
            b.txPending.setTextColor(ContextCompat.getColor(this, R.color.white))
            showDataMenu("Expire")
        }

    }

    fun showDataMenu (status: String) {
        val request = @RequiresApi(Build.VERSION_CODES.O)
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                daftarTrans.clear()
                //mengambil data dari dqatabse melalui web service
                if (response == ""){
                    b.noTransaksi.visibility = View.VISIBLE
                    b.reTransaksi.visibility = View.GONE
                }
                else{
                    b.noTransaksi.visibility = View.GONE
                    b.reTransaksi.visibility = View.VISIBLE
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        var mhs = HashMap<String, String>()
                        mhs.put("id_transaksi", jsonObject.getString("id_trans"))
                        mhs.put("email", jsonObject.getString("email"))
                        mhs.put("kategori", jsonObject.getString("kategori"))
                        mhs.put("tanggal_pembayaran", jsonObject.getString("tanggal_pembayaran"))
                        mhs.put("jumlah_bayar", jsonObject.getString("jumlah_bayar"))
                        mhs.put("status", jsonObject.getString("status"))
                        mhs.put("methode", jsonObject.getString("methode"))
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
                hm.put("email", Global.email)
                hm.put("status", status)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }


}