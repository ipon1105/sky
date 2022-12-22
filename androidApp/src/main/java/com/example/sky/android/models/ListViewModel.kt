package com.example.sky.android.models

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.sky.android.models.data.Flat
import com.example.sky.navigation.NavRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Модель для экрана со списком квартир
class ListViewModel(): ViewModel(){
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

    var flatList by mutableStateOf(getBaseList())
        private set

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

    // Обновить список
    fun updateList(){
        if (isOneUpdate) {
            isUpdating = true

            // Паралельная работа
            viewModelScope.launch(Dispatchers.Main) {

                // Попытка загрузки данных
                try {
                    val admin = getAdminFromFirestore()
                    val list = getFlatListFromFirestore(admin)

                    setListFlat(list)

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
            val flatId = "null";
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
        isOneUpdate = true
    }

    // Переопределить список квартир
    private fun setListFlat(list: List<Flat>){
        flatList = list
        flatList += null
    }

    // Возвращает пустой лист
    private fun getBaseList():List<Flat?>{
        return listOf(null)
    }
}