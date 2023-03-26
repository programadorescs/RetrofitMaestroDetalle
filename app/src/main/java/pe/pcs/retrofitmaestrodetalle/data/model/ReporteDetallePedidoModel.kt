package pe.pcs.retrofitmaestrodetalle.data.model

data class ReporteDetallePedidoModel(
    var descripcion: String = "",
    var cantidad: Int = 0,
    var precio: Double = 0.0,
    var importe: Double = 0.0
)
