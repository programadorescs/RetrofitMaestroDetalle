package pe.pcs.retrofitmaestrodetalle.core

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ConstantsApp.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}