package com.example.toy.android.chart

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.example.toy.android.entity.Pie
import com.example.toy.android.utils.EasingUtils

class PieChart : View {

  private val startAngle = -90F
  private val pieList = arrayListOf<Pie>()
  private val colorArray = arrayOf(Color.BLACK, Color.GREEN, Color.RED, Color.BLUE, Color.YELLOW)
  private val ovalRectF = RectF()
  private lateinit var animator: ValueAnimator

  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
      : super(context, attrs, defStyleAttr) {
    init()
  }

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)
    val left = 0F
    Int
    val right = height.toFloat()
    val top = 0F
    val bottom = height.toFloat()
    ovalRectF.set(left, top, right, bottom)

    pieList.forEach { pie ->
      canvas?.drawArc(
        ovalRectF,
        pie.startAngle,
        pie.sweepAngle * pie.progress,
        true,
        pie.backgroundPaint
      )
    }
  }

  private fun init() {
    animator = ValueAnimator.ofFloat(0F, 1F)
    animator.duration = 1000
    animator.interpolator = LinearInterpolator()
    animator.addUpdateListener {
      val t = it.currentPlayTime / it.duration.toFloat()

      if (t in 0.0F..0.2F) {
        pieList[0].progress = EasingUtils(0.0F, 0.2F).ratio(t).let { ratio ->
          EasingUtils.easeInOutCubic(ratio)
        }
      }

      if (t in 0.0F..0.4F) {
        pieList[1].progress = EasingUtils(0.0F, 0.4F).ratio(t).let { ratio ->
          EasingUtils.easeInOutCubic(ratio)
        }
      }

      if (t in 0.0F..0.6F) {
        pieList[2].progress = EasingUtils(0.0F, 0.6F).ratio(t).let { ratio ->
          EasingUtils.easeInOutCubic(ratio)
        }
      }

      if (t in 0.0F..0.8F) {
        pieList[3].progress = EasingUtils(0.0F, 0.8F).ratio(t).let { ratio ->
          EasingUtils.easeInOutCubic(ratio)
        }
      }

      if (t in 0.0F..1.0F) {
        pieList[4].progress = EasingUtils(0.0F, 1.0F).ratio(t).let { ratio ->
          EasingUtils.easeInOutCubic(ratio)
        }
      }
      postInvalidate()
    }
  }

  fun setData(data: List<Float>) {
    if (pieList.isNotEmpty()) {
      pieList.clear()
    }

    data.forEachIndexed { index, number ->
      val pie = Pie().apply {
        setValue(data.sum(), number)
        startAngle = when (index) {
          0 -> this@PieChart.startAngle
          else -> pieList[index - 1].startAngle + pieList[index - 1].sweepAngle
        }
        setBackGround(colorArray[index])
      }
      pieList.add(pie)
    }
    postInvalidate()
  }

  fun startAnimation() {
    pieList.map { it.progress = 0F }
    postInvalidate()
    animator.start()
  }
}