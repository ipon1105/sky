package com.example.sky.android.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.sky.android.models.data.Admin
import com.example.sky.android.models.data.UserData
import com.example.sky.android.utils.connection.removeFromList
import com.example.sky.navigation.NavRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingViewModel() : ViewModel(){
    var admin by mutableStateOf(Admin())
        private set
    var status by mutableStateOf(0)
        private set
    var workerList by mutableStateOf(listOf<UserData>())
        private set
    var workerDialog by mutableStateOf(false)
        private set
    var value by mutableStateOf("")
        private set


    init {
        initStatus()
    }

    private fun initStatus(){
        viewModelScope.launch(Dispatchers.Main) {
            try {
                status = getStatus(getUserId())

                if (status == 2){
                    workerList = emptyList()
                    admin = getAdminFromFirestore()
                    for (worker in admin.workerList) {
                        var w = getWorkerFromFirestore(worker)
                        if (w != null)
                            workerList += getUserData(w.info)
                    }

                }
            } catch (e: Exception){

            }
        }
    }

    fun newWorker(value: Boolean){
        workerDialog = value
    }

    fun deleteElement(index: Int){
        viewModelScope.launch(Dispatchers.Main) {
            try {
                var worker = getWorkerFromFirestore(admin.workerList.get(index))
                if (worker != null) {
                    admin.workerList = removeFromList(admin.workerList, index) as List<String>
                    workerList = removeFromList(workerList, index) as List<UserData>

                    updateAdminInFirestore(admin)

                    var newList = emptyList<String>()
                    for (el in worker.adminList) {
                        if (el == getUserId())
                            continue
                        newList += el
                    }

                    worker.adminList = newList
                    updateWorker(worker)
                }

            } catch (e: Exception){

            }
        }

    }

    fun newValue(value:String) {
        this.value = value
    }

    fun newWorkerAdd(){
        newWorker(false)

        viewModelScope.launch(Dispatchers.Main) {
            var worker = getWorkerFromFirestore(value)
            if(worker != null)
            {
                admin.workerList += value
                updateAdminInFirestore(admin)

                worker.adminList += admin.auth
                updateWorker(worker)
            }
            initStatus()
        }
    }

    fun detail(navController: NavHostController, index: Int){

    }
}