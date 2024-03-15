package com.example.wilisnet.fragment.user

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wilisnet.Global
import com.example.wilisnet.activity.user.DashboardActivity
import com.example.wilisnet.adapter.AdapterDataMenu
import com.example.wilisnet.adapter.AdapterDataTagihan
import com.example.wilisnet.databinding.FragmentTagihanBinding
import org.json.JSONArray
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class TagihanFragment : Fragment() {
    private lateinit var b : FragmentTagihanBinding
    val ip = Global.ip
    val url = "http://$ip/wilisnet/show_tagihan.php"

    lateinit var menuAdapter: AdapterDataTagihan
    val daftarMenu = mutableListOf<HashMap<String, String>>()
    lateinit var thisParent : DashboardActivity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as DashboardActivity
        b = FragmentTagihanBinding.inflate(inflater, container, false)
        return b.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        thisParent.setLoading()

        b.txPaket.text = Global.nama_paket

        if(Global.stpembayaran == "Sudah Bayar"){

        }
        else if(Global.stpembayaran == "Pending"){
            b.txtenggat.text = "Pembayaran Pending"
        }
        else{

        }
        b.txtenggat
        menuAdapter = AdapterDataTagihan(daftarMenu,thisParent)
        b.reTagihan.layoutManager = LinearLayoutManager(thisParent)
        b.reTagihan.adapter = menuAdapter
        showDataMenu()

    }

    var count = 200

    fun showDataMenu() {
        val request = @RequiresApi(Build.VERSION_CODES.O)
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                daftarMenu.clear()
                //mengambil data dari dqatabse melalui web service
                if (response == ""){
                    b.txtenggat.text = "Tidak ada Tagihan"
                    b.noBill.visibility = View.VISIBLE
                    thisParent.hideloading()
                }
                else{
                    count = 0
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        var mhs = HashMap<String, String>()
                        mhs.put("id", jsonObject.getString("id"))
                        mhs.put("id_transaksi", jsonObject.getString("id_transaksi"))
                        mhs.put("status", jsonObject.getString("status"))
                        mhs.put("nama", jsonObject.getString("nama_paket"))
                        mhs.put("kecepatan", jsonObject.getString("kecepatan"))
                        mhs.put("harga", jsonObject.getString("harga"))
                        mhs.put("periode", jsonObject.getString("periode"))
                        daftarMenu.add(mhs)
                        val status = jsonObject.getString("status")
                        if (status == "Belum Bayar"){
                            count = count+1
                        }

                    }
                    menuAdapter.notifyDataSetChanged()
                    b.noBill.visibility = View.GONE
                    thisParent.hideloading()

                    if(count == 0){
                        b.txtenggat.text = "Pembayaran Selesai"
                    }else if(count == 1){
                        val today = LocalDate.now()

                        // Inisialisasi variabel targetDate
                        var targetDate: LocalDate

                        // Cek kondisi tanggal saat ini
                        if (today.dayOfMonth <= 6) {
                            // Jika tanggal saat ini 1-6, cari tanggal 7 bulan ini
                            targetDate = today.withDayOfMonth(7)
                        } else if (today.dayOfMonth == 7) {
                            // Jika tanggal saat ini adalah tanggal 7, gunakan tanggal saat ini
                            targetDate = today
                        } else {
                            // Jika tanggal saat ini 8 ke atas, cari tanggal 7 bulan depan
                            targetDate = today.plusMonths(1).withDayOfMonth(7)
                        }

                        // Menghitung selisih hari antara tanggal saat ini dengan targetDate
                        val daysRemaining = ChronoUnit.DAYS.between(today, targetDate)

                        b.txtenggat.text = "Tenggat Dalam $daysRemaining hari"
                    }else{
                        b.txtenggat.text = "Sudah Lewat Tenggat"
                    }
                }

            },
            Response.ErrorListener { _ ->
                Toast.makeText(thisParent, "Terjadi kesalahan koneksi ke server", Toast.LENGTH_LONG)
                    .show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                //file yang dikirim ke web service
                hm.put("email", Global.email)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(thisParent)
        queue.add(request)

    }


}