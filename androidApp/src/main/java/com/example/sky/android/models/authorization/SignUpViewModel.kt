package com.example.sky.android.models.authorization

import android.util.Patterns
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.sky.android.composables.*
import com.example.sky.android.models.*
import com.example.sky.android.models.data.Admin
import com.example.sky.android.models.data.UserData
import com.example.sky.android.screens.consistDigits
import com.example.sky.navigation.NavRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class SignUpViewModel: ViewModel() {
    // Валидация данных
    val isEmailValid       by derivedStateOf { Patterns.EMAIL_ADDRESS.matcher(email).matches() }
    val isPassValid        by derivedStateOf { password.length > 7 }
    val isProvingPassValid by derivedStateOf { provingPassword.length > 7 && password.equals(provingPassword)}
    val isNameValid        by derivedStateOf { name.length in nameMin..nameMax && !consistDigits(name) }
    val isSurnameValid     by derivedStateOf { surname.length in surnameMin..surnameMax && !consistDigits(surname) }
    val isPatronymicValid  by derivedStateOf { patronymic.length in patronymicMin..patronymicMax && !consistDigits(patronymic) }
    val isPhoneValid       by derivedStateOf { Patterns.PHONE.matcher(phone).matches() }

    var internet by mutableStateOf(false)
        private set
    var name by mutableStateOf("")
        private set
    var surname by mutableStateOf("")
        private set
    var patronymic by mutableStateOf("")
        private set
    var phone by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var provingPassword by mutableStateOf("")
        private set
    var status by mutableStateOf(1)
        private set
    var isAuthLoading by mutableStateOf(false)
        private set
    var showErrorMessages by mutableStateOf(false)
        private set
    var isTCError by mutableStateOf(false)
        private set
    var isPrivacyPolicyError by mutableStateOf(false)
        private set
    var isLoginError by mutableStateOf(false)
        private set
    var isBackError by mutableStateOf(false)
        private set
    var showDialog by mutableStateOf(false)
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

    @JvmName("setName1")
    fun setName(value: String){
        name = value
    }

    @JvmName("setSurname1")
    fun setSurname(value: String){
        surname = value
    }

    @JvmName("setPatronymic1")
    fun setPatronymic(value: String){
        patronymic = value
    }

    @JvmName("setPhone1")
    fun setPhone(value: String){
        phone = value
    }

    @JvmName("setEmail1")
    fun setEmail(value: String){
        email = value
    }

    @JvmName("setPassword1")
    fun setPassword(value: String){
        password = value
    }

    @JvmName("setProvingPassword1")
    fun setProvingPassword(value: String){
        provingPassword = value
    }

    @JvmName("setStatus1")
    fun setStatus(value: Int){
        status = value
    }

    // Перейти на страницу Пользовательское Соглашение
    fun goLinkToTC(navController: NavHostController){
        if (isAuthLoading) {
            isTCError = true

            viewModelScope.launch(Dispatchers.Main) {
                delay(10.seconds)
                isTCError = false
            }
        } else
            navController.navigate(route = NavRoute.TermsAndConditions.route)
    }

    // Перейти на страницу Политика Конфиденциальности
    fun goLinkToPrivacyPolicy(navController: NavHostController){
        if (isAuthLoading) {
            isPrivacyPolicyError = true

            viewModelScope.launch(Dispatchers.Main) {
                delay(10.seconds)
                isPrivacyPolicyError = false
            }
        } else
            navController.navigate(route = NavRoute.PrivacyPolicy.route)
    }

    // Перейти на страницу входа
    fun goLinkToLogin(navController: NavHostController){
        if (isAuthLoading) {
            isLoginError = true

            viewModelScope.launch(Dispatchers.Main) {
                delay(10.seconds)
                isLoginError = false
            }
        } else
            navController.navigate(route = NavRoute.Login.route)
    }

    // Перейти на предыдущую страницу
    fun goToBack(navController: NavHostController){
        if (isAuthLoading) {
            isBackError = true

            viewModelScope.launch(Dispatchers.Main) {
                delay(10.seconds)
                isBackError = false
            }
        } else
            navController.popBackStack()
    }

    // Весь цикл регистрации нового пользователя
    private fun registration(navController: NavHostController){
        isAuthLoading = true

        // Личные данные пользователя
        val data = UserData(
            name = name,
            surname = surname,
            patronymic = patronymic,
            photo = "",
            telephone = phone,
            status = status
        )

        // Паралельная работа
        viewModelScope.launch(Dispatchers.Main){

            // Попытка регистрации
            try{
                // Регистрация нового пользователя
                val userId = registrationNewUser(email, password)
                val userData = createUserData(data)
                if (status == 2) {
                        createAdmin(
                            userId = userId,
                            admin = Admin(
                                auth = userId,
                                info = userData
                            )
                        )
                    } else {
                        createWorker(
                            userId = userId,
                            worker = Worker(
                                auth = userId,
                                info = userData
                            )
                        )
                    }
                isAuthLoading = false
                navController.navigate(NavRoute.Main.route)

            // Неудачная регистрация нового пользователя
            } catch (it: Exception){
                isAuthLoading = false
                showDialog = true
                dialogMsg = it.message.toString()
            }
        }
    }

    // Функция входа на главные экраны
    fun btnContinueClick(navController: NavHostController){
        if (!internet)
            showDialog = true
        else
            if (!isEmailValid || !isNameValid || !isPassValid || !isProvingPassValid || !isPhoneValid)
                showErrorMessages = true
            else
                registration(navController)
    }
}