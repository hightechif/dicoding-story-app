package com.fadhil.storyapp.util

import android.content.Context
import java.io.File
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