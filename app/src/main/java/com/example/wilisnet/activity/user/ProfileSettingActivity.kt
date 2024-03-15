package com.example.wilisnet.activity.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wilisnet.Global
import com.example.wilisnet.R
import com.example.wilisnet.databinding.ActivityPasswordBinding
import com.example.wilisnet.databinding.ActivityProfileSettingBinding
import com.example.wilisnet.databinding.ActivityTagihanBinding
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ProfileSettingActivity : AppCompatActivity() {

    private lateinit var b: ActivityProfileSettingBinding
    val url = "http://${Global.ip}/wilisnet/kelola_profil.php"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityProfileSettingBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)


        b.edEmail.setText(Global.email)
        b.edNama.setText(Global.nama)
        b.edhp.setText(Global.phone)
        b.edalamatP.setText(Global.alamat)

        b.btnSimpan.setOnClickListener{
            queryInsertUpdateDelete("insert")
        }


    }

    fun queryInsertUpdateDelete(mode : String){
        val request = object : StringRequest(
            Method.POST,url,
            Response.Listener{ response ->
                val jsonObject = JSONObject(response)


                val error = jsonObject.getString("kode")

                if(error.equals("000")){
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent).also { finish() }
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
                        hm.put("nama",b.edNama.text.toString())
                        hm.put("alamat",b.edalamatP.text.toString())
                        hm.put("nhp",b.edhp.text.toString())

                    }
                    "update"->{
                        hm.put("mode","update")
                        hm.put("email",Global.email)
                        hm.put("nama",b.edNama.text.toString())
                        hm.put("alamat",b.edalamatP.text.toString())
                        hm.put("nhp",b.edhp.text.toString())
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