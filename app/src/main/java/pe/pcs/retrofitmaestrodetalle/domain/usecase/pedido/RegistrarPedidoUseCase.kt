package pe.pcs.retrofitmaestrodetalle.domain.usecase.pedido

import pe.pcs.retrofitmaestrodetalle.domain.ResponseStatus
import pe.pcs.retrofitmaestrodetalle.domain.makeCall
import pe.pcs.retrofitmaestrodetalle.data.model.toDatabase
import pe.pcs.retrofitmaestrodetalle.data.repository.PedidoRepository
import pe.pcs.retrofitmaestrodetalle.domain.model.Pedido
import javax.inject.Inject

class RegistrarPedidoUseCase @Inject constructor(private val repository: PedidoRepository) {

    suspend operator fun invoke(entidad: Pedido): ResponseStatus<Int> {

        return makeCall {
            repository.registrar(entidad.toDatabase()).body()?.data ?: 0
        }

    }

}