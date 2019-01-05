package bohdan.sushchak.mywallet.ui.create_order

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.ui.base.ScoptedFragment
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CreateOrderFragment : ScoptedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: CreateOrderViewModelFactory by instance()
    private lateinit var viewModel: CreateOrderViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.create_order_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(CreateOrderViewModel::class.java)

        bindUI()
    }

    private fun bindUI() = launch {

    }



}
