package com.felipe.aprendamosalimpiar.ui.activities.game

import com.felipe.aprendamosalimpiar.data.models.ConfiguracionJuego

class VariablesDeDificiltad {
        private var inicioTiempo: Long = 0L
        private var errores: Int = 0
        private var herramientasUsadas: Int = 0

        fun iniciarCronometro() {
            inicioTiempo = System.currentTimeMillis()
        }

        fun registrarError() {
            errores++
        }

        fun registrarHerramientaUsada() {
            herramientasUsadas++
        }

        private fun obtenerTiempoTranscurrido(): Int {
            return ((System.currentTimeMillis() - inicioTiempo) / 1000).toInt()
        }

        fun verificarSiPerdio(): Boolean {
            val dificultad = ConfiguracionJuego.dificultad

            // Verificar intentos
            if (dificultad.intentosPermitidos != -1 && errores >= dificultad.intentosPermitidos) {
                return true
            }

            // Verificar tiempo
            if (dificultad.tiempoLimiteSegundos != -1 &&
                obtenerTiempoTranscurrido() >= dificultad.tiempoLimiteSegundos) {
                return true
            }

            return false
        }

        fun getMetricas(): Triple<Int, Int, Int> {
            return Triple(
                obtenerTiempoTranscurrido(), // Tiempo en segundos
                errores,                    // Intentos fallidos
                herramientasUsadas          // Herramientas usadas
            )
        }
    }
