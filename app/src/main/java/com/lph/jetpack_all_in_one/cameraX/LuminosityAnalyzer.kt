package com.lph.jetpack_all_in_one.cameraX

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

class LuminosityAnalyzer(var listener :ImageAnalysis.Analyzer):ImageAnalysis.Analyzer {
    //分析图片
    override fun analyze(image: ImageProxy) {
        Log.e("LuminosityAnalyzer", "analyze:${image.toString()} ")

        listener.analyze(image)
        image.close()
    }
}