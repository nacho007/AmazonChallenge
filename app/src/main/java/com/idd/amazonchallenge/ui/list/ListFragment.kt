package com.idd.amazonchallenge.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.idd.amazonchallenge.BuildConfig
import com.idd.amazonchallenge.R
import com.idd.amazonchallenge.constants.LOCAL
import com.idd.amazonchallenge.constants.NETWORK
import com.idd.amazonchallenge.constants.PAGE_SIZE
import com.idd.amazonchallenge.databinding.FragmentListBinding
import com.idd.amazonchallenge.ui.MainActivity
import com.idd.amazonchallenge.utils.Utils
import com.idd.amazonchallenge.utils.toast
import com.idd.domain.models.reddit.RedditResponseDataChildrenData
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by ignaciodeandreisdenis on 1/7/21.
 */
class ListFragment : Fragment() {

    private val binding by lazy { FragmentListBinding.inflate(layoutInflater) }
    private lateinit var viewAdapter: AdapterItem
    private val viewModel: ListViewModel by viewModel()
    private var scrollListener: EndlessRecyclerViewScrollListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            srl.setColorSchemeResources(
                R.color.purple_500,
                R.color.purple_500,
                R.color.purple_500
            )

            srl.setOnRefreshListener {
                viewModel.deleteAllPost()
                scrollListener?.resetState()
                getRedditEntries()
            }

            tvEmpty.visibility = GONE
            btnDismissAll.setOnClickListener { removeAllItems() }
        }

        observeViewModel()
        viewModel.loadData()
    }

    private fun createListener() {
        scrollListener = object :
            EndlessRecyclerViewScrollListener(binding.rvItems.layoutManager as LinearLayoutManager) {

            public override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                if (viewAdapter.itemCount / PAGE_SIZE >= page) {
                    viewAdapter.dataSet[viewAdapter.itemCount - 1].isLoading = true
                    getRedditEntries()
                }
            }
        }
    }

    private fun getRedditEntries() {
        if (BuildConfig.FLAVOR == LOCAL) {
            viewModel.getLocalRedditEntries()
        } else if (BuildConfig.FLAVOR == NETWORK) {
            viewModel.getNetWorkRedditEntries()
        }
    }

    private fun updatePostStatus(id: String) {
        if (BuildConfig.FLAVOR == LOCAL) {
            viewModel.updateLocalPostStatus(id)
        } else if (BuildConfig.FLAVOR == NETWORK) {
            viewModel.updateNetworkPostStatus(id)
        }
    }

    private fun observeViewModel() {
        viewModel.stateLiveData.observe(viewLifecycleOwner, {
            if (it.isLoading && !binding.srl.isRefreshing) {
                binding.pbLoader.visibility = VISIBLE
            } else {
                binding.pbLoader.visibility = GONE
            }

            it.redditEntries?.let { redditResponse ->
                if (binding.rvItems.adapter == null) {
                    binding.rvItems.apply {
                        addItemDecoration(
                            DividerItemDecoration(
                                context,
                                DividerItemDecoration.VERTICAL
                            )
                        )
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(context)

                        createListener()
                        addOnScrollListener(scrollListener as RecyclerView.OnScrollListener)

                        viewAdapter = AdapterItem(redditResponse) { itemResponse ->
                            when {
                                itemResponse.avatarUrl.isNotEmpty() -> {
                                    Utils.openWebBrowser(requireContext(), itemResponse.avatarUrl)
                                }
                                itemResponse.removingPost -> {
                                    itemResponse.item?.let { redditItem ->
                                        removeItem(itemResponse.view, redditItem)
                                    }
                                }
                                else -> {
                                    itemResponse.item?.id?.let { id ->
                                        updatePostStatus(id)
                                    }

                                    viewAdapter.pressedPost(
                                        binding.rvItems.getChildAdapterPosition(
                                            itemResponse.view
                                        )
                                    )
                                    itemResponse.item?.let { redditItem ->
                                        (activity as MainActivity).setDetail(redditItem)
                                    }
                                }
                            }
                        }
                        adapter = viewAdapter
                    }

                    viewAdapter.stateRestorationPolicy =
                        RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                } else {
                    viewAdapter.dataSet = redditResponse
                    viewAdapter.notifyDataSetChanged()
                    binding.srl.isRefreshing = false
                    context?.toast(getString(R.string.list_updated))
                }

                binding.tvEmpty.visibility =
                    if (it.redditEntries.isNotEmpty()) {
                        GONE
                    } else {
                        VISIBLE
                    }

            }
        })
    }

    private fun removeAllItems() {
        binding.tvEmpty.visibility = VISIBLE
        for (i in viewAdapter.itemCount - 1 downTo 0) {
            viewAdapter.deleteItem(i)
        }
        viewModel.deleteAllPost()
    }

    private fun removeItem(view: View, item: RedditResponseDataChildrenData) {
        viewAdapter.deleteItem(binding.rvItems.getChildAdapterPosition(view))
        viewModel.deletePost(item)
    }

    companion object {
        const val REDDIT_PARAMS = "redditParams"
    }

}
