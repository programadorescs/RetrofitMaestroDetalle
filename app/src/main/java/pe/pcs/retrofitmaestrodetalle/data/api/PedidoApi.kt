package pe.pcs.retrofitmaestrodetalle.data.api

import pe.pcs.retrofitmaestrodetalle.data.model.PedidoModel
import pe.pcs.retrofitmaestrodetalle.data.response.DefaultIntResponse
import pe.pcs.retrofitmaestrodetalle.data.response.ListaPedidoApiResponse
import pe.pcs.retrofitmaestrodetalle.data.response.ListaReporteDetallePedidoApiResponse
import retrofit2.Response
import retrofit2.http.*

interface PedidoApi {

    @POST("pedido/registrar")
    suspend fun registrar(
        @Body entidad: PedidoModel
    ): Response<DefaultIntResponse>

    @PUT("pedido/anular/{id}")
    suspend fun anular(@Path("id") id: Int): Response<DefaultIntResponse>

    @GET("pedido/listarPorFecha")
    suspend fun listarPorFecha(
        @Query("desde") desde: String,
        @Query("hasta") hasta: String
    ): Response<ListaPedidoApiResponse>

    @GET("pedido/listarDetalle/{id}")
    suspend fun listarDetalle(@Path("id") id: Int): Response<ListaReporteDetallePedidoApiResponse>

}