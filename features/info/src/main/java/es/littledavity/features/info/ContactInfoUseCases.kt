/*
 * Copyright 2021 dev.id
 */
package es.littledavity.features.info

import es.littledavity.domain.contacts.usecases.GetContactUseCase
import es.littledavity.domain.contacts.usecases.ObserveContactLikeStateUseCase
import es.littledavity.domain.contacts.usecases.ToggleContactLikeStateUseCase
import javax.inject.Inject

internal class ContactInfoUseCases @Inject constructor(
    val getContactUseCase: GetContactUseCase,
    val observeContactLikeStateUseCase: ObserveContactLikeStateUseCase,
    val toggleContactLikeStateUseCase: ToggleContactLikeStateUseCase
)
