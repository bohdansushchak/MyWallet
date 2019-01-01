package bohdan.sushchak.mywallet.ui.diagram

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import bohdan.sushchak.mywallet.R

class DiagramFragment : Fragment() {

    companion object {
        fun newInstance() = DiagramFragment()
    }

    private lateinit var viewModel: DiagramViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.diagram_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DiagramViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
