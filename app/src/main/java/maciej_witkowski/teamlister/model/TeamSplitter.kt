package maciej_witkowski.teamlister.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import androidx.core.content.ContextCompat
import maciej_witkowski.teamlister.R

private val TAG = TeamSplitter::class.java.simpleName
private const val DEFAULT_SPLIT_RATIO =0.25 //other should be used for "name 10 10 name" pattern.

class TeamSplitter constructor(private val inputData: MutableList<TextLineLight>, val inputImage: Bitmap, private val appContext: Context) {
    var team1 = mutableListOf<PlayerData>()
    var team2 = mutableListOf<PlayerData>()
    init {
        splitAuto()
    }

    fun splitAuto() {
        Log.d(TAG, "splitAuto")
        val imageHeight = inputImage.height
        val imageWidth = inputImage.width
        val canvas = Canvas(inputImage)
        val team1Paint = Paint()
        team1Paint.color = ContextCompat.getColor(appContext, R.color.team_1)
        team1Paint.style = Paint.Style.STROKE
        team1Paint.strokeWidth = 6F
        val team2Paint = Paint()
        team2Paint.color = ContextCompat.getColor(appContext, R.color.team_2)
        team2Paint.style = Paint.Style.STROKE
        team2Paint.strokeWidth = 6F
        val teamFirst = mutableListOf<PlayerData>()
        val teamSecond = mutableListOf<PlayerData>()
        if (inputData.size > 0) {//TODO split function to splitting and drawing
            val min = inputData.minBy { it.boundingBox.left }
            inputData.sortBy { it.boundingBox.top }
            for (line in inputData) {

                if (line.boundingBox.left < (min!!.boundingBox.left + (imageWidth * DEFAULT_SPLIT_RATIO))) {
                    canvas.drawRect(line.boundingBox, team1Paint)
                    teamFirst.add(line.data)
                    Log.d(TAG, "Team1: " + line.data)
                } else {
                    canvas.drawRect(line.boundingBox, team2Paint)
                    teamSecond.add(line.data)
                    Log.d(TAG, "Team2: " + line.data)
                }
            }
        }
        Log.d(TAG, "Splittersize: " + team1.size.toString())
        team1 = teamFirst
        team2 = teamSecond
    }


    fun splitToTeam1() {
        val canvas = Canvas(inputImage)
        val paint = Paint()
        paint.color = ContextCompat.getColor(appContext, R.color.team_1)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 6F
        val teamFirst = mutableListOf<PlayerData>()
        val teamSecond = mutableListOf<PlayerData>()
        if (inputData.size > 0) {
            for (line in inputData) {
                Log.d(TAG, "Team 1 drawing")
                canvas.drawRect(line.boundingBox, paint)
                teamFirst.add(line.data)
            }
        }
        team1 = teamFirst
        team2 = teamSecond
    }

    fun splitToTeam2() {
        val canvas = Canvas(inputImage)
        val paint = Paint()
        paint.color = ContextCompat.getColor(appContext, R.color.team_2)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 6F
        val teamFirst = mutableListOf<PlayerData>()
        val teamSecond = mutableListOf<PlayerData>()
        if (inputData.size > 0) {
            for (line in inputData) {
                Log.d(TAG, "Team 2 drawing")
                canvas.drawRect(line.boundingBox, paint)
                teamSecond.add(line.data)
            }
        }
        team1 = teamFirst
        team2 = teamSecond
    }
}