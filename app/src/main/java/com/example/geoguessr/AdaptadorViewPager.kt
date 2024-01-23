package com.example.fragmentsyviewpager

import android.content.res.Resources.NotFoundException
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.geoguessr.AsiaGame
import com.example.geoguessr.EuropeGame
import com.example.geoguessr.SpainFragment

class AdaptadorViewPager(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
       return when(position){
           0->{SpainFragment()}
           1->{EuropeGame()}
           2->{AsiaGame()}
           else->{throw NotFoundException("Posicion no encontrada")}
       }
    }
}