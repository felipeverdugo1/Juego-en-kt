package com.felipe.aprendamosalimpiar.ui.activities.abm.patient

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.felipe.aprendamosalimpiar.R
import com.felipe.aprendamosalimpiar.data.AppDatabase
import com.felipe.aprendamosalimpiar.data.models.Patient
import kotlinx.coroutines.*

class CreatePatientActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etAge: EditText
    private lateinit var etObservations: EditText
    private lateinit var btnSave: Button
    private lateinit var db: AppDatabase
    private var therapistId: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_patient)

        etName = findViewById(R.id.etPatientName)
        etAge = findViewById(R.id.etPatientAge)
        etObservations = findViewById(R.id.etPatientObservations)
        btnSave = findViewById(R.id.btnSavePatient)
        db = AppDatabase.getDatabase(this)
        therapistId = intent.getLongExtra("therapist_id", 0)

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val age = etAge.text.toString().toIntOrNull() ?: 0
            val observations = etObservations.text.toString().trim()

            if (name.isEmpty() || age <= 0) {
                Toast.makeText(this, "Nombre y edad son obligatorios", Toast.LENGTH_SHORT).show()
            } else {
                GlobalScope.launch {
                    db.patientDao().insert(
                        Patient(
                            therapistId = therapistId,
                            name = name,
                            age = age,
                            observations = observations
                        )
                    )
                    runOnUiThread {
                        Toast.makeText(this@CreatePatientActivity, "Paciente guardado", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }
}