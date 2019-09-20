package com.app.rachmad.movie.sqlite

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.app.rachmad.movie.BuildConfig
import com.app.rachmad.movie.`object`.FilmWidgetData
import com.app.rachmad.movie.`object`.MovieData
import com.app.rachmad.movie.`object`.TvData
import com.app.rachmad.movie.sqlite.query.FavoriteQuery

@Database(entities = [MovieData::class, TvData::class, FilmWidgetData::class], version = 2, exportSchema = false)
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
                        .addMigrations(MIGRATION_1_2)
                        .build()
            }
            return instance
        }

        var MIGRATION_1_2: Migration = object: Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `widget` (`id` INTEGER NOT NULL DEFAULT 0, `name` TEXT NOT NULL DEFAULT \"\", `image` TEXT, PRIMARY KEY(`id`))")
            }
        }
    }
}