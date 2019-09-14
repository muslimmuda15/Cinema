package com.app.rachmad.movie.sqlite

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.rachmad.movie.BuildConfig
import com.app.rachmad.movie.`object`.MovieData
import com.app.rachmad.movie.`object`.TvData
import com.app.rachmad.movie.sqlite.query.FavoriteQuery

@Database(entities = [MovieData::class, TvData::class], version = 1, exportSchema = false)
abstract class CinemaDatabase: RoomDatabase(){
    abstract fun favoriteDatabase(): FavoriteQuery
    companion object{
        private var instance: CinemaDatabase? = null
        fun getInstance(c: Context): CinemaDatabase?{
            if(instance == null) {
                this.instance = Room.databaseBuilder(c.applicationContext,
                        CinemaDatabase::class.java,
                        BuildConfig.DB_NAME)
                        .allowMainThreadQueries()
                        .build()
            }
            return instance
        }
    }
}