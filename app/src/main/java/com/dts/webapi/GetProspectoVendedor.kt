package com.dts.webapi

import com.dts.base.clsClasses
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetProspectoVendedor {
    @GET("/api/ProspVendedor/GetProspectoVendedor")
    fun GetProspectoVendedor(
        @Query("Empresa") Empresa: Int,
        @Query("Anio") Anio: Int,
        @Query("Semana") Semana: Int)
            : Call<List<clsClasses.clsAPIVendedorProspecto?>?>?
}