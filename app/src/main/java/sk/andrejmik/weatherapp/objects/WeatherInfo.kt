package sk.andrejmik.weatherapp.objects

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

/**
 * Full weather info
 */
open class WeatherInfo : BaseObject()
{
    @SerializedName("id")
    var cityId: Int = 0

    @SerializedName("name")
    var cityName: String = ""

    @SerializedName("visibility")
    var visibility: Int = 0

    @SerializedName("dt")
    var dateTime: Long = 0

    @SerializedName("timezone")
    var timeZone: Int = 0

    @SerializedName("cod")
    var cod: Int = 0

    @SerializedName("coord")
    lateinit var coordinates: Coordinates

    @SerializedName("weather")
    lateinit var weather: List<Weather>

    @SerializedName("base")
    lateinit var base: String

    @SerializedName("main")
    lateinit var temperature: Temperature

    @SerializedName("sys")
    lateinit var additional: Additional

    @SerializedName("wind")
    lateinit var wind: Wind

    @SerializedName("clouds")
    lateinit var clouds: Clouds

    fun getDateTimeFormatted(): String
    {
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        return formatter.format(Date(dateTime * 1000))
    }
}