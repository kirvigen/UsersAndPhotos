package com.kirvigen.usersphoto.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.util.LruCache
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import com.kirvigen.usersphoto.R
import java.io.IOException
import java.net.URL


class PhotoLoader {

    private lateinit var url:String
    private var callback:LoaderCallback? = null
    private var fade:Boolean = false

    interface LoaderCallback{
        fun onSuccess(bitmap: Bitmap,url:String){}
        fun onError(e: Exception?){}
    }

    fun load(url: String):PhotoLoader{
        this.url = url
        return this
    }

    fun initCallback(callback: LoaderCallback):PhotoLoader{
        this.callback = callback
        return this
    }

    fun fade(fade:Boolean):PhotoLoader{
        this.fade = fade
        return this
    }

    fun setImageBitmap(imageView: ImageView,bitmap: Bitmap){
        imageView.setImageBitmap(bitmap)
        if(fade) {
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.interpolator = DecelerateInterpolator()
            fadeIn.duration = 200

            val animation = AnimationSet(false)
            animation.addAnimation(fadeIn)

            imageView.animation = animation
        }
    }

    @SuppressLint("ResourceType")
    fun into(imageView: ImageView, cache: LruCache<String, Bitmap>):AsyncTask<String,Void,Bitmap>?{
        imageView.setImageResource(R.color.White)
        cache.get(url)?.also {
            setImageBitmap(imageView,it)
            callback?.onSuccess(it,url)
        }?:run{
             return doAsync{bitmap,u->
                bitmap?.let {
                    setImageBitmap(imageView,it)
                    callback?.onSuccess(it,url)
                    cache.put(url, it)
                }
                if(bitmap == null)
                    callback?.onError(null)
            }.execute(url)
        }
        return null
    }
    class doAsync(val post: (Bitmap?,String) -> Unit) : AsyncTask<String, Void, Bitmap>() {
        lateinit var u: String
        override fun doInBackground(vararg urls: String): Bitmap? {
            return try{
                u = urls[0]
                val url = URL(u)
                val urlConnection = url.openConnection()
                urlConnection.addRequestProperty("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36 OPR/71.0.3770.287")
                 return  BitmapFactory.decodeStream(urlConnection.getInputStream())
            } catch (e: IOException) {
                null
            }
        }
        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            post(result,u)
        }
    }
}