package pe.pcs.retrofitmaestrodetalle.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.pcs.retrofitmaestrodetalle.ui.core.ResponseStatus
import pe.pcs.retrofitmaestrodetalle.domain.model.Pedido
import pe.pcs.retrofitmaestrodetalle.domain.model.ReporteDetallePedido
import pe.pcs.retrofitmaestrodetalle.domain.usecase.pedido.AnularPedidoUseCase
import pe.pcs.retrofitmaestrodetalle.domain.usecase.pedido.ListarDetallePedidoUseCase
import pe.pcs.retrofitmaestrodetalle.domain.usecase.pedido.ListarPedidoPorFechaUseCase
import pe.pcs.retrofitmaestrodetalle.ui.core.makeCall
import javax.inject.Inject

@HiltViewModel
class ReportePedidoViewModel @Inject constructor(
    private val anularPedidoUseCase: AnularPedidoUseCase,
    private val listarPedidoPorFechaUseCase: ListarPedidoPorFechaUseCase,
    private val listarDetallePedidoUseCase: ListarDetallePedidoUseCase
) : ViewModel() {

    private val _listaPedido = MutableLiveData<List<Pedido>?>(mutableListOf())
    val listaPedido: LiveData<List<Pedido>?> = _listaPedido

    private val _listaDetalle = MutableLiveData<List<ReporteDetallePedido>?>(mutableListOf())
    val listaDetalle: LiveData<List<ReporteDetallePedido>?> = _listaDetalle

    private val _itemPedido = MutableLiveData<Pedido?>()
    val itemPedido: LiveData<Pedido?> = _itemPedido

    private val _statePedido = MutableLiveData<ResponseStatus<List<Pedido>>>()
    val statePedido: LiveData<ResponseStatus<List<Pedido>>> = _statePedido

    private val _statusDetalle = MutableLiveData<ResponseStatus<List<ReporteDetallePedido>>>()
    val statusDetalle: LiveData<ResponseStatus<List<ReporteDetallePedido>>> = _statusDetalle

    private val _stateAnularPedido = MutableLiveData<ResponseStatus<Int>>()
    val stateAnularPedido: LiveData<ResponseStatus<Int>> = _stateAnularPedido

    private fun handleStatePedido(responseStatus: ResponseStatus<List<Pedido>>) {
        if (responseStatus is ResponseStatus.Success)
            _listaPedido.value = responseStatus.data

        _statePedido.value = responseStatus
    }

    private fun handleStateDetalle(responseStatus: ResponseStatus<List<ReporteDetallePedido>>) {
        if (responseStatus is ResponseStatus.Success)
            _listaDetalle.value = responseStatus.data

        _statusDetalle.value = responseStatus
    }

    private fun handleStateAnularPedido(responseStatus: ResponseStatus<Int>) {
        _stateAnularPedido.value = responseStatus
    }

    fun resetStateAnularPedido() {
        _stateAnularPedido.value = ResponseStatus.Success(0)
    }

    fun setItem(entidad: Pedido?) {
        _itemPedido.postValue(entidad)
    }

    fun anularPedido(id: Int, desde: String, hasta: String) {

        viewModelScope.launch {
            _stateAnularPedido.value = ResponseStatus.Loading()

            handleStateAnularPedido(
                makeCall { anularPedidoUseCase(id) }
            )

            handleStatePedido(
                makeCall { listarPedidoPorFechaUseCase(desde, hasta) }
            )
        }
    }

    fun listarPedido(desde: String, hasta: String) {

        viewModelScope.launch {
            _statePedido.value = ResponseStatus.Loading()
            handleStatePedido(
                makeCall { listarPedidoPorFechaUseCase(desde, hasta) }
            )
        }
    }

    fun listarDetalle(idPedido: Int) {

        viewModelScope.launch {
            _statusDetalle.value = ResponseStatus.Loading()
            handleStateDetalle(
                makeCall { listarDetallePedidoUseCase(idPedido) }
            )
        }
    }
}