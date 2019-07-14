package maciej_witkowski.teamlister.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class PhotoReport(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @ColumnInfo(name = "file_path") val filePath: String,
    @ColumnInfo(name= "is_send") val isSend: Boolean,
    @ColumnInfo(name= "date_send") val dateSend: Date?
)
