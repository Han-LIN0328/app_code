package com.example.integrate

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

// 踩地雷遊戲 Activity
class mines_kt : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout          // 棋盤 GridLayout
    private lateinit var tvWinMessage: TextView          // 贏的訊息 TextView

    private val rows = 10                                // 棋盤列數
    private val cols = 8                                 // 棋盤行數
    private var mineCount = 10                           // 地雷數量（隨機 10~20）
    private val cells = mutableListOf<Button>()         // 存放每個格子的 Button
    private lateinit var mines: BooleanArray            // true 表示該格是地雷
    private lateinit var neighborMineCount: IntArray    // 記錄每格周圍地雷數量
    private var isGameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mines)

        // 適配邊緣到系統欄（Edge to Edge）
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 返回選單按鈕
        val backBtn = findViewById<Button>(R.id.btnBackToMenu)
        backBtn.setOnClickListener {
            finish() // 結束此 Activity，返回上一頁
        }

        // 初始化棋盤與訊息顯示
        gridLayout = findViewById(R.id.gridLayout)
        tvWinMessage = findViewById(R.id.tvWinMessage)

        // 重新開始按鈕
        findViewById<Button>(R.id.btnRestart).setOnClickListener {
            restartGame()
        }

        // 啟動遊戲
        restartGame()
    }

    // 重新開始遊戲
    private fun restartGame() {
        isGameOver = false
        tvWinMessage.visibility = android.view.View.GONE          // 隱藏勝利訊息
        mineCount = (10..20).random()                             // 隨機設定地雷數量
        mines = BooleanArray(rows * cols) { false }              // 初始化地雷位置
        neighborMineCount = IntArray(rows * cols) { 0 }          // 初始化周圍地雷計數
        createBoard()                                            // 建立棋盤格子
        placeMines()                                             // 隨機放置地雷
        calculateNeighborMineCount()                              // 計算每格周圍地雷數量
    }

    // 建立棋盤格子
    @SuppressLint("ClickableViewAccessibility")
    private fun createBoard() {
        gridLayout.removeAllViews()    // 清空之前的棋盤
        cells.clear()

        for (i in 0 until rows * cols) {
            val cell = Button(this).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(1, 1, 1, 1)
                }
                setBackgroundColor(ContextCompat.getColor(this@mines_kt, R.color.mine_cell_background))   // 預設背景色
                gravity = Gravity.CENTER
                textSize = 12f
                text = ""

                // 左鍵點擊/觸控點擊 → 開啟格子
                setOnClickListener {
                    if (text.toString() != getString(R.string.flag) && !isGameOver) {
                        openCell(i)
                    }
                }

                // 長按 → 插旗 (for touch)
                setOnLongClickListener {
                    if (isEnabled && !isGameOver) {
                        text = if (text == getString(R.string.flag)) "" else getString(R.string.flag)
                    }
                    true
                }

                // 右鍵點擊 → 插旗 (for mouse)
                setOnContextClickListener {
                    if (isEnabled && !isGameOver) {
                        text = if (text == getString(R.string.flag)) "" else getString(R.string.flag)
                    }
                    true
                }
            }

            cells.add(cell)
            gridLayout.addView(cell)
        }
    }

    // 隨機放置地雷
    private fun placeMines() {
        var placed = 0
        val total = rows * cols
        while (placed < mineCount) {
            val pos = Random.nextInt(total)
            if (!mines[pos]) {
                mines[pos] = true
                placed++
            }
        }
    }

    // 計算每格周圍地雷數量
    private fun calculateNeighborMineCount() {
        for (pos in mines.indices) {
            if (mines[pos]) continue  // 如果是地雷，跳過
            val neighbors = getNeighbors(pos)
            var count = 0
            for (n in neighbors) {
                if (mines[n]) count++
            }
            neighborMineCount[pos] = count
        }
    }

    // 開啟格子
    private fun openCell(pos: Int) {
        val cell = cells[pos]
        if (!cell.isEnabled) return

        if (mines[pos]) {
            // 點到地雷 → 顯示炸彈
            cell.text = getString(R.string.bomb)
            cell.textSize = 28f
            cell.setTextColor(ContextCompat.getColor(this, R.color.black))
            cell.setBackgroundColor(ContextCompat.getColor(this, R.color.mine_cell_bomb_background))
            cell.isEnabled = false
            gameOver(false)
        } else {
            // 顯示周圍地雷數量
            val count = neighborMineCount[pos]
            cell.text = if (count == 0) "" else count.toString()
            cell.textSize = 18f
            cell.setTextColor(
                ContextCompat.getColor(this, when (count) {
                    1 -> R.color.mine_cell_text_1
                    2 -> R.color.mine_cell_text_2
                    3 -> R.color.mine_cell_text_3
                    4 -> R.color.mine_cell_text_4
                    5 -> R.color.mine_cell_text_5
                    6 -> R.color.mine_cell_text_6
                    7 -> R.color.mine_cell_text_7
                    8 -> R.color.mine_cell_text_8
                    else -> R.color.black
                })
            )
            cell.isEnabled = false
            cell.setBackgroundColor(ContextCompat.getColor(this, R.color.mine_cell_revealed))

            // 如果周圍沒有地雷，自動打開周圍格子
            if (count == 0) {
                val neighbors = getNeighbors(pos)
                for (n in neighbors) {
                    openCell(n)
                }
            }
            checkWin()
        }
    }

    // 檢查是否勝利
    private fun checkWin() {
        if (isGameOver) return
        val hasWon = cells.withIndex().all { (index, cell) ->
            if (mines[index]) true else !cell.isEnabled
        }

        if (hasWon) {
            gameOver(true)
        }
    }

    // 遊戲結束
    private fun gameOver(isWin: Boolean) {
        isGameOver = true
        for ((i, cell) in cells.withIndex()) {
            cell.isEnabled = false
            if (mines[i] && cell.text.toString() != getString(R.string.bomb)) {
                cell.text = getString(R.string.bomb)
                cell.textSize = 28f
                cell.setTextColor(ContextCompat.getColor(this, R.color.black))
                cell.setBackgroundColor(ContextCompat.getColor(this, R.color.mine_cell_bomb_background))
            }
        }
        tvWinMessage.text = if (isWin) getString(R.string.congratulations_you_won) else getString(R.string.game_over_you_hit_a_mine)
        tvWinMessage.visibility = android.view.View.VISIBLE
    }

    // 取得指定格子的鄰居位置
    private fun getNeighbors(pos: Int): List<Int> {
        val neighbors = mutableListOf<Int>()
        val r = pos / cols
        val c = pos % cols
        for (dr in -1..1) {
            for (dc in -1..1) {
                if (dr == 0 && dc == 0) continue
                val nr = r + dr
                val nc = c + dc
                if (nr in 0 until rows && nc in 0 until cols) {
                    neighbors.add(nr * cols + nc)
                }
            }
        }
        return neighbors
    }
}
