package com.example.qrcodeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView

class ScannerActivity : AppCompatActivity(), ZBarScannerView.ResultHandler {
    lateinit var zbView :ZBarScannerView
    private val i = Intent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        zbView = ZBarScannerView(this)
        setContentView(zbView)
    }

    override fun onResume() {
        super.onResume()
        zbView.setResultHandler(this)
        zbView.startCamera()
    }
    override fun onPause() {
        super.onPause()
        zbView.stopCamera()
    }
    override fun handleResult(result: Result?) {
     i.putExtra("result", result?.contents)
      setResult(RESULT_OK, i)
        finish()

    }
}