package com.example.sky.android.models.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sky.android.models.getStatus
import com.example.sky.android.models.getUserId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingViewModel() : ViewModel(){
    var status by mutableStateOf(0)
        private set

    init {
        initStatus()
    }

    private fun initStatus(){
        viewModelScope.launch(Dispatchers.Main) {
            try {
                status = getStatus(getUserId())
            } catch (e: Exception){

            }
        }
    }
}