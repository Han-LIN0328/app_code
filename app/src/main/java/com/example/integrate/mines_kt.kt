package com.example.integrate

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import kotlin.random.Random

// 踩地雷遊戲 Activity
class mines_kt : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout          // 棋盤 GridLayout
    private lateinit var tvWinMessage: TextView          // 贏的訊息 TextView
    private lateinit var messageCard: View               // 訊息卡片容器

    private val rows = 14                                // 增加列數到 14
    private val cols = 8                                 // 棋盤行數保持 8
    private var mineCount = 15                           // 稍微增加地雷數量
    private val cells = mutableListOf<MaterialButton>()  // 存放每個格子的 MaterialButton
    private lateinit var mines: BooleanArray            // true 表示該格是地雷
    private lateinit var neighborMineCount: IntArray    // 記錄每格周圍地雷數量
    private var isGameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mines)

        // 適配系統欄
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 返回選單
        findViewById<Button>(R.id.btnBackToMenu).setOnClickListener {
            finish()
        }

        gridLayout = findViewById(R.id.gridLayout)
        tvWinMessage = findViewById(R.id.tvWinMessage)
        messageCard = findViewById(R.id.messageCard)

        findViewById<Button>(R.id.btnRestart).setOnClickListener {
            restartGame()
        }

        restartGame()
    }

    private fun restartGame() {
        isGameOver = false
        messageCard.visibility = View.GONE
        mineCount = (15..25).random() 
        mines = BooleanArray(rows * cols) { false }
        neighborMineCount = IntArray(rows * cols) { 0 }
        createBoard()
        placeMines()
        calculateNeighborMineCount()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createBoard() {
        gridLayout.removeAllViews()
        cells.clear()

        for (i in 0 until rows * cols) {
            val cell = MaterialButton(this).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(1, 1, 1, 1) // 邊距縮小，讓 3D 效果更連貫
                }
                
                // 移除 MaterialButton 的預設內距與邊距
                setPadding(0, 0, 0, 0)
                minHeight = 0
                minWidth = 0
                iconPadding = 0
                insetTop = 0
                insetBottom = 0
                cornerRadius = 0 // 踩地雷格子通常是方形的，更有凸起感
                
                // 使用凸起感的背景
                background = ContextCompat.getDrawable(this@mines_kt, R.drawable.mine_cell_raised)
                backgroundTintList = null // 確保不被預設顏色覆蓋
                
                // 移除原有的邊框設定，因為背景已經包含了陰影
                strokeWidth = 0
                
                gravity = Gravity.CENTER
                textSize = 14f 
                typeface = Typeface.DEFAULT_BOLD
                text = ""

                setOnClickListener {
                    if (text.toString() != getString(R.string.flag) && !isGameOver) {
                        openCell(i)
                    }
                }

                setOnLongClickListener {
                    toggleFlag(this)
                    true
                }

                setOnContextClickListener {
                    toggleFlag(this)
                    true
                }
            }

            cells.add(cell)
            gridLayout.addView(cell)
        }
    }

    private fun toggleFlag(cell: MaterialButton) {
        if (cell.isEnabled && !isGameOver) {
            cell.text = if (cell.text == getString(R.string.flag)) "" else getString(R.string.flag)
            cell.setTextColor(if (cell.text == getString(R.string.flag)) Color.YELLOW else Color.WHITE)
        }
    }

    private fun placeMines() {
        var placed = 0
        while (placed < mineCount) {
            val pos = Random.nextInt(rows * cols)
            if (!mines[pos]) {
                mines[pos] = true
                placed++
            }
        }
    }

    private fun calculateNeighborMineCount() {
        for (pos in mines.indices) {
            if (mines[pos]) continue
            neighborMineCount[pos] = getNeighbors(pos).count { mines[it] }
        }
    }

    private fun openCell(pos: Int) {
        if (pos !in cells.indices) return
        val cell = cells[pos]
        if (!cell.isEnabled) return

        // 開啟後變為平整背景
        cell.background = ContextCompat.getDrawable(this, R.drawable.mine_cell_revealed_bg)
        cell.backgroundTintList = null
        cell.isEnabled = false

        if (mines[pos]) {
            cell.text = getString(R.string.bomb)
            cell.setBackgroundColor(ContextCompat.getColor(this, R.color.mine_cell_bomb_background))
            gameOver(false)
        } else {
            val count = neighborMineCount[pos]
            cell.text = if (count == 0) "" else count.toString()
            cell.setTextColor(getMineTextColor(count))

            if (count == 0) {
                getNeighbors(pos).forEach { openCell(it) }
            }
            checkWin()
        }
    }

    private fun getMineTextColor(count: Int): Int {
        val colorRes = when (count) {
            1 -> R.color.mine_cell_text_1
            2 -> R.color.mine_cell_text_2
            3 -> R.color.mine_cell_text_3
            4 -> R.color.mine_cell_text_4
            5 -> R.color.mine_cell_text_5
            6 -> R.color.mine_cell_text_6
            7 -> R.color.mine_cell_text_7
            8 -> R.color.mine_cell_text_8
            else -> R.color.white
        }
        return ContextCompat.getColor(this, colorRes)
    }

    private fun checkWin() {
        val hasWon = cells.withIndex().all { (index, cell) ->
            if (mines[index]) true else !cell.isEnabled
        }
        if (hasWon) gameOver(true)
    }

    private fun gameOver(isWin: Boolean) {
        isGameOver = true
        cells.forEachIndexed { i, cell ->
            cell.isEnabled = false
            if (mines[i] && cell.text != getString(R.string.flag)) {
                cell.text = getString(R.string.bomb)
                if (!isWin) {
                    cell.background = null
                    cell.setBackgroundColor(ContextCompat.getColor(this, R.color.mine_cell_bomb_background))
                }
            }
        }
        tvWinMessage.text = if (isWin) getString(R.string.congratulations_you_won) else "遊戲結束！你踩到地雷了"
        messageCard.visibility = View.VISIBLE
    }

    private fun getNeighbors(pos: Int): List<Int> {
        val neighbors = mutableListOf<Int>()
        val r = pos / cols
        val c = pos % cols
        for (dr in -1..1) {
            for (dc in -1..1) {
                if (dr == 0 && dc == 0) continue
                val nr = r + dr
                val nc = c + dc
                if (nr in 0 until rows && nc in 0 until cols) neighbors.add(nr * cols + nc)
            }
        }
        return neighbors
    }
}
