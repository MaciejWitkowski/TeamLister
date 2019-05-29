package maciej_witkowski.teamlister.view
import androidx.preference.PreferenceFragmentCompat
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import maciej_witkowski.teamlister.R


class SettingsFragment : PreferenceFragmentCompat() {


    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
        when (key) {

        }
    }
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Settings"
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
