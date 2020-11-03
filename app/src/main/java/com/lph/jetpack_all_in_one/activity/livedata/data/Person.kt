package com.lph.jetpack_all_in_one.activity.livedata.data

import androidx.lifecycle.MutableLiveData

data class Person(var name:String,var age:Int,var sex:Int)

object SingletonLiveData{
    private  val list = ArrayList<Person>()
     val personList : MutableLiveData<ArrayList<Person>> by lazy {
        MutableLiveData<ArrayList<Person>>()
    }
    fun addPerson(person: Person){
        list.add(person)
        personList.value = list
    }

    fun clear(){
        list.clear()
        personList.value = list
    }

}