package pe.pcs.retrofitmaestrodetalle.domain.model

import pe.pcs.retrofitmaestrodetalle.data.model.PedidoModel

data class Pedido(
    var id: Int = 0,
    var fecha: String = "",
    var cliente: String = "",
    var estado: String = "",
    var total: Double = 0.0,
    var detalles: List<DetallePedido>? = emptyList()
)

fun PedidoModel.toDomain() = Pedido(
    id = id,
    fecha = fecha,
    cliente = cliente,
    estado = estado,
    total = total,
    detalles = detalles?.map {
        it.toDomain()
    }
)