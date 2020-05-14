package sk.andrejmik.weatherapp.objects

import com.google.gson.annotations.SerializedName

open class Coordinates
{
    @SerializedName("lon")
    var longitude: Double = 0.0

    @SerializedName("lat")
    var latitude: Double = 0.0
}