package maciej_witkowski.teamlister.model

import android.graphics.Rect
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TextLineLight(val data: PlayerData, val boundingBox: Rect): Parcelable