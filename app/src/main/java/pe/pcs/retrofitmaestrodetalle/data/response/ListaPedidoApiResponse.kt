package pe.pcs.retrofitmaestrodetalle.data.response

import pe.pcs.retrofitmaestrodetalle.data.model.PedidoModel

data class ListaPedidoApiResponse(
    val isSuccess: Boolean,
    val message: String,
    val data: List<PedidoModel>
)
