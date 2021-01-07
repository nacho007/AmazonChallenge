package com.idd.infrastructure.repositories

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.idd.domain.repositories.SavePictureRepository
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

/**
 * Created by ignaciodeandreisdenis on 1/7/21.
 */
class SavePictureRepositoryImpl(
    private val context: Context
) : SavePictureRepository {
    override fun <T> savePicture(param: T): Boolean {
        val filename = "${System.currentTimeMillis()}.jpg"

        var fos: OutputStream? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, IMAGE_JPG)
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            (param as Bitmap).compress(Bitmap.CompressFormat.JPEG, 100, it)
            return true
        }

        return false
    }

    companion object {
        const val IMAGE_JPG = "image/jpg"
    }
}