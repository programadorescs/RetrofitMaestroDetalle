package pe.pcs.retrofitmaestrodetalle

import android.app.Application
import android.content.Context
import com.google.android.gms.ads.MobileAds

class RetrofitMaestroDetalleApp: Application() {

    override fun onCreate() {
        super.onCreate()

        // AdMob
        MobileAds.initialize(this)
    }

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