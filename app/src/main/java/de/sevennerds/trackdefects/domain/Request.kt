package de.sevennerds.trackdefects.domain

data class Request<T>(val data: T,
                      val hasConnection: Boolean = true)