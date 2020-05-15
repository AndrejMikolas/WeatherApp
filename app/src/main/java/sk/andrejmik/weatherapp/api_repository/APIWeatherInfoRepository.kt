package sk.andrejmik.weatherapp.api_repository

import android.database.Observable
import sk.andrejmik.weatherapp.objects.WeatherInfo
import sk.andrejmik.weatherapp.repository_interface.IWeatherInfoRepository

open class APIWeatherInfoRepository : IWeatherInfoRepository
{
    override fun get(): Observable<WeatherInfo>
    {
        TODO("Not yet implemented")
    }
}