package sk.andrejmik.weatherapp.objects

open class WeatherInfo
{
    var cityId: Int = 0
    var cityName: String = ""
    var visibility: Int = 0
    var dateTime: Long = 0
    var timeZone: Int = 0
    var cod: Int = 0
    lateinit var coordinates: Coordinates
    lateinit var weather: Weather
    lateinit var base: String
    lateinit var temperature: Temperature
    lateinit var additional: Additional
    lateinit var wind: Wind
    lateinit var clouds: Clouds
}