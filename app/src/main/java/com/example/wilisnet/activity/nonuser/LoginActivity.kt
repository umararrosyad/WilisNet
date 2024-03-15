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
import com.example.wilisnet.databinding.ActivityLoginBinding
import com.example.wilisnet.databinding.ActivityWelcomeBinding
import org.json.JSONArray

    class LoginActivity : AppCompatActivity() {
    private lateinit var b : ActivityLoginBinding

    val ip = Global.ip
    val url = "http://$ip/wilisnet/login.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        b.btnLogin.setOnClickListener {
            login(b.edemail.text.toString(),b.editTextTextPersonName2.text.toString())
        }
        b.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    fun login(username : String,pass : String) {
        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                //mengambil data dari Databse melalui web service
                if (response == ""){
                    Toast.makeText(this, "Username atau password salah", Toast.LENGTH_LONG)
                        .show()
                }
                else{
                    val jsonArray = JSONArray(response)

                    for (x in 0..(jsonArray.length() - 1)) {
                        Global.email = b.edemail.text.toString()
                        val intent = Intent(this, OtploginActivity::class.java)
                        startActivity(intent).also { finish() }
                    }
                }

            },
            Response.ErrorListener { error ->
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