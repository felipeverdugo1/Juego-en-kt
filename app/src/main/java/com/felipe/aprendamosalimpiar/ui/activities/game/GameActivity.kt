package com.felipe.aprendamosalimpiar.ui.activities.game

import Game
import TTSHelper
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.felipe.aprendamosalimpiar.R
import com.felipe.aprendamosalimpiar.data.AppDatabase
import com.felipe.aprendamosalimpiar.data.models.ConfiguracionJuego
import com.felipe.aprendamosalimpiar.data.models.Direccion
import com.felipe.aprendamosalimpiar.data.models.EstadoCaballo
import com.felipe.aprendamosalimpiar.data.models.Herramienta
import com.felipe.aprendamosalimpiar.data.models.NivelJuego
import com.felipe.aprendamosalimpiar.data.models.ParteCuerpo
import com.felipe.aprendamosalimpiar.data.models.TipoMovimiento
import com.felipe.aprendamosalimpiar.ui.activities.therapist.PatientMainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

class GameActivity : AppCompatActivity() {


    private lateinit var imgCaballo: ImageView

    private lateinit var herramienta_1: ImageView
    private lateinit var herramienta_2: ImageView
    private lateinit var herramienta_3: ImageView
    private lateinit var herramienta_4: ImageView
    private lateinit var herramienta_5: ImageView

    private lateinit var ttsHelper: TTSHelper

    private lateinit var mancha_1: ImageView
    private lateinit var mancha_2: ImageView
    private lateinit var mancha_3: ImageView
    private lateinit var db: AppDatabase
    private lateinit var mancha_4: ImageView
    private var lastX = 0f  // Variable para guardar la última posición X de la herramienta
    private var lastY = 0f  // Variable para guardar la última posición Y de la herramienta
    private val umbralMovimientoX = 50f  // Umbral para el movimiento horizontal (X)
    private val umbralMovimientoY = 70f  // Umbral para el movimiento vertical (Y)
    private val ultimosMovimientos = mutableListOf<Direccion>()

    private var lastTimeChecked = 0L  // Última vez que se detectó movimiento
    private val tiempoDelay = 300L  // Delay de 50 ms entre detecciones


    private lateinit var estadoDelCaballo: EstadoCaballo
    private lateinit var parteCuerpo: ParteCuerpo
    private lateinit var herramientaCorrecta: Herramienta
    private lateinit var herramientas: List<Herramienta>

    // lo que hace es un diccionario donde  tiene el id(mancha) y cantidad de veces que la herramienta pasa por la mancha
    private val contadorManchas = mutableMapOf<Int, Int>()
    private var manchasLimpias: Int = 0


    private fun setImageSaturation(imageView: ImageView, saturation: Float) {
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(saturation)
        imageView.colorFilter = ColorMatrixColorFilter(colorMatrix)
    }


    private fun agregarMovimiento(direccion: Direccion) {
        ultimosMovimientos.add(direccion)
        if (ultimosMovimientos.size > 10) {
            ultimosMovimientos.removeAt(0)
        }
    }

    private fun cambiarEstadoDelaMancha(imgMancha: ImageView) {
        if (imgMancha.imageAlpha > 50)
            imgMancha.imageAlpha -= 8

    }


