package maciej_witkowski.teamlister.view


import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.firebase.FirebaseApp
import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.preferences.SettingsFragment
import maciej_witkowski.teamlister.tasks.CleaningService

class MainActivity : AppCompatActivity() {
    companion object{
        var isFirstLaunch=true
    }
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_list -> {
                loadFragment(ProcessedTeamFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_camera -> {
                loadFragment(PhotoViewPagerFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_gallery -> {
                loadFragment(GalleryViewPagerFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

        fun loadFragment(fragment: Fragment){
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit()
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_settings ->{
                loadFragment(SettingsFragment())
                return true}
            R.id.action_photo_report ->{
                loadFragment(PhotoReportFragment())
                return true}
            R.id.action_report_summary ->{
                loadFragment(ReportSummaryFragment())
                return true}
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
        FirebaseApp.initializeApp(this)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        if (isFirstLaunch) {
            isFirstLaunch=false
            baseContext.startService(Intent(baseContext, CleaningService::class.java))
        }
    }

}
