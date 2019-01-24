package bohdan.sushchak.mywallet.ui.graph


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.data.model.GraphItem
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

    private lateinit var adapter: GroupAdapter<ViewHolder>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
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
        })
    }

    private fun updateGraphList(graphItems: List<GraphItem>) {

        if (::adapter.isInitialized) {

            adapter.update(graphItems)
            return
        }

        adapter = GroupAdapter()
        adapter.addAll(graphItems)

        rcGraphList.adapter = adapter
        rcGraphList.layoutManager = LinearLayoutManager(context)
    }
}