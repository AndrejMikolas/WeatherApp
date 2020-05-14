package sk.andrejmik.weatherapp.objects

import com.google.gson.annotations.SerializedName

open class Weather
{
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("main")
    var main: String = ""

    @SerializedName("description")
    var description: String = ""

    @SerializedName("icon")
    var icon: String = ""
}