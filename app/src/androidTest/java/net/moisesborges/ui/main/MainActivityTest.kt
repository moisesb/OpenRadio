/*
 * MIT License
 *
 * Copyright (c) 2019 Moisés Borges dos Anjos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
        onView(withId(R.id.search_menu_item))
            .perform(click())

        onView(withId(R.id.settings_fragment_container))
            .check(matches(isDisplayed()))
    }

    @Test fun whenFavoritesRadiosIsSelectedThenDisplayFavoritesRadiosFragment() {
        onView(withId(R.id.my_stations_menu_item))
            .perform(click())

        onView(withId(R.id.favorites_radios_container))
            .check(matches(isDisplayed()))
    }

    @Test fun whenTopRadiosIsSelectedThenDisplayTopRadiosFragment() {
        onView(withId(R.id.my_stations_menu_item))
            .perform(click())
        onView(withId(R.id.home_stations_menu_item))
            .perform(click())

        onView(withId(R.id.top_radio_fragment_container))
            .check(matches(isDisplayed()))
    }
}