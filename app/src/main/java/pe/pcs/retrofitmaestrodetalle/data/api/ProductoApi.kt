package pe.pcs.retrofitmaestrodetalle.data.api

import pe.pcs.retrofitmaestrodetalle.data.model.ProductoModel
import pe.pcs.retrofitmaestrodetalle.data.response.DefaultIntResponse
import pe.pcs.retrofitmaestrodetalle.data.response.ListaProductoApiResponse
import retrofit2.Response
import retrofit2.http.*

interface ProductoApi {

    @GET("producto/listar")
    suspend fun listar(): Response<ListaProductoApiResponse>

    @GET("producto/listarPorDescripcion/{dato}")
    suspend fun listarPorNombre(@Path("dato") dato: String): Response<ListaProductoApiResponse>

    @POST("producto/registrar")
    suspend fun registrar(
        @Body entidad: ProductoModel
    ): Response<DefaultIntResponse>

    @PUT("producto/actualizar")
    suspend fun actualizar(
        @Body entidad: ProductoModel
    ): Response<DefaultIntResponse>

    @DELETE("producto/eliminar/{id}")
    suspend fun eliminar(@Path("id") id: Long): Response<DefaultIntResponse>

}