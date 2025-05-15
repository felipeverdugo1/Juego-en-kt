package com.felipe.aprendamosalimpiar.data.models

import com.felipe.aprendamosalimpiar.R

enum class Direccion {
    IZQUIERDA,
    DERECHA,
    ARRIBA,
    ABAJO,
    CIRCULAR
}


enum class Dificultad {
    FACIL,
    MEDIO,
    DIFICIL
}

enum class EstadoCaballo {
    muySucio,
    algoSucio,
    limpio,
    muyLimpio;


    fun limpiar(): EstadoCaballo {
        return when (this) {
            muySucio -> algoSucio
            algoSucio -> limpio
            limpio -> muyLimpio
            muyLimpio -> muyLimpio
        }
    }
}




enum class PosicionLimpieza(
   val imagenResId: Int ,
) {
    IZQUIERDA(R.drawable.silueta_left),
    DERECHA(R.drawable.silueta_right),
    ABAJO_DERECHA(R.drawable.silueta_bottom_right),
    ABAJO_IZQUIERDA(R.drawable.silueta_bottom_left)
}



enum class ParteCuerpo(
    val imagenResId: Int,
    val tipoPosicionLimpieza: PosicionLimpieza,
    val layoutMarginStart : Int
)
{
    CABEZA(R.drawable.cabeza_4, PosicionLimpieza.IZQUIERDA, 30),
    CUELLO(R.drawable.cuello, PosicionLimpieza.IZQUIERDA, 30),
    PALETA(R.drawable.paleta, PosicionLimpieza.DERECHA, 120),
    LOMO(R.drawable.lomo, PosicionLimpieza.IZQUIERDA, 100),
    PANZA(R.drawable.panza, PosicionLimpieza.ABAJO_DERECHA, 100),
    ANCA(R.drawable.anca, PosicionLimpieza.IZQUIERDA, 150),
    MANOS(R.drawable.manos, PosicionLimpieza.ABAJO_IZQUIERDA, 100),
    PATAS(R.drawable.patas, PosicionLimpieza.ABAJO_DERECHA, 180),
    VERIJA(R.drawable.verija, PosicionLimpieza.ABAJO_DERECHA, 160),
    CUERPO_GENERAL(R.drawable.img_caballo2, PosicionLimpieza.IZQUIERDA, 100),
    CRINES(R.drawable.crines, PosicionLimpieza.DERECHA, 110),
    COLA(R.drawable.cola, PosicionLimpieza.IZQUIERDA, 190),
    VASOS(R.drawable.casco, PosicionLimpieza.ABAJO_DERECHA, 150)
}

