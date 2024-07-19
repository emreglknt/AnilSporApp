package com.example.recipeapppaparaproject.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date
import androidx.core.content.FileProvider

fun captureAndShareScreenshot(context: Context, view: View) {
    view.isDrawingCacheEnabled = true
    val bitmap = Bitmap.createBitmap(view.drawingCache)
    view.isDrawingCacheEnabled = false

    val path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath + "/screenshot.jpg"
    val file = File(path)
    try {
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    val uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".fileprovider", file)
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.type = "image/jpeg"
    shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
    shareIntent.setPackage("com.whatsapp")
    context.startActivity(Intent.createChooser(shareIntent, "Share Screenshot"))
}
