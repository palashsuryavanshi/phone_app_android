package com.phone.app.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.phone.app.core.database.dao.CallRecordDao
import com.phone.app.core.database.dao.ContactDao
import com.phone.app.core.database.entity.CallRecordEntity
import com.phone.app.core.database.entity.ContactEntity

@Database(
    entities = [
        ContactEntity::class,
        CallRecordEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PhoneDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
    abstract fun callRecordDao(): CallRecordDao

    companion object {
        @Volatile
        private var INSTANCE: PhoneDatabase? = null

        fun getInstance(context: Context): PhoneDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PhoneDatabase::class.java,
                    "phone_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}