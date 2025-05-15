package com.felipe.aprendamosalimpiar.data

import Game
import GameDao
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.felipe.aprendamosalimpiar.data.dao.PatientDao
import com.felipe.aprendamosalimpiar.data.dao.TherapistDao
import com.felipe.aprendamosalimpiar.data.models.Patient
import com.felipe.aprendamosalimpiar.data.models.Therapist
import androidx.room.RoomDatabase
import kotlin.jvm.Volatile

@Database(entities = [Therapist::class, Patient::class,Game::class], version = 2)
    abstract class AppDatabase : RoomDatabase() {
    abstract fun therapistDao(): TherapistDao
    abstract fun patientDao(): PatientDao
    abstract fun gameDao(): GameDao

//    companion object {
//        fun getDatabase(context: Context): AppDatabase {
//            return Room.databaseBuilder(
//                context,
//                AppDatabase::class.java,
//                "app_database"
//            ).build()
//        }
//    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                                context.applicationContext,
                                AppDatabase::class.java, // ✅ tipo explícito
                                "app_database"
                            ).fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }


    }
}

