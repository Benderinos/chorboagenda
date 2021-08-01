/*
 * Copyright 2021 dev.id
 */
package es.littledavity.database.chorbo.datastores

import com.paulrybitskyi.hiltbinder.BindType
import es.littledavity.core.providers.TimestampProvider
import es.littledavity.database.Constants
import es.littledavity.database.chorbo.entities.LikedContact
import javax.inject.Inject

internal interface LikedContactFactory {
    fun createLikeContact(contactId: Int): LikedContact
}

@BindType
internal class LikedContactFactoryImpl @Inject constructor(
    private val timestampProvider: TimestampProvider
) : LikedContactFactory {
    override fun createLikeContact(contactId: Int) = LikedContact(
        id = Constants.ENTITIY_AUTOGENERATED_ID_INDICATOR,
        contactId = contactId,
        likeTimestamp = timestampProvider.getUnixTimestamp()
    )
}
