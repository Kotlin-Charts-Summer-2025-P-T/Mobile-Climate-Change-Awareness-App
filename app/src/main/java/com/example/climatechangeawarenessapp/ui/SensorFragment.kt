package com.example.climatechangeawarenessapp.ui

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.climatechangeawarenessapp.databinding.FragmentSensorBinding
import com.example.climatechangeawarenessapp.util.SensorHelper

class SensorFragment : Fragment(), SensorEventListener {
    private lateinit var sensorHelper: SensorHelper
    private lateinit var binding: FragmentSensorBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSensorBinding.inflate(inflater, container, false)
        sensorHelper = SensorHelper(requireContext())
        registerSensors()
        return binding.root
    }

    private fun registerSensors() {
        sensorHelper.getAvailableSensors().forEach { (_, sensor) ->
            sensor?.let { sensorHelper.registerListener(it, this) }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_AMBIENT_TEMPERATURE -> binding.tempValue.text = "${it.values[0]}°C"
                Sensor.TYPE_LIGHT -> binding.lightValue.text = "${it.values[0]} lx"
                Sensor.TYPE_RELATIVE_HUMIDITY -> binding.humidityValue.text = "${it.values[0]}%"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    override fun onDestroyView() {
        super.onDestroyView()
        sensorHelper.unregisterListener(this)
    }
}
