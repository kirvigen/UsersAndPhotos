package com.kirvigen.usersphoto.Data

import android.util.Log
import com.kirvigen.usersphoto.Objects.Album
import com.kirvigen.usersphoto.Objects.Photo
import com.kirvigen.usersphoto.Objects.User
import org.json.JSONArray
import java.net.URL
import javax.security.auth.callback.Callback

class Repository {
    val serverUrl = "https://jsonplaceholder.typicode.com/"

    interface Result<T>{
        fun onResponse(result:T)
        fun onError(error:Exception)
    }

    fun loadUsers(callback: Result<MutableList<User>>){
        loadAlbums(object :Result<MutableList<Album>>{
            override fun onResponse(dataAlbum: MutableList<Album>) {
                sendRequest("${serverUrl}users",object : Result<String>{
                    override fun onResponse(result: String) {
                        try{
                            val array = JSONArray(result)
                            val data = mutableListOf<User>()
                            for(i in 0 until array.length()){
                                val item = array.getJSONObject(i)
                                val id =  item.getInt("id")
                                val albums = dataAlbum.filter { it.userId == id }.toMutableList()
                                data.add(User(item.getString("name"),id,albums))
                            }
                            callback.onResponse(data)
                        }catch (e:Exception){
                            callback.onError(e)
                        }
                    }
                    override fun onError(error: Exception) {callback.onError(error)}
                })
            }
            override fun onError(error: Exception) { callback.onError(error) }
        })
    }

    private fun loadAlbums(callback: Result<MutableList<Album>>){
        loadPhoto(object : Result<MutableList<Photo>>{
            override fun onResponse(dataPhoto: MutableList<Photo>) {
                sendRequest("${serverUrl}albums",object : Result<String>{
                    override fun onResponse(result: String) {
                        try{
                            val array = JSONArray(result)
                            val data = mutableListOf<Album>()
                            for(i in 0 until array.length()){
                                val item = array.getJSONObject(i)
                                val id =  item.getInt("id")
                                val photos = dataPhoto.filter { it.albumId == id }.toMutableList()
                                data.add(Album(item.getInt("userId"), id, photos))
                            }
                            callback.onResponse(data)
                        }catch (e:Exception){
                            callback.onError(e)
                        }
                    }
                    override fun onError(error: Exception) {callback.onError(error)}
                })
            }
            override fun onError(error: Exception) {callback.onError(error)}
        })
    }

    private fun loadPhoto(callback: Result<MutableList<Photo>>){
        sendRequest("${serverUrl}photos",object : Result<String>{
            override fun onResponse(result: String) {
                try{
                    val array = JSONArray(result)
                    val data = mutableListOf<Photo>()
                    for(i in 0 until array.length()){
                        val item = array.getJSONObject(i)
                        data.add(Photo(item.getString("title"),
                            item.getInt("albumId"),
                            item.getString("url")))
                    }
                    callback.onResponse(data)
                }catch (e:Exception){
                    callback.onError(e)
                }
            }
            override fun onError(error: Exception) {callback.onError(error)}
        })
    }

    private fun sendRequest(url:String,callback: Result<String>){
        Thread{
            try {
                callback.onResponse(URL(url).readText())
            }catch (e:Exception){
                callback.onError(e)
            }
        }.start()
    }
}