package com.example.wilisnet.activity.nonuser

import android.Manifest.permission.SEND_SMS
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.telephony.SmsManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wilisnet.Global
import com.example.wilisnet.Global.Companion.email
import com.example.wilisnet.activity.user.DashboardActivity
import com.example.wilisnet.databinding.ActivityOtpregisBinding
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class OtpregisActivity : AppCompatActivity() {
    val ip = Global.ip
    val url = "http://$ip/wilisnet/send2.php"
    private lateinit var b : ActivityOtpregisBinding

    val url2 = "http://$ip/wilisnet/register.php"
    var otp = ""

    var email = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityOtpregisBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)
        otp = generateOTP()
        email = Global.email
        queryInsertUpdateDelete("insert")
        time()
        b.textView32.setOnClickListener {
            time()
            otp = generateOTP()
            b.textView32.visibility = View.GONE
            queryInsertUpdateDelete("insert")

        }

        b.btnLanjutkan.setOnClickListener {
            val ooo = b.edemail.text.toString()
            if(ooo == otp){
                val sharedPref = getSharedPreferences("nama_pref", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("login", "login")
                editor.putString("email", email)
                editor.apply()
                queryRegister("insert")
                val intent = Intent(this, Login2Activity::class.java)
                startActivity(intent).also { finish() }
            }else{
                Toast.makeText(this,"OTP yang anda masukkan salah", Toast.LENGTH_LONG).show()
            }

        }
    }

    fun time(){
        val totalTimeInMillis = 120000L

        // Mengatur interval waktu update hitungan mundur (setiap 1 detik)
        val countDownInterval = 1000L

        // Membuat objek CountDownTimer
        object : CountDownTimer(totalTimeInMillis, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                // Menghitung waktu tersisa dalam hitungan mundur
                val timeLeftInSeconds = millisUntilFinished / 1000

                // Mengubah waktu tersisa dalam hitungan mundur menjadi format menit:detik
                val minutes = timeLeftInSeconds / 60
                val seconds = timeLeftInSeconds % 60
                val timeLeftFormatted = String.format("%02d:%02d", minutes, seconds)

                // Menampilkan waktu tersisa dalam hitungan mundur pada TextView
                b.txTime.text = timeLeftFormatted
            }

            override fun onFinish() {
                b.txTime.text = "Waktu telah habis"
                b.textView32.visibility = View.VISIBLE
            }
        }.start() // Memulai hitungan mundur
    }
    fun generateOTP(): String {
        val chars = "0123456789"
        val otp = StringBuilder()
        for (i in 0 until 6) {
            otp.append(chars.random())
        }
        return otp.toString()
    }

    fun queryInsertUpdateDelete(mode : String){
        val request = object : StringRequest(
            Method.POST,url,
            Response.Listener{ response ->

            },
            Response.ErrorListener { error ->
                Toast.makeText(this,error.toString(), Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "insert"->{
                        hm.put("mode","insert")
                        hm.put("email",email)
                        hm.put("otp",otp)

                    }
                    "update"->{
                        hm.put("mode","insert")
                        hm.put("email",email)
                    }
                    "delete"->{
                        hm.put("mode","delete")
                        hm.put("email",email)
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }

    fun queryRegister(mode : String){
        val request = object : StringRequest(
            Method.POST,url2,
            Response.Listener{ response ->
//                    Toast.makeText(this,response, Toast.LENGTH_LONG).show()
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if(error.equals("000")){

                }else{
                    Toast.makeText(this,error, Toast.LENGTH_LONG).show()
                }


            },
            Response.ErrorListener { error ->
                Toast.makeText(this,error.toString(), Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "insert"->{
                        hm.put("mode","insert")
                        hm.put("email",Global.email)
                        hm.put("password",Global.pass)
                        hm.put("nama",Global.nama)
                        hm.put("phone",Global.phone)
                        hm.put("alamat",Global.alamat)

                    }
                    "update"->{
                        hm.put("mode","insert")
                        hm.put("email",Global.email)
                        hm.put("password",Global.pass)
                        hm.put("nama",Global.nama)
                        hm.put("phone",Global.phone)
                        hm.put("alamat",Global.alamat)

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