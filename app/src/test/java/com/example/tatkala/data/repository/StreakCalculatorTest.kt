package com.example.tatkala.data.repository

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class StreakCalculatorTest {
    @Test
    fun currentStreakCountsConsecutiveDatesFromToday() {
        val today = LocalDate.of(2026, 10, 8)
        val dates = setOf(
            today,
            today.minusDays(1),
            today.minusDays(2),
            today.minusDays(4)
        )

        assertEquals(3, StreakCalculator.currentStreak(dates, today))
    }

    @Test
    fun currentStreakAllowsYesterdayAsStart() {
        val today = LocalDate.of(2026, 10, 8)
        val dates = setOf(today.minusDays(1), today.minusDays(2))

        assertEquals(2, StreakCalculator.currentStreak(dates, today))
    }

    @Test
    fun currentStreakStopsWhenTodayAndYesterdayAreMissing() {
        val today = LocalDate.of(2026, 10, 8)
        val dates = setOf(today.minusDays(2), today.minusDays(3))

        assertEquals(0, StreakCalculator.currentStreak(dates, today))
    }
}
