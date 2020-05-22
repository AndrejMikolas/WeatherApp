package sk.andrejmik.weatherapp.repository_interface

import sk.andrejmik.weatherapp.api_repository.APIWeatherInfoRepository

/**
 * Factory for getting current repository type based on param RepositoryType
 */
class RepositoryFactory
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