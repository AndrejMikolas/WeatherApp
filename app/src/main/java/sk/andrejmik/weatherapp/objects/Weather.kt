package sk.andrejmik.weatherapp.objects

import com.google.gson.annotations.SerializedName

/**
 * Basic weather info
 */
open class Weather : BaseObject()
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