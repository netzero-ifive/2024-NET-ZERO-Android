package com.example.myapplication.ui.screens.scan

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning


@Composable
fun ScanPage() {
    var scanResult by remember { mutableStateOf<String?>(null) }


    val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_AZTEC
        )
        .enableAutoZoom() // available on 16.1.0 and higher
        .build()
    val context = LocalContext.current
    val scanner = GmsBarcodeScanning.getClient(context, options)



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    val intent = Intent(context, ScanResultActivity::class.java)
                    context.startActivity(intent)
                }
                .addOnCanceledListener {
                    scanResult = "Scan cancelled"
                    Log.d("REZero", "취소")
                }
                .addOnFailureListener { e ->
                    scanResult = "Scan failed: ${e.message}"
                    Log.d("REZero", "실패 $e")
                }

    }
}