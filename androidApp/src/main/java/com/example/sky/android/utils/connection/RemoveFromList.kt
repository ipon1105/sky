package com.example.sky.android.utils.connection

// Функция удаления из списка по индексу
fun removeFromList(list: List<Any>, index: Int):List<Any>{
    var resList = emptyList<Any>()

    for ((i, el) in list.withIndex()){
        if (i == index)
            continue
        resList+=el
    }

    return resList
}