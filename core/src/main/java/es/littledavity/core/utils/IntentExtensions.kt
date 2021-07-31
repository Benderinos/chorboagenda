/*
 * Copyright 2021 dev.id
 */
package es.littledavity.core.utils

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

fun Intent.attachNewTaskFlagIfNeeded(context: Context) {
    if(context !is AppCompatActivity) {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}
