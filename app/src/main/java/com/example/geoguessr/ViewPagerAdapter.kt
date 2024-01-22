package com.example.geoguessr

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.io.File
import java.util.*


class ViewPagerAdapter(val context: Context, val imageList: List<String>, val nivel: Int) : PagerAdapter() {

    var lat:Double = 0.0
    var long:Double = 0.0
    var nombrePlato : String = ""
    var ciudadPlato : String = ""
    var detallesPlato : String = ""
    var fotoPlato = ""
    override fun getCount(): Int {
        return imageList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val itemView: View = mLayoutInflater.inflate(R.layout.image_slider_item, container, false)

        val imageView: ImageView = itemView.findViewById<View>(R.id.idIVImage) as ImageView

        cargarImagenDesdeStorage(imageView, imageList[position])

        // Añade un OnClickListener al ImageView
        imageView.setOnClickListener {
            // Realiza alguna acción según la posición, por ejemplo, abre otra actividad
            when (position) {
                0 -> openMap1(context,position)
                1 -> openMap2(context,position)
                2 -> openMap3(context,position)
                3 -> openMap4(context,position)
                4 -> openMap5(context,position)
            }
        }

        Objects.requireNonNull(container).addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

    private fun cargarImagenDesdeStorage(imageView: ImageView, imageUrl: String) {
        val storageRef = Firebase.storage.reference
        val localFile = File.createTempFile("tempImage", "jpg")

        val refFoto = storageRef.child(imageUrl)
        refFoto.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            imageView.setImageBitmap(bitmap)
        }.addOnFailureListener {
            Toast.makeText(context, "Algo ha fallado en la descarga de la foto", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openMap1(context: Context,position:Int) {
        if (nivel == 1){
            lat = 38.9861
            long = -3.9270
            fotoPlato = "gachas_ciudad_real"
            nombrePlato = "Gachas"
            ciudadPlato = "Ciudad Real"
            detallesPlato = "Las gachas son un plato tradicional preparado en diversas regiones del mundo. " +
                    "Su base consiste en harina de cereal (avena, maíz, trigo o cebada) mezclada con agua o leche, cocida a fuego lento y sazonada con sal. " +
                    "Se pueden personalizar según las preferencias, siendo algunas variantes dulces con miel o frutas, y otras saladas con queso o hierbas. " +
                    "Las gachas pueden servirse caliente y se acompañan a menudo con ingredientes adicionales como frutos secos. " +
                    "La receta varía según la cultura y las tradiciones culinarias locales."

        }else{
            fotoPlato = "cochinillo_segovia"
            nombrePlato = "Cochinillo"
            ciudadPlato = "Segovia"
            detallesPlato = "El cochinillo asado es un plato que destaca por el uso de un lechón o cerdo joven, típicamente con menos de un mes de edad. " +
                    "La preparación común implica sazonar la piel del cochinillo con sal y a menudo se utiliza ajo en la marinada o se frota sobre la piel para dar sabor. Además, se pueden incorporar hierbas y especias para realzar el gusto durante el proceso de asado. " +
                    "El resultado final es una carne tierna con una piel crujiente y dorada, convirtiendo al cochinillo asado en un plato apreciado en diversas culturas gastronómicas."
            lat = 40.9429
            long = -4.1088
        }
        val intent = Intent(context, MapsGame::class.java)

        intent.putExtra("LATITUD",lat)
        intent.putExtra("LONGITUD",long)
        intent.putExtra("NIVEL",nivel)
        intent.putExtra("CIUDAD",ciudadPlato)
        intent.putExtra("NOMBREPLATO",nombrePlato)
        intent.putExtra("DETALLES",detallesPlato)
        intent.putExtra("FOTOPLATO",fotoPlato)
        // Pasa la posición como un extra en el Intent
        intent.putExtra("IMAGE_POSITION", position)


        context.startActivity(intent)
    }

    private fun openMap2(context: Context,position:Int) {
        if (nivel == 1){
            lat = 39.4699
            long = -0.3763
            fotoPlato = "paella_valencia"
            nombrePlato = "Paella"
            ciudadPlato = "Valencia"
            detallesPlato = "La paella es un plato característico de la cocina española, especialmente de la región de Valencia. Sus ingredientes principales incluyen arroz, caldo (ya sea de pollo, pescado o mariscos), y una variedad de proteínas como pollo, conejo, mariscos (mejillones, gambas) y a veces caracoles. " +
                    "También se incorporan ingredientes como tomate, pimiento, azafrán y a menudo judías verdes. La paella se cocina tradicionalmente en una paellera, una sartén plana y amplia, y se destaca por su capa inferior de arroz dorado y crujiente conocida como socarrat. " +
                    "Es un plato emblemático de la gastronomía española, con muchas variantes regionales y personales."
        }else{
            lat = 37.9922
            long = -1.1307
            fotoPlato = "zarangollo_murica"
            nombrePlato = "Zarangollo"
            ciudadPlato = "Murcia"
            detallesPlato = "El Zarangollo es un plato tradicional de la Región de Murcia, España. Sus ingredientes principales son el calabacín, la cebolla, los huevos, aceite de oliva, sal y pimienta. " +
                    "El Zarangollo es apreciado por su sencillez y la mezcla armoniosa de sabores vegetales. " +
                    "Puede servirse como un plato principal o como acompañamiento, y es una deliciosa representación de la cocina regional española."
        }

        val intent = Intent(context, MapsGame::class.java)
        intent.putExtra("LATITUD",lat)
        intent.putExtra("LONGITUD",long)
        intent.putExtra("NIVEL",nivel)
        intent.putExtra("CIUDAD",ciudadPlato)
        intent.putExtra("NOMBREPLATO",nombrePlato)
        intent.putExtra("DETALLES",detallesPlato)
        intent.putExtra("FOTOPLATO",fotoPlato)
        // Pasa la posición como un extra en el Intent
        intent.putExtra("IMAGE_POSITION", position)
        context.startActivity(intent)
    }
    private fun openMap3(context: Context,position:Int) {
        if (nivel == 1){
            lat = 43.3614
            long = -5.8593
            fotoPlato = "fabada_asturias"
            nombrePlato = "Fabada"
            ciudadPlato = "Asturias"
            detallesPlato = "La fabada asturiana es un plato tradicional de la cocina de Asturias, una región en el norte de España. Se trata de un guiso abundante y sustancioso que tiene como ingrediente principal las fabes, " +
                    "una variedad de judía blanca grande y mantecosa." +
                    "La fabada asturiana es apreciada por su combinación de sabores ricos y su textura reconfortante. " +
                    "Es un plato típico que refleja la tradición gastronómica de Asturias y se disfruta especialmente en épocas más frías del año."
        }else{
            lat = 41.6488
            long = -0.8891
            fotoPlato = "ternasco_aragon"
            nombrePlato = "Ternasco"
            ciudadPlato = "Aragon"
            detallesPlato = "La carne de ternasco aragonés es apreciada en la gastronomía local y " +
                    "se utiliza en una variedad de platos tradicionales, como asados, guisos y otras preparaciones culinarias típicas de la región. Este tipo de carne es valorado por su terneza y sabor distintivo," +
                    " y su producción y consumo están respaldados por medidas de calidad específicas."
        }
        val intent = Intent(context, MapsGame::class.java)

        intent.putExtra("LATITUD",lat)
        intent.putExtra("LONGITUD",long)
        intent.putExtra("NIVEL",nivel)
        intent.putExtra("CIUDAD",ciudadPlato)
        intent.putExtra("NOMBREPLATO",nombrePlato)
        intent.putExtra("DETALLES",detallesPlato)
        intent.putExtra("FOTOPLATO",fotoPlato)
        // Pasa la posición como un extra en el Intent
        intent.putExtra("IMAGE_POSITION", position)
        context.startActivity(intent)
    }

    private fun openMap4(context: Context,position:Int) {
        if (nivel == 1){
            lat = 38.9161
            long = -6.3438
            fotoPlato = "migas_merida"
            nombrePlato = "Migas"
            ciudadPlato = "Merida"
            detallesPlato = "Las migas de Mérida son una deliciosa especialidad culinaria típica de la región de Extremadura, España. " +
                    "Para prepararlas, se utiliza pan duro desmenuzado, que se cocina con ajo en aceite de oliva hasta que adquiere una textura crujiente. Se le añade pimentón para dar color y sabor, y se sazona con sal al gusto.\n" +
                    "\n" +
                    "Estas migas se sirven calientes y se pueden acompañar con una variedad de ingredientes, como uvas para un toque dulce, chorizo, morcilla o panceta para un sabor más robusto, y pimientos asados para agregar frescura."
        }else{
            lat = 42.6954
            long = -1.6761
            fotoPlato = "pimientos_navarra"
            nombrePlato = "Pimientos de Piquillo"
            ciudadPlato = "Navarra"
            detallesPlato = "Los pimientos de Piquillo de Navarra son una variedad de pimientos originaria de la región de Navarra, en el norte de España. Estos pimientos se distinguen por su forma triangular y tamaño relativamente pequeño. " +
                    "Tienen una textura carnosa y un sabor dulce característico." +
                    "Estos pimientos son conocidos por su versatilidad y son muy apreciados en la gastronomía española. Se pueden utilizar de diversas maneras en la cocina, " +
                    "ya sea rellenos de carne, mariscos o queso, o simplemente asados y servidos como acompañamiento. "
        }
        val intent = Intent(context, MapsGame::class.java)

        intent.putExtra("LATITUD",lat)
        intent.putExtra("LONGITUD",long)
        intent.putExtra("NIVEL",nivel)
        intent.putExtra("CIUDAD",ciudadPlato)
        intent.putExtra("NOMBREPLATO",nombrePlato)
        intent.putExtra("DETALLES",detallesPlato)
        intent.putExtra("FOTOPLATO",fotoPlato)
        // Pasa la posición como un extra en el Intent
        intent.putExtra("IMAGE_POSITION", position)
        context.startActivity(intent)
    }
    private fun openMap5(context: Context,position:Int) {
        if (nivel == 1){
            lat = 40.4168
            long = -3.7038
            fotoPlato = "cocido_madrid"
            nombrePlato = "Cocido"
            ciudadPlato = "Madrid"
            detallesPlato = "El cocido es un plato tradicional que se prepara de diversas maneras en diferentes regiones del mundo, siendo especialmente destacado en la cocina española y latinoamericana. " +
                    "Este plato se caracteriza por su versatilidad y la combinación de ingredientes variados.El cocido se cocina generalmente en varias etapas. Primero, se cocinan las legumbres con las carnes y verduras en un caldo sabroso. " +
                    "Luego, se sirve por separado, presentando las legumbres y las carnes en platos distintos."
        }else{
            lat = 42.2871
            long = -2.5396
            fotoPlato = "menestra_la_rioja"
            nombrePlato = "Menestra"
            ciudadPlato = "La Rioja"
            detallesPlato = "La Menestra de La Rioja es un plato emblemático de la región española de La Rioja, reconocido por la frescura y la diversidad de sus ingredientes. Este guiso resalta la combinación armoniosa de verduras de temporada, que pueden incluir espárragos, guisantes, alcachofas, judías verdes, zanahorias y habas. " +
                    "La elección de verduras puede variar según la temporada y la disponibilidad en la región. La Menestra de La Rioja destaca por ser un plato que celebra los sabores auténticos de las verduras de la región, ofreciendo una experiencia culinaria que refleja la riqueza gastronómica de La Rioja."
        }
        val intent = Intent(context, MapsGame::class.java)

        intent.putExtra("LATITUD",lat)
        intent.putExtra("LONGITUD",long)
        intent.putExtra("NIVEL",nivel)
        intent.putExtra("CIUDAD",ciudadPlato)
        intent.putExtra("NOMBREPLATO",nombrePlato)
        intent.putExtra("DETALLES",detallesPlato)
        intent.putExtra("FOTOPLATO",fotoPlato)
        // Pasa la posición como un extra en el Intent
        intent.putExtra("IMAGE_POSITION", position)
        context.startActivity(intent)
    }

}


