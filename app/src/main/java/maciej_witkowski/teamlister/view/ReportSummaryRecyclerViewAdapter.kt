package maciej_witkowski.teamlister.view

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.exifinterface.media.ExifInterface
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.report_row_landscape.view.*
import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.model.PhotoReport
import maciej_witkowski.teamlister.utils.ImageUtils
import java.lang.ref.WeakReference


private val TAG = ReportSummaryRecyclerViewAdapter::class.java.simpleName

class ReportSummaryRecyclerViewAdapter(private var items: List<PhotoReport>?, private val context: Context) : RecyclerView.Adapter<ViewHolderReport>() {

    override fun getItemCount(): Int {
        return if (items != null) {
            items!!.size
        } else
            0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderReport {

        return ViewHolderReport(
                LayoutInflater.from(context).inflate(R.layout.report_row_landscape, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolderReport, position: Int) {
        val path = items!![position].filePath
        val exif = ExifInterface(path)
        val dateTaken = exif.getAttribute(ExifInterface.TAG_DATETIME)
        dateTaken?.let { holder.tvDateTaken.text = context.getString(R.string.report_taken, dateTaken) }
        val dateSend = items!![position].dateSend
        if (dateSend != null)
            holder.tvDateSend.text = context.getString(R.string.report_send, dateSend.toString())
        else
            holder.tvDateSend.text = context.getString(R.string.report_not_send)
        val string = "file://$path"
        if (items?.get(position)?.isSend == true) {
            Glide.with(holder.ivState.context)
                    .load(R.drawable.ic_cloud_done_green_24dp)
                    .into(holder.ivState)
        } else {
            Glide.with(holder.ivState.context)
                    .load(R.drawable.ic_cloud_upload_yellow_24dp)
                    .into(holder.ivState)
        }
        ImageUtils.glideWithRoundedCorners(string, WeakReference(holder.ivPhoto), context)
        holder.tvNumber.text = context.getString(R.string.report_number, position + 1)
    }

}

class ViewHolderReport(view: View) : RecyclerView.ViewHolder(view) {
    val tvNumber = view.tv_report_number!!
    val ivState = view.iv_report_state!!
    val ivPhoto = view.iv_report_photo!!
    val tvDateTaken = view.tv_date_taken!!
    val tvDateSend = view.tv_date_reported!!
}
