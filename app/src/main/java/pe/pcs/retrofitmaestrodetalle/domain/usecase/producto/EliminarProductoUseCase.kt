package pe.pcs.retrofitmaestrodetalle.domain.usecase.producto

import pe.pcs.retrofitmaestrodetalle.domain.repository.ProductoRepository
import javax.inject.Inject

class EliminarProductoUseCase @Inject constructor(private val repository: ProductoRepository) {

    suspend operator fun invoke(id: Long): Int {
        return repository.eliminar(id)
    }

}