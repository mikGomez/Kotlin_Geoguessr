package com.example.geoguessr

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.fragmentsyviewpager.AdaptadorViewPager
import com.example.geoguessr.databinding.ActivityGameselectorBinding
import com.example.geoguessr.databinding.ActivityLeverselectorBinding
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.material.tabs.TabLayoutMediator

class GameSelector : AppCompatActivity() {
    lateinit var binding: ActivityGameselectorBinding
    private var soundEnabled = true
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
                0->{"Comidas España"}
                1->{"Comidas Latinas"}
                2->{"Comidas Asia"}
                3->{"Comidas EEUU"}
                else->{throw Resources.NotFoundException("Posición no encontrada") }
            }

        }.attach()
    }

    override fun onBackPressed() {
        // Realiza acciones específicas antes de cerrar la actividad
        volverLogin(this)
        // Por ejemplo, puedes mostrar un cuadro de diálogo de confirmación

        // Luego, llama al método original para cerrar la actividad
        super.onBackPressed()
    }

    private fun volverLogin(context: Context) {
        val signInClient = Identity.getSignInClient(this)
        signInClient.signOut()

        val intent = Intent(context, Login::class.java)

        context.startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mnOp1 -> {
                toggleSound()
            }

            R.id.mnBusqueda -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toggleSound() {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        if (soundEnabled) {
            Toast.makeText(
                this,
                "Se ha activado el sonido",
                Toast.LENGTH_SHORT
            ).show()

        } else {
            Toast.makeText(
                this,
                "Se ha desactivado el sonido",
                Toast.LENGTH_SHORT
            ).show()

        }
        soundEnabled = !soundEnabled
    }
}