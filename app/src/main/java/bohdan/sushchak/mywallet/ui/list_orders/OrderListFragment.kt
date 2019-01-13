package bohdan.sushchak.mywallet.ui.list_orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.adapters.ExpandableListOrderAdapter
import bohdan.sushchak.mywallet.data.db.entity.Order
import bohdan.sushchak.mywallet.data.model.OrdersByDate
import bohdan.sushchak.mywallet.internal.convertOrdersByDate
import bohdan.sushchak.mywallet.ui.base.BaseFragment
import kotlinx.android.synthetic.main.order_list_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class OrderListFragment : BaseFragment(), KodeinAware {

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
                .createNavigateOnClickListener(R.id.action_orderListFragment_to_createOrderFragment))
    }

    private fun bindUI() = launch {

        viewModel.orders.await().observe(this@OrderListFragment, Observer { orders ->
            val ordersByDate = convertOrdersByDate(orders)
            updateOrderList(ordersByDate)
        })
    }

    private fun updateOrderList(ordersByDate: List<OrdersByDate>) {

        val adapter = ExpandableListOrderAdapter(context!!,ordersByDate )

        expListViewOrders.setAdapter(adapter)

        adapter.onLongClick = { view, order ->
            showPopupEditRemove(view,
                    edit = { editCategory(order) },
                    remove = {
                        removeCategory(order)
                    })
        }

        adapter.onClick = { order ->
            viewOrder(order)
        }

    }

    private fun removeCategory(order: Order) {

        showDialog(title = R.string.d_remove_order, msg = R.string.d_remove_order_are_you_sure,
                yes = { viewModel.removeOrder(order) },
                cancel = {})
    }

    private fun editCategory(order: Order) {

    }

    private fun viewOrder(order: Order) {

    }
}
