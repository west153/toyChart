package com.example.toy.android.chart

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import com.example.toy.android.entity.Pie
import com.example.toy.android.utils.EasingUtils
import com.example.toy.android.utils.TimeToValue

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
    val right = height.toFloat()
    val top = 0F
    val bottom = height.toFloat()
    ovalRectF.set(left, top, right, bottom)

    for (index in (pieList.size - 1) downTo 0) {
      val sweep = if (index == 0)
        pieList[index].sweepAngle
      else
        pieList[index].sweepAngle + (index - index until index).map { pieList[it].sweepAngle }.sum()

      canvas?.drawArc(
        ovalRectF,
        startAngle,
        sweep * pieList[index].progress,
        true,
        pieList[index].backgroundPaint
      )

      if (index == 1) {
        Log.d("chart", "${pieList[index].progress}, $sweep")
      }
    }

  }

  private fun init() {
    animator = ValueAnimator.ofFloat(0F, 1F)
    animator.duration = 1000
    animator.interpolator = LinearInterpolator()
    animator.addUpdateListener {
      val t = it.animatedValue as Float
      val chartT = EasingUtils.easeInOutCubic(t)
      pieList[0].progress = TimeToValue(0F, 0.2F, 0F, 1F).easing(chartT)
      pieList[1].progress = TimeToValue(0F, 0.4F, 0F, 1F).easing(chartT)
      pieList[2].progress = TimeToValue(0F, 0.6F, 0F, 1F).easing(chartT)
      pieList[3].progress = TimeToValue(0F, 0.8F, 0F, 1F).easing(chartT)
      pieList[4].progress = TimeToValue(0F, 1F, 0F, 1F).easing(chartT)
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