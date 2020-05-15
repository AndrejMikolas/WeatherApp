package sk.andrejmik.weatherapp.objects

import com.google.gson.annotations.SerializedName

open class Clouds : BaseObject()
{
    @SerializedName("all")
    var all: Int = 0
}