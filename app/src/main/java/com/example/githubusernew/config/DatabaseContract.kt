package com.example.githubusernew.config

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY  = "com.example.githubusernew"
    const val SCHEME = "content"

    internal class UserFavoriteColumns: BaseColumns{
        companion object{
            const val TABLE_NAME = "favoriteuser"
            const val ID = "_id"
            const val LOGIN = "login"
            const val AVATARA_URL = "avatar_url"
            const val TYPE = "type"
            const val FAVORITE = "favorite"

            val CONTENT_URI : Uri = Uri.Builder().scheme(SCHEME)
                .authority(com.example.githubusernew.config.DatabaseContract.AUTHORITY)
                .appendPath(com.example.githubusernew.config.DatabaseContract.UserFavoriteColumns.Companion.TABLE_NAME)
                .build()
        }
    }
}