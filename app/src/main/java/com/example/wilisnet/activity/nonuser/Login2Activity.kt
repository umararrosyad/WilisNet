    package com.example.wilisnet.activity.nonuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wilisnet.Global
import com.example.wilisnet.R
import com.example.wilisnet.activity.user.DashboardActivity
import com.example.wilisnet.databinding.ActivityLogin2Binding
import com.example.wilisnet.databinding.ActivityLoginBinding
import com.example.wilisnet.databinding.ActivityWelcomeBinding
import org.json.JSONArray

    class Login2Activity : AppCompatActivity() {
    private lateinit var b : ActivityLogin2Binding
        val ip = Global.ip
        val url = "http://$ip/wilisnet/login.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLogin2Binding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        b.btnLogin.setOnClickListener {
            login(b.edemail.text.toString(),b.editTextTextPersonName2.text.toString())
        }
    }

    fun login(username : String,pass : String) {
        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                //mengambil data dari dqatabse melalui web service
                if (response == ""){
                    Toast.makeText(this, "Username atau password salah", Toast.LENGTH_LONG)
                        .show()
                }
                else{
                    val jsonArray = JSONArray(response)

                    for (x in 0..(jsonArray.length() - 1)) {
                        Global.email = b.edemail.text.toString()
                        val intent = Intent(this, DashboardActivity::class.java)
                        startActivity(intent).also { finish() }
//                        val jsonObject = jsonArray.getJSONObject(x)
//                        val level =jsonObject.getInt("level")
//                        when (level) {
//                            1 -> {
//                                val intent = Intent(this, DashboardActivity::class.java)
//                                startActivity(intent).also { finish() }
//                            }
//                            2 -> {
////                                val intent = Intent(this, Dashboard2Activity::class.java)
////                                startActivity(intent).also { finish() }
//                            }
//                            else -> {
//                                // Jika level lain, lakukan aksi untuk pengguna lain
//                            }
//                        }
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
                hm.put("username", username)
                hm.put("password", pass)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}