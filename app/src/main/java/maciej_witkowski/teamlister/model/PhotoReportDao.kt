package maciej_witkowski.teamlister.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.*

@Dao
interface PhotoReportDao {
    @Query("SELECT * FROM photoReport")
    fun getAll():LiveData<List<PhotoReport>>

    @Query("SELECT * FROM photoReport")
    fun getRaw():List<PhotoReport>

    @Query("SELECT * FROM photoReport WHERE uid = :id")
    fun getPhotoReport(id: String): PhotoReport

    @Query("SELECT file_path FROM photoReport")
    fun getPaths():List<String>

/*    @Query("UPDATE photoReport SET is_send=:isSend, date_send=:date  WHERE file_path = :path")
    fun update(path:String, isSend:Boolean, date:Date)*/

    @Query("UPDATE photoReport SET is_send=:isSend  WHERE file_path = :path")
    fun update(path:String, isSend:Boolean)

    @Query("DELETE FROM photoreport")
    fun deleteAll()

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertPhotoReport(photoReport: PhotoReport)
}
