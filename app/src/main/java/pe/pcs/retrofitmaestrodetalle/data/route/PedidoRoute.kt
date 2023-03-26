package pe.pcs.retrofitmaestrodetalle.data.route

import pe.pcs.retrofitmaestrodetalle.data.model.PedidoModel
import pe.pcs.retrofitmaestrodetalle.data.model.ResponseHttp
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PedidoRoute {

    @POST("pedido/registrar")
    suspend fun registrar(
        @Body entidad: PedidoModel
    ) : Response<ResponseHttp>
}