package uz.gita.memorygamemn.data.sourse

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class Shared private constructor(val context : Context) {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var shared: Shared
        fun getInstance(context: Context): Shared {
            if (!(Companion::shared.isInitialized)) {
                shared = Shared(context)

            }
            return shared
        }
    }

    private val pref: SharedPreferences = context.getSharedPreferences("FILE", Context.MODE_PRIVATE)
    fun setTime(time: Long) {
        pref.edit().putLong("TIME", time).apply()
    }

    fun getTime(): Long {
        return pref.getLong("TIME",0)
    }


    fun setVolume(boolean: Boolean) {
        pref.edit().putBoolean("VOLUME", boolean).apply()
    }

    fun getVolume(): Boolean {
        return pref.getBoolean("VOLUME",true)
    }



}