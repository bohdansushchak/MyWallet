package bohdan.sushchak.mywallet.ui.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.adapters.OrderAdapter
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
import bohdan.sushchak.mywallet.internal.*
import bohdan.sushchak.mywallet.internal.view.startFadeInAnimation
import bohdan.sushchak.mywallet.ui.base.BaseFragment
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import kotlinx.android.synthetic.main.calendar_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.*


class CalendarFragment : BaseFragment(), KodeinAware {

    override val kodein by closestKodein()

    private lateinit var orderAdapter: OrderAdapter

    private val viewModelFactory: CalendarViewModelFactory by instance()
    private lateinit var viewModel: CalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.calendar_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CalendarViewModel::class.java)

        bindUI()
    }

    @SuppressLint("SetTextI18n")
    private fun bindUI() = launch {
        initCalendar()

        val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(context!!.getDrawable(R.drawable.divider_white)!!)
        recyclerViewOrders.addItemDecoration(dividerItemDecoration)

        viewModel.orders.observe(this@CalendarFragment, Observer {
            tvListIsEmpty.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            updateListOrder(it)
        })

        viewModel.calendarDates.await().observe(this@CalendarFragment, Observer { events ->
            calendarView.addEvents(events.toMutableList())
        })

        viewModel.totalPrice.observe(this@CalendarFragment, Observer {
            updateTotalPrice(it)
        })
    }

    private fun updateTotalPrice(totalPrice: Double) {
        val currency = context?.let { getSavedCurrency(it) }
        tvTotal.text = "${totalPrice.myToString()} $currency"

        tvTotal.startFadeInAnimation(context)
    }

    private fun updateListOrder(orders: List<OrderEntity>) {
        if (::orderAdapter.isInitialized) {
            orderAdapter.update(orders)
            orderAdapter.notifyDataSetChanged()
            recyclerViewOrders.scheduleLayoutAnimation()
            initLongClick(orders)
            return
        }

        orderAdapter = OrderAdapter(context!!, orders)
        val llManager = LinearLayoutManager(context)

        val recyclerAnimation = AnimationUtils
            .loadLayoutAnimation(context, R.anim.layout_animation_fall_down)

        recyclerViewOrders.apply {
            adapter = orderAdapter
            layoutManager = llManager
            layoutAnimation = recyclerAnimation
        }
        initLongClick(orders)
    }

    private fun initLongClick(orders: List<OrderEntity>) {
        orderAdapter.onLongClick = { view, position ->
            showPopupEditRemove(view,
                edit = {},
                remove = {
                    showDialog(title = R.string.d_remove_order, msg = R.string.d_remove_order_are_you_sure,
                        yes = {
                            viewModel.removeOrder(orders[position])
                        })
                })
        }
    }

    private fun initCalendar() {
        val date = Calendar.getInstance().getOnlyDate()
        viewModel.updateOrders(date = date.time)

        tvMonthTitle.text = formatDate(date, Constants.MONTH_FORMAT)

        calendarView.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                val newDate = Calendar.getInstance().apply { time = dateClicked }.getOnlyDate()
                viewModel.updateOrders(newDate.time)
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                tvMonthTitle.text = formatDate(firstDayOfNewMonth, Constants.MONTH_FORMAT)
            }
        })
    }
}


