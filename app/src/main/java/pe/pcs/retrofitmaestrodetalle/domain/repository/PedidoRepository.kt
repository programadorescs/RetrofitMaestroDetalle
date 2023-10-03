package pe.pcs.retrofitmaestrodetalle.domain.repository

import pe.pcs.retrofitmaestrodetalle.domain.model.Pedido
import pe.pcs.retrofitmaestrodetalle.domain.model.ReporteDetallePedido

interface PedidoRepository {

    suspend fun registrar(entidad: Pedido): Int

    suspend fun anular(id: Int): Int

    suspend fun listarPorFecha(desde: String, hasta: String): List<Pedido>

    suspend fun listarDetalle(id: Int): List<ReporteDetallePedido>

}