package pe.pcs.retrofitmaestrodetalle.domain.usecase.pedido

import pe.pcs.retrofitmaestrodetalle.domain.model.ReporteDetallePedido
import pe.pcs.retrofitmaestrodetalle.domain.repository.PedidoRepository
import javax.inject.Inject

class ListarDetallePedidoUseCase @Inject constructor(private val repository: PedidoRepository) {

    suspend operator fun invoke(idPedido: Int): List<ReporteDetallePedido> {

        return repository.listarDetalle(idPedido)

    }

}