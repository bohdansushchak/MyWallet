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
import bohdan.sushchak.mywallet.adapters.OrdersByDateAdapter
import bohdan.sushchak.mywallet.data.db.entity.CategoryEntity
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
import bohdan.sushchak.mywallet.data.model.OrdersByDateGroup
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

    private lateinit var adapter: OrdersByDateAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.order_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(OrderListViewModel::class.java)

        bindUI()
    }

    private fun bindUI() = launch {

        viewModel.orders.await().observe(this@OrderListFragment, Observer { orders ->
            val ordersByDate = convertOrdersByDate(orders)
            updateOrderList(ordersByDate)
        })

        viewModel.categories.await().observe(this@OrderListFragment, Observer {
            initButtonCreateOrder(it)
        })
    }

    private fun initButtonCreateOrder(list: List<CategoryEntity>): Unit {

        fabCreateOrder.setOnClickListener {
            val navigationController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)

            if (list.isNotEmpty())
                navigationController.navigate(R.id.action_orderListFragment_to_createOrderFragment)
            else {
                showDialog(R.string.d_title_no_categories, R.string.d_msg_no_categories, yes = {
                    navigationController.navigate(R.id.action_orderListFragment_to_settingsFragment)
                })
            }
        }
    }

    private fun updateOrderList(ordersByDate: List<OrdersByDateGroup>) {
/*
        if(::adapter.isInitialized){



            return
        }
*/
        adapter = OrdersByDateAdapter(context!!, ordersByDate)

        rcViewOrders.adapter = adapter
        rcViewOrders.layoutManager = LinearLayoutManager(context)
/*
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
*/
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::adapter.isInitialized)
            adapter.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (::adapter.isInitialized)
            adapter.onRestoreInstanceState(savedInstanceState)
    }

    private fun removeCategory(order: OrderEntity) {

        showDialog(title = R.string.d_remove_order, msg = R.string.d_remove_order_are_you_sure,
                yes = { viewModel.removeOrder(order) },
                cancel = {})
    }

    private fun editCategory(order: OrderEntity) {

    }

    private fun viewOrder(order: OrderEntity) {

    }
}
