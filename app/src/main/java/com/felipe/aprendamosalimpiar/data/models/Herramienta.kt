package com.felipe.aprendamosalimpiar.data.models

import com.felipe.aprendamosalimpiar.R





enum class Herramienta(
    val tipoMovimiento: TipoMovimiento,
    val partesPermitidas: List<ParteCuerpo>,
    val imagenResId: Int // ID del drawable

) {
    RASQUETA_BLANDA(
        TipoMovimiento.VERTICAL_O_HORIZONTAL, listOf(
            ParteCuerpo.CABEZA,
            ParteCuerpo.MANOS,
            ParteCuerpo.PATAS
    ), R.drawable.rasqueta_blanda),

    RASQUETA_DURA(
        TipoMovimiento.VERTICAL_O_HORIZONTAL, listOf(
            ParteCuerpo.CUELLO,
            ParteCuerpo.PALETA,
            ParteCuerpo.LOMO,
            ParteCuerpo.PANZA,
            ParteCuerpo.ANCA
    ),R.drawable.rasqueta_dura),

    CEPILLO_DURO(
        TipoMovimiento.DERECHA_O_ABAJO, listOf(
            ParteCuerpo.CRINES,
            ParteCuerpo.COLA
    ),R.drawable.cepillo_duro),

    CEPILLO_BLANDO(
        TipoMovimiento.DERECHA_O_ABAJO, listOf(
            ParteCuerpo.CUERPO_GENERAL
    ),R.drawable.cepillo_blando),

    ESCARBA_VASOS(
        TipoMovimiento.VERTICAL_O_HORIZONTAL, listOf(
            ParteCuerpo.VASOS
    ),R.drawable.escarba_vasos);
}

enum class TipoMovimiento(val direcciones: List<List<Direccion>>) {
    DERECHA_O_ABAJO(listOf(
        listOf(Direccion.DERECHA,Direccion.ABAJO),
        listOf(Direccion.ABAJO,Direccion.DERECHA),
        listOf(Direccion.ABAJO),
        listOf(Direccion.DERECHA)
    )),
    VERTICAL_O_HORIZONTAL(listOf(
        listOf(Direccion.ARRIBA, Direccion.ABAJO),
        listOf(Direccion.IZQUIERDA, Direccion.DERECHA)
    ))
}