package com.example.sky.android.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.sky.android.models.data.UserData
import com.example.sky.android.models.data.getUserDataLoading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountViewModel:ViewModel() {
    var userData by mutableStateOf(getUserDataLoading())
        private set
    var expanded by mutableStateOf(false)
        private set

    init {
        loadUserData()
    }

    // Загрузить пользовательские данные
    fun loadUserData(){
        viewModelScope.launch(Dispatchers.Main) {
            try {
                var data = UserData()

                if (getStatus(getUserId()) == 2)
                    data = getUserData(getAdminFromFirestore().info)
                else
                    data = getUserData(getWorkerFromFirestore().info)

                userData = data
            } catch (e: Exception){

            }
        }
    }

    // Отредактировать личные данные
    fun editAccount(navController: NavHostController){
        //TODO: Сделать редактирование экрана аккаунта
    }

    @JvmName("setExpanded1")
    fun setExpanded(value: Boolean){
        expanded = value
    }
}