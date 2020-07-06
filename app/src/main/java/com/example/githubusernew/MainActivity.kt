package com.example.githubusernew

import android.content.ContentValues
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

        val handlerThread = HandlerThread("DataObserve")
        handlerThread.start()
        val handler =android.os.Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadUserFavorites(userItems)
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
//                selectedUser(data)
            }

            //btn favorite click
            override fun onBtnFavoriteClicked(view: View, data: User) {
                var iconFavorite = R.drawable.like_color
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
                    iconFavorite = R.drawable.like_white
                }
                view.btnFavorite.setImageResource(iconFavorite)
            }
        })
    }

    private fun setupViewModel(){
        //menghubungkan class UsersViewModel dengan MainActivity / View
        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(SearchViewModel::class.java)

        //mencari user berdasarkan inputan username pada custom_search_view
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
        //menggunakan coroutine
        GlobalScope.launch (Dispatchers.Main){
            val deferredItemsFavorite = async(Dispatchers.IO) {

                val cursor = this@MainActivity.contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val userFavorites = deferredItemsFavorite.await() //data user favorite dr db

            adapter.setData(userItems, userFavorites) //Set data dari APi dan db ke adapter untuk di olah.
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

        //insert with contentResolver (Provider)
        view.context.contentResolver.insert(CONTENT_URI, values)
    }

    //delete data in db by id
    private fun deleteUserById (view: View, id: String){
        //mwnghapus dengan id
        uriWithId = Uri.parse("$CONTENT_URI/$id")

        //delete with contentResolver (Provider)
        view.context.contentResolver.delete(uriWithId, null, null)

    }

    //intent ke DetailActivity
//    private fun selectedUser(userItems: User){
//        val username = userItems.login
//
//        val intentToDetailActivity = Intent(this@MainActivity, DetailActivity::class.java)
//        intentToDetailActivity.putExtra(DetailActivity)
//        startActivity(intentToDetailActivity)
//    }

    //progress indicator logic
    private fun showLoading(state: Boolean){
        if (state){
            progress_bar.visibility = View.VISIBLE
        }else{
            progress_bar.visibility = View.GONE
        }
    }


//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.main_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId){
//            R.id.menu_favorite ->{
//                val intent = Intent(this, FavoriteActivity::class.java)
//                startActivity(intent)
//                return true
//            }
//            R.id.action_change_setting ->{
//                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
//                startActivity(intent)
//            }
//            R.id.action_reminder_setting ->{
//                val intent = Intent(this, SettingActivity::class.java)
//                startActivity(intent)
//            }
//            else -> return  true
//        }
//        return super.onOptionsItemSelected(item)
//    }
}