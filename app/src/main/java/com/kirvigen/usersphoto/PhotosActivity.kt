package com.kirvigen.usersphoto

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.util.LruCache
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kirvigen.usersphoto.Adapters.PhotosAdapter
import com.kirvigen.usersphoto.Objects.Photo
import com.kirvigen.usersphoto.Objects.User
import kotlinx.android.synthetic.main.activity_users.*

class PhotosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val photos = mutableListOf<Photo>()
        intent?.getParcelableExtra<User>("User")?.albums?.forEach {
            it.photos.forEach {photo ->  photos.add(photo) }
        }
        initRecycler(photos)

    }

    private fun initRecycler(photos:MutableList<Photo>){
        recycler.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recycler.adapter = PhotosAdapter(photos)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}