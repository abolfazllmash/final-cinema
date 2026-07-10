package com.example.ui

object PersianCalendar {
    data class PersianDate(val year: Int, val month: Int, val day: Int) {
        override fun toString(): String = String.format("%02d-%02d", month, day)
    }

    fun today(): PersianDate {
        val cal = java.util.Calendar.getInstance()
        val gYear = cal.get(java.util.Calendar.YEAR)
        val gMonth = cal.get(java.util.Calendar.MONTH) + 1
        val gDay = cal.get(java.util.Calendar.DAY_OF_MONTH)
        return gregorianToJalali(gYear, gMonth, gDay)
    }

    fun isWithin(today: PersianDate, from: String, to: String): Boolean {
        val todayStr = today.toString() // "MM-DD"
        return if (from <= to) {
            todayStr >= from && todayStr <= to
        } else {
            todayStr >= from || todayStr <= to
        }
    }

    fun gregorianToJalali(gy: Int, gm: Int, gd: Int): PersianDate {
        val gDays = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 335)
        var gy2 = if (gm > 2) gy - 1600 else gy - 1601
        val leap = gy2 / 4 - gy2 / 100 + gy2 / 400
        var days = 365 * gy2 + leap + gDays[gm - 1] + gd - 80

        val jy = 979 + 33 * (days / 12053) + 4 * ((days % 12053) / 1461)
        days %= 12053
        days %= 1461
        val jy2 = jy + (days - 1) / 365

        days = if (days > 0) (days - 1) % 365 else 364
        val jm: Int
        val jd: Int
        if (days < 186) {
            jm = 1 + days / 31
            jd = 1 + days % 31
        } else {
            jm = 7 + (days - 186) / 30
            jd = 1 + (days - 186) % 30
        }
        return PersianDate(jy2, jm, jd)
    }
}
