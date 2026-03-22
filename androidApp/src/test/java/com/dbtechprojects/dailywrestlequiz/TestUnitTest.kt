package com.dbtechprojects.dailywrestlequiz

import org.junit.Test
import io.kotest.matchers.shouldBe


class TestUnitTest {
    @Test
    fun addition_isCorrect() {
        (2+2) shouldBe 4
    }

    @Test
    fun subtraction_isCorrect() {
        (2-2) shouldBe 0
    }

    @Test
    fun division_isCorrect() {
        (25/5) shouldBe 5
    }
}