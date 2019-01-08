package bohdan.sushchak.mywallet.ui.list_orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.adapters.OrderAdapter
import bohdan.sushchak.mywallet.data.db.entity.Order
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
                .createNavigateOnClickListener(R.id.createOrderFragment))
    }

    private fun bindUI() = launch {

        viewModel.orderList.await().observe(this@OrderListFragment, Observer { orders ->
            updateOrderList(orders)
        })
    }

    private fun updateOrderList(orders: List<Order>) {

        val adapter = OrderAdapter(context!!, orders)
        val linearManager = LinearLayoutManager(context)

        recyclerViewOrders.adapter = adapter
        recyclerViewOrders.layoutManager = linearManager

        adapter.onLongClick = { view, position ->
            showPopupEditRemove(view,
                    edit = { editCategory(orders[position]) },
                    remove = {
                        removeCategory(orders[position])
                    })
        }

        adapter.onClick = { _, position ->
            viewOrder(orders[position])
        }

    }

    private fun removeCategory(order: Order) {

        showDialog(title = "Remove order", msg = "Are you sure to remove order?",
                yes = { viewModel.removeOrder(order) },
                cancel = {})

    }

    private fun editCategory(order: Order) {

    }

    private fun viewOrder(order: Order) {

    }
}
