/*
 * Copyright 2021 dalodev
 */
package es.littledavity.imageLoading

import android.graphics.Bitmap

interface Transformation {

    val key: String

    fun transform(source: Bitmap): Bitmap
}
