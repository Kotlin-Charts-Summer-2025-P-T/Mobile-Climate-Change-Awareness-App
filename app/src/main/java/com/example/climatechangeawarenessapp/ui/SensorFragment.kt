package com.example.climatechangeawarenessapp.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.climatechangeawarenessapp.databinding.FragmentApiBinding

class SensorFragment : Fragment(), SensorEventListener {

    private lateinit var binding: FragmentApiBinding
    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private var tempSensor: Sensor? = null
    private var humiditySensor: Sensor? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentApiBinding.inflate(inflater, container, false)

        // Hide climate data and show only sensor data
        binding.layoutClimateData.visibility = View.GONE
        binding.layoutSensorData.visibility = View.VISIBLE

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)

        Log.d("Sensors", "Temp sensor: $tempSensor, Humidity sensor: $humiditySensor")

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        lightSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
        tempSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
        humiditySensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
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
}
