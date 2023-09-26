package pe.pcs.retrofitmaestrodetalle.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.pcs.retrofitmaestrodetalle.core.ResponseStatus
import pe.pcs.retrofitmaestrodetalle.domain.model.Producto
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

    private val _stateLista = MutableLiveData<ResponseStatus<List<Producto>>>()
    val stateLista: LiveData<ResponseStatus<List<Producto>>> = _stateLista

    private val _stateGrabar = MutableLiveData<ResponseStatus<Int>>()
    val stateGrabar: LiveData<ResponseStatus<Int>> = _stateGrabar

    private val _stateEliminar = MutableLiveData<ResponseStatus<Int>>()
    val stateEliminar: LiveData<ResponseStatus<Int>> = _stateEliminar

    private fun handleStateLista(responseStatus: ResponseStatus<List<Producto>>) {
        if (responseStatus is ResponseStatus.Success)
            _lista.value = responseStatus.data

        _stateLista.value = responseStatus
    }

    private fun handleStateGrabar(responseStatus: ResponseStatus<Int>) {
        _stateGrabar.value = responseStatus
    }

    private fun handleStateEliminar(responseStatus: ResponseStatus<Int>) {
        _stateEliminar.value = responseStatus
    }

    fun resetStateGrabar() {
        _stateGrabar.value = ResponseStatus.Success(0)
    }

    fun resetStateEliminar() {
        _stateEliminar.value = ResponseStatus.Success(0)
    }

    fun setItem(entidad: Producto?) {
        _item.postValue(entidad)
    }

    fun listar(dato: String) {
        viewModelScope.launch {
            _stateLista.value = ResponseStatus.Loading()
            handleStateLista(listarUseCase(dato))
        }
    }

    fun grabar(entidad: Producto) {

        viewModelScope.launch {
            _stateGrabar.value = ResponseStatus.Loading()

            if (entidad.id == 0)
                handleStateGrabar(registrarUseCase(entidad))
            else
                handleStateGrabar(actualizarUseCase(entidad))

            handleStateLista(listarUseCase(""))
        }

    }

    fun eliminar(id: Long) {

        viewModelScope.launch {
            _stateEliminar.value = ResponseStatus.Loading()

            handleStateEliminar(eliminarUseCase(id))
            handleStateLista(listarUseCase(""))
        }

    }
}