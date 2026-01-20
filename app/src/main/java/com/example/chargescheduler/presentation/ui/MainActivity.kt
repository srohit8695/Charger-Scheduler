package com.example.chargescheduler.presentation.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.chargescheduler.R
import com.example.chargescheduler.databinding.ActivityMainBinding
import com.example.chargescheduler.domain.scheduler.ChargingSchedule
import com.example.chargescheduler.presentation.viewmodel.ChargingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val chargingViewModel: ChargingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        lifecycleScope.launch {
            chargingViewModel.schedule.collectLatest { schedule ->
                schedule?.let {
                    displaySchedule(it)
                }
            }
        }
    }

    private fun displaySchedule(schedule: ChargingSchedule) {
        val sb = StringBuilder("Charging Schedule:\n\n")

        for ((charger, tasks) in schedule.getSchedule()) {
            sb.append("${charger.id}:\n")
            for (task in tasks) {
                sb.append("  → ${task.truck.id} ")
                    .append("(%.2f hrs)\n".format(task.hoursRequired))
            }
            sb.append("\n")
        }

        binding.displaySchedule.text = sb.toString()

    }
}