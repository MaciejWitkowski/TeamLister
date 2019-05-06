package maciej_witkowski.teamlister.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_pick_result.*
import maciej_witkowski.teamlister.R
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import android.graphics.Color



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class PickResultFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pick_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //drawLine()
    }

    private fun drawLine(){

        val bm = (ivPhoto.getDrawable() as BitmapDrawable).bitmap
        val canvas = Canvas(bm)
        val rectPaint = Paint()
        rectPaint.color = Color.GRAY
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = 6F
        canvas.drawLine(0F, 0F, 20F, 20F, rectPaint);
    }
}
