package pe.pcs.retrofitmaestrodetalle.data.response

import pe.pcs.retrofitmaestrodetalle.data.model.ProductoModel

data class ListaProductoApiResponse(
    val isSuccess: Boolean,
    val message: String,
    val data: List<ProductoModel>
)
