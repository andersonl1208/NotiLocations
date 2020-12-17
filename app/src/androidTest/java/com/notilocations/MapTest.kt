package com.notilocations


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.CoordinatesProvider
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MapTest {

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
    fun mapTest() {

        onView(withId(R.id.addTask)).perform(click())

        onView(withId(R.id.title_input)).perform(replaceText("test"), closeSoftKeyboard())

        onView(withId(R.id.addLocation)).perform(click())

        pressBack()

        Thread.sleep(1000)

        onView(withId(R.id.submitTask)).perform(click())

        pressBack()

        Thread.sleep(1000)

        onView(withId(R.id.addLocation)).perform(click())

        onView(withId(R.id.map)).perform(longClickXY(100, 150))

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
        ).perform(replaceText("Example"), closeSoftKeyboard())

        onView(withId(android.R.id.button1)).perform(scrollTo(), click())

        onView(
            allOf(
                withContentDescription("Task locations"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tab_layout),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(withId(R.id.map)).perform(longClickXY(100, 200))

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
        ).perform(replaceText("hello"), closeSoftKeyboard())

        onView(withId(android.R.id.button1)).perform(scrollTo(), click())

        onView(withId(R.id.title_input)).perform(replaceText("another one"), closeSoftKeyboard())

        onView(withId(R.id.submitTask)).perform(click())

        onView(
            allOf(
                withContentDescription("Task locations"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tab_layout),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        ).perform(click())

//        onView(
//            allOf(
//                withContentDescription("Example")
//            )
//        ).check(matches(isDisplayed()))
//
//        onView(
//            allOf(
//                withContentDescription("hello"),
//                withParent(
//                    allOf(
//                        withContentDescription("Google Map"),
//                        withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))
//                    )
//                ),
//                isDisplayed()
//            )
//        ).check(matches(isDisplayed()))
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
