package pe.pcs.retrofitmaestrodetalle.domain.usecase.pedido

import pe.pcs.retrofitmaestrodetalle.core.ResponseStatus
import pe.pcs.retrofitmaestrodetalle.core.makeCall
import pe.pcs.retrofitmaestrodetalle.data.repository.PedidoRepository
import pe.pcs.retrofitmaestrodetalle.domain.model.Pedido
import pe.pcs.retrofitmaestrodetalle.domain.model.toDomain
import javax.inject.Inject

class ListarPedidoPorFechaUseCase @Inject constructor(private val repository: PedidoRepository) {

    suspend operator fun invoke(desde: String, hasta: String): ResponseStatus<List<Pedido>> {

        return makeCall {
            repository.listarPorFecha(desde, hasta).body()?.data?.map {
                it.toDomain()
            } ?: listOf()
        }

    }

}