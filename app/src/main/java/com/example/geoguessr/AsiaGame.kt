package com.example.geoguessr

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.geoguessr.databinding.FragmentAsiaGameBinding


class AsiaGame : Fragment() {
    lateinit var binding: FragmentAsiaGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAsiaGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView.setImageResource(R.drawable.comida_asia)

        binding.imageView.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "El juego está en desarrollo",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun abrirActividad() {
        val intent = Intent(activity, LevelSelector::class.java)
        startActivity(intent)
    }
}