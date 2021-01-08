package com.idd.amazonchallenge.ui.details

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.idd.amazonchallenge.R
import com.idd.amazonchallenge.databinding.FragmentDetailBinding
import com.idd.amazonchallenge.utils.showPermissionRequestDialog
import com.idd.amazonchallenge.utils.toast
import com.idd.domain.models.reddit.RedditParams
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by ignaciodeandreisdenis on 1/7/21.
 */
class DetailFragment : Fragment() {

    private val binding by lazy { FragmentDetailBinding.inflate(layoutInflater) }
    private val viewModel: DetailViewModel by viewModel()

    var redditParams: RedditParams? = null
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        redditParams = arguments?.getSerializable(REDDIT_PARAMS) as RedditParams?
        setPermissionCallback()
    }

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
        observeViewModel()
        setData()
    }

    private fun observeViewModel() {
        viewModel.stateLiveData.observe(viewLifecycleOwner, {
            if (it.saved) {
                context?.toast(getString(R.string.saved_to_photos))
            }
        })
    }

    private fun setData() {
        binding.apply {
            btnSaveImage.setOnClickListener {
                if (bitmap != null) {
                    checkPermission()
                }
            }

            if (redditParams != null) {
                tvAuthor.text = redditParams?.author

                if (redditParams?.avatarUrl?.isNotEmpty() == true && URLUtil.isValidUrl(redditParams?.avatarUrl)) {
                    btnSaveImage.visibility = View.VISIBLE

                    Glide.with(requireContext())
                        .load(redditParams?.avatarUrl)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                context?.toast(getString(R.string.failed_to_load_image))
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                bitmap = (resource as BitmapDrawable).bitmap
                                return false
                            }

                        })
                        .into(ivAvatar)
                    ivAvatar.visibility = View.VISIBLE
                } else {
                    btnSaveImage.visibility = View.GONE
                    ivAvatar.visibility = View.GONE
                }

                tvTitle.text = redditParams?.title
            }
        }
    }

    private fun setPermissionCallback() {
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    viewModel.savePicture(bitmap)
                }
            }
    }

    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.savePicture(bitmap)
            }
            shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                context?.showPermissionRequestDialog(
                    getString(R.string.permission_title),
                    getString(R.string.write_permission_request)
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    companion object {
        const val REDDIT_PARAMS = "redditParams"
    }
}
