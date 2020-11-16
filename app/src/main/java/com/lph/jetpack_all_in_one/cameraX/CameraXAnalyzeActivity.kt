package com.lph.jetpack_all_in_one.cameraX

import android.Manifest
import android.graphics.*
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.lph.jetpack_all_in_one.R
import com.tbruyelle.rxpermissions3.RxPermissions
import kotlinx.android.synthetic.main.activity_camera_x.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class CameraXAnalyzeActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    var imageCapture: ImageCapture? = null

    private val rxPermissions by lazy {
        RxPermissions(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_x)

        btn_take.setOnClickListener {

            rxPermissions
                .request(Manifest.permission.CAMERA)
                .subscribe { granted ->
                    if (granted) {
                        openCamera()
                    } else {
                        Toast.makeText(this, "请开启相机权限", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        btn_take_photo.setOnClickListener {
            takePhoto()
        }
    }

    private fun openCamera() {

        //cameraProviderFuture 用于讲摄像头的生命周期绑定到生命周期所有者上面。
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {

            //得到 cameraProvider实例 用于绑定生命周期
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)  //从取景器中获取表面提供
            }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA //相机选择器

            //获取拍照的对象
            imageCapture  = ImageCapture.Builder().build()
            //获取分析实例
            val imageAnalysis = ImageAnalysis.Builder().build().also {
                it.setAnalyzer(ContextCompat.getMainExecutor(this), LuminosityAnalyzer(){
                   val decodeBitmap = it.toBitmap()
                    iv_preview.setImageBitmap(decodeBitmap)
                })
            }
            try {
                cameraProvider.unbindAll()
                //和预览照片却别所在  这个地方 它bind的多了imageCapture这个类
                cameraProvider.bindToLifecycle(this@CameraXAnalyzeActivity,cameraSelector,preview,imageCapture,imageAnalysis)

            }catch (e:Exception){
                e.printStackTrace()
                throw e
            }
        },ContextCompat.getMainExecutor(this))

    }

    fun takePhoto(){
        val imageCapture = imageCapture?:return //如果为空就return
        val photoFile = File(
            getOutputDirectory(),
            SimpleDateFormat(FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(outputOptions,ContextCompat.getMainExecutor(this),object :ImageCapture.OnImageSavedCallback{
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                val msg = "Photo capture succeeded: $savedUri"
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                Log.d(TAG, msg)
            }

            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(baseContext, "error", Toast.LENGTH_SHORT).show()
                exception.printStackTrace()
            }

        })

    }
    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }




    fun ImageProxy.toBitmap(): Bitmap {
        val yBuffer = planes[0].buffer // Y
        val vuBuffer = planes[2].buffer // VU

        val ySize = yBuffer.remaining()
        val vuSize = vuBuffer.remaining()

        val nv21 = ByteArray(ySize + vuSize)

        yBuffer.get(nv21, 0, ySize)
        vuBuffer.get(nv21, ySize, vuSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, this.width, this.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 50, out)
        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}