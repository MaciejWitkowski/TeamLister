package maciej_witkowski.teamlister.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import maciej_witkowski.teamlister.utils.ImageUtils

private val TAG = BitmapLoader::class.java.simpleName

class BitmapLoader {
    private val MAX_HEIGHT=4096
    private val MAX_WIDTH=4096

    fun getBitmap(path:String):Bitmap{
        Log.d(TAG, path)
        val exif = ExifInterface(path)
        val rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val rotationInDegrees = ImageUtils.exifToDegrees(rotation)
        val myBitmap = BitmapFactory.decodeFile(path)//TODO OOM error after few loads
        val mutableImage = myBitmap.copy(Bitmap.Config.ARGB_8888, true)
        myBitmap.recycle()
        val matrix = Matrix()
        var height = mutableImage.height
        var width = mutableImage.width
        if (height > MAX_HEIGHT)//TODO Better scaling for big files, always should be mutable
            height = MAX_HEIGHT
        if (width > MAX_WIDTH)
            width = MAX_WIDTH
        if (rotation != 0) {
            matrix.preRotate(rotationInDegrees)
            return Bitmap.createBitmap(mutableImage, 0, 0, width, height, matrix, true)
        }
        else if (myBitmap.height > MAX_HEIGHT || myBitmap.width > MAX_WIDTH) {
            return Bitmap.createBitmap(mutableImage, 0, 0, width, height)
        }
        return mutableImage
    }

}