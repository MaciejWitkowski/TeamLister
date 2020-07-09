package maciej_witkowski.teamlister.preferences
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.utils.IOnBackPressed


class SettingsFragment : PreferenceFragmentCompat(), IOnBackPressed {


    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
        when (key) {
            }
        }
    override fun onBackPressed(): Boolean {
        return true
    }


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.app_title_settings)
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
