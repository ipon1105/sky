package com.example.sky.android

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.sky.android.models.*
import com.example.sky.android.models.authorization.SignUpViewModel
import com.example.sky.android.models.data.Client
import com.example.sky.android.models.data.Flat
import com.example.sky.navigation.NavRoute
import com.example.sky.navigation.SkyNavHost
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.sky.android.composables.ui.*

class MainActivity : ComponentActivity() {

    // TODO: ПР + Менеджер (KMM) + Доделаю календарь с подсветкой и записью
    // TODO: КОНКУРС: Девайс: Карта(Скрыть или Алерт)

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (false)
                tempScreen()
            else
            if (!getUserId().equals(""))
                SkyNavHost(NavRoute.Main.route)
            else
                SkyNavHost()
        }

        // Прячем системную панель навигации
        window.insetsController?.apply {
            hide(WindowInsets.Type.navigationBars())
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}

private var viewModel = TempScreenViewModel()
@SuppressLint("UnrememberedMutableState")
@Composable
fun tempScreen(){
    Log.d("viewModel", "Start")

    Column(modifier = Modifier
        .background(color = Color.White)
        .fillMaxSize()) {
        VerticalCustomTextField(title = viewModel.client.image, onValueChange = {})
        VerticalCustomTextField(title = viewModel.client.fio, onValueChange = {})
        VerticalCustomTextField(title = viewModel.client.phone, onValueChange = {})
        VerticalCustomTextField(title = viewModel.client.passport, onValueChange = {})
        VerticalCustomTextField(title = viewModel.client.registration, onValueChange = {})
    }

    viewModel.start()
    Log.d("viewModel", "Stop")
}

class TempScreenViewModel : ViewModel(){
    var client by mutableStateOf(Client())
        private set

    fun start(){
        viewModelScope.launch(Dispatchers.Main) {
            try {
                client = loadClient("fkhJf4mDE8A2b0zV2GFT")
            } catch (e: Exception){}
        }
    }

    fun saveClient(clientId: String, client: Client){
        viewModelScope.launch(Dispatchers.Main) {
            try {
                //save
            } catch (e: Exception){

            }
        }
    }

    fun createtClient( client: Client){
        viewModelScope.launch(Dispatchers.Main) {
            try {
                Log.d("viewModel", createClient(client))
            } catch (e: Exception){

            }
        }
    }
}