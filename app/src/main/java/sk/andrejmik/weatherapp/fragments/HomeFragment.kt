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
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_fragment.*
import sk.andrejmik.weatherapp.R
import sk.andrejmik.weatherapp.databinding.HomeFragmentBinding
import sk.andrejmik.weatherapp.objects.WeatherInfo
import sk.andrejmik.weatherapp.utlis.Event
import sk.andrejmik.weatherapp.utlis.LoadEvent

class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener
{
    //region Private
    private lateinit var binding: HomeFragmentBinding
    private lateinit var viewModel: HomeViewModel

    private lateinit var snackNetworkError: Snackbar
    private lateinit var snackUnknownError: Snackbar
    private lateinit var snackNotFound: Snackbar

    private val weatherInfoLoadedObserver = Observer<WeatherInfo> { weatherInfo ->
        weatherInfo?.let {
            binding.weatherInfo = weatherInfo
            Picasso.with(context).load("https://openweathermap.org/img/wn/${weatherInfo.weather[0].icon}@2x.png").into(binding.imageViewWeaIcon)
        }
    }
    /**
     * Observer for handling events of loading weather info. For various types of event it either starts or stops refreshing animation, enable or
     * disable swipe container and shows or dismiss informational snackbars
     */
    private val eventObserver = Observer<Event<LoadEvent>> { event ->
        event?.getContentIfNotHandled()?.let {
            when (event.peekContent())
            {
                LoadEvent.UNKNOWN_ERROR ->
                {
                    controlSnack(snackUnknownError, true)
                    controlSnack(snackNetworkError, false)
                    swipe_container.isEnabled = true
                    swipe_container.isRefreshing = false
                }
                LoadEvent.NETWORK_ERROR ->
                {
                    controlSnack(snackUnknownError, false)
                    controlSnack(snackNetworkError, true)
                    swipe_container.isEnabled = true
                    swipe_container.isRefreshing = false
                }
                LoadEvent.COMPLETE ->
                {
                    controlSnack(snackUnknownError, false)
                    controlSnack(snackNetworkError, false)
                    swipe_container.isEnabled = true
                    swipe_container.isRefreshing = false
                    Toast.makeText(context, resources.getString(R.string.updated), Toast.LENGTH_SHORT).show()
                }
                LoadEvent.STARTED ->
                {
                    controlSnack(snackUnknownError, false)
                    controlSnack(snackNetworkError, false)
                    swipe_container.isRefreshing = true
                }
                LoadEvent.NO_MORE ->
                {
                }
                LoadEvent.NOT_FOUND ->
                {
                    controlSnack(snackUnknownError, false)
                    controlSnack(snackNetworkError, false)
                    controlSnack(snackNotFound, true)
                    swipe_container.isEnabled = true
                    swipe_container.isRefreshing = false
                }
            }
        }
    }

    /**
     * Listener for button on error snackbar to reload data
     */
    private var clickRetryLoadListener = View.OnClickListener {
        loadWeather(false)
    }
    //endregion


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
        setupSnacks()
        loadWeather(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val activity = (requireActivity() as AppCompatActivity)
        activity.setSupportActionBar(bottom_bar)
    }

    /**
     * Load new weather info.
     * @param initial
     *      if TRUE, data will be loaded for city saved in shared preferences
     *      if FALSE, data will be loaded for last city searched by user
     */
    private fun loadWeather(initial: Boolean)
    {
        if (initial)
        {
            //TODO initial search with saved value from shared prefs
            viewModel.searchChanged("Povazska bystrica")
        } else
        {
            viewModel.loadWeatherInfo()
        }
    }

    /**
     * Preparing views
     */
    private fun setupViews()
    {
        searchview_city.setIconifiedByDefault(true)
        searchview_city.isFocusable = true
    }

    /**
     * Set listeners for views
     */
    private fun setupListeners()
    {
        fab_search.setOnClickListener {
            searchview_city.visibility = View.VISIBLE
            searchview_city.isIconified = false
            searchview_city.setQuery("", false)
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
                viewModel.searchChanged(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean
            {
                return false
            }
        })
        searchview_city.setOnQueryTextFocusChangeListener { view, focused ->
            if (!focused)
            {
                fab_search.visibility = View.VISIBLE
                searchview_city.visibility = View.GONE
            }
        }
    }

    /**
     * Setting observers
     */
    private fun setupObservers()
    {
        viewModel.getWeatherInfo().observe(viewLifecycleOwner, weatherInfoLoadedObserver)
        viewModel.onEvent.observe(viewLifecycleOwner, eventObserver)
    }

    /**
     * Creates and sets params of snackbars which will be used repeatedly
     */
    private fun setupSnacks()
    {
        snackNetworkError = Snackbar.make(swipe_container, resources.getString(R.string.network_error), Snackbar.LENGTH_INDEFINITE)
        snackNetworkError.setAction(resources.getString(R.string.retry), clickRetryLoadListener)
        snackUnknownError = Snackbar.make(swipe_container, resources.getString(R.string.unknown_error), Snackbar.LENGTH_INDEFINITE)
        snackUnknownError.setAction(resources.getString(R.string.retry), clickRetryLoadListener)
        snackNotFound = Snackbar.make(swipe_container, resources.getString(R.string.city_not_found), Snackbar.LENGTH_LONG)
    }

    /**
     * Show or dismiss snackbar provided in param
     * @param snackbar: Snackbar instance to be controlled
     * @param visible: if snackbar will be shown or dismissed
     */
    private fun controlSnack(snackbar: Snackbar, visible: Boolean)
    {
        if (visible)
        {
            snackbar.show()
        } else
        {
            snackbar.dismiss()
        }
    }

    /**
     * Override method for swipe to refresh
     */
    @Override
    override fun onRefresh()
    {
        loadWeather(false)
    }

}
