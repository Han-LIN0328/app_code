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
    private var answer = 0
    private var guessCount = 0
    private var lowerBound = 1
    private var upperBound = 100

    // 畫面元件
    private lateinit var input: EditText
    private lateinit var result: TextView
    private lateinit var showAnswer: TextView
    private lateinit var scope: TextView

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
        input = findViewById(R.id.pleasein)
        result = findViewById(R.id.several)
        showAnswer = findViewById(R.id.showanswer)
        scope = findViewById(R.id.scope)

        val guessBtn = findViewById<Button>(R.id.button2)
        val revealBtn = findViewById<Button>(R.id.button1)
        val resetBtn = findViewById<Button>(R.id.button3)
        val backBtn = findViewById<Button>(R.id.btnBackToMenu)

        // 返回上一頁
        backBtn.setOnClickListener {
            finish()
        }

        // 猜數字邏輯
        guessBtn.setOnClickListener {
            val guessText = input.text.toString()
            if (guessText.isEmpty()) {
                input.error = getString(R.string.please_enter_a_number)
                return@setOnClickListener
            }

            val guess = guessText.toIntOrNull()
            if (guess == null || guess !in lowerBound..upperBound) {
                input.error = getString(R.string.please_enter_a_number_in_range, lowerBound, upperBound)
                return@setOnClickListener
            }

            guessCount++
            result.text = getString(R.string.guess_count, guessCount)

            when {
                guess == answer -> {
                    showAnswer.text = getString(R.string.congratulations_correct_answer, answer)
                }
                guess > answer -> {
                    upperBound = guess - 1
                    showAnswer.text = getString(R.string.too_high)
                }
                else -> {
                    lowerBound = guess + 1
                    showAnswer.text = getString(R.string.too_low)
                }
            }

            scope.text = getString(R.string.current_range, lowerBound, upperBound)
            input.text.clear()
            input.error = null // 清除錯誤提示
        }

        // 公布答案
        revealBtn.setOnClickListener {
            showAnswer.text = getString(R.string.correct_answer_is, answer)
        }

        // 重置遊戲
        resetBtn.setOnClickListener {
            resetGame()
        }

        // 初始化遊戲
        resetGame()
    }

    // 初始化或重置遊戲
    private fun resetGame() {
        answer = Random.nextInt(1, 101) // 生成 1~100 的答案
        guessCount = 0
        lowerBound = 1
        upperBound = 100
        input.text.clear()
        input.error = null
        result.text = ""
        showAnswer.text = ""
        scope.text = getString(R.string.current_range, lowerBound, upperBound)
    }
}
