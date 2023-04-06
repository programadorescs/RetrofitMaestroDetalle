package pe.pcs.retrofitmaestrodetalle.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pe.pcs.retrofitmaestrodetalle.data.model.DetallePedidoModel
import pe.pcs.retrofitmaestrodetalle.data.model.PedidoModel
import pe.pcs.retrofitmaestrodetalle.data.model.ProductoModel
import pe.pcs.retrofitmaestrodetalle.data.model.ResponseHttp
import pe.pcs.retrofitmaestrodetalle.data.repository.PedidoRepository
import pe.pcs.retrofitmaestrodetalle.data.repository.ProductoRepository
import javax.inject.Inject

@HiltViewModel
class PedidoViewModel @Inject constructor(
    private val repoPedido: PedidoRepository,
    private val repoProducto: ProductoRepository
) : ViewModel() {

    var listaProducto = MutableLiveData<List<ProductoModel>>()

    private var _listaCarrito = MutableLiveData<MutableList<DetallePedidoModel>>()
    var listaCarrito: MutableLiveData<MutableList<DetallePedidoModel>> = _listaCarrito

    private var _totalItem = MutableLiveData<Int>()
    var totalItem: LiveData<Int> = _totalItem

    private var _totalImporte = MutableLiveData<Double>()
    var totalImporte: LiveData<Double> = _totalImporte

    private var _itemProducto = MutableLiveData<ProductoModel?>()
    val itemProducto: LiveData<ProductoModel?> = _itemProducto

    private val _progressBar = MutableLiveData<Boolean>()
    var progressBar: LiveData<Boolean> = _progressBar

    var mErrorStatus = MutableLiveData<String?>()

    var operacionExitosa = MutableLiveData<ResponseHttp?>()

    init {
        _listaCarrito.value = mutableListOf()
    }

    // Para el item seleccionado
    fun setItemProducto(item: ProductoModel?) {
        _itemProducto.value = item
    }

    // Para el item seleccionado
    fun agregarProductoCarrito(cantidad: Int, precio: Double) {
        if (cantidad == 0 || precio == 0.0) return

        if (itemProducto.value == null) return

        listaCarrito.value?.forEach {
            if (it.idproducto == itemProducto.value?.id) {
                setItemProducto(null)
                mErrorStatus.postValue("Ya existe este elemento en su lista...")
                return
            }
        }

        val entidad = DetallePedidoModel().apply {
            idproducto = itemProducto.value!!.id
            this.descripcion = itemProducto.value!!.descripcion
            this.cantidad = cantidad
            this.precio = precio
            this.importe = cantidad * precio
        }

        setItemProducto(null)

        _listaCarrito.value?.add(entidad)

        _totalItem.postValue(
            listaCarrito.value?.sumOf { it.cantidad }
        )

        _totalImporte.postValue(
            listaCarrito.value?.sumOf { it.cantidad * it.precio }
        )
    }

    fun quitarProductoCarrito(item: DetallePedidoModel) {
        _listaCarrito.value?.remove(item)

        _totalItem.postValue(
            listaCarrito.value?.sumOf { it.cantidad }
        )

        _totalImporte.postValue(
            listaCarrito.value?.sumOf { it.cantidad * it.precio }
        )

        listaCarrito.postValue(_listaCarrito.value)
    }

    fun limpiarCarrito() {
        _listaCarrito.value = mutableListOf()

        _totalItem.postValue(
            listaCarrito.value?.sumOf { it.cantidad }
        )

        _totalImporte.postValue(
            listaCarrito.value?.sumOf { it.cantidad * it.precio }
        )
    }

    fun listarCarrito() {
        listaCarrito.postValue(_listaCarrito.value)
    }

    fun setAumentarCantidadProducto(item: DetallePedidoModel) {
        _listaCarrito.value?.forEach {
            if (it.idproducto == item.idproducto) {
                it.cantidad++
                it.importe = it.cantidad * it.precio
            }
        }

        _totalItem.postValue(
            listaCarrito.value?.sumOf { it.cantidad }
        )

        _totalImporte.postValue(
            listaCarrito.value?.sumOf { it.cantidad * it.precio }
        )

        listaCarrito.postValue(_listaCarrito.value)
    }

    fun setDisminuirCantidadProducto(item: DetallePedidoModel) {
        // Recorre la lista para disminuir la cantidad del producto seleccionado
        _listaCarrito.value?.forEach {
            if (it.idproducto == item.idproducto && it.cantidad > 1) {
                it.cantidad--
                it.importe = it.cantidad * it.precio
            }
        }

        // Obtiene la cantidad de producto en el carrito
        _totalItem.postValue(
            listaCarrito.value?.sumOf { it.cantidad }
        )

        // Suma el importe de los productos para obtener el total a pagar
        _totalImporte.postValue(
            listaCarrito.value?.sumOf { it.cantidad * it.precio }
        )

        listaCarrito.postValue(_listaCarrito.value)
    }

    fun listarProducto(dato: String) {
        _progressBar.postValue(true)

        viewModelScope.launch {
            try {
                listaProducto.postValue(
                    Gson().fromJson(
                        repoProducto.listar(dato).body()!!.data,
                        object : TypeToken<List<ProductoModel>>() {}.type
                    )
                )
            } catch (e: Exception) {
                mErrorStatus.postValue(e.message)
            } finally {
                _progressBar.postValue(false)
            }
        }
    }

    fun registrar(entidad: PedidoModel) {
        _progressBar.postValue(true)

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val rpta = repoPedido.registrar(entidad)
                    rpta
                } catch (e: Exception) {
                    mErrorStatus.postValue(e.message)
                    null
                } finally {
                    _progressBar.postValue(false)
                }
            }

            operacionExitosa.postValue(result?.body())
        }
    }

}