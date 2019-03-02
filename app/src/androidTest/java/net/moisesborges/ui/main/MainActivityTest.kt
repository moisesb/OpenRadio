package net.moisesborges.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import net.moisesborges.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test fun whenMainActivityStartThenDisplayTopRadioFragment() {
        onView(withId(R.id.top_radio_fragment_container))
            .check(matches(isDisplayed()))
    }

    @Test fun whenSettingsIsSelectedThenDisplaySettingsFragment() {
        onView(withId(R.id.settings_menu_item))
            .perform(click())

        onView(withId(R.id.settings_fragment_container))
            .check(matches(isDisplayed()))
    }

    @Test fun whenFavoritesRadiosIsSelectedThenDisplayFavoritesRadiosFragment() {
        onView(withId(R.id.favorites_radios_menu_item))
            .perform(click())

        onView(withId(R.id.favorites_radios_container))
            .check(matches(isDisplayed()))
    }

    @Test fun whenTopRadiosIsSelectedThenDisplayTopRadiosFragment() {
        onView(withId(R.id.favorites_radios_menu_item))
            .perform(click())
        onView(withId(R.id.top_radios_menu_item))
            .perform(click())

        onView(withId(R.id.top_radio_fragment_container))
            .check(matches(isDisplayed()))
    }
}