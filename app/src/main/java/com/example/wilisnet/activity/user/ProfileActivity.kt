package com.example.wilisnet.activity.user

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wilisnet.Global
import com.example.wilisnet.R
import com.example.wilisnet.databinding.ActivityProfileBinding
import com.example.wilisnet.helper.MediaHealper
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap

class ProfileActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var b : ActivityProfileBinding

    val url = "http://${Global.ip}/wilisnet/data_dashboard.php"
    val url2 = "http://${Global.ip}/wilisnet/kelola_foto.php"
    lateinit var mediaHealper: MediaHealper

    var imStr = ""
    var namaFile = ""
    var fileUri = Uri.parse("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityProfileBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        b.loading.visibility = View.VISIBLE
        mediaHealper = MediaHealper(this)

        getData()

        b.btnProfile.setOnClickListener(this)
        b.btnPass.setOnClickListener(this)
        b.btnHistory.setOnClickListener(this)
        b.btnNotifikasi.setOnClickListener(this)


        b.btnEditimg.setOnClickListener {
            b.editFoto.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(this, R.anim.keatas2)
            b.cEdit.startAnimation(animation)

        }

        b.editFoto.setOnClickListener{
            b.editFoto.visibility = View.GONE
        }

        b.btnGaleri.setOnClickListener{
            namaFile = mediaHealper.getOutputMediaFilename()
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent,mediaHealper.getRcGallery())
            b.editFoto.visibility = View.GONE

        }

        b.btnHapus.setOnClickListener{
            queryInsertUpdateDelete("delete")
            Picasso.get()
                .load(R.drawable.userr) // Ganti dengan ID Drawable gambar yang diinginkan
                .into(b.imgProfil)
        }


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
                        b.txnamaP.text = jsonObject.getString("nama").uppercase(Locale.ROOT)
                        Global.nama =jsonObject.getString("nama")
                        Global.id_paket =jsonObject.getString("id_paket")
                        Global.nama_paket = jsonObject.getString("nama_paket")
                        Global.kecepatan = jsonObject.getString("kecepatan")
                        Global.poin = jsonObject.getString("poin")
                        Global.profil = jsonObject.getString("url")
                        Global.alamat = jsonObject.getString("alamat")
                        Global.phone = jsonObject.getString("phone")
                        b.txEmailP.text = Global.email
                        if(Global.profil == "null"){
                            Picasso.get()
                                .load(R.drawable.userr) // Ganti dengan ID Drawable gambar yang diinginkan
                                .into(b.imgProfil)
                        }else{

                            Picasso.get().load(Global.profil).into(b.imgProfil);

                        }

                        b.loading.visibility = View.GONE

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
                hm.put("email", Global.email)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_profile->{
                val intent = Intent(this, ProfileSettingActivity::class.java)
                startActivity(intent)
            }
//            R.id.btn_berhenti->{
//
//            }
            R.id.btn_history->{
                val intent = Intent(this, TransaksiActivity ::class.java)
                startActivity(intent)
            }
            R.id.btn_pass->{
                val intent = Intent(this, PasswordActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun queryInsertUpdateDelete(mode : String){
        val request = object : StringRequest(Method.POST,url2,
            Response.Listener{ response ->
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if(error.equals("000")){

                }else{
                    Toast.makeText(this,"Operasi Gagal", Toast.LENGTH_LONG).show()
                }


            },
            Response.ErrorListener { _ ->

                Toast.makeText(this,"Tidak dapat terhubung ke server CRUD", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>() //file yang dikirim ke web service
                when(mode){
                    "insert"->{
                        hm.put("mode","insert")
                        hm.put("email",Global.email)
                        hm.put("image",imStr)
                        hm.put("file",namaFile)

                    }
                    "update"->{
                        hm.put("mode","Update")
                        hm.put("email",Global.email)
                        hm.put("image",imStr)
                        hm.put("file",namaFile)
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == mediaHealper.getRcGallery()){
                imStr = mediaHealper.getBitmapToString(data!!.data,b.imgProfil)
            }
            queryInsertUpdateDelete("insert")
        }

    }
}