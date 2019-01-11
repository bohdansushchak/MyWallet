package bohdan.sushchak.mywallet.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.adapters.OrderAdapter
import bohdan.sushchak.mywallet.data.db.entity.Order
import bohdan.sushchak.mywallet.ui.base.BaseFragment
import kotlinx.android.synthetic.main.calendar_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.*

class CalendarFragment : BaseFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: CalendarViewModelFactory by instance()
    private lateinit var viewModel: CalendarViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.calendar_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /*
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Calendar"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null*/

        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(CalendarViewModel::class.java)


        bindUI()
    }

    private fun bindUI() = launch {
        initCalendar()

        viewModel.orders.observe(this@CalendarFragment, Observer {
            updateListOrder(it)
        })
    }

    private fun updateListOrder(orders: List<Order>) {

        val adapter = OrderAdapter(context!!, orders)
        val layoutManager = LinearLayoutManager(context)

        recyclerViewOrders.adapter = adapter
        recyclerViewOrders.layoutManager = layoutManager

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

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)

        updateOrder(year, month, dayOfMonth)

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            updateOrder(year, month, dayOfMonth)
        }
    }

    private fun updateOrder(year: Int, month: Int, dayOfMonth: Int) {

        val calendar = Calendar.getInstance()
        calendar.clear()

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        viewModel.updateOrders(calendar.time.time)
    }
}
