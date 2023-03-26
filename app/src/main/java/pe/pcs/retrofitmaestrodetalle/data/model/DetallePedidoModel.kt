package pe.pcs.retrofitmaestrodetalle.data.model

import com.google.gson.annotations.SerializedName

data class DetallePedidoModel(
    @SerializedName("id") var id: Int = 0,
    @SerializedName("cantidad") var cantidad: Int = 0,
    @SerializedName("costo") var costo: Double = 0.0,
    @SerializedName("precio") var precio: Double = 0.0,
    @SerializedName("importe") var importe: Double = 0.0,
    @SerializedName("descripcion") var descripcion: String = ""
)
