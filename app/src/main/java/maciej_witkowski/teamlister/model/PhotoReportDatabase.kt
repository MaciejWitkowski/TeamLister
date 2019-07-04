package maciej_witkowski.teamlister.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PhotoReport::class], version = 1)
abstract class PhotoReportDatabase : RoomDatabase() {

    abstract fun photoReportDao(): PhotoReportDao
    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: PhotoReportDatabase? = null

        fun getInstance(context: Context): PhotoReportDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }
        private fun buildDatabase(context: Context): PhotoReportDatabase {
            return Room.databaseBuilder(context, PhotoReportDatabase::class.java, "database-name")
                .enableMultiInstanceInvalidation()
                .build()
        }

    }



}
