package com.babu.smartlock.sessionManager

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.sepon.katexentertainment.sessionManager.Constance.Companion.name_sharedPref
import com.sepon.katexentertainment.sessionManager.Constance.Companion.session_key_email
import com.sepon.katexentertainment.sessionManager.Constance.Companion.session_key_name
import com.sepon.katexentertainment.sessionManager.Constance.Companion.session_key_userPicUrl

class UserSessionManager(val context: Context) {

    private var pref: SharedPreferences? = null

    init {
        pref = context.getSharedPreferences(name_sharedPref, 0)
    }

    fun createSession(
                      name: String,
                      email: String,
                      userPicUrl: String) {

        pref?.edit {
            putString(session_key_name, name)
            putString(session_key_email, email)
            putString(session_key_userPicUrl, userPicUrl)

        }

    }

    var userName: String?
        get() = pref!!.getString(session_key_name, "")
        set(value) {
            pref!!.edit().putString(session_key_name, value).apply()
        }

    var userEmail: String?
        get() = pref!!.getString(session_key_email, "")
        set(value) {
            pref!!.edit().putString(session_key_email, value).apply()
        }

    var userPic: String?
        get() = pref!!.getString(session_key_userPicUrl, "")
        set(value) {
            pref!!.edit().putString(session_key_userPicUrl, value).apply()
        }
}