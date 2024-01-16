package com.example.geoguessr

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import java.util.*

class ViewPagerAdapter(val context: Context, val imageList: List<Int>) : PagerAdapter() {

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

        imageView.setImageResource(imageList[position])

        // Añade un OnClickListener al ImageView
        imageView.setOnClickListener {
            // Realiza alguna acción según la posición, por ejemplo, abre otra actividad
            when (position) {
                0 -> openMap1(context)
                1 -> openMap2(context)
                2 -> openMap3(context)
                3 -> openMap4(context)
                4 -> openMap5(context)
            }
        }

        Objects.requireNonNull(container).addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

    private fun openMap1(context: Context) {
        val intent = Intent(context, MapsGame::class.java)
        context.startActivity(intent)
    }

    private fun openMap2(context: Context) {
        val intent = Intent(context, MapsGame::class.java)
        context.startActivity(intent)
    }
    private fun openMap3(context: Context) {
        val intent = Intent(context, MapsGame::class.java)
        context.startActivity(intent)
    }

    private fun openMap4(context: Context) {
        val intent = Intent(context, MapsGame::class.java)
        context.startActivity(intent)
    }
    private fun openMap5(context: Context) {
        val intent = Intent(context, MapsGame::class.java)
        context.startActivity(intent)
    }
}


