package com.example.mallandroid.common

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object TokenManager {
    private const val PREFS_NAME = "mall_prefs"
    private const val KEY_TOKEN = "token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USERNAME = "username"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(context: Context, token: String) {
        getPrefs(context).edit { putString(KEY_TOKEN, token) }
    }

    fun getToken(context: Context): String? {
        return getPrefs(context).getString(KEY_TOKEN, null)
    }

    fun clearToken(context: Context) {
        getPrefs(context).edit { remove(KEY_TOKEN) }
    }

    fun saveUserInfo(context: Context, userId: Long, username: String) {
        getPrefs(context).edit {
            putLong(KEY_USER_ID, userId)
                .putString(KEY_USERNAME, username)
        }
    }

    fun getUserId(context: Context): Long {
        return getPrefs(context).getLong(KEY_USER_ID, -1)
    }

    fun getUsername(context: Context): String? {
        return getPrefs(context).getString(KEY_USERNAME, null)
    }

    fun clearUserInfo(context: Context) {
        getPrefs(context).edit {
            remove(KEY_USER_ID)
                .remove(KEY_USERNAME)
        }
    }

    fun isLoggedIn(context: Context): Boolean {
        return !getToken(context).isNullOrEmpty()
    }
}

// 用于非Context环境获取Token
object GlobalTokenManager {
    private var token: String? = null

    fun setToken(token: String?) {
        this.token = token
    }

    fun getToken(): String? = token
}