package bohdan.sushchak.mywallet.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation

import androidx.navigation.ui.setupWithNavController
import bohdan.sushchak.mywallet.R
import bohdan.sushchak.mywallet.ui.authorization.AuthorizationActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

const val IS_START_KEY = "isStart"
const val IS_BOTTOM_NAVIGATION_GONE = "isBottomNavigationGone"

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

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
        navController.addOnDestinationChangedListener(this@MainActivity)
        initAuthorizationObserver()
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        val actionBar = supportActionBar
        val title = destination.label
        if (!TextUtils.isEmpty(title)) {
            actionBar?.title = title
        }

        val isStartArg = arguments?.get(IS_START_KEY) as Boolean?
        val isBottomNavigationGone = arguments?.get(IS_BOTTOM_NAVIGATION_GONE) as Boolean?
        val isStartFragment = controller.graph.startDestination == destination.id

        actionBar?.setDisplayHomeAsUpEnabled(!(isStartFragment || isStartArg ?: false))
        navigationBottomUpdate(isBottomNavigationGone)
    }

    override fun onDestroy() {
        super.onDestroy()
        navController.removeOnDestinationChangedListener(this@MainActivity)
    }

    private fun navigationBottomUpdate(isShouldGone: Boolean?) {

        fun goneNavigation() {
            if (bottom_nav.visibility == View.GONE)
                return
            val anim = AnimationUtils.loadAnimation(this, R.anim.bottom_navigation_gone)
            bottom_nav.startAnimation(anim)
            bottom_nav.visibility = View.GONE
        }

        fun showNavigation() {
            if (bottom_nav.visibility == View.VISIBLE)
                return
            val anim = AnimationUtils.loadAnimation(this, R.anim.bottom_navigation_show)
            bottom_nav.startAnimation(anim)
            bottom_nav.visibility = View.VISIBLE
        }

        if (isShouldGone == true) goneNavigation() else showNavigation()
    }

    private fun initAuthorizationObserver() {
        FirebaseAuth.getInstance().addAuthStateListener {
            if(it.currentUser == null){
                startActivity<AuthorizationActivity>()
                finish()
            }
        }
    }
}
