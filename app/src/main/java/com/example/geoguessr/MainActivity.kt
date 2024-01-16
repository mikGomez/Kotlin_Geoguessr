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
        val nivel = intent.getIntExtra("levelGame", -1)
        if (nivel == 1){

            imageList = ArrayList<Int>()
            imageList = imageList + R.drawable.gachas_ciudad_real
            imageList = imageList + R.drawable.paella_valencia
            imageList = imageList + R.drawable.fabada_asturias
            imageList = imageList + R.drawable.migas_merida
            imageList = imageList + R.drawable.cocido_madrid
        } else {
            imageList = ArrayList<Int>()
            imageList = imageList + R.drawable.cochinillo_segovia
            imageList = imageList + R.drawable.zarangollo_murica
            imageList = imageList + R.drawable.ternasco_aragon
            imageList = imageList + R.drawable.pimientos_navarra
            imageList = imageList + R.drawable.menestra_la_rioja
        }


        viewPagerAdapter = ViewPagerAdapter(this@MainActivity, imageList)

        viewPager.adapter = viewPagerAdapter
    }
}
