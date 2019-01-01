package bohdan.sushchak.mywallet

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule


class MyWalletApplication() : Application(), KodeinAware{

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MyWalletApplication))


    }

    override fun onCreate() {
        super.onCreate()
    }
}