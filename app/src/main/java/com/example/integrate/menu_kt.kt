package com.example.integrate

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class menu_kt : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)  // 載入 menu.xml 介面

        // 綁定畫面上的六個 ImageButton
        val imageButton1: ImageButton = findViewById(R.id.imageButton1)
        val imageButton2: ImageButton = findViewById(R.id.imageButton2)
        val imageButton3: ImageButton = findViewById(R.id.imageButton3)
        val imageButton4: ImageButton = findViewById(R.id.imageButton4)
        val imageButton5: ImageButton = findViewById(R.id.imageButton5)
        val imageButton6: ImageButton = findViewById(R.id.imageButton6)

        // 設定點擊事件 → 呼叫 handleButtonClick
        imageButton1.setOnClickListener {
            handleButtonClick("按鈕 1")
        }
        imageButton2.setOnClickListener {
            handleButtonClick("按鈕 2")
        }
        imageButton3.setOnClickListener {
            handleButtonClick("按鈕 3")
        }
        imageButton4.setOnClickListener {
            handleButtonClick("按鈕 4")
        }
        imageButton5.setOnClickListener {
            handleButtonClick("按鈕 5")
        }
        imageButton6.setOnClickListener {
            handleButtonClick("按鈕 6")
        }
    }

    // 處理按鈕點擊事件
    private fun handleButtonClick(buttonName: String) {
        // 顯示 Toast 提示哪個按鈕被點擊
        Toast.makeText(this, "$buttonName 被點擊了！", Toast.LENGTH_SHORT).show()

        // 根據按鈕名稱跳轉到對應功能頁面
        when (buttonName) {
            "按鈕 1" -> {
                val intent = Intent(this, cal_bmi_kt::class.java)  // BMI 計算頁
                startActivity(intent)
            }
            "按鈕 2" -> {
                val intent = Intent(this, tmp_kt::class.java)       // 溫度轉換頁
                startActivity(intent)
            }
            "按鈕 3" -> {
                val intent = Intent(this, guess_kt::class.java)     // 猜數字遊戲頁
                startActivity(intent)
            }
            "按鈕 4" -> {
                val intent = Intent(this, tic_kt::class.java)       // 井字遊戲頁
                startActivity(intent)
            }
            "按鈕 5" -> {
                val intent = Intent(this, snake_kt::class.java)     // 貪吃蛇遊戲頁
                startActivity(intent)
            }
            "按鈕 6" -> {
                val intent = Intent(this, mines_kt::class.java)     // 踩地雷遊戲頁
                startActivity(intent)
            }
        }
    }
}
