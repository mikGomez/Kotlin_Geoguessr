package com.example.geoguessr

import android.content.Context
import android.content.DialogInterface
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
    var puntuacion:Int= 0

    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeverselectorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val builder = AlertDialog.Builder(this)
        cargarPuntuacion()


        binding.btnLevel1.setOnClickListener {

            with(builder)
            {
                setTitle("VAS A ENTRAR AL NIVEL 1")
                setMessage("Tu puntuación actual se borrará. ¿Estas seguro que deseas continuar?")
                setPositiveButton(
                    "SI",
                    DialogInterface.OnClickListener(function = { dialog: DialogInterface, which: Int ->
                        puntuacion = 0
                        guardarPuntuacion(puntuacion)
                        binding.txtPUNTUACIONINT.setText(puntuacion.toString())
                        openImages(context, 1)
                    })
                )
                setNegativeButton("NO", ({ dialog: DialogInterface, which: Int ->
                    goGameSelector(context)
                }))


                show() //builder.show()
            }
        }
        binding.btnLevel2.setOnClickListener {

            with(builder)
            {
                setTitle("VAS A ENTRAR AL NIVEL 2")
                setMessage("Tu puntuación actual se borrará. ¿Estas seguro que deseas continuar?")
                setPositiveButton(
                    "SI",
                    DialogInterface.OnClickListener(function = { dialog: DialogInterface, which: Int ->
                        puntuacion = 0
                        guardarPuntuacion(puntuacion)
                        binding.txtPUNTUACIONINT.setText(puntuacion.toString())
                        openImages(context, 2)
                    })
                )
                setNegativeButton("NO", ({ dialog: DialogInterface, which: Int ->
                    goGameSelector(context)
                }))


                show() //builder.show()
            }
        }

        binding.btnHistoricos.setOnClickListener {
            mostrarHistoricos()
        }
    }
    override fun onBackPressed() {
        // Realiza acciones específicas antes de cerrar la actividad
        volverGameSelector(this)
        // Por ejemplo, puedes mostrar un cuadro de diálogo de confirmación

        // Luego, llama al método original para cerrar la actividad
        super.onBackPressed()
    }

    private fun volverGameSelector(context: Context) {
        val intent = Intent(context, GameSelector::class.java)

        context.startActivity(intent)
    }

    private fun goGameSelector(context: Context) {
        val intent = Intent(context, GameSelector::class.java)

        context.startActivity(intent)
    }
    private fun cargarPuntuacion(){
        // Obtener el email del usuario actualmente autenticado
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        if (userEmail != null) {
            // Consultar Firestore para obtener el nombre del usuario
            db.collection("Usuarios")
                .document(userEmail)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // El documento existe, obtener el nombre y establecerlo en el botón de perfil
                        val punt = document.getLong("puntuacion")
                        val record = document.getLong("record")
                        binding.txtPUNTUACIONINT.setText(punt.toString())
                        binding.txtRECORDINT.setText(record.toString())

                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Fallo al cargar la puntuacion", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("PVR", "Fallo al cargar la puntuacion", exception)

                }
        } else {
            Toast.makeText(
                applicationContext,
                "Fallo al mostrar la actualizacion", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openImages(context: Context, level: Int) {
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
                        val historicos =
                            documentSnapshot.get("historico.historico") as? List<Map<String, Any>>

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

    /**
     * Metodo para guardar la puntuacion que gana el jugador
     */
    private fun guardarPuntuacion(puntuacion: Int) {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            db.collection("Usuarios")
                .document(user.email.toString())
                .update("puntuacion", puntuacion)
                .addOnSuccessListener {
                    Log.e("PVR", "los puntos son : $puntuacion")
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        applicationContext,
                        "Fallo al actualizar la puntuacion", Toast.LENGTH_SHORT
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