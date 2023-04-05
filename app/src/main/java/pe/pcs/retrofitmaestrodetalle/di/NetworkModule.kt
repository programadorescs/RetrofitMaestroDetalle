package pe.pcs.retrofitmaestrodetalle.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pe.pcs.retrofitmaestrodetalle.data.route.PedidoRoute
import pe.pcs.retrofitmaestrodetalle.data.route.ProductoRoute
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /*
    Esta función retornará Retrofit para ser inyectado en cualquier parte
     */
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.18.4:3000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /*
    Dagger hilt inyectará automáticamente Retrofit (provideRetrofit) en la función
    fun providerProducto(retrofit: Retrofit)
     */
    @Singleton
    @Provides
    fun provideProducto(retrofit: Retrofit): ProductoRoute {
        return retrofit.create(ProductoRoute::class.java)
    }

    @Singleton
    @Provides
    fun providePedido(retrofit: Retrofit): PedidoRoute {
        return retrofit.create(PedidoRoute::class.java)
    }

}