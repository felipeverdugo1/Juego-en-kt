package com.felipe.aprendamosalimpiar.data.models

import com.felipe.aprendamosalimpiar.data.AppDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class NivelJuego(
    val nombre: String,
    val herramientaRequerida: Herramienta,
    val parteCuerpo: ParteCuerpo
) {
    NIVEL_1("Nivel 1", Herramienta.RASQUETA_BLANDA, ParteCuerpo.CABEZA),
    NIVEL_2("Nivel 2", Herramienta.RASQUETA_DURA, ParteCuerpo.CUELLO),
    NIVEL_3("Nivel 3", Herramienta.RASQUETA_DURA, ParteCuerpo.PALETA),
    NIVEL_4("Nivel 4", Herramienta.RASQUETA_DURA, ParteCuerpo.LOMO),
    NIVEL_5("Nivel 5", Herramienta.RASQUETA_DURA, ParteCuerpo.PANZA),
    NIVEL_6("Nivel 6", Herramienta.RASQUETA_DURA, ParteCuerpo.ANCA),
    NIVEL_7("Nivel 7", Herramienta.RASQUETA_BLANDA, ParteCuerpo.MANOS),
    NIVEL_8("Nivel 8", Herramienta.RASQUETA_BLANDA, ParteCuerpo.PATAS),
    NIVEL_9("Nivel 9", Herramienta.RASQUETA_BLANDA, ParteCuerpo.VERIJA),
    NIVEL_10("Nivel 10", Herramienta.CEPILLO_BLANDO, ParteCuerpo.CUERPO_GENERAL),
    NIVEL_11("Nivel 11", Herramienta.CEPILLO_DURO, ParteCuerpo.CRINES),
    NIVEL_12("Nivel 12", Herramienta.CEPILLO_DURO, ParteCuerpo.COLA),
    NIVEL_13("Nivel 13", Herramienta.ESCARBA_VASOS, ParteCuerpo.VASOS);



     fun siguienteNivel(): NivelJuego {
        val niveles = entries.toTypedArray() // Obtiene todos los niveles en orden
        val indiceActual = niveles.indexOf(this) // Encuentra el índice del nivel actual
        return if (indiceActual < niveles.size - 1) {
            niveles[indiceActual + 1] // Retorna el siguiente nivel
        } else {
            this // Si ya es el último nivel, se mantiene
        }
    }
}

object ConfiguracionJuego {

    // La dificultad se mantiene solo mientras la app está en ejecución
    lateinit var dificultad: Dificultad
    var patientId : Long = 0
    lateinit var nivel: NivelJuego
    // Nueva propiedad para la voz (valor predeterminado: femenina)
    var voz: TipoVoz = TipoVoz.NINGUNA

    // Enums para organizar las opciones
    enum class TipoVoz { FEMENINA, MASCULINA, NINGUNA }

    fun iniciarJuego(patient_id : Long) {


        // Se asegura que la dificultad esté inicializada antes de comenzar
        if (!this::dificultad.isInitialized) {
            // Si no está inicializada, asigna un valor predeterminado (por ejemplo, MEDIO)
            dificultad = Dificultad.FACIL
        }
        nivel = NivelJuego.NIVEL_1
        patientId = patient_id
    }

    fun actualizarDificultad(nuevaDificultad: Dificultad) {
        dificultad = nuevaDificultad
    }


    fun siguienteNivel() {
        nivel = nivel.siguienteNivel()
    }






}



