package com.example.cryptocurrency

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testSwipeRefresh() {

        Thread.sleep(3000)

        onView(withId(R.id.feed)).check(matches(isDisplayed()))

        // Swipe to refresh
        onView(withId(R.id.swipeRefreshLayout)).perform(swipeDown())
    }

}