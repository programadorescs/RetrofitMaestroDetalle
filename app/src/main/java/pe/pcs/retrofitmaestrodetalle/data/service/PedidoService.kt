package pe.pcs.retrofitmaestrodetalle.data.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pe.pcs.retrofitmaestrodetalle.data.model.PedidoModel
import pe.pcs.retrofitmaestrodetalle.data.model.ResponseHttp
import pe.pcs.retrofitmaestrodetalle.data.route.PedidoRoute
import retrofit2.Response
import javax.inject.Inject

class PedidoService @Inject constructor(private val api: PedidoRoute) {

    suspend fun registrar(entidad: PedidoModel): Response<ResponseHttp> {

        return withContext(Dispatchers.IO) {
            api.registrar(entidad)
        }

    }

    suspend fun anular(id: Int): Response<ResponseHttp> {

        return withContext(Dispatchers.IO) {
            api.anular(id)
        }

    }

    suspend fun listarPorFecha(desde: String, hasta: String): Response<ResponseHttp> {

        return withContext(Dispatchers.IO) {
            api.listarPorFecha(desde, hasta)
        }

    }

    suspend fun listarDetalle(id: Int): Response<ResponseHttp> {

        return withContext(Dispatchers.IO) {
            api.listarDetalle(id)
        }

    }

}