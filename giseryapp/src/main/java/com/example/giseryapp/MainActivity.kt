package com.example.giseryapp

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.giseryapp.adapter.FavAdapter
import com.example.giseryapp.config.DatabaseContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.example.giseryapp.helper.MappingHelper
import com.example.giseryapp.model.Favorite
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.favorite_items.view.*
import kotlinx.android.synthetic.main.layout_flipper.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: FavAdapter
    private lateinit var uriWithId: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadUserFavoritesAsync()
        showRecyclerListViewUser()
    }

    private fun loadUserFavoritesAsync() {
        GlobalScope.launch(Dispatchers.Main){
            progress.visibility = View.VISIBLE
            val deferredUserFavorite = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null,null, null, null)
            MappingHelper.mapCursorToArrayList(
                    cursor
                )
            }
            progress.visibility = View.INVISIBLE
            val userFavorite = deferredUserFavorite.await()
            if(userFavorite.size > 0 ){
                adapter.listFavorites = userFavorite
            } else{
                adapter.listFavorites = ArrayList()
                delay(300)
                Toast.makeText(this@MainActivity, getString(R.string.no_users_found), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showRecyclerListViewUser(){
        adapter = FavAdapter()


        rvFavorite.layoutManager = LinearLayoutManager(this)
        rvFavorite.setHasFixedSize(true)
        rvFavorite.adapter = adapter

        adapter.setOnItemClickCallback(object : FavAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Favorite) {

            }

            override fun btnFavoriteClicked(view: View, data: Favorite) {
                if(data.favorite == 1){
                    deleteUserById(view,data.id.toString())
                    Toast.makeText(this@MainActivity, "${data.login} ${getString(R.string.deleteFavorite)}", Toast.LENGTH_SHORT).show()
                    data.favorite = 0
                    val iconFavorite = R.drawable.ic_baseline_favorite_24
                    view.btn_favorite.setImageResource(iconFavorite)
                }
            }

        })
    }

    private fun deleteUserById (view: View, id: String){
        uriWithId = Uri.parse("$CONTENT_URI/$id")

        view.context.contentResolver.delete(uriWithId, null, null)
    }

    override fun onRestart() {
        showRecyclerListViewUser()
        loadUserFavoritesAsync()
        super.onRestart()
    }
}