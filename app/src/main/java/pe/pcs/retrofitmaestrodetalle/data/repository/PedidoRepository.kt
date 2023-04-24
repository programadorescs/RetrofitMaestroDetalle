package pe.pcs.retrofitmaestrodetalle.data.repository

import pe.pcs.retrofitmaestrodetalle.data.model.PedidoModel
import pe.pcs.retrofitmaestrodetalle.data.api.PedidoApi
import pe.pcs.retrofitmaestrodetalle.data.response.DefaultIntResponse
import pe.pcs.retrofitmaestrodetalle.data.response.ListaPedidoApiResponse
import pe.pcs.retrofitmaestrodetalle.data.response.ListaReporteDetallePedidoApiResponse
import retrofit2.Response
import javax.inject.Inject

class PedidoRepository @Inject constructor(private val api: PedidoApi) {

    suspend fun registrar(entidad: PedidoModel): Response<DefaultIntResponse> {

        return api.registrar(entidad)

    }

    suspend fun anular(id: Int): Response<DefaultIntResponse> {

        return api.anular(id)

    }

    suspend fun listarPorFecha(desde: String, hasta: String): Response<ListaPedidoApiResponse> {

        return api.listarPorFecha(desde, hasta)

    }

    suspend fun listarDetalle(id: Int): Response<ListaReporteDetallePedidoApiResponse> {

        return api.listarDetalle(id)

    }

}