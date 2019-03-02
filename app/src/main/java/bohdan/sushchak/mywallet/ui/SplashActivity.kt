package bohdan.sushchak.mywallet.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //if(FirebaseAuth.getInstance().currentUser != null)
        startActivity<MainActivity>()
        finish()
    }
}