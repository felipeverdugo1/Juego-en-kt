package com.felipe.aprendamosalimpiar.ui.activities.therapist

import Game
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.felipe.aprendamosalimpiar.R
import com.felipe.aprendamosalimpiar.data.AppDatabase
import kotlinx.coroutines.launch

class EstadisticasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estadisticas)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerEstadisticas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val pacienteId = intent.getLongExtra("patient_id", 0L)
        val patientName = intent.getStringExtra("patient_name")

        if (pacienteId == 0L) {
            Log.e("EstadisticasActivity", "ID de paciente inválido")
            return
        }

        lifecycleScope.launch {
            try {
                val db = AppDatabase.getDatabase(this@EstadisticasActivity)
                val partidas = db.gameDao().getPartidasPorPaciente(pacienteId)

                Log.d("EstadisticasActivity", "Partidas encontradas: ${partidas.size}")

                runOnUiThread {
                    if (partidas.isEmpty()) {
                        findViewById<TextView>(R.id.tvEmptyMessage).visibility = View.VISIBLE
                    } else {
                        recyclerView.adapter = PartidaAdapter(partidas)
                    }
                }
            } catch (e: Exception) {
                Log.e("EstadisticasActivity", "Error al obtener partidas", e)
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
        holder.tvFecha.text = "Fecha: ${partida.date}"
        holder.tvDificultad.text = "Dificultad: ${partida.difficulty}"
        holder.tvUltimoNivel.text = "Último Nivel: ${partida.lastLevel}"
    }

    override fun getItemCount() = partidas.size
}