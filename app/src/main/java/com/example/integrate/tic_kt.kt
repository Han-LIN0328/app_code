package com.example.integrate

import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class tic_kt : AppCompatActivity() {

    // 二維陣列存放棋盤按鈕
    private lateinit var boardButtons: Array<Array<Button>>
    // 顯示遊戲狀態或勝利訊息
    private lateinit var statusTextView: TextView
    // 選擇先攻圖案的 Switch
    private lateinit var switchFirstPlayer: Switch

    // 當前玩家 ("O" 或 "X")
    private var currentPlayer = "O"  // 預設為O先攻
    // 棋盤狀態，null 表示尚未下
    private var board = Array(3) { arrayOfNulls<String>(3) }
    // 遊戲是否結束
    private var gameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // 啟用 Edge-to-Edge 顯示
        setContentView(R.layout.tic)

        // 設定系統欄位 Insets（避免按鈕被遮擋）
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 返回選單按鈕
        val backBtn = findViewById<Button>(R.id.btnBackToMenu)
        backBtn.setOnClickListener {
            finish() // 返回上一頁（通常是 menu_kt）
        }

        // 初始化顯示與 Switch
        statusTextView = findViewById(R.id.tv)
        switchFirstPlayer = findViewById(R.id.aa)

        // 初始化棋盤按鈕陣列
        boardButtons = arrayOf(
            arrayOf(findViewById(R.id.button1), findViewById(R.id.button2), findViewById(R.id.button3)),
            arrayOf(findViewById(R.id.button4), findViewById(R.id.button5), findViewById(R.id.button6)),
            arrayOf(findViewById(R.id.button7), findViewById(R.id.button8), findViewById(R.id.button9))
        )

        // 設定每個棋盤按鈕的點擊事件
        for (row in 0..2) {
            for (col in 0..2) {
                boardButtons[row][col].setOnClickListener {
                    onButtonClick(row, col)
                }
            }
        }

        // 「再來一局」按鈕功能
        findViewById<Button>(R.id.res).setOnClickListener {
            resetGame()
        }

        // Switch 功能設定（切換先攻玩家）
        switchFirstPlayer.setOnCheckedChangeListener { _, isChecked ->
            currentPlayer = if (isChecked) "X" else "O"
            switchFirstPlayer.text = if (isChecked) getString(R.string.starting_player_cross) else getString(R.string.starting_player_circle)
            resetGame() // 切換後立即重置遊戲
        }

        // 初始 Switch 文字與狀態
        switchFirstPlayer.text = getString(R.string.starting_player_circle)
        statusTextView.text = getString(R.string.player_turn, currentPlayer)
    }

    // 處理玩家點擊棋盤
    private fun onButtonClick(row: Int, col: Int) {
        if (board[row][col] != null || gameOver) return  // 已下過或遊戲結束不能點

        board[row][col] = currentPlayer  // 記錄玩家落子
        boardButtons[row][col].text = currentPlayer  // 更新按鈕文字

        // 判斷勝利
        if (checkWin()) {
            statusTextView.text = getString(R.string.player_wins, currentPlayer)
            gameOver = true
        }
        // 判斷平手
        else if (isDraw()) {
            statusTextView.text = getString(R.string.draw_game)
            gameOver = true
        }
        // 切換玩家
        else {
            currentPlayer = if (currentPlayer == "X") "O" else "X"
            statusTextView.text = getString(R.string.player_turn, currentPlayer)
        }
    }

    // 檢查是否有玩家勝利
    private fun checkWin(): Boolean {
        for (i in 0..2) {
            // 檢查橫列
            if (board[i][0] != null && board[i][0] == board[i][1] && board[i][1] == board[i][2]) return true
            // 檢查直列
            if (board[0][i] != null && board[0][i] == board[1][i] && board[1][i] == board[2][i]) return true
        }
        // 檢查對角線
        if (board[0][0] != null && board[0][0] == board[1][1] && board[1][1] == board[2][2]) return true
        if (board[0][2] != null && board[0][2] == board[1][1] && board[1][1] == board[2][0]) return true

        return false
    }

    // 檢查是否平手
    private fun isDraw(): Boolean {
        return board.all { row -> row.all { it != null } }
    }

    // 重置遊戲
    private fun resetGame() {
        board = Array(3) { arrayOfNulls<String>(3) }
        gameOver = false

        // 清空按鈕文字
        for (row in 0..2) {
            for (col in 0..2) {
                boardButtons[row][col].text = ""
            }
        }

        // 根據 Switch 狀態重設先攻玩家
        currentPlayer = if (switchFirstPlayer.isChecked) "X" else "O"
        statusTextView.text = getString(R.string.game_restarted, currentPlayer)
    }

}
