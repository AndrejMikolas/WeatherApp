package sk.andrejmik.weatherapp.api_repository

import android.util.Log
import androidx.annotation.NonNull
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import sk.andrejmik.weatherapp.objects.WeatherInfo
import sk.andrejmik.weatherapp.repository_interface.IWeatherInfoRepository
import java.io.IOException
import java.util.concurrent.TimeUnit

open class APIWeatherInfoRepository : IWeatherInfoRepository
{
    val TAG = "WEAT_API"

    override fun get(@NonNull cityName: String): Observable<WeatherInfo>
    {
        val requestUrl: String = APIConstants.weatherApiUrl.plus("&q=$cityName")
        val client = OkHttpClient()
        val request = Request.Builder().url(requestUrl).get().build()

        return Observable.create<WeatherInfo> { emitter ->
            client.newCall(request).enqueue(object : Callback
            {
                override fun onFailure(call: Call, e: IOException)
                {
                    emitter.onError(e)
                }

                override fun onResponse(call: Call, response: Response)
                {
                    if (response.isSuccessful)
                    {
                        try
                        {
                            val jsonBody = response.body?.string()
                            val result = Gson().fromJson(
                                jsonBody.toString(),
                                WeatherInfo::class.java
                            )
                            emitter.onNext(result)
                            emitter.onComplete()
                        } catch (e: Exception)
                        {
                            Log.e(TAG, "Failed to deserialize JSON body from $requestUrl: ${e.message}")
                            emitter.onError(e)
                        }
                    } else
                    {
                        emitter.onError(IllegalStateException("${response.code}"))
                    }
                }
            })
            emitter.setCancellable { }
        }.timeout(10, TimeUnit.SECONDS).subscribeOn(Schedulers.computation()).observeOn(Schedulers.computation())
    }

}