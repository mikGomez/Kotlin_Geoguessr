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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.geoguessr.databinding.ActivityLeverselectorBinding
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
    private var nivel: Int = 0
    private var intentos: Int = 5
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser


    val db = Firebase.firestore


    var alMarcadores = ArrayList<Marker>()
    lateinit var binding: ActivityMapsGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cargarPuntuacion()


        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        binding.txtIntentosNum.setText(intentos.toString())
        val position = intent.getIntExtra("IMAGE_POSITION", -1)
        latitud = intent.getDoubleExtra("LATITUD", 0.0)
        longitud = intent.getDoubleExtra("LONGITUD", 0.0)
        nivel = intent.getIntExtra("NIVEL", 0)

        recuperarDescubierto(position)

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
                            // El documento existe, recuperar los datos
                            val nivel1Data = document.get("nivel.nivel1") as? Map<String, Boolean>

                            if (nivel1Data != null) {
                                // Verificar si la variable en la posición específica es true o false
                                val variableValue = nivel1Data["descubierto$position"] ?: false
                                if (!variableValue){
                                    binding.mapView.setVisibility(View.VISIBLE)
                                    binding.txtTienes.setVisibility(View.VISIBLE)
                                    binding.txtPuntuacion.setVisibility(View.VISIBLE)
                                    binding.txtIntentosNum.setVisibility(View.VISIBLE)
                                    binding.txtIntentos.setVisibility(View.VISIBLE)
                                    binding.mapView.setVisibility(View.VISIBLE)

                                    binding.imgDetalle.setVisibility(View.GONE)
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

                                    binding.mapView.setVisibility(View.GONE)
                                    binding.txtTienes.setVisibility(View.GONE)
                                    binding.txtPuntuacion.setVisibility(View.GONE)
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
                showToast(context, "La comida está más al norte. Te quedan $intentos intentos")
            } else if (latitud < latitudJugador) {
                showToast(context, "La comida está más al sur. Te quedan $intentos intentos")
            }
            if (intentos == 0) {
                winner = false
                dialog(winner)
            }
        }
    }

    private fun dialog(winner: Boolean) {
        val builder = AlertDialog.Builder(this)

        if (winner) {
            with(builder)
            {
                setTitle("HAS ACERTADO")

                var valorPuntuacion = binding.txtPuntuacionNum.text.toString()
                var puntuacion = valorPuntuacion.toInt()
                puntuacion += 100

                binding.txtPuntuacionNum.setText(puntuacion.toString())
                Toast.makeText(
                    applicationContext,
                    puntuacion.toString(), Toast.LENGTH_SHORT
                ).show()
                setMessage("El plato tal tal. Pulsa OK para elegir otro plato o SALIR para ir al menu de juegos")

                setPositiveButton(
                    "OK",
                    DialogInterface.OnClickListener(function = { dialog: DialogInterface, which: Int ->
                        volverMenuPrincipal(this@MapsGame)
                    })
                )
                setNegativeButton("Salir", ({ dialog: DialogInterface, which: Int ->
                    closeApp(puntuacion)
                }))


                show() //builder.show()
            }
        } else {
            with(builder)
            {
                setTitle("SE HAN ACABADO TODOS TUS INTENTOS")
                setMessage("Pulsa para volver al menu de juegos")
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

    private fun closeApp(puntu: Int) {
        val builder = AlertDialog.Builder(this)
        val process: Process
        with(builder)
        {
            setTitle("VAS A SALIR AL MENU DE JUEGO")
            setMessage("¿Deseas guardar los datos de tu partida? (nivel, puntuación y tiradas) ")
            //Otra forma es definir directamente aquí lo que se hace cuando se pulse.
            setPositiveButton(
                "GUARDAR Y SALIR",
                DialogInterface.OnClickListener(function = { dialog: DialogInterface, which: Int ->
                    saveGame()
                    guardarPuntuacion(puntu)
                    goGameSelector(this@MapsGame)
                })
            )
            setNegativeButton("NO GUARDAR Y SALIR", ({ dialog: DialogInterface, which: Int ->
                goGameSelector(this@MapsGame)
            }))
            show() //builder.show()
        }
    }

    private fun saveGame() {

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
        val location = LatLng(37.7749, -122.4194) // Coordenadas de San Francisco, por ejemplo

        //googleMap.addMarker(MarkerOptions().position(location).title("Marker in San Francisco"))
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))

        map = googleMap
        //Se pueden seleccionar varios tiops de mapas:
        //  None --> no muestra nada, solo los marcadores. (MAP_TYPE_NONE)
        //  Normal --> El mapa por defecto. (MAP_TYPE_NORMAL)
        //  Satélite --> Mapa por satélite.  (MAP_TYPE_SATELLITE)
        //  Híbrido --> Mapa híbrido entre Normal y Satélite. (MAP_TYPE_HYBRID) Muestra satélite y mapas de carretera, ríos, pueblos, etc... asociados.
        //  Terreno --> Mapa de terrenos con datos topográficos. (MAP_TYPE_TERRAIN)
        map.mapType = GoogleMap.MAP_TYPE_HYBRID
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
        map.setOnPoiClickListener(this)
        map.setOnMapLongClickListener(this)
        map.setOnMarkerClickListener(this)
        createMarker()
        enableMyLocation() //--> Hanilita, pidiendo permisos, la localización actual.
        //irubicacioActual() //--> Nos coloca en la ubicación actual directamente. Comenta createMarker par ver esto.
        //pintarCirculo()
        //pintarRuta()
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

    /**
     * Con este método vamos a ajustar el tamaño de todos los iconos que usemos en los marcadores.
     */
    fun sizeIcon(idImage: Int): BitmapDescriptor {
        val altura = 60
        val anchura = 60

        var draw = ContextCompat.getDrawable(this, idImage) as BitmapDrawable
        val bitmap = draw.bitmap  //Aquí tenemos la imagen.

        //Le cambiamos el tamaño:
        val smallBitmap = Bitmap.createScaledBitmap(bitmap, anchura, altura, false)
        return BitmapDescriptorFactory.fromBitmap(smallBitmap)

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

    /**
     * Nos coloca en la ubicación actual.
     */
    @SuppressLint("MissingPermission")
    private fun irubicacioActual() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val miUbicacion = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val latLng = LatLng(miUbicacion!!.latitude, miUbicacion.longitude)
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng,
                18f
            )
        ) //--> Mueve la cámara a esa posición, sin efecto. El valor real indica el nivel de Zoom, de menos a más.
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
            CameraUpdateFactory.newLatLngZoom(markerMadrid, 10f),
            4000,
            null
        )
    }

    /**
     * Metodo para el registro/login con google
     */
    private fun guardarPuntuacion(puntuacion: Int) {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            db.collection("Usuarios")
                .document(user.email.toString())
                .update("record", puntuacion)
                .addOnSuccessListener {
                    Toast.makeText(
                        applicationContext,
                        puntuacion.toString(), Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        applicationContext,
                        "Fallo al actualizar la puntuacion", Toast.LENGTH_SHORT
                    ).show()

                }
        }
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
                        val punt = document.getLong("record")

                        binding.txtPuntuacionNum.setText(punt.toString())
                        Toast.makeText(
                            applicationContext,
                            punt.toString(), Toast.LENGTH_SHORT
                        ).show()
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


}
