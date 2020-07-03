package com.example.githubusernew.config

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.githubusernew.config.DatabaseContract.UserFavoriteColumns.Companion.ID
import com.example.githubusernew.config.DatabaseContract.UserFavoriteColumns.Companion.TABLE_NAME
import java.sql.SQLException

class UserFavHelper(context: Context) {
    companion object{
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper : DatabaseHelper
        private val INSTANCE: UserFavHelper? = null
            fun getInstance(context: Context) : UserFavHelper = INSTANCE ?: synchronized(this) {
                TODO()
                INSTANCE ?: UserFavHelper(context)
            }
        private lateinit var database: SQLiteDatabase
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun  open(){
        database = databaseHelper.writableDatabase
    }

    //get Data
    fun QueryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$ID ASC"
        )
    }

    fun insert(values: ContentValues?):Long{
        return database.insert(DATABASE_TABLE,null, values)
    }

    fun deleteById(id: String): Int{
        return database.delete(DATABASE_TABLE, "$ID = '$id'", null)
    }
}



