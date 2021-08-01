/*
 * Copyright 2021 dev.id
 */
package es.littledavity.features.add

import es.littledavity.commons.ui.base.events.Route

internal sealed class AddContactRoute : Route {
    object Back : AddContactRoute()
    object List : AddContactRoute()
    object Edit : AddContactRoute()
    object SettingsApp : AddContactRoute()
}
