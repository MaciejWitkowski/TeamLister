package maciej_witkowski.teamlister.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.exifinterface.media.ExifInterface
import android.util.DisplayMetrics
import android.util.Log
import android.util.Rational
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.lang.ref.WeakReference

private val TAG  = ImageUtils::class.java.simpleName
class ImageUtils {
    companion object {
        fun exifToDegrees(exifOrientation: Int): Float {
            return when (exifOrientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }
        }

        fun orientationFromExif(path: String): Orientation {
            val exif = ExifInterface(path)
            val rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            return if (rotation == ExifInterface.ORIENTATION_ROTATE_90 || rotation == ExifInterface.ORIENTATION_ROTATE_270) //portrait photo
                Orientation.PORTRAIT
            else
                Orientation.LANDSCAPE
        }

        fun rescaleBitmapToWidthHeight(bitmap: Bitmap, screenWidth:Int, screenHeight: Int): Bitmap {
            val imageWidth =bitmap.width
            val imageHeight = bitmap.height
            val output = if (Rational(imageWidth, imageHeight) == Rational(4, 3) || Rational(imageWidth, imageHeight) == Rational(3, 4)) {//4:3
                if (imageHeight > imageWidth)//portrait
                    Bitmap.createScaledBitmap(bitmap, screenWidth, screenWidth * imageHeight / imageWidth, false)
                else    //landscape
                    Bitmap.createScaledBitmap(bitmap, screenWidth, screenWidth * imageWidth / imageHeight, false)
            } else if (Rational(imageWidth, imageHeight) == Rational(1, 1)) {// 1:1
                Bitmap.createScaledBitmap(bitmap, screenWidth, screenWidth, false)
            } else { //wide
                if (imageHeight > imageWidth)//portrait
                    Bitmap.createScaledBitmap(bitmap, screenHeight* imageWidth / imageHeight, screenHeight, false)
                else    //landscape
                    Bitmap.createScaledBitmap(bitmap, screenWidth, screenWidth*imageHeight/imageWidth, false)
            }
            Log.d(TAG, screenWidth.toString()+ screenHeight.toString())
            Log.d(TAG,output.width.toString()+output.height.toString())

            return output
        }

        fun rescaleBitmapToWidth(bitmap: Bitmap,screenWidth: Int):Bitmap{
            val imageWidth =bitmap.width
            val imageHeight = bitmap.height
            val output = if (Rational(imageWidth, imageHeight) == Rational(4, 3) || Rational(imageWidth, imageHeight) == Rational(3, 4)) {//4:3
                if (imageHeight > imageWidth)//portrait
                    Bitmap.createScaledBitmap(bitmap, screenWidth, screenWidth * imageHeight / imageWidth, false)
                else    //landscape
                    Bitmap.createScaledBitmap(bitmap, screenWidth, screenWidth * imageWidth / imageHeight, false)
            } else if (Rational(imageWidth, imageHeight) == Rational(1, 1)) {// 1:1
                Bitmap.createScaledBitmap(bitmap, screenWidth, screenWidth, false)
            } else { //wide
                if (imageHeight > imageWidth)//portrait
                    Bitmap.createScaledBitmap(bitmap, screenWidth, screenWidth*imageHeight/imageWidth, false)
                else    //landscape
                    Bitmap.createScaledBitmap(bitmap, screenWidth, screenWidth*imageHeight/imageWidth, false)
            }
            return output
        }


        fun glideDefault(path: String, metrics: DisplayMetrics, imageViewRef: WeakReference<ImageView>, context: Context) {
            val imageView = imageViewRef.get() ?: return
                Glide.with(context)
                    .load(path)
                    .apply(RequestOptions().fitCenter())
                    .into(imageView)
        }
        fun glideWithRoundedCorners(path: String, imageViewRef: WeakReference<ImageView>, context: Context) {
            val imageView = imageViewRef.get() ?: return
            Glide.with(context)
                    .load(path)
                    .apply(RequestOptions().transform(RoundedCorners(100), FitCenter()))
                    .into(imageView)
        }
    }

}