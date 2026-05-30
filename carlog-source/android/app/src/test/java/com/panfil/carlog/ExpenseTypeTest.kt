package com.panfil.carlog

import com.google.common.truth.Truth.assertThat
import com.panfil.carlog.domain.ExpenseType
import org.junit.Test

class ExpenseTypeTest {

    @Test
    fun `all expense types have non-empty labels`() {
        ExpenseType.entries.forEach { type ->
            assertThat(type.label).isNotEmpty()
        }
    }

    @Test
    fun `expense type count matches expected`() {
        assertThat(ExpenseType.entries).hasSize(8)
    }

    @Test
    fun `fuel type has correct label`() {
        assertThat(ExpenseType.FUEL.label).isEqualTo("Топливо")
    }

    @Test
    fun `expense type names are unique`() {
        val names = ExpenseType.entries.map { it.name }
        assertThat(names).containsNoDuplicates()
    }

    @Test
    fun `expense type labels are unique`() {
        val labels = ExpenseType.entries.map { it.label }
        assertThat(labels).containsNoDuplicates()
    }
}
