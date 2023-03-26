package pe.pcs.retrofitmaestrodetalle.data.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pe.pcs.retrofitmaestrodetalle.core.RetrofitClient
import pe.pcs.retrofitmaestrodetalle.data.model.PedidoModel
import pe.pcs.retrofitmaestrodetalle.data.model.ResponseHttp
import pe.pcs.retrofitmaestrodetalle.data.route.PedidoRoute
import retrofit2.Response

class PedidoService {

    private val retrofit = RetrofitClient.getRetrofit()

    suspend fun registrar(entidad: PedidoModel): Response<ResponseHttp> {

        return withContext(Dispatchers.IO) {
            retrofit.create(PedidoRoute::class.java).registrar(entidad)
        }

    }
}