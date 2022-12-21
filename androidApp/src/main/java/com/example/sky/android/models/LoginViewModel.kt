package com.example.sky.android.models

import android.util.Patterns
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.sky.navigation.NavRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class LoginViewModel: ViewModel() {
    val isEmailValid by derivedStateOf { Patterns.EMAIL_ADDRESS.matcher(email).matches() }
    val isPasswordValid by derivedStateOf { password.length > 7 }

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var isAuthLoading by mutableStateOf(false)
        private set
    var isLinkForgotPassword by mutableStateOf(false)
        private set
    var isLinkRegister by mutableStateOf(false)
        private set
    var showDialog by mutableStateOf(false)
        private set
    var showErrorMessages by mutableStateOf(false)
        private set
    var dialogMsg by mutableStateOf("")
        private set

    @JvmName("setShowErrorMessages1")
    fun setShowErrorMessages(value: Boolean){
        showErrorMessages = value
    }

    @JvmName("setDialogMsg1")
    fun setDialogMsg(value: String){
        dialogMsg = value
    }

    @JvmName("setShowDialog1")
    fun setShowDialog(value: Boolean){
        showDialog = value
    }

    @JvmName("setEmail1")
    fun setEmail(value: String){
        email = value
    }

    @JvmName("setPassword1")
    fun setPassword(value: String){
        password = value
    }

    @JvmName("setAuthLoading1")
    fun setAuthLoading(value: Boolean){
        isAuthLoading = value
        isLinkRegister = false
        isLinkForgotPassword = false
    }

    // Перейти на страницу Регистрация
    fun goLinkToRegister(navController: NavHostController){
        if (isAuthLoading) {
            isLinkRegister = true

            viewModelScope.launch {
                delay(10.seconds)
                isLinkRegister = false
            }
        } else
            navController.navigate(route = NavRoute.SignUp.route)
    }

    // Перейти на страницу Забыл пароль
    fun goLinkToForgotPassword(navController: NavHostController){
        if (isAuthLoading) {
            isLinkForgotPassword = true

            viewModelScope.launch {
                delay(10.seconds)
                isLinkForgotPassword = false
            }
        } else
            navController.navigate(route = NavRoute.ForgotPassword.route)
    }

    // Функция входа на главные экраны
    fun login(navController: NavHostController){
        navController.navigate(NavRoute.Main.route)
    }

    // Функция входа на главные экраны
    fun tryLogin(baseListener: (isSuccessful: Boolean, msg: String) -> Unit){
        isAuthLoading = true
        // Паралельная работа
        viewModelScope.launch(Dispatchers.IO){
            // Попытка входа
            try{
                signIn(email, password){ isSuccessful, msg ->
                    baseListener.invoke(true, msg)
                }
            // Неудачная попытка входа
            } catch (it: Exception){
                baseListener.invoke(false, it.message.toString())
            }
        }
    }
}
