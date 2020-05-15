package sk.andrejmik.weatherapp.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import sk.andrejmik.weatherapp.api_repository.APIWeatherInfoRepository
import sk.andrejmik.weatherapp.objects.WeatherInfo
import sk.andrejmik.weatherapp.repository_interface.IWeatherInfoRepository

class HomeViewModel : ViewModel()
{
    private var weatherInfoRepository: IWeatherInfoRepository = APIWeatherInfoRepository()

    private val weatherInfo: MutableLiveData<WeatherInfo> by lazy {
        MutableLiveData<WeatherInfo>()
    }

    fun getWeatherInfo(): LiveData<WeatherInfo>
    {
        return weatherInfo
    }

    fun loadWeatherInfo(cityName: String)
    {
        weatherInfoRepository.get(cityName).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(this::next, this::error, this::complete)
    }

    private fun next(loadedWeatherInfo: WeatherInfo)
    {
        weatherInfo.postValue(loadedWeatherInfo)
    }

    private fun complete()
    {
        //onEvent.postValue(Event(LoadEvent.COMPLETE))
    }

    private fun error(t: Throwable)
    {
        //onEvent.postValue(Event(LoadEvent.UNKNOWN_ERROR))
    }

    // TODO: Implement the ViewModel
}
