package sk.andrejmik.weatherapp.utlis

import android.content.Context
import android.net.ConnectivityManager
import sk.andrejmik.weatherapp.WeatherApp


open class NetworkHelper
{
    companion object
    {
        fun verifyAvailableNetwork(): Boolean
        {
            val connectivityManager = WeatherApp.getContext()?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }

}