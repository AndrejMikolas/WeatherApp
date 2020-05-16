package sk.andrejmik.weatherapp.fragments

import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import sk.andrejmik.weatherapp.WeatherApp
import sk.andrejmik.weatherapp.api_repository.APIWeatherInfoRepository
import sk.andrejmik.weatherapp.objects.WeatherInfo
import sk.andrejmik.weatherapp.repository_interface.IWeatherInfoRepository
import sk.andrejmik.weatherapp.utlis.Event
import sk.andrejmik.weatherapp.utlis.LoadEvent
import sk.andrejmik.weatherapp.utlis.NetworkHelper

class HomeViewModel : ViewModel()
{
    private var weatherInfoRepository: IWeatherInfoRepository = APIWeatherInfoRepository()

    private val weatherInfo: MutableLiveData<WeatherInfo> by lazy {
        MutableLiveData<WeatherInfo>()
    }
    val onEvent = MutableLiveData<Event<LoadEvent>>()
    private val searchStringLiveData = MutableLiveData<String>("")

    fun searchChanged(cityName: String)
    {
        searchStringLiveData.value = cityName
        loadWeatherInfo(false)
    }

    fun getWeatherInfo(): LiveData<WeatherInfo>
    {
        return weatherInfo
    }

    fun loadWeatherInfo(initial: Boolean)
    {
        onEvent.postValue(Event(LoadEvent.STARTED))
        if (!NetworkHelper.verifyAvailableNetwork())
        {
            onEvent.postValue(Event(LoadEvent.NETWORK_ERROR))
            return
        }
        var cityToSearch: String = ""
        if (initial)
        {
            val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(WeatherApp.getContext())
            val savedCity = prefs.getString("city", "");
            if (!savedCity.isNullOrEmpty())
            {
                cityToSearch = savedCity
            }
        } else
        {
            cityToSearch = searchStringLiveData.value!!
        }
        weatherInfoRepository.get(cityToSearch).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onNext, this::onError, this::onComplete)
    }

    private fun onNext(loadedWeatherInfo: WeatherInfo)
    {
        weatherInfo.postValue(loadedWeatherInfo)
    }

    private fun onComplete()
    {
        onEvent.postValue(Event(LoadEvent.COMPLETE))
        saveCityToSharedPrefs()
    }

    private fun onError(t: Throwable)
    {
        if (t.message.equals("404"))
        {
            onEvent.postValue(Event(LoadEvent.NOT_FOUND))
            return
        }
        onEvent.postValue(Event(LoadEvent.UNKNOWN_ERROR))
        saveCityToSharedPrefs()
    }

    private fun saveCityToSharedPrefs()
    {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(WeatherApp.getContext())
        val editor = prefs.edit()
        editor.putString("city", searchStringLiveData.value)
        editor.apply()
    }
}
