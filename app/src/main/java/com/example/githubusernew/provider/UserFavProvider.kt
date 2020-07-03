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
        private lateinit var userFavHelper: UserFavHelper
    }

    init {
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER_FAVORITE)

        sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", USER_FAVORITE_ID )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        s: String?,
        string1: Array<out String>?,
        s1: String?
    ): Cursor? {
        TODO("Not yet implemented")
        return when(sUriMatcher.match(uri)){
            USER_FAVORITE -> userFavHelper.QueryAll()
            else -> null
        }
    }

    override fun onCreate(): Boolean {
        TODO("Not yet implemented")
        userFavHelper = UserFavHelper.getInstance(context as Context)
        userFavHelper.open()
        return true
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
        val deleted : Int = when(USER_FAVORITE_ID){
            sUriMatcher.match(uri) -> userFavHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
        return null
    }

}