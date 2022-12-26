package com.example.sky.android.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sky.android.models.data.UserData
import com.example.sky.android.models.data.Worker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkerSearchViewModel() : ViewModel(){
    var workerList by mutableStateOf(emptyList<Worker>())
    var usdataList by mutableStateOf(emptyList<UserData>())

    init {

    }

    fun newNotify(worker: Worker){
        viewModelScope.launch(Dispatchers.Main) {

        }
    }
}