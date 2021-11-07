/*
 * Copyright 2021 dalodev
 */
package es.littledavity.imageLoading

import android.widget.ImageView

interface ImageLoader {

    fun loadImage(config: Config)

    fun cancelRequests(target: ImageView)
}
