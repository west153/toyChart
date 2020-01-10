package com.example.toy.android

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import com.example.toy.android.entity.Bar
import com.example.toy.android.utils.EasingUtils

class BarChart : View {

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

  private val verticalBarRectFMap = hashMapOf<Int, Bar>()

  private val textPaint = Paint().apply {
    this.color = Color.WHITE
    this.isAntiAlias = true
    this.textAlign = Paint.Align.CENTER
    this.textSize = 20.dp
  }

  private val horizontalBarPaint = Paint().apply {
    this.color = Color.RED
    this.strokeWidth = 1.dp
  }

  private lateinit var animator: ValueAnimator
  private lateinit var paintAnimator: ValueAnimator
  private var ratio = 0F
  private var colorFrom = 0
  private var colorTo = 0
  private var colorPaint = 0
  private var barRatio = arrayOf(0F, 0F, 0F)
  private var textRatio = arrayOf(0F, 0F, 0F)

  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
      : super(context, attrs, defStyleAttr) {
    init()
  }

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)
    val horizontalMinX = paddingLeft
    val horizontalMaxX = width - paddingLeft
    val horizontalLineH = height.toFloat() - paddingTop

    canvas?.drawLine(
      horizontalMinX,
      horizontalLineH,
      ratio * (horizontalMaxX - horizontalMinX) + horizontalMinX,
      horizontalLineH,
      linePaint
    )

    val verticalLineW = width - paddingLeft
    val verticalMinY = paddingTop
    val verticalMaxY = height - paddingTop

    canvas?.drawLine(
      verticalLineW,
      verticalMaxY,
      verticalLineW,
      ratio * (verticalMinY - verticalMaxY) + verticalMaxY,
      linePaint
    )

    values.forEachIndexed { index, value ->
      val margin = 20.dp
      val minW = paddingLeft + 2.dp
      val maxW = 150F
      val maxH = height - (paddingTop + 1.dp)
      val minH = maxH - value
      val startX = minW + ((maxW + margin) * index)
      val endX = startX + maxW

      val bar = verticalBarRectFMap[index]
      val rectF = bar?.rectF?.apply {
        this.set(startX, barRatio[index] * (minH - maxH) + maxH, endX, maxH)
      } ?: return@forEachIndexed

      if (bar.isSelected != null) {
        Log.d("colorPaint", "$colorPaint")
        verticalBarPaint.color = colorPaint
      } else
        verticalBarPaint.color = Color.GREEN

      canvas?.drawRect(rectF, verticalBarPaint)

      val textAlpha = (255 * textRatio[index]).toInt()
      textPaint.alpha = textAlpha
      canvas?.drawText(value.toString(), (startX + endX) / 2, minH - 4.dp, textPaint)
    }

    values2.forEachIndexed { index, value ->
      val h = 150F
      val margin = 20.dp
      val minH = paddingTop + 2.dp
      val maxH = h + minH
      val startY = minH + ((maxH + margin) * index)
      val stopY = startY + h
      val startX = width - (paddingLeft + 1.dp)
      val stopX = barRatio[0] * ((startX - value) - startX) + startX

      canvas?.drawRect(startX, startY, stopX, stopY, horizontalBarPaint)
    }
  }

  fun startAnimation() {
    clear()
    animator.start()
  }

  fun touchEvent(x: Float, y: Float): Boolean {
    val filter = verticalBarRectFMap
      .values
      .find { it.rectF.contains(x, y) }

    verticalBarRectFMap
      .values
      .forEach {
        if (it == filter) {
          val selected = it.isSelected != true
          it.isSelected = selected
          if (selected) {
            colorFrom = Color.GREEN
            colorTo = Color.BLUE
          } else {
            colorFrom = Color.BLUE
            colorTo = Color.GREEN
          }
        } else
          it.isSelected = null
      }

    return when (filter) {
      null -> false
      else -> {
        startColorAnimation()
        true
      }
    }
  }

  private fun clear() {
    ratio = 0F

    barRatio[0] = 0F
    barRatio[1] = 0F
    barRatio[2] = 0F

    textRatio[0] = 0F
    textRatio[1] = 0F
    textRatio[2] = 0F
  }

  private fun init() {
    animator = ValueAnimator.ofFloat(0F, 1F)
    animator.duration = 1000
    animator.interpolator = LinearInterpolator()
    animator.addUpdateListener {
      val t = it.currentPlayTime / it.duration.toFloat()
      ratio = EasingUtils(0F, 1F).ratio(t).let { ratio ->
        EasingUtils.easeInOutCubic(ratio)
      }

      if (t in 0.2F..0.5F) {
        barRatio[0] = EasingUtils(0.2F, 0.5F).ratio(t).let { ratio ->
          EasingUtils.easeInOutQuint(ratio)
        }
      }

      if (t in 0.4F..0.7F) {
        barRatio[1] = EasingUtils(0.4F, 0.7F).ratio(t).let { ratio ->
          EasingUtils.easeInOutQuad(ratio)
        }
      }

      if (t in 0.6F..0.9F) {
        barRatio[2] = EasingUtils(0.6F, 0.9F).ratio(t).let { ratio ->
          EasingUtils.easeInOutQuart(ratio)
        }
      }

      if (t in 0.4F..0.6F) {
        textRatio[0] = EasingUtils(0.4F, 0.6F).ratio(t)
      }

      if (t in 0.6F..0.8F) {
        textRatio[1] = EasingUtils(0.6F, 0.8F).ratio(t)
      }

      if (t in 0.8F..1.0F) {
        textRatio[2] = EasingUtils(0.8F, 1.0F).ratio(t)
      }
      postInvalidate()
    }


    values.forEachIndexed { index, _ ->
      verticalBarRectFMap[index] = Bar(RectF())
    }
  }

  private fun startColorAnimation() {
    paintAnimator = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
    paintAnimator.duration = 1000
    paintAnimator.interpolator = AccelerateDecelerateInterpolator()
    paintAnimator.addUpdateListener {
      colorPaint = it.animatedValue as Int
      postInvalidate()
    }
    paintAnimator.start()
  }

  private val Int.dp: Float
    get() = this * Resources.getSystem().displayMetrics.density
}