package maciej_witkowski.teamlister.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.simple_tv_row.view.*
import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.model.PlayerData

class RawTeamAdapter(private var items : MutableList<PlayerData>?, private val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return if (items!=null){
            items!!.size
        } else
           0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.simple_tv_row, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvNumber.text = items?.get(position)?.number+" "+ items?.get(position)?.name
    }

}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val tvNumber = view.textView!!
}