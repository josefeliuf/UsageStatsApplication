package com.example.usageapplicationstats.statsManager

import android.annotation.SuppressLint
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import java.util.*
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS

import android.app.AppOpsManager
import android.app.AppOpsManager.MODE_ALLOWED
import android.app.Application
import android.os.Process.myUid
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class UsageStatsViewModel(application: Application): AndroidViewModel(application) {

    private lateinit var usageStatsManager: UsageStatsManager
    private lateinit var packageManager: PackageManager

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    init {
        getManagers()
    }

    @SuppressLint("InlinedApi")
    private fun getManagers() {
        usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        packageManager = context.packageManager
    }

    private fun getBeginTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_WEEK, -1)
        return calendar.timeInMillis
    }

    fun retrieveUsageStats() {
        val beginTime = getBeginTime()
        val endTime = System.currentTimeMillis()
        Log.d("UsageStats", "beginTime: $beginTime, endTime: $endTime")
        viewModelScope.launch {
            while(isActive) {
                val usageStats: Map<String, UsageStats> = usageStatsManager.queryAndAggregateUsageStats(beginTime, endTime)
                for ((k, v) in usageStats) {
                    Log.d("UsageStats", "package: $k, totalTimeInForeground: ${v.totalTimeInForeground}")
                }
                delay(10000)
            }
        }
    }

    fun checkForPermission(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, myUid(), context.packageName)
        return mode == MODE_ALLOWED
    }

}