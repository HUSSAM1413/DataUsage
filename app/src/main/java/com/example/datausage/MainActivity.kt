package com.example.datausage

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvUsage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvUsage = findViewById(R.id.tvUsage)
        findViewById<Button>(R.id.btnPermission).setOnClickListener {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
    }

    override fun onResume() {
        super.onResume()
        if (DataUsageHelper.hasPermission(this)) {
            val bytes = DataUsageHelper.getMonthlyMobileBytes(this, billingDay = 1)
            tvUsage.text = DataUsageHelper.formatBytes(bytes)
        } else {
            tvUsage.text = "فعّل صلاحية الوصول لبيانات الاستخدام"
        }
    }
}
