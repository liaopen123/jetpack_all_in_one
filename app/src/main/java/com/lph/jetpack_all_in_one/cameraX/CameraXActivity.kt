package com.lph.jetpack_all_in_one.cameraX

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.lph.jetpack_all_in_one.R
import com.tbruyelle.rxpermissions3.RxPermissions
import kotlinx.android.synthetic.main.activity_camera_x.*
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class CameraXActivity : AppCompatActivity() {




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


            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(this@CameraXActivity,cameraSelector,preview)

            }catch (e:Exception){
                e.printStackTrace()
                throw e
            }
        },ContextCompat.getMainExecutor(this))

    }






}