package com.example.climatechangeawarenessapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.climatechangeawarenessapp.databinding.FragmentApiBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// DTO for Open-Meteo daily response
data class DailyClimateResponse(
    val latitude: Double,
    val longitude: Double,
    val daily: DailyData,
    val daily_units: DailyUnits
)

data class DailyData(
    val time: List<String>,
    val temperature_2m_mean: List<Double>
)

data class DailyUnits(
    val temperature_2m_mean: String
)

interface OpenMeteoService {
    @GET("v1/climate")
    fun getClimate(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("daily") daily: String = "temperature_2m_mean",
        @Query("models") models: String = "ERA5_LAND"
    ): Call<DailyClimateResponse>
}

class ApiFragment : Fragment() {
    private var _binding: FragmentApiBinding? = null
    private val binding get() = _binding!!

    private val api by lazy {
        Retrofit.Builder()
            .baseUrl("https://archive-api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenMeteoService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewApiTitle.text = "Climate Trend (Mean Temp)"
        fetchClimateData()
    }

    private fun fetchClimateData() {
        api.getClimate(
            lat = 6.5244,
            lon = 3.3792, // Lagos coordinates as example
            startDate = "2000-01-01",
            endDate = "2024-12-31"
        ).enqueue(object : Callback<DailyClimateResponse> {
            override fun onResponse(
                call: Call<DailyClimateResponse>,
                response: Response<DailyClimateResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    val temps = body.daily.temperature_2m_mean
                    val dates = body.daily.time
                    val unit = body.daily_units.temperature_2m_mean
                    binding.textViewApiDetails.text = buildString {
                        append("Last Year Avg Temp: ${temps.last()} $unit\n")
                        append("Date: ${dates.last()}")
                    }
                } else {
                    binding.textViewApiDetails.text = "Error: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<DailyClimateResponse>, t: Throwable) {
                binding.textViewApiDetails.text = "Failure: ${t.message}"
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
