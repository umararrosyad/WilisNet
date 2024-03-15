package com.example.wilisnet.fragment.user

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wilisnet.Global
import com.example.wilisnet.R
import com.example.wilisnet.activity.user.DashboardActivity
import com.example.wilisnet.adapter.AdapterDataMenu
import com.example.wilisnet.adapter.AdapterDataVoucher
import com.example.wilisnet.databinding.FragHomeBinding
import com.github.lzyzsd.circleprogress.ArcProgress
import com.squareup.picasso.Picasso
import org.json.JSONArray
import java.text.DecimalFormat
import kotlin.collections.HashMap
import kotlin.reflect.typeOf


class HomeFragment : Fragment() {
    private lateinit var b : FragHomeBinding

    val ip = Global.ip
    val url = "http://$ip/wilisnet/data_dashboard.php"
    val url2 = "http://$ip/wilisnet/show_paket.php"
    val url3 = "http://$ip/wilisnet/show_voucher.php"
    val url4 = "http://$ip/wilisnet/show_pengajuan_user.php"

    val url5 = "http://$ip/wilisnet/mikrotik/routeros-api-master/examples/penggunaan.php"
    var id_paket = ""
    lateinit var menuAdapter: AdapterDataMenu
    val daftarMenu = mutableListOf<HashMap<String, String>>()

    var imageViewList = ArrayList<ImageView>()

    var kategori = "tidak ada"
    var nama_paket = "tidak ada"
    var status = "tidak ada"
    var alasan = "tidak ada"


    var penggunaan = false

    var bupload = "0,0"
    var bdownload = "0,0"


    lateinit var VoucherAdapter: AdapterDataVoucher
    val daftarVoucher = mutableListOf<HashMap<String, String>>()

    lateinit var thisParent : DashboardActivity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as DashboardActivity
        b = FragHomeBinding.inflate(inflater, container, false)

