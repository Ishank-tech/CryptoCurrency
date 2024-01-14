package com.example.cryptocurrency

import android.content.Context
import com.example.cryptocurrency.models.Currency
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class CurrencyAdapterTest {

    private val context = Mockito.mock(Context::class.java)

    @Before
    fun setUp() {

    }

    @Test
    fun testGetItemCount() {
        val currencies = listOf(
            Currency("BTC", "Bitcoin", "", 0.123456),
            Currency("ETH", "Ethereum", "", 0.789012)
        )
        val adapter = CurrencyAdapter(context)
        adapter.currencies = currencies
        assertEquals(2, adapter.itemCount)
        assertEquals("Bitcoin", adapter.currencies[0].fullName)
    }

    @After
    fun tearDown() {
    }
}