package pe.pcs.retrofitmaestrodetalle.ui.utils

sealed class ResponseStatus<T> {
    class Success<T>(val data: T) : ResponseStatus<T>()
    class Loading<T> : ResponseStatus<T>()
    class Error<T>(val message: String) : ResponseStatus<T>()
}
