package com.example.githubusernew.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.githubusernew.config.DatabaseContract.AUTHORITY
import com.example.githubusernew.config.DatabaseContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.example.githubusernew.config.DatabaseContract.UserFavoriteColumns.Companion.TABLE_NAME
import com.example.githubusernew.config.UserFavHelper

/*
    * Author : Herry Prasetyo
    * years : 2020
 */
class UserFavProvider :ContentProvider() {
    companion object{

        private const val USER_FAVORITE = 1
        private const val USER_FAVORITE_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var userFavoriteHelper: UserFavHelper
    }


    init {
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER_FAVORITE)

        sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", USER_FAVORITE_ID)
    }

    override fun onCreate(): Boolean {
        userFavoriteHelper = UserFavHelper.getInstance(context as Context)
        userFavoriteHelper.open()
        return true
    }

    //mambaca semua data
    override fun query(uri: Uri, strings: Array<String>?, s: String?, string1: Array<String>?, s1: String?): Cursor? {
        return when(sUriMatcher.match(uri)){
            USER_FAVORITE -> userFavoriteHelper.queryAll()
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    //memasukkan data
    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (USER_FAVORITE){
            sUriMatcher.match(uri) -> userFavoriteHelper.insert(contentValues)
            else ->0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }


    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        val deleted: Int = when(USER_FAVORITE_ID){
            sUriMatcher.match(uri) -> userFavoriteHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }

}