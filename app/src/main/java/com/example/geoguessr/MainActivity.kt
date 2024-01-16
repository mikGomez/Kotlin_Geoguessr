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

        // Establece un listener para los clics en el ViewPager
        viewPager.setOnClickListener {
            // Obtiene la posición de la imagen actual en el ViewPager
            val currentPosition = viewPager.currentItem

            // Realiza alguna acción según la posición, por ejemplo, abre otra actividad
            when (currentPosition) {
                0 -> openActivity1()
                1 -> openActivity2()
            }
        }
    }

    private fun openActivity1() {
        val intent = Intent(this, JuegoMapa::class.java)
        startActivity(intent)
    }

    private fun openActivity2() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
}