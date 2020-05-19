package sk.andrejmik.weatherapp.repository_interface

import sk.andrejmik.weatherapp.api_repository.APIWeatherInfoRepository

class RepositoryProvider
{
    companion object
    {
        private val repositoryType = RepositoryType.API

        fun getWeatherInfoRepository(): IWeatherInfoRepository
        {
            when (repositoryType)
            {
                RepositoryType.API -> return APIWeatherInfoRepository()
            }
        }
    }

    enum class RepositoryType
    {
        API
    }
}