package com.dts.webapi

import com.dts.base.clsClasses
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetVendedorEmpresa {
    @GET("/api/Vendedor/GetVendedorEmpresa")
    fun GetVendedorEmpresa(@Query("Empresa") Empresa: Int)
            : Call<List<clsClasses.clsAPIVendedor?>?>?
}