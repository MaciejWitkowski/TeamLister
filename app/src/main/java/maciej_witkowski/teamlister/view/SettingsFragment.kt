package maciej_witkowski.remoterelease
import androidx.preference.PreferenceFragmentCompat
import android.content.SharedPreferences
import android.os.Bundle
import maciej_witkowski.teamlister.R


class SettingsFragment : PreferenceFragmentCompat() {


    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
        when (key) {

        }
    }
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        activity!!.title = "Settings"
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onPause() {
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        super.onPause()
    }

}
