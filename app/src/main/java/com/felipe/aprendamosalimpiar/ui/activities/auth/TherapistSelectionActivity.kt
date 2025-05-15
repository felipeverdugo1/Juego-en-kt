package com.felipe.aprendamosalimpiar.ui.activities.auth

import TTSHelper
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.felipe.aprendamosalimpiar.R
import com.felipe.aprendamosalimpiar.data.AppDatabase
import com.felipe.aprendamosalimpiar.data.models.Therapist
import com.felipe.aprendamosalimpiar.ui.activities.abm.therapist.CreateTherapistActivity
import com.felipe.aprendamosalimpiar.ui.activities.therapist.TherapistMainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TherapistSelectionActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var btnAddTherapist: Button
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_therapist_selection)


        listView = findViewById(R.id.listViewTherapists)
        btnAddTherapist = findViewById(R.id.btnAddTherapist)
        db = AppDatabase.getDatabase(this)

        // Cargar lista de terapeutas
        loadTherapists()

        // Botón para agregar terapeuta
        btnAddTherapist.setOnClickListener {
            startActivity(Intent(this, CreateTherapistActivity::class.java))
        }

        // Seleccionar terapeuta
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedTherapist = listView.adapter.getItem(position) as Therapist
            val intent = Intent(this, TherapistMainActivity::class.java).apply {
                putExtra("therapist_id", selectedTherapist.id)
                putExtra("therapist_name", selectedTherapist.name)
            }
            startActivity(intent)
        }
    }

    private fun loadTherapists() {
        GlobalScope.launch(Dispatchers.Main) {
            val therapists = db.therapistDao().getAllTherapists()
            val adapter = ArrayAdapter(
                this@TherapistSelectionActivity,
                android.R.layout.simple_list_item_1,
                therapists
            )
            listView.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        loadTherapists() // Recargar lista al volver de AddPatientActivity
    }


}