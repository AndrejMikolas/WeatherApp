package sk.andrejmik.weatherapp.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import sk.andrejmik.weatherapp.R
import sk.andrejmik.weatherapp.WeatherApp
import sk.andrejmik.weatherapp.objects.WeatherInfo
import sk.andrejmik.weatherapp.repository_interface.IWeatherInfoRepository
import sk.andrejmik.weatherapp.repository_interface.RepositoryFactory
import sk.andrejmik.weatherapp.utlis.Event
import sk.andrejmik.weatherapp.utlis.LoadEvent
import sk.andrejmik.weatherapp.utlis.NetworkHelper
import sk.andrejmik.weatherapp.utlis.PreferencesManager

class HomeViewModel : ViewModel()
{
    private var weatherInfoRepository: IWeatherInfoRepository = RepositoryFactory.getWeatherInfoRepository()
    private val weatherInfo: MutableLiveData<WeatherInfo> by lazy {
        MutableLiveData<WeatherInfo>()
    }
    private val searchStringLiveData = MutableLiveData("")
    private var isInitialLoad = true
    val onEvent = MutableLiveData<Event<LoadEvent>>()

    /**
     * Load weather data on init
     */
    init
    {
        loadWeatherInfo()
    }

    /**
     * Method used to notify this viewmodel when user searches for new city
     */
    fun searchChanged(cityName: String)
    {
        searchStringLiveData.value = cityName
        loadWeatherInfo()
    }

    fun getWeatherInfo(): LiveData<WeatherInfo>
    {
        return weatherInfo
    }

    /**
     * Starts loading weather info from current type of repository
     */
    fun loadWeatherInfo()
    {
        onEvent.postValue(Event(LoadEvent.STARTED))
        if (!NetworkHelper.verifyAvailableNetwork())
        {
            onEvent.postValue(Event(LoadEvent.NETWORK_ERROR))
            return
        }
        val cityToSearch: String
        if (isInitialLoad || searchStringLiveData.value == null)
        {
            val savedCity = PreferencesManager.getString(PreferencesManager.PREFERENCES_KEY_SEARCHED_CITY)
            cityToSearch = if (savedCity.isNotEmpty())
            {
                savedCity
            } else
            {
                WeatherApp.getContext()!!.resources.getString(R.string.default_city)
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
        isInitialLoad = false
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
        searchStringLiveData.value?.let { PreferencesManager.putString(PreferencesManager.PREFERENCES_KEY_SEARCHED_CITY, it) }
    }
}
