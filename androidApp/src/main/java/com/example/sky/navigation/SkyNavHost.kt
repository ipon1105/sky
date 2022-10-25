package com.example.sky.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sky.android.screens.*

sealed class NavRoute(val route: String){
    object Login: NavRoute("login_screen")
    object SignUp: NavRoute("sign_up_screen")
    object ForgotPassword: NavRoute("forgot_password_screen")
    object PrivacyPolicy: NavRoute("privacy_policy_screen")
    object TermsAndConditions: NavRoute("terms_and_conditions_screen")
    object Main: NavRoute("main_screen")

}

@Composable
fun SkyNavHost() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = NavRoute.Login.route){
        composable(NavRoute.Login.route){ LoginScreen(navController = navController) }
        composable(NavRoute.SignUp.route){ SignUpScreen(navController = navController) }
        composable(NavRoute.ForgotPassword.route){ ForgotPasswordScreen(navController = navController) }
        composable(NavRoute.PrivacyPolicy.route){ PrivacyPolicyScreen(navController = navController) }
        composable(NavRoute.TermsAndConditions.route){ TermsAndConditionsScreen(navController = navController) }
        composable(NavRoute.Main.route){ MainScreen(navController = navController) }
    }
}