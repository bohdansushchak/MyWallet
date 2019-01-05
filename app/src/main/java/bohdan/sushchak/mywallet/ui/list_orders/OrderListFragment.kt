package bohdan.sushchak.mywallet.ui.list_orders

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment

import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.ui.base.ScoptedFragment
import kotlinx.android.synthetic.main.order_list_fragment.*
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class OrderListFragment : ScoptedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: OrderListViewModelFactory by instance()
    private lateinit var viewModel: OrderListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.order_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(OrderListViewModel::class.java)

        bindUI()



        fabCreateOrder.setOnClickListener(Navigation
                        .createNavigateOnClickListener(R.id.createOrderFragment))

        /*fabCreateOrder.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.createOrderFragment)
        }*/
    }

    private fun bindUI() = launch {

    }


}
