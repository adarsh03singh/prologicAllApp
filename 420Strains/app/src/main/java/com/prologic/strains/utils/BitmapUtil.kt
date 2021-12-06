package com.prologicwebsolution.gsk.utils

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import com.prologic.strains.utils.UriPathHelper
import java.io.*
import java.util.*


object BitmapUtil {
    fun getBitmapToFilePath(context: Context, bitmap: Bitmap): String? {
        val wrapper = ContextWrapper(context)
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file.absolutePath
    }

    fun rotateImageIfRequired(context: Context, uri: Uri): Bitmap {
        val input = context.contentResolver.openInputStream(uri)
        var bitmap = BitmapFactory.decodeStream(input)
        input!!.close()
        val exifInterface = ExifInterface(UriPathHelper.getPath(context, uri))
        val orientation = exifInterface.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 ->
                bitmap = rotateImage(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 ->
                bitmap = rotateImage(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 ->
                bitmap = rotateImage(bitmap, 270)
            else -> {
            }
        }
        bitmap = getResizedBitmap(bitmap, 500)
        return bitmap
    }

    fun rotateImage(img: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 0) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }
    /*-------------------------------------------------------------------------------*/
    /*----------------------For Many type bitmap for future-----------------------*/
    /*-------------------------------------------------------------------------------*/

    fun getBitmapFormUri(ac: Context, uri: Uri?): Bitmap? {
        var input = ac.contentResolver.openInputStream(uri!!)
        val onlyBoundsOptions = BitmapFactory.Options()
        onlyBoundsOptions.inJustDecodeBounds = true
        onlyBoundsOptions.inDither = true //optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 //optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
        input!!.close()
        val originalWidth = onlyBoundsOptions.outWidth
        val originalHeight = onlyBoundsOptions.outHeight
        if (originalWidth == -1 || originalHeight == -1) return null
        val hh = 800f
        val ww = 480f
        var be = 1
        if (originalWidth > originalHeight && originalWidth > ww) {
            be = (originalWidth / ww).toInt()
        } else if (originalWidth < originalHeight && originalHeight > hh) {
            be = (originalHeight / hh).toInt()
        }
        if (be <= 0) be = 1
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inSampleSize = be
        bitmapOptions.inDither = true
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888
        input = ac.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions)
        input!!.close()
        return compressImage(bitmap)
    }

    fun compressImage(image: Bitmap?): Bitmap? {
        val baos = ByteArrayOutputStream()
        image!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        var options = 100
        while (baos.toByteArray().size / 1024 > 100) {
            baos.reset()
            image.compress(Bitmap.CompressFormat.JPEG, options, baos)
            options -= 10
        }
        val isBm = ByteArrayInputStream(baos.toByteArray())
        return BitmapFactory.decodeStream(isBm, null, null)
    }
}