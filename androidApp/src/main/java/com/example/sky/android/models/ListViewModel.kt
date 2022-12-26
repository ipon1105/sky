package com.example.sky.android.models

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.sky.android.models.data.CardFlat
import com.example.sky.android.models.data.Flat
import com.example.sky.navigation.NavRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Модель для экрана со списком квартир
class ListViewModel(): ViewModel(){
    var status by mutableStateOf(0)
        private set

    var internet by mutableStateOf(true)
        private set
    var showDialog by mutableStateOf(false)
        private set
    var isUpdating by mutableStateOf(false)
        private set
    var isOneUpdate by mutableStateOf(true)
        private set
    var isBtnFreeOn by mutableStateOf(true)
        private set
    var isBtnDirtyOn by mutableStateOf(true)
        private set
    var isBtnBusyOn by mutableStateOf(true)
        private set

    var search by mutableStateOf("")
        private set
    var dialogMsg by mutableStateOf("")
        private set

    var cardFlatList by mutableStateOf(listOf<CardFlat>())
        private set
    var flatList by mutableStateOf(getBaseList())
        private set

    // Обновить список
    fun updateList(){
        if (isOneUpdate && !isUpdating) {
            isUpdating = true

            // Паралельная работа
            viewModelScope.launch(Dispatchers.Main) {

                // Попытка загрузки данных
                try {
                    status = getStatus(getUserId())

                    // Загружаем со стороны администратора
                    if (status == 2) {
                        cardFlatList = emptyList()
                        val admin = getAdminFromFirestore()
                        val list = getFlatListFromFirestore(admin)

                        for (l in list) {
                            if (l.photos.isEmpty())
                                cardFlatList += CardFlat(l, Uri.EMPTY)
                            else
                                cardFlatList += CardFlat(l, getUriLink(l.photos[0]))
                        }
                    // Загружаем со стороны Уборщика
                    } else {
                        val worker = getWorkerFromFirestore()
                        val list = getWorkerListFromFirestore(worker)

                        for (l in list) {
                            if (l.photos.isEmpty())
                                cardFlatList += CardFlat(l, Uri.EMPTY)
                            else
                                cardFlatList += CardFlat(l, getUriLink(l.photos[0]))
                        }
                    }

                    isOneUpdate = false
                    isUpdating = false
                }
                // Неудачная попытка загрузки
                catch (it: Exception) {
                    isOneUpdate = false
                    isUpdating = false

                    showDialog = true
                    dialogMsg = it.message.toString()
                }
            }
        }
    }

    // Новая квартира
    fun cardClickToNewFlat(navController: NavHostController){
        if (!internet)
            showDialog = true
        else {
            val flatId = "null"
            navController.navigate(route = NavRoute.Editor.route + "/${flatId}")
        }
    }

    // Страница подробнее
    fun cardClickFlatInfo(navController: NavHostController, flatId: String){
        if (!internet)
            showDialog = true
        else
            navController.navigate(route = NavRoute.FlatInfo.route + "/${flatId}")
    }

    // Загрузить список квартир по умолчанию
    fun refreshList(){
        flatList = getBaseList()
        cardFlatList = emptyList()
        isOneUpdate = true
    }

    // Возвращает пустой лист
    private fun getBaseList():List<Flat?>{
        return listOf(null)
    }

    @JvmName("setInternet1")
    fun setInternet(value: Boolean){
        internet = value
    }

    @JvmName("setShowDialog1")
    fun setShowDialog(value: Boolean){
        showDialog = value
    }

    @JvmName("setSearch1")
    fun setSearch(value: String){
        search = value
    }

    @JvmName("setBtnFreeOn1")
    fun setBtnFreeOn(value: Boolean){
        isBtnFreeOn = value
    }

    @JvmName("setBtnDirtyOn1")
    fun setBtnDirtyOn(value: Boolean){
        isBtnDirtyOn = value
    }

    @JvmName("setBtnBusyOn1")
    fun setBtnBusyOn(value: Boolean){
        isBtnBusyOn = value
    }
}