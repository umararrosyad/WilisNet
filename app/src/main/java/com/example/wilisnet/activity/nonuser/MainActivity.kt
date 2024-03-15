package com.example.wilisnet.activity.nonuser

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.wilisnet.Global
import com.example.wilisnet.R
import com.example.wilisnet.activity.admin.DashboardAdmActivity
import com.example.wilisnet.activity.user.DashboardActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed({
            val sharedPreferences = getSharedPreferences("nama_pref", Context.MODE_PRIVATE)
            val myValue = sharedPreferences.getString("login", "defaultValue")
            val email = sharedPreferences.getString("email", "defaultValue")

            if(myValue=="defaultValue"){
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
            }
            else if(myValue == "login"){

                if(email == "admin"){
                    val intent = Intent(this, DashboardAdmActivity::class.java)
                    startActivity(intent)
                }else{
                    Global.email = email.toString()
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                }

            }else{
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
            }
            // Memberikan jeda selama 2 detik sebelum beralih ke aktivitas berikutnya

            finish()
        }, 2000)

    }
}