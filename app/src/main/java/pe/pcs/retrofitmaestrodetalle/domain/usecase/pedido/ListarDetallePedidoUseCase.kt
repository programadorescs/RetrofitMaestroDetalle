package pe.pcs.retrofitmaestrodetalle.domain.usecase.pedido

import pe.pcs.retrofitmaestrodetalle.domain.ResponseStatus
import pe.pcs.retrofitmaestrodetalle.domain.makeCall
import pe.pcs.retrofitmaestrodetalle.data.repository.PedidoRepository
import pe.pcs.retrofitmaestrodetalle.domain.model.ReporteDetallePedido
import pe.pcs.retrofitmaestrodetalle.domain.model.toDomain
import javax.inject.Inject

class ListarDetallePedidoUseCase @Inject constructor(private val repository: PedidoRepository) {

    suspend operator fun invoke(idPedido: Int): ResponseStatus<List<ReporteDetallePedido>> {

        return makeCall {
            repository.listarDetalle(idPedido).body()?.data?.map {
                it.toDomain()
            } ?: listOf()
        }

    }

}