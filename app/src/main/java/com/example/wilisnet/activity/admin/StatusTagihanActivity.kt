package com.example.wilisnet.activity.admin

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
import com.example.wilisnet.adapter.AdapterDataStatusTagihan
import com.example.wilisnet.adapter.AdapterTransSemua
import com.example.wilisnet.databinding.ActivityRekapBinding
import com.example.wilisnet.databinding.ActivityStatusTagihanBinding
import org.json.JSONArray
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.HashMap

class StatusTagihanActivity : AppCompatActivity() {
    private lateinit var b : ActivityStatusTagihanBinding

    val url = "http://${Global.ip}/wilisnet/show_status_tagihan.php"
    val url2 = "http://${Global.ip}/tagihan/notif_android/"

    lateinit var transAdapter: AdapterDataStatusTagihan
    val daftarTrans = mutableListOf<HashMap<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityStatusTagihanBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)
        b.loading.visibility = View.VISIBLE
        transAdapter = AdapterDataStatusTagihan(daftarTrans,this)
        b.reStatusTagihan.layoutManager = LinearLayoutManager(this)
        b.reStatusTagihan.adapter = transAdapter
        showDataTagihan()

        b.imageView37.setOnClickListener {
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
            showDataNunggak()

        }
        b.charian.setOnClickListener {
            b.cbulan.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
            b.csemua.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
            b.charian.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue))

            b.txBulanan.setTextColor(ContextCompat.getColor(this, R.color.black))
            b.txHarian.setTextColor(ContextCompat.getColor(this, R.color.white))
            b.txSemua.setTextColor(ContextCompat.getColor(this, R.color.black))
            b.loading.visibility = View.VISIBLE

            showDataLunas()


        }
        b.csemua.setOnClickListener {
            b.cbulan.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
            b.csemua.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue))
            b.charian.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))

            b.txBulanan.setTextColor(ContextCompat.getColor(this, R.color.black))
            b.txHarian.setTextColor(ContextCompat.getColor(this, R.color.black))
            b.txSemua.setTextColor(ContextCompat.getColor(this, R.color.white))
            b.loading.visibility = View.VISIBLE

            showDataTagihan()

        }


        b.btnKirimTagihan.setOnClickListener {
            b.loading.visibility = View.VISIBLE
            kirim()
        }


    }

    fun showDataTagihan () {
        val request = @RequiresApi(Build.VERSION_CODES.O)
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                daftarTrans.clear()
                //mengambil data dari dqatabse melalui web service
                if (response == ""){
                    b.loading.visibility = View.GONE
                }
                else{
                    b.loading.visibility = View.GONE
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        var mhs = HashMap<String, String>()
                        mhs.put("nama", jsonObject.getString("nama"))
                        mhs.put("nama_paket", jsonObject.getString("nama_paket"))
                        mhs.put("foto", jsonObject.getString("foto"))
                        mhs.put("jumlah_belum_lunas", jsonObject.getString("jumlah_belum_lunas"))
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
//                hm.put("tanggal", tanggal)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }

    fun kirim() {
        val request = @RequiresApi(Build.VERSION_CODES.O)
        object : StringRequest(
            Request.Method.POST, url2,
            Response.Listener { response ->
                //mengambil data dari dqatabse melalui web service
                if (response == ""){
                    b.loading.visibility = View.GONE
                    Toast.makeText(this,"Berhasil Kirim Notifikasi", Toast.LENGTH_LONG)
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
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }

    fun showDataLunas () {
        val request = @RequiresApi(Build.VERSION_CODES.O)
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                daftarTrans.clear()
                //mengambil data dari dqatabse melalui web service
                if (response == ""){
                    b.loading.visibility = View.GONE
                }
                else{
                    b.loading.visibility = View.GONE
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        var mhs = HashMap<String, String>()
                        mhs.put("nama", jsonObject.getString("nama"))
                        mhs.put("nama_paket", jsonObject.getString("nama_paket"))
                        mhs.put("foto", jsonObject.getString("foto"))
                        mhs.put("jumlah_belum_lunas", jsonObject.getString("jumlah_belum_lunas"))

                        val jumlah = jsonObject.getInt("jumlah_belum_lunas")
                        if(jumlah == 1){

                        }else if(jumlah >= 2){

                        }else{
                            daftarTrans.add(mhs)
                        }


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
                //file yang dikirim ke web servivce
//                hm.put("tanggal", tanggal)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }

    fun showDataNunggak () {
        val request = @RequiresApi(Build.VERSION_CODES.O)
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                daftarTrans.clear()
                //mengambil data dari dqatabse melalui web service
                if (response == ""){
                    b.loading.visibility = View.GONE
                }
                else{
                    b.loading.visibility = View.GONE
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        var mhs = HashMap<String, String>()
                        mhs.put("nama", jsonObject.getString("nama"))
                        mhs.put("nama_paket", jsonObject.getString("nama_paket"))
                        mhs.put("foto", jsonObject.getString("foto"))
                        mhs.put("jumlah_belum_lunas", jsonObject.getString("jumlah_belum_lunas"))

                        val jumlah = jsonObject.getInt("jumlah_belum_lunas")
                        if(jumlah == 1){

                        }else if(jumlah >= 2){
                            daftarTrans.add(mhs)
                        }else{

                        }


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
//                hm.put("tanggal", tanggal)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }

}