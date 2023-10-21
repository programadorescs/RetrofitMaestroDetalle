package pe.pcs.retrofitmaestrodetalle.domain.usecase.pedido

import pe.pcs.retrofitmaestrodetalle.domain.model.Pedido
import pe.pcs.retrofitmaestrodetalle.domain.repository.PedidoRepository
import javax.inject.Inject

class ListarPedidoPorFechaUseCase @Inject constructor(private val repository: PedidoRepository) {

    suspend operator fun invoke(desde: String, hasta: String): List<Pedido> {

        return repository.listarPorFecha(desde, hasta)

    }

}