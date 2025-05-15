package com.felipe.aprendamosalimpiar.ui.activities.game

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.felipe.aprendamosalimpiar.R
import com.felipe.aprendamosalimpiar.data.models.ConfiguracionJuego
import com.felipe.aprendamosalimpiar.data.models.Dificultad



class ConfigActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
        val btnJugar = findViewById<Button>(R.id.btnJugar)
        val btnFacil = findViewById<Button>(R.id.btnFacil)
        btnFacil.text = Dificultad.FACIL.name
        val btnMedio = findViewById<Button>(R.id.btnMedio)
        btnMedio.text = Dificultad.MEDIO.name
        val btnDificil = findViewById<Button>(R.id.btnDificil)
        btnDificil.text = Dificultad.DIFICIL.name


        btnFacil.setOnClickListener {
            ConfiguracionJuego.actualizarDificultad(Dificultad.FACIL)
            Toast.makeText(this, "Dificultad: Fácil seleccionada", Toast.LENGTH_SHORT).show()
        }

        btnMedio.setOnClickListener {
            ConfiguracionJuego.actualizarDificultad(Dificultad.MEDIO)
            Toast.makeText(this, "Dificultad: Medio seleccionada", Toast.LENGTH_SHORT).show()
        }

        btnDificil.setOnClickListener {
            ConfiguracionJuego.actualizarDificultad(Dificultad.DIFICIL)
            Toast.makeText(this, "Dificultad: Difícil seleccionada", Toast.LENGTH_SHORT).show()
        }


        btnJugar.setOnClickListener {
            val intent = Intent(this, PreLevelActivity::class.java)
            ConfiguracionJuego.iniciarJuego()
            startActivity(intent)
        }


        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupVoice)

        // Selecciona el botón correcto según la configuración actual
        // Selecciona el botón según la configuración actual
        radioGroup.check(
            when (ConfiguracionJuego.voz) {
                ConfiguracionJuego.TipoVoz.FEMENINA -> R.id.radioFemale
                ConfiguracionJuego.TipoVoz.MASCULINA -> R.id.radioMale
                ConfiguracionJuego.TipoVoz.NINGUNA -> R.id.radioNone
            }
        )

        // Actualiza la configuración al cambiar la selección
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            ConfiguracionJuego.voz = when (checkedId) {
                R.id.radioFemale -> ConfiguracionJuego.TipoVoz.FEMENINA
                R.id.radioMale -> ConfiguracionJuego.TipoVoz.MASCULINA
                else -> ConfiguracionJuego.TipoVoz.NINGUNA
            }
        }
    }
}






