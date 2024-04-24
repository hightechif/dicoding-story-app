package com.fadhil.storyapp.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

object DateTimeUtil {

    const val edtsFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val zoneIdUTC: ZoneId = ZoneId.of("UTC")
    val zoneIdJakarta: ZoneId = ZoneId.of("Asia/Jakarta")
    val zoneOffsetUTC: ZoneOffset = ZoneOffset.of("+00:00")
    val zoneOffsetJakarta: ZoneOffset = ZoneOffset.of("+07:00")

    fun getUTCDate(date: String): Date? {
        val dateFormat = SimpleDateFormat(edtsFormat,
            Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return try {
            dateFormat.parse(date)
        }
        catch (e: ParseException) {
            null
        }
    }

    fun getUTCDate(date: Date): String {
        val dateFormat = SimpleDateFormat(edtsFormat, Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")
        return dateFormat.format(date)
    }

    fun getUTCLocalDate(dateTimeStr: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern(edtsFormat, Locale.getDefault())
        return LocalDateTime.parse(dateTimeStr, formatter)
    }

    fun getUTCLocalDate(dateTime: LocalDateTime, pattern: String = edtsFormat): String {
        val zdt = dateTime.atZone(ZoneOffset.UTC)
        val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
        return zdt.format(dtf)
    }

    fun getUTCLocalDate(time: Long, pattern: String = edtsFormat): String {
        val instant = Instant.ofEpochMilli(time)
        val dateTime = LocalDateTime.ofInstant(instant, zoneIdUTC)
        val offsetTime: OffsetDateTime = dateTime.atOffset(ZoneOffset.UTC)
        val dtf = DateTimeFormatter.ofPattern(pattern, Locale("ID"))
        return offsetTime.format(dtf)
    }

    fun getJKTDate(dateTime: LocalDateTime, pattern: String = edtsFormat): String {
        val offsetTime: OffsetDateTime = dateTime.atOffset(zoneOffsetJakarta) //21:11:06 +07:00
        val dtf = DateTimeFormatter.ofPattern(pattern, Locale("ID"))
        return offsetTime.format(dtf)
    }

    fun getJKTDate(time: Long, pattern: String = edtsFormat): String {
        val instant = Instant.ofEpochMilli(time)
        val dateTime = LocalDateTime.ofInstant(instant, zoneIdJakarta)
        val offsetTime: OffsetDateTime = dateTime.atOffset(zoneOffsetJakarta) //21:11:06 +07:00
        val dtf = DateTimeFormatter.ofPattern(pattern, Locale("ID"))
        return offsetTime.format(dtf)
    }

    fun getJKTDate(dateTimeStr: String, pattern: String = edtsFormat): LocalDateTime {
        val dtf = DateTimeFormatter.ofPattern(pattern, Locale("ID"))
        return LocalDateTime.parse(dateTimeStr, dtf)
    }

    fun getJKTLocalDateTime(date: Date): LocalDateTime? =
        Instant.ofEpochMilli(date.time)
            .atZone(ZoneId.of(ZoneOffset.of("+07:00").id))
            .toLocalDateTime()

    fun getJKTValidDateString(dateTimeStr: String?, patternFrom: String = edtsFormat, patternTo: String = edtsFormat): String {
        if (dateTimeStr.isNullOrEmpty()) return  ""
        val dtf = DateTimeFormatter.ofPattern(patternFrom, Locale("ID"))
        val dateTime = LocalDateTime.parse(dateTimeStr, dtf)

        val validDtf = DateTimeFormatter.ofPattern(patternTo, Locale("ID"))
        return dateTime.format(validDtf)
    }
}