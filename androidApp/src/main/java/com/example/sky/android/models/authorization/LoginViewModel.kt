package com.example.sky.android.models.authorization

import android.util.Patterns
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.sky.android.models.signIn
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
    var internet by mutableStateOf(false)
        private set
    var dialogMsg by mutableStateOf("")
        private set

    @JvmName("setInternet1")
    fun setInternet(value: Boolean){
        internet = value
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

    // Перейти на страницу Регистрация
    fun goLinkToRegister(navController: NavHostController){
        if (isAuthLoading) {
            isLinkRegister = true

            viewModelScope.launch(Dispatchers.Main) {
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

            viewModelScope.launch(Dispatchers.Main) {
                delay(10.seconds)
                isLinkForgotPassword = false
            }
        } else
            navController.navigate(route = NavRoute.ForgotPassword.route)
    }

    // Функция входа на главные экраны
    private fun tryLogin(navController: NavHostController){
        isAuthLoading = true

        // Новый поток для входа
        viewModelScope.launch(Dispatchers.Main){
            // Попытка входа
            try{

                if (signIn(email, password))
                {
                    isAuthLoading = false
                    navController.navigate(NavRoute.Main.route)
                }

            // Неудачная попытка входа
            } catch (it: Exception){
                isAuthLoading = false
                showDialog = true
                dialogMsg = it.message.toString()
            }
        }
    }

    // Нажатие на кнопку входа
    fun btnLoginClick(navController: NavHostController){
        if (!internet)
            showDialog = true
        else
            if (!isEmailValid || !isPasswordValid)
                showErrorMessages = true
            else
                tryLogin(navController = navController)
    }
}
