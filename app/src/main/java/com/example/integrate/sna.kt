package com.example.integrate

import android.content.Context
import android.graphics.*
import android.os.*
import android.view.*
import kotlin.random.Random

// 自訂 View 來實作貪吃蛇遊戲
class SnakeView(context: Context) : View(context), Runnable {

    private val snake = mutableListOf<Point>() // 蛇身節點座標列表
    private var direction = Direction.RIGHT     // 初始移動方向
    private var food = Point(5, 5)             // 食物座標
    private val gridSize = 40                   // 每格大小
    private val handler = Handler(Looper.getMainLooper()) // 用於定時更新畫面
    private var running = true                  // 遊戲是否運行中

    init {
        // 初始化遊戲，延遲 300ms 重複更新
        post {
            resetGame()
            handler.postDelayed(this, 300)
        }

        // 觸控事件，用於判斷滑動方向來控制蛇移動
        setOnTouchListener(object : OnTouchListener {
            private var startX = 0f
            private var startY = 0f

            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        startX = event.x
                        startY = event.y
                    }
                    MotionEvent.ACTION_UP -> {
                        val dx = event.x - startX
                        val dy = event.y - startY
                        // 水平方向滑動
                        if (Math.abs(dx) > Math.abs(dy)) {
                            if (dx > 0 && direction != Direction.LEFT) direction = Direction.RIGHT
                            else if (dx < 0 && direction != Direction.RIGHT) direction = Direction.LEFT
                        }
                        // 垂直方向滑動
                        else {
                            if (dy > 0 && direction != Direction.UP) direction = Direction.DOWN
                            else if (dy < 0 && direction != Direction.DOWN) direction = Direction.UP
                        }
                    }
                }
                return true
            }
        })
    }

    // 畫面繪製
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.BLACK) // 背景黑色

        val paint = Paint().apply { color = Color.GREEN }
        // 畫蛇身
        for (p in snake) {
            canvas.drawRect(
                p.x * gridSize.toFloat(), p.y * gridSize.toFloat(),
                (p.x + 1) * gridSize.toFloat(), (p.y + 1) * gridSize.toFloat(), paint
            )
        }

        // 畫食物
        paint.color = Color.RED
        canvas.drawRect(
            food.x * gridSize.toFloat(), food.y * gridSize.toFloat(),
            (food.x + 1) * gridSize.toFloat(), (food.y + 1) * gridSize.toFloat(), paint
        )
    }

    // Runnable 的定時執行方法
    override fun run() {
        if (!running) {
            // 如果遊戲結束，重置遊戲
            running = true
            resetGame()
            handler.postDelayed(this, 300)
            return
        }

        moveSnake() // 移動蛇身
        invalidate() // 重新繪製畫面
        handler.postDelayed(this, 300) // 300ms 後再次執行
    }

    // 移動蛇身邏輯
    private fun moveSnake() {
        val head = snake.first()
        val newHead = Point(head.x, head.y)

        // 根據方向更新頭的位置
        when (direction) {
            Direction.RIGHT -> newHead.x++
            Direction.LEFT -> newHead.x--
            Direction.UP -> newHead.y--
            Direction.DOWN -> newHead.y++
        }

        val maxX = width / gridSize
        val maxY = height / gridSize

        // 撞牆或咬到自己 -> 遊戲結束
        if (newHead.x !in 0 until maxX || newHead.y !in 0 until maxY || snake.contains(newHead)) {
            running = false
            return
        }

        snake.add(0, newHead) // 新的頭加入蛇身列表

        if (newHead == food) {
            spawnFood() // 吃到食物生成新的食物
        } else {
            snake.removeAt(snake.lastIndex) // 沒吃到食物則尾巴移除
        }
    }

    // 隨機生成食物，避開蛇身
    private fun spawnFood() {
        val maxX = width / gridSize
        val maxY = height / gridSize

        do {
            food = Point(Random.nextInt(maxX), Random.nextInt(maxY))
        } while (snake.contains(food))
    }

    // 重置遊戲
    private fun resetGame() {
        snake.clear()
        val maxX = width / gridSize
        val maxY = height / gridSize
        val startX = Random.nextInt(maxX)
        val startY = Random.nextInt(maxY)

        snake.add(Point(startX, startY)) // 蛇頭隨機生成
        direction = Direction.RIGHT
        spawnFood() // 隨機生成食物
        invalidate() // 重新繪製
    }

    // 移動方向
    enum class Direction {
        UP, DOWN, LEFT, RIGHT
    }
}
