package pe.pcs.retrofitmaestrodetalle.domain.repository

import pe.pcs.retrofitmaestrodetalle.domain.model.Producto

interface ProductoRepository {

    suspend fun listar(dato: String): List<Producto>

    suspend fun registrar(entidad: Producto): Int

    suspend fun actualizar(entidad: Producto): Int

    suspend fun eliminar(id: Long): Int

}