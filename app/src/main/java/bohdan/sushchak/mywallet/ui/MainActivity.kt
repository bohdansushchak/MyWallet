package bohdan.sushchak.mywallet.ui

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation

import androidx.navigation.ui.setupWithNavController
import bohdan.sushchak.mywallet.R
import kotlinx.android.synthetic.main.activity_main.*

const val IS_START_KEY = "isStart"

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener{

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        navController = Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment)

        bottom_nav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(this)
    }


    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        val actionBar = getSupportActionBar()
        val title = destination.label
        if (!TextUtils.isEmpty(title)) {
            actionBar?.setTitle(title)
        }

        val isStartArg = arguments?.get(IS_START_KEY) as Boolean?
        val isStartFragment = controller.getGraph().getStartDestination() == destination.getId()

        actionBar?.setDisplayHomeAsUpEnabled(!(isStartFragment || isStartArg?: false))
    }


    override fun onDestroy() {
        super.onDestroy()
        navController.removeOnDestinationChangedListener(this)
    }
}
