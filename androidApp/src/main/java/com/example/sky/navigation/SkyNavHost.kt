package com.example.sky.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sky.android.models.FlatEditorViewModel
import com.example.sky.android.screens.*
import com.example.sky.android.screens.main.FlatEditorScreen
import com.example.sky.android.screens.main.FlatInfoScreen
import com.example.sky.android.screens.main.SettingScreen
import com.example.sky.android.screens.main.WorkerSearchScreen

sealed class NavRoute(val route: String){
    object Login: NavRoute("login_screen")
    object SignUp: NavRoute("sign_up_screen")
    object ForgotPassword: NavRoute("forgot_password_screen")
    object PrivacyPolicy: NavRoute("privacy_policy_screen")
    object TermsAndConditions: NavRoute("terms_and_conditions_screen")
    object Main: NavRoute("main_screen")
    object Editor: NavRoute("flat_editor_screen")
    object FlatInfo: NavRoute("flat_info_screen")
    object SettingInfo: NavRoute("setting_screen")
    object WorkerSearch: NavRoute("worker_search_screen")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SkyNavHost(startDestination: String = NavRoute.Login.route) {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = startDestination){
        composable(NavRoute.Login.route){ LoginScreen(navController = navController) }
        composable(NavRoute.SignUp.route){ SignUpScreen(navController = navController) }
        composable(NavRoute.ForgotPassword.route){ ForgotPasswordScreen(navController = navController) }
        composable(NavRoute.PrivacyPolicy.route){PrivacyPolicyScreen(navController = navController)}
        composable(NavRoute.TermsAndConditions.route){ TermsAndConditionsScreen(navController = navController) }
        composable(NavRoute.Main.route){ MainScreen(navController = navController) }
        composable(NavRoute.Editor.route + "/{flatId}"){
            val id = it.arguments?.getString("flatId");
            if (id != null)
                FlatEditorScreen(navController = navController, flatId = id)
        }
        composable(NavRoute.FlatInfo.route + "/{flatId}"){
            val id = it.arguments?.getString("flatId");
            if (id != null)
                FlatInfoScreen(navController = navController, flatId = id)
        }
        composable(NavRoute.SettingInfo.route){ SettingScreen(navController = navController) }
        composable(NavRoute.WorkerSearch.route){ WorkerSearchScreen(navController = navController) }
    }
}