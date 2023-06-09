package pe.pcs.retrofitmaestrodetalle.domain.usecase.pedido

import pe.pcs.retrofitmaestrodetalle.core.ResponseStatus
import pe.pcs.retrofitmaestrodetalle.core.makeCall
import pe.pcs.retrofitmaestrodetalle.data.repository.PedidoRepository
import javax.inject.Inject

class AnularPedidoUseCase @Inject constructor(private val repository: PedidoRepository) {

    suspend operator fun invoke(idPedido: Int): ResponseStatus<Int> {

        return makeCall {
            repository.anular(idPedido).body()?.data ?: 0
        }

    }

}