package pe.pcs.retrofitmaestrodetalle.domain.model

import pe.pcs.retrofitmaestrodetalle.data.model.ProductoModel

data class Producto(
    var id: Int = 0,
    var descripcion: String = "",
    var costo: Double = 0.0,
    var precio: Double = 0.0
)

fun ProductoModel.toDomain() = Producto(
    id = id,
    descripcion = descripcion,
    costo = costo,
    precio = precio
)