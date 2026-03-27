package com.example.integrate

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
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

        // 設定點擊事件，直接跳轉到對應功能頁面
        imageButton1.setOnClickListener {
            startActivity(Intent(this, cal_bmi_kt::class.java))  // BMI 計算頁
        }
        imageButton2.setOnClickListener {
            startActivity(Intent(this, tmp_kt::class.java))       // 溫度轉換頁
        }
        imageButton3.setOnClickListener {
            startActivity(Intent(this, guess_kt::class.java))     // 猜數字遊戲頁
        }
        imageButton4.setOnClickListener {
            startActivity(Intent(this, tic_kt::class.java))       // 井字遊戲頁
        }
        imageButton5.setOnClickListener {
            startActivity(Intent(this, snake_kt::class.java))     // 貪吃蛇遊戲頁
        }
        imageButton6.setOnClickListener {
            startActivity(Intent(this, mines_kt::class.java))     // 踩地雷遊戲頁
        }
    }
}
