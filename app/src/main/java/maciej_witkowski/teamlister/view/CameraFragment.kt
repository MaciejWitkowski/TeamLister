package maciej_witkowski.teamlister.view


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.mindorks.paracamera.Camera
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.fragment_camera.*

import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.utils.TextRecognitionModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG = "FIREBASE"
private const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
/**
 * A simple [Fragment] subclass.
 *
 */
class CameraFragment : Fragment() {
    private lateinit var camera: Camera
    private val textRecognitionModels = ArrayList<TextRecognitionModel>()
    private var imageHeight=0
    private var imageWidth=0
    private var teamFirst = mutableListOf<FirebaseVisionText.Line>()
    private var teamSecond= mutableListOf<FirebaseVisionText.Line>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        message.text = "Hello Kotlin"
        camera = Camera.Builder()
            .resetToCorrectOrientation(true)
            .setTakePhotoRequestCode(Camera.REQUEST_TAKE_PHOTO)
            .setDirectory("pics")
            .setName("testlister_${System.currentTimeMillis()}")
            .setImageFormat(Camera.IMAGE_JPEG)
            .setCompression(75)
            .build(this)
        getCameraPermissions()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
                val bitmap = camera.cameraBitmap
                if (bitmap != null) {
                    analyzeImage(bitmap)
                } else {
                    Toast.makeText(requireContext(), "picutre not taken", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun analyzeImage(image: Bitmap?) {
        if (image == null) {
            Toast.makeText(requireContext(), "There was some error", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(requireContext(), "There was some error", Toast.LENGTH_SHORT).show()
            }
    }

    private fun recognizeText(result: FirebaseVisionText?, image: Bitmap?) {
        if (result == null || image == null) {
            Toast.makeText(requireContext(), "There was some error", Toast.LENGTH_SHORT).show()
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
        Log.d(TAG, "Min: "+min?.cornerPoints!![0].x)//NPE TODO
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
        //message.setText(newSb)
    }

    private fun teamToLogD(data: MutableList<FirebaseVisionText.Line>){
        for (line in data) {
            Log.d(TAG, line.text + " " + line.getCornerPoints()!![0].toString())
            Log.d(TAG, line.getCornerPoints()!![1].toString())
            Log.d(TAG, line.getCornerPoints()!![2].toString())
            Log.d(TAG, line.getCornerPoints()!![3].toString())
        }
    }


    private fun isValidLine(line: FirebaseVisionText.Line):Boolean{
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
                    Toast.makeText(requireContext(), "Granted", Toast.LENGTH_LONG).show()
                    takePicture()
                    //startCamera()
                    // Always true pre-M
                    // I can control the camera now
                } else {
                    Toast.makeText(requireContext(), "Not Granted", Toast.LENGTH_LONG).show()
                    // permission denied
                }
            }
    }


    private fun takePicture() {
        try {
            camera.takePicture()
        } catch (e: Exception) {
            // Show a toast for exception
            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
        }
    }

}
