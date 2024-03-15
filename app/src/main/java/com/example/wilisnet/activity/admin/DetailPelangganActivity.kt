package com.example.wilisnet.activity.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wilisnet.Global
import com.example.wilisnet.R
import com.example.wilisnet.databinding.ActivityDetailPelangganBinding
import com.example.wilisnet.databinding.ActivityLoginAdmBinding
import com.squareup.picasso.Picasso
import org.json.JSONArray

class DetailPelangganActivity : AppCompatActivity() {


    private lateinit var b : ActivityDetailPelangganBinding
    var email = ""
    val url = "http://${Global.ip}/wilisnet/get_detail_pelanggan.php"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDetailPelangganBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        val intent = intent
        if (intent != null) {
            email = intent.getStringExtra("email").toString()
        }

        b.loading.visibility = View.VISIBLE

        getData()
    }

    fun getData() {
        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                if (response == ""){
                    Toast.makeText(this, "Tidak Ada Data", Toast.LENGTH_LONG)
                        .show()
                }
                else{
//                    Toast.makeText(this, response, Toast.LENGTH_LONG)
//                        .show()
                    val jsonArray = JSONArray(response)

                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        b.txnamaP.text = jsonObject.getString("nama").toUpperCase()
                        b.txEmailP.text = jsonObject.getString("email")
                        b.txAalamtDP.text = jsonObject.getString("alamat")
                        b.txNHPDP.text = jsonObject.getString("phone")
                        b.txLanggananDP.text = jsonObject.getString("langganan")

                        val foto =jsonObject.getString("url")
                        if (foto== "null"){
                            Picasso.get()
                                .load(R.drawable.userr)
                                .into(b.imgProfil)
                        }else{
                            Picasso.get().load(foto).into(b.imgProfil);
                        }

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
                hm.put("email",email)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

}