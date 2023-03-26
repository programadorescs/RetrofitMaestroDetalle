package pe.pcs.retrofitmaestrodetalle.data.model

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class ResponseHttp(
    @SerializedName("success") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: JsonArray
    //@SerializedName("data") val data: JsonObject
)