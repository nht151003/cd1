package com.example.researchmanager.data

import android.content.Context
import com.example.researchmanager.data.dao.DocumentDao
import com.example.researchmanager.data.dao.FieldDao
import com.example.researchmanager.data.dao.NoteDao
import com.example.researchmanager.data.dao.SharedDocumentDao
import com.example.researchmanager.data.dao.UserDao

object DatabaseProvider {
    private var appDatabase: AppDatabase? = null

    fun init(context: Context) {
        if (appDatabase == null) {
            appDatabase = AppDatabase.getInstance(context)
        }
    }

    fun getUserDao(): UserDao {
        checkNotNull(appDatabase) { "AppDatabase not initialized. Call init(context) first." }
        return appDatabase!!.userDao()
    }

    fun getFieldDao(): FieldDao {
        checkNotNull(appDatabase)
        return appDatabase!!.fieldDao()
    }

    fun getDocumentDao(): DocumentDao {
        checkNotNull(appDatabase)
        return appDatabase!!.documentDao()
    }

    fun getNoteDao(): NoteDao {
        checkNotNull(appDatabase)
        return appDatabase!!.noteDao()
    }

    fun getSharedDocumentDao(): SharedDocumentDao {
        checkNotNull(appDatabase)
        return appDatabase!!.sharedDocumentDao()
    }
}
