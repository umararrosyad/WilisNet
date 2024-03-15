package com.example.wilisnet.activity.nonuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.wilisnet.R
import com.example.wilisnet.activity.admin.DashboardAdmActivity
import com.example.wilisnet.activity.admin.LoginAdmActivity
import com.example.wilisnet.databinding.ActivityDashboardBinding
import com.example.wilisnet.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var b : ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityWelcomeBinding.inflate(layoutInflater)
        val view = b.root

        setContentView(view)
        b.btnLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }

        b.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        b.btnAdmin.setOnClickListener {
            val intent = Intent(this, LoginAdmActivity::class.java)
            startActivity(intent)
        }


    }
}