package com.dts.webapi

import com.dts.base.clsClasses.clsSucursalId
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SucursalesByID {
    @POST("/api/P_SUCURSAL/GetSucursalesByIds")
    fun getSucursalId(@Body IdList: List<Int?>?): Call<List<clsSucursalId?>?>?
}