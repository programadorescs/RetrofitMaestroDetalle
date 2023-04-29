package pe.pcs.retrofitmaestrodetalle.domain.usecase.producto

import pe.pcs.retrofitmaestrodetalle.core.ResponseStatus
import pe.pcs.retrofitmaestrodetalle.core.makeCall
import pe.pcs.retrofitmaestrodetalle.data.model.toDatabase
import pe.pcs.retrofitmaestrodetalle.data.repository.ProductoRepository
import pe.pcs.retrofitmaestrodetalle.domain.model.Producto
import javax.inject.Inject

class ActualizarProductoUseCase @Inject constructor(private val repository: ProductoRepository) {

    suspend operator fun invoke(entidad: Producto): ResponseStatus<Int> {
        return makeCall {
            repository.actualizar(entidad.toDatabase()).body()?.data ?: 0
        }
    }

}