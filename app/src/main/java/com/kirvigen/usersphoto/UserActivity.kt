package com.kirvigen.usersphoto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewOutlineProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kirvigen.usersphoto.Adapters.UserAdapter
import com.kirvigen.usersphoto.Data.Repository
import com.kirvigen.usersphoto.Objects.Album
import com.kirvigen.usersphoto.Objects.User
import kotlinx.android.synthetic.main.activity_users.*

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        loadData()

    }

    private fun loadData(){
        Repository().loadUsers(object : Repository.Result<MutableList<User>>{
            override fun onResponse(users: MutableList<User>) {
                runOnUiThread {
                    progress.visibility = View.GONE
                    initRecycler(users)
                }
            }
            override fun onError(error: Exception) {
                runOnUiThread { showError() }
            }
        })
    }

    private fun initRecycler(users:MutableList<User>){
        val adapter = UserAdapter(users)
        adapter.setOnClickListener {toPhotosActivity(it)}
        recycler.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recycler.adapter = adapter
    }

    fun toPhotosActivity(user:User){
        val intent = Intent(this,PhotosActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("User",user)
        startActivity(intent)
    }

    private fun showError(){
        progress.visibility = View.GONE
        error_dialog.visibility = View.VISIBLE
        refresh.setOnClickListener {
            progress.visibility = View.VISIBLE
            error_dialog.visibility = View.GONE
            loadData()
        }
    }
}