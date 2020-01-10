package com.example.toy.android

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

  @SuppressLint("ClickableViewAccessibility")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val chart = findViewById<PieChart>(R.id.chart)

    chart.setOnClickListener {
      chart.startAnimation()
    }

    chart.setOnTouchListener { _, event ->
      if (event.action == MotionEvent.ACTION_UP) {
        return@setOnTouchListener chart.touchEvent(event.x, event.y)
      }
      false
    }
  }

}
