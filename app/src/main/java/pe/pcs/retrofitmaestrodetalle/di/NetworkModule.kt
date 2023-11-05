package pe.pcs.retrofitmaestrodetalle.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pe.pcs.retrofitmaestrodetalle.data.api.PedidoApi
import pe.pcs.retrofitmaestrodetalle.data.api.ProductoApi
import pe.pcs.retrofitmaestrodetalle.data.utils.Constants
import pe.pcs.retrofitmaestrodetalle.data.repository.PedidoRepositoryImpl
import pe.pcs.retrofitmaestrodetalle.data.repository.ProductoRepositoryImpl
import pe.pcs.retrofitmaestrodetalle.domain.repository.PedidoRepository
import pe.pcs.retrofitmaestrodetalle.domain.repository.ProductoRepository
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
            .baseUrl(Constants.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /*
    Dagger hilt inyectará automáticamente Retrofit (provideRetrofit) en la función
    fun providerProducto(retrofit: Retrofit)
     */
    @Singleton
    @Provides
    fun provideProducto(retrofit: Retrofit): ProductoApi {
        return retrofit.create(ProductoApi::class.java)
    }

    @Singleton
    @Provides
    fun providePedido(retrofit: Retrofit): PedidoApi {
        return retrofit.create(PedidoApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRepositoryProducto(api: ProductoApi): ProductoRepository {
        return ProductoRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideRepositoryPedido(api: PedidoApi): PedidoRepository {
        return PedidoRepositoryImpl(api)
    }

}