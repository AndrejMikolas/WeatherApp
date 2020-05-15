package sk.andrejmik.weatherapp.repository_interface

import android.database.Observable
import sk.andrejmik.weatherapp.objects.WeatherInfo

interface IWeatherInfoRepository : IRepository<WeatherInfo>
{
    fun get(): Observable<WeatherInfo>
}