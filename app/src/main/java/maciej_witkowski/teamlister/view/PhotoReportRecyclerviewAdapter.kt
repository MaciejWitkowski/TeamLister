package maciej_witkowski.teamlister.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.report_row.view.*
import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.model.PhotoReport


class PhotoReportRecyclerviewAdapter(private var items : List<PhotoReport>?, private val context: Context) : RecyclerView.Adapter<ViewHolderReport>() {

    override fun getItemCount(): Int {
        return if (items!=null){
            items!!.size
        } else
            0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderReport {
        return ViewHolderReport(LayoutInflater.from(context).inflate(R.layout.report_row, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolderReport, position: Int) {
        holder.tvPath.text = items?.get(position)?.filePath
        holder.tvState.text=items?.get(position)?.isSend.toString()

    }

}

class ViewHolderReport (view: View) : RecyclerView.ViewHolder(view) {
    val tvPath = view.tv_report_path!!
    val tvState =view.tv_report_state!!
}