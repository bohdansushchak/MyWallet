package bohdan.sushchak.mywallet.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bohdan.sushchak.mywallet.ui.MainActivity
import bohdan.sushchak.mywallet.ui.authorization.AuthorizationActivity
import org.jetbrains.anko.startActivity
import com.google.firebase.auth.FirebaseAuth


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        if(user == null) {
            startActivity<AuthorizationActivity>()
        }
        else {
            startActivity<MainActivity>()
        }
        finish()
    }
}