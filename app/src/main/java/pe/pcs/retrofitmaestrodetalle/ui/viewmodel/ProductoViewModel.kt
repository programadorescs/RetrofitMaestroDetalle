package pe.pcs.retrofitmaestrodetalle.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pe.pcs.retrofitmaestrodetalle.data.model.ProductoModel
import pe.pcs.retrofitmaestrodetalle.data.model.ResponseHttp
import pe.pcs.retrofitmaestrodetalle.data.service.ProductoService
import retrofit2.Response

class ProductoViewModel: ViewModel() {

    private val service = ProductoService()

    private val _lista = MutableLiveData<Response<ResponseHttp>>()
    val lista: LiveData<Response<ResponseHttp>> = _lista

    private var _item = MutableLiveData<ProductoModel?>()
    val item: LiveData<ProductoModel?> = _item

    private val _progressBar = MutableLiveData<Boolean>()
    var progressBar: LiveData<Boolean> = _progressBar

    var mErrorStatus = MutableLiveData<String?>()

    var operacionExitosa = MutableLiveData<ResponseHttp?>()

    fun setItem(entidad: ProductoModel?) {
        _item.postValue(entidad)
    }

    fun listar(dato: String) {
        _progressBar.postValue(true)

        viewModelScope.launch {
            try {
                _lista.postValue(service.listar(dato))
            } catch (e: Exception) {
                mErrorStatus.postValue(e.message)
            } finally {
                _progressBar.postValue(false)
            }
        }
    }

    fun grabar(entidad: ProductoModel) {
        _progressBar.postValue(true)

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val rpta = if(entidad.id == 0)
                        service.registrar(entidad)
                    else
                        service.actualizar(entidad)

                    _lista.postValue(service.listar(""))
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

    fun eliminar(id: Long) {
        _progressBar.postValue(true)

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val rpta = service.eliminar(id)
                    _lista.postValue(service.listar(""))
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