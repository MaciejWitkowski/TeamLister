package maciej_witkowski.teamlister.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.vision.text.FirebaseVisionText
import kotlinx.android.synthetic.main.simple_tv_row.view.*
import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.model.TextLineLight


class RawTeamAdapter(private var items : MutableList<TextLineLight>?, private val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return if (items==null){
            0
        } else
            items!!.size
    }

    /*override fun getItemCount(): Int {
            return items.size
    }*/
    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.simple_tv_row, parent, false))
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // holder.tvNumber?.text = items?.get(position)
        holder.tvNumber.text = items?.get(position)?.text
        //setText(String)
    }

    /*  public fun updateRv(data: ArrayList<String>) {
          items=data
          notifyDataSetChanged()
      }*/

}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvNumber = view.textView!!
}