    private fun cambiarEstadoDelCaballo(imgCaballo: ImageView, estado: EstadoCaballo) {
        when (estado) {

            EstadoCaballo.muySucio -> {
                imgCaballo.imageAlpha = 128
                setImageSaturation(imgCaballo, 0f)

            }

            EstadoCaballo.algoSucio -> {
                imgCaballo.imageAlpha = 150
                setImageSaturation(imgCaballo, 0.3f)

            }

            EstadoCaballo.limpio -> {
                imgCaballo.imageAlpha = 200
                setImageSaturation(imgCaballo, 0.6f)

            }

            EstadoCaballo.muyLimpio -> {
                imgCaballo.imageAlpha = 255
                setImageSaturation(imgCaballo, 1f)


            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game2)
        initComponents()
        initPreGame()
        initView()
        initGame()

        var btnGuardarYSalir = findViewById<Button>(R.id.btnGuardarYSalir)

        btnGuardarYSalir.setOnClickListener {
            guardarPartidaYSalir()
        }

    }


    private fun initComponents() {


        imgCaballo = findViewById<ImageView>(R.id.imgCaballo)

        mancha_1 = findViewById<ImageView>(R.id.mancha1)
        mancha_2 = findViewById<ImageView>(R.id.mancha2)
        mancha_3 = findViewById<ImageView>(R.id.mancha3)
        mancha_4 = findViewById<ImageView>(R.id.mancha4)



        herramienta_1 = findViewById<ImageView>(R.id.herramienta1)
        herramienta_2 = findViewById<ImageView>(R.id.herramienta2)
        herramienta_3 = findViewById<ImageView>(R.id.herramienta3)
        herramienta_4 = findViewById<ImageView>(R.id.herramienta4)
        herramienta_5 = findViewById<ImageView>(R.id.herramienta5)


    }


    private fun initPreGame() {
        parteCuerpo = ConfiguracionJuego.nivel.parteCuerpo
        herramientaCorrecta = ConfiguracionJuego.nivel.herramientaRequerida
        estadoDelCaballo = EstadoCaballo.muySucio


        herramientas = listOf(
            Herramienta.RASQUETA_DURA,
            Herramienta.RASQUETA_BLANDA,
            Herramienta.CEPILLO_DURO,
            Herramienta.CEPILLO_BLANDO,
            Herramienta.ESCARBA_VASOS
        )


    }

    private fun initView() {
        imgCaballo.setImageResource(parteCuerpo.imagenResId)
        //Dependiendo del nivel se muestran mas o menos
        //Usamos la herramienta 1 como la correcta
        herramienta_1.setImageResource(herramientaCorrecta.imagenResId)
        herramienta_1.visibility = View.VISIBLE
        cambiarEstadoDelCaballo(imgCaballo, EstadoCaballo.muySucio)
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun initGame() {

        ttsHelper = TTSHelper(this)
        if (ConfiguracionJuego.voz != ConfiguracionJuego.TipoVoz.NINGUNA) {
            Handler(Looper.getMainLooper()).postDelayed({
                ttsHelper.speak("Limpia las manchas con la herramienta correspondiente")
            }, 500)

            Handler(Looper.getMainLooper()).postDelayed({
                ttsHelper.speak("Arrastra una herramienta")
            }, 500)

        }

        // Inicializa el contador para cada mancha en 0
        listOf(mancha_1, mancha_2, mancha_3, mancha_4).forEach { contadorManchas[it.id] = 0 }
        manchasLimpias = 0


        herramienta_1.setOnTouchListener(object : View.OnTouchListener {

            //Guardo la posicion inicial de la herramienta
            val posicionInicialHerramientaX = herramienta_1.x
            val posicionInicialHerramientaY = herramienta_1.y


            private var posicionHerramientaX = 0f
            private var posicionHerramientaY = 0f


            override fun onTouch(view: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        posicionHerramientaX = view.x - event.rawX
                        posicionHerramientaY = view.y - event.rawY
                        view.bringToFront()


                    }

                    MotionEvent.ACTION_MOVE -> {
                        // Cuando el usuario mueve se va actualizando la vista
                        view.x = event.rawX + posicionHerramientaX
                        view.y = event.rawY + posicionHerramientaY
                        verificarMovimiento(view) //Verifica si la herramienta esta pasando por la mancha


                    }

                    MotionEvent.ACTION_UP -> {
                        // Si el usuario suelta la herramienta vuelve a la posicion inicial
                        view.animate()
                            .x(posicionInicialHerramientaX)
                            .y(posicionInicialHerramientaY)
                            .setDuration(300) // Duración de la animación en milisegundos
                            .start()
                    }
                }
                return true
            }
        })
    }

    private fun verificarMovimiento(herramienta: View) {


        val manchas = listOf(mancha_1, mancha_2, mancha_3, mancha_4)

        for (mancha in manchas) {
            if (coincidenPuntos(herramienta, mancha)) {
                val idMancha = mancha.id
                ultimosMovimientos.clear()
                contadorManchas[idMancha] = (contadorManchas[idMancha] ?: 0) + 1
                cambiarEstadoDelaMancha(mancha)

                if (contadorManchas[idMancha] == 10) {
                    // Se puede ajustar el numero para aumentar la dificultad o poner algo para desactivar la mancha luego de 2 seg para que el contador(numero) sea mas razonanble
                    estadoDelCaballo = estadoDelCaballo.limpiar()
                    cambiarEstadoDelCaballo(imgCaballo, estadoDelCaballo)
                    mancha.visibility = View.GONE
                    manchasLimpias += 1


                    if (manchasLimpias == 4) {
                        herramienta.animate().x(0F).y(0F)
                            .setDuration(300)
                            .start()
                        pasarDeNivel()
                    }
                }


            }
        }
    }


