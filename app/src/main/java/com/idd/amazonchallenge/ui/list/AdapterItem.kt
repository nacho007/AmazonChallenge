package com.idd.amazonchallenge.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.idd.amazonchallenge.R
import com.idd.amazonchallenge.databinding.AdapterItemBinding
import com.idd.amazonchallenge.utils.TimeUtils
import com.idd.domain.models.reddit.RedditResponseDataChildrenData

/**
 * Created by ignaciodeandreisdenis on 1/6/21.
 */
class AdapterItem(
    var dataSet: MutableList<RedditResponseDataChildrenData>,
    private val itemClickListener: (
        itemResponse: ItemResponse
    ) -> Unit
) : RecyclerView.Adapter<AdapterItem.ViewHolder>() {

    var isLoading = false

    data class ItemResponse(
        val view: View,
        val item: RedditResponseDataChildrenData? = null,
        val avatarUrl: String = "",
        val removingPost: Boolean = false
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setItem(dataSet[position], isLoading)
    }

    override fun getItemCount() = dataSet.size

    fun deleteItem(position: Int) {
        if (dataSet.isNotEmpty() && position != RecyclerView.NO_POSITION) {
            dataSet.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun pressedPost(position: Int) {
        dataSet[position].readPost = true
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: AdapterItemBinding,
        private val itemClickListener: (
            itemResponse: ItemResponse
        ) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setItem(item: RedditResponseDataChildrenData, isLoading: Boolean) {
            if (item.readPost) {
                binding.ivStatus.visibility = GONE
            } else {
                binding.ivStatus.visibility = VISIBLE
            }


            binding.pbLoader.visibility = GONE
//            if (isLoading) {
//                binding.pbLoader.visibility = VISIBLE
//            } else {
//                binding.pbLoader.visibility = GONE
//            }

            binding.tvAuthor.text = item.author
            binding.tvTitle.text = item.title
            binding.tvCreationDate.text =
                TimeUtils.entryDateFormatter(itemView.context, item.created)
            binding.tvNumComments.text =
                itemView.context.getString(R.string.comments, item.numComments.toString())

            if (item.thumbnail.isNotEmpty() && URLUtil.isValidUrl(item.thumbnail)) {
                Glide.with(itemView.context)
                    .load(item.thumbnail)
                    .placeholder(
                        ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.ic_baseline_image_24
                        )
                    )
                    .into(binding.ivAvatar)
                binding.ivAvatar.visibility = VISIBLE
            } else {
                binding.ivAvatar.visibility = GONE
            }

            binding.ivAvatar.setOnClickListener {
                itemClickListener.invoke(
                    ItemResponse(
                        view = binding.root,
                        avatarUrl = item.url
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
