package com.idd.amazonchallenge.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.idd.amazonchallenge.R
import com.idd.amazonchallenge.databinding.AdapterItemBinding
import com.idd.amazonchallenge.utils.TimeUtils
import com.idd.domain.models.reddit.RedditResponseDataChildren

/**
 * Created by ignaciodeandreisdenis on 1/6/21.
 */
class AdapterItem(
    var dataSet: MutableList<RedditResponseDataChildren>,
    private val itemClickListener: (
        itemResponse: ItemResponse
    ) -> Unit
) : RecyclerView.Adapter<AdapterItem.ViewHolder>() {

    data class ItemResponse(
        val view: View,
        val item: RedditResponseDataChildren? = null,
        val avatarUrl: String = "",
        val removingPost: Boolean = false
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setItem(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

    fun deleteItem(position: Int) {
        if (dataSet.isNotEmpty() && position != RecyclerView.NO_POSITION) {
            dataSet.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun pressedPost(position: Int) {
        dataSet[position].data.visited = true
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: AdapterItemBinding,
        private val itemClickListener: (
            itemResponse: ItemResponse
        ) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setItem(item: RedditResponseDataChildren) {
            if (item.data.visited) {
                binding.ivStatus.visibility = View.GONE
            } else {
                binding.ivStatus.visibility = View.VISIBLE
            }

            binding.tvAuthor.text = item.data.author
            binding.tvTitle.text = item.data.title
            binding.tvCreationDate.text =
                TimeUtils.entryDateFormatter(itemView.context, item.data.created)
            binding.tvNumComments.text =
                itemView.context.getString(R.string.comments, item.data.numComments.toString())

            if (item.data.thumbnail.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(item.data.thumbnail)
                    .into(binding.ivAvatar)
                binding.ivAvatar.visibility = View.VISIBLE
            } else {
                binding.ivAvatar.visibility = View.GONE
            }

            binding.ivAvatar.setOnClickListener {
                itemClickListener.invoke(
                    ItemResponse(
                        view = binding.root,
                        avatarUrl = item.data.url
                    )
                )
            }

            binding.clContent.setOnClickListener {
                itemClickListener.invoke(ItemResponse(view = binding.root, item = item))
            }

            binding.tvDismissPost.setOnClickListener {
                itemClickListener.invoke(
                    ItemResponse(
                        view = binding.root,
                        item = item,
                        removingPost = true
                    )
                )
            }
        }
    }
}
