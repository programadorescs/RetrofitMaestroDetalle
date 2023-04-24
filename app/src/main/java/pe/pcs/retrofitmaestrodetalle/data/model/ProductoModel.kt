package pe.pcs.retrofitmaestrodetalle.data.model

import com.google.gson.annotations.SerializedName
import pe.pcs.retrofitmaestrodetalle.domain.model.Producto

data class ProductoModel(
    @SerializedName("id") var id: Int = 0,
    @SerializedName("descripcion") var descripcion: String = "",
    @SerializedName("costo") var costo: Double = 0.0,
    @SerializedName("precio") var precio: Double = 0.0
)

fun Producto.toDatabase() = ProductoModel(
    id = id,
    descripcion = descripcion,
    costo = costo,
    precio = precio
)
