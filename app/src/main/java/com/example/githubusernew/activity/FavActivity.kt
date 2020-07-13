package com.example.githubusernew.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusernew.R
import com.example.githubusernew.adapter.FavAdapter
import com.example.githubusernew.config.DatabaseContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.example.githubusernew.model.Favorite
import com.example.githubusernew.util.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.favorite_items.view.*
import kotlinx.coroutines.*

class FavActivity : AppCompatActivity() {
    private lateinit var adapter: FavAdapter
    private lateinit var uriWithId : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        supportActionBar?.title = "UserFavorite"
        showRecyclerListViewFavoriteUser()
        loadUserFavoriteAsync()
    }

    private fun loadUserFavoriteAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progress_bar.visibility = View.VISIBLE
            val deferredUserFavorites = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progress_bar.visibility = View.INVISIBLE
            val userFavorites = deferredUserFavorites.await()
            if(userFavorites.size > 0){
                adapter.listFavorites = userFavorites
            } else{
                adapter.listFavorites = ArrayList()
                delay(300)
                Toast.makeText(this@FavActivity, getString(R.string.no_data), Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun showRecyclerListViewFavoriteUser() {
        adapter = FavAdapter()

        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)
        rv_favorite.adapter = adapter

        adapter.setOnItemClickCallback(object : FavAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Favorite) {
                selectedUser(data)
            }

            override fun btnFavoriteClicked(view: View, data: Favorite) {
                if(data.favorite == 1){
                    deleteUserById(view, data.id.toString())
                    Toast.makeText(this@FavActivity, "${data.login} ${getString(R.string.removeUser)}", Toast.LENGTH_SHORT).show()
                    data.favorite = 0
                    val iconFavorite = R.drawable.ic_favorite_border
                    view.btn_favorite.setImageResource(iconFavorite)
                }
            }
        })
    }

    private fun deleteUserById(view: View, id: String) {
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + id)
        view.context.contentResolver.delete(uriWithId, null, null)
    }

    private fun selectedUser(userFav: Favorite) {
        val username = userFav.login

        val intentToDetail = Intent(this@FavActivity, DetailActivity::class.java)
        intentToDetail.putExtra(DetailActivity.EXTRA_USERNAME, username)
        startActivity(intentToDetail)

    }

    override fun onRestart() {
        showRecyclerListViewFavoriteUser()
        loadUserFavoriteAsync()
        super.onRestart()
    }
}