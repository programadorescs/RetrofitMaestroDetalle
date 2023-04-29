package pe.pcs.retrofitmaestrodetalle.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.pcs.retrofitmaestrodetalle.core.ResponseStatus
import pe.pcs.retrofitmaestrodetalle.domain.model.DetallePedido
import pe.pcs.retrofitmaestrodetalle.domain.model.Pedido
import pe.pcs.retrofitmaestrodetalle.domain.model.Producto
import pe.pcs.retrofitmaestrodetalle.domain.usecase.pedido.RegistrarPedidoUseCase
import pe.pcs.retrofitmaestrodetalle.domain.usecase.producto.ListarProductoUseCase
import javax.inject.Inject

@HiltViewModel
class PedidoViewModel @Inject constructor(
    private val registarPedidoUseCase: RegistrarPedidoUseCase,
    private val listarProductoUseCase: ListarProductoUseCase
) : ViewModel() {

    var listaProducto = MutableLiveData<List<Producto>?>(null)

    private var _listaCarrito = MutableLiveData<MutableList<DetallePedido>>()
    var listaCarrito: MutableLiveData<MutableList<DetallePedido>> = _listaCarrito

    private var _totalItem = MutableLiveData<Int>()
    var totalItem: LiveData<Int> = _totalItem

    private var _totalImporte = MutableLiveData<Double>()
    var totalImporte: LiveData<Double> = _totalImporte

    private var _itemProducto = MutableLiveData<Producto?>()
    val itemProducto: LiveData<Producto?> = _itemProducto

    var mErrorStatus = MutableLiveData<String?>()

    private val _status = MutableLiveData<ResponseStatus<List<Producto>>?>()
    val status: LiveData<ResponseStatus<List<Producto>>?> = _status

    private val _statusInt = MutableLiveData<ResponseStatus<Int>?>()
    val statusInt: LiveData<ResponseStatus<Int>?> = _statusInt

    private fun handleResponseStatus(responseStatus: ResponseStatus<List<Producto>>) {
        if (responseStatus is ResponseStatus.Success) {
            listaProducto.value = responseStatus.data
        }

        _status.value = responseStatus
    }

    private fun handleResponseStatusInt(responseStatus: ResponseStatus<Int>) {
        _statusInt.value = responseStatus
    }

    init {
        _listaCarrito.value = mutableListOf()
    }

    fun resetApiResponseStatus() {
        _status.value = null
    }

    fun resetApiResponseStatusInt() {
        _statusInt.value = null
    }

    // Para el item seleccionado
    fun setItemProducto(item: Producto?) {
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

        val entidad = DetallePedido().apply {
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

    fun quitarProductoCarrito(item: DetallePedido) {
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

    fun setAumentarCantidadProducto(item: DetallePedido) {
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

    fun setDisminuirCantidadProducto(item: DetallePedido) {
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

        viewModelScope.launch {
            _status.value = ResponseStatus.Loading()
            handleResponseStatus(listarProductoUseCase(dato))
        }
    }

    fun registrar(entidad: Pedido) {

        viewModelScope.launch {
            _statusInt.value = ResponseStatus.Loading()
            handleResponseStatusInt(registarPedidoUseCase(entidad))
        }
    }

}