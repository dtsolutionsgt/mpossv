package com.dts.webapi

import com.dts.base.clsClasses
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GetVentaSucursalesByIds {
    @POST("/api/P_SUCURSAL/GetVentaSucursalesByIds")
    fun GetVentaSucursalesByIds(@Body IdList: List<Int?>?,
                                @Query("fechaDesde") fechaDesde: String,
                                @Query("fechaHasta") fechaHasta: String)
    : Call<List<clsClasses.clsSucursalVenta?>?>?
}