package com.felipe.aprendamosalimpiar.ui.activities.abm.therapist

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.felipe.aprendamosalimpiar.R
import com.felipe.aprendamosalimpiar.data.AppDatabase
import com.felipe.aprendamosalimpiar.data.models.Therapist
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CreateTherapistActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var btnSave: Button
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_therapist)

        etName = findViewById(R.id.etTherapistName)
        btnSave = findViewById(R.id.btnSaveTherapist)
        db = AppDatabase.getDatabase(this)

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Ingrese un nombre", Toast.LENGTH_SHORT).show()
            } else {
                GlobalScope.launch {
                    val existingTherapist = db.therapistDao().getTherapistByName(name)
                    if (existingTherapist != null) {
                        runOnUiThread {
                            Toast.makeText(
                                this@CreateTherapistActivity,
                                "Ya existe un terapeuta con ese nombre",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        db.therapistDao().insert(Therapist(name = name))
                        runOnUiThread {
                            Toast.makeText(
                                this@CreateTherapistActivity,
                                "Terapeuta guardado",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish() // Cierra la actividad y vuelve a la lista
                        }
                    }
                }
            }
        }
    }
}

