package com.notilocations


import android.view.View
import android.widget.SeekBar
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Matcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class SettingsTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.ACCESS_FINE_LOCATION"
        )

    @Test
    fun settingsTest() {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(mActivityTestRule.activity)


        val floatingActionButton = onView(
            withId(R.id.settingsButton)
        )

        floatingActionButton.perform(click())

        val recyclerView = onView(
            withId(R.id.recycler_view)
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(1, click()))

        assert(sharedPreferences.getBoolean("dark_theme", false))
        Espresso.pressBack()
        floatingActionButton.perform(click())
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(1, click()))
        assertFalse(sharedPreferences.getBoolean("dark_theme", true))

        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(2, click()))
        assert(sharedPreferences.getBoolean("voice_notification", false))
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(2, click()))
        assertFalse(sharedPreferences.getBoolean("voice_notification", true))

        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(3, click()))
        val appCompatEditText = onView(
            withId(android.R.id.edit)
        )
        appCompatEditText.perform(scrollTo(), replaceText("5.3"), closeSoftKeyboard())
        val appCompatButton = onView(
            withId(android.R.id.button1)
        )
        appCompatButton.perform(scrollTo(), click())
        assertEquals("5.3", sharedPreferences.getString("distance", ""))

        val seekBar = onView(
            withId(R.id.seekbar)
        )


        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(5, click()))
        assert(sharedPreferences.getBoolean("max_speed_enabled", false))
        seekBar.check(matches(isEnabled()))

        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(5, click()))
        //seekBar.check(matches(not(isEnabled())))
        assertFalse(sharedPreferences.getBoolean("max_speed_enabled", true))
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(5, click()))

        onView(withId(R.id.seekbar)).perform(setProgress(51))

        onView(withId(R.id.seekbar_value)).check(matches(withText("61")))

        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(5, click()))
    }

    private fun setProgress(progress: Int): ViewAction? {
        return object : ViewAction {
            override fun perform(uiController: UiController?, view: View) {
                (view as SeekBar).progress = progress
            }

            override fun getDescription(): String {
                return "Set a progress"
            }

            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(SeekBar::class.java)
            }
        }
    }

//    private fun withTextColor(color: Color): Matcher<View?>? {
//        return object : BoundedMatcher<View?, TextView>(TextView::class.java) {
//            override fun matchesSafely(textView: TextView): Boolean {
//                val colorId = ContextCompat.getColor(textView.context, expectedId)
//                return textView.currentTextColor == colorId
//            }
//
//            override fun describeTo(description: Description) {
//                description.appendText("with text color: ")
//                description.appendValue(expectedId)
//            }
//        }
//    }
}
