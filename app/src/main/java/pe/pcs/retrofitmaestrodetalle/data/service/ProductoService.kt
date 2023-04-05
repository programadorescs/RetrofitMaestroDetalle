package pe.pcs.retrofitmaestrodetalle.data.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pe.pcs.retrofitmaestrodetalle.data.model.ProductoModel
import pe.pcs.retrofitmaestrodetalle.data.model.ResponseHttp
import pe.pcs.retrofitmaestrodetalle.data.route.ProductoRoute
import retrofit2.Response
import javax.inject.Inject

class ProductoService @Inject constructor(private val api: ProductoRoute)  {

    suspend fun listar(dato: String): Response<ResponseHttp> {

        return withContext(Dispatchers.IO) {
            if(dato.isEmpty())
                api.listar()
            else
                api.listarPorNombre(dato)
        }

    }

    suspend fun registrar(entidad: ProductoModel): Response<ResponseHttp> {

        return withContext(Dispatchers.IO) {
            api.registrar(entidad)
        }

    }

    suspend fun actualizar(entidad: ProductoModel): Response<ResponseHttp> {

        return withContext(Dispatchers.IO) {
            api.actualizar(entidad)
        }

    }

    suspend fun eliminar(id: Long): Response<ResponseHttp> {

        return withContext(Dispatchers.IO) {
            api.eliminar(id)
        }

    }

}