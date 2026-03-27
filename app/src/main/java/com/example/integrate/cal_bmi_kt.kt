package com.example.integrate

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class cal_bmi_kt : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cal_bmi) // 載入 cal_bmi.xml 介面

        // 設定邊界補白，避免系統狀態列或導航列遮擋
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 綁定畫面元件
        val heightInput = findViewById<EditText>(R.id.in_ht)      // 身高輸入
        val weightInput = findViewById<EditText>(R.id.in_kg)      // 體重輸入
        val resultText = findViewById<TextView>(R.id.bmi)         // BMI 結果顯示
        val calculateButton = findViewById<Button>(R.id.button)   // 計算按鈕
        val backButton = findViewById<Button>(R.id.btn_back)      // 返回按鈕

        // 計算按鈕點擊事件
        calculateButton.setOnClickListener {
            // 清除先前的錯誤提示
            heightInput.error = null
            weightInput.error = null

            val heightStr = heightInput.text.toString()
            val weightStr = weightInput.text.toString()

            val height = heightStr.toFloatOrNull()
            val weight = weightStr.toFloatOrNull()

            if (height != null && weight != null && height > 0 && weight > 0) {
                // 將身高轉為公尺，計算 BMI
                val heightInMeters = height / 100
                val bmi = weight / (heightInMeters * heightInMeters)

                // 判斷 BMI 分類
                val result = when {
                    bmi < 18.5 -> getString(R.string.bmi_underweight)
                    bmi < 24 -> getString(R.string.bmi_normal)
                    bmi < 27 -> getString(R.string.bmi_overweight)
                    bmi < 30 -> getString(R.string.bmi_obese_mild)
                    bmi < 35 -> getString(R.string.bmi_obese_moderate)
                    else -> getString(R.string.bmi_obese_severe)
                }

                // 顯示結果並設為可見
                resultText.text = getString(R.string.bmi_result_format, bmi, result)
                resultText.visibility = View.VISIBLE
            } else {
                // 欄位資料不完整或無效
                if (heightStr.isEmpty()) {
                    heightInput.error = getString(R.string.data_error)
                }
                if (weightStr.isEmpty()) {
                    weightInput.error = getString(R.string.data_error)
                }
                resultText.text = ""
                resultText.visibility = View.GONE
            }
        }

        // 返回按鈕點擊事件 → 回到選單畫面
        backButton.setOnClickListener {
            finish()
        }
    }
}
