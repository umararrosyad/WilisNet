package com.example.wilisnet.helper

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore

import android.support.v4.os.IResultReceiver
import android.util.Base64
import android.widget.ImageView
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class MediaHealper(context:Context) {
    val context=context
    var namaFile = ""

    fun getRcGallery():Int{
        return REQ_CODE_GALLERY
    }

    fun bitmapToString(bmp:Bitmap):String{
        val outputStream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG,60,outputStream)
        val byteArray= outputStream.toByteArray()
        return Base64.encodeToString(byteArray,Base64.DEFAULT)
    }
    @Suppress("DEPRECATION")
    fun getBitmapToString (uri: Uri?, imv: ImageView):String {
        var bmp = MediaStore.Images.Media.getBitmap(
            this.context.contentResolver,uri)
        var dim = 720
        if (bmp.height> bmp.width){
            bmp = Bitmap.createScaledBitmap(bmp,
                (bmp.width*dim).div(bmp.height),dim,true)
        }else{
            bmp = Bitmap.createScaledBitmap(bmp,
                dim,(bmp.height*dim).div(bmp.width),true)
        }
        imv.setImageBitmap(bmp)
        imv.setImageBitmap(bmp)
        return bitmapToString(bmp)
    }
    fun getOutputMediaFilename():String{
        val timeStamp = SimpleDateFormat("yyyymmddhhss", Locale.getDefault()).format(Date())
        this.namaFile = "DC_${timeStamp}.jpg"
        return this.namaFile
    }

    companion object{
        const val REQ_CODE_GALLERY = 100
    }
}