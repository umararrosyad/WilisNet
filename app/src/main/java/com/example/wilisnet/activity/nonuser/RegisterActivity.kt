package com.example.wilisnet.activity.nonuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wilisnet.Global
import com.example.wilisnet.R
import com.example.wilisnet.activity.user.DashboardActivity
import com.example.wilisnet.databinding.ActivityLoginBinding
import com.example.wilisnet.databinding.ActivityRegisterBinding
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class RegisterActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var b : ActivityRegisterBinding
    val ip = Global.ip
    val url = "http://$ip/wilisnet/register.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityRegisterBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        b.btnRegister2.setOnClickListener(this)
        b.btnLogin2.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_register2->{
                Global.email = b.edemail.text.toString()
                Global.pass = b.edpassword.text.toString()
                Global.nama = b.ednama.text.toString()
                Global.phone = b.edphone.text.toString()
                Global.alamat = b.edalamat.text.toString()

                val intent = Intent(this, OtpregisActivity::class.java)
                startActivity(intent)

//                queryInsertUpdateDelete("insert")

            }
            R.id.btnLogin2->{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }



}