package sk.andrejmik.weatherapp.utlis

/**
 * Type of events when loading data
 */
enum class LoadEvent
{
    STARTED, COMPLETE, UNKNOWN_ERROR, NETWORK_ERROR, NO_MORE, NOT_FOUND
}