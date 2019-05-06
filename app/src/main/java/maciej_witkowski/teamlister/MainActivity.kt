package maciej_witkowski.teamlister

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import maciej_witkowski.remoterelease.SettingsFragment
import maciej_witkowski.teamlister.view.CameraFragment
import maciej_witkowski.teamlister.view.PickResultFragment

private const val TAG = "FIREBASE"
private const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_list -> {
                loadFragment(PickResultFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_camera -> {
                loadFragment(CameraFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_gallery -> {
                getGalleryPermissions()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun loadFragment(fragment: Fragment){
       // if (savedInstanceState == null) {//to avoid fragment recreation
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.contentFrame, fragment)
                .commit()
        //}
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_settings ->{Toast.makeText(this.applicationContext, "Settings clicked", Toast.LENGTH_SHORT).show()
                loadFragment(SettingsFragment())
                return true}
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setSupportActionBar(toolbar)
        FirebaseApp.initializeApp(this)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }


    private fun getGalleryPermissions(){
        val rxPermissions = RxPermissions(this);
        var a = rxPermissions
            .request(Manifest.permission.READ_EXTERNAL_STORAGE)
            .subscribe { granted ->
                if (granted) {
                    Toast.makeText(this, "Granted", Toast.LENGTH_LONG).show()
                    selectFromGallery()
                    //startCamera()
                    // Always true pre-M
                    // I can control the camera now
                } else {
                    Toast.makeText(this, "Not Granted", Toast.LENGTH_LONG).show()
                    // permission denied
                }
            }
    }
    private fun selectFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
        }
    }




}
