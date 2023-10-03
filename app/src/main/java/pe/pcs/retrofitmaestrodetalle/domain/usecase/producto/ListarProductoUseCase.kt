package pe.pcs.retrofitmaestrodetalle.domain.usecase.producto

import pe.pcs.retrofitmaestrodetalle.core.ResponseStatus
import pe.pcs.retrofitmaestrodetalle.core.makeCall
import pe.pcs.retrofitmaestrodetalle.domain.model.Producto
import pe.pcs.retrofitmaestrodetalle.domain.repository.ProductoRepository
import javax.inject.Inject

class ListarProductoUseCase @Inject constructor(val repository: ProductoRepository) {

    suspend operator fun invoke(dato: String): ResponseStatus<List<Producto>> {

        return makeCall {
            repository.listar(dato)
        }

    }

}