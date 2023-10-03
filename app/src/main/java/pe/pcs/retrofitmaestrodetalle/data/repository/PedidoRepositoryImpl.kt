package pe.pcs.retrofitmaestrodetalle.data.repository

import pe.pcs.retrofitmaestrodetalle.data.api.PedidoApi
import pe.pcs.retrofitmaestrodetalle.data.model.toDatabase
import pe.pcs.retrofitmaestrodetalle.domain.model.Pedido
import pe.pcs.retrofitmaestrodetalle.domain.model.ReporteDetallePedido
import pe.pcs.retrofitmaestrodetalle.domain.model.toDomain
import pe.pcs.retrofitmaestrodetalle.domain.repository.PedidoRepository
import javax.inject.Inject

class PedidoRepositoryImpl @Inject constructor(private val api: PedidoApi): PedidoRepository {

    override suspend fun registrar(entidad: Pedido): Int {
        return api.registrar(entidad.toDatabase()).data
    }

    override suspend fun anular(id: Int): Int {
        return api.anular(id).data
    }

    override suspend fun listarPorFecha(desde: String, hasta: String): List<Pedido> {
        return api.listarPorFecha(desde, hasta).data.map {
            it.toDomain()
        }
    }

    override suspend fun listarDetalle(id: Int): List<ReporteDetallePedido> {
        return api.listarDetalle(id).data.map {
            it.toDomain()
        }
    }

}