@file:JvmName("UuidUtil")

package de.sevennerds.trackdefects.util

import java.util.*

fun getUuidV4() = UUID.randomUUID().toString()