package com.dts.webapi

import com.dts.base.clsClasses
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface GetCotVendedorDet {
    @POST("/api/Cot_Vendedor/GetCotVendedorDet")
    fun getCotizVendedorDocs(@Body body: clsClasses.clsAPIVendedorCotizacionBody): Call<List<clsClasses.clsAPIVendedorCotizacionDet?>?>?
}