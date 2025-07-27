package com.example.climatechangeawarenessapp.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val elevation: Double,
    val current: CurrentWeather
)

data class CurrentWeather(
    val time: String,

    @SerializedName("temperature_2m")
    val temperature2m: Double,

    @SerializedName("wind_speed_10m")
    val windSpeed10m: Double
)
