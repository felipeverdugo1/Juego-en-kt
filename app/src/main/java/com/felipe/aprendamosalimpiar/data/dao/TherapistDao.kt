package com.felipe.aprendamosalimpiar.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.felipe.aprendamosalimpiar.data.models.Therapist

@Dao
interface TherapistDao {
    @Insert
    suspend fun insert(therapist: Therapist)

    @Query("SELECT * FROM therapists")
    suspend fun getAllTherapists(): List<Therapist>

    @Query("SELECT name FROM therapists")
    suspend fun getAllTherapistsName(): List<String>

    @Query("SELECT * FROM therapists WHERE id = :id LIMIT 1")
    suspend fun getTherapistById(id: Long): Therapist

    @Update
    suspend fun update(therapist: Therapist)

    @Delete
    suspend fun delete(therapist: Therapist)

    @Query("SELECT * FROM therapists WHERE name = :name LIMIT 1")
    suspend fun getTherapistByName(name: String): Therapist?

    @Query("DELETE FROM therapists WHERE id = :id")
    suspend fun deleteById(id: Int)



}