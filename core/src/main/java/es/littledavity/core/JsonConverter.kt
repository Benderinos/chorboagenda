/*
 * Copyright 2021 dalodev
 */
package es.littledavity.core

import es.littledavity.core.utils.decodeFromStringOrNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JsonConverter @Inject constructor(val json: Json) {

    inline fun <reified T> toJson(clazz: T): String = this.json.encodeToString(clazz)

    inline fun <reified T> fromJson(json: String): T? = this.json.decodeFromStringOrNull(json)
}
