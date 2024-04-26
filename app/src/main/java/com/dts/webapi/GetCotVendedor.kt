package com.dts.webapi

import com.dts.base.clsClasses
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface GetCotVendedor {
    @POST("/api/Cot_Vendedor/GetCotVendedor")
    fun getCotizVendedor(@Body body: clsClasses.clsAPIVendedorCotizacionBody): Call<List<clsClasses.clsAPIVendedorCotizacion?>?>?
}