package pe.pcs.retrofitmaestrodetalle.data.response

import com.google.gson.annotations.SerializedName
import pe.pcs.retrofitmaestrodetalle.data.model.ProductoModel

data class ListaProductoApiResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<ProductoModel>
)
