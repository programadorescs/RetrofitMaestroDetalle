package pe.pcs.retrofitmaestrodetalle.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pe.pcs.retrofitmaestrodetalle.data.model.PedidoModel
import pe.pcs.retrofitmaestrodetalle.data.model.ResponseHttp
import pe.pcs.retrofitmaestrodetalle.data.service.PedidoService
import retrofit2.Response

class ReportePedidoViewModel: ViewModel() {

    private val service = PedidoService()

    private val _listaPedido = MutableLiveData<Response<ResponseHttp>>()
    val listaPedido: LiveData<Response<ResponseHttp>> = _listaPedido

    private val _listaDetalle = MutableLiveData<Response<ResponseHttp>>()
    val listaDetalle: LiveData<Response<ResponseHttp>> = _listaDetalle

    private val _itemPedido = MutableLiveData<PedidoModel?>()
    val itemPedido: LiveData<PedidoModel?> = _itemPedido

    private val _progressBar = MutableLiveData<Boolean>()
    var progressBar: LiveData<Boolean> = _progressBar

    var mErrorStatus = MutableLiveData<String?>()

    var operacionExitosa = MutableLiveData<ResponseHttp?>()

    /*init {
        _listaPedido.value = mutableListOf()
        _listaDetalle.value = mutableListOf()
    }*/

    fun setItem(entidad: PedidoModel?) {
        _itemPedido.postValue(entidad)
    }

    fun anularPedido(id: Int, desde: String, hasta: String) {
        _progressBar.postValue(true)

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val rpta = service.anular(id)
                    _listaPedido.postValue(service.listarPorFecha(desde, hasta))
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

    fun listarPedido(desde: String, hasta: String) {
        _progressBar.postValue(true)

        viewModelScope.launch {
            try {
                _listaPedido.postValue(service.listarPorFecha(desde, hasta))
            } catch (e: Exception) {
                mErrorStatus.postValue(e.message)
            } finally {
                _progressBar.postValue(false)
            }
        }
    }

    fun listarDetalle(idPedido: Int) {
        _progressBar.postValue(true)

        viewModelScope.launch {
            try {
                _listaDetalle.postValue(service.listarDetalle(idPedido))
            } catch (e: Exception) {
                mErrorStatus.postValue(e.message)
            } finally {
                _progressBar.postValue(false)
            }
        }
    }
}