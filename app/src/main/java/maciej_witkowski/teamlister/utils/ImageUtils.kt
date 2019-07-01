package maciej_witkowski.teamlister.utils

import androidx.exifinterface.media.ExifInterface


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

    }
}