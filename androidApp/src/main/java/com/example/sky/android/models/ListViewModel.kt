package com.example.sky.android.models

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.sky.android.models.data.Flat
import com.example.sky.navigation.NavRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Модель для
class ListViewModel(): ViewModel(){
    var internetConnection by mutableStateOf(true)
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

    @JvmName("setInternetConnection1")
    fun setInternetConnection(value: Boolean){
        internetConnection = value
    }

    @JvmName("setDialogMsg1")
    fun setDialogMsg(value: String){
        dialogMsg = value
    }

    @JvmName("setUpdating1")
    fun setUpdating(value: Boolean){
        isUpdating = value
    }

    @JvmName("setOneUpdate1")
    fun setOneUpdate(value: Boolean){
        isOneUpdate = value
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
    fun updateList(baseListener: (isSuccessful: Boolean, list: List<Flat>, msg: String) -> Unit){
        if (isOneUpdate) {
            isUpdating = true

            // Паралельная работа
            viewModelScope.launch(Dispatchers.IO) {

                // Попытка загрузки данных
                try {
                    baseListener.invoke(true, getFlatListFromFirestore(), "")
                }
                // Неудачная попытка загрузки
                catch (it: Exception) {
                    baseListener.invoke(false, getBaseList() as List<Flat>, it.message.toString())
                }
            }
        }
    }

    fun setListFlat(list: List<Flat>){
        flatList = list
        flatList += null
    }

    // Равны ли списки
    fun listEquals(list: List<Flat>) : Boolean{
        if (flatList.size != list.size)
            return false
        for (flat in flatList)
            if (!list.contains(flat))
                return false
        return true
    }

    // Новая квартира
    fun toNewFlat(navController: NavHostController){
        if (!internetConnection)
            showDialog = true
        else {
            val flatId = "null";
            navController.navigate(route = NavRoute.Editor.route + "/${flatId}")
        }
    }

    // Страница подробнее
    fun toFlatInfo(navController: NavHostController, flatId: String){
        if (!internetConnection)
            showDialog = true
        else
            navController.navigate(route = NavRoute.FlatInfo.route + "/${flatId}")
    }

    // Загрузить список квартир по умолчанию
    fun refreshList(){
        flatList = getBaseList()
        isOneUpdate = true
    }

    // Возвращает пустой лист
    fun getBaseList():List<Flat?>{
        return listOf(null)
    }
}