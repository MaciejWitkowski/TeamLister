package maciej_witkowski.teamlister.utils

import android.content.Context
import androidx.exifinterface.media.ExifInterface
import android.util.DisplayMetrics
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

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
        fun orientationFromExif(path:String):Orientation{
            val exif = ExifInterface(path)
            val rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            return if (rotation == ExifInterface.ORIENTATION_ROTATE_90||rotation==ExifInterface.ORIENTATION_ROTATE_270) //portrait photo
                Orientation.PORTRAIT
            else
                Orientation.LANDSCAPE
        }


        fun glideToWidth43(path: String, metrics: DisplayMetrics, imageView :ImageView,context:Context){
            val width = metrics.widthPixels
            val orientation = ImageUtils.orientationFromExif(path)
            if (orientation == Orientation.PORTRAIT) //portrait photo
                Glide.with(context)
                    .load(path)
                    .apply(RequestOptions().override(width, (width * 4 / 3)))
                    .into(imageView)
            else
                Glide.with(context)
                    .load(path)
                    .apply(RequestOptions().override(width, width*3/4))
                    .into(imageView)
        }
        }

}