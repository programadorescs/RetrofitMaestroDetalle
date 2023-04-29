package pe.pcs.retrofitmaestrodetalle.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.pcs.retrofitmaestrodetalle.core.ResponseStatus
import pe.pcs.retrofitmaestrodetalle.domain.model.Pedido
import pe.pcs.retrofitmaestrodetalle.domain.model.ReporteDetallePedido
import pe.pcs.retrofitmaestrodetalle.domain.usecase.pedido.AnularPedidoUseCase
import pe.pcs.retrofitmaestrodetalle.domain.usecase.pedido.ListarDetallePedidoUseCase
import pe.pcs.retrofitmaestrodetalle.domain.usecase.pedido.ListarPedidoPorFechaUseCase
import javax.inject.Inject

@HiltViewModel
class ReportePedidoViewModel @Inject constructor(
    private val anularPedidoUseCase: AnularPedidoUseCase,
    private val listarPedidoPorFechaUseCase: ListarPedidoPorFechaUseCase,
    private val listarDetallePedidoUseCase: ListarDetallePedidoUseCase
) : ViewModel() {

    private val _listaPedido = MutableLiveData<List<Pedido>?>()
    val listaPedido: LiveData<List<Pedido>?> = _listaPedido

    private val _listaDetalle = MutableLiveData<List<ReporteDetallePedido>?>()
    val listaDetalle: LiveData<List<ReporteDetallePedido>?> = _listaDetalle

    private val _itemPedido = MutableLiveData<Pedido?>()
    val itemPedido: LiveData<Pedido?> = _itemPedido

    private val _status = MutableLiveData<ResponseStatus<List<Pedido>>?>()
    val status: LiveData<ResponseStatus<List<Pedido>>?> = _status

    private val _statusDetalle = MutableLiveData<ResponseStatus<List<ReporteDetallePedido>>?>()
    val statusDetalle: LiveData<ResponseStatus<List<ReporteDetallePedido>>?> = _statusDetalle

    private val _statusInt = MutableLiveData<ResponseStatus<Int>?>()
    val statusInt: LiveData<ResponseStatus<Int>?> = _statusInt

    private fun handleResponseStatusPedido(responseStatus: ResponseStatus<List<Pedido>>) {
        if (responseStatus is ResponseStatus.Success) {
            _listaPedido.value = responseStatus.data
        }

        _status.value = responseStatus
    }

    private fun handleResponseStatusDetalle(responseStatus: ResponseStatus<List<ReporteDetallePedido>>) {
        if (responseStatus is ResponseStatus.Success) {
            _listaDetalle.value = responseStatus.data
        }

        _statusDetalle.value = responseStatus
    }

    private fun handleResponseStatusInt(responseStatus: ResponseStatus<Int>) {
        _statusInt.value = responseStatus
    }

    init {
        _listaPedido.value = mutableListOf()
        _listaDetalle.value = mutableListOf()
    }

    fun resetApiResponseStatus() {
        _status.value = null
    }

    fun resetApiResponseStatusDetalle() {
        _statusDetalle.value = null
    }

    fun resetApiResponseStatusInt() {
        _statusInt.value = null
    }

    fun setItem(entidad: Pedido?) {
        _itemPedido.postValue(entidad)
    }

    fun anularPedido(id: Int, desde: String, hasta: String) {

        viewModelScope.launch {
            _statusInt.value = ResponseStatus.Loading()

            handleResponseStatusInt(anularPedidoUseCase(id))
            handleResponseStatusPedido(listarPedidoPorFechaUseCase(desde, hasta))
        }
    }

    fun listarPedido(desde: String, hasta: String) {

        viewModelScope.launch {
            _status.value = ResponseStatus.Loading()
            handleResponseStatusPedido(listarPedidoPorFechaUseCase(desde, hasta))
        }
    }

    fun listarDetalle(idPedido: Int) {

        viewModelScope.launch {
            _statusDetalle.value = ResponseStatus.Loading()
            handleResponseStatusDetalle(listarDetallePedidoUseCase(idPedido))
        }
    }
}