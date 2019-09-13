package com.app.rachmad.movie.sqlite

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.rachmad.movie.BuildConfig
import com.app.rachmad.movie.sqlite.query.FavoriteQuery
import com.app.rachmad.movie.sqlite.table.FavoriteTable

@Database(entities = arrayOf(FavoriteTable::class), version = 1, exportSchema = false)
abstract class CinemaDatabase: RoomDatabase(){
    abstract fun favoriteDatabase(): FavoriteQuery
    companion object{
        var instance: CinemaDatabase? = null
        fun getInstance(c: Context): CinemaDatabase?{
            if(instance == null) {
                instance = Room.databaseBuilder(c.applicationContext,
                        CinemaDatabase::class.java,
                        BuildConfig.DB_NAME)
                        .allowMainThreadQueries()
                        .build()
            }
            return instance
        }

        fun destroyInstance(){
            instance = null
        }
    }
}