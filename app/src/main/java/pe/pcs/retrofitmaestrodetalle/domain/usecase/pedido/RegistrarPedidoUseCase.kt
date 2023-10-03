package pe.pcs.retrofitmaestrodetalle.domain.usecase.pedido

import pe.pcs.retrofitmaestrodetalle.core.ResponseStatus
import pe.pcs.retrofitmaestrodetalle.core.makeCall
import pe.pcs.retrofitmaestrodetalle.domain.model.Pedido
import pe.pcs.retrofitmaestrodetalle.domain.repository.PedidoRepository
import javax.inject.Inject

class RegistrarPedidoUseCase @Inject constructor(private val repository: PedidoRepository) {

    suspend operator fun invoke(entidad: Pedido): ResponseStatus<Int> {

        return makeCall {
            repository.registrar(entidad)
        }

    }

}