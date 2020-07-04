package com.example.githubusernew.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubusernew.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class SearchViewModel: ViewModel() {
    val listUsers = MutableLiveData<ArrayList<User>>()

    fun setUsers(username: String){
        val listItems = ArrayList<User>()
        val apiUrl = "https://api.github.com/search/users?q=$username"
        val tokenValue = "0e9f2530036a4d1b335ae12dfa925651fb71e031"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $tokenValue")
        client.addHeader("User-Agent", "request")
        client.get(apiUrl, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for(i in 0 until items.length()){
                        val user = items.getJSONObject(i)
                        val userItems = User()
                        userItems.id = user.getInt("id")
                        userItems.login = user.getString("login")
                        userItems.avatarUrl = user.getString("avatar_url")
                        userItems.type = user.getString("type")
                        listItems.add(userItems)
                    }
                    listUsers.postValue(listItems)
                }catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("onFailure", error?.message.toString())
            }

        })
    }

    fun getUsers(): LiveData<ArrayList<User>>{
        return listUsers
    }
}