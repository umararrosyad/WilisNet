package com.example.wilisnet.activity.admin

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wilisnet.Global
import com.example.wilisnet.R
import com.example.wilisnet.activity.nonuser.WelcomeActivity
import com.example.wilisnet.adapter.AdapterDataHeaderDaily
import com.example.wilisnet.databinding.ActivityDashboardAdmBinding
import org.json.JSONArray

class DashboardAdmActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var b : ActivityDashboardAdmBinding

    val url = "http://${Global.ip}/wilisnet/show_daily.php"

    lateinit var transAdapter: AdapterDataHeaderDaily
    val daftarTrans = mutableListOf<HashMap<String, String>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDashboardAdmBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)
        b.loading.visibility = View.VISIBLE

        b.btnNotifikasi.setOnClickListener(this)
        b.btnPelanggan.setOnClickListener(this)
        b.btnRekap.setOnClickListener(this)
        b.btnTagihan.setOnClickListener(this)

        transAdapter = AdapterDataHeaderDaily(daftarTrans,this)
        b.reDailyadmin.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        b.reDailyadmin.adapter = transAdapter
        showDataMenu("Settlement")

        b.cLogout.setOnClickListener {
            b.ConfirmDA.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(this, R.anim.keatas)
            b.LconfirmDA.startAnimation(animation)

            b.btnIyaDA.setOnClickListener {
                val sharedPref = getSharedPreferences("nama_pref", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("login", "defaultValue")
                editor.putString("email", "defaultValue")
                editor.apply()
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent).also { finish() }
            }
            b.btnTidakDA.setOnClickListener {
                b.ConfirmDA.visibility = View.GONE
            }
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
                    b.loading.visibility = View.GONE
                }
                else{
                    b.loading.visibility = View.GONE
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        var mhs = HashMap<String, String>()
                        mhs.put("id_transaksi", jsonObject.getString("id_trans"))
                        mhs.put("nama", jsonObject.getString("nama"))
                        mhs.put("kategori", jsonObject.getString("kategori"))
                        mhs.put("jumlah_bayar", jsonObject.getString("jumlah_bayar"))
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
                hm.put("status", status)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_rekap->{
                val intent = Intent(this, RekapActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_tagihan->{
                val intent = Intent(this, StatusTagihanActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_pelanggan->{
                val intent = Intent(this, PelangganActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_notifikasi->{
                val intent = Intent(this, NotifikasiAdminActivity::class.java)
                startActivity(intent)
            }
        }
    }
}