package com.example.toy.android.entity

import android.graphics.Color
import android.graphics.Paint
import com.example.toy.android.utils.dp

class Pie {

  var startAngle = 0F
  var endAngle = 0F
  var sweepAngle = 0F

  private var totalValue: Float = 0F
  private var value: Float = 0F
  var progress = 0F

  val backgroundPaint = Paint().apply {
    this.color = Color.RED
    this.strokeWidth = 1.dp
    this.style = Paint.Style.FILL
  }

  val strokePaint
    get() = Paint().apply {
      this.color = Color.BLACK
      this.strokeWidth = 1.dp
      this.style = Paint.Style.STROKE
    }

  fun setValue(total: Float, value: Float) {
    this.totalValue = total
    this.value = value
  }


  fun setBackGround(res: Int) {
    backgroundPaint.color = res
  }
}