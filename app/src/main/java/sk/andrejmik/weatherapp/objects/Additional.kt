package sk.andrejmik.weatherapp.objects

import com.google.gson.annotations.SerializedName

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
}