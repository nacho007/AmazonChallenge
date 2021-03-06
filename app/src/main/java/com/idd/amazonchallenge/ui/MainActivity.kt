package com.idd.amazonchallenge.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.idd.amazonchallenge.databinding.ActivityMainBinding
import com.idd.amazonchallenge.ui.details.DetailFragment
import com.idd.amazonchallenge.ui.list.ListFragment
import com.idd.domain.models.reddit.RedditParams
import com.idd.domain.models.reddit.RedditResponseDataChildrenData

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    fun setDetail(item: RedditResponseDataChildrenData) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fcvDetails.id, DetailFragment().apply {
                val args = Bundle()
                args.putSerializable(
                    ListFragment.REDDIT_PARAMS,
                    RedditParams(item.author ?: "", item.thumbnail, item.title)
                )
                arguments = args
            }).addToBackStack(null).commit()
    }

}