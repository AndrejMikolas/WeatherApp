package sk.andrejmik.weatherapp.objects

import com.google.gson.annotations.SerializedName

open class Additional
{
    @SerializedName("type")
    var type: Int = 0

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("message")
    var message: Double = 0.0

    @SerializedName("sunrise")
    var sunrise: Double = 0.0

    @SerializedName("sunset")
    var sunset: Double = 0.0

    @SerializedName("country")
    var countryCode: String = ""
}