package com.felipe.aprendamosalimpiar.ui.activities.therapist

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.felipe.aprendamosalimpiar.R
import com.felipe.aprendamosalimpiar.data.AppDatabase
import com.felipe.aprendamosalimpiar.data.models.Patient
import com.felipe.aprendamosalimpiar.data.models.Therapist
import com.felipe.aprendamosalimpiar.ui.activities.abm.patient.CreatePatientActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TherapistMainActivity : AppCompatActivity() {

    private lateinit var listViewPatients: ListView
    private lateinit var btnAddPatient: Button
    private lateinit var btnBack: Button
    private lateinit var db: AppDatabase
    private lateinit var therapist: Therapist
    private lateinit var etTherapistName: EditText

    private var therapistId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_therapist_main)
        etTherapistName = findViewById(R.id.etTherapistName)
        listViewPatients = findViewById(R.id.listViewPatients)
        btnAddPatient = findViewById(R.id.btnAddPatient)
        btnBack = findViewById(R.id.btnBack)
        db = AppDatabase.getDatabase(this)
        therapistId = intent.getLongExtra("therapist_id", -1)
        val therapistName = intent.getStringExtra("therapist_name")



        if (therapistId.toInt() != -1) {
            GlobalScope.launch {
                therapist = db.therapistDao().getTherapistById(therapistId)
                runOnUiThread {
                    etTherapistName.setText(therapist.name)
                }
            }
        }



        val tvBienvenido = findViewById<TextView>(R.id.tvBienvenido)
        tvBienvenido.text = "Bienvenido, ${therapistName}"



        findViewById<Button>(R.id.btnUpdate).setOnClickListener {
            val newName = etTherapistName.text.toString()
            if (newName.isBlank()) {
                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
            } else {
                GlobalScope.launch {
                    therapist.name = newName
                    db.therapistDao().update(therapist)
                    runOnUiThread {
                        Toast.makeText(this@TherapistMainActivity, "Nombre actualizado", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }





        findViewById<Button>(R.id.btnDelete).setOnClickListener {
        GlobalScope.launch {
            db.therapistDao().delete(therapist)
            runOnUiThread {
                Toast.makeText(this@TherapistMainActivity, "Cuenta eliminada", Toast.LENGTH_SHORT).show()
                // Redirigir a login o cerrar la app
                finish()
            }
        }
    }




        loadPatients()

        // Botón para agregar paciente
        btnAddPatient.setOnClickListener {
            val intent = Intent(this, CreatePatientActivity::class.java).apply {
                putExtra("therapist_id", therapistId)
            }
            startActivity(intent)
        }

        // Botón para volver
        btnBack.setOnClickListener {
            finish()
        }

        // Seleccionar paciente para jugar/configurar
        listViewPatients.setOnItemClickListener { _, _, position, _ ->
            val selectedPatient = listViewPatients.adapter.getItem(position) as Patient
            val intent = Intent(this, PatientMainActivity::class.java).apply {
                putExtra("patient_id", selectedPatient.id)
                putExtra("patient_name", selectedPatient.name)
            }
            startActivity(intent)
        }
    }



    private fun loadPatients() {
        GlobalScope.launch(Dispatchers.Main) {
            val patients = db.patientDao().getPatientsByTherapist(therapistId)
            val adapter = ArrayAdapter(
                this@TherapistMainActivity,
                android.R.layout.simple_list_item_1,
                patients
            )
            listViewPatients.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        loadPatients() // Recargar lista al volver de AddPatientActivity
    }
}