# Maestro-Detalle usando Retrofit, patrón MVVM e inyección de dependencia con Dagger Hilt
Este es un ejemplo de una aplicación de Maestro-Detalle que utiliza Retrofit para consumir una API, el patrón MVVM para separar la lógica de la vista y la inyección de dependencia con Dagger Hilt para facilitar la creación y gestión de objetos.

## Requisitos

- Android Studio Giraffe | 2022.3.1 Patch 1 o superior.
- Android Gradle Plugin Version 8.1.2
- Gradle Version 8.0
- Kotlin 1.9.10 o superior.
- Descargar o clonar el repositorio de la apirest implementada en NodeJs (https://github.com/programadorescs/MaestroDetalleNodeJs) a su pc local, allí encontrará el script de la base de datos para Postgresql necesario para la api, asimismo deberá de ejecutar la apirest en su pc local para que pueda comunicarse la app móvil y consumir los datos.

## Dependencias

- Retrofit: Para el consumo de la api.
- ViewModel y LiveData: Para la implementación del patrón MVVM.
- Dagger Hilt: Para la inyección de dependencias.
- Google Admob: Para mostrar publicidad a pantalla completa.

## Estructura del proyecto

- core: Contiene las clases comunes para la implementación de mensajes, fechas, publicidad (admob) y demas utilidades.
- data: Contiene las clases e interfaces para el consumo de la api.
- di: Contiene las clases para la configuración de Dagger Hilt.
- ui: Contiene las clases para la implementación de la interfaz de usuario, incluyendo los Fragments y los ViewModels.

## Configuración del proyecto

- Crea un nuevo proyecto de Android en Android Studio.
- Agrega las dependencias de Retrofit y Dagger Hilt al archivo build.gradle del proyecto y modulo.
- Configura la clase de aplicación para usar Dagger Hilt agregando la anotación @HiltAndroidApp:

```kotlin
@HiltAndroidApp
class RetrofitMaestroDetalleApp : Application() {
    // ...
}
```

- Crear una interfaz que represente la API a la que se va a consumir, por ejemplo:

```kotlin
interface ProductoApi {

    @GET("producto/listar")
    suspend fun listar(): Response<ListaProductoApiResponse>

    @GET("producto/listarPorDescripcion/{dato}")
    suspend fun listarPorNombre(@Path("dato") dato: String): Response<ListaProductoApiResponse>

    @POST("producto/registrar")
    suspend fun registrar(
        @Body entidad: ProductoModel
    ): Response<DefaultIntResponse>

    @PUT("producto/actualizar")
    suspend fun actualizar(
        @Body entidad: ProductoModel
    ): Response<DefaultIntResponse>

    @DELETE("producto/eliminar/{id}")
    suspend fun eliminar(@Path("id") id: Long): Response<DefaultIntResponse>

}
```

- Crear un objeto para configurar Retrofit y proporcionar un proveedor para ProductoApi y PedidoApi:

```kotlin
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
    fun provideProducto(retrofit: Retrofit): ProductoApi {
        return retrofit.create(ProductoApi::class.java)
    }

    @Singleton
    @Provides
    fun providePedido(retrofit: Retrofit): PedidoApi {
        return retrofit.create(PedidoApi::class.java)
    }

}
```

- Usa la anotación @Inject para inyectar ProductoApi en una clase como el repositorio:

```kotlin
class ProductoRepository @Inject constructor(
    private val api: ProductoApi
) {

}
```

- En los casos de usos inyectaremos el repositorio:

```kotlin
class ListarProductoUseCase @Inject constructor(val repository: ProductoRepository) {

}
```

- En los viewModel se usará la anotación @HiltViewModel y @Inject constructor para inyectar los casos de usos:

```kotlin
@HiltViewModel
class ProductoViewModel @Inject constructor(
    private val listarUseCase: ListarProductoUseCase,
    private val registrarUseCase: RegistrarProductoUseCase,
    private val actualizarUseCase: ActualizarProductoUseCase,
    private val eliminarUseCase: EliminarProductoUseCase
) : ViewModel() {

}
```

- Agrega la anotación @AndroidEntryPoint a la actividad principal y en los fragment para habilitar la inyección de dependencia:
```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    // ...
}

@AndroidEntryPoint
class ProductoFragment : Fragment() {
    // ...
}
```

## Imagenes de los endpoints

### Listar todos los productos
![Image text](https://github.com/programadorescs/RetrofitMaestroDetalle/blob/master/app/src/main/assets/listarProductos.png)

### Listar productos por descripcion
![Image text](https://github.com/programadorescs/RetrofitMaestroDetalle/blob/master/app/src/main/assets/listarProductoPorDescripcion.png)

### Registrar producto
![Image text](https://github.com/programadorescs/RetrofitMaestroDetalle/blob/master/app/src/main/assets/registrarProducto.png)

### Actualizar producto
![Image text](https://github.com/programadorescs/RetrofitMaestroDetalle/blob/master/app/src/main/assets/actualizarProducto.png)

### Eliminar producto
![Image text](https://github.com/programadorescs/RetrofitMaestroDetalle/blob/master/app/src/main/assets/eliminarProducto.png)

### Listar pedidos por fechas
![Image text](https://github.com/programadorescs/RetrofitMaestroDetalle/blob/master/app/src/main/assets/pedidoPorFecha.png)

### Listar detalle de un pedido
![Image text](https://github.com/programadorescs/RetrofitMaestroDetalle/blob/master/app/src/main/assets/detalleDeUnPedido.png)

### Registrar pedido
![Image text](https://github.com/programadorescs/RetrofitMaestroDetalle/blob/master/app/src/main/assets/registrarPedido.png)

### Anular pedido
![Image text](https://github.com/programadorescs/RetrofitMaestroDetalle/blob/master/app/src/main/assets/anularPedido.png)


## Imagenes de la app

### Menú principal
![Image text](https://github.com/programadorescs/RetrofitMaestroDetalle/blob/master/app/src/main/assets/Screenshot_20230406_112206_pe.pcs.retrofitmaestrodetalle.jpg)

### Catálogo de productos para realizar pedidos

![Image text](https://github.com/programadorescs/RetrofitMaestroDetalle/blob/master/app/src/main/assets/Screenshot_20230406_112235_pe.pcs.retrofitmaestrodetalle.jpg)

### Indicar la cantidad y/o modificar el precio

![Image text](https://github.com/programadorescs/RetrofitMaestroDetalle/blob/master/app/src/main/assets/Screenshot_20230406_112246_pe.pcs.retrofitmaestrodetalle.jpg)

### Lista de productos a confirmar, tiene la opcion de agregar un nombre de cliente
![Image text](https://github.com/programadorescs/RetrofitMaestroDetalle/blob/master/app/src/main/assets/Screenshot_20230406_112303_pe.pcs.retrofitmaestrodetalle.jpg)

### Lista de productos
![Image text](https://github.com/programadorescs/RetrofitMaestroDetalle/blob/master/app/src/main/assets/Screenshot_20230406_112316_pe.pcs.retrofitmaestrodetalle.jpg)

### Registro o actualizacion de un producto
![Image text](https://github.com/programadorescs/RetrofitMaestroDetalle/blob/master/app/src/main/assets/Screenshot_20230406_112322_pe.pcs.retrofitmaestrodetalle.jpg)

### Reporte de pedidos según fechas
![Image text](https://github.com/programadorescs/RetrofitMaestroDetalle/blob/master/app/src/main/assets/Screenshot_20230406_112329_pe.pcs.retrofitmaestrodetalle.jpg)

### Muestra el detalle de un pedido realizado
![Image text](https://github.com/programadorescs/RetrofitMaestroDetalle/blob/master/app/src/main/assets/Screenshot_20230406_112349_pe.pcs.retrofitmaestrodetalle.jpg)

### Acerca de
![Image text](https://github.com/programadorescs/RetrofitMaestroDetalle/blob/master/app/src/main/assets/Screenshot_20230406_112357_pe.pcs.retrofitmaestrodetalle.jpg)

## Conclusiones

La combinación de estas tecnologías permite una arquitectura más escalable, mantenible y testeable para nuestras aplicaciones de Android. Retrofit se encarga de abstraer el proceso de llamadas a la API, Dagger Hilt facilita la inyección de dependencia para la creación y gestión de objetos y el patrón MVVM permite una separación clara entre la lógica de la vista y el modelo de datos.
