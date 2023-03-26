package pe.pcs.retrofitmaestrodetalle.data.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pe.pcs.retrofitmaestrodetalle.core.RetrofitClient
import pe.pcs.retrofitmaestrodetalle.data.model.ProductoModel
import pe.pcs.retrofitmaestrodetalle.data.model.ResponseHttp
import pe.pcs.retrofitmaestrodetalle.data.route.ProductoRoute
import retrofit2.Response

class ProductoService {

    private val retrofit = RetrofitClient.getRetrofit()

    suspend fun listar(dato: String): Response<ResponseHttp> {

        return withContext(Dispatchers.IO) {
            if(dato.isEmpty())
                retrofit.create(ProductoRoute::class.java).listar()
            else
                retrofit.create(ProductoRoute::class.java).listarPorNombre(dato)
        }

    }

    suspend fun registrar(entidad: ProductoModel): Response<ResponseHttp> {

        return withContext(Dispatchers.IO) {
            retrofit.create(ProductoRoute::class.java).registrar(entidad)
        }

    }

    suspend fun actualizar(entidad: ProductoModel): Response<ResponseHttp> {

        return withContext(Dispatchers.IO) {
            retrofit.create(ProductoRoute::class.java).actualizar(entidad)
        }

    }

    suspend fun eliminar(id: Long): Response<ResponseHttp> {

        return withContext(Dispatchers.IO) {
            retrofit.create(ProductoRoute::class.java).eliminar(id)
        }

    }

}