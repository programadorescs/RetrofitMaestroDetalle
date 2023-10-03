package pe.pcs.retrofitmaestrodetalle.data.repository

import pe.pcs.retrofitmaestrodetalle.data.api.ProductoApi
import pe.pcs.retrofitmaestrodetalle.data.model.toDatabase
import pe.pcs.retrofitmaestrodetalle.domain.model.Producto
import pe.pcs.retrofitmaestrodetalle.domain.model.toDomain
import pe.pcs.retrofitmaestrodetalle.domain.repository.ProductoRepository
import javax.inject.Inject

class ProductoRepositoryImpl @Inject constructor(private val api: ProductoApi): ProductoRepository {

    override suspend fun listar(dato: String): List<Producto> {
        return if (dato.isEmpty())
            api.listar().data.map { it.toDomain() }
        else
            api.listarPorNombre(dato).data.map { it.toDomain() }
    }

    override suspend fun registrar(entidad: Producto): Int {
        return api.registrar(entidad.toDatabase()).data
    }

    override suspend fun actualizar(entidad: Producto): Int {
        return api.actualizar(entidad.toDatabase()).data
    }

    override suspend fun eliminar(id: Long): Int {
        return api.eliminar(id).data
    }

}