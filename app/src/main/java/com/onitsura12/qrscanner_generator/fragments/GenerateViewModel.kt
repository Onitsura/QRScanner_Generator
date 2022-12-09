package com.onitsura12.qrscanner_generator.fragments

import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlin.random.Random


class GenerateViewModel : ViewModel() {

    val bmpLive: MutableLiveData<Bitmap> = MutableLiveData()


    init {
        generateQr("Hello ^_^")
    }


    fun generateQr(data: String) {
        val writer = QRCodeWriter()

        val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 200, 200)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val color = randomColor()
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) color else Color.BLACK)
            }
        }
        bmpLive.value = bmp
    }

    private fun randomColor(): Int {
        val random = Random
        val colors = arrayListOf(
            Color.DKGRAY, Color.BLUE, Color.CYAN, Color.WHITE, Color.GREEN, Color.RED, Color.MAGENTA
        )
        val position = random.nextInt(colors.size)
        return colors[position]
    }

    fun share() {

    }

}