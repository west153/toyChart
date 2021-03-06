package com.example.toy.android

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.example.toy.android.chart.BarChart
import com.example.toy.android.chart.PieChart

class MainActivity : AppCompatActivity() {

  @SuppressLint("ClickableViewAccessibility")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val chart = findViewById<BarChart>(R.id.chart)
    val chart2 = findViewById<PieChart>(R.id.chart2)
    chart2.setData(arrayListOf(160F, 80F, 75F, 5F, 80F))

    chart2.setOnClickListener {
      chart2.startAnimation()
    }

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
