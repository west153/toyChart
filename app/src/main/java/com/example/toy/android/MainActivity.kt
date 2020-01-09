package com.example.toy.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    findViewById<PieChart>(R.id.chart).setOnClickListener {
      findViewById<PieChart>(R.id.chart).startAnimation()
    }
  }

}