package maciej_witkowski.teamlister.model

import android.graphics.Bitmap


data class BitmapWithExif(val bitmap: Bitmap, val iso: String, val shutter:String)