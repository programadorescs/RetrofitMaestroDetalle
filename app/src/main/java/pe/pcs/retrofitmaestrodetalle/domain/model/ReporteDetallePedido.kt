package pe.pcs.retrofitmaestrodetalle.domain.model

import pe.pcs.retrofitmaestrodetalle.data.model.ReporteDetallePedidoModel

data class ReporteDetallePedido(
    var descripcion: String = "",
    var cantidad: Int = 0,
    var precio: Double = 0.0,
    var importe: Double = 0.0
)

fun ReporteDetallePedidoModel.toDomain() = ReporteDetallePedido(
    descripcion = descripcion,
    cantidad = cantidad,
    precio = precio,
    importe = importe
)
