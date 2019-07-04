package org.rovioli.trachka

import java.util.concurrent.TimeUnit

// TODO: replace this with a correct date
val week = arrayOf(
    "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
)

fun getDayOfWeek(i: Int) = week[i - 1]

fun currentTimeSeconds(): Long? = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())