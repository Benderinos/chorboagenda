/*
 * Copyright 2021 dalodev
 */
package es.littledavity.commons.ui.base.events

sealed class GeneralCommand : Command {
    class ShowShortToast(val message: String) : GeneralCommand()
    class ShowLongToast(val message: String) : GeneralCommand()
}
