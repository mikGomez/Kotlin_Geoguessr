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
        }else{
            lat = 40.9429
            long = -4.1088
        }
        val intent = Intent(context, MapsGame::class.java)

        intent.putExtra("LATITUD",lat)
        intent.putExtra("LONGITUD",long)
        intent.putExtra("NIVEL",nivel)
        // Pasa la posición como un extra en el Intent
        intent.putExtra("IMAGE_POSITION", position)


        context.startActivity(intent)
    }

    private fun openMap2(context: Context,position:Int) {
        if (nivel == 1){
            lat = 39.4699
            long = -0.3763
        }else{
            lat = 37.9922
            long = -1.1307
        }

        val intent = Intent(context, MapsGame::class.java)
        intent.putExtra("LATITUD",lat)
        intent.putExtra("LONGITUD",long)
        intent.putExtra("NIVEL",nivel)
        // Pasa la posición como un extra en el Intent
        intent.putExtra("IMAGE_POSITION", position)
        context.startActivity(intent)
    }
    private fun openMap3(context: Context,position:Int) {
        if (nivel == 1){
            lat = 43.3614
            long = -5.8593
        }else{
            lat = 41.6488
            long = -0.8891
        }
        val intent = Intent(context, MapsGame::class.java)

        intent.putExtra("LATITUD",lat)
        intent.putExtra("LONGITUD",long)
        intent.putExtra("NIVEL",nivel)
        // Pasa la posición como un extra en el Intent
        intent.putExtra("IMAGE_POSITION", position)
        context.startActivity(intent)
    }

    private fun openMap4(context: Context,position:Int) {
        if (nivel == 1){
            lat = 38.9161
            long = -6.3438
        }else{
            lat = 42.6954
            long = -1.6761
        }
        val intent = Intent(context, MapsGame::class.java)

        intent.putExtra("LATITUD",lat)
        intent.putExtra("LONGITUD",long)
        intent.putExtra("NIVEL",nivel)
        // Pasa la posición como un extra en el Intent
        intent.putExtra("IMAGE_POSITION", position)
        context.startActivity(intent)
    }
    private fun openMap5(context: Context,position:Int) {
        if (nivel == 1){
            lat = 40.4168
            long = -3.7038
        }else{
            lat = 42.2871
            long = -2.5396
        }
        val intent = Intent(context, MapsGame::class.java)

        intent.putExtra("LATITUD",lat)
        intent.putExtra("LONGITUD",long)
        intent.putExtra("NIVEL",nivel)
        // Pasa la posición como un extra en el Intent
        intent.putExtra("IMAGE_POSITION", position)
        context.startActivity(intent)
    }

}


