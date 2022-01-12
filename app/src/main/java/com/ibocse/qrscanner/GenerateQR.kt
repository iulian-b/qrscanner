package com.ibocse.qrscanner

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.layout_generate.*
import android.widget.Toast

class GenerateQR : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_generate)
}

    var FIRSTGEN = false

    fun onClickGen(view: android.view.View) {
        val content = idEdt.text.toString()
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        idIVQrcode.setImageBitmap(bitmap)
        FIRSTGEN = true
    }

    fun onClickFABSave(view: android.view.View) {
        if (!FIRSTGEN){ toast("Codul este gol"); return }
        val bitmap = (idIVQrcode.drawable as BitmapDrawable).bitmap
        val savedImageURL = MediaStore.Images.Media.insertImage(
                contentResolver,
                bitmap,
                "QR",
                "QR cod generat cu qrscanner"
        )
        val uri: Uri = Uri.parse(savedImageURL)
        toast("Salvat cod QR in: $uri")
    }

    private fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}