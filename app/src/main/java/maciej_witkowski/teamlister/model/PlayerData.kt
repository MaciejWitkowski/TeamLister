package maciej_witkowski.teamlister.model
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlayerData(val number: String, val name: String):Parcelable