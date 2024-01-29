package com.example.geoguessr

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.geoguessr.databinding.ActivityMapsGameBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.io.File
import java.lang.Math.atan2
import java.lang.Math.cos
import java.lang.Math.sin
import java.lang.Math.sqrt

class MapsGame : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, GoogleMap.OnPoiClickListener,
    GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {

    private val LOCATION_REQUEST_CODE: Int = 0
    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0
    private  var nombrePlato = ""
    private var detallesPlato = ""
    private var ciudadPlato = ""
    private var nivel: Int = 0
    private var intentos: Int = 5
    private var position : Int = 0
    private var fotoPlato : String = ""
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser


    var mediaPlayer: MediaPlayer? = null


    val db = Firebase.firestore


    var alMarcadores = ArrayList<Marker>()
    lateinit var binding : ActivityMapsGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cargarPuntuacion()


        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        binding.txtIntentosNum.setText(intentos.toString())
        position = intent.getIntExtra("IMAGE_POSITION", -1)
        latitud = intent.getDoubleExtra("LATITUD", 0.0)
        longitud = intent.getDoubleExtra("LONGITUD", 0.0)
        nivel = intent.getIntExtra("NIVEL", 0)
        ciudadPlato = intent.getStringExtra("CIUDAD").toString()
        nombrePlato = intent.getStringExtra("NOMBREPLATO").toString()
        detallesPlato = intent.getStringExtra("DETALLES").toString()
        fotoPlato = intent.getStringExtra("FOTOPLATO").toString()

        recuperarDescubierto(position)

            binding.imgDetalle.setOnClickListener {
                if (fotoPlato.equals("gachas_ciudad_real")) {
                    val intent = Intent(this, VideoComida::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(
                        applicationContext,
                        "El video esta en mantenimiento",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }


    }


    override fun onBackPressed() {
        // Realiza acciones específicas antes de cerrar la actividad
        volverMainActivity(this)

        super.onBackPressed()
    }

    private fun volverMainActivity(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("levelGame",nivel)

        context.startActivity(intent)
    }

    private fun recuperarDescubierto(position: Int){
        if (currentUser != null) {
            // El usuario está autenticado, obtener el correo electrónico
            val userEmail = currentUser.email

            // Verificar si el correo electrónico no es nulo
            if (userEmail != null) {
                // Construir la referencia al documento del usuario en Firestore
                val userDocument = db.collection("Usuarios").document(userEmail)

                // Obtener los datos del usuario desde Firestore
                userDocument.get()
                    .addOnSuccessListener { document ->
                        if (document != null && document.exists()) {
                            var nivel1Data = document.get("nivel.nivel1") as? Map<String, Boolean>
                            if(nivel == 2){
                                nivel1Data = document.get("nivel.nivel2") as? Map<String, Boolean>
                            }
                            // El documento existe, recuperar los datos
                            if (nivel1Data != null) {
                                // Verificar si la variable en la posición específica es true o false
                                val variableValue = nivel1Data["descubierto$position"] ?: false
                                if (!variableValue){
                                    binding.mapView.setVisibility(View.VISIBLE)
                                    binding.txtName2.setVisibility(View.VISIBLE)
                                    binding.txtTienes.setVisibility(View.VISIBLE)
                                    binding.txtPuntuacion.setVisibility(View.VISIBLE)
                                    binding.txtPuntuacionNum.setVisibility(View.VISIBLE)
                                    binding.txtIntentosNum.setVisibility(View.VISIBLE)
                                    binding.txtIntentos.setVisibility(View.VISIBLE)
                                    binding.mapView.setVisibility(View.VISIBLE)
                                    binding.txtName2.text = nombrePlato

                                    binding.imgDetalle.setVisibility(View.GONE)
                                    binding.txtName.setVisibility(View.GONE)
                                    binding.txtDetalles.setVisibility(View.GONE)
                                    binding.txtCambiarCiudad.setVisibility(View.GONE)
                                    binding.txtCambiarDescr.setVisibility(View.GONE)
                                    binding.txtCiudad.setVisibility(View.GONE)
                                    binding.txtDescr.setVisibility(View.GONE)
                                    binding.imageView2.setVisibility(View.GONE)
                                    binding.imageView3.setVisibility(View.GONE)
                                    binding.imageView4.setVisibility(View.GONE)
                                    binding.imageView5.setVisibility(View.GONE)
                                    binding.imageView6.setVisibility(View.GONE)
                                    binding.txtVideo.setVisibility(View.GONE)
                                }else{
                                    binding.imgDetalle.setVisibility(View.VISIBLE)
                                    binding.txtDetalles.setVisibility(View.VISIBLE)
                                    binding.txtCambiarCiudad.setVisibility(View.VISIBLE)
                                    binding.txtCambiarDescr.setVisibility(View.VISIBLE)
                                    binding.txtCiudad.setVisibility(View.VISIBLE)
                                    binding.txtDescr.setVisibility(View.VISIBLE)
                                    binding.imageView2.setVisibility(View.VISIBLE)
                                    binding.imageView3.setVisibility(View.VISIBLE)
                                    binding.imageView4.setVisibility(View.VISIBLE)
                                    binding.imageView5.setVisibility(View.VISIBLE)
                                    binding.imageView6.setVisibility(View.VISIBLE)
                                    binding.txtVideo.setVisibility(View.VISIBLE)
                                    binding.txtName.setVisibility(View.VISIBLE)
                                    binding.txtCambiarDescr.text = detallesPlato
                                    binding.txtCambiarCiudad.text = ciudadPlato
                                    binding.txtName.text = nombrePlato

                                    val drawable = resources.getIdentifier(fotoPlato,"drawable",packageName)
                                    binding.imgDetalle.setImageResource(drawable)

                                    binding.mapView.setVisibility(View.GONE)
                                    binding.txtName2.setVisibility(View.GONE)
                                    binding.txtTienes.setVisibility(View.GONE)
                                    binding.txtPuntuacion.setVisibility(View.GONE)
                                    binding.txtPuntuacionNum.setVisibility(View.GONE)
                                    binding.txtIntentosNum.setVisibility(View.GONE)
                                    binding.txtIntentos.setVisibility(View.GONE)
                                    binding.mapView.setVisibility(View.GONE)
                                }

                            } else {
                                Log.e(TAG, "No se encontraron datos en nivel1")
                            }
                        } else {
                            Log.e(TAG, "El documento del usuario no existe")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Error al obtener el documento del usuario", exception)
                    }
            } else {
                Log.e(TAG, "El correo electrónico del usuario es nulo")
            }
        } else {
            Log.e(TAG, "El usuario no está autenticado")
        }
    }

    private fun comprobarCoordenadas(
        context: Context,
        latitud: Double,
        longitud: Double,
        marcador: Marker
    ) {
        var valorPuntuacion = binding.txtPuntuacionNum.text.toString()
        var puntuacion = valorPuntuacion.toInt()
        val latitudJugador: Double = marcador.position.latitude
        val longitudJugador: Double = marcador.position.longitude
        val winner: Boolean
        val distancia: Double =
            calcularDistancia(latitud, longitud, latitudJugador, longitudJugador)

        if (distancia <= 3) {
            winner = true
            dialog(winner)
        } else {
            intentos--
            binding.txtIntentosNum.setText(intentos.toString())
            if (latitud > latitudJugador) {
                Toast.makeText(
                    applicationContext,
                    "La comida está más al norte. Te quedan $intentos intentos", Toast.LENGTH_LONG
                ).show()
                var nuevaPunt:Int = 0
                nuevaPunt = restarPuntuacion(puntuacion,intentos)
                guardarPuntuacion(nuevaPunt)
                binding.txtPuntuacionNum.setText(nuevaPunt.toString())

            } else if (latitud < latitudJugador) {
                Toast.makeText(
                    applicationContext,
                    "La comida está más al sur. Te quedan $intentos intentos", Toast.LENGTH_LONG
                ).show()
                var nuevaPunt:Int = 0
                nuevaPunt = restarPuntuacion(puntuacion,intentos)
                guardarPuntuacion(nuevaPunt)
                binding.txtPuntuacionNum.setText(nuevaPunt.toString())
            }
            if (intentos == 0) {
                winner = false
                dialog(winner)
            }
        }
    }

    private fun dialog(winner: Boolean) {
        val builder = AlertDialog.Builder(this)
        var valorPuntuacion = binding.txtPuntuacionNum.text.toString()
        var puntuacion = valorPuntuacion.toInt()
        if (winner) {
            with(builder)
            {
                setTitle("HAS ACERTADO")
                sonidoAcertado()
                puntuacion += 100
                guardarPuntuacion(puntuacion)
                guardarRecord(puntuacion)
                actualizarDescubierto(position,true)

                binding.txtPuntuacionNum.setText(puntuacion.toString())
                Toast.makeText(
                    applicationContext,
                    puntuacion.toString(), Toast.LENGTH_SHORT
                ).show()
                setMessage("Pulsa OK para elegir otro plato o SALIR para ir al menu de juegos")

                setPositiveButton(
                    "OK",
                    DialogInterface.OnClickListener(function = { dialog: DialogInterface, which: Int ->
                        volverMenuPrincipal(this@MapsGame)
                    })
                )
                setNegativeButton("Salir", ({ dialog: DialogInterface, which: Int ->
                    goGameSelector(this@MapsGame)
                }))


                show() //builder.show()
            }
        } else {
            with(builder)
            {
                actualizarDescubierto(position,false)
                setTitle("SE HAN ACABADO TODOS TUS INTENTOS")
                setMessage("Se han restado 10 puntos. Pulsa para volver al menu de juegos")
                sonidoIntentosCero()
                var nuevaPunt:Int = 0
                nuevaPunt = restarPuntuacion(puntuacion,intentos)
                guardarPuntuacion(nuevaPunt)
                binding.txtPuntuacionNum.setText(nuevaPunt.toString())
                //Otra forma es definir directamente aquí lo que se hace cuando se pulse.
                setPositiveButton(
                    "OK",
                    DialogInterface.OnClickListener(function = { dialog: DialogInterface, which: Int ->
                        goGameSelector(this@MapsGame)
                    })
                )
                show() //builder.show()
            }
        }

    }

    private fun goGameSelector(context: Context) {
        val intent = Intent(context, GameSelector::class.java)

        context.startActivity(intent)
    }

    private fun volverMenuPrincipal(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        // Pasa la posición como un extra en el Intent
        intent.putExtra("levelGame", nivel)
        context.startActivity(intent)
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun calcularDistancia(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val radioTierra = 6371 // Radio de la Tierra en kilómetros

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a =
            sin(dLat / 2) * sin(dLat / 2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                    sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return radioTierra * c
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMapClickListener {
            Toast.makeText(this, "Manten pulsado para marcar una posicion", Toast.LENGTH_SHORT).show()
        }
        map.mapType = GoogleMap.MAP_TYPE_HYBRID
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
        map.setOnPoiClickListener(this)
        map.setOnMapLongClickListener(this)
        map.setOnMarkerClickListener(this)
        createMarker()
        enableMyLocation()
    }


    //----------------------------------------------------------------------------------------
    @SuppressLint("MissingPermission")
    fun enableMyLocation() {
        if (!::map.isInitialized) return
        if (isPermissionsGranted()) {
            map.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }
    }

    /**
     * función que usaremos a lo largo de nuestra app para comprobar si el permiso ha sido aceptado o no.
     */
    fun isPermissionsGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    /**
     * Método que solicita los permisos.
     */
    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(this, "Ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
        }
    }


//-----------------------------------------------------------------------------------------------------
    //----------------------------------------- Eventos en el mapa ----------------------------------------
    //-----------------------------------------------------------------------------------------------------

    /**
     * Se dispara cuando pulsamos la diana que nos centra en el mapa (punto negro, arriba a la derecha en forma de diana).
     */
    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "Recentrando", Toast.LENGTH_SHORT).show()
        return false
    }

    /**
     * Se dispara cuando pulsamos en nuestra localización exacta donde estámos ahora (punto azul).
     */
    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(this, "Estás en ${p0.latitude}, ${p0.longitude}", Toast.LENGTH_SHORT).show()
    }

    /**
     * Con el parámetro podremos obtener información del punto de interés. Este evento se lanza cuando pulsamos en un POI.
     */
    override fun onPoiClick(p0: PointOfInterest) {
        Toast.makeText(this, "Pulsado.", Toast.LENGTH_LONG).show()
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.run {
            setTitle("Información del lugar.")
            setMessage("Id: " + p0!!.placeId + "\nNombre: " + p0!!.name + "\nLatitud: " + p0!!.latLng.latitude.toString() + " \nLongitud: " + p0.latLng.longitude.toString())
            setPositiveButton("Aceptar") { dialog: DialogInterface, i: Int ->

            }
        }
        dialogBuilder.create().show()

    }

    /**
     * Con el parámetro crearemos un marcador nuevo. Este evento se lanzará al hacer un long click en alguna parte del mapa.
     */
    override fun onMapLongClick(p0: LatLng) {
        map.clear()
        var marcador = map.addMarker(MarkerOptions().position(p0!!).title("Nuevo marcador"))
        alMarcadores.add(marcador!!)

        pintarCirculo(marcador.position)
        comprobarCoordenadas(this, latitud, longitud, marcador)
        guardarHistorico(marcador.position.latitude,marcador.position.longitude)

        Log.e("ACSCO", "Marcador añadido, marcadores actuales: ${alMarcadores.toString()}")
    }

    /**
     * Este evento se lanza cuando hacemos click en un marcador.
     */
    override fun onMarkerClick(p0: Marker): Boolean {
        Toast.makeText(this, "Estás en ${p0!!.title}, ${p0!!.position}", Toast.LENGTH_SHORT).show()

        p0.remove()  //---> Para borrarlo cuando hago click sobre él solo hay que descomentar esto.
        alMarcadores.removeAt(alMarcadores.indexOf(p0))
        Log.e("ACSCO", "Marcador eliminado, marcadores actuales: ${alMarcadores.toString()}")
        map.clear()

        return true;
    }

    //------------------------------------------------------------------------------------------------------

    /**
     * Dibuja una línea recta desde nuestra ubicación actual al CIFP Virgen de Gracia.
     */
    fun pintarCirculo(centerPosition: LatLng) {
        map.addCircle(CircleOptions().run {
            center(centerPosition)
            radius(3000.0)
            strokeColor(Color.BLUE)
            fillColor(Color.GREEN)
        })
    }

    /**
     * Método en el que crearemos algunos marcadores de ejemplo.
     */
    private fun createMarker() {
        val markerMadrid = LatLng(40.4168, -3.7038)
        /*
        Los markers se crean de una forma muy sencilla, basta con crear una instancia de un objeto LatLng() que recibirá dos
        parámetros, la latitud y la longitud. Yo en este ejemplo he puesto las coordenadas de mi playa favorita.
        */
        //map.addMarker(MarkerOptions().position(markerCIFP).title("Mi CIFP favorito!"))
        //Si queremos cambiar el color del icono, en este caso azul cyan, con un subtexto.
        val markCIFP = map.addMarker(
            MarkerOptions().position(markerMadrid).title("Mi instituto favorito!").icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)
            ).snippet("IES MAESTRE DE CALATRAVA")
        )
        alMarcadores.add(markCIFP!!)

        val marcadorComida = LatLng(latitud, longitud)

        val markCR = map.addMarker(MarkerOptions().position(marcadorComida))
        alMarcadores.add(markCR!!)


        /*
        La función animateCamera() recibirá tres parámetros:

            Un CameraUpdateFactory que a su vez llevará otros dos parámetros, el primero las coordenadas donde queremos hacer zoom
                y el segundo valor (es un float) será la cantidad de zoom que queremos hacer en dichas coordenadas.
            La duración de la animación en milisegundos, por ejemplo 4000 milisegundos son 4 segundos.
            Un listener que no vamos a utilizar, simplemente añadimos null.
         */
        //------------ Zoom hacia un marcador ------------
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(markerMadrid, 6f),
            4000,
            null
        )
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

    /**
     * Metodo para cargar la puntuacion actual del usuario
     */
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

                        binding.txtPuntuacionNum.setText(punt.toString())

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

    /**
     * Metood para actualizar el estado de un mapa si el jugador acierta
     */
    private fun actualizarDescubierto(position: Int, nuevoValor: Boolean) {
        if (currentUser != null) {
            val userEmail = currentUser.email

            if (userEmail != null) {
                val userDocument = db.collection("Usuarios").document(userEmail)

                // Construir el mapa de actualización

                var updateMap = hashMapOf<String, Any>(
                    "nivel.nivel1.descubierto$position" to nuevoValor
                )
                if (nivel == 2){
                    updateMap = hashMapOf<String, Any>(
                        "nivel.nivel2.descubierto$position" to nuevoValor
                    )
                }

                // Actualizar el documento en Firestore
                userDocument.update(updateMap)
                    .addOnSuccessListener {
                        Log.d(TAG, "Descubierto$position actualizado exitosamente a $nuevoValor")
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Error al actualizar descubierto$position", exception)
                    }
            } else {
                Log.e(TAG, "El correo electrónico del usuario es nulo")
            }
        } else {
            Log.e(TAG, "El usuario no está autenticado")
        }
    }

    /**
     * Metodo para guardar el historico de tiradas
     */
    private fun guardarHistorico(lat: Double, long: Double) {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            // Construye el objeto de historial que quieres guardar
            val nuevoHistorico = hashMapOf(
                "latitud" to lat,
                "longitud" to long,
            )
            try {
                // Actualiza el historial en Firestore
                db.collection("Usuarios")
                    .document(user.email.toString())
                    .update("historico.historico", FieldValue.arrayUnion(nuevoHistorico))
                    .addOnSuccessListener {
                        Log.d("Firestore", "Historial guardado exitosamente")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Error al guardar el historial", e)
                        Toast.makeText(
                            applicationContext,
                            "Fallo al guardar el historial",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } catch (e: Exception) {
                Log.e("Firestore", "Excepción al guardar historial: ${e.message}")
                Toast.makeText(
                    applicationContext,
                    "Excepción al guardar historial",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Metodo para restar PUNTUACION si falla
     */
    private fun restarPuntuacion(puntu:Int, nIntentos:Int):Int{
        var nuevaPunt:Int= 0
        if(puntu == 0){
            nuevaPunt = puntu
        }
        else if (nIntentos == 0){
            nuevaPunt = puntu - 10
        }else{
            nuevaPunt = puntu - 5
        }


        return nuevaPunt
    }

    private fun guardarRecord(puntuacion: Int){
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            db.collection("Usuarios")
                .document(user.email.toString())
                .update("record", puntuacion)
                .addOnSuccessListener {
                    Log.e("PVR", "El record es : $puntuacion")
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        applicationContext,
                        "Fallo al actualizar el record", Toast.LENGTH_SHORT
                    ).show()

                }
        }
    }

    private fun sonidoAcertado(){
        mediaPlayer = MediaPlayer.create(this,R.raw.acertado)

        mediaPlayer?.start()
    }

    private fun sonidoIntentosCero(){
        mediaPlayer = MediaPlayer.create(this,R.raw.intentoscero)

        mediaPlayer?.start()
    }
}
