/*
 * Copyright 2021 dalodev
 */
package es.littledavity.commons.ui.base.rv

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class ItemDiffCallback<IT : Item<*, *>> : DiffUtil.ItemCallback<IT>() {

    override fun areItemsTheSame(oldItem: IT, newItem: IT) = if (
        oldItem::class.java.isAssignableFrom(newItem::class.java) &&
        oldItem is HasUniqueIdentifier<*> &&
        newItem is HasUniqueIdentifier<*>
    ) {
        oldItem.uniqueIdentifier == newItem.uniqueIdentifier
    } else {
        oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: IT, newItem: IT) = oldItem == newItem
}
