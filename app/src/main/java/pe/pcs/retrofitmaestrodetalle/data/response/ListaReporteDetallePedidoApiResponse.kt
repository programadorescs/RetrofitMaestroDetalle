package pe.pcs.retrofitmaestrodetalle.data.response

import pe.pcs.retrofitmaestrodetalle.data.model.ReporteDetallePedidoModel

data class ListaReporteDetallePedidoApiResponse(
    val isSuccess: Boolean,
    val message: String,
    val data: List<ReporteDetallePedidoModel>
)
