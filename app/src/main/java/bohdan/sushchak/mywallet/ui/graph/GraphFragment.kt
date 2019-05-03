package bohdan.sushchak.mywallet.ui.graph


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.VERTICAL
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.data.model.DateLimit
import bohdan.sushchak.mywallet.internal.Constants.DATE_MONTH_YEAR
import bohdan.sushchak.mywallet.internal.formatDate
import bohdan.sushchak.mywallet.ui.base.BaseFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.graph_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class GraphFragment : BaseFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: GraphViewModelFactory by instance()
    private lateinit var viewModel: GraphViewModel

    private lateinit var groupAdapter: GroupAdapter<ViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.graph_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(GraphViewModel::class.java)

        bindUI()
    }

    private fun bindUI() = launch {
        viewModel.graphItems.observe(this@GraphFragment, Observer {
            updateGraphList(it)
            group_loading.visibility = View.GONE
        })

        viewModel.dateLimit.observe(this@GraphFragment, Observer { dateLimit ->
            updateDateLimits(dateLimit)
        })
    }

    private fun updateDateLimits(dateLimit: DateLimit) {
        tvDateFrom.text = formatDate(dateLimit.startDate, DATE_MONTH_YEAR)
        tvDateTo.text = formatDate(dateLimit.endDate, DATE_MONTH_YEAR)

        if (dateLimit.biggestDate != 0L && dateLimit.leastDate != 0L)
            initChooseDateListeners(dateLimit)
    }

    private fun updateGraphList(graphItems: List<GraphItem>) {
        groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(graphItems)
        }

        rcGraphList.apply {
            adapter = groupAdapter
            layoutManager = LinearLayoutManager(context)
        }

        if (rcGraphList.itemDecorationCount == 0) {
            val decoration = DividerItemDecoration(context, VERTICAL)
            val drawable = resources.getDrawable(R.drawable.transparent_decoration_drawable)
            decoration.setDrawable(drawable)
            rcGraphList.addItemDecoration(decoration)
        }
    }

    private fun initChooseDateListeners(dateLimit: DateLimit) {
        tvDateFrom.setOnClickListener {
            pickDate(
                dateLimit.startDate,
                minDate = dateLimit.leastDate,
                maxDate = dateLimit.endDate
            ) { pickedDate ->
                if (pickedDate.time > dateLimit.endDate)
                    makeToast(R.string.t_from_date_cant_be_greater_than_to_date)
                else {
                    viewModel.updateDateLimit(startDate = pickedDate.time)
                    group_loading.visibility = View.VISIBLE
                }
            }
        }

        tvDateTo.setOnClickListener {
            pickDate(
                dateLimit.endDate,
                minDate = dateLimit.startDate,
                maxDate = dateLimit.biggestDate
            ) { pickedDate ->
                if (pickedDate.time < dateLimit.startDate)
                    makeToast(R.string.t_to_date_cant_be_less_than_from_date)
                else {
                    viewModel.updateDateLimit(endDate = pickedDate.time)
                    group_loading.visibility = View.VISIBLE
                }
            }
        }
    }
}
