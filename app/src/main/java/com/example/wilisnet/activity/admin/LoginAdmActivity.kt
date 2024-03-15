package com.example.wilisnet.activity.admin

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wilisnet.Global
import com.example.wilisnet.R
import com.example.wilisnet.activity.nonuser.RegisterActivity
import com.example.wilisnet.databinding.ActivityLoginAdmBinding
import com.example.wilisnet.databinding.ActivityPengajuanBinding
import org.json.JSONArray

class LoginAdmActivity : AppCompatActivity() {

    private lateinit var b : ActivityLoginAdmBinding

    val url = "http://${Global.ip}/wilisnet/mikrotik/routeros-api-master/examples/login.php"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginAdmBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        b.btnLogin.setOnClickListener {
            val address = b.edAddress.text.toString()
            val username = b.edUsernameAdm.text.toString()
            val pass = b.edPassAdm.text.toString()

            b.loading.visibility = View.VISIBLE
            login(Global.vpn, username, pass)
        }


    }

    fun login (address: String, username:String, pass :String) {
        val request = @RequiresApi(Build.VERSION_CODES.O)
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->

                if(response == "login"){
                    b.loading.visibility = View.GONE
                    val sharedPref = getSharedPreferences("nama_pref", Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putString("login", "login")
                    editor.putString("email", "admin")
                    editor.apply()
                    val intent = Intent(this, DashboardAdmActivity::class.java)
                    startActivity(intent)
                }else if (response == "gagal"){
                    b.loading.visibility = View.GONE
                    Toast.makeText(this, "Login Gagal", Toast.LENGTH_LONG)
                        .show()
                }
            },
            Response.ErrorListener { _ ->
                b.loading.visibility = View.GONE
                Toast.makeText(this, "Login Gagal", Toast.LENGTH_LONG)
                    .show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                //file yang dikirim ke web service
                hm.put("address",address)
                hm.put("username",username)
                hm.put("pass",pass)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }


}