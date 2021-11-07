/*
 * Copyright 2021 dalodev
 */
package es.littledavity.core.utils

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

inline fun <reified T> Json.decodeFromStringOrNull(json: String): T? = try {
    decodeFromString(json)
} catch (error: Throwable) {
    null
}
