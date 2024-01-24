package com.example.geoguessr

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.geoguessr.databinding.ActivityLoginBinding

import com.example.geoguessr.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore


import java.io.File

@Suppress("VARIABLE_WITH_REDUNDANT_INITIALIZER")
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: ViewPagerAdapter
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    val db = Firebase.firestore
    lateinit var imageList: List<String> // Cambiado de Int a String para almacenar las URL de las imágenes



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = findViewById(R.id.idViewPager)
        val nivel = intent.getIntExtra("levelGame", -1)

        imageList = ArrayList<String>()
        cargarImagenesDesdeStorage(nivel)

        binding.btnRes.setOnClickListener {
            resetUsu(nivel)
        }

        viewPagerAdapter = ViewPagerAdapter(this@MainActivity, imageList, nivel)
        viewPager.adapter = viewPagerAdapter
    }

    override fun onBackPressed() {
        // Realiza acciones específicas antes de cerrar la actividad
        volverLevelSelector(this)
        // Por ejemplo, puedes mostrar un cuadro de diálogo de confirmación

        // Luego, llama al método original para cerrar la actividad
        super.onBackPressed()
    }

    private fun volverLevelSelector(context: Context) {
        val intent = Intent(context, LevelSelector::class.java)

        context.startActivity(intent)
    }

    private fun cargarImagenesDesdeStorage(nivel: Int) {
        val nivelPath = if (nivel == 1) "nivel1" else "nivel2"
        // Iterar sobre las imágenes en Firebase Storage
        for (i in 1..5) {
            val imageName = "imagen_$i.jpg"
            val imageUrl = "$nivelPath/$imageName"
            imageList += imageUrl
        }
    }
    private fun resetUsu(nivel: Int){
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val usuarioDocRef = db.collection("Usuarios").document(user.email.toString())
            var nuevosDatos = hashMapOf<String, Any>(
                "puntuacion" to 0,
                "nivel.nivel1.descubierto0" to false,
                "nivel.nivel1.descubierto1" to false,
                "nivel.nivel1.descubierto2" to false,
                "nivel.nivel1.descubierto3" to false,
                "nivel.nivel1.descubierto4" to false,
                "nivel.nivel2.descubierto0" to false,
                "nivel.nivel2.descubierto1" to false,
                "nivel.nivel2.descubierto2" to false,
                "nivel.nivel2.descubierto3" to false,
                "nivel.nivel2.descubierto4" to false
            )
            usuarioDocRef.update(nuevosDatos)
                .addOnSuccessListener {
                    Log.e("PVR", "Los puntos son: ${nuevosDatos["puntuacion"]}")
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        applicationContext,
                        "Fallo al actualizar los campos", Toast.LENGTH_SHORT
                    ).show()
                }
        }

    }
}

