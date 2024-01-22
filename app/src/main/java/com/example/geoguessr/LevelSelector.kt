package com.example.geoguessr

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.geoguessr.databinding.ActivityLeverselectorBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class LevelSelector : AppCompatActivity() {

    lateinit var binding: ActivityLeverselectorBinding

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser


    val db = Firebase.firestore
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

        binding.btnHistoricos.setOnClickListener {
            mostrarHistoricos()
        }
    }

    private fun openImages(context: Context, level:Int) {
        val intent = Intent(context, MainActivity::class.java)
        // Pasa la posición como un extra en el Intent
        intent.putExtra("levelGame", level)
        context.startActivity(intent)
    }

    private fun mostrarHistoricos() {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            // Consulta Firestore para obtener el historial del usuario
            db.collection("Usuarios")
                .document(user.email.toString())
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // El documento existe, recupera el historial
                        val historicos = documentSnapshot.get("historico.historico") as? List<Map<String, Any>>

                        if (historicos != null && historicos.isNotEmpty()) {
                            // Construye el texto para el diálogo
                            val historicosText = StringBuilder()

                            for ((index, historico) in historicos.withIndex()) {
                                val latitud = historico["latitud"]
                                val longitud = historico["longitud"]

                                // Construye el texto para cada historico
                                historicosText.append("Historico ${index + 1} - Latitud: $latitud Longitud: $longitud\n\n")
                            }

                            // Muestra el diálogo con la información del historial
                            mostrarDialogoHistoricos(historicosText.toString())
                        } else {
                            // No hay historicos para mostrar
                            Toast.makeText(
                                applicationContext,
                                "No hay historicos para mostrar",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        // El documento del usuario no existe
                        Toast.makeText(
                            applicationContext,
                            "El documento del usuario no existe",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener { exception ->
                    // Error al obtener el documento del usuario
                    Log.e("Firestore", "Error al obtener el documento del usuario", exception)
                    Toast.makeText(
                        applicationContext,
                        "Error al obtener el historial",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun mostrarDialogoHistoricos(texto: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Historial")
        builder.setMessage(texto)
        builder.setPositiveButton("Aceptar", null)
        builder.show()
    }

}