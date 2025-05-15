package com.felipe.aprendamosalimpiar.ui.activities.game

import TTSHelper
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.felipe.aprendamosalimpiar.R
import com.felipe.aprendamosalimpiar.data.models.ConfiguracionJuego
import com.felipe.aprendamosalimpiar.data.models.ParteCuerpo


class PreLevelActivity : AppCompatActivity() {



    private lateinit var ttsHelper: TTSHelper

    private lateinit var btnSiguente : Button
    private lateinit var tvNivel : TextView
    private lateinit var imgSilueta : ImageView
    private lateinit var tvParteCuerpo: TextView


    private fun aplicarMargenSegunParte(imageView: ImageView, parte: ParteCuerpo) {
        val params = imageView.layoutParams as ViewGroup.MarginLayoutParams
        params.marginStart = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            parte.layoutMarginStart.toFloat(),
            imageView.resources.displayMetrics
        ).toInt()
        imageView.layoutParams = params
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_level)
        initComponents()
        initListeners()
        initView()




    }



    private fun initComponents() {

        btnSiguente = findViewById<Button>(R.id.btnSiguiente)

        tvNivel = findViewById<TextView>(R.id.tvNivel)

        tvParteCuerpo = findViewById<TextView>(R.id.tvParteDelCuerpo)

        imgSilueta = findViewById<ImageView>(R.id.imgSilueta)





    }


    private fun initListeners() {

        btnSiguente.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)

        }

    }


    private fun initView() {
        val nivel = ConfiguracionJuego.nivel

        imgSilueta.setImageResource(nivel.parteCuerpo.tipoPosicionLimpieza.imagenResId)
        tvNivel.text = nivel.nombre
        tvParteCuerpo.text = nivel.parteCuerpo.name
        aplicarMargenSegunParte(imgSilueta, nivel.parteCuerpo)


        ttsHelper = TTSHelper(this)
        if (ConfiguracionJuego.voz != ConfiguracionJuego.TipoVoz.NINGUNA) {

            Handler(Looper.getMainLooper()).postDelayed({
                ttsHelper.speak(nivel.nombre)
            }, 500)


            Handler(Looper.getMainLooper()).postDelayed({
                ttsHelper.speak((nivel.parteCuerpo.name))
            }, 500)



        }





    }


    override fun onDestroy() {
        ttsHelper.shutdown()
        super.onDestroy()
    }




}







