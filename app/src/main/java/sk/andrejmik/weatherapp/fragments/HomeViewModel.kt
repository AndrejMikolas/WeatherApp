package sk.andrejmik.weatherapp.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
        loadWeatherInfo()
    }

    fun getWeatherInfo(): LiveData<WeatherInfo>
    {
        return weatherInfo
    }

    fun loadWeatherInfo()
    {
        onEvent.postValue(Event(LoadEvent.STARTED))
        if (!NetworkHelper.verifyAvailableNetwork())
        {
            onEvent.postValue(Event(LoadEvent.NETWORK_ERROR))
            return
        }
        if (searchStringLiveData.value?.isEmpty()!!)
        {
            onEvent.postValue(Event(LoadEvent.NOT_FOUND))
            return
        }
        weatherInfoRepository.get(searchStringLiveData.value!!).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onNext, this::onError, this::onComplete)
    }

    private fun onNext(loadedWeatherInfo: WeatherInfo)
    {
        weatherInfo.postValue(loadedWeatherInfo)
    }

    private fun onComplete()
    {
        onEvent.postValue(Event(LoadEvent.COMPLETE))
    }

    private fun onError(t: Throwable)
    {
        if (t.message.equals("404"))
        {
            onEvent.postValue(Event(LoadEvent.NOT_FOUND))
            return
        }
        onEvent.postValue(Event(LoadEvent.UNKNOWN_ERROR))
    }
}
