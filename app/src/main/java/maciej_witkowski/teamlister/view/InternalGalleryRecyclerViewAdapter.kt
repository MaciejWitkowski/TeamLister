package maciej_witkowski.teamlister.view

import android.content.Context
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
import kotlinx.android.synthetic.main.internal_photo_row.view.*
import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.utils.ImageUtils
import maciej_witkowski.teamlister.utils.Orientation


private const val LANDSCAPE = 0
private const val PORTRAIT = 1
private val TAG = InternalGalleryRecyclerViewAdapter::class.java.simpleName

class InternalGalleryRecyclerViewAdapter(
    private var items: List<String>?,
    private val context: Context,
    private val screenWidth: Int
) :
    RecyclerView.Adapter<ViewHolderInternalPhoto>() {
    var onItemClick: (Int) -> Unit = {}
    override fun getItemCount(): Int {
        return if (items != null) {
            items!!.size
        } else
            0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderInternalPhoto {
        if (viewType == PORTRAIT)
            return ViewHolderInternalPhoto(
                LayoutInflater.from(context).inflate(
                    R.layout.internal_photo_row,
                    parent,
                    false
                )
            )
        else
            return ViewHolderInternalPhoto(
                LayoutInflater.from(context).inflate(
                    R.layout.internal_photo_row,
                    parent,
                    false
                )
            )
    }

    override fun getItemViewType(position: Int): Int {
        val orientation = ImageUtils.orientationFromExif(items!![position])
        return if (orientation == Orientation.PORTRAIT) //portrait photo
            PORTRAIT
        else
            LANDSCAPE
    }

    override fun onBindViewHolder(holder: ViewHolderInternalPhoto, position: Int) {
        val path = items!![position]
        val exif = ExifInterface(path)
        val dateTaken = exif.getAttribute(ExifInterface.TAG_DATETIME)
        dateTaken?.let { holder.tvDateTaken.text = context.getString(R.string.report_taken, dateTaken) }
        val string = "file://$path"
        val uri = string.toUri()
       holder.itemView.setOnClickListener {
            onItemClick(holder.adapterPosition)
        }

        if (holder.itemViewType == PORTRAIT) {
            val height = screenWidth * 4 / 3
            val requestOptions =
                RequestOptions().transform(RoundedCorners(100), FitCenter()).override(screenWidth, height)
            Glide.with(holder.ivPhoto.context)
                .load(uri)
                .apply(requestOptions)
                .into(holder.ivPhoto)
        } else {//landscape
            val height = screenWidth * 0.75.toInt()
            val requestOptions = RequestOptions().transform(RoundedCorners(100), FitCenter())
                .override(screenWidth, height)
            Glide.with(holder.ivPhoto.context)
                .load(uri)
                .apply(requestOptions)
                .into(holder.ivPhoto)
        }
    }

}







class ViewHolderInternalPhoto(view: View) : RecyclerView.ViewHolder(view) {
    val ivPhoto = view.iv_internal_photo!!
    val tvDateTaken = view.tv_internal_photo_date!!
}
