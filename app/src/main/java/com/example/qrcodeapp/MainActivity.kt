package com.example.qrcodeapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {

    private var imView : ImageView? = null
    private var button : Button? = null
    private var bScanner : Button? = null
    private var tvResult : TextView? = null

    val writer = QRCodeWriter()
    private var ptData : EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imView = findViewById(R.id.imageView)
        button = findViewById(R.id.button)
        bScanner = findViewById(R.id.bScanner)
        tvResult = findViewById(R.id.tvResult)
        ptData = findViewById(R.id.ptData)

        tvResult?.visibility = View.INVISIBLE
        button?.setOnClickListener {
            generateQRCode(ptData?.text.toString())
        }

        bScanner?.setOnClickListener {
            checkCameraPermission()
        }

    }

    private fun generateQRCode(text : String){

        val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        imView?.setImageBitmap(bitmap)
    }

    val result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            val res = it.data?.getStringExtra("result")
            tvResult?.text = res
            tvResult?.visibility = View.VISIBLE
        }
    }

    private fun checkCameraPermission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED){
            result.launch(Intent(this, ScannerActivity::class.java))
        }
        else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 19)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 19){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                result.launch(Intent(this, ScannerActivity::class.java))
            }
        }
    }
}