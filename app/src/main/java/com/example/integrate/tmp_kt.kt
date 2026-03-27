package com.example.integrate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class tmp_kt : AppCompatActivity() {

    // 使用者輸入溫度欄位
    private lateinit var inputTemperature: EditText

    // 顯示轉換後溫度
    private lateinit var outputTemperature: TextView

    // 攝氏轉華氏按鈕
    private lateinit var celsiusToFahrenheitButton: Button

    // 華氏轉攝氏按鈕
    private lateinit var fahrenheitToCelsiusButton: Button

    // 返回選單按鈕
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // 開啟邊緣延伸模式，畫面會延伸到系統欄

        setContentView(R.layout.tmp)

        // 設置安全區域內距
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 初始化 View 元件
        inputTemperature = findViewById(R.id.Now)
        outputTemperature = findViewById(R.id.After_conversion)
        celsiusToFahrenheitButton = findViewById(R.id.CtoF)
        fahrenheitToCelsiusButton = findViewById(R.id.FtoC)
        backButton = findViewById(R.id.btnBackToMenu)

        // 攝氏轉華氏按鈕事件
        celsiusToFahrenheitButton.setOnClickListener {
            val inputText = inputTemperature.text.toString()
            if (inputText.isNotEmpty()) {
                val celsius = inputText.toDoubleOrNull()
                if (celsius != null) {
                    val fahrenheit = celsius * 9 / 5 + 32 // 計算華氏
                    outputTemperature.text = getString(R.string.fahrenheit_format, fahrenheit) // 顯示結果
                    inputTemperature.error = null // 清除錯誤提示
                } else {
                    outputTemperature.text = ""
                    inputTemperature.error = getString(R.string.invalid_input) // 顯示錯誤提示
                }
            } else {
                outputTemperature.text = "" // 若輸入空白則清空結果
                inputTemperature.error = null // 清除錯誤提示
            }
        }

        // 華氏轉攝氏按鈕事件
        fahrenheitToCelsiusButton.setOnClickListener {
            val inputText = inputTemperature.text.toString()
            if (inputText.isNotEmpty()) {
                val fahrenheit = inputText.toDoubleOrNull()
                if (fahrenheit != null) {
                    val celsius = (fahrenheit - 32) * 5 / 9 // 計算攝氏
                    outputTemperature.text = getString(R.string.celsius_format, celsius) // 顯示結果
                    inputTemperature.error = null // 清除錯誤提示
                } else {
                    outputTemperature.text = ""
                    inputTemperature.error = getString(R.string.invalid_input) // 顯示錯誤提示
                }
            } else {
                outputTemperature.text = "" // 若輸入空白則清空結果
                inputTemperature.error = null // 清除錯誤提示
            }
        }

        // 返回選單按鈕事件
        backButton.setOnClickListener {
            val intent = Intent(this, menu_kt::class.java) // 建立 Intent 跳轉到選單
            startActivity(intent)
            finish() // 結束目前畫面，避免返回此畫面
        }
    }
}
