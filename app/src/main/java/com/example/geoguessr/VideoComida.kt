package com.example.geoguessr

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import android.widget.Toast
import com.example.geoguessr.databinding.ActivityVideoComidaBinding


class VideoComida : AppCompatActivity() {
    var mediaControls: MediaController? = null
    lateinit var binding : ActivityVideoComidaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityVideoComidaBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.botPlay.setOnClickListener {
            //nos posiciona en el vídeo que queremos manejar
            if (mediaControls == null) {
                // creating an object of media controller class
                mediaControls = MediaController(this)

                // set the anchor view for the video view
                mediaControls!!.setAnchorView(binding.vv)
            }
            // set the media controller for video view
            binding.vv!!.setMediaController(mediaControls)

            // set the absolute path of the video file which is going to be played
            binding.vv!!.setVideoURI(
                Uri.parse("android.resource://"
                        + packageName + "/" + R.raw.mememe))

            binding.vv!!.requestFocus()

            // arranca the video
            binding.vv!!.start()

            // display a toast message
            // after the video is completed
            binding.vv!!.setOnCompletionListener {
                Toast.makeText(applicationContext, "Video completed",
                    Toast.LENGTH_LONG).show()
            }

            // display a toast message if any
            // error occurs while playing the video
            binding.vv!!.setOnErrorListener { mp, what, extra ->
                Toast.makeText(applicationContext, "An Error Occurred " +
                        "While Playing Video !!!", Toast.LENGTH_LONG).show()
                false
            }
        }

        binding.botPausar.setOnClickListener {

            //pausa la ejecución
            binding.vv!!.pause()

        }

        binding.botContinuar.setOnClickListener {
            //continua la ejecución por dónde iba si se había pausado
            binding.vv!!.start()
        }

        binding.botDetener.setOnClickListener {
            //detiene completamente
            binding.vv!!.stopPlayback()
        }

    }
}