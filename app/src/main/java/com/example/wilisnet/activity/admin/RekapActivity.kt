package com.example.wilisnet.activity.admin

import android.app.DatePickerDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.NumberPicker
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wilisnet.Global
import com.example.wilisnet.R
import com.example.wilisnet.adapter.AdapterDailyAdm
import com.example.wilisnet.adapter.AdapterDataTagihan
import com.example.wilisnet.adapter.AdapterTransSemua
import com.example.wilisnet.databinding.ActivityDashboardAdmBinding
import com.example.wilisnet.databinding.ActivityRekapBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import org.json.JSONArray
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class RekapActivity : AppCompatActivity() {
    lateinit var b : ActivityRekapBinding

    val url = "http://${Global.ip}/wilisnet/show_trans_semua.php"
    val ur2 = "http://${Global.ip}/wilisnet/show_trans_harian.php"
    val url3= "http://${Global.ip}/wilisnet/show_trans_bulanan.php"
    var tgl = ""
    var langkah = "semua"
    var step = "semua"


    var tahun = 2023

    lateinit var transAdapter: AdapterTransSemua
    val daftarTrans = mutableListOf<HashMap<String, String>>()

    var waktu = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityRekapBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)
        b.loading.visibility = View.VISIBLE

        transAdapter = AdapterTransSemua(daftarTrans,this)
        b.reRecap.layoutManager = LinearLayoutManager(this)
        b.reRecap.adapter = transAdapter
        showDataMenu("")


        b.imageView34.setOnClickListener {
            finish()
        }

        b.LCetak.setOnClickListener {
            b.LCetak.visibility = View.GONE

        }


        b.btnIyaCetak.setOnClickListener {
            val url = "http://${Global.ip}/penjualan/cetak/$step/$waktu/"
            cetak(url)
        }

        b.btnTambah.setOnClickListener {
            tahun = tahun + 1
            b.txTahunTahun.text = tahun.toString()
        }

        b.btnKurang.setOnClickListener {
            tahun = tahun - 1
            b.txTahunTahun.text = tahun.toString()
        }




        b.btnTgl.setOnClickListener {
            if(langkah == "harian"){

                b.LTahun.visibility = View.VISIBLE

                b.btnIyaTahun.setOnClickListener {
                    showDataharian(tahun.toString())
                    b.LTahun.visibility = View.GONE

                    b.txtxbiasa.setText("Tahun $tahun")
                }
            }else{
                b.LTahun.visibility = View.VISIBLE

                b.btnIyaTahun.setOnClickListener {
                    var tahun = tahun.toString()
                    b.LTahun.visibility = View.GONE
                    val popupMenu = PopupMenu(this,b.btnTgl )
                    popupMenu.inflate(R.menu.menu_bulan)

                    popupMenu.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.januari -> {
                                showDataBulanan(tahun,"1")
                                b.loading.visibility = View.VISIBLE
                                b.txtxbiasa.setText("Bulan January")
                                true
                            }
                            R.id.februari -> {
                                showDataBulanan(tahun,"2")
                                b.loading.visibility = View.VISIBLE
                                b.txtxbiasa.setText("Bulan Februari")
                                true
                            }
                            R.id.maret -> {
                                showDataBulanan(tahun,"3")
                                b.loading.visibility = View.VISIBLE
                                b.txtxbiasa.setText("Bulan Maret")
                                true
                            }
                            R.id.april -> {
                                showDataBulanan(tahun,"4")
                                b.loading.visibility = View.VISIBLE
                                b.txtxbiasa.setText("Bulan April")
                                true
                            }
                            R.id.mei -> {
                                showDataBulanan(tahun,"5")
                                b.loading.visibility = View.VISIBLE
                                b.txtxbiasa.setText("Bulan Mei")

                                true
                            }
                            R.id.juni -> {
                                showDataBulanan(tahun,"6")
                                b.loading.visibility = View.VISIBLE
                                b.txtxbiasa.setText("Bulan Juni")

                                true
                            }
                            R.id.juli -> {
                                showDataBulanan(tahun,"7")
                                b.loading.visibility = View.VISIBLE
                                b.txtxbiasa.setText("Bulan Juli")

                                true
                            }
                            R.id.agustus -> {
                                showDataBulanan(tahun,"8")
                                b.loading.visibility = View.VISIBLE
                                b.txtxbiasa.setText("Bulan Agustus")

                                true
                            }
                            R.id.september -> {
                                showDataBulanan(tahun,"9")
                                b.loading.visibility = View.VISIBLE
                                b.txtxbiasa.setText("Bulan September")

                                true
                            }
                            R.id.oktober -> {
                                showDataBulanan(tahun,"10")
                                b.loading.visibility = View.VISIBLE
                                b.txtxbiasa.setText("Bulan Oktober")

                                true
                            }
                            R.id.november -> {
                                showDataBulanan(tahun,"11")
                                b.loading.visibility = View.VISIBLE
                                b.txtxbiasa.setText("Bulan November")

                                true
                            }
                            R.id.desember -> {
                                showDataBulanan(tahun,"12")
                                b.loading.visibility = View.VISIBLE
                                b.txtxbiasa.setText("Bulan Desember")

                                true
                            }
                            else -> false
                        }
                    }

                    popupMenu.show()
                }


            }

        }


        b.cbulan.setOnClickListener {
            b.cbulan.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue))
            b.csemua.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
            b.charian.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))

            b.txBulanan.setTextColor(ContextCompat.getColor(this, R.color.white))
            b.txHarian.setTextColor(ContextCompat.getColor(this, R.color.black))
            b.txSemua.setTextColor(ContextCompat.getColor(this, R.color.black))
            b.txtxsemua.visibility = View.GONE
            b.txtotalSemua.visibility = View.GONE

            b.txtxbiasa.visibility = View.VISIBLE
            b.txtotal.visibility = View.VISIBLE

            b.btnTgl.visibility = View.VISIBLE

            langkah = "bulanan"
            step = "bulanan"
            val calendar = Calendar.getInstance()
            val month = calendar.get(Calendar.MONTH) + 1
            val year = calendar.get(Calendar.YEAR)// Perhatikan penambahan +1 karena indeks bulan dimulai dari 0
            val monthNames = arrayOf(
                "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli",
                "Agustus", "September", "Oktober", "November", "Desember"
            )

            val monthName = monthNames[month-1]
            b.txtxbiasa.setText("Bulan $monthName")
            b.loading.visibility = View.VISIBLE

            showDataBulanan("$year","${month}")
        }
        b.charian.setOnClickListener {
            b.cbulan.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
            b.csemua.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
            b.charian.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue))

            b.txBulanan.setTextColor(ContextCompat.getColor(this, R.color.black))
            b.txHarian.setTextColor(ContextCompat.getColor(this, R.color.white))
            b.txSemua.setTextColor(ContextCompat.getColor(this, R.color.black))

            b.txtxsemua.visibility = View.GONE
            b.txtotalSemua.visibility = View.GONE

            b.txtxbiasa.visibility = View.VISIBLE
            b.txtotal.visibility = View.VISIBLE

            b.btnTgl.visibility = View.VISIBLE

            langkah = "harian"

            step = "tahunan"

            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1 // Perhatikan penambahan +1 karena indeks bulan dimulai dari 0
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            val today = "$year:$month:$dayOfMonth"
            val selectedDate = "$year"
            b.txtxbiasa.setText("Tahun $selectedDate")
            b.loading.visibility = View.VISIBLE
            showDataharian("$year")

        }
        b.csemua.setOnClickListener {
            b.cbulan.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
            b.csemua.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue))
            b.charian.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))

            b.txBulanan.setTextColor(ContextCompat.getColor(this, R.color.black))
            b.txHarian.setTextColor(ContextCompat.getColor(this, R.color.black))
            b.txSemua.setTextColor(ContextCompat.getColor(this, R.color.white))
            b.txtxsemua.visibility = View.VISIBLE
            b.txtotalSemua.visibility = View.VISIBLE

            b.txtxbiasa.visibility = View.GONE
            b.txtotal.visibility = View.GONE

            b.btnTgl.visibility = View.GONE
            langkah = "semua"
            step = "semua"
            b.loading.visibility = View.VISIBLE
            showDataMenu("")

        }
    }

    fun showDataMenu (status: String) {
        val request = @RequiresApi(Build.VERSION_CODES.O)
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                daftarTrans.clear()
                //mengambil data dari dqatabse melalui web service
                if (response == ""){
                    b.imgNo.visibility = View.VISIBLE
                    b.reRecap.visibility = View.GONE
                    b.txtotalSemua.setText("Rp.0")
                    b.loading.visibility = View.GONE
                }
                else{
                    b.loading.visibility = View.GONE
                    b.imgNo.visibility = View.GONE
                    b.reRecap.visibility = View.VISIBLE
                    var jum = 0
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        var mhs = HashMap<String, String>()
                        mhs.put("waktu", jsonObject.getString("waktu"))
                        mhs.put("jumlah", jsonObject.getString("jumlah"))

                        jum += jsonObject.getString("jumlah").toInt()
                        daftarTrans.add(mhs)

                    }
                    transAdapter.notifyDataSetChanged()
                    val decimalFormat = DecimalFormat("#,###")
                    val formattedNumber = decimalFormat.format(jum)
                    b.txtotalSemua.setText("Rp. $formattedNumber")
                }

            },
            Response.ErrorListener { _ ->
                Toast.makeText(this, "Terjadi kesalahan koneksi ke server", Toast.LENGTH_LONG)
                    .show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                //file yang dikirim ke web service
                hm.put("status", status)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }

    fun showDataharian (tanggal: String) {
        val request = @RequiresApi(Build.VERSION_CODES.O)
        object : StringRequest( 
            Request.Method.POST, ur2,
            Response.Listener { response ->
                daftarTrans.clear()
                //mengambil data dari dqatabse melalui web service
                if (response == ""){

                    b.imgNo.visibility = View.VISIBLE
                    b.reRecap.visibility = View.GONE
                    b.txtotal.setText("Rp.0")
                    b.loading.visibility = View.GONE
                }
                else{
                    b.imgNo.visibility = View.GONE
                    b.reRecap.visibility = View.VISIBLE
                    b.loading.visibility = View.GONE
                    var jum = 0
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        var mhs = HashMap<String, String>()
                        mhs.put("waktu", jsonObject.getString("waktu"))
                        mhs.put("jumlah", jsonObject.getString("jumlah"))

                        jum += jsonObject.getString("jumlah").toInt()
                        daftarTrans.add(mhs)

                    }
                    transAdapter.notifyDataSetChanged()
                    val decimalFormat = DecimalFormat("#,###")
                    val formattedNumber = decimalFormat.format(jum)
                    b.txtotal.setText("Rp. $formattedNumber")
                }

            },
            Response.ErrorListener { _ ->
                Toast.makeText(this, "Terjadi kesalahan koneksi ke server", Toast.LENGTH_LONG)
                    .show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                //file yang dikirim ke web service
                hm.put("tahun", tanggal)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }

    fun showDataBulanan (tahun: String, bulan: String) {
        val request = @RequiresApi(Build.VERSION_CODES.O)
        object : StringRequest(
            Request.Method.POST, url3,
            Response.Listener { response ->
                daftarTrans.clear()
                //mengambil data dari dqatabse melalui web service
                if (response == ""){

                    b.imgNo.visibility = View.VISIBLE
                    b.reRecap.visibility = View.GONE
                    b.loading.visibility = View.GONE
                    b.txtotal.setText("Rp.0")
                }
                else{
                    b.imgNo.visibility = View.GONE
                    b.reRecap.visibility = View.VISIBLE
                    b.loading.visibility = View.GONE
                    var jum = 0
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        var mhs = HashMap<String, String>()
                        mhs.put("waktu", jsonObject.getString("waktu"))
                        mhs.put("jumlah", jsonObject.getString("jumlah"))

                        jum += jsonObject.getString("jumlah").toInt()
                        daftarTrans.add(mhs)

                    }
                    transAdapter.notifyDataSetChanged()
                    val decimalFormat = DecimalFormat("#,###")
                    val formattedNumber = decimalFormat.format(jum)
                    b.txtotal.setText("Rp. $formattedNumber")
                }

            },
            Response.ErrorListener { _ ->
                Toast.makeText(this, "Terjadi kesalahan koneksi ke server", Toast.LENGTH_LONG)
                    .show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                //file yang dikirim ke web service
                hm.put("tahun", tahun)
                hm.put("bulan", bulan)

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }


    fun cetak (url :String) {
        val request = @RequiresApi(Build.VERSION_CODES.O)
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                b.loading.visibility = View.GONE
                b.LCetak.visibility = View.GONE

                if(response == "gagal"){
                    Toast.makeText(this, "Gagal mendownload PDF", Toast.LENGTH_LONG)
                        .show()
                }else{
                    Log.d("Response", response)
                    // Panggil fungsi untuk membuat file PDF setelah menerima respons dari server
                    val pdfPath = response

                    val currentTime = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
                    val fileName = "Detail_Transaksi_$currentTime.pdf"

                    // Download file PDF dari server menggunakan path yang diterima
                    val request = DownloadManager.Request(Uri.parse(pdfPath))
                        .setTitle("Laporan PDF")
                        .setDescription("Mengunduh laporan PDF")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

                    val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    downloadManager.enqueue(request)

                    Toast.makeText(this, "Mengunduh laporan PDF...", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG)
                    .show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                //file yang dikirim ke web service
//                hm.put("tahun", tahun)
//                hm.put("bulan", bulan)

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }
}