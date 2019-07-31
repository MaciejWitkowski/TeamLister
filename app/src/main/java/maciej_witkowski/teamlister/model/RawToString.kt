package maciej_witkowski.teamlister.model

import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.utils.CaseFormat
import maciej_witkowski.teamlister.utils.RemoveBracketFormat
import maciej_witkowski.teamlister.utils.TextUtils

private val TAG = RawToString::class.java.simpleName
class RawToString {

    fun rawToTeam(data: MutableList<PlayerData>?,context: Context):String {
        val sharedPref = getDefaultSharedPreferences(context)
        val fixT = sharedPref.getBoolean("fixt", true)
        val replaceAscii = sharedPref.getBoolean("replace_ascii", true)
        val append = sharedPref.getString("append_t2", null)
        val prepend = sharedPref.getString("prepend_t2", null)
        val numberPosition = sharedPref.getString("number", "start")!!
        val caseEnum = try {
            CaseFormat.valueOf(sharedPref.getString("case", "UPPER_LOWER")!!)
        } catch (e: IllegalArgumentException) {
            Log.d(TAG, "INVALID CaseFormat value: $e")
            CaseFormat.UPPER_LOWER
        }
        val bracketsEnum = try {
            RemoveBracketFormat.valueOf(sharedPref.getString("brackets", "NONE")!!)
        } catch (e: IllegalArgumentException) {
            Log.d(TAG, "INVALID RemoveBracketFormat value: $e")
            RemoveBracketFormat.NONE
        }
        val newSb = StringBuilder()
        if (data != null) {
            val names = context.resources.getStringArray(R.array.names).toList()
            Log.d(TAG, "Names size: " + names.size.toString())
            for (line in data) {
                var tmp = line.name
                tmp = TextUtils.fixRandomWrongSigns(tmp)
                if (fixT) {
                    tmp = TextUtils.dictionaryNameFix(tmp, names)
                    tmp = TextUtils.fixWrongT(tmp)  //TODO to builder, now needs to be called in particular order
                }
                tmp = TextUtils.caseFormatting(tmp, caseEnum)
                if (replaceAscii)
                    tmp = TextUtils.replaceNonAsciiChars(tmp)
                tmp = TextUtils.removeBrackets(tmp, bracketsEnum)
                if (numberPosition == "start") {
                    newSb.append(append + line.number + prepend + " " + tmp + "\r\n")
                } else if (numberPosition=="end") {
                    newSb.append(tmp + " " + append + line.number + prepend + "\r\n")
                }
            }
        }
        //newSb.trim()//TODO
        return newSb.toString().trim()
    }
}