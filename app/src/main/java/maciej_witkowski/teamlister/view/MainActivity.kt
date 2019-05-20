package maciej_witkowski.teamlister.view

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import kotlinx.android.synthetic.main.content_main.*
import maciej_witkowski.remoterelease.SettingsFragment
import maciej_witkowski.teamlister.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


private const val TAG = "FIREBASE"
private const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1

class MainActivity : AppCompatActivity() {
    //lateinit var bundle:Bundle
    var bundle : Bundle? = null
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_list -> {
                Log.d(TAG, "list fragment should be loaded")
                loadFragment(ListFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_camera -> {
                Log.d(TAG, "View Pager should be loaded")
                loadFragment(ViewPagerFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_gallery -> {
                //getGalleryPermissions()
                loadFragment(ListResultFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun loadFragment(fragment: Fragment){
       // if (bundle == null) {//to avoid fragment recreation
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.contentFrame, fragment)
                .commit()
     //   }
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
        bundle=savedInstanceState
        FirebaseApp.initializeApp(this)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        File("/storage/emulated/0").walk().forEach {
            Log.d("PATH",it.toString())
        }


        /*
        ///data/user/0/maciej_witkowski.teamlister/files/pics
        //val path="data/user/0/maciej_witkowski.teamlister/files/pics"
        //val listOfFiles = getFilesDir().list()

        File("/data/user/0/maciej_witkowski.teamlister/files").walk().forEach {
            Log.d("files",it.toString())
        }

        val filepath = Environment.getExternalStorageDirectory().getAbsolutePath().toString()
        Log.d("PATH",filepath)
        val fileName="name.txt"
        val fileContent="test"
        //var myExternalFile:File = File(getExternalFilesDir(filepath),fileName)
        var myExternalFile:File = File(filepath,fileName)
        try {
            Log.e("PATH","final path: "+myExternalFile.absolutePath)
            val fileOutPutStream = FileOutputStream(myExternalFile)
            fileOutPutStream.write(fileContent.toByteArray())
            fileOutPutStream.close()
            Log.d("PATH","ok")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("PATH","error")
        }
        File("/storage/emulated/0").walk().forEach {
            Log.d("PATH",it.toString())
        }*/
    }


    private fun getGalleryPermissions(){
        val rxPermissions = RxPermissions(this);
        var a = rxPermissions
            .request(Manifest.permission.READ_EXTERNAL_STORAGE)
            .subscribe { granted ->
                if (granted) {
                    Toast.makeText(this, "Granted", Toast.LENGTH_LONG).show()
                    selectFromGallery()
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
