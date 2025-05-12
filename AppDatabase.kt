package com.example.researchmanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.researchmanager.data.dao.*
import com.example.researchmanager.data.model.*

@Database(
    entities = [
        User::class,
        Field::class,
        Document::class,
        Note::class,
        SharedDocument::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun fieldDao(): FieldDao
    abstract fun documentDao(): DocumentDao
    abstract fun noteDao(): NoteDao
    abstract fun sharedDocumentDao(): SharedDocumentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "research_manager_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
