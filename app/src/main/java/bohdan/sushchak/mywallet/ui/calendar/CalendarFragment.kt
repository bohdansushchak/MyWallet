package bohdan.sushchak.mywallet.ui.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.adapters.OrderAdapter
import bohdan.sushchak.mywallet.data.db.entity.OrderEntity
import bohdan.sushchak.mywallet.internal.Constants
import bohdan.sushchak.mywallet.internal.formatDate
import bohdan.sushchak.mywallet.internal.myToString
import bohdan.sushchak.mywallet.internal.onlyDateInMillis
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

    private lateinit var adapter: OrderAdapter

    private val viewModelFactory: CalendarViewModelFactory by instance()
    private lateinit var viewModel: CalendarViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
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
            updateListOrder(it)
        })

        viewModel.calendarDates.await().observe(this@CalendarFragment, Observer { events ->
            calendarView.addEvents(events.toMutableList())
        })

        viewModel.totalPrice.observe(this@CalendarFragment, Observer { totalPrice ->
            tvTotal.text = "${getString(R.string.total)} ${totalPrice.myToString()}"
        })
    }

    private fun updateListOrder(orders: List<OrderEntity>) {
        if (::adapter.isInitialized) {
            adapter.update(orders)
            initLongClick(orders)
            return
        }

        adapter = OrderAdapter(context!!, orders)
        val layoutManager = LinearLayoutManager(context)

        recyclerViewOrders.adapter = adapter
        recyclerViewOrders.layoutManager = layoutManager
        initLongClick(orders)
    }

    private fun initLongClick(orders: List<OrderEntity>) {
        adapter.onLongClick = { view, position ->
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

        val cal = Calendar.getInstance()
        cal.onlyDateInMillis {
            val date = it
            viewModel.updateOrders(date) }

        tvMonthTitle.text = formatDate(Date(), Constants.MONTH_FORMAT)

        calendarView.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {
                cal.time = dateClicked
                cal.onlyDateInMillis {
                    val date = it
                    viewModel.updateOrders(date) }
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                tvMonthTitle.text = formatDate(firstDayOfNewMonth, Constants.MONTH_FORMAT)
            }
        })
    }

}


