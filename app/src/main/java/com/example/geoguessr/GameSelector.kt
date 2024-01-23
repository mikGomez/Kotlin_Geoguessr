package com.example.geoguessr

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.fragmentsyviewpager.AdaptadorViewPager
import com.example.geoguessr.databinding.ActivityGameselectorBinding
import com.example.geoguessr.databinding.ActivityLeverselectorBinding
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.material.tabs.TabLayoutMediator

class GameSelector : AppCompatActivity() {
    lateinit var binding: ActivityGameselectorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameselectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarPrincipal)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding.toolbarPrincipal.setNavigationOnClickListener {
            val signInClient = Identity.getSignInClient(this)
            signInClient.signOut()

            Log.e(ContentValues.TAG,"Cerrada sesión completamente")
            finish()
        }

        binding.viewPager.adapter = AdaptadorViewPager(this)
        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab,index->
            tab.text = when(index){
                0->{"Spain Game"}
                1->{"Europe Game"}
                2->{"Asia Game"}
                else->{throw Resources.NotFoundException("Posición no encontrada") }
            }

        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mnOp1 -> {
                irAVentanaOpcion1()
            }

            R.id.mnBusqueda -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val irAVentanaOpcion1: () -> Unit = {
        val intent = Intent(this, LevelSelector::class.java)
        startActivity(intent)
    }
}