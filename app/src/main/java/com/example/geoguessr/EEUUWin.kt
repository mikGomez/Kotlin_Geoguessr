package com.example.geoguessr

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class EEUUWin : AppCompatActivity() {
    var mediaPlayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eeuuwin)

        sonidoAcertado()


    }
    override fun onBackPressed() {
        // Realiza acciones específicas antes de cerrar la actividad
        mediaPlayer?.stop()
        volverMainActivity(this)
        super.onBackPressed()
    }
    private fun sonidoAcertado(){
        mediaPlayer = MediaPlayer.create(this,R.raw.eeuuwin)

        mediaPlayer?.start()
    }
    private fun volverMainActivity(context: Context) {
        val intent = Intent(context, GameSelector::class.java)

        context.startActivity(intent)
    }
}