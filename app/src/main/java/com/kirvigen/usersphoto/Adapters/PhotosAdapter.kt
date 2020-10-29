package com.kirvigen.usersphoto.Adapters

import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.DisplayMetrics
import android.util.Log
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kirvigen.usersphoto.Objects.Photo
import com.kirvigen.usersphoto.R
import com.kirvigen.usersphoto.Utils.PhotoLoader
import com.kirvigen.usersphoto.Utils.Utils


class PhotosAdapter(val photos: MutableList<Photo>): RecyclerView.Adapter<PhotosAdapter.Holder>() {
    private var memoryCache: LruCache<String, Bitmap>

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory/3
        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / 1024
            }
        }
    }

    override fun onFailedToRecycleView(holder: Holder): Boolean {
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val holder = Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_photo, parent,
                false
            )
        )

        val metrics: DisplayMetrics = parent.context.resources.displayMetrics
        val width = metrics.widthPixels
        val image =  holder.itemView.findViewById<View>(R.id.image)
        val layoutParams = image.layoutParams
        layoutParams.height = ((width - Utils.DpToPx(10,parent.context)*2).toInt())
        image.layoutParams = layoutParams

        return holder
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val photo = photos[position]
        holder.bind(photo)
        holder.loadImage(photo,memoryCache)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onViewRecycled(holder: Holder) {
        super.onViewRecycled(holder)
        holder.downloadTask?.cancel(true)
    }

    class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val title:TextView = itemView.findViewById(R.id.title)
        private val image:ImageView = itemView.findViewById(R.id.image)
        private val progressBar:ProgressBar = itemView.findViewById(R.id.progress)

        var downloadTask:AsyncTask<String,Void,Bitmap>? = null

        fun bind(photo: Photo){
            image.setImageResource(R.color.White)
            progressBar.visibility = View.VISIBLE
            title.text = photo.title
        }

        fun loadImage(photo:Photo,memoryCache:LruCache<String,Bitmap>){
            downloadTask = PhotoLoader()
                .load(photo.url)
                .fade(true)
                .initCallback(object : PhotoLoader.LoaderCallback{
                    override fun onSuccess(bitmap: Bitmap, url: String) {
                         progressBar.visibility = View.GONE
                    }
                })
                .into(image,memoryCache)
        }
    }
}