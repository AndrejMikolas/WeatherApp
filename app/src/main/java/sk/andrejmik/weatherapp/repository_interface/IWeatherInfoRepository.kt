package sk.andrejmik.weatherapp.repository_interface

import io.reactivex.Observable
import sk.andrejmik.weatherapp.objects.WeatherInfo

/**
 * Repository declaration for WeatherInfo
 */
interface IWeatherInfoRepository : IRepository<WeatherInfo>
{
    fun get(cityName: String): Observable<WeatherInfo>
}