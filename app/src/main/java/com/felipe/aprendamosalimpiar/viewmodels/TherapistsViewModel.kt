package com.felipe.aprendamosalimpiar.viewmodels

import androidx.lifecycle.ViewModel
import com.felipe.aprendamosalimpiar.data.AppDatabase
import com.felipe.aprendamosalimpiar.data.models.Therapist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TherapistViewModel(private val db: AppDatabase) : ViewModel() {

    suspend fun getAllTherapists(): List<Therapist> {
        return withContext(Dispatchers.IO) {
            db.therapistDao().getAllTherapists()
        }
    }

    suspend fun insertTherapist(name: String) {
        withContext(Dispatchers.IO) {
            db.therapistDao().insert(Therapist(name = name))
        }
    }
}