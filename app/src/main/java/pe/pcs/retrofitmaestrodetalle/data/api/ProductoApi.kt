package pe.pcs.retrofitmaestrodetalle.data.api

import pe.pcs.retrofitmaestrodetalle.data.model.ProductoModel
import pe.pcs.retrofitmaestrodetalle.data.model.ResponseHttp
import retrofit2.Response
import retrofit2.http.*

interface ProductoApi {

    @GET("producto/listar")
    suspend fun listar(): Response<ResponseHttp>

    @GET("producto/listarPorDescripcion/{dato}")
    suspend fun listarPorNombre(@Path("dato") dato: String): Response<ResponseHttp>

    @POST("producto/registrar")
    suspend fun registrar(
        @Body entidad: ProductoModel
    ): Response<ResponseHttp>

    @PUT("producto/actualizar")
    suspend fun actualizar(
        @Body entidad: ProductoModel
    ): Response<ResponseHttp>

    @DELETE("producto/eliminar/{id}")
    suspend fun eliminar(@Path("id") id: Long): Response<ResponseHttp>

}