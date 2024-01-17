package com.example.geoguessr

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.geoguessr.databinding.ActivityGameselectorBinding
import com.example.geoguessr.databinding.ActivityLeverselectorBinding

class GameSelector : AppCompatActivity() {
    lateinit var binding: ActivityGameselectorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameselectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSpain.setOnClickListener {
            openLevelSelector(this)
        }
    }

    private fun openLevelSelector(context: Context) {
        val intent = Intent(context, LevelSelector::class.java)

        context.startActivity(intent)
    }
}