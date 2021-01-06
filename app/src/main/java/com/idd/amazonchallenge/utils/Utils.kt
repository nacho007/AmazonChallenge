package com.idd.amazonchallenge.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.idd.amazonchallenge.R

object Utils {

    fun openWebBrowser(context: Context, url: String?) {
        try {
            val intentBrowser = Intent(Intent.ACTION_VIEW)
            intentBrowser.data = Uri.parse(url)
            context.startActivity(intentBrowser)
        } catch (e: ActivityNotFoundException) {
            context.toast(context.getString(R.string.install_a_web_browser))
            e.printStackTrace()
        }
    }
}