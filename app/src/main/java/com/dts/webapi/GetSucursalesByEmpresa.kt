package com.dts.webapi

import com.dts.base.clsClasses
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetSucursalesByEmpresa {
    @GET("/api/P_SUCURSAL/GetSucursalesByEmpresa")
    fun GetSucursalesByEmpresa(@Query("Empresa") Empresa: Int)
            : Call<List<clsClasses.clsAPISucursal?>?>?
}