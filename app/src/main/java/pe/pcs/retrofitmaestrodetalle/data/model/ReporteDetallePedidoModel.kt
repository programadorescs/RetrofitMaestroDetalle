package pe.pcs.retrofitmaestrodetalle.data.model

import com.google.gson.annotations.SerializedName

data class ReporteDetallePedidoModel(
    @SerializedName("descripcion") var descripcion: String = "",
    @SerializedName("cantidad") var cantidad: Int = 0,
    @SerializedName("precio") var precio: Double = 0.0,
    @SerializedName("importe") var importe: Double = 0.0
)
