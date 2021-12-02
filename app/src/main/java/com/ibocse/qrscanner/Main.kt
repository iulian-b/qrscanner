package com.ibocse.qrscanner

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import kotlinx.android.synthetic.main.layout_main.*
import android.content.Intent
import android.net.Uri
import android.view.View


private const val CAMERA_REQUEST_CODE = 101

class MainActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private var TYPE = "TXT"  // TXT, URL, TEL, CNT
    private var RESULT = ""
    var unicodePhone = 0x1F4DE
    var unicodeLink = 0x1F310
    var unicodeContact = 0x1F194
    var unicodeText = 0x1F4AC

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)

        setupPermission()
        codeScanner()
    }


    private fun codeScanner() {
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        codeScanner = CodeScanner(this, scannerView)

        codeScanner.camera = CodeScanner.CAMERA_BACK // CAMERA_BACK = camera anterioara; CAMERA_FRONT = camera frontala
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // sau CONTINUOUS
        codeScanner.formats = CodeScanner.ALL_FORMATS // lista de tipuri de BarcodeFormat,
        codeScanner.scanMode = ScanMode.CONTINUOUS // SINGLE, CONTINUOUS, PREVIEW
        codeScanner.isAutoFocusEnabled = true // autofocus
        codeScanner.isFlashEnabled = false // flash

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                //Toast.makeText(this, "${it.text}", Toast.LENGTH_LONG).show()    //PT DEBUGI
                val result = "${it.text}"
                RESULT = result

                if (result.contains("BEGIN:VCARD")) {   //contact
                    TYPE = "CNT"
                    setVisible(2)
                    tv_textView.text = getEmoji(unicodeContact)
                    var tmp = ""
                    tmp = result.substring(result.indexOf("VERSION")+14)
                    tmp = tmp.substringBefore("ORG:")
                    tmp.trim()
                    tmp = tmp.replace(";"," ")
                    tv_textViewCNT.setTextColor(Color.parseColor("#ebcc34"))
                    tv_textViewCNT.text = tmp

                } else if (result.contains("tel:")) {    //telefon
                    TYPE = "TEL"
                    setVisible(1)
                    tv_textView.text = getEmoji(unicodePhone)
                    tv_textViewTEL.text = result.drop(4)
                    tv_textViewTEL.setTextColor(Color.parseColor("#50c91c"))

                } else if (result.contains("http")) {   //link
                    TYPE = "URL"
                    setVisible(0)
                    tv_textView.text = getEmoji(unicodeLink)
                    tv_textViewURL.text = result
                    tv_textViewURL.setTextColor(Color.parseColor("#03dbfc"))
                    tv_textViewURL.movementMethod = LinkMovementMethod.getInstance()

                } else {   //text
                    TYPE = "TXT"
                    setVisible(3)
                    tv_textView.text = getEmoji(unicodeText)
                    //tv_textViewURL.setTextColor(Color.parseColor("#FFFFFF"))
                    tv_textViewTXT.text = result

                    copyToClipboard(result)
                }
            }
        }

        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Eroare la initializarea camerei: ${it.message}",
                        Toast.LENGTH_LONG).show()
            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }


    fun onClickTEL(view: android.view.View) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse(RESULT)
        startActivity(intent)
    }

    fun onClickCNT(view: android.view.View) {
        //TODO
        // add contact
    }

    fun Context.copyToClipboard(text: CharSequence){
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label",text)
        clipboard.setPrimaryClip(clip)
    }

    fun setVisible(X: Int){
        when (X) {
            0 -> {tv_textViewURL.visibility = View.VISIBLE
                tv_textViewTEL.visibility = View.INVISIBLE
                tv_textViewCNT.visibility = View.INVISIBLE
                tv_textViewTXT.visibility = View.INVISIBLE
                tv_textViewURL.isClickable = true
                tv_textViewTEL.isClickable = false
                tv_textViewCNT.isClickable = false
                tv_textViewTXT.isClickable = false}

            1 -> {tv_textViewURL.visibility = View.INVISIBLE
                tv_textViewTEL.visibility = View.VISIBLE
                tv_textViewCNT.visibility = View.INVISIBLE
                tv_textViewTXT.visibility = View.INVISIBLE
                tv_textViewURL.isClickable = false
                tv_textViewTEL.isClickable = true
                tv_textViewCNT.isClickable = false
                tv_textViewTXT.isClickable = false}

            2 -> {tv_textViewURL.visibility = View.INVISIBLE
                tv_textViewTEL.visibility = View.INVISIBLE
                tv_textViewCNT.visibility = View.VISIBLE
                tv_textViewTXT.visibility = View.INVISIBLE
                tv_textViewURL.isClickable = false
                tv_textViewTEL.isClickable = false
                tv_textViewCNT.isClickable = true
                tv_textViewTXT.isClickable = false}

            3 -> {tv_textViewURL.visibility = View.INVISIBLE
                tv_textViewTEL.visibility = View.INVISIBLE
                tv_textViewCNT.visibility = View.INVISIBLE
                tv_textViewTXT.visibility = View.VISIBLE
                tv_textViewURL.isClickable = false
                tv_textViewTEL.isClickable = false
                tv_textViewCNT.isClickable = false
                tv_textViewTXT.isClickable = true}
        }


    }
    fun getEmoji(unicode: Int): String {
        return String(Character.toChars(unicode))
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermission() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun makeRequest(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permisia camerei nu a fost acordata", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}