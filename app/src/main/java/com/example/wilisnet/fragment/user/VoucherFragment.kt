package com.example.wilisnet.fragment.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wilisnet.Global
import com.example.wilisnet.R
import com.example.wilisnet.activity.user.DashboardActivity
import com.example.wilisnet.adapter.AdapterDataVoucher3
import com.example.wilisnet.databinding.FragmentVoucerBinding
import org.json.JSONArray

class VoucherFragment : Fragment(),View.OnClickListener{
    private lateinit var b : FragmentVoucerBinding

    lateinit var VoucherAdapter: AdapterDataVoucher3
    val daftarVoucher = mutableListOf<HashMap<String, String>>()
    lateinit var thisParent : DashboardActivity

    val ip = Global.ip
    val url = "http://$ip/wilisnet/show_myvoucher.php"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as DashboardActivity
        b = FragmentVoucerBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onStart() {
        super.onStart()
        thisParent.setLoading()

        VoucherAdapter = AdapterDataVoucher3(daftarVoucher,thisParent)
        b.reMyvoucher.layoutManager = LinearLayoutManager(thisParent, LinearLayoutManager.HORIZONTAL, false)
        b.reMyvoucher.adapter = VoucherAdapter
        showVoucher("Belum Terpakai","Sudah Bayar")

        b.btnTersedia.setOnClickListener(this)
        b.btnTerpakai.setOnClickListener(this)
        b.btnPending.setOnClickListener(this)
        b.btnGagal.setOnClickListener(this)
    }

    fun showVoucher(penggunaan :String,status :String) {
        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                daftarVoucher.clear()
                if (response == ""){
                    b.noVoucher.visibility = View.VISIBLE
                    b.reMyvoucher.visibility= View.GONE
                    thisParent.hideloading()

                }
                else{
//                    Toast.makeText(thisParent, response, Toast.LENGTH_LONG)
//                        .show()
                    b.reMyvoucher.visibility= View.VISIBLE
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        var mhs = HashMap<String, String>()
                        mhs.put("id", jsonObject.getString("id"))
                        mhs.put("id_voucher", jsonObject.getString("id_voucher"))
                        mhs.put("nama_voucher", jsonObject.getString("nama_voucher"))
                        mhs.put("durasi", jsonObject.getString("durasi"))
                        mhs.put("besar_paket", jsonObject.getString("besar_paket"))
                        mhs.put("user", jsonObject.getString("user"))
                        mhs.put("status_penggunaan", jsonObject.getString("status_penggunaan"))
                        mhs.put("status", jsonObject.getString("status"))
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
                hm.put("email", Global.email)
                hm.put("penggunaan", penggunaan)
                hm.put("status", status)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(thisParent)
        queue.add(request)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_tersedia->{
                b.noVoucher.visibility = View.GONE
                b.btnTersedia.setCardBackgroundColor(ContextCompat.getColor(thisParent, R.color.blue))
                b.btnTerpakai.setCardBackgroundColor(ContextCompat.getColor(thisParent, R.color.white))
                b.btnGagal.setCardBackgroundColor(ContextCompat.getColor(thisParent, R.color.white))
                b.btnPending.setCardBackgroundColor(ContextCompat.getColor(thisParent, R.color.white))

                b.txTersedia.setTextColor(ContextCompat.getColor(thisParent, R.color.white))
                b.txTerpakai.setTextColor(ContextCompat.getColor(thisParent, R.color.black))
                b.txGagal.setTextColor(ContextCompat.getColor(thisParent, R.color.black))
                b.txPending.setTextColor(ContextCompat.getColor(thisParent, R.color.black))

                b.txHeadline.setText("Voucher Saya")
                showVoucher("Belum Terpakai","Sudah Bayar")

            }
            R.id.btn_terpakai->{
                b.noVoucher.visibility = View.GONE
                b.btnTersedia.setCardBackgroundColor(ContextCompat.getColor(thisParent, R.color.white))
                b.btnTerpakai.setCardBackgroundColor(ContextCompat.getColor(thisParent, R.color.blue))
                b.btnGagal.setCardBackgroundColor(ContextCompat.getColor(thisParent, R.color.white))
                b.btnPending.setCardBackgroundColor(ContextCompat.getColor(thisParent, R.color.white))

                b.txTersedia.setTextColor(ContextCompat.getColor(thisParent, R.color.black))
                b.txTerpakai.setTextColor(ContextCompat.getColor(thisParent, R.color.white))
                b.txGagal.setTextColor(ContextCompat.getColor(thisParent, R.color.black))
                b.txPending.setTextColor(ContextCompat.getColor(thisParent, R.color.black))
                b.txHeadline.setText("Voucher Terpakai")
                showVoucher("Dibuat","Sudah Bayar")



            }
            R.id.btn_pending->{
                b.noVoucher.visibility = View.GONE
                b.btnTersedia.setCardBackgroundColor(ContextCompat.getColor(thisParent, R.color.white))
                b.btnTerpakai.setCardBackgroundColor(ContextCompat.getColor(thisParent, R.color.white))
                b.btnGagal.setCardBackgroundColor(ContextCompat.getColor(thisParent, R.color.white))
                b.btnPending.setCardBackgroundColor(ContextCompat.getColor(thisParent, R.color.blue))

                b.txTersedia.setTextColor(ContextCompat.getColor(thisParent, R.color.black))
                b.txTerpakai.setTextColor(ContextCompat.getColor(thisParent, R.color.black))
                b.txGagal.setTextColor(ContextCompat.getColor(thisParent, R.color.black))
                b.txPending.setTextColor(ContextCompat.getColor(thisParent, R.color.white))

                b.txHeadline.setText("Voucher Pending Pembayaran")
                showVoucher("Belum Terpakai","Pending")


            }
            R.id.btn_gagal->{
                b.noVoucher.visibility = View.GONE
                b.btnTersedia.setCardBackgroundColor(ContextCompat.getColor(thisParent, R.color.white))
                b.btnTerpakai.setCardBackgroundColor(ContextCompat.getColor(thisParent, R.color.white))
                b.btnGagal.setCardBackgroundColor(ContextCompat.getColor(thisParent, R.color.blue))
                b.btnPending.setCardBackgroundColor(ContextCompat.getColor(thisParent, R.color.white))

                b.txTersedia.setTextColor(ContextCompat.getColor(thisParent, R.color.black))
                b.txTerpakai.setTextColor(ContextCompat.getColor(thisParent, R.color.black))
                b.txGagal.setTextColor(ContextCompat.getColor(thisParent, R.color.white))
                b.txPending.setTextColor(ContextCompat.getColor(thisParent, R.color.black))
                b.txHeadline.setText("Voucher Gagal Dibeli")
                showVoucher("Belum Terpakai","Gagal")



            }
        }
    }


}