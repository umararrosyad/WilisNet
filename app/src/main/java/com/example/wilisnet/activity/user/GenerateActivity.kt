package com.example.wilisnet.activity.user

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wilisnet.Global
import com.example.wilisnet.R
import com.example.wilisnet.databinding.ActivityDetailVoucherBinding
import com.example.wilisnet.databinding.ActivityGenerateBinding
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class GenerateActivity : AppCompatActivity() {

    lateinit var b : ActivityGenerateBinding

    val url = "http://${Global.ip}/wilisnet/mikrotik/routeros-api-master/examples/ganeratevoucher.php"
    val url2 = "http://${Global.ip}/wilisnet/guna_voucher.php"

    var detail = false
    var id =""

    var status = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityGenerateBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)
        val intent = intent
        id = intent.getStringExtra("id").toString()
        b.txNamaD.text = intent.getStringExtra("nama_voucher")
        b.txBesarD.text = intent.getStringExtra("besar")
        b.txMasaD.text = intent.getStringExtra("durasi")
        b.txUsername.text = intent.getStringExtra("user")

        b.txsalin.setOnClickListener {
            val textToCopy = b.txUsername.text.toString()
            copyToClipboard(textToCopy)
            Toast.makeText(this,"Username telah disalin ke Clipboard", Toast.LENGTH_LONG).show()
        }




        status = intent.getStringExtra("status").toString()

        if(status == "Belum Terpakai"){
            b.btnGanerate.visibility = View.VISIBLE
            b.btnPakai.visibility = View.GONE
            b.txsalin.visibility = View.GONE
        }else if(status == "Dibuat"){
            b.btnGanerate.visibility = View.GONE
            b.btnPakai.visibility = View.VISIBLE
            b.txsalin.visibility = View.VISIBLE
        }

        b.btnPakai.setOnClickListener {
            val intent = Intent(this, WebActivity::class.java)
            startActivity(intent)
        }

        b.btnGanerate.setOnClickListener {
            val user = generateRandomString(6)
            val pass = user
            buatvoucher(user, pass)
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        clipboardManager.setPrimaryClip(clipData)

        // Tambahkan tindakan setelah teks disalin, jika diperlukan
        // Misalnya, tampilkan notifikasi, pesan, atau feedback lainnya
        // setelah teks berhasil disalin.
    }

    fun generateRandomString(length: Int): String {
        val allowedChars = ('a'..'z') + ('A'..'Z')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun buatvoucher(user : String, password:String){
        val request = object : StringRequest(
            Method.POST,url,
            Response.Listener{ response ->
                Toast.makeText(this,response, Toast.LENGTH_LONG).show()

                if (response =="Pengguna hotspot berhasil ditambahkan."){
                    updateData(user,password)

                }else if(response == "failure: already have user with this name for this server" ){
                    val user = generateRandomString(6)
                    val pass = user
                    buatvoucher(user, pass)
                }



            },
            Response.ErrorListener { error ->
                Toast.makeText(this,error.toString(), Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                hm.put("username",user)
                hm.put("password",password)
                hm.put("time",b.txMasaD.text.toString())
                hm.put("besar",b.txBesarD.text.toString())
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }

    fun updateData(user : String,pass: String){
        val request = object : StringRequest(
            Method.POST,url2,
            Response.Listener{ response ->
//                Toast.makeText(this,response, Toast.LENGTH_LONG).show()
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if(error.equals("000")){
                    finish()
                }else{
                    Toast.makeText(this,"Operasi Gagal", Toast.LENGTH_LONG).show()
                }

            },
            Response.ErrorListener { error ->
                Toast.makeText(this,error.toString(), Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                //file yang dikirim ke web service
                hm.put("mode","update")
                hm.put("user",user)
                hm.put("password",pass)
                hm.put("status","Dibuat")
                hm.put("id",id)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }
}