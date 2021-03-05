package ir.vbile.app.movieom.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*

abstract class BaseAdapter<T>(
        private val layoutId: Int
) : RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {

    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    protected val diffCallback = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    protected abstract val differ: AsyncListDiffer<T>

    var items: List<T>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        layoutId,
                        parent,
                        false
                )
        )
    }

    protected var onItemClickListener: ((T) -> Unit)? = null

    fun setItemClickListener(listener: (T) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return items.size
    }
}