package com.felipe.aprendamosalimpiar.ui.activities.abm.patient

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.felipe.aprendamosalimpiar.R
import com.felipe.aprendamosalimpiar.data.AppDatabase
import com.felipe.aprendamosalimpiar.data.models.Patient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UpdatePatientActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase

    private lateinit var patient: Patient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_patient)
        var patientId = intent.getLongExtra("patient_id", -1)

        val nameField = findViewById<EditText>(R.id.editName)
        val ageField = findViewById<EditText>(R.id.editAge)
        val obsField = findViewById<EditText>(R.id.editObservations)
        val saveBtn = findViewById<Button>(R.id.btnSavePatient)
        db = AppDatabase.getDatabase(this)


        GlobalScope.launch {
            patient = db.patientDao().getById(patientId)

            runOnUiThread {
                nameField.setText(patient.name)
                ageField.setText(patient.age.toString())
                obsField.setText(patient.observations)
            }
        }

        saveBtn.setOnClickListener {
            val newName = nameField.text.toString()
            val newAge = ageField.text.toString().toIntOrNull() ?: 0
            val newObs = obsField.text.toString()

            if (newName.isEmpty() || newAge <= 0) {
                Toast.makeText(this, "Nombre y edad son obligatorios", Toast.LENGTH_SHORT).show()
            } else {
                GlobalScope.launch {
                    val updatedPatient = patient.copy(
                        name = newName,
                        age = newAge,
                        observations = newObs
                    )
                    db.patientDao().update(updatedPatient)

                    runOnUiThread {
                        Toast.makeText(this@UpdatePatientActivity, "Paciente actualizado", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }



    }
}