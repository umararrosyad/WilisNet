package com.example.wilisnet.fragment.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wilisnet.Global
import com.example.wilisnet.activity.nonuser.OtploginActivity
import com.example.wilisnet.activity.user.DashboardActivity
import com.example.wilisnet.activity.user.PaketActivity
import com.example.wilisnet.activity.user.VoucherActivity
import com.example.wilisnet.adapter.AdapterDataMenu
import com.example.wilisnet.adapter.AdapterDataTagihan
import com.example.wilisnet.adapter.AdapterDataVoucher
import com.example.wilisnet.databinding.FragmentBelanjaBinding
import com.example.wilisnet.databinding.FragmentTagihanBinding
import org.json.JSONArray

class BelanjaFragment : Fragment() {
    private lateinit var b : FragmentBelanjaBinding
    val ip = Global.ip
    val url = "http://$ip/wilisnet/show_paket.php"
    val url2 = "http://$ip/wilisnet/show_voucher.php"

    lateinit var menuAdapter: AdapterDataMenu
    val daftarMenu = mutableListOf<HashMap<String, String>>()

    lateinit var VoucherAdapter: AdapterDataVoucher
    val daftarVoucher = mutableListOf<HashMap<String, String>>()
    lateinit var thisParent : DashboardActivity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as DashboardActivity
        b = FragmentBelanjaBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onStart() {
        super.onStart()

        thisParent.setLoading()

        b.txpaketD.text = Global.nama_paket
        if (Global.kecepatan == ""){
            b.txkeD.text = "0"
        }else{
            b.txkeD.text = Global.kecepatan
        }
        b.txPoin.text = Global.poin


        menuAdapter = AdapterDataMenu(daftarMenu,thisParent)
        b.reBelanjaPaket.layoutManager = LinearLayoutManager(thisParent, LinearLayoutManager.HORIZONTAL, false)
        b.reBelanjaPaket.adapter = menuAdapter
        showDataPaket("")

        VoucherAdapter = AdapterDataVoucher(daftarVoucher,thisParent)
        b.reBelanjaVoucher.layoutManager = LinearLayoutManager(thisParent, LinearLayoutManager.HORIZONTAL, false)
        b.reBelanjaVoucher.adapter = VoucherAdapter
        showVoucher("")

        b.txDetail.setOnClickListener {
            val intent = Intent(thisParent, VoucherActivity::class.java)
            startActivity(intent)
        }
        b.txDetail3.setOnClickListener {
            val intent = Intent(thisParent, PaketActivity::class.java)
            startActivity(intent)
        }
    }

    fun showDataPaket(namaMhs: String) {
        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                daftarMenu.clear()
                if (response == ""){

                }
                else{
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        var mhs = HashMap<String, String>()
                        mhs.put("id_paket", jsonObject.getString("id_paket"))
                        mhs.put("nama_paket", jsonObject.getString("nama_paket"))
                        mhs.put("harga", jsonObject.getString("harga"))
                        mhs.put("kecepatan", jsonObject.getString("kecepatan"))
                        mhs.put("url", jsonObject.getString("url"))
                        daftarMenu.add(mhs)
                    }
                    menuAdapter.notifyDataSetChanged()
                }

            },
            Response.ErrorListener { _ ->
                Toast.makeText(thisParent, "Terjadi kesalahan koneksi ke server", Toast.LENGTH_LONG)
                    .show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                //file yang dikirim ke web service
                hm.put("nama_menu", namaMhs)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(thisParent)
        queue.add(request)

    }

    fun showVoucher(namaMhs: String) {
        val request = object : StringRequest(
            Request.Method.POST, url2,
            Response.Listener { response ->
                daftarVoucher.clear()
                if (response == ""){

                }
                else{
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        var mhs = HashMap<String, String>()
                        mhs.put("id_voucher", jsonObject.getString("id_voucher"))
                        mhs.put("nama_voucher", jsonObject.getString("nama_voucher"))
                        mhs.put("durasi", jsonObject.getString("durasi"))
                        mhs.put("harga", jsonObject.getString("harga"))
                        mhs.put("besar_paket", jsonObject.getString("besar_paket"))
                        mhs.put("pengguna", jsonObject.getString("pengguna"))
                        mhs.put("url", jsonObject.getString("url"))
                        daftarVoucher.add(mhs)
                    }
                    VoucherAdapter.notifyDataSetChanged()

                    thisParent.hideloading()
                }

            },
            Response.ErrorListener { _ ->
                Toast.makeText(thisParent, "Terjadi kesalahan koneksi ke server", Toast.LENGTH_LONG)
                    .show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                //file yang dikirim ke web service
                hm.put("nama_menu", namaMhs)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(thisParent)
        queue.add(request)

    }
}