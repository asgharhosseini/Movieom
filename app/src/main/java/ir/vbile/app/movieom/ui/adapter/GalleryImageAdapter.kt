package ir.vbile.app.movieom.ui.adapter

import android.view.*
import androidx.recyclerview.widget.*
import com.bumptech.glide.*
import ir.vbile.app.movieom.R
import kotlinx.android.synthetic.main.item_gallery_image.view.*
import javax.inject.*

class GalleryImageAdapter @Inject constructor(private val glide: RequestManager) :
        RecyclerView.Adapter<GalleryImageAdapter.MoviesViewHolder>() {


    inner class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem


        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_gallery_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val image = differ.currentList[position]
        holder.itemView.apply {
            glide.load(image).into(ivGalleryImage)

            setOnClickListener {
                onItemClickListener?.let { it(image) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((String) -> Unit)? = null
    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }
}