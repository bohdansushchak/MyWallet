package bohdan.sushchak.mywallet.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import bohdan.sushchak.mywallet.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}