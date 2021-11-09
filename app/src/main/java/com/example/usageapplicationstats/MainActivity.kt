package com.example.usageapplicationstats

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import com.example.usageapplicationstats.statsManager.UsageStatsViewModel

class MainActivity : AppCompatActivity() {

    private val usageStatsViewModel: UsageStatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!usageStatsViewModel.checkForPermission()) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(intent)
        }

        getTotalTimeInForeground()

    }

    private fun getTotalTimeInForeground() {

        usageStatsViewModel.retrieveUsageStats()
    }


}