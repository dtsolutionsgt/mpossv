package com.dts.base

class clsClasses {

    class clsSucursalId {
        var CODIGO_SUCURSAL = 0
        var DESCRIPCION = ""
        var DIRECCION = ""
        var FEL_FECHA_VENCE_CONTRATO = ""
    }

    class clsSucursalVenta {
        var EMPRESA = 0
        var SUCURSAL = 0
        var MONTO = 0.0
        var CANT = 0
    }

    inner class clsSucursalEstado {
        var idsucursal: Int=0
        var nombre: String=""
        var estado: Int=-1
        var estadoDesc: String=""
        var totalCierre: Double=-1.0
        var diferCierre: Double=0.0
        var totalVentas: Double=-1.0
        var cantVentas: Int=-1
    }

    inner class clsSucursal {
        var codigo_sucursal = 0
        var empresa = 0
        var nombre: String? = null
        var modo_inventario = 0
    }

    //region AppBase

    inner class clsMenu {
        var id = 0
        var nombre: String? = null
    }

    inner class clsParams {
        var id = 0
        var dbver = 0
        var param1: String? = null
        var param2: String? = null
        var param3 = 0
        var param4 = 0
        var lic1: String? = null
        var lic2 = 0
    }

    inner class clsParamsext {
        var id = 0
        var nombre: String? = null
        var valor: String? = null
        var tipo: String? = null
    }

    //endregion

}