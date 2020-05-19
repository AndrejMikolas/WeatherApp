package sk.andrejmik.weatherapp.fragments

import android.content.SharedPreferences
import android.content.res.Resources
import android.preference.PreferenceManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import sk.andrejmik.weatherapp.R
import sk.andrejmik.weatherapp.WeatherApp
import sk.andrejmik.weatherapp.objects.WeatherInfo
import sk.andrejmik.weatherapp.repository_interface.IWeatherInfoRepository
import sk.andrejmik.weatherapp.repository_interface.RepositoryProvider
import sk.andrejmik.weatherapp.utlis.Event
import sk.andrejmik.weatherapp.utlis.LoadEvent
import sk.andrejmik.weatherapp.utlis.NetworkHelper

class HomeViewModel : ViewModel()
{
    private var weatherInfoRepository: IWeatherInfoRepository = RepositoryProvider.getWeatherInfoRepository()

    private val weatherInfo: MutableLiveData<WeatherInfo> by lazy {
        MutableLiveData<WeatherInfo>()
    }
    val onEvent = MutableLiveData<Event<LoadEvent>>()
    private val searchStringLiveData = MutableLiveData("")

    /**
     * Load weather data on init
     */
    init
    {
        loadWeatherInfo(true)
    }

    /**
     * Method used to notify this viewmodel when user searches for new city
     */
    fun searchChanged(cityName: String)
    {
        searchStringLiveData.value = cityName
        loadWeatherInfo(false)
    }

    fun getWeatherInfo(): LiveData<WeatherInfo>
    {
        return weatherInfo
    }

    /**
     * @param initial
     *      if TRUE, data will be loaded for city saved in shared preferences
     *      if FALSE, data will be loaded for last city searched by user
     */
    fun loadWeatherInfo(initial: Boolean)
    {
        onEvent.postValue(Event(LoadEvent.STARTED))
        if (!NetworkHelper.verifyAvailableNetwork())
        {
            onEvent.postValue(Event(LoadEvent.NETWORK_ERROR))
            return
        }
        val cityToSearch: String
        if (initial)
        {
            val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(WeatherApp.getContext())
            val savedCity = prefs.getString("city", "")
            if (!savedCity.isNullOrEmpty())
            {
                cityToSearch = savedCity
            } else
            {
                cityToSearch = WeatherApp.getContext()!!.resources.getString(R.string.default_city)
            }
            searchStringLiveData.value = cityToSearch
        } else
        {
            cityToSearch = searchStringLiveData.value!!
        }
        weatherInfoRepository.get(cityToSearch).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onNext, this::onError, this::onComplete)
    }

    /**
     * Event on next data loaded
     */
    private fun onNext(loadedWeatherInfo: WeatherInfo)
    {
        weatherInfo.postValue(loadedWeatherInfo)
    }

    /**
     * Event on completed data loading
     */
    private fun onComplete()
    {
        onEvent.postValue(Event(LoadEvent.COMPLETE))
        saveCityToSharedPrefs()
    }

    /**
     * Event on error when loading data
     */
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

    /**
     * Saving currently searched city to shared preferences
     */
    private fun saveCityToSharedPrefs()
    {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(WeatherApp.getContext())
        val editor = prefs.edit()
        editor.clear()
        editor.putString("city", searchStringLiveData.value)
        editor.apply()
    }
}
