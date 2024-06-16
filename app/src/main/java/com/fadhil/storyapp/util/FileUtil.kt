package com.fadhil.storyapp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.time.Instant


object FileUtil {

    fun createCustomTempFile(context: Context): File {
        val filesDir = context.externalCacheDir
        val now = Instant.now()
        val ldt = now.atZone(DateTimeUtil.zoneIdJakarta).toLocalDateTime()
        val timeStamp = DateTimeUtil.getUTCLocalDate(ldt, "ddMMyyyyHHmmss")
        return File.createTempFile(timeStamp, ".jpg", filesDir)
    }

}

fun File.reduceFileImage(): File {
    val file = this
    val bitmap = BitmapFactory.decodeFile(file.path)

    val fOut = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);

    fOut.flush();
    fOut.close();

    return file
}