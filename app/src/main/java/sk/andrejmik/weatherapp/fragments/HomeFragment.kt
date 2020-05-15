package sk.andrejmik.weatherapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_fragment.*
import sk.andrejmik.weatherapp.R
import sk.andrejmik.weatherapp.databinding.HomeFragmentBinding
import sk.andrejmik.weatherapp.objects.WeatherInfo

class HomeFragment : Fragment()
{
    private lateinit var binding: HomeFragmentBinding
    private lateinit var viewModel: HomeViewModel

    private val weatherInfoLoadedObserver = Observer<WeatherInfo> { weatherInfo ->
        weatherInfo?.let {
            binding.weatherInfo = weatherInfo
            var url:String="https://openweathermap.org/img/wn/${weatherInfo.weather[0].icon}@2x.png"
            Picasso.with(context).load(url).into(binding.imageViewWeaIcon)
        }
    }

    companion object
    {
        fun newInstance() = HomeFragment()
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
        viewModel.loadWeatherInfo("Povazska bystrica")
        viewModel.getWeatherInfo().observe(viewLifecycleOwner, weatherInfoLoadedObserver)
    }

}
