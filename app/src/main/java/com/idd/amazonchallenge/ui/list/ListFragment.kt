package com.idd.amazonchallenge.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.idd.amazonchallenge.R
import com.idd.amazonchallenge.databinding.FragmentListBinding
import com.idd.amazonchallenge.ui.MainActivity
import com.idd.amazonchallenge.utils.Utils
import com.idd.amazonchallenge.utils.toast
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by ignaciodeandreisdenis on 1/7/21.
 */
class ListFragment : Fragment() {

    private val binding by lazy { FragmentListBinding.inflate(layoutInflater) }
    private lateinit var viewAdapter: AdapterItem
    private val viewModel: ListViewModel by viewModel()

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
                viewModel.getLocalRedditEntries(true)
            }

            btnDismissAll.setOnClickListener { removeAllItems() }
        }
        observeViewModel()
        viewModel.getLocalRedditEntries(false)
    }

    private fun observeViewModel() {
        viewModel.stateLiveData.observe(viewLifecycleOwner, {
            val isRefreshing = it.isRefreshing

            if (it.isLoading) {
                binding.pbLoader.visibility = View.VISIBLE
            } else {
                binding.pbLoader.visibility = View.GONE
            }

            it.redditEntries?.let { redditResponse ->
                if (isRefreshing) {
                    viewAdapter.dataSet =
                        redditResponse.redditResponseData.children?.toMutableList()
                            ?: arrayListOf()
                    viewAdapter.notifyDataSetChanged()
                    binding.srl.isRefreshing = false
                    context?.toast(getString(R.string.list_updated))
                } else {
                    binding.rvItems.apply {
                        addItemDecoration(
                            DividerItemDecoration(
                                context,
                                DividerItemDecoration.VERTICAL
                            )
                        )
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(context)

                        viewAdapter = AdapterItem(
                            redditResponse.redditResponseData.children?.toMutableList()
                                ?: mutableListOf()
                        ) { view, item, avatarUrl ->
                            when {
                                avatarUrl?.isNotEmpty() == true -> {
                                    Utils.openWebBrowser(requireContext(), avatarUrl)
                                }
                                item == null -> {
                                    removeItem(view)
                                }
                                else -> {
                                    viewModel.updateLocalPostStatus(item.data.id)
                                    viewAdapter.pressedPost(
                                        binding.rvItems.getChildAdapterPosition(
                                            view
                                        )
                                    )
                                    (activity as MainActivity).setDetail(item)
                                }
                            }
                        }
                        adapter = viewAdapter
                    }
                }
            }
        })
    }

    private fun removeAllItems() {
        for (i in viewAdapter.itemCount - 1 downTo 0) {
            viewAdapter.deleteItem(i)
        }
    }

    private fun removeItem(view: View) {
        viewAdapter.deleteItem(binding.rvItems.getChildAdapterPosition(view))
    }

    companion object {
        const val REDDIT_PARAMS = "redditParams"
    }

}
