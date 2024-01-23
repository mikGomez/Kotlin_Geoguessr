package com.example.geoguessr

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.geoguessr.databinding.FragmentEuropeGameBinding


class EuropeGame : Fragment() {
    lateinit var binding: FragmentEuropeGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEuropeGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageView.setImageResource(R.drawable.pimientos_navarra)
        binding.imageView.setOnClickListener {
            abrirActividad()
        }
    }

    private fun abrirActividad() {
        val intent = Intent(activity, LevelSelector::class.java)
        startActivity(intent)
    }
}