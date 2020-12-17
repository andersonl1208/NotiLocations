package com.notilocations


import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.CoordinatesProvider
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class CreateTaskTest {

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
    fun createTaskTest() {

        onView(withId(R.id.addTask)).perform(click())

        val seekBar = onView(
            withId(R.id.maxSpeedInput)
        )
        seekBar.check(matches(not(isEnabled())))

        onView(withId(R.id.title_input)).perform(replaceText("Hello"), closeSoftKeyboard())

        onView(withId(R.id.description_input)).perform(replaceText("World"), closeSoftKeyboard())

        onView(withId(R.id.distanceInput)).perform(replaceText("123.1"), closeSoftKeyboard())

        onView(withId(R.id.maxSpeedEnabledInput)).perform(click())

        seekBar.perform(setProgress(25))

        onView(withId(R.id.triggerOnExitInput)).perform(click())

        onView(withId(R.id.title_input)).check(matches(withText("Hello")))

        onView(withId(R.id.description_input)).check(matches(withText("World")))

        onView(withId(R.id.distanceInput)).check(matches(withText("123.1")))

        onView(withId(R.id.maxSpeedEnabledInput)).check(matches(isChecked())) //TODO

        seekBar.check(matches(isEnabled()))

        onView(withId(R.id.maxSpeedValue)).check(matches(withText("35")))

        onView(withId(R.id.triggerOnExitInput)).check(matches(isChecked()))

        onView(withId(R.id.addLocation)).perform(click())

        onView(withId(R.id.map)).perform(longClickXY(100, 100))

        onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.custom),
                        childAtPosition(
                            withId(R.id.customPanel),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(replaceText("Test"), closeSoftKeyboard())

        onView(withId(android.R.id.button1)).perform(scrollTo(), click())

        onView(withId(R.id.taskRecycler)).perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        onView(withId(R.id.title_input)).check(matches(withText("Hello")))

        onView(withId(R.id.description_input)).check(matches(withText("World")))

        onView(withId(R.id.distanceInput)).check(matches(withText("123.1")))

        onView(withId(R.id.maxSpeedEnabledInput)).check(matches(isChecked()))

        seekBar.check(matches(isEnabled()))

        onView(withId(R.id.maxSpeedValue)).check(matches(withText("35")))

        onView(withId(R.id.triggerOnExitInput)).check(matches(isChecked()))

        onView(withId(R.id.addLocation)).check(matches(withText("Update Location")))

        onView(withId(R.id.deleteButton)).check(matches(isDisplayed()))

        onView(withId(R.id.addLocation)).perform(click())

        pressBack()

        onView(withId(R.id.submitTask)).perform(click())

        onView(withId(R.id.taskRecycler)).perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        onView(withId(R.id.deleteButton)).perform(click())

        onView(withId(R.id.addTask)).perform(click())

        onView(withId(R.id.title_input)).perform(replaceText("test"), closeSoftKeyboard())

        onView(withId(R.id.maxSpeedEnabledInput)).perform(click())

        onView(withId(R.id.submitTask)).perform(click())

        onView(withId(R.id.map)).perform(longClickXY(120, 120))

        val appCompatButton3 = onView(
            allOf(
                withId(android.R.id.button1), withText("OK"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.buttonPanel),
                        0
                    ),
                    3
                )
            )
        )
        appCompatButton3.perform(scrollTo(), click())

        onView(withId(R.id.addTask)).perform(click())

        onView(withId(R.id.submitTask)).perform(click())

        // Make sure that we are still on the same screen as validation should have failed.
        onView(withId(R.id.titleTextView)).check(matches(isDisplayed()))

        pressBack()

        // Make sure that we went back to the main screen
        onView(withId(R.id.addTask)).check(matches(isDisplayed()))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
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

    private fun longClickXY(x: Int, y: Int): ViewAction? {
        return GeneralClickAction(
            Tap.LONG,
            CoordinatesProvider { view ->
                val screenPos = IntArray(2)
                view.getLocationOnScreen(screenPos)
                val screenX = screenPos[0] + x.toFloat()
                val screenY = screenPos[1] + y.toFloat()
                floatArrayOf(screenX, screenY)
            },
            Press.FINGER,
            0,
            0
        )
    }
}
