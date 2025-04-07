package com.example.timesheet

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.timesheet.data.data_class.User
import com.example.timesheet.data.data_class.UserResponse

object SessionManager {

    private const val PREF_NAME = "nani_app_prefs"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_TOKEN = "token"

    private var currentUser: UserResponse? = null
    private var token: String? = null

    private fun getPrefs(context: Context) =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveUser(context: Context, user: User) {
        currentUser = user.response
        token = user.token

        getPrefs(context).edit().apply {
            putString(KEY_USER_ID, user.response.user_id)
            putString(KEY_TOKEN, user.token)
            apply()
        }
    }

    fun getUser(context: Context): UserResponse? {
        if (currentUser == null) {
            val userId = getPrefs(context).getString(KEY_USER_ID, null)
            val savedToken = getPrefs(context).getString(KEY_TOKEN, null)

            if (userId != null && savedToken != null) {
                currentUser = UserResponse(
                    token = savedToken,
                    user_id = userId,
                    expires = 0
                )
                token = savedToken
            }
        }
        return currentUser
    }

    fun getToken(context: Context): String? {
        if (token == null) {
            token = getPrefs(context).getString(KEY_TOKEN, null)
        }
        return token
    }


    fun clearUser(context: Context) {
        currentUser = null
        token = null

        getPrefs(context).edit().clear().apply()
    }
}
class TokenStorage(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("auth_token", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("auth_token", null)
    }

    fun clearToken() {
        prefs.edit().remove("auth_token").apply()
    }
}


fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
