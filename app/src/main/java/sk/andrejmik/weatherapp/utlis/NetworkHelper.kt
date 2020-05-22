package sk.andrejmik.weatherapp.utlis

import android.content.Context
import android.net.ConnectivityManager
import sk.andrejmik.weatherapp.WeatherApp

/**
 * Helper for network status on device
 */
open class NetworkHelper
{
    companion object
    {
        /**
         * If device has available network
         */
        fun verifyAvailableNetwork(): Boolean
        {
            val connectivityManager = WeatherApp.getContext()?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }

}