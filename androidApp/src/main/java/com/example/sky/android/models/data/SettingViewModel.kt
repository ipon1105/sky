package com.example.sky.android.models.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.sky.android.models.*
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

    init {
        initStatus()
    }

    private fun initStatus(){
        viewModelScope.launch(Dispatchers.Main) {
            try {
                status = getStatus(getUserId())

                if (status == 2){
                    admin = getAdminFromFirestore()
                    for (worker in admin.workerList)
                        workerList += getUserData(getWorkerFromFirestore(worker).info)

                }
            } catch (e: Exception){

            }
        }
    }

    fun newFlat(navController: NavHostController){
        navController.navigate(route = NavRoute.WorkerSearch.route)
    }

    fun deleteElement(index: Int){
        viewModelScope.launch(Dispatchers.Main) {
            try {
                var worker = getWorkerFromFirestore(admin.workerList.get(index))
                admin.workerList = removeFromList(admin.workerList, index) as List<String>
                workerList = removeFromList(workerList, index) as List<UserData>

                updateAdminInFirestore(admin)

                var newList = emptyList<String>()
                for (el in worker.adminList)
                {
                    if (el.equals(getUserId()))
                        continue
                    newList += el
                }

                worker.adminList = newList
                updateWorker(worker)


            } catch (e: Exception){

            }
        }

    }

    fun detail(navController: NavHostController, index: Int){

    }
}