package sk.andrejmik.weatherapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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
            Picasso.with(context).load("https://openweathermap.org/img/wn/${weatherInfo.weather[0].icon}@2x.png").into(binding.imageViewWeaIcon)
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
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        swipe_container.setOnRefreshListener(this)
        setupObservers()
        setupViews()
        setupListeners()
        loadWeather()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val activity = (requireActivity() as AppCompatActivity)
        activity.setSupportActionBar(bottom_bar)
    }

    private fun setupViews()
    {
        searchview_city.setIconifiedByDefault(true)
        searchview_city.isFocusable = true
    }

    //@SuppressLint("RestrictedApi")
    private fun setupListeners()
    {
        fab_search.setOnClickListener {
            searchview_city.visibility = View.VISIBLE
            searchview_city.isIconified = false
            fab_search.visibility = View.GONE
        }
        searchview_city.setOnCloseListener {
            fab_search.visibility = View.VISIBLE
            searchview_city.visibility = View.GONE
            true
        }
        searchview_city.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query: String?): Boolean
            {
                fab_search.visibility = View.VISIBLE
                searchview_city.visibility = View.GONE
                return false
                //TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean
            {
                return false
                //TODO("Not yet implemented")
            }
        })
    }

    private fun setupObservers()
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
