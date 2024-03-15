package com.example.wilisnet.activity.user

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.wilisnet.Global
import com.example.wilisnet.R
import com.example.wilisnet.activity.nonuser.WelcomeActivity
import com.example.wilisnet.adapter.AdapterDataMenu
import com.example.wilisnet.databinding.ActivityDashboardBinding
import com.example.wilisnet.fragment.user.TagihanFragment
import com.example.wilisnet.fragment.user.BelanjaFragment
import com.example.wilisnet.fragment.user.HomeFragment
import com.example.wilisnet.fragment.user.VoucherFragment
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import org.json.JSONArray
import java.text.NumberFormat
import java.util.*
import kotlin.collections.HashMap

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var b : ActivityDashboardBinding
    lateinit var ft : FragmentTransaction
    lateinit var fragtagihan : TagihanFragment
    lateinit var fragvoucer : VoucherFragment
    lateinit var fragbelanja : BelanjaFragment
    lateinit var fraghome : HomeFragment
    val ip = Global.ip
    val url = "http://$ip/wilisnet/data_dashboard.php"
    val url2 = "http://$ip/wilisnet/sum_notif.php"
    var id_paket = ""

    lateinit var menuAdapter: AdapterDataMenu
    val daftarMenu = mutableListOf<HashMap<String, String>>()

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDashboardBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)
        b.lonceng.visibility = View.GONE
        b.titikMerah.visibility = View.GONE
        getData()
        sum_notif()

        b.dialog.visibility = View.GONE

        b.dialog.setOnClickListener {
            b.dialog.visibility = View.GONE
        }







        fragtagihan = TagihanFragment()
        fragbelanja = BelanjaFragment()
        fragvoucer = VoucherFragment()
        fraghome = HomeFragment()
        var bottomNavigation = b.btmNav
        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.nav_home))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.nav_tagihan))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.nav_belanja))
        bottomNavigation.add(MeowBottomNavigation.Model(4, R.drawable.nav_kupon))





        bottomNavigation.setOnClickMenuListener {

            when (it.id) {
                1 -> {
                    ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.FrameUser,fraghome).commit()
                    b.FrameUser.visibility = View.VISIBLE

                }
                2 -> {
                    ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.FrameUser,fragtagihan).commit()
                    b.FrameUser.visibility = View.VISIBLE
                }

                3 -> {
                    ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.FrameUser,fragbelanja).commit()
                    b.FrameUser.visibility = View.VISIBLE
                }
                4 -> {
                    ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.FrameUser,fragvoucer).commit()
                    b.FrameUser.visibility = View.VISIBLE
                }
                else -> throw IllegalArgumentException("Unknown menu ID ${it.id}")
            }

        }

        ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.FrameUser,fraghome).commit()
        b.FrameUser.visibility = View.VISIBLE

        bottomNavigation.show(1, true)

        bottomNavigation.show(1, true)



        drawerLayout = b.drawerLayout
        navigationView = b.navigationView

        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.open_nav,
            R.string.close_nav
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Mengaktifkan Navigation Drawer saat tombol diklik
        b.cardView10.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.FrameUser, HomeFragment()).commit()
            navigationView.setCheckedItem(R.id.nav_home)
        }


        b.lonceng.setOnClickListener {
            val intent = Intent(this, NotifikasiActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onStart() {
        super.onStart()
        getData()
        sum_notif()
    }

    override fun onRestart() {
        super.onRestart()
        getData()
        sum_notif()
    }

    override fun onResume() {
        super.onResume()
        getData()
        sum_notif()
    }


    fun setLoading(){
        b.loading.visibility = View.VISIBLE
    }

    fun hideloading(){
        b.loading.visibility = View.GONE
    }

    fun getData() {
        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                if (response == ""){
                    Toast.makeText(this, "Username atau password salah", Toast.LENGTH_LONG)
                        .show()
                }
                else{
//                    Toast.makeText(this, response, Toast.LENGTH_LONG)
//                        .show()
                    val jsonArray = JSONArray(response)

                    for (x in 0..(jsonArray.length() - 1)) {
//                        Global.email = b.edemail.text.toString()
                        val jsonObject = jsonArray.getJSONObject(x)
                        b.txNamaUser.text = jsonObject.getString("nama")
                            .uppercase(Locale.getDefault())
                        Global.id_paket =jsonObject.getString("id_paket")
                        Global.nama_paket = jsonObject.getString("nama_paket")
                        Global.kecepatan = jsonObject.getString("kecepatan")
                        Global.poin = jsonObject.getString("poin")
                        Global.profil = jsonObject.getString("url")
                        b.txEmail.text = Global.email

                        val headerView = navigationView.getHeaderView(0)
                        val textViewUsername = headerView.findViewById<TextView>(R.id.textView13)
                        textViewUsername.text = b.txNamaUser.text
                        val textViewEmail = headerView.findViewById<TextView>(R.id.textEmail)
                        textViewEmail.text = b.txEmail.text
                        val img = headerView.findViewById<ImageView>(R.id.imageprofil)
                        if(Global.profil == "null"){
                            Picasso.get()
                                .load(R.drawable.userr) // Ganti dengan ID Drawable gambar yang diinginkan
                                .into(img)
                            Picasso.get()
                                .load(R.drawable.userr) // Ganti dengan ID Drawable gambar yang diinginkan
                                .into(b.imageprofil)
                        }else{
                            Picasso.get().load(Global.profil).into(img);
                            Picasso.get().load(Global.profil).into(b.imageprofil);

                        }

                    }
                }

            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG)
                    .show()

                Log.d("url", error.toString())
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                //file yang dikirim ke web service
                hm.put("email", Global.email)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home ->{
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_settings ->{
                val intent = Intent(this, TransaksiActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_share ->{
                val intent = Intent(this, NotifikasiActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_histori->{
                val intent = Intent(this, RiwayatPaketActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_about -> supportFragmentManager.beginTransaction()

            R.id.nav_logout -> {

                b.dialog.visibility = View.VISIBLE
                b.btnIya.setOnClickListener {
                    val sharedPref = getSharedPreferences("nama_pref", Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putString("login", "defaultValue")
                    editor.putString("email", "defaultValue")
                    editor.apply()
                    val intent = Intent(this, WelcomeActivity::class.java)
                    startActivity(intent).also { finish() }
                }
                b.btnTidak.setOnClickListener {
                    b.dialog.visibility = View.GONE
                }

            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun sum_notif() {
        val request = object : StringRequest(
            Request.Method.POST, url2,
            Response.Listener { response ->
                if (response == ""){
                    b.lonceng.visibility = View.GONE
                    b.titikMerah.visibility = View.GONE
                }
                else{
//                    Toast.makeText(this, response, Toast.LENGTH_LONG)
//                        .show()
                    val jsonArray = JSONArray(response)
                    var notif = 0
                    for (x in 0..(jsonArray.length() - 1)) {
                        notif += 1
                    }
                    b.lonceng.visibility = View.VISIBLE
                    b.titikMerah.visibility = View.VISIBLE
                    b.jumlahNotif.setText(notif.toString())

                }

            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG)
                    .show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                //file yang dikirim ke web service
                hm.put("email", Global.email)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}