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
import pe.pcs.retrofitmaestrodetalle.data.model.ResponseHttp
import pe.pcs.retrofitmaestrodetalle.data.service.ProductoService
import javax.inject.Inject

@HiltViewModel
class ProductoViewModel @Inject constructor(private val service: ProductoService) : ViewModel() {

    //private val service = ProductoService()

    private val _lista = MutableLiveData<List<ProductoModel>>()
    val lista: LiveData<List<ProductoModel>> = _lista

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
                _lista.postValue(
                    Gson().fromJson(
                        service.listar(dato).body()!!.data,
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

    fun grabar(entidad: ProductoModel) {
        _progressBar.postValue(true)

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val rpta = if (entidad.id == 0)
                        service.registrar(entidad)
                    else
                        service.actualizar(entidad)

                    _lista.postValue(
                        Gson().fromJson(
                            service.listar("").body()!!.data,
                            object : TypeToken<List<ProductoModel>>() {}.type
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

    fun eliminar(id: Long) {
        _progressBar.postValue(true)

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val rpta = service.eliminar(id)
                    _lista.postValue(
                        Gson().fromJson(
                            service.listar("").body()!!.data,
                            object : TypeToken<List<ProductoModel>>() {}.type
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
}