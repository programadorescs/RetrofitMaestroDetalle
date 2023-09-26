package pe.pcs.retrofitmaestrodetalle.data.repository

import pe.pcs.retrofitmaestrodetalle.data.api.ProductoApi
import pe.pcs.retrofitmaestrodetalle.data.model.ProductoModel
import pe.pcs.retrofitmaestrodetalle.data.response.DefaultIntResponse
import pe.pcs.retrofitmaestrodetalle.data.response.ListaProductoApiResponse
import retrofit2.Response
import javax.inject.Inject

class ProductoRepository @Inject constructor(private val api: ProductoApi) {

    suspend fun listar(dato: String): Response<ListaProductoApiResponse> {

        return if (dato.isEmpty())
            api.listar()
        else
            api.listarPorNombre(dato)

    }

    suspend fun registrar(entidad: ProductoModel): Response<DefaultIntResponse> {

        return api.registrar(entidad)

    }

    suspend fun actualizar(entidad: ProductoModel): Response<DefaultIntResponse> {

        return api.actualizar(entidad)

    }

    suspend fun eliminar(id: Long): Response<DefaultIntResponse> {

        return api.eliminar(id)

    }

}