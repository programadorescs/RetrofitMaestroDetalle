package pe.pcs.retrofitmaestrodetalle.data.response

import com.google.gson.annotations.SerializedName

data class DefaultIntResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Int
)
