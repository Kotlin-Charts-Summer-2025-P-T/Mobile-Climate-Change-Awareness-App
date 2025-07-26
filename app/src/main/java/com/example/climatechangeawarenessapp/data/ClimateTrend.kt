package com.example.climatechangeawareness.data

data class ClimateTrend(
    val gcm: String,
    val variable: String,
    val region: String,
    val season: String,
    val fromYear: Int,
    val toYear: Int,
    val annualData: List<Double>
)

