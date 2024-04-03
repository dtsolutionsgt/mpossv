package com.dts.base

class clsClasses {

    inner class clsListaSucursal(cs: Int, descripcion: String) {
        var codigo: Int = cs
        var nombre: String = descripcion
    }

    inner class clsListaVendedor (cv:Int,nv:String,cs:Int) {
        var codigo: Int = cv
        var nombre: String = nv
        var idsucursal: Int = cs
    }

    inner class clsVendedorProspecto (cv:Int,nv:String,cs:Int) {
        var codigo: Int = cv
        var nombre: String = nv
        var cant: Int = 0
        var meta: Int = 15
        var idsucursal: Int = cs
    }

    inner class clsVendedorProspectoDatos (cv:Int,ca:Int,mt:Int,est:String) {
        var codigo: Int = cv
        var cant: Int = ca
        var meta: Int = mt
        var estado : String = est
    }

    //region API

    inner class clsAPISucursal {
        var CODIGO_SUCURSAL : Int = 0
        var DESCRIPCION : String = ""
        var DIRECCION : String = ""
        var FEL_FECHA_VENCE_CONTRATO : String = ""
    }

    inner class clsAPIVendedor {
        var CODIGO_VENDEDOR : Int = 0
        var NOMBRE : String = ""
        var SUCURSAL : Int = 0
        var NOMBRE_SUCURSAL : String = ""
    }

    inner class clsAPIVendedorProspecto {
        var CODIGO_VENDEDOR : Int = 0
        var NOMBRE_VENDEDOR : String = ""
        var ANIO_OBJ : Int = 0
        var SEMANA_OBJ : Int = 0
        var OBJETIVO_CANTIDAD : Int = 0
        var CANTIDAD_REALIZADA : Int = 0
        var ESTADO_OBJETIVO : String = ""
        var CODIGO_SUCURSAL : Int = 0
    }

    //endregion

    //region Obsoleto

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

    //endregion

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