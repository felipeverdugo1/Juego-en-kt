package com.felipe.aprendamosalimpiar.ui.activities.game

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.felipe.aprendamosalimpiar.R
import com.felipe.aprendamosalimpiar.data.models.ConfiguracionJuego
import com.felipe.aprendamosalimpiar.data.models.Dificultad


class MenuActivity : AppCompatActivity() {
//    private lateinit var dificultad: Dificultad
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_menu)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//
//
//
//
//        val btnJugar = findViewById<Button>(R.id.btnJugar)
//        val btnConfigurar = findViewById<Button>(R.id.btnConfigurar)
//
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//        setSupportActionBar(toolbar)
//
//        btnJugar.setOnClickListener {
//            val intent = Intent(this, PreLevelActivity::class.java)
//            ConfiguracionJuego.iniciarJuego()
//            startActivity(intent)
//
//        }
//
//        btnConfigurar.setOnClickListener {
//            val intent = Intent(this, ConfigActivity::class.java)
//            // habria que hacer como una variable global que conecte con la activity de config
//            startActivity(intent)
//
//
//        }
//    }
//



}