        return b.root
    }

    override fun onStart() {
        super.onStart()

        b.CardPenggunaan.visibility = View.GONE

        b.btnDetail2.setOnClickListener {
            if (penggunaan == false){
                val rotateAnimation = RotateAnimation(0f, 90f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
                rotateAnimation.duration = 50 // Durasi animasi dalam milidetik
                rotateAnimation.fillAfter = true // Menetapkan posisi ImageView setelah animasi selesai



//                val apa2 = b.txDownload.text.toString().split(" ")
//                val sWithDot2 =  apa2[0].replace(",", ".")
//                val targetValue2 = sWithDot2.toFloat()// Angka target
//
////                val targetValue = 200f// Angka target
//                val duration2 = 2000 // Durasi animasi dalam milidetik
//
//                val textView2 = b.txDownload
//
//                val valueAnimator2 = ValueAnimator.ofFloat(0F, targetValue2)// Animasi dari 0 ke targetValue
//                valueAnimator2.duration = duration.toLong() // Mengatur durasi animasi
//                valueAnimator2.addUpdateListener { animator ->
//                    val animatedValue = animator.animatedValue as Float
//                    val formattedValue = String.format("%.2f", animatedValue)
//                    textView2.text = "$formattedValue GB"
//                }
//                valueAnimator2.start()

                b.btnDetail.startAnimation(rotateAnimation)

                val targetValue =  bupload.replace(",", ".").toFloat()
//                val targetValue = 200f// Angka target
                val duration = 2000 // Durasi animasi dalam milidetik

                val textView = b.txUpload

                val valueAnimator = ValueAnimator.ofFloat(0F, targetValue)// Animasi dari 0 ke targetValue
                valueAnimator.duration = duration.toLong() // Mengatur durasi animasi
                valueAnimator.addUpdateListener { animator ->
                    val animatedValue = animator.animatedValue as Float
                    val formattedValue = String.format("%.2f", animatedValue)
                    textView.text = "$formattedValue GB"
                }
                valueAnimator.start()

                val targetValue2 =  bdownload.replace(",", ".").toFloat()
//                val targetValue = 200f// Angka target
                val duration2 = 2000 // Durasi animasi dalam milidetik

                val textView2 = b.txDownload

                val valueAnimator2 = ValueAnimator.ofFloat(0F, targetValue2)// Animasi dari 0 ke targetValue
                valueAnimator2.duration = duration2.toLong() // Mengatur durasi animasi
                valueAnimator2.addUpdateListener { animator ->
                    val animatedValue = animator.animatedValue as Float
                    val formattedValue = String.format("%.2f", animatedValue)
                    textView2.text = "$formattedValue GB"
                }
                valueAnimator2.start()



                b.CardPenggunaan.visibility = View.VISIBLE
                val arcProgress: ArcProgress = b.aa2
                val objectAnimator = ObjectAnimator.ofInt(arcProgress, "progress", 0, 100)
                objectAnimator.duration = 2000 // Durasi animasi (dalam milidetik)
                objectAnimator.start()

                val arcProgress2: ArcProgress = b.aa3
                val objectAnimator2 = ObjectAnimator.ofInt(arcProgress2, "progress", 0, 100)
                objectAnimator2.duration = 2000 // Durasi animasi (dalam milidetik)
                objectAnimator2.start()
                penggunaan = true
            }
            else{
                val rotateAnimation = RotateAnimation(90f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
                rotateAnimation.duration = 50 // Durasi animasi dalam milidetik
                rotateAnimation.fillAfter = true // Menetapkan posisi ImageView setelah animasi selesai

                b.btnDetail.startAnimation(rotateAnimation)
                b.CardPenggunaan.visibility = View.GONE
                penggunaan = false
            }
        }

        imageViewList = arrayListOf(
            b.imgTidak,
            b.imgDiajukan,
            b.imgDisetujui,
            b.imgDikerjakan,
            b.imgSelesai
        )



        thisParent.setLoading()
        getData()

        b.txpaketD2.text = Global.nama_paket




        menuAdapter = AdapterDataMenu(daftarMenu,thisParent)
        b.rePaket.layoutManager = LinearLayoutManager(thisParent, LinearLayoutManager.HORIZONTAL, false)
        b.rePaket.adapter = menuAdapter
        showDataPaket("")



        VoucherAdapter = AdapterDataVoucher(daftarVoucher,thisParent)
        b.reBelanjaVoucher.layoutManager = LinearLayoutManager(thisParent, LinearLayoutManager.HORIZONTAL, false)
        b.reBelanjaVoucher.adapter = VoucherAdapter
        showVoucher("")

        getPengajuan()

        b.CardPaket.setOnClickListener {

        }


        b.cardView21.setOnClickListener {
            b.Detail.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(thisParent, R.anim.keatas)
            b.LconformDetail.startAnimation(animation)

            if(status == "tidak ada"){
                b.txDetailPe.text = "Kamu Belum Terdaftar Memiliki Paket Internet Apapun Silahkan Melakukan Pengajuan Paket internet."
            }else if(status == "Diajukan"){
                b.txDetailPe.text = "Anda Telah Mengajukan $kategori untuk $nama_paket, Mohon Tunggu Pengajuan Anda Disetujui."
            }
            else if(status == "Ditolak"){
                b.txDetailPe.text = "Mohon Maaf, Pengajuan $kategori untuk $nama_paket harus kami tolak Dengan Alasan $alasan"
            }
            else if (status == "Disetujui"){
                if(kategori == "pasang baru"){
                    b.txDetailPe.text = "Selamat Pengajuan $kategori Kamu untuk $nama_paket Sudah Disetujui Silahkan Melunasi Tagihan Pemasangan Terlebih Dahulu."
                }else if(kategori == "ganti paket"){
                    b.txDetailPe.text = "Selamat Pengajuan $kategori Kamu untuk $nama_paket Sudah Disetujui Harap Menunggu Sampai Proses Pengerjaan Dimulai."
                }
            }else if(status == "Dikerjakan"){
                b.txDetailPe.text = "Sedikit Lagi..... Pengajuan $kategori Kamu Sudah Dikerjakan"
            }
            else if(status == "Selesai"){
                b.txDetailPe.text = "Selamat..... Pengajuan $kategori Kamu Untuk $nama_paket Sudah Selesai!"
            }
        }

        b.Detail.setOnClickListener {
            b.Detail.visibility = View.GONE
        }
        b.btnOk.setOnClickListener {
            b.Detail.visibility = View.GONE
        }



    }

    fun getData() {
        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                if (response == ""){

                }
                else{
//                    Toast.makeText(this, response, Toast.LENGTH_LONG)
//                        .show()
                    val jsonArray = JSONArray(response)

                    for (x in 0..(jsonArray.length() - 1)) {
//                        Global.email = b.edemail.text.toString()
                        val jsonObject = jsonArray.getJSONObject(x)
                        Global.id_paket =jsonObject.getString("id_paket")
                        Global.nama_paket = jsonObject.getString("nama_paket")
                        Global.kecepatan = jsonObject.getString("kecepatan")
                        Global.poin = jsonObject.getString("poin")
                        Global.ppp = jsonObject.getString("ppoe_name")

                        b.txpaketD2.text =jsonObject.getString("nama_paket")

                        if (Global.kecepatan == ""){
                            b.txkeD.text = "0"
                        }else{
                            b.txkeD.text = Global.kecepatan
                        }
                        b.txPoin.text = Global.poin
                        if(Global.ppp == ""){

                        }else{
                            getPenggunaan()
                        }

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

    fun getPengajuan() {
        val request = object : StringRequest(
            Request.Method.POST, url4,
            Response.Listener { response ->
                if (response == ""){
                    for (x in 0 until  imageViewList.size ){
                        if(x == 0){
                            imageViewList[x].setImageResource(R.drawable.centang1)
                        }else{
                            imageViewList[x].setImageResource(R.color.white)
                        }
                    }

                    kategori = "tidak ada"
                    nama_paket = "tidak ada"
                    status = "tidak ada"
                }
                else{
//                    Toast.makeText(thisParent, response, Toast.LENGTH_LONG)
//                        .show()
                    val jsonArray = JSONArray(response)

                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        nama_paket = jsonObject.getString("nama_paket")
                        kategori = jsonObject.getString("kategori")
                        status  = jsonObject.getString("status")
                        alasan  = jsonObject.getString("alasan")
                        var keterangan  = jsonObject.getString("keterangan").toUpperCase()
                       var nama_paket_lama  = jsonObject.getString("nama_paket_lama").toUpperCase()
                        var ketegoria = kategori.toUpperCase()
                        var nama_paketa = nama_paket.toUpperCase()
                        if(kategori == "Berhenti Langganan"){
                            b.txKategori.text = "$ketegoria $keterangan $nama_paket_lama"
                        }else{
                            b.txKategori.text = "$ketegoria $keterangan KE $nama_paketa"
                        }
                        Log.d("status", status)

                        if(status == "Diajukan"){
                            for (x in 0 until  imageViewList.size ){
                                if(x <= 1){
                                    imageViewList[x].setImageResource(R.drawable.centang1)
                                }else{
                                    imageViewList[x].setImageResource(R.color.white)
                                }
                            }
                        }
                        else if(status == "Disetujui"){
                            for (x in 0 until  imageViewList.size ){
                                if(x <=2){
                                    imageViewList[x].setImageResource(R.drawable.centang1)
                                }else{
                                    imageViewList[x].setImageResource(R.color.white)
                                }
                            }
                        }
                        else if(status == "Ditolak"){
                            for (x in 0 until  imageViewList.size ){
                                if(x <=2){
                                    imageViewList[x].setImageResource(R.drawable.ditolak)
                                }else{
                                    imageViewList[x].setImageResource(R.color.white)
                                }
                            }
                        }
                        else if(status == "Dikerjakan"){
                            for (x in 0 until  imageViewList.size ){
                                if(x <= 3){
                                    imageViewList[x].setImageResource(R.drawable.centang1)
                                }else{
                                    imageViewList[x].setImageResource(R.color.white)
                                }
                            }
                        }
                        else if(status == "Selesai"){
                            for (x in 0 until  imageViewList.size ){
                                imageViewList[x].setImageResource(R.drawable.centang1)

                            }
                        }
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

    fun showDataPaket(namaMhs: String) {
        val request = object : StringRequest(
            Request.Method.POST, url2,
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
            Request.Method.POST, url3,
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

    fun getPenggunaan() {
        val request = object : StringRequest(
            Request.Method.POST, url5,
            Response.Listener { response ->
                val penggunaan = response

                val parts = penggunaan.split("/")
                val upload = parts[0]
                val download = parts[1]

                val tupload = convertBytesToGigabytes(upload.toLong())
                val tdownload = convertBytesToGigabytes(download.toLong())

                bupload = tupload
                bdownload = tdownload



                b.txUpload.setText("$tupload GB")
                b.txDownload.setText("$tdownload GB")

            },
            Response.ErrorListener { _ ->
                Toast.makeText(thisParent, "Terjadi kesalahan koneksi ke server", Toast.LENGTH_LONG)
                    .show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                //file yang dikirim ke web service
                hm.put("ppoe", Global.ppp)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(thisParent)
        queue.add(request)
    }

    fun convertBytesToGigabytes(bytes: Long): String {
        val gigabytes = bytes.toDouble() / (1024 * 1024 * 1024)
        val decimalFormat = DecimalFormat("#.##")
        return decimalFormat.format(gigabytes)
    }
}