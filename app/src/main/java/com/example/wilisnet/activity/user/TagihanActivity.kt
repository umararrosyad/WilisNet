package com.example.wilisnet.activity.user

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wilisnet.Global
import com.example.wilisnet.R
import com.example.wilisnet.databinding.ActivityPaketBinding
import com.example.wilisnet.databinding.ActivityTagihanBinding
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.snap.Gopay
import com.midtrans.sdk.corekit.models.snap.Shopeepay
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class TagihanActivity : AppCompatActivity(), TransactionFinishedCallback {

    private lateinit var b : ActivityTagihanBinding
    val ip = Global.ip
    val url = "http://$ip/wilisnet/insert_transaksi.php"
    val url2 = "http://$ip/wilisnet/update_poin.php"


    var id = ""
    var nama = ""
    var kecepatan = ""
    var id_transaksi = ""
    var status = ""
    var periode = ""
    var metode = ""
    var harga = ""
    var kode = ""

    var harga_fix = ""

    var sisa_poin = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityTagihanBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)


        val intent = intent
        id = intent.getStringExtra("id").toString()
        id_transaksi = intent.getStringExtra("id_transaksi").toString()
        status = intent.getStringExtra("status").toString()
        periode = intent.getStringExtra("periode").toString()
        metode = intent.getStringExtra("metode").toString()
        harga = intent.getStringExtra("harga").toString()
        kode = intent.getStringExtra("kode").toString()
        nama = intent.getStringExtra("nama").toString()
        kecepatan = intent.getStringExtra("kecepatan").toString()
        val decimalFormat = DecimalFormat("#,###")
        val formattedNumber = decimalFormat.format(harga.toInt())
        b.txHargaa.text = "Rp $formattedNumber"
        b.txHargaD.text = "Rp $formattedNumber"
        b.txperiode.text = periode
        b.txnamaT.text = nama
        b.txkecepatan.text = kecepatan
        b.txStatusT2.text = status
        b.txkode.text = kode

        if(status == "Belum Bayar" ){
            b.cTransaksi.visibility = View.GONE
            b.cJudulTransaksi.visibility = View.GONE
        }else if(status =="Sudah Bayar"){
            b.Cpembayran.visibility = View.GONE
        }

        if (b.txStatusT2.text.toString() == "Belum Bayar"){
            b.imgRadar.setImageResource(R.drawable.bulat_merah);
        }else{
            b.imgRadar.setImageResource(R.drawable.bulat_hijau);
        }

        harga_fix = harga

        b.txpoin.setText(Global.poin)
        b.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val poin = Global.poin.toInt()
                var harga = harga.toInt()
                sisa_poin = poin

                if (harga - poin > 10000) {
                    harga -= poin
                    sisa_poin = 0
                } else if (harga - poin <=10000) {
                    sisa_poin = poin - (harga-10000)
                    harga = 10000
                }

                b.txpoin.text= "$sisa_poin"
                val formattedNumber = NumberFormat.getNumberInstance(Locale("id")).format(harga)
                b.txHargaD.text = "Rp.$formattedNumber"
                harga_fix = harga.toString()
            } else {
                val poin = Global.poin.toInt()
                var harga1 = harga.toInt()
                b.txpoin.text= "$poin"
                val formattedNumber = NumberFormat.getNumberInstance(Locale("id")).format(harga1)
                b.txHargaD.text = "Rp.$formattedNumber"
                harga_fix = harga
                sisa_poin = Global.poin.toInt()
            }
        }

        b.btnBayarTD.setOnClickListener {
            val timeStamp = SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault()).format(Date())
            id_transaksi = "TRANS"+timeStamp

            val sdkUIFlowBuilder: SdkUIFlowBuilder = SdkUIFlowBuilder.init()
                .setClientKey("SB-Mid-client-ZhPbQMg6l9XeNEXP") // client_key is mandatory
                .setContext(this) // context is mandatory
                .setTransactionFinishedCallback(this)
                .setMerchantBaseUrl("http://${Global.ip}/wilisnet/charge/index.php/") //set merchant url
                .enableLog(true) // enable sdk log
                .setColorTheme(CustomColorTheme("#3C486B", "#3C486B", "#3C486B"))
                // will replace theme on snap theme on MAP
                .setLanguage("id")
            sdkUIFlowBuilder.buildSDK()
            MidtransSDK.getInstance().transactionRequest = initTransactionRequest()
            MidtransSDK.getInstance().startPaymentUiFlow(this)
            queryRegister("insert")


        }
    }

    private fun initTransactionRequest(): TransactionRequest {
        val transactionRequestNew = TransactionRequest(id_transaksi, harga_fix.toDouble())
        transactionRequestNew.customerDetails = initCustomerDetails()
        transactionRequestNew.itemDetails = initItemDetail()
        transactionRequestNew.gopay = Gopay("mysamplesdk:://midtrans")
        transactionRequestNew.shopeepay = Shopeepay("mysamplesdk:://midtrans")
        return transactionRequestNew
    }

    private fun initItemDetail(): ArrayList<ItemDetails>? {
        val item = ArrayList<ItemDetails>()
        val item1 = ItemDetails("1",harga_fix.toDouble(),1,b.txnamaT.text.toString())
        item.add(item1)
        return item
    }

    private fun initCustomerDetails(): CustomerDetails {
        //define customer detail (mandatory for coreflow)
        val mCustomerDetails = CustomerDetails()
        mCustomerDetails.phone = Global.phone
        mCustomerDetails.firstName = Global.nama
        mCustomerDetails.email = Global.email
        mCustomerDetails.customerIdentifier = Global.email
        return mCustomerDetails
    }

    override fun onTransactionFinished(p0: TransactionResult?) {
        p0?.response?.let { response ->
            when (p0.status) {
                TransactionResult.STATUS_SUCCESS -> {
                    finish()
                    update_poin("update")
                }
                TransactionResult.STATUS_PENDING -> {
                    finish()
                    update_poin("update")

                }
                TransactionResult.STATUS_FAILED -> {
                    finish()
                }
                else -> {}
            }
            response.transactionStatus?.let { messages ->
                Log.d("Validation Messages", messages.toString())
            }
        } ?: run {
            if (p0?.isTransactionCanceled == true) {
                // Handle canceled transaction
            } else {
                // Handle transaction failure
            }
        }
    }

    fun queryRegister(mode : String){
        val request = object : StringRequest(
            Method.POST,url,
            Response.Listener{ response ->
//                    Toast.makeText(this,response, Toast.LENGTH_LONG).show()
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if(error.equals("000")){

                }else{
                    Toast.makeText(this,error, Toast.LENGTH_LONG).show()
                }


            },
            Response.ErrorListener { error ->
                Toast.makeText(this,error.toString(), Toast.LENGTH_LONG).show()
            }){
            @RequiresApi(Build.VERSION_CODES.O)
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()

                val dateTime: LocalDateTime = LocalDateTime.now()
                val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val formattedDateTime: String = dateTime.format(formatter)
                when(mode){
                    "insert"->{
                        hm.put("mode","insert")
                        hm.put("email",Global.email)
                        hm.put("id_transaksi",id_transaksi)
                        hm.put("periode",periode)
                        hm.put("kategori","tagihan")
                        hm.put("jumlah",harga)
                        hm.put("tanggal",formattedDateTime)

                    }
                    "update"->{
                        hm.put("mode","insert")
                        hm.put("email",Global.email)
                        hm.put("id_transaksi",id_transaksi)
                        hm.put("kategori","tagihan")

                    }
                    "delete"->{
                        hm.put("mode","delete")
                        hm.put("email",Global.email)
                        hm.put("id_transaksi",id_transaksi)
                        hm.put("kategori","tagihan")

                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    fun update_poin(mode : String){
        val request = object : StringRequest(
            Method.POST,url2,
            Response.Listener{ response ->
//                    Toast.makeText(this,response, Toast.LENGTH_LONG).show()
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if(error.equals("000")){

                }else{
                    Toast.makeText(this,error, Toast.LENGTH_LONG).show()
                }


            },
            Response.ErrorListener { error ->
                Toast.makeText(this,error.toString(), Toast.LENGTH_LONG).show()
            }){
            @RequiresApi(Build.VERSION_CODES.O)
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "insert"->{
                        hm.put("mode","insert")
                        hm.put("email",Global.email)
                        hm.put("poin", sisa_poin.toString())
                    }
                    "update"->{
                        hm.put("mode","update")
                        hm.put("email",Global.email)
                        hm.put("poin", sisa_poin.toString())

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

}