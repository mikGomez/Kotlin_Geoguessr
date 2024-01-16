package com.example.geoguessr

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.geoguessr.databinding.ActivityLeverselectorBinding

class LevelSelector : AppCompatActivity() {

    lateinit var binding: ActivityLeverselectorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeverselectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLevel1.setOnClickListener {
            openImages(this,1)
        }
        binding.btnLevel2.setOnClickListener {
            openImages(this,2)
        }
    }

    private fun openImages(context: Context, level:Int) {
        val intent = Intent(context, MainActivity::class.java)
        // Pasa la posición como un extra en el Intent
        intent.putExtra("levelGame", level)
        context.startActivity(intent)
    }
}