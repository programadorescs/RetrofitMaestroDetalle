package pe.pcs.retrofitmaestrodetalle

import android.app.Application
import android.content.Context

class RetrofitMaestroDetalleApp: Application() {

    companion object {
        private var instancia: RetrofitMaestroDetalleApp? = null

        fun getAppContext(): Context {
            return instancia!!.applicationContext
        }
    }

    init {
        instancia = this
    }
}