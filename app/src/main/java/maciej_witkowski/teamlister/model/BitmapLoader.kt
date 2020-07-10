package maciej_witkowski.teamlister.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import android.util.Rational
import androidx.exifinterface.media.ExifInterface
import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.utils.ImageUtils

private val TAG = BitmapLoader::class.java.simpleName

private const val MAX_HEIGHT=4096
private const val MAX_WIDTH=4096

class BitmapLoader(val context: Context) {
    var photoExist =false

    fun getBitmap(path:String):Bitmap{
        Log.d(TAG, path)
        try {
            val exif = ExifInterface(path)//TODO throws FileNotFoundException
            val rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            val rotationInDegrees = ImageUtils.exifToDegrees(rotation)
            val myBitmap = BitmapFactory.decodeFile(path)
            val mutableImage = myBitmap.copy(Bitmap.Config.ARGB_8888, true)
            myBitmap.recycle()
            val matrix = Matrix()
            var height = mutableImage.height
            var width = mutableImage.width
            val rational =Rational(height,width)// 4:3
/*
        if (height>width){
            if (height> MAX_HEIGHT) {
                height= MAX_HEIGHT
            }
            width=height*(mutableImage.height/mutableImage.width)
        }
        else if (width>height){
            if (width> MAX_WIDTH){
                width= MAX_WIDTH
            }
            height=width*(mutableImage.height/mutableImage.width)
        }
*/
            if (height > MAX_HEIGHT)//TODO Better scaling for big files
                height = MAX_HEIGHT
            if (width > MAX_WIDTH)
                width = MAX_WIDTH

            if (rotation != 0) {
                matrix.preRotate(rotationInDegrees)
                photoExist=true
                return Bitmap.createBitmap(mutableImage, 0, 0, width, height, matrix, true)
            }
            else if (myBitmap.height > MAX_HEIGHT || myBitmap.width > MAX_WIDTH) {
                photoExist=true
                return Bitmap.createBitmap(mutableImage, 0, 0, width, height)
            }
            photoExist=true
            return mutableImage
        } catch (e: Exception) {
            photoExist=false
            return getDefault()
        }
    }

    fun getDefault():Bitmap{
        val bitmap=BitmapFactory.decodeResource(context.resources, R.drawable.tl_no_photo_temp)
        val mutableImage = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        bitmap.recycle()
        return mutableImage
    }


}