package com.example.githubusernew.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubusernew.model.Follower
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowerViewModel : ViewModel() {
    val listFollower = MutableLiveData<ArrayList<Follower>>()

    fun setFollower(username: String?){
        val listItems = ArrayList<Follower>()
        val apiUrl = "https://api.github.com/users/$username/followers"
        val tokenValue = "0e9f2530036a4d1b335ae12dfa925651fb71e031"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $tokenValue")
        client.addHeader("User-Agent","request")
        client.get(apiUrl, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val  responseArray = JSONArray(result)
                    for (i in 0 until responseArray.length()){
                        val follower = responseArray.getJSONObject(i)
                        val followerItems = Follower()
                        followerItems.login = follower.getString("login")
                        followerItems.avatarUrl = follower.getString("avatar_url")
                        followerItems.type = follower.getString("type")

                        listItems.add(followerItems)
                    }
                    listFollower.postValue(listItems)
                }catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("Exception", error?.message.toString())
            }
        })
    }

    fun getFollower(): LiveData<ArrayList<Follower>>{
        return listFollower
    }
}