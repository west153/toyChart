package com.example.toy.android

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class PieChart : View {

  var startAngle = 90
  var paddingLeft = 8.dp
  var paddingTop = 2.dp

  private val values = arrayOf(200F, 100F, 300F)
  private val values2 = arrayOf(200F, 100F, 300F, 250F, 350F)

  private val linePaint = Paint().apply {
    this.color = Color.BLACK
    this.strokeWidth = 2.dp
  }

  private val verticalBarPaint = Paint().apply {
    this.color = Color.GREEN
    this.strokeWidth = 1.dp
  }

  private val horizontalBarPaint = Paint().apply {
    this.color = Color.RED
    this.strokeWidth = 1.dp
  }

  private lateinit var animator: ValueAnimator

  private var barSize = 150F
  private var verticalBarPadding = 30.dp
  private var horizontalBarPadding = 20.dp
  private var horizontalBaseLineX = 1F

  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
      : super(context, attrs, defStyleAttr) {
  }

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)
    val x = width - (paddingLeft + paddingLeft)

    canvas?.drawLine(
      paddingLeft,
      height.toFloat() - paddingTop,
      paddingLeft + x * horizontalBaseLineX,
      height.toFloat() - paddingTop,
      linePaint
    )

    val y = height - (paddingTop + paddingTop)
    canvas?.drawLine(
      width - paddingLeft,
      paddingTop + y,
      width - paddingLeft,
      (paddingTop + y) - (y * horizontalBaseLineX),
      linePaint
    )

    values.forEachIndexed { index, value ->
      val lp = paddingLeft + 2.dp
      val left = lp + (barSize + verticalBarPadding) * index
      val right = left + barSize
      val bottom = this.height - paddingTop + 2.dp
      canvas?.drawRect(left, height - value, right, bottom, verticalBarPaint)
    }


    values2.forEachIndexed { index, value ->
      val left = width - value
      val top = 4.dp + (barSize + horizontalBarPadding) * index
      val bottom = top + barSize
      val right = width - paddingLeft + 2.dp
      canvas?.drawRect(left, top, right, bottom, horizontalBarPaint)
    }
  }

  private val Int.dp: Float
    get() = this * Resources.getSystem().displayMetrics.density
}