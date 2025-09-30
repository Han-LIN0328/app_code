package com.example.integrate

import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class guess_kt : AppCompatActivity() {
    // 遊戲答案與狀態
    private var answer = 0          // 正確答案
    private var guessCount = 0      // 猜的次數
    private var lowerBound = 1      // 最小範圍
    private var upperBound = 100    // 最大範圍

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // 支援全螢幕邊界顯示
        setContentView(R.layout.guess)

        // 調整系統邊界 inset
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 取得畫面元件
        val input = findViewById<EditText>(R.id.pleasein)       // 輸入框
        val result = findViewById<TextView>(R.id.several)       // 顯示猜的次數
        val showAnswer = findViewById<TextView>(R.id.showanswer) // 顯示提示或答案
        val scope = findViewById<TextView>(R.id.scope)          // 顯示目前猜測範圍

        val guessBtn = findViewById<Button>(R.id.button2)       // 執行猜測按鈕
        val revealBtn = findViewById<Button>(R.id.button1)      // 公布答案按鈕
        val resetBtn = findViewById<Button>(R.id.button3)       // 再來一局按鈕
        val backBtn = findViewById<Button>(R.id.btnBackToMenu)  // 返回選單按鈕

        // 返回上一頁
        backBtn.setOnClickListener {
            finish()
        }

        // 初始化或重置遊戲
        fun resetGame() {
            answer = Random.nextInt(1, 101) // 生成 1~100 的答案
            guessCount = 0
            lowerBound = 1
            upperBound = 100
            input.text.clear()
            result.text = ""
            showAnswer.text = ""
            scope.text = "你的目前範圍：$lowerBound ~ $upperBound"
        }

        // 猜數字邏輯
        guessBtn.setOnClickListener {
            val guessText = input.text.toString()
            if (guessText.isEmpty()) {
                Toast.makeText(this, "請輸入一個數字", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val guess = guessText.toIntOrNull()
            if (guess == null || guess !in lowerBound..upperBound) {
                Toast.makeText(this, "請輸入 $lowerBound ~ $upperBound 之間的數字", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            guessCount++
            result.text = "猜的次數：$guessCount"

            when {
                guess == answer -> {
                    showAnswer.text = "恭喜你猜對了！答案是 $answer"
                }
                guess > answer -> {
                    upperBound = guess - 1
                    showAnswer.text = "太大了！"
                }
                else -> {
                    lowerBound = guess + 1
                    showAnswer.text = "太小了！"
                }
            }

            scope.text = "你的目前範圍：$lowerBound ~ $upperBound"
            input.text.clear()
        }

        // 公布答案
        revealBtn.setOnClickListener {
            showAnswer.text = "正確答案是：$answer"
        }

        // 重置遊戲
        resetBtn.setOnClickListener {
            resetGame()
        }

        // 初始化遊戲
        resetGame()
    }
}
