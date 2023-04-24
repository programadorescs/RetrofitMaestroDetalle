package pe.pcs.retrofitmaestrodetalle.domain.usecase.producto

import pe.pcs.retrofitmaestrodetalle.domain.ResponseStatus
import pe.pcs.retrofitmaestrodetalle.domain.makeCall
import pe.pcs.retrofitmaestrodetalle.data.model.toDatabase
import pe.pcs.retrofitmaestrodetalle.data.repository.ProductoRepository
import pe.pcs.retrofitmaestrodetalle.domain.model.Producto
import javax.inject.Inject

class RegistrarProductoUseCase @Inject constructor(private val repository: ProductoRepository) {

    suspend operator fun invoke(entidad: Producto): ResponseStatus<Int> {
        return makeCall {
            repository.registrar(entidad.toDatabase()).body()?.data ?: 0
        }
    }

}