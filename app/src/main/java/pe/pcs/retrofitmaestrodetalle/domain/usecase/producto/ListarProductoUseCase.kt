package pe.pcs.retrofitmaestrodetalle.domain.usecase.producto

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import pe.pcs.retrofitmaestrodetalle.domain.ResponseStatus
import pe.pcs.retrofitmaestrodetalle.domain.makeCall
import pe.pcs.retrofitmaestrodetalle.data.repository.ProductoRepository
import pe.pcs.retrofitmaestrodetalle.domain.model.Producto
import pe.pcs.retrofitmaestrodetalle.domain.model.toDomain
import javax.inject.Inject

class ListarProductoUseCase @Inject constructor(val repository: ProductoRepository) {

    suspend operator fun invoke(dato: String): ResponseStatus<List<Producto>> {

        return makeCall {
            repository.listar(dato).body()?.data?.map {
                it.toDomain()
            } ?: listOf()
        }

    }

/*    suspend operator fun invoke(dato: String): Flow<List<Producto>?> {
        val response = repository.listar(dato)

        if(response.body()?.isSuccess == true)
            return flowOf(response.body()?.data?.map {
                it.toDomain()
            })

        return flowOf(null)
    }*/

}