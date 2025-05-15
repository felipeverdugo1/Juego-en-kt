package com.felipe.aprendamosalimpiar.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class Patient(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val therapistId: Long,  // Relación con terapeuta
    val name: String,
    val age: Int,
    val observations: String
)
{
    override fun toString(): String {
        return name.uppercase()
    }
}