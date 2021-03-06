package ir.vbile.app.movieom.ui.adapter

import android.view.*
import androidx.recyclerview.widget.*
import com.bumptech.glide.*
import ir.vbile.app.movieom.R
import ir.vbile.app.movieom.data.model.movies.*
import kotlinx.android.synthetic.main.item_genre_movies.view.*

import javax.inject.*

class GenreMoviesAdapter @Inject constructor(private val glide: RequestManager) :
        RecyclerView.Adapter<GenreMoviesAdapter.MoviesViewHolder>() {


    inner class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<ShortMovie>() {
        override fun areItemsTheSame(oldItem: ShortMovie, newItem: ShortMovie): Boolean {
            return oldItem.id == newItem.id


        }

        override fun areContentsTheSame(oldItem: ShortMovie, newItem: ShortMovie): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_genre_movies, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val movie = differ.currentList[position]
        holder.itemView.apply {
            glide.load(movie.poster).into(image)
            glide.load(movie.poster).into(image2)
            glide.load(movie.poster).into(image3)
            title.text = movie.title
            rank.text = movie.imdbRating
            year.text = movie.year
            country.text = movie.country
            setOnClickListener {
                onItemClickListener?.let { it(movie) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((ShortMovie) -> Unit)? = null
    fun setOnItemClickListener(listener: (ShortMovie) -> Unit) {
        onItemClickListener = listener
    }
}