package pe.pcs.retrofitmaestrodetalle.domain.usecase.producto

import pe.pcs.retrofitmaestrodetalle.core.ResponseStatus
import pe.pcs.retrofitmaestrodetalle.core.makeCall
import pe.pcs.retrofitmaestrodetalle.domain.repository.ProductoRepository
import javax.inject.Inject

class EliminarProductoUseCase @Inject constructor(private val repository: ProductoRepository) {

    suspend operator fun invoke(id: Long): ResponseStatus<Int> {
        return makeCall {
            repository.eliminar(id)
        }
    }

}