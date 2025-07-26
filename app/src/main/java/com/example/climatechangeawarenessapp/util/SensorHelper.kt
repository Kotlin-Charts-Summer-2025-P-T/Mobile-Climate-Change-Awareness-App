package com.example.climatechangeawarenessapp.util

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class SensorHelper(context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    fun getAvailableSensors(): Map<String, Sensor?> {
        return mapOf(
            "Temperature" to sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
            "Light" to sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
            "Humidity" to sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
        )
    }

    fun registerListener(sensor: Sensor, listener: SensorEventListener) {
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun unregisterListener(listener: SensorEventListener) {
        sensorManager.unregisterListener(listener)
    }
}
