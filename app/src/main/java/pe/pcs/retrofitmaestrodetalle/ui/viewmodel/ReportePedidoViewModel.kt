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
import pe.pcs.retrofitmaestrodetalle.data.model.PedidoModel
import pe.pcs.retrofitmaestrodetalle.data.model.ReporteDetallePedidoModel
import pe.pcs.retrofitmaestrodetalle.data.model.ResponseHttp
import pe.pcs.retrofitmaestrodetalle.data.service.PedidoService
import javax.inject.Inject

@HiltViewModel
class ReportePedidoViewModel @Inject constructor(
    private val service: PedidoService
) : ViewModel() {

    private val _listaPedido = MutableLiveData<List<PedidoModel>>()
    val listaPedido: LiveData<List<PedidoModel>> = _listaPedido

    private val _listaDetalle = MutableLiveData<List<ReporteDetallePedidoModel>>()
    val listaDetalle: LiveData<List<ReporteDetallePedidoModel>> = _listaDetalle

    private val _itemPedido = MutableLiveData<PedidoModel?>()
    val itemPedido: LiveData<PedidoModel?> = _itemPedido

    private val _progressBar = MutableLiveData<Boolean>()
    var progressBar: LiveData<Boolean> = _progressBar

    var mErrorStatus = MutableLiveData<String?>()

    var operacionExitosa = MutableLiveData<ResponseHttp?>()

    init {
        _listaPedido.value = mutableListOf()
        _listaDetalle.value = mutableListOf()
    }

    fun setItem(entidad: PedidoModel?) {
        _itemPedido.postValue(entidad)
    }

    fun anularPedido(id: Int, desde: String, hasta: String) {
        _progressBar.postValue(true)

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val rpta = service.anular(id)
                    _listaPedido.postValue(
                        Gson().fromJson(
                            service.listarPorFecha(desde, hasta).body()!!.data,
                            object : TypeToken<List<PedidoModel>>() {}.type
                        )
                    )
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
                _listaPedido.postValue(
                    Gson().fromJson(
                        service.listarPorFecha(desde, hasta).body()!!.data,
                        object : TypeToken<List<PedidoModel>>() {}.type
                    )
                )
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
                _listaDetalle.postValue(
                    Gson().fromJson(
                        service.listarDetalle(idPedido).body()!!.data,
                        object : TypeToken<List<ReporteDetallePedidoModel>>() {}.type
                    )
                )
            } catch (e: Exception) {
                mErrorStatus.postValue(e.message)
            } finally {
                _progressBar.postValue(false)
            }
        }
    }
}