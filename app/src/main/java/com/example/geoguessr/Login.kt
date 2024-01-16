package com.example.geoguessr

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.geoguessr.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.FirebaseMessaging

class Login : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseauth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    val TAG = "PVR"

    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Para la autenticación, de cualquier tipo.
        firebaseauth = FirebaseAuth.getInstance()


        //se hace un signOut por si había algún login antes.
        firebaseauth.signOut()
        //clearGooglePlayServicesCache()

        //esta variable me conecta con  google. y todo este bloque prepara la ventana de google que se destripa en el loginInGoogle
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.idGoogle))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.btnGoogle.setOnClickListener {
            loginEnGoogle()
        }
    }

    //******************************* Para el login con Google ******************************
    private fun loginEnGoogle() {
        //este método es nuestro.
        val signInClient = googleSignInClient.signInIntent
        launcherVentanaGoogle.launch(signInClient)
        //milauncherVentanaGoogle.launch(signInClient)
    }

    //con este launcher, abro la ventana que me lleva a la validacion de Google.
    private val launcherVentanaGoogle =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            //si la ventana va bien, se accede a las propiedades que trae la propia ventana q llamamos y recogemos en result.
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                manejarResultados(task)
            }
        }

    //es como una muñeca rusa, vamos desgranando, de la ventana a task y de task a los datos concretos que me da google.
    private fun manejarResultados(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                actualizarUI(account)
            }
        } else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizarUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d(TAG, "Token actual del dispositivo: $token")
            } else {
                Log.e(TAG, "Error al obtener el token del dispositivo", task.exception)
            }
        }

        firebaseauth.signInWithCredential(credential).addOnCompleteListener { authTask ->
            if (authTask.isSuccessful) {
                val userEmail = account.email.toString()

                // Verificar si el usuario ya existe en Firestore antes de guardarlo
                db.collection("Usuarios")
                    .document(userEmail)
                    .get()
                    .addOnCompleteListener { userSnapshotTask ->
                        if (userSnapshotTask.isSuccessful) {
                            val userSnapshot = userSnapshotTask.result
                            if (userSnapshot != null && userSnapshot.exists()) {
                                // El usuario ya existe en Firestore, no sobrescribir los datos
                                Log.d(TAG, "El usuario ya existe en Firestore")
                                // Actualizar la información del usuario en la interfaz de usuario
                                actualizarInformacionUsuario()
                                // Ir a la actividad principal
                                openLevelSelector()
                            } else {
                                // El usuario no existe en Firestore, guardar los datos
                                val user = hashMapOf(
                                    "nombre" to account.displayName,
                                    "record" to 0,
                                    "nivel" to 1
                                )

                                // Guardar los datos del usuario en Firestore
                                db.collection("Usuarios")
                                    .document(userEmail)
                                    .set(user)
                                    .addOnSuccessListener {
                                        Log.d(TAG, "Usuario añadido con éxito a la base de datos")
                                    }
                                    .addOnFailureListener {
                                        Log.e(TAG, "Usuario no añadido a la base de datos", it)
                                    }

                                // Actualizar la información del usuario en la interfaz de usuario
                                actualizarInformacionUsuario()
                                // Ir a la actividad principal
                                openLevelSelector()
                            }
                        } else {
                            Log.e(
                                TAG,
                                "Error al verificar la existencia del usuario en Firestore",
                                userSnapshotTask.exception
                            )
                        }
                    }
            } else {
                Toast.makeText(this, authTask.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Metodo para el registro/login con google
     */
    private fun actualizarInformacionUsuario() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // Obtener los datos actualizados del usuario desde Firestore
            db.collection("Usuarios")
                .document(user.email.toString())
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        // Actualizar la interfaz de usuario con los datos de Firestore
                        val nombre = document.getString("nombre")
                        // Actualizar la interfaz de usuario con el nuevo nombre, etc.
                        // Puedes hacer lo mismo para otros datos del usuario
                    } else {
                        Log.d(TAG, "No se encontró el documento en Firestore")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error al obtener datos de Firestore", e)
                }
        }
    }

    private fun openLevelSelector() {

        val homeIntent = Intent(this, LevelSelector::class.java).apply {

        }
        startActivity(homeIntent)
    }




}