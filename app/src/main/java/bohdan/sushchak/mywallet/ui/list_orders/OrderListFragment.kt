package bohdan.sushchak.mywallet.ui.list_orders

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
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

    private lateinit var orderAdapter: OrdersByDateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.order_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(OrderListViewModel::class.java)

        bindUI()

        rcViewOrders.setOnTouchListener { _, event -> recyclerTouchShowOrGoneFab(event) }
    }

    private fun bindUI() = launch {
        viewModel.orders.await().observe(this@OrderListFragment, Observer { orders ->
            tvListIsEmpty.visibility = if (orders.isEmpty()) View.VISIBLE else View.GONE
            val ordersByDate = convertOrdersByDate(orders)
            updateOrderList(ordersByDate)
        })

        viewModel.categories.await().observe(this@OrderListFragment, Observer {
            initButtonCreateOrder(it)
        })
    }

    private fun initButtonCreateOrder(list: List<CategoryEntity>) {
        fabCreateOrder.setOnClickListener {

            val navigationController =
                Navigation.findNavController(activity!!, R.id.nav_host_fragment)

            if (list.isNotEmpty())
                navigationController.navigate(R.id.actionCreateOrder)
            else {
                showDialog(R.string.d_title_no_categories, R.string.d_msg_no_categories, yes = {
                    navigationController.navigate(R.id.action_orderListFragment_to_settingsFragment)
                })
            }
        }
    }

    private fun updateOrderList(ordersByDate: List<OrdersByDateGroup>) {
        orderAdapter = OrdersByDateAdapter(context!!, ordersByDate).apply {
            onLongClick = { view, order ->
                showPopupEditRemove(view,
                    edit = { editOrder(view, order) },
                    remove = {
                        removeCategory(order)
                    })
            }
            onClick = { v, order ->
                viewOrder(v, order)
            }
        }

        rcViewOrders.apply {
            adapter = orderAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::orderAdapter.isInitialized)
            orderAdapter.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (::orderAdapter.isInitialized)
            orderAdapter.onRestoreInstanceState(savedInstanceState)
    }

    private fun removeCategory(order: OrderEntity) {
        showDialog(title = R.string.d_remove_order, msg = R.string.d_remove_order_are_you_sure,
            yes = { viewModel.removeOrder(order) })
    }

    private fun editOrder(view: View, order: OrderEntity) {
        val action = OrderListFragmentDirections.actionCreateOrder()
        action.order = order
        getNavController(view).navigate(action)
    }

    private fun viewOrder(view: View, order: OrderEntity) {

    }

    private var downY: Float = -1f
    private var upY: Float = -1f

    @SuppressLint("RestrictedApi")
    private fun recyclerTouchShowOrGoneFab(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_MOVE -> if (downY == -1f) downY = event.y
            MotionEvent.ACTION_DOWN -> if (downY == -1f) downY = event.y

            MotionEvent.ACTION_UP -> {
                upY = event.y

                Log.d("ACTION UP", "$downY  $upY")

                if (downY < upY && fabCreateOrder.visibility == View.GONE) {
                    val animMoveDown =
                        AnimationUtils.loadAnimation(
                            context,
                            R.anim.move_from_bottom_to_current_position
                        )
                    fabCreateOrder.startAnimation(animMoveDown)
                    fabCreateOrder.visibility = View.VISIBLE
                } else if (downY > upY && fabCreateOrder.visibility == View.VISIBLE) {
                    val animMoveDown =
                        AnimationUtils.loadAnimation(
                            context,
                            R.anim.move_from_current_position_to_bottom
                        )
                    fabCreateOrder.startAnimation(animMoveDown)
                    fabCreateOrder.visibility = View.GONE
                }

                downY = -1f
                upY = -1f
            }
        }
        return false
    }
}