    private fun coincidenPuntos(herramienta: View, mancha: ImageView): Boolean {


        val rect1 = IntArray(2).also { herramienta.getLocationOnScreen(it) }
        val rect2 = IntArray(2).also { mancha.getLocationOnScreen(it) }

        val x1 = rect1[0]
        val y1 = rect1[1]
        val ancho1 = herramienta.width
        val alto1 = herramienta.height

        val x2 = rect2[0]
        val y2 = rect2[1]
        val ancho2 = mancha.width
        val alto2 = mancha.height

        val movimientoX = x1 - lastX
        val movimientoY = y1 - lastY

        var movimientoDetectado = false

        if (abs(movimientoY) > umbralMovimientoY) {
            movimientoDetectado = true
            val direccion = if (movimientoY > 0) Direccion.ABAJO else Direccion.ARRIBA
            agregarMovimiento(direccion)
            Log.d("Movimiento", "Movimiento detectado: $direccion")
        }

        if (abs(movimientoX) > umbralMovimientoX) {
            movimientoDetectado = true
            val direccion = if (movimientoX > 0) Direccion.DERECHA else Direccion.IZQUIERDA
            agregarMovimiento(direccion)
            Log.d("Movimiento", "Movimiento detectado: $direccion")
        }

        if (movimientoDetectado) {
            lastX = x1.toFloat()
            lastY = y1.toFloat()
        }


        val coincide = (x1 < x2 + ancho2 &&
                x1 + ancho1 > x2 &&
                y1 < y2 + alto2 &&
                y1 + alto1 > y2)

        if (coincide && esMovimientoValido(
                herramientaCorrecta.tipoMovimiento,
                ultimosMovimientos
            )
        ) {
            Log.d("Movimiento", "Correcto")
        }


        return (coincide && esMovimientoValido(
            herramientaCorrecta.tipoMovimiento,
            ultimosMovimientos
        ))

    }


    private fun pasarDeNivel() {
        Log.i("log", ConfiguracionJuego.nivel.name)
        Log.i("log", ConfiguracionJuego.nivel.siguienteNivel().name)
        if (ConfiguracionJuego.nivel.name == NivelJuego.NIVEL_13.name) {
            val intent = Intent(this, FinalizarJuegoActivity::class.java)
            intent.putExtra("MENSAJE", "¡Ganaste!")
            startActivity(intent)
        } else {
            val intent = Intent(this, PasarNivelActitvity::class.java)
            intent.putExtra("MENSAJE", "¡Pasaste de nivel!")
            ConfiguracionJuego.siguienteNivel()
            startActivity(intent)
        }
    }

    private fun guardarPartidaYSalir() {
        Log.d("GameActivity", "Iniciando guardarPartidaYSalir()")

        val pacienteId = ConfiguracionJuego.patientId.also {
            Log.d("GameActivity", "Paciente ID: $it")
        }

        if (pacienteId == 0L) {
            Log.e("GameActivity", "ID de paciente inválido")
            showErrorAndFinish("Error: ID de paciente no válido")
            return
        }

        val fecha = try {
            SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date()).also {
                Log.d("GameActivity", "Fecha generada: $it")
            }
        } catch (e: Exception) {
            Log.e("GameActivity", "Error formateando fecha", e)
            showErrorAndFinish("Error al generar fecha")
            return
        }

        // Usamos lifecycleScope en lugar de GlobalScope
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                Log.d("GameActivity", "Iniciando inserción en DB")

                val partida = Game(
                    patientId = pacienteId,
                    date = fecha,
                    difficulty = ConfiguracionJuego.dificultad.name ?: run {
                        Log.e("GameActivity", "Dificultad es null")
                        "UNKNOWN"
                    },
                    lastLevel = ConfiguracionJuego.nivel.nombre ?: run {
                        Log.e("GameActivity", "Nivel es null")
                        "UNKNOWN"
                    }
                ).also {
                    Log.d("GameActivity", "Partida a insertar: $it")
                }

                val db = AppDatabase.getDatabase(this@GameActivity)
                val insertedId = db.gameDao().insert(partida)
                Log.d("GameActivity", "Partida insertada con ID: $insertedId")

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@GameActivity,
                        "Partida guardada exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()

                    Log.d("GameActivity", "Redirigiendo a PatientMainActivity")
                    Intent(this@GameActivity, PatientMainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        putExtra("patient_id", pacienteId)
                        startActivity(this)
                    }
                }
            } catch (e: Exception) {
                Log.e("GameActivity", "Error al guardar partida", e)
                withContext(Dispatchers.Main) {
                    showErrorAndFinish("Error al guardar la partida: ${e.localizedMessage}")
                }
            }
        }
    }

    private fun showErrorAndFinish(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        finish()
    }

        private fun esMovimientoValido(
            tipoMovimiento: TipoMovimiento,
            movimientos: List<Direccion>
        ): Boolean {
            return tipoMovimiento.direcciones.any { listaPermitida ->
                movimientos.all { it in listaPermitida }
            }
        }

    }


