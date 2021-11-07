/*
 * Copyright 2021 dalodev
 */
package es.littledavity.commons.ui.base.rv

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.littledavity.commons.ui.extensions.layoutInflater
import es.littledavity.commons.ui.extensions.observeChanges
import kotlin.reflect.KClass

@JvmInline
private value class ViewType(val type: Int)

abstract class AbstractRecyclerViewAdapter<IT : Item<*, in Dependencies>, Dependencies : ItemDependencies>(
    context: Context,
    items: List<IT> = emptyList(),
    dependencies: ItemDependencies = NoDependencies
) : ListAdapter<IT, RecyclerView.ViewHolder>(ItemDiffCallback()) {

    private val inflater = context.layoutInflater
    private val viewHolderFactories = mutableMapOf<ViewType, ViewHolderFactory>()

    var dependencies by observeChanges(dependencies) { _, _ -> notifyDataSetChanged() }

    var listenerBinder: ListenerBinder<IT>? = null

    init {
        initItems(items)
    }

    private fun initItems(items: List<IT>) {
        if (items.isNotEmpty()) {
            submitList(items)
        }
    }

    override fun submitList(list: List<IT>?) {
        list?.extractViewHolderFactories()
        super.submitList(list)
    }

    override fun submitList(list: List<IT>?, commitCallback: Runnable?) {
        list?.extractViewHolderFactories()
        super.submitList(list, commitCallback)
    }

    private fun List<IT>.extractViewHolderFactories() {
        viewHolderFactories.clear()
        for (item in this) {
            val viewType = item::class.toViewType()

            if (viewHolderFactories[viewType] == null) {
                viewHolderFactories[viewType] = item
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        viewHolderFactories[ViewType(viewType)]?.create(inflater, parent, dependencies)
            ?: error("The ViewHolder factory was not found.")

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(this[position]) {
            bind(holder, dependencies)

            if (holder is HasListeners) {
                listenerBinder?.invoke(this, holder)
            }
        }
    }

    override fun getItemViewType(position: Int) = this[position]::class.toViewType().type

    operator fun get(position: Int): IT = currentList[position]

    private fun KClass<out Item<*, *>>.toViewType() = ViewType(this.qualifiedName.hashCode())

    override fun getItemId(position: Int) = this[position].itemId
}
