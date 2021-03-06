package com.example.giseryapp.config

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
            const val AVATAR_URL = "avatar_url"
            const val TYPE = "type"
            const val FAVORITE = "favorite"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}