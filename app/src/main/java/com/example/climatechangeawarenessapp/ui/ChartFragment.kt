package com.example.climatechangeawarenessapp.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.climatechangeawarenessapp.databinding.FragmentChartBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class ChartFragment : Fragment() {
    private lateinit var binding: FragmentChartBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChartBinding.inflate(inflater, container, false)
        setupLineChart()
        return binding.root
    }

    private fun setupLineChart() {
        val entries = ArrayList<Entry>()
        entries.add(Entry(2020f, 0.4f))
        entries.add(Entry(2021f, 0.5f))
        entries.add(Entry(2022f, 0.6f))

        val dataSet = LineDataSet(entries, "Global Temp Anomaly")
        dataSet.color = Color.RED
        dataSet.valueTextColor = Color.BLACK

        val lineData = LineData(dataSet)
        binding.chart.data = lineData
        binding.chart.invalidate()
    }
}
