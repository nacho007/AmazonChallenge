package com.idd.amazonchallenge.utils

import android.content.Context
import android.widget.Toast

/**
 * Created by ignaciodeandreisdenis on 1/6/21.
 */
fun Context.toast(text: String?) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}