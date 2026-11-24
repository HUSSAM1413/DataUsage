package com.example.datausage

import android.app.AppOpsManager
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.net.ConnectivityManager
import android.os.Process
import java.util.Calendar

object DataUsageHelper {

    fun hasPermission(context: Context): Boolean {
        val ops = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = ops.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    // billingDay = يوم بداية دورة الباقة (مثلاً 1 أو 15)
    fun getMonthlyMobileBytes(context: Context, billingDay: Int = 1): Long {
        val nsm = context.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager

        val now = Calendar.getInstance()
        val start = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, billingDay)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (after(now)) add(Calendar.MONTH, -1)
        }

        // TYPE_MOBILE فقط = بيانات الجوال، الواي فاي مستبعد
        val bucket = nsm.querySummaryForDevice(
            ConnectivityManager.TYPE_MOBILE,
            null,
            start.timeInMillis,
            now.timeInMillis
        )
        return bucket.rxBytes + bucket.txBytes
    }

    fun formatBytes(bytes: Long): String {
        val mb = bytes / (1024.0 * 1024.0)
        return if (mb >= 1024) "%.2f GB".format(mb / 1024) else "%.1f MB".format(mb)
    }
}
