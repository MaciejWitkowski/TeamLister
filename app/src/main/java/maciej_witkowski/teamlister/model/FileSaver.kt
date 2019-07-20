package maciej_witkowski.teamlister.model

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaScannerConnection
import android.os.Environment
import android.util.Log
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import java.io.File
import java.io.FileOutputStream

private val TAG = FileSaver::class.java.simpleName

class FileSaver {
    private fun saveTeam(team: String?, filename: String, folderName: String?, context: Context) {
        if (!team.isNullOrEmpty()) {
            var filepath = Environment.getExternalStorageDirectory().absolutePath.toString()
            if (!folderName.isNullOrEmpty()) {
                filepath = "$filepath/$folderName"
                val folder = File((Environment.getExternalStorageDirectory()).toString() + File.separator + folderName)
                if (!folder.exists()) {
                    folder.mkdir()
                }
            }
            Log.d(TAG, "first path: $filepath")
            val myExternalFile = File(filepath, filename)
            Log.d(TAG, "final path: " + myExternalFile.absolutePath)
            val fileOutPutStream = FileOutputStream(myExternalFile, false)
            fileOutPutStream.write(team.toByteArray())
            fileOutPutStream.flush()
            fileOutPutStream.close()
            myExternalFile.setExecutable(true)
            myExternalFile.setReadable(true)
            myExternalFile.setWritable(true)
            MediaScannerConnection.scanFile(context, arrayOf(myExternalFile.toString()), null, null)
            Log.d(TAG, "ok")
        }
    }


    fun saveToFiles(team1: String?, team2: String?,context: Context) {
        val sharedPref = getDefaultSharedPreferences(context)
        val twoFiles = sharedPref.getBoolean("saveto2", true)
        if (twoFiles)
            save2files(team1, team2, sharedPref, context)
        else
            save1file(team1, team2, sharedPref, context)
    }


    private fun save2files(team1: String?, team2: String?, sharedPref: SharedPreferences, context: Context) {
        val folder = sharedPref.getString("folder", "team_lister")
        var name1 = sharedPref.getString("name_t1", "team1")
        if (!name1.isNullOrEmpty())
            name1 += ".txt"
        else
            name1 = "team1.txt"
        var name2 = sharedPref.getString("name_t2", "team1")
        if (!name2.isNullOrEmpty())
            name2 += ".txt"
        else
            name2 = "team2.txt"
        saveTeam(team1, name1, folder, context)
        saveTeam(team2, name2, folder, context)
    }

    private fun save1file(team1: String?, team2: String?, sharedPref: SharedPreferences, context: Context) {
        val folder = sharedPref.getString("folder", "team_lister")
        var name = sharedPref.getString("name_t1", "team1")
        if (!name.isNullOrEmpty())
            name += ".txt"
        else
            name = "team1.txt"
        saveTeam(team1 + team2, name, folder, context)
    }


}
