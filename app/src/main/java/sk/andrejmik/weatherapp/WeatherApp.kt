package sk.andrejmik.weatherapp

import android.app.Application
import android.content.Context

open class WeatherApp : Application()
{
    companion object
    {
        private var appInstance: Application? = null

        fun getContext(): Context?
        {
            return appInstance!!.applicationContext
        }
    }

    override fun onCreate()
    {
        super.onCreate()
        appInstance = this
    }
}