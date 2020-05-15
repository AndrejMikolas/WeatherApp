package sk.andrejmik.weatherapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_fragment.*
import sk.andrejmik.weatherapp.R
import sk.andrejmik.weatherapp.databinding.HomeFragmentBinding
import sk.andrejmik.weatherapp.objects.WeatherInfo
import sk.andrejmik.weatherapp.utlis.Event
import sk.andrejmik.weatherapp.utlis.LoadEvent

class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener
{
    private lateinit var binding: HomeFragmentBinding
    private lateinit var viewModel: HomeViewModel

    companion object
    {
        fun newInstance() = HomeFragment()
    }

    private val weatherInfoLoadedObserver = Observer<WeatherInfo> { weatherInfo ->
        weatherInfo?.let {
            binding.weatherInfo = weatherInfo
            var url: String = "https://openweathermap.org/img/wn/${weatherInfo.weather[0].icon}@2x.png"
            Picasso.with(context).load(url).into(binding.imageViewWeaIcon)
        }
    }

    private val eventObserver = Observer<Event<LoadEvent>> { event ->
        event?.getContentIfNotHandled()?.let {
            when (event.peekContent())
            {
                LoadEvent.UNKNOWN_ERROR ->
                {
//                    controlSnackNetworkError(false)
//                    controlSnackUnknownError(true)
                    swipe_container.isEnabled = true
                    swipe_container.isRefreshing = false
                }
                LoadEvent.NETWORK_ERROR ->
                {
//                    controlSnackNetworkError(true)
//                    controlSnackUnknownError(false)
//                    snackUnknownError.dismiss()
                    swipe_container.isEnabled = true
                    swipe_container.isRefreshing = false
                }
                LoadEvent.COMPLETE ->
                {
                    //dismissSnackBars()
                    swipe_container.isEnabled = true
                    swipe_container.isRefreshing = false
                    Toast.makeText(context, resources.getString(R.string.updated), Toast.LENGTH_SHORT).show()
                }
                LoadEvent.STARTED ->
                {
//                    dismissSnackBars()
                    swipe_container.isRefreshing = true
                }
                LoadEvent.NO_MORE ->
                {
                }
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        swipe_container.setOnRefreshListener(this)
        prepareObservers()
        loadWeather()
    }

    private fun prepareObservers()
    {
        viewModel.getWeatherInfo().observe(viewLifecycleOwner, weatherInfoLoadedObserver)
        viewModel.onEvent.observe(viewLifecycleOwner, eventObserver)
    }

    private fun loadWeather()
    {
        viewModel.loadWeatherInfo("Povazska bystrica")
    }

    @Override
    override fun onRefresh()
    {
        loadWeather()
    }

}
