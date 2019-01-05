package bohdan.sushchak.mywallet.ui.preferences

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import bohdan.sushchak.mywallet.R

class PreferencesFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}