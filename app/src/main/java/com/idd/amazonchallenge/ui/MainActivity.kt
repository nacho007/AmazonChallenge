package com.idd.amazonchallenge.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.idd.amazonchallenge.databinding.ActivityMainBinding
import com.idd.amazonchallenge.utils.Utils
import com.idd.domain.repositories.RedditLocalRepository
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val redditLocalRepository: RedditLocalRepository by inject()
    private lateinit var viewAdapter: AdapterItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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
                redditLocalRepository.getReddits().redditResponseData.children?.toMutableList()
                    ?: mutableListOf()
            ) { _, _, avatarUrl ->
                when {
                    avatarUrl?.isNotEmpty() == true -> {
                        Utils.openWebBrowser(this@MainActivity, avatarUrl)
                    }
                }
            }
            adapter = viewAdapter
        }
    }

}