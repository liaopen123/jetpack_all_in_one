package com.lph.jetpack_all_in_one.databinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.lph.jetpack_all_in_one.R
import com.lph.jetpack_all_in_one.databinding.model.entity.Chapter

class DataBindingTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_data_binding)
        val dataBinding = DataBindingUtil.setContentView<ActivityDataBindingBinding>(
            this,
            R.layout.activity_data_binding
        )
        var chapter  = Chapter()    //创建实例对象  并且赋值给databinding对象
        chapter.name="单向绑定"
        chapter.sex="双向绑定"
        chapter.age=19

        dataBinding.test =chapter
        dataBinding.activity = this

    }
    
    
    fun onMyButtonClick(){
        Toast.makeText(this, "233", Toast.LENGTH_SHORT).show()
    }
}