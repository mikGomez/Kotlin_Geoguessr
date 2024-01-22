package com.example.geoguessr

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager.widget.ViewPager

import com.example.geoguessr.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.storage.storage


import java.io.File

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var imageList: List<String> // Cambiado de Int a String para almacenar las URL de las imágenes



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.idViewPager)
        val nivel = intent.getIntExtra("levelGame", -1)

        imageList = ArrayList<String>()
        cargarImagenesDesdeStorage(nivel)

        viewPagerAdapter = ViewPagerAdapter(this@MainActivity, imageList, nivel)
        viewPager.adapter = viewPagerAdapter
    }

    private fun cargarImagenesDesdeStorage(nivel: Int) {
        val nivelPath = if (nivel == 1) "nivel1" else "nivel2"

        // Iterar sobre las imágenes en Firebase Storage
        for (i in 1..5) {
            val imageName = "imagen_$i.jpg"
            val imageUrl = "$nivelPath/$imageName"
            imageList += imageUrl
        }
    }

}

