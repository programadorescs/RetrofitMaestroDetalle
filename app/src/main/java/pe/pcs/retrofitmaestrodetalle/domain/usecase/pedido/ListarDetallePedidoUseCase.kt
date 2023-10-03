package pe.pcs.retrofitmaestrodetalle.domain.usecase.pedido

import pe.pcs.retrofitmaestrodetalle.core.ResponseStatus
import pe.pcs.retrofitmaestrodetalle.core.makeCall
import pe.pcs.retrofitmaestrodetalle.domain.model.ReporteDetallePedido
import pe.pcs.retrofitmaestrodetalle.domain.repository.PedidoRepository
import javax.inject.Inject

class ListarDetallePedidoUseCase @Inject constructor(private val repository: PedidoRepository) {

    suspend operator fun invoke(idPedido: Int): ResponseStatus<List<ReporteDetallePedido>> {

        return makeCall {
            repository.listarDetalle(idPedido)
        }

    }

}