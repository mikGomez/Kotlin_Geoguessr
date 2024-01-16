package com.example.geoguessr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MapsGame : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juego_mapa)

        val position = intent.getIntExtra("IMAGE_POSITION", -1)
        when(position){
            0-> launch1()
            1-> launch2()
            2-> launch3()
            3-> launch4()
            4-> launch5()
        }
    }

    private fun launch5() {
    }

    private fun launch4() {
    }

    private fun launch3() {
    }

    private fun launch2() {
    }

    private fun launch1() {

    }
}