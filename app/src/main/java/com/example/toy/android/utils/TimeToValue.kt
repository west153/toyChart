package com.example.toy.android.utils

import com.example.toy.android.utils.EasingUtils.Companion.linear

class TimeToValue(
  val startT: Float,
  val durationT: Float,
  val minV: Float,
  val maxV: Float
) {
  private val easingT = EasingUtils(startT, durationT + startT)
  private val easingV = EasingUtils(minV, maxV)

  fun easing(t: Float, easing: ((Float) -> Float) = ::linear) = when {
    t in easingT.min..easingT.max -> easingV.value(easing.invoke(easingT.ratio(t)))
    t < startT -> minV
    else -> maxV
  }
}