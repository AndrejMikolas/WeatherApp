package sk.andrejmik.weatherapp.objects

import com.google.gson.annotations.SerializedName

/**
 * Temperature info
 */
open class Temperature : BaseObject()
{
    @SerializedName("temp")
    var currentTemp: Double = 0.0

    @SerializedName("feels_like")
    var feelTemp: Double = 0.0

    @SerializedName("temp_min")
    var minTemp: Double = 0.0

    @SerializedName("temp_max")
    var maxTemp: Double = 0.0

    @SerializedName("pressure")
    var pressure: Int = 0

    @SerializedName("humidity")
    var humidity: Int = 0
}