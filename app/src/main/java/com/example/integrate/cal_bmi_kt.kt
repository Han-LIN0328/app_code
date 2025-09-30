package com.example.integrate

import android.os.Bundle
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
            val heightStr = heightInput.text.toString()
            val weightStr = weightInput.text.toString()

            if (heightStr.isNotEmpty() && weightStr.isNotEmpty()) {
                // 將身高轉為公尺，計算 BMI
                val height = heightStr.toFloat() / 100
                val weight = weightStr.toFloat()
                val bmi = weight / (height * height)

                // 判斷 BMI 分類
                val result = when {
                    bmi < 18.5 -> "過輕"
                    bmi < 24 -> "正常"
                    bmi < 27 -> "過重"
                    bmi < 30 -> "輕度肥胖"
                    bmi < 35 -> "中度肥胖"
                    else -> "重度肥胖"
                }

                // 顯示結果
                resultText.text = "BMI: %.2f\n結果：%s".format(bmi, result)
            } else {
                // 欄位資料不完整
                resultText.text = "資料有誤，請輸入完整資料"
            }
        }

        // 返回按鈕點擊事件 → 回到選單畫面
        backButton.setOnClickListener {
            finish()
        }
    }
}
