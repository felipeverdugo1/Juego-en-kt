package com.felipe.aprendamosalimpiar.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Locale


@Entity(tableName = "therapists")
data class Therapist(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var name: String) {
    override fun toString(): String {
        return name.uppercase(Locale.getDefault()) // Ahora el adapter mostrará directamente el nombre
    }
}