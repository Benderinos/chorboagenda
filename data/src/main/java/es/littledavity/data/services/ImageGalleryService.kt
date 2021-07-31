/*
 * Copyright 2021 dev.id
 */
package es.littledavity.data.services

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.paulrybitskyi.hiltbinder.BindType
import dagger.hilt.android.qualifiers.ApplicationContext
import es.littledavity.data.contacts.DataContact
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

interface ImageGalleryService {
    fun createMediaFile(contact: DataContact): String?
}

@BindType
internal class ImageGalleryServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ImageGalleryService {

    companion object {
        private const val QUALITY = 100
    }

    override fun createMediaFile(contact: DataContact): String? {
        val bitmapImage = contact.image?.id?.toUri()?.let(::getBitmap)
        bitmapImage?.let { return saveMediaImage(it, contact) }
        return null
    }

    fun getBitmap(uri: Uri): Bitmap {
        val imageStream = context.contentResolver?.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(imageStream)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, baos)
        val bytes = baos.toByteArray()
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun getBitmap(imageBase64: String): Bitmap {
        val decodedBytes = Base64.decode(imageBase64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    fun saveMediaFileLegacy(
        imageToSave: Bitmap,
        fileName: String
    ): String {
        val externalStorageVolumes = ContextCompat.getExternalFilesDirs(context, null)
        val primaryExternalStorage =
            externalStorageVolumes[0]?.path
                ?: externalStorageVolumes[1].path
                ?: error("Storage volumes not found ")

        val dirName = "$primaryExternalStorage/chorboagenda"

        val direct = File(
            dirName
        )

        if (!direct.exists()) {
            val wallpaperDirectory = File(dirName)
            wallpaperDirectory.mkdirs()
        }
        val uuid = UUID.randomUUID().toString()
        val file = File(dirName, fileName + uuid)
        if (file.exists()) {
            file.delete()
        }
        try {
            val out = FileOutputStream(file)
            imageToSave.compress(Bitmap.CompressFormat.JPEG, QUALITY, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file.path
    }

    fun saveMediaImage(imageToSave: Bitmap, chorbo: DataContact): String {
        val relativeLocation = "${Environment.DIRECTORY_PICTURES}/${chorbo.name}"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, chorbo.id)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)
            }
        }
        val resolver = context.contentResolver

        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val uri = resolver.insert(contentUri, contentValues)
            ?: throw IOException("Failed to create new MediaStore record.")

        val stream = resolver.openOutputStream(uri)
            ?: throw IOException("Failed to get output stream.")

        if (!imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
            throw IOException("Failed to save bitmap")
        }
        stream.close()
        return uri.toString()
    }

    fun saveMediaImage(imageToSave: Bitmap, chorbo: DataContact, fileName: String): String {
        val relativeLocation = "${Environment.DIRECTORY_PICTURES}/${chorbo.name}"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)
        }
        val resolver = context.contentResolver

        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val uri = resolver.insert(contentUri, contentValues)
            ?: throw IOException("Failed to create new MediaStore record.")

        val stream = resolver.openOutputStream(uri)
            ?: throw IOException("Failed to get output stream.")

        if (!imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
            throw IOException("Failed to save bitmap")
        }
        stream.close()
        return uri.path.orEmpty()
    }
}
