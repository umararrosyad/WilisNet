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
import com.example.wilisnet.R
import com.example.wilisnet.adapter.AdapterDataPelanggan
import com.example.wilisnet.adapter.AdapterDataStatusTagihan
import com.example.wilisnet.databinding.ActivityNotifikasiAdminBinding
import com.example.wilisnet.databinding.ActivityPelangganBinding
import com.squareup.picasso.Picasso
import org.json.JSONArray
import java.util.Locale

class PelangganActivity : AppCompatActivity() {

    val url = "http://${Global.ip}/wilisnet/get_pelanggan_count.php"
    val url1 = "http://${Global.ip}/wilisnet/get_pelanggan.php"

    lateinit var transAdapter: AdapterDataPelanggan
    val daftarTrans = mutableListOf<HashMap<String, String>>()

    private lateinit var b : ActivityPelangganBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityPelangganBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        getData()

        transAdapter = AdapterDataPelanggan(daftarTrans,this)
        b.reStatusTagihan.layoutManager = LinearLayoutManager(this)
        b.reStatusTagihan.adapter = transAdapter
        showDataPelanggan()

        b.imageView50.setOnClickListener {
            finish()
        }

    }

    fun getData() {
        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                if (response == ""){
                    Toast.makeText(this, "Tidak Ada Data User", Toast.LENGTH_LONG)
                        .show()
                }
                else{
//                    Toast.makeText(this, response, Toast.LENGTH_LONG)
//                        .show()
                    val jsonArray = JSONArray(response)

                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        b.txJumlahAkun.text = jsonObject.getString("jumlah_akun") + " user"
                        b.txLangganan.text = jsonObject.getString("langganan") + " user"
                        b.txNonLangganan.text = jsonObject.getString("non_langganan") + " user"

                        b.loading.visibility = View.GONE

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


    fun showDataPelanggan() {
        val request = @RequiresApi(Build.VERSION_CODES.O)
        object : StringRequest(
            Request.Method.POST, url1,
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
                        mhs.put("langganan", jsonObject.getString("langganan"))
                        mhs.put("url", jsonObject.getString("url"))
                        mhs.put("email", jsonObject.getString("email"))
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
                //file yang dikirim ke web servivce
//                hm.put("tanggal", tanggal)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }

}