package com.example.geoguessr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager

import com.example.geoguessr.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var imageList: List<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.idViewPager)

        imageList = ArrayList<Int>()
        imageList = imageList + R.drawable.img1
        imageList = imageList + R.drawable.img2
        imageList = imageList + R.drawable.img3
        imageList = imageList + R.drawable.img4
        imageList = imageList + R.drawable.img5

        viewPagerAdapter = ViewPagerAdapter(this@MainActivity, imageList)

        viewPager.adapter = viewPagerAdapter
    }
}
