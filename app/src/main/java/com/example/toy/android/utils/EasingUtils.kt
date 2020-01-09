package com.example.toy.android.utils

class EasingUtils(
  val min: Float,
  val max: Float
) {
  fun ratio(t: Float) = (t - min) / (max - min)
  fun value(ratio: Float) = ratio * (max - min) + min

  companion object {
    fun linear(t: Float): Float {
      return t
    }

    // accelerating from zero velocity
    fun easeInQuad(t: Float): Float {
      return t * t
    }

    // decelerating to zero velocity
    fun easeOutQuad(t: Float): Float {
      return t * (2 - t)
    }

    // acceleration until halfway, then deceleration
    fun easeInOutQuad(t: Float): Float {
      return if (t < .5f) 2 * t * t else -1 + (4 - 2 * t) * t
    }

    // accelerating from zero velocity
    fun easeInCubic(t: Float): Float {
      return t * t * t
    }

    // decelerating to zero velocity
    fun easeOutCubic(t: Float): Float {
      val tmpT = t - 1
      return tmpT * tmpT * tmpT + 1
    }

    // acceleration until halfway, then deceleration
    fun easeInOutCubic(t: Float): Float {
      return if (t < .5f) 4 * t * t * t else (t - 1) * (2 * t - 2) * (2 * t - 2) + 1
    }

    // accelerating from zero velocity
    fun easeInQuart(t: Float): Float {
      return t * t * t * t
    }

    // decelerating to zero velocity
    fun easeOutQuart(t: Float): Float {
      val tmpT = t - 1
      return 1 - tmpT * tmpT * tmpT * tmpT
    }

    // acceleration until halfway, then deceleration
    fun easeInOutQuart(t: Float): Float {
      val tmpT = t - 1
      return if (t < .5f) 8 * t * t * t * t else 1 - 8 * tmpT * tmpT * tmpT * tmpT
    }

    // accelerating from zero velocity
    fun easeInQuint(t: Float): Float {
      return t * t * t * t * t
    }

    // decelerating to zero velocity
    fun easeOutQuint(t: Float): Float {
      val tmpT = t - 1
      return 1 + tmpT * tmpT * tmpT * tmpT * tmpT
    }

    // acceleration until halfway, then deceleration
    fun easeInOutQuint(t: Float): Float {
      val tmpT = t - 1
      return if (t < .5f) 16 * t * t * t * t * t else 1 + 16 * tmpT * tmpT * tmpT * tmpT * tmpT
    }
  }
}