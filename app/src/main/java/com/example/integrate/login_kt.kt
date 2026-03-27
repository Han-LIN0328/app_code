package com.example.integrate

import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random
import com.example.integrate.R
import com.example.integrate.menu_kt

class login_kt : AppCompatActivity() {

    // 定義登入畫面需要用到的 UI 元件
    private lateinit var captchaImage: ImageView
    private lateinit var captchaInput: EditText
    private lateinit var loginButton: Button
    private lateinit var accountInput: EditText
    private lateinit var passwordInput: EditText

    // 當前驗證碼的文字
    private var currentCaptcha: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        // 綁定 UI 元件
        captchaImage = findViewById(R.id.captchaImage)
        captchaInput = findViewById(R.id.captchaInput)
        loginButton = findViewById(R.id.button1)
        accountInput = findViewById(R.id.inac)
        passwordInput = findViewById(R.id.inpas)

        // 初始化顯示驗證碼
        generateAndSetCaptcha()

        // 點擊驗證碼圖片 → 重新產生驗證碼
        captchaImage.setOnClickListener {
            generateAndSetCaptcha()
        }

        // 點擊登入按鈕 → 驗證帳號、密碼、驗證碼
        loginButton.setOnClickListener {
            // 清除之前的錯誤提示
            captchaInput.error = null
            accountInput.error = null
            passwordInput.error = null

            val inputCaptcha = captchaInput.text.toString()
            val inputAccount = accountInput.text.toString()
            val inputPassword = passwordInput.text.toString()

            // 預設正確帳號與密碼 (注意：這在真實應用中是不安全的)
            val correctAccount = "user"
            val correctPassword = "pass"

            when {
                // 驗證碼錯誤
                !inputCaptcha.equals(currentCaptcha, ignoreCase = true) -> {
                    captchaInput.error = getString(R.string.captcha_error)
                    generateAndSetCaptcha()
                }
                // 帳號或密碼錯誤
                inputAccount != correctAccount || inputPassword != correctPassword -> {
                    accountInput.error = getString(R.string.login_error)
                    passwordInput.error = getString(R.string.login_error)
                }
                // 登入成功 → 進入選單頁面
                else -> {
                    Toast.makeText(this, getString(R.string.login_successful), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, menu_kt::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    // 產生驗證碼並顯示在圖片上
    private fun generateAndSetCaptcha() {
        currentCaptcha = generateCaptchaText()
        val bitmap = generateCaptchaImage(currentCaptcha)
        captchaImage.setImageBitmap(bitmap)
    }

    // 隨機產生驗證碼文字 (預設 5 碼)
    private fun generateCaptchaText(length: Int = 5): String {
        val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789" // 避免易混淆字元
        return (1..length).map { chars.random() }.joinToString("")
    }

    // 根據驗證碼文字產生圖片 (含干擾線)
    private fun generateCaptchaImage(captchaText: String): Bitmap {
        val width = 160
        val height = 60
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()

        // 背景顏色
        canvas.drawColor(Color.LTGRAY)

        // 設定字體樣式
        val charCount = captchaText.length
        val charSpacing = width / (charCount + 1)
        paint.textSize = 40f
        paint.typeface = Typeface.DEFAULT_BOLD
        paint.isAntiAlias = true

        // 畫每個字，並隨機旋轉角度
        for (i in captchaText.indices) {
            val char = captchaText[i].toString()
            paint.color = Color.rgb(Random.nextInt(50, 256), Random.nextInt(50, 256), Random.nextInt(50, 256))
            canvas.save()
            val angle = Random.nextInt(-30, 31).toFloat()
            val x = charSpacing * (i + 1).toFloat()
            val y = 45f
            canvas.rotate(angle, x, y)
            canvas.drawText(char, x, y, paint)
            canvas.restore()
        }

        // 畫干擾線，增加辨識困難
        paint.strokeWidth = 2f
        for (i in 0 until 4) {
            paint.color = Color.rgb(Random.nextInt(100, 256), Random.nextInt(100, 256), Random.nextInt(100, 256))
            canvas.drawLine(
                Random.nextInt(width).toFloat(),
                Random.nextInt(height).toFloat(),
                Random.nextInt(width).toFloat(),
                Random.nextInt(height).toFloat(),
                paint
            )
        }

        return bitmap
    }
}
