/*
 * Copyright 2021 dev.id
 */
package es.littledavity.data.contacts.usecases

import com.paulrybitskyi.hiltbinder.BindType
import es.littledavity.core.providers.DispatcherProvider
import es.littledavity.data.commons.utils.toDataPagination
import es.littledavity.data.contacts.datastores.ContactsLocalDataStore
import es.littledavity.domain.contacts.commons.ObserveContactsUseCaseParams
import es.littledavity.domain.contacts.usecases.ObserveContactsUseCase
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@BindType
internal class ObserveContactsUseCaseImpl @Inject constructor(
    private val likedcontactsLocalDataStore: ContactsLocalDataStore,
    private val dispatcherProvider: DispatcherProvider,
    private val gameMapper: ContactMapper
) : ObserveContactsUseCase {

    override suspend fun execute(params: ObserveContactsUseCaseParams) =
        likedcontactsLocalDataStore.observeContacts(params.pagination.toDataPagination())
            .map(gameMapper::mapToDomainContacts)
            .flowOn(dispatcherProvider.computation)
}
