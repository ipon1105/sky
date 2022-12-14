package com.example.sky.android.models

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sky.android.models.data.Admin
import com.example.sky.android.models.data.Flat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

// TODO: БАГ, при быстром использовании обновления, загрузка может быть не успешной
// Модель для редактора квартир
class FlatEditorViewModel(var flatId: String? = null): ViewModel(){
    val admin = mutableStateOf(Admin())
    val state = mutableStateOf(Flat())

    init {
        getAdmin()
    }

    // Загрукза административных данных
    private fun getAdmin(){
        viewModelScope.launch {
            admin.value = getAdminFromFirestore( Firebase.auth.currentUser?.uid ?: "")
        }
    }

    // Загрузить данные
    fun loadFlat(){
        viewModelScope.launch {
            if (flatId != null)
                state.value = getFlatFromFirestore(flatId!!)
        }
    }

    // Обновить данные
    fun updateFlat(){
        viewModelScope.launch {
            if (flatId != null)
                updateFlatToFirestore(flatId!!, state.value)
            else
                flatId = createFlatInFirestore(state.value, admin.value)
        }
    }
}