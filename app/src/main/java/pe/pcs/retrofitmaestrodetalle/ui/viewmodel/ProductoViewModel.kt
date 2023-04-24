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
import pe.pcs.retrofitmaestrodetalle.data.model.ProductoModel
import pe.pcs.retrofitmaestrodetalle.data.repository.ProductoRepository
import pe.pcs.retrofitmaestrodetalle.domain.ResponseStatus
import pe.pcs.retrofitmaestrodetalle.domain.model.Producto
import pe.pcs.retrofitmaestrodetalle.domain.usecase.pedido.RegistrarPedidoUseCase
import pe.pcs.retrofitmaestrodetalle.domain.usecase.producto.ActualizarProductoUseCase
import pe.pcs.retrofitmaestrodetalle.domain.usecase.producto.EliminarProductoUseCase
import pe.pcs.retrofitmaestrodetalle.domain.usecase.producto.ListarProductoUseCase
import pe.pcs.retrofitmaestrodetalle.domain.usecase.producto.RegistrarProductoUseCase
import javax.inject.Inject

@HiltViewModel
class ProductoViewModel @Inject constructor(
    private val listarUseCase: ListarProductoUseCase,
    private val registrarUseCase: RegistrarProductoUseCase,
    private val actualizarUseCase: ActualizarProductoUseCase,
    private val eliminarUseCase: EliminarProductoUseCase
) : ViewModel() {

    private val _lista = MutableLiveData<List<Producto>?>()
    val lista: LiveData<List<Producto>?> = _lista

    private var _item = MutableLiveData<Producto?>()
    val item: LiveData<Producto?> = _item

    private val _status = MutableLiveData<ResponseStatus<List<Producto>>?>()
    val status: LiveData<ResponseStatus<List<Producto>>?> = _status

    private val _statusInt = MutableLiveData<ResponseStatus<Int>?>()
    val statusInt: LiveData<ResponseStatus<Int>?> = _statusInt

    private fun handleResponseStatus(responseStatus: ResponseStatus<List<Producto>>) {
        if (responseStatus is ResponseStatus.Success) {
            _lista.value = responseStatus.data
        }

        _status.value = responseStatus
    }

    private fun handleResponseStatusInt(responseStatus: ResponseStatus<Int>) {
        _statusInt.value = responseStatus
    }

    fun resetApiResponseStatus() {
        _status.value = null
    }

    fun resetApiResponseStatusInt() {
        _statusInt.value = null
    }

    fun setItem(entidad: Producto?) {
        _item.postValue(entidad)
    }

    fun listar(dato: String) {
        viewModelScope.launch {
            _status.value = ResponseStatus.Loading()
            handleResponseStatus(listarUseCase(dato))
        }
    }

    fun grabar(entidad: Producto) {

        viewModelScope.launch {
            _statusInt.value = ResponseStatus.Loading()

            if (entidad.id == 0)
                handleResponseStatusInt(registrarUseCase(entidad))
            else
                handleResponseStatusInt(actualizarUseCase(entidad))

            handleResponseStatus(listarUseCase(""))
        }

    }

    fun eliminar(id: Long) {

        viewModelScope.launch {
            _statusInt.value = ResponseStatus.Loading()

            handleResponseStatusInt(eliminarUseCase(id))
            handleResponseStatus(listarUseCase(""))
        }

    }
}