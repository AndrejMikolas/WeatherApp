package sk.andrejmik.weatherapp.objects

import com.google.gson.annotations.SerializedName

open class Wind
{
    @SerializedName("speed")
    var speed: Double = 0.0

    @SerializedName("deg")
    var degree: Int = 0
}