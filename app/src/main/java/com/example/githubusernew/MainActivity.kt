package com.example.githubusernew

import android.content.ContentValues
import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusernew.activity.DetailActivity
import com.example.githubusernew.adapter.UserAdapter
import com.example.githubusernew.config.DatabaseContract
import com.example.githubusernew.config.DatabaseContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.example.githubusernew.model.Favorite
import com.example.githubusernew.model.User
import com.example.githubusernew.util.helper.MappingHelper
import com.example.githubusernew.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.useritems.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: UserAdapter
    private lateinit var userViewModel: SearchViewModel
    private lateinit var userItems: ArrayList<User>
    private lateinit var uriWithId: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()
        showRecyclerListViewUser()
    }

    private fun showRecyclerListViewUser(){
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        rvView.layoutManager = LinearLayoutManager(this)
        rvView.adapter = adapter

        //Membuat thread baru untuk melihat perubahan (observe) supaya tidak mengganggu kinerja thread utama.
        val handlerThread = HandlerThread("DataObserve")
        handlerThread.start()
        val handler =android.os.Handler(handlerThread.looper)
        //fungsi observer
        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadUserFavorites(userItems)
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{

            override fun onItemClicked(data: User) {
                selectedUser(data)
            }

            //btn favorite click
            override fun onBtnFavoriteClicked(view: View, data: User) {
                var iconFavorite = R.drawable.ic_favorite
                if(data.favorite == 0){
                    //data dimasukkan ke db
                    insertToDb(view, data.id, data.login, data.avatarUrl, data.type)
                    Toast.makeText(this@MainActivity, "${data.login} ${getString(R.string.saveToFavorite)}", Toast.LENGTH_SHORT).show()
                    data.favorite = 1

                }else{
                    //data dihapus dr db
                    deleteUserById(view, data.id.toString())
                    Toast.makeText(this@MainActivity, "${data.login} ${getString(R.string.removeUser)}", Toast.LENGTH_SHORT).show()
                    data.favorite = 0
                    iconFavorite = R.drawable.ic_favorite_border
                }
                view.btnFavorite.setImageResource(iconFavorite)
            }
        })
    }

    private fun setupViewModel(){
        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(SearchViewModel::class.java)

        btn_search_user.setOnClickListener {
            val username = edit_text_user.text.toString()
            if (username.isEmpty()) return@setOnClickListener

            showLoading(true)
            userViewModel.setUsers(username)
        }
        userViewModel.getUsers().observe(this, Observer { userItems ->
            if (userItems != null){
                this.userItems = userItems
                loadUserFavorites(userItems)
                showLoading(false)
            }
        })
    }

    private var userFavoriteGlobal = ArrayList<Favorite>()
    private fun loadUserFavorites(userItems: ArrayList<User>) {
        GlobalScope.launch (Dispatchers.Main){
            val deferredItemsFavorite = async(Dispatchers.IO) {

                val cursor = this@MainActivity.contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val userFavorites = deferredItemsFavorite.await()
            adapter.setData(userItems, userFavorites)
            userFavoriteGlobal = userFavorites
        }
    }

    //db insert data user favorite
    private fun insertToDb(
        view: View,
        id: Int,
        login: String?,
        avatarUrl: String?,
        type: String?
    ){
        val values = ContentValues()
        values.put(DatabaseContract.UserFavoriteColumns.ID, id)
        values.put(DatabaseContract.UserFavoriteColumns.LOGIN, login)
        values.put(DatabaseContract.UserFavoriteColumns.AVATARA_URL, avatarUrl)
        values.put(DatabaseContract.UserFavoriteColumns.TYPE, type)
        values.put(DatabaseContract.UserFavoriteColumns.FAVORITE, 1)

        view.context.contentResolver.insert(CONTENT_URI, values)
    }

    private fun deleteUserById (view: View, id: String){
        uriWithId = Uri.parse("$CONTENT_URI/$id")

        view.context.contentResolver.delete(uriWithId, null, null)

    }

    //DetailActivity
    private fun selectedUser(userItems: User){
        val username = userItems.login

        val intentToDetailActivity = Intent(this@MainActivity, DetailActivity::class.java)
        intentToDetailActivity.putExtra(DetailActivity.EXTRA_USERNAME, username)
        startActivity(intentToDetailActivity)
    }

    //progress indicator logic
    private fun showLoading(state: Boolean){
        if (state){
            progress_bar.visibility = View.VISIBLE
        }else{
            progress_bar.visibility = View.GONE
        }
    }

}