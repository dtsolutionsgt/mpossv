package com.dts.base

data class d_menuitem(
    val mid: Int,
    val nombre: String
    )

data class d_listDialogItem(
    var idresource: Int,
    var codigo: String ,
    var text: String ,
    var text2: String
)

data class d_sucursalestado(
    var idsucursal: Int,
    var nombre: String,
    var estado: Int,
    var estadoDesc: String,
    var totalCierre: Double,
    var diferCierre: Double,
    var totalVentas: Double,
    var cantVentas: Int
)