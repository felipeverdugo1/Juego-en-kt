package com.felipe.aprendamosalimpiar.ui.activities.therapist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.felipe.aprendamosalimpiar.R
import com.felipe.aprendamosalimpiar.data.AppDatabase
import com.felipe.aprendamosalimpiar.data.models.ConfiguracionJuego
import com.felipe.aprendamosalimpiar.data.models.Patient
import com.felipe.aprendamosalimpiar.ui.activities.abm.patient.UpdatePatientActivity
import com.felipe.aprendamosalimpiar.ui.activities.game.ConfigActivity
import com.felipe.aprendamosalimpiar.ui.activities.game.PreLevelActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PatientMainActivity : AppCompatActivity() {
    private lateinit var btnBack: Button
    private var patient_id: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        var db = AppDatabase.getDatabase(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_main)
        btnBack = findViewById(R.id.btnBack)
        patient_id = intent.getLongExtra("patient_id", 0)
        val patientName = intent.getStringExtra("patient_name")

        val tvBienvenido = findViewById<TextView>(R.id.tvBienvenido)

            tvBienvenido.text = "Ha seleccionada al paciente, ${patientName?.uppercase()}"


            findViewById<Button>(R.id.btnEdit).setOnClickListener {
                val intent = Intent(this, UpdatePatientActivity::class.java)
                intent.putExtra("patient_id", patient_id)
                startActivity(intent)
                finish()
            }

            findViewById<Button>(R.id.btnDelete).setOnClickListener {
                GlobalScope.launch {
                    db.therapistDao().deleteById(patient_id.toInt())
                    runOnUiThread {
                        Toast.makeText(
                            this@PatientMainActivity,
                            "Paciente eliminado/a",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Redirigir a login o cerrar la app
                        finish()
                    }
                }
            }



        findViewById<Button>(R.id.btnVerEstadisticas).setOnClickListener {
            val intent = Intent(this, EstadisticasActivity::class.java).apply {
                putExtra("patient_id", patient_id) // Asegúrate de tener el ID del paciente
                putExtra("patient_name", patientName)
            }
            startActivity(intent)
        }

            findViewById<Button>(R.id.btnConfigGame).setOnClickListener {
                val intent = Intent(this, ConfigActivity::class.java)
                intent.putExtra("patient_id", patient_id)
                startActivity(intent)
            }


            // Botón para volver
            btnBack.setOnClickListener {
                finish()
            }


            findViewById<Button>(R.id.btnPlayGame).setOnClickListener {
                val intent = Intent(this, PreLevelActivity::class.java)
                ConfiguracionJuego.iniciarJuego()
                startActivity(intent)

            }




        }
    }
