package ir.vbile.app.movieom.ui.adapter

import android.view.*
import androidx.recyclerview.widget.*

import ir.vbile.app.movieom.*
import ir.vbile.app.movieom.data.model.genre.*
import kotlinx.android.synthetic.main.item_home_genre.view.*
import javax.inject.*

class GenreAdapter: RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    inner class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Genre>() {
        override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
            return oldItem.id == newItem.id


        }

        override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        return GenreViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_home_genre, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = differ.currentList[position]
        holder.itemView.apply {
            title.text=genre.name
            setOnClickListener {
                onItemClickListener?.let { it(genre) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Genre) -> Unit)? = null
    fun setOnItemClickListener(listener:(Genre)->Unit){
        onItemClickListener=listener
    }
}