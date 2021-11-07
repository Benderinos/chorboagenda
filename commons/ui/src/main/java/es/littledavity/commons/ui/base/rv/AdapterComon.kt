/*
 * Copyright 2021 dalodev
 */
package es.littledavity.commons.ui.base.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

typealias ListenerBinder<IT> = (IT, RecyclerView.ViewHolder) -> Unit

interface ViewHolderFactory {
    fun create(
        inflater: LayoutInflater,
        parent: ViewGroup,
        dependencies: ItemDependencies
    ): RecyclerView.ViewHolder
}

interface Bindable {
    fun bind(
        viewHolder: RecyclerView.ViewHolder,
        dependencies: ItemDependencies
    )
}

interface HasUniqueIdentifier<out T> {
    val uniqueIdentifier: T
}

interface HasListeners

interface ItemDependencies

object NoDependencies : ItemDependencies

interface Item<Model : Any, Dependencies : ItemDependencies> : ViewHolderFactory, Bindable {

    val model: Model

    val itemId: Long
        get() = RecyclerView.NO_ID
}

abstract class AbstractItem<
    Model : Any,
    ViewHolder : RecyclerView.ViewHolder,
    Dependencies : ItemDependencies
    >(final override val model: Model) : Item<Model, Dependencies> {

    @Suppress("unchecked_cast")
    final override fun create(
        inflater: LayoutInflater,
        parent: ViewGroup,
        dependencies: ItemDependencies
    ) = createViewHolder(
        inflater = inflater,
        parent = parent,
        dependencies = dependencies as Dependencies
    )

    protected abstract fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        dependencies: Dependencies
    ): ViewHolder

    @Suppress("unchecked_cast")
    final override fun bind(viewHolder: RecyclerView.ViewHolder, dependencies: ItemDependencies) {
        performBinding(
            viewHolder = viewHolder as ViewHolder,
            dependencies = dependencies as Dependencies
        )
    }

    protected abstract fun performBinding(viewHolder: ViewHolder, dependencies: Dependencies)

    final override fun equals(other: Any?) = model == (other as? Item<*, *>)?.model

    final override fun hashCode() = model.hashCode()

    final override fun toString() = model.toString()
}
