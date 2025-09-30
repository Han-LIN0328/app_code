package com.example.integrate

import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

// 貪吃蛇遊戲 Activity
class snake_kt : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 設定此 Activity 的畫面布局為 snake.xml
        setContentView(R.layout.snake)

        // 找到 FrameLayout 容器，準備放入自訂 SnakeView
        val container = findViewById<FrameLayout>(R.id.snakeViewContainer)

        // 建立 SnakeView (自訂貪吃蛇畫面)
        val snakeView = SnakeView(this)

        // 將 SnakeView 加入 FrameLayout 中，顯示在畫面上
        container.addView(snakeView)

        // 找到「返回選單」按鈕
        val backBtn = findViewById<Button>(R.id.btnBackToMenu)
        backBtn.setOnClickListener {
            finish() // 點擊後結束此 Activity，回到上一頁（通常是主選單）
        }
    }
}
