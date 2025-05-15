package com.felipe.aprendamosalimpiar.ui.activities.therapist

import Game
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.felipe.aprendamosalimpiar.R
import com.felipe.aprendamosalimpiar.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class EstadisticasActivity  : AppCompatActivity() {


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_estadisticas)

            val recyclerView = findViewById<RecyclerView>(R.id.recyclerEstadisticas)
            recyclerView.layoutManager = LinearLayoutManager(this)

            // Obtén el ID del paciente (deberías pasarlo desde la actividad anterior)
            val pacienteId = intent.getLongExtra("patient_id", 0L)


            val patientName = intent.getStringExtra("patient_name")

//            val tvBienvenido = findViewById<TextView>(R.id.tvBienvenido)
//
//            tvBienvenido.text = "Ha seleccionada al paciente, ${patientName?.uppercase()}"

            GlobalScope.launch(Dispatchers.IO) {
                val partidas = AppDatabase.getDatabase(this@EstadisticasActivity)
                    .gameDao()
                    .getPartidasPorPaciente(pacienteId)

                runOnUiThread {
                    recyclerView.adapter = PartidaAdapter(partidas)
                }
            }
        }
    }

    class PartidaAdapter(private val partidas: List<Game>) : RecyclerView.Adapter<PartidaAdapter.ViewHolder>() {
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvFecha: TextView = view.findViewById(R.id.tvFecha)
            val tvDificultad: TextView = view.findViewById(R.id.tvDificultad)
            val tvUltimoNivel: TextView = view.findViewById(R.id.tvLastLevel)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_partida, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val partida = partidas[position]
            Log.d("pepe",partida.date)
            holder.tvFecha.text = "Fecha: ${partida.date}"
            holder.tvDificultad.text = "Dificultad: ${partida.difficulty.name}"
            holder.tvUltimoNivel.text = "Ultimo Nivel: ${partida.lastLevel.name}"

        }

        override fun getItemCount() = partidas.size
    }
