package bohdan.sushchak.mywallet.ui.order

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import bohdan.sushchak.mywallet.R

class CreateOrderFragment : Fragment() {

    companion object {
        fun newInstance() = CreateOrderFragment()
    }

    private lateinit var viewModel: CreateOrderViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.create_order_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CreateOrderViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
