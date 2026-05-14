package com.example.mahilashaktiunnati.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mahilashaktiunnati.data.dao.AppDao
import com.example.mahilashaktiunnati.data.entity.Member
import com.example.mahilashaktiunnati.data.entity.Savings
import com.example.mahilashaktiunnati.data.entity.Loan
import com.example.mahilashaktiunnati.data.entity.Repayment

@Database(
    entities = [Member::class, Savings::class, Loan::class, Repayment::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mahila_shakti_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}