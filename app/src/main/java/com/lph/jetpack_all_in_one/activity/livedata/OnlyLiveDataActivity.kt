package com.lph.jetpack_all_in_one.activity.livedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import com.lph.jetpack_all_in_one.R
import com.lph.jetpack_all_in_one.activity.livedata.data.Person
import com.lph.jetpack_all_in_one.activity.livedata.data.SingletonLiveData

/**
 * 只有livedata用法 和 与frament交互的使用方法
 */
class OnlyLiveDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_only_live_data)
        val observer = Observer<ArrayList<Person>> {
            findViewById<TextView>(R.id.tv_des).text = it.last().toString()
        }
        SingletonLiveData.personList.observe(this,observer)
        findViewById<Button>(R.id.btn_add).setOnClickListener {
            SingletonLiveData.addPerson(Person("lph",233,1))
        }
    }
}