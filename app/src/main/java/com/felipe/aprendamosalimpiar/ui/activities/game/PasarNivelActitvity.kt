package com.felipe.aprendamosalimpiar.ui.activities.game

import TTSHelper
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.felipe.aprendamosalimpiar.R

class PasarNivelActitvity : AppCompatActivity() {

        private lateinit var tvView: TextView
        private lateinit var btnSalir : Button
        private lateinit var mensaje : String


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)


            setContentView(R.layout.activity_pasar_nivel)


            initComponents()
            initExtras()
            initListeners()



            tvView.text = mensaje


        }


    private fun initComponents() {
         tvView =findViewById(R.id.tvViewMensaje)
         btnSalir= findViewById(R.id.btnSalir)

    }


    private fun initExtras() {
        mensaje = intent.getStringExtra("MENSAJE").toString()

        val ttsHelper = TTSHelper(this)
        Handler(Looper.getMainLooper()).postDelayed({
            ttsHelper.speak(mensaje)
        }, 100)




    }


    private fun initListeners() {

        btnSalir.setOnClickListener {

            val intent = Intent(this, PreLevelActivity::class.java)
            startActivity(intent)
            // Finalizar la actividad actual para que no quede en el stack
            finish()
        }

    }






}


