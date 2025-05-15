package com.felipe.aprendamosalimpiar.data


import Game
import GameDao
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.felipe.aprendamosalimpiar.data.dao.PatientDao
import com.felipe.aprendamosalimpiar.data.dao.TherapistDao
import com.felipe.aprendamosalimpiar.data.models.Patient
import com.felipe.aprendamosalimpiar.data.models.Therapist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Therapist::class, Patient::class, Game::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun therapistDao(): TherapistDao
    abstract fun patientDao(): PatientDao
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aprendamos_a_limpiar_db"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    populateInitialData(database)
                                }
                            }
                        }
                    }).addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration(dropAllTables = true) // Solo para desarrollo
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun populateInitialData(database: AppDatabase) {
            // Insertar datos iniciales
            val therapistDao = database.therapistDao()
            val patientDao = database.patientDao()
            val gameDao = database.gameDao()

            // Insertar terapeuta de ejemplo
            val therapistId = therapistDao.insert(Therapist(name = "Dr. Smith"))

            // Insertar pacientes de ejemplo
            val patient1Id = patientDao.insert(
                Patient(
                    therapistId = 1,
                    name = "Juan Pérez",
                    age = 8,
                    observations = "Paciente con TEA nivel 1"
                )
            )

            val patient2Id = patientDao.insert(
                Patient(
                    therapistId = 1,
                    name = "María González",
                    age = 10,
                    observations = "Paciente con TEA nivel 2"
                )
            )

            // Insertar juegos de ejemplo
            gameDao.insertAll(
                listOf(
                    Game(
                        patientId = 1,
                        date = "2025-05-10",
                        difficulty = "FACIL",
                        lastLevel = "Nivel 2"
                    ),
                    Game(
                        patientId = 1,
                        date = "2025-05-12",
                        difficulty = "DIFICIL",
                        lastLevel = "Nivel 3"
                    ),
                    Game(
                        patientId = 2,
                        date = "2025-05-11",
                        difficulty = "DIFICIL",
                        lastLevel = "Nivel 4"
                    )
                )
            )
        }
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Crear la nueva tabla Game
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `games` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT,
                `patientId` INTEGER NOT NULL,
                `date` TEXT NOT NULL,
                `difficulty` TEXT NOT NULL,
                `lastLevel` TEXT NOT NULL,
                FOREIGN KEY(`patientId`) REFERENCES `patients`(`id`) ON DELETE CASCADE
            )
        """)
    }
}