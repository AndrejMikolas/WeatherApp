package sk.andrejmik.weatherapp.utlis

import android.content.SharedPreferences
import android.preference.PreferenceManager
import sk.andrejmik.weatherapp.WeatherApp

class PreferencesManager
{
    companion object
    {
        //region KEYS
        const val PREFERENCES_KEY_SEARCHED_CITY = "city"
        //endregion

        //region INTERNAL METHODS
        private fun getSharedPreferences(): SharedPreferences
        {
            return PreferenceManager.getDefaultSharedPreferences(WeatherApp.getContext())
        }

        private fun getEditor(): SharedPreferences.Editor
        {
            val prefs = getSharedPreferences()
            val editor = prefs.edit()
            editor.clear()
            return editor
        }
        //endregion

        public fun putString(key: String, value: String)
        {
            val editor = getEditor()
            editor.putString(key, value)
            editor.apply()
        }

        public fun getString(key: String): String
        {
            val prefs = getSharedPreferences()
            return prefs.getString(key, "")!!
        }
    }
}