package com.fadhil.storyapp.util

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Base64
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ImageUtils {
    @Throws(IOException::class)
    fun createImageFile(context: Context): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "IMG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    fun getFileName(context: Context, uri: Uri?): String {
        return if (uri == null) {
            ""
        } else {
            var result: String? = null
            if (uri.scheme == "content") {
                val cursor = context.contentResolver.query(uri, null, null, null, null)
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        result = if (index >= 0) cursor.getString(index) else ""
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                } finally {
                    cursor?.close()
                }
            }
            if (result == null) {
                result = uri.path
                val cut = result!!.lastIndexOf(File.separator)
                if (cut != -1) {
                    result = result.substring(cut + 1)
                }
            }
            result
        }
    }

    private fun Uri.getExtension(context: Context): String? {
        return try {
            val path = getFileName(context, this)
            return path.substring(path.lastIndexOf("."))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            this.lastPathSegment
        }
    }

    fun Uri.isImage(context: Context): Boolean {
        return getExtension(context) in arrayOf(".jpg", ".jpeg", ".JPG", ".JPEG")
    }

    fun getFileSize(context: Context, fileUri: Uri): Long? {
        val returnCursor = context.contentResolver.query(fileUri, null, null, null, null)
        val sizeIndex = returnCursor?.getColumnIndex(OpenableColumns.SIZE) ?: return null

        returnCursor.moveToFirst()
        val size = returnCursor.getLong(sizeIndex)
        returnCursor.close()

        return size
    }

//    fun Context.imageChooser(cameraCallback: () -> Unit, galleryCallback: () -> Unit) {
//        val optionsMenu = arrayOf<CharSequence>(
//            getString(R.string.camera),
//            getString(R.string.gallery)
//        )
//        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
//        builder.setItems(optionsMenu) { _, i ->
//            when (optionsMenu[i]) {
//                optionsMenu[0] -> {
//                    cameraCallback.invoke()
//                }
//
//                optionsMenu[1] -> {
//                    galleryCallback.invoke()
//                }
//            }
//        }
//        builder.show()
//    }

    fun File.getUri(context: Context): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            this
        )
    }

    fun getFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val byteArray = inputStream?.readBytes()

        val file = createImageFile(context)
        val fos = FileOutputStream(file)
        fos.write(byteArray)
        inputStream?.close()
        fos.close()
        return file
    }

    fun Uri.toBase64(context: Context): String {
        var base64 = ""
        var inputStream: InputStream? = null

        try {
            inputStream = context.contentResolver.openInputStream(this)
            val outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var bytesRead: Int

            if (inputStream != null) {
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }
            }

            val imageBytes = outputStream.toByteArray()
            base64 = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }

        return base64
    }
}