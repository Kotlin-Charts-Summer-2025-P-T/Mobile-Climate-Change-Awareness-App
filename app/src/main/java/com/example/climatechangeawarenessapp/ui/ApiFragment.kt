package com.example.climatechangeawarenessapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.climatechangeawarenessapp.databinding.FragmentApiBinding
import com.example.climatechangeawarenessapp.network.RetrofitInstance
import com.example.climatechangeawarenessapp.util.LocationHelper
import kotlinx.coroutines.launch

class ApiFragment : Fragment(), SensorEventListener {

    private lateinit var binding: FragmentApiBinding
    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private var tempSensor: Sensor? = null
    private var humiditySensor: Sensor? = null
    private lateinit var locationHelper: LocationHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentApiBinding.inflate(inflater, container, false)

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)

        // Set up location helper
        locationHelper = LocationHelper(requireContext())

        // Show climate data always
        binding.layoutClimateData.visibility = View.VISIBLE

        // Show sensor data only if available
        val hasSensor = lightSensor != null || tempSensor != null || humiditySensor != null
        binding.layoutSensorData.visibility = if (hasSensor) View.VISIBLE else View.GONE

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        lightSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
        tempSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
        humiditySensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }

        getLocationAndFetchWeather()
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_LIGHT -> {
                    binding.textViewLight.text = "Light: ${it.values[0]} lux"
                }
                Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                    binding.textViewTemperature.text = "Temperature: ${it.values[0]} °C"
                }
                Sensor.TYPE_RELATIVE_HUMIDITY -> {
                    binding.textViewHumidity.text = "Humidity: ${it.values[0]} %"
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    @SuppressLint("MissingPermission")
    private fun getLocationAndFetchWeather() {
        locationHelper.getLocation { location: Location? ->
            if (location != null) {
                fetchWeatherData(location.latitude, location.longitude)
            } else {
                binding.textViewApiDetails.text = "❌ Unable to get location."
            }
        }
    }

    private fun fetchWeatherData(latitude: Double, longitude: Double) {
        lifecycleScope.launch {
            try {
                val weatherResponse = RetrofitInstance.api.getWeatherData(
                    latitude = latitude,
                    longitude = longitude,
                    current = "temperature_2m,wind_speed_10m",
                    hourly = ""
                )
                val weather = weatherResponse.current
                val result = """
                    🌡️ Temp: ${weather.temperature2m}°C
                    🌬️ Wind: ${weather.windSpeed10m} km/h
                """.trimIndent()
                binding.textViewApiDetails.text = result
            } catch (e: Exception) {
                binding.textViewApiDetails.text = "❌ Error: ${e.localizedMessage}"
            }
        }
    }
}
