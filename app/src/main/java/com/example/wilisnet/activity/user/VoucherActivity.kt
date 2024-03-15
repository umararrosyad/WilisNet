package com.example.wilisnet.activity.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wilisnet.Global
import com.example.wilisnet.adapter.AdapterDataMenu
import com.example.wilisnet.adapter.AdapterDataVoucher
import com.example.wilisnet.adapter.AdapterDataVoucher2
import com.example.wilisnet.databinding.ActivityDashboardBinding
import com.example.wilisnet.databinding.ActivityVoucherBinding
import org.json.JSONArray

class VoucherActivity : AppCompatActivity() {

    private lateinit var b : ActivityVoucherBinding

    val ip = Global.ip
    val url2 = "http://$ip/wilisnet/show_voucher.php"

    lateinit var VoucherAdapter: AdapterDataVoucher2
    val daftarVoucher = mutableListOf<HashMap<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityVoucherBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)
        VoucherAdapter = AdapterDataVoucher2(daftarVoucher,this)
        b.reVoucher.layoutManager = LinearLayoutManager(this)
        b.reVoucher.adapter = VoucherAdapter
        showVoucher("")
        showVoucher("")
    }

    fun showVoucher(namaMhs: String) {
        val request = object : StringRequest(
            Request.Method.POST, url2,
            Response.Listener { response ->
                daftarVoucher.clear()
                if (response == ""){

                }
                else{
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        var mhs = HashMap<String, String>()
                        mhs.put("id_voucher", jsonObject.getString("id_voucher"))
                        mhs.put("nama_voucher", jsonObject.getString("nama_voucher"))
                        mhs.put("durasi", jsonObject.getString("durasi"))
                        mhs.put("harga", jsonObject.getString("harga"))
                        mhs.put("besar_paket", jsonObject.getString("besar_paket"))
                        mhs.put("pengguna", jsonObject.getString("pengguna"))
                        mhs.put("url", jsonObject.getString("url"))
                        daftarVoucher.add(mhs)
                    }
                    VoucherAdapter.notifyDataSetChanged()
                }

            },
            Response.ErrorListener { error ->
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