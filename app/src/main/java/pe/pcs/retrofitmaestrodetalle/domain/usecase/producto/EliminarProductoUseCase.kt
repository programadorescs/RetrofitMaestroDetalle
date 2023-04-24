package pe.pcs.retrofitmaestrodetalle.domain.usecase.producto

import pe.pcs.retrofitmaestrodetalle.domain.ResponseStatus
import pe.pcs.retrofitmaestrodetalle.domain.makeCall
import pe.pcs.retrofitmaestrodetalle.data.repository.ProductoRepository
import javax.inject.Inject

class EliminarProductoUseCase @Inject constructor(private val repository: ProductoRepository) {

    suspend operator fun invoke(id: Long): ResponseStatus<Int> {
        return makeCall {
            repository.eliminar(id).body()?.data ?: 0
        }
    }

}