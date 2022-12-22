package com.example.sky.android.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.sky.android.models.data.Flat
import com.example.sky.android.screens.refreshModel
import com.example.sky.navigation.NavRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Модель для редактора квартир
class FlatInfoViewModel(var flatId: String = ""): ViewModel(){
    var isFlatLoading by mutableStateOf(false)
        private set
    var flat by mutableStateOf(Flat())
        private set
    var expanded by mutableStateOf(false)
        private set
    var isDeleting by mutableStateOf(false)
        private set
    var showConfirmDialog by mutableStateOf(false)
        private set
    var showDialog by mutableStateOf(false)
        private set
    var showInfoBlock by mutableStateOf(false)
        private set
    var dialogMsg by mutableStateOf("")
        private set

    init {
        loadFlat()
    }

    @JvmName("setShowConfirmDialog1")
    fun setShowConfirmDialog(value: Boolean){
        showConfirmDialog = value
    }

    @JvmName("setShowInfoBlock1")
    fun setShowInfoBlock(value: Boolean){
        showInfoBlock = value
    }

    @JvmName("setShowDialog1")
    fun setShowDialog(value: Boolean){
        showDialog = value
    }

    @JvmName("setExpanded1")
    fun setExpanded(value: Boolean){
        expanded = value
    }

    // Загрузить данные
    private fun loadFlat(){
        viewModelScope.launch(Dispatchers.Main) {
            try {
                isFlatLoading = true
                flat = getFlatFromFirestore(flatId)
                isFlatLoading = false
            } catch (e: Exception){
                showDialog = true
                dialogMsg = e.message.toString()
            }
        }
    }

    // Удалить запись
    fun btnDeleteClick(){
        if (isDeleting || isFlatLoading)
            showDialog = true
        else
            showConfirmDialog = true
    }

    fun deleting(navController: NavHostController){
        isDeleting = true
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val flatId = deleteFlatFromFirestore(flatId)
                val admin = getAdminFromFirestore()
                deleteFlatFromAdminFromFirestore(flatId, admin)

                isDeleting = false
                showDialog = false

                refreshModel()
                navController.popBackStack()
            } catch (e:Exception){
                isDeleting = false
                showDialog = true
                dialogMsg = e.message.toString()
            }

        }
    }

    // Редактировать запись
    fun btnEditClick(navController: NavHostController){
        if (isDeleting)
            showDialog = true
        else
            navController.navigate(route = NavRoute.Editor.route + "/${flatId}")
    }

    // Вернуться на предыдущую страницу
    fun btnBackClick(navController: NavHostController){
        if (isDeleting)
            showDialog = true
        else
            navController.popBackStack()
    }
}