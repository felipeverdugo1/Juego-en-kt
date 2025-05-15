package com.felipe.aprendamosalimpiar.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.felipe.aprendamosalimpiar.data.models.Patient
import com.felipe.aprendamosalimpiar.data.models.Therapist


@Dao
interface PatientDao {
        @Insert
        suspend fun insert(patient: Patient)

        @Query("SELECT * FROM patients")
        suspend fun getAllPatients(): List<Patient>

        @Query("SELECT * FROM patients WHERE therapistId = :therapistId")
        suspend fun getPatientsByTherapist(therapistId: Long): List<Patient>

        @Update
        suspend fun update(patient: Patient)

        @Delete
        suspend fun delete(patient: Patient)


        @Query("SELECT * FROM patients WHERE id = :id LIMIT 1")
        suspend fun getById(id: Long): Patient

        @Query("DELETE FROM patients WHERE id = :id")
        suspend fun deleteById(id: Int)


}
