package maciej_witkowski.teamlister

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.mindorks.paracamera.Camera
import maciej_witkowski.remoterelease.SettingsFragment
import maciej_witkowski.teamlister.utils.TextRecognitionModel

private const val TAG = "FIREBASE"
private const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1

class MainActivity : AppCompatActivity() {
    private lateinit var camera: Camera
    private val textRecognitionModels = ArrayList<TextRecognitionModel>()
    private var imageHeight=0
    private var imageWidth=0
    private var teamFirst = mutableListOf<FirebaseVisionText.Line>()
    private var teamSecond= mutableListOf<FirebaseVisionText.Line>()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                getCameraPermissions()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                getGalleryPermissions()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_settings ->{Toast.makeText(this.applicationContext, "Settings clicked", Toast.LENGTH_SHORT).show()
                val fragment = SettingsFragment()
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.container, fragment)
                ft.commit()
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
        message.text = "Hello Kotlin"
        camera = Camera.Builder()
            .resetToCorrectOrientation(true)
            .setTakePhotoRequestCode(Camera.REQUEST_TAKE_PHOTO)
            .setDirectory("pics")
            .setName("testlister_${System.currentTimeMillis()}")
            .setImageFormat(Camera.IMAGE_JPEG)
            .setCompression(75)
            .build(this)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
                val bitmap = camera.cameraBitmap
                if (bitmap != null) {
                    analyzeImage(bitmap)
                } else {
                    Toast.makeText(this.applicationContext, "picutre not taken", Toast.LENGTH_SHORT).show()
                }
            }
            else if(requestCode==REQUEST_SELECT_IMAGE_IN_ALBUM){

            }
        }
    }


    private fun analyzeImage(image: Bitmap?) {
        if (image == null) {
            Toast.makeText(this, "There was some error", Toast.LENGTH_SHORT).show()
            return
        }
        imageView.setImageBitmap(null)
        textRecognitionModels.clear()

        val firebaseVisionImage = FirebaseVisionImage.fromBitmap(image)
        val textRecognizer = FirebaseVision.getInstance().onDeviceTextRecognizer
        textRecognizer.processImage(firebaseVisionImage)
            .addOnSuccessListener {
                val mutableImage = image.copy(Bitmap.Config.ARGB_8888, true)
                imageHeight=mutableImage.height
                imageWidth=mutableImage.width
                Log.d(TAG,"Height: "+mutableImage.height.toString()+ " Width: "+mutableImage.width.toString())
                recognizeText(it, mutableImage)

                imageView.setImageBitmap(mutableImage)
            }
            .addOnFailureListener {
                Toast.makeText(this, "There was some error", Toast.LENGTH_SHORT).show()
            }
    }

    private fun recognizeText(result: FirebaseVisionText?, image: Bitmap?) {
        if (result == null || image == null) {
            Toast.makeText(this, "There was some error", Toast.LENGTH_SHORT).show()
            return
        }

        val canvas = Canvas(image)
        val rectPaint = Paint()
        rectPaint.color = Color.RED
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = 4F
        val textPaint = Paint()
        textPaint.color = Color.RED
        textPaint.textSize = 40F

        var index = 0
        message.setText("")
        val sb = StringBuilder()
        var data = mutableListOf<FirebaseVisionText.Line>()
        for (block in result.textBlocks) {
            for (line in block.lines) {
                if (isValidLine(line)) {
                    canvas.drawRect(line.boundingBox, rectPaint)
                    sb.append(line.text)
                    sb.append("\n")
                    //val lineData = NameData(text = line.text, cord = line.getCornerPoints()!![0].y.toFloat())
                    data.add(line)
                }
                textRecognitionModels.add(TextRecognitionModel(index++, line.text))
            }
        }
        sortLines(data)
    }


private fun sortLines(data: MutableList<FirebaseVisionText.Line>){//TODO crashes if empty
    val min=data.minBy {it.cornerPoints!![0].x  }
    Log.d(TAG, "Min: "+min?.cornerPoints!![0].x)
    data.sortBy { it.cornerPoints!![0].y.toFloat() }
    val newSb = StringBuilder()
    for (line in data) {
        if(line.cornerPoints!![0].x<(min.cornerPoints!![0].x+(imageHeight*0.25))) {
            teamFirst.add(line)
            newSb.append(line.text + "\n")
        }
        else{
            teamSecond.add(line)
        }

    }
    teamToLogD(teamFirst)
    Log.d(TAG, "-------------------------------------------")
    teamToLogD(teamSecond)
    message.setText(newSb)
}

    private fun teamToLogD(data: MutableList<FirebaseVisionText.Line>){
        for (line in data) {
            Log.d(TAG, line.text + " " + line.getCornerPoints()!![0].toString())
            Log.d(TAG, line.getCornerPoints()!![1].toString())
            Log.d(TAG, line.getCornerPoints()!![2].toString())
            Log.d(TAG, line.getCornerPoints()!![3].toString())
        }
    }


    private fun isValidLine(line:FirebaseVisionText.Line):Boolean{
        var isValid=false
        if (line.text[0].isDigit() && line.text.length > 5)
        isValid=true
        return isValid
    }




    private fun getCameraPermissions() {
        val rxPermissions = RxPermissions(this);
        var a = rxPermissions
            .request(Manifest.permission.CAMERA)
            .subscribe { granted ->
                if (granted) {
                    Toast.makeText(this, "Granted", Toast.LENGTH_LONG).show()
                    takePicture()
                    //startCamera()
                    // Always true pre-M
                    // I can control the camera now
                } else {
                    Toast.makeText(this, "Not Granted", Toast.LENGTH_LONG).show()
                    // permission denied
                }
            }
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

    private fun takePicture() {
        try {
            camera.takePicture()
        } catch (e: Exception) {
            // Show a toast for exception
            Toast.makeText(this.applicationContext, "error", Toast.LENGTH_SHORT).show()
        }
    }


}
