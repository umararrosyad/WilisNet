package com.example.wilisnet.activity.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.example.wilisnet.databinding.ActivityGenerateBinding
import com.example.wilisnet.databinding.ActivityNotifikasiBinding
import org.json.JSONArray
import org.json.JSONObject

class NotifikasiActivity : AppCompatActivity() {

    lateinit var b : ActivityNotifikasiBinding
    val url = "http://${Global.ip}/wilisnet/show_notif.php"
    val url2 = "http://${Global.ip}/wilisnet/update_notifikasi.php"
    lateinit var menuAdapter: AdapterDataNotifikasi
    val daftarMenu = mutableListOf<HashMap<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityNotifikasiBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        menuAdapter = AdapterDataNotifikasi(daftarMenu,this)
        b.reNotif.layoutManager = LinearLayoutManager(this)
        b.reNotif.adapter = menuAdapter
        showDataNotif()

    }


    fun showDataNotif() {
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
                        mhs.put("id_notifikasi", jsonObject.getString("id_notifikasi"))
                        mhs.put("kode", jsonObject.getString("kode"))
                        mhs.put("subjek", jsonObject.getString("subjek"))
                        mhs.put("isi", jsonObject.getString("isi"))
                        mhs.put("waktu", jsonObject.getString("waktu"))
                        mhs.put("baca", jsonObject.getString("baca"))

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
                hm.put("email", Global.email)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }


    override fun onDestroy() {
        super.onDestroy()
        baca("update")
    }

    fun baca(mode : String){
        val request = object : StringRequest(
            Method.POST,url2,
            Response.Listener{ response ->
                val jsonObject = JSONObject(response)


                val error = jsonObject.getString("kode")

                if(error.equals("000")){

                }else{
                    Toast.makeText(this,"Operasi Gagal", Toast.LENGTH_LONG).show()
                }


            },
            Response.ErrorListener { _ ->

                Toast.makeText(this,"Tidak dapat terhubung ke server CRUD", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "insert"->{
                        hm.put("mode","insert")
                        hm.put("email",Global.email)

                    }
                    "update"->{
                        hm.put("mode","update")
                        hm.put("email",Global.email)
                    }
                    "delete"->{
                        hm.put("mode","delete")
                        hm.put("email",Global.email)
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }
}