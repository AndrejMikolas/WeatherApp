package sk.andrejmik.weatherapp.api_repository

import sk.andrejmik.weatherapp.BuildConfig

class APIConstants
{
    companion object
    {
        /**
         * API endpoint for current weather
         */
        const val weatherApiUrl = "https://api.openweathermap.org/data/2.5/weather?appid=${BuildConfig.WEATHER_API_KEY}&units=metric"
    }
}