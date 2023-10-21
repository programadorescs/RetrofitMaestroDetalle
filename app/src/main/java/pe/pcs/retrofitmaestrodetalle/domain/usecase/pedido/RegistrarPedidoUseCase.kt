package pe.pcs.retrofitmaestrodetalle.domain.usecase.pedido

import pe.pcs.retrofitmaestrodetalle.domain.model.Pedido
import pe.pcs.retrofitmaestrodetalle.domain.repository.PedidoRepository
import javax.inject.Inject

class RegistrarPedidoUseCase @Inject constructor(private val repository: PedidoRepository) {

    suspend operator fun invoke(entidad: Pedido): Int {

        return repository.registrar(entidad)

    }

}