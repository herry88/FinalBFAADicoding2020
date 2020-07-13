package com.example.giseryapp.helper

import android.database.Cursor
import com.example.giseryapp.config.DatabaseContract
import com.example.giseryapp.model.Favorite

object MappingHelper {
    fun mapCursorToArrayList(userFavCursor: Cursor?) : ArrayList<Favorite>{
         val favoriteList = ArrayList<Favorite>()
         userFavCursor?.apply {
             while (moveToNext()){
                 val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserFavoriteColumns.ID))
                 val login = getString(getColumnIndexOrThrow(DatabaseContract.UserFavoriteColumns.LOGIN))
                 val avatarUrl = getString(getColumnIndexOrThrow(DatabaseContract.UserFavoriteColumns.AVATAR_URL))
                 val type = getString(getColumnIndexOrThrow(DatabaseContract.UserFavoriteColumns.TYPE))
                 val favorite = getInt(getColumnIndexOrThrow(DatabaseContract.UserFavoriteColumns.FAVORITE))

                 favoriteList.add(Favorite(id, login, avatarUrl, type, favorite))
             }
         }
        return favoriteList
    }
}