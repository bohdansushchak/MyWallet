package bohdan.sushchak.mywallet

import android.app.Application
import bohdan.sushchak.mywallet.data.db.MyWalletDatabase
import bohdan.sushchak.mywallet.data.repository.MyWalletRepository
import bohdan.sushchak.mywallet.data.repository.MyWalletRepositoryImpl
import bohdan.sushchak.mywallet.ui.order.CreateOrderViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


class MyWalletApplication() : Application(), KodeinAware{

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MyWalletApplication))

        //region db
        bind() from singleton { MyWalletDatabase(instance()) }
        bind() from singleton { instance<MyWalletDatabase>().categoryDao() }
        bind() from singleton { instance<MyWalletDatabase>().productDao() }
        bind() from singleton { instance<MyWalletDatabase>().orderDao() }
        //endregion

        bind<MyWalletRepository>() with singleton { MyWalletRepositoryImpl(instance(), instance(), instance()) }

        //region viewmodels
        bind() from provider { CreateOrderViewModelFactory(instance()) }

        //endregion
    }

    override fun onCreate() {
        super.onCreate()
    }
}