package pe.pcs.retrofitmaestrodetalle.domain.usecase.pedido

import pe.pcs.retrofitmaestrodetalle.domain.repository.PedidoRepository
import javax.inject.Inject

class AnularPedidoUseCase @Inject constructor(private val repository: PedidoRepository) {

    suspend operator fun invoke(idPedido: Int): Int {
        return repository.anular(idPedido)
    }

}