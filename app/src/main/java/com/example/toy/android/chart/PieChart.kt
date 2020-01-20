package com.example.toy.android.chart

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import com.example.toy.android.entity.Pie
import com.example.toy.android.utils.EasingUtils
import com.example.toy.android.utils.TimeToValue
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

class PieChart : View, View.OnTouchListener {

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
    val centerXOfCanvas = width / 2
    val rectW = height.toFloat() / 2
    val left = centerXOfCanvas - rectW
    val right = centerXOfCanvas + rectW
    val top = 0F
    val bottom = height.toFloat()
    ovalRectF.set(left, top, right, bottom)

    for (index in (pieList.size - 1) downTo 0) {

      pieList[index].sweepAngle = if (index == 0)
        pieList[index].endAngle
      else
        pieList[index].endAngle + (index - index until index).map { pieList[it].endAngle }.sum()

      canvas?.drawArc(
        ovalRectF,
        startAngle,
        pieList[index].sweepAngle * pieList[index].progress,
        true,
        pieList[index].backgroundPaint
      )
    }
  }

  private fun init() {
    setOnTouchListener(this)
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

    for (i in data.indices) {
      val pie = Pie().apply {
        setValue(data.sum(), data[i])
        startAngle = when (i == 0) {
          true -> this@PieChart.startAngle
          else -> pieList[i - 1].startAngle + pieList[i - 1].endAngle
        }
        endAngle = (data[i] / data.sum()) * 360
        setBackGround(colorArray[i])
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

  override fun onTouch(v: View?, event: MotionEvent?): Boolean {
    if (event?.action == MotionEvent.ACTION_UP) {
      return touchToPie(event.x, event.y)
    }
    return true
  }

  private fun touchToPie(x: Float, y: Float): Boolean {
    val centerX = ovalRectF.centerX()
    val centerY = ovalRectF.centerY()
    val radius = (ovalRectF.top + ovalRectF.bottom) / 2

    // sqrt((x2 - x1)^2 + (y2 - y1)^2)
    val pointX = abs(centerX - x)
    val pointY = abs(centerY - y)
    val touchDistanceToCenter = sqrt(pointX.pow(2F) + pointY.pow(2F))
    val isTouchPie = radius > touchDistanceToCenter

    return if (isTouchPie) {
      val horizontalDirection = centerX < x // true : right, false: left
      val verticalDirection = centerY < y // true: bottom, false: top

      val degree = if (horizontalDirection && !verticalDirection) {
        //degree 0 ~ 90
        Math.toDegrees(acos(pointY / touchDistanceToCenter).toDouble())
      } else if (horizontalDirection && verticalDirection) {
        //degree 90 ~ 180
        Math.toDegrees(acos(pointX / touchDistanceToCenter).toDouble()) + 90
      } else if (!horizontalDirection && verticalDirection) {
        //degree 180 ~ 270
        Math.toDegrees(acos(pointY / touchDistanceToCenter).toDouble()) + 180
      } else {
        //degree 270~ 360
        Math.toDegrees(acos(pointX / touchDistanceToCenter).toDouble()) + 270
      }
      return containsPie(degree)
    } else
      false
  }

  private fun containsPie(degree: Double): Boolean {
    for (i in pieList.indices) {
      val pie = pieList[i]
      val startAngle = pie.startAngle + 90F
      if (degree in startAngle..pie.sweepAngle) {
        Log.d("touchPie", "index:$i, degree:$degree, sweepAngle:${pie.sweepAngle}")
        return true
      }
    }
    return false
  }

}