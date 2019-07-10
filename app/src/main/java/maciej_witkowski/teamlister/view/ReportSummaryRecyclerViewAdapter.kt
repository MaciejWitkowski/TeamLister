package maciej_witkowski.teamlister.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.report_row_portrait.view.*
import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.model.PhotoReport
private const val LANDSCAPE =0
private const val PORTRAIT =1
private const val TAG ="RSRVA"

class ReportSummaryRecyclerViewAdapter(private var items: List<PhotoReport>?, private val context: Context, private val screenWidth: Int) :
    RecyclerView.Adapter<ViewHolderReportPortrait>() {

    override fun getItemCount(): Int {
        return if (items != null) {
            items!!.size
        } else
            0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderReportPortrait {
        if (viewType == PORTRAIT)
            return ViewHolderReportPortrait(
                LayoutInflater.from(context).inflate(
                    R.layout.report_row_portrait,
                    parent,
                    false
                )
            )
        else
            return ViewHolderReportPortrait(
                LayoutInflater.from(context).inflate(
                    R.layout.report_row_landscape,
                    parent,
                    false
                )
            )
    }

    override fun getItemViewType(position: Int): Int {
        val path = items!![position].filePath
        val exif = ExifInterface(path)
        val rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        Log.d(TAG, rotation.toString())
        return if (rotation==ExifInterface.ORIENTATION_ROTATE_90) //portrait photo
            PORTRAIT
        else
            LANDSCAPE
    }

    override fun onBindViewHolder(holder: ViewHolderReportPortrait, position: Int) {

        if (holder.itemViewType == PORTRAIT) {//portrait
            //holder.tvPath.text = items?.get(position)?.filePath
            if (items?.get(position)?.isSend==true){
                Glide.with(holder.ivState.context)
                    .load(R.drawable.ic_cloud_done_green_24dp)
                    .into(holder.ivState)
            }
            else{
                Glide.with(holder.ivState.context)
                    .load(R.drawable.ic_cloud_upload_yellow_24dp)
                    .into(holder.ivState)
            }
            val path = items?.get(position)?.filePath
            val string = "file://$path"
            val uri = string.toUri()
            val width=(screenWidth*0.75).toInt()
            Log.d(TAG,width.toString())

            val requestOptions = RequestOptions().transform( RoundedCorners(100), FitCenter()).override(width, screenWidth)
           // requestOptions.transformations( RoundedCorners(20))

            Glide.with(holder.ivPhoto.context)
                .load(uri)
                //.transforms(RoundedCorners(20))
                .apply(requestOptions)
                .into(holder.ivPhoto)
        }
        else{//landscape
            //holder.tvPath.text = items?.get(position)?.filePath
            if (items?.get(position)?.isSend==true){
                Glide.with(holder.ivState.context)
                    .load(R.drawable.ic_cloud_done_green_24dp)
                    .into(holder.ivState)
            }
            else{
                Glide.with(holder.ivState.context)
                    .load(R.drawable.ic_cloud_upload_yellow_24dp)
                    .into(holder.ivState)
            }
            val path = items?.get(position)?.filePath
            val string = "file://$path"
            val uri = string.toUri()
            val requestOptions = RequestOptions().transform( RoundedCorners(100), FitCenter()).override(screenWidth, screenWidth*0.75.toInt())
            Glide.with(holder.ivPhoto.context)
                .load(uri)
                .apply(requestOptions)
                .into(holder.ivPhoto)
        }

    }

}

class ViewHolderReportPortrait(view: View) : RecyclerView.ViewHolder(view) {
    //val tvPath = view.tv_report_path!!
    val ivState = view.iv_report_state!!
    val ivPhoto = view.iv_report_photo!!
}
