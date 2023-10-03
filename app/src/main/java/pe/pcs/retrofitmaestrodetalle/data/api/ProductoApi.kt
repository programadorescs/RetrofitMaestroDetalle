package pe.pcs.retrofitmaestrodetalle.data.api

import pe.pcs.retrofitmaestrodetalle.data.model.ProductoModel
import pe.pcs.retrofitmaestrodetalle.data.response.DefaultIntResponse
import pe.pcs.retrofitmaestrodetalle.data.response.ListaProductoApiResponse
import retrofit2.http.*

interface ProductoApi {

    @GET("producto/listar")
    suspend fun listar(): ListaProductoApiResponse

    @GET("producto/listarPorDescripcion/{dato}")
    suspend fun listarPorNombre(@Path("dato") dato: String): ListaProductoApiResponse

    @POST("producto/registrar")
    suspend fun registrar(
        @Body entidad: ProductoModel
    ): DefaultIntResponse

    @PUT("producto/actualizar")
    suspend fun actualizar(
        @Body entidad: ProductoModel
    ): DefaultIntResponse

    @DELETE("producto/eliminar/{id}")
    suspend fun eliminar(@Path("id") id: Long): DefaultIntResponse

}