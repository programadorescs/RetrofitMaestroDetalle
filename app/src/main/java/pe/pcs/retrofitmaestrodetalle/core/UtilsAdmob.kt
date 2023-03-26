package pe.pcs.retrofitmaestrodetalle.core

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import pe.pcs.retrofitmaestrodetalle.R
import pe.pcs.retrofitmaestrodetalle.RetrofitMaestroDetalleApp

abstract class UtilsAdmob {

    companion object {
        var interstitial: InterstitialAd? = null

        fun initInterstitial() {
            InterstitialAd.load(
                RetrofitMaestroDetalleApp.getAppContext(),
                RetrofitMaestroDetalleApp.getAppContext().getString(R.string.id_admob_intersticial),
                AdRequest.Builder().build(),
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(p0: InterstitialAd) {
                        interstitial = p0
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        interstitial = null
                    }
                })
        }
    }

}