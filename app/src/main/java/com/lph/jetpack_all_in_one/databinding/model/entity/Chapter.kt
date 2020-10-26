package com.lph.jetpack_all_in_one.databinding.model.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.lph.jetpack_all_in_one.BR


class Chapter:BaseObservable() {

     var name=""
    set(value) {
        field = value
        notifyChange()
        notifyPropertyChanged(BR._all)
    }
     var sex=""
    set(value) {
        field = value
        notifyChange()
        notifyPropertyChanged(BR._all)

    }

    var age:Int=10
    fun changeText(){
        name ="改变了"
    }

}