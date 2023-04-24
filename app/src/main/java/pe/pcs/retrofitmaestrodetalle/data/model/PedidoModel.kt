package pe.pcs.retrofitmaestrodetalle.data.model

import com.google.gson.annotations.SerializedName
import pe.pcs.retrofitmaestrodetalle.domain.model.Pedido

data class PedidoModel(
    @SerializedName("id") var id: Int = 0,
    @SerializedName("fecha") var fecha: String = "",
    @SerializedName("cliente") var cliente: String = "",
    @SerializedName("estado") var estado: String = "",
    @SerializedName("total") var total: Double = 0.0,
    @SerializedName("detalles") var detalles: List<DetallePedidoModel>? = emptyList()
)

fun Pedido.toDatabase() = PedidoModel(
    id = id,
    fecha = fecha,
    cliente = cliente,
    estado = estado,
    total = total,
    detalles = detalles?.map {
        it.toDatabase()
    }
)
