package pe.pcs.retrofitmaestrodetalle.domain.usecase.producto

import pe.pcs.retrofitmaestrodetalle.domain.model.Producto
import pe.pcs.retrofitmaestrodetalle.domain.repository.ProductoRepository
import javax.inject.Inject

class ActualizarProductoUseCase @Inject constructor(private val repository: ProductoRepository) {

    suspend operator fun invoke(entidad: Producto): Int {
        return repository.actualizar(entidad)
    }

}