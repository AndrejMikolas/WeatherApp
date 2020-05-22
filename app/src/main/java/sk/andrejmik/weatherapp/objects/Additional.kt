package sk.andrejmik.weatherapp.objects

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

/**
 * Additional info for weather
 */
open class Additional : BaseObject()
{
    @SerializedName("type")
    var type: Int = 0

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("message")
    var message: Double = 0.0

    @SerializedName("sunrise")
    var sunrise: Long = 0

    @SerializedName("sunset")
    var sunset: Long = 0

    @SerializedName("country")
    var countryCode: String = ""

    fun getSunriseFormatted(): String
    {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(Date(sunrise * 1000))
    }

    fun getSunsetFormatted(): String
    {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(Date(sunset * 1000))
    }
}