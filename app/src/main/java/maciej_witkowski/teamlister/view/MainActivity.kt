package maciej_witkowski.teamlister.view


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.firebase.FirebaseApp
import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.preferences.SettingsFragment
import maciej_witkowski.teamlister.tasks.CleaningService

private val TAG = MainActivity::class.java.simpleName

private const val REQUEST_CAMERA_PERMISSION = 1
private const val REQUEST_STORAGE_PERMISSION = 2

class MainActivity : AppCompatActivity() {
    companion object {
        var isFirstLaunch = true
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_list -> {
                loadFragment(ProcessedTeamFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_camera -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    loadFragment(PhotoViewPagerFragment())
                else {
                    getCameraPermission()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_gallery -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    loadFragment(GalleryViewPagerFragment())
                else
                    getStoragePermission()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit()
    }


    private fun getCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Toast.makeText(this, "Team Lister needs yours permission to use camera", Toast.LENGTH_LONG).show()
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            }
        } else {
        }
    }

    private fun getStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Team Lister needs yours permission to read photos from storage", Toast.LENGTH_LONG).show()
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSION)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSION)
            }
        } else {
        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Need permissions for camera", Toast.LENGTH_LONG).show()
                }
                return
            }
            REQUEST_STORAGE_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "Storage permission granted", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Need permissions for reading storage", Toast.LENGTH_LONG).show()
                }
                return

            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)

            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                loadFragment(SettingsFragment())
                return true
            }
            R.id.action_photo_report -> {
                loadFragment(PhotoReportFragment())
                return true
            }
            R.id.action_report_summary -> {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    loadFragment(ReportSummaryFragment())
                else
                    getStoragePermission()
                return true
            }
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
            isFirstLaunch = false
            baseContext.startService(Intent(baseContext, CleaningService::class.java))
        }
    }
}
