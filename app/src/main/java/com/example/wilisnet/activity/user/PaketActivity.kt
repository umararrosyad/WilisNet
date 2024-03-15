package com.example.wilisnet.activity.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wilisnet.Global
import com.example.wilisnet.R
import com.example.wilisnet.adapter.AdapterDataMenu
import com.example.wilisnet.adapter.AdapterDataMenu2
import com.example.wilisnet.databinding.ActivityPaketBinding
import com.example.wilisnet.databinding.ActivityVoucherBinding
import org.json.JSONArray

class PaketActivity : AppCompatActivity() {

    private lateinit var b : ActivityPaketBinding

    val ip = Global.ip
    val url = "http://$ip/wilisnet/show_paket.php"
    lateinit var menuAdapter: AdapterDataMenu2
    val daftarMenu = mutableListOf<HashMap<String, String>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityPaketBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)
    }

    override fun onStart() {
        super.onStart()
        menuAdapter = AdapterDataMenu2(daftarMenu,this)
        b.reVoucher.layoutManager = LinearLayoutManager(this)
        b.reVoucher.adapter = menuAdapter
        showDataPaket("")
    }

    fun showDataPaket(namaMhs: String) {
        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                daftarMenu.clear()
                if (response == ""){

                }
                else{
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        var mhs = HashMap<String, String>()
                        mhs.put("id_paket", jsonObject.getString("id_paket"))
                        mhs.put("nama_paket", jsonObject.getString("nama_paket"))
                        mhs.put("harga", jsonObject.getString("harga"))
                        mhs.put("kecepatan", jsonObject.getString("kecepatan"))
                        mhs.put("url", jsonObject.getString("url"))
                        daftarMenu.add(mhs)
                    }
                    menuAdapter.notifyDataSetChanged()
                }

            },
            Response.ErrorListener { _ ->
                Toast.makeText(this, "Terjadi kesalahan koneksi ke server", Toast.LENGTH_LONG)
                    .show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                //file yang dikirim ke web service
                hm.put("nama_menu", namaMhs)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }
}