package com.notilocations


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CreateTask {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun createTask() {
        val floatingActionButton = onView(
            allOf(
                withId(R.id.addTask), withContentDescription("Add a task"),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.title_input),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("Store"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.description_input),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("Get Eggs"), closeSoftKeyboard())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.distanceInput),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("2.5"), closeSoftKeyboard())

        val switchCompat = onView(
            allOf(
                withId(R.id.maxSpeedEnabledInput),
                withText("Max speed to trigger notification (mph)"),
                isDisplayed()
            )
        )
        switchCompat.perform(click())

        val floatingActionButton2 = onView(
            allOf(
                withId(R.id.submitTask), withContentDescription("Submit"),
                isDisplayed()
            )
        )
        floatingActionButton2.perform(click())

//        val appCompatEditText4 = onView(
//            allOf(
//                withId(R.id.title_input), withText("Store"),
//                isDisplayed()
//            )
//        )
//        appCompatEditText4.perform(pressImeActionButton())
//
//        val appCompatEditText5 = onView(
//            allOf(
//                withId(R.id.description_input), withText("Get Eggs"),
//                isDisplayed()
//            )
//        )
//        appCompatEditText5.perform(pressImeActionButton())
//
//        val appCompatEditText6 = onView(
//            allOf(
//                withId(R.id.distanceInput), withText("2.5"),
//                isDisplayed()
//            )
//        )
//        appCompatEditText6.perform(pressImeActionButton())

        val imageView = onView(
            allOf(
                withContentDescription("Zoom in"),
                isDisplayed()
            )
        )
        imageView.perform(click())

        val imageView2 = onView(
            allOf(
                withContentDescription("Zoom in"),
                isDisplayed()
            )
        )
        imageView2.perform(click())

        val imageView3 = onView(
            allOf(
                withContentDescription("Zoom in"),
                isDisplayed()
            )
        )
        imageView3.perform(click())

        val imageView4 = onView(
            allOf(
                withContentDescription("Zoom in"),
                isDisplayed()
            )
        )
        imageView4.perform(click())

        val imageView5 = onView(
            allOf(
                withContentDescription("Zoom in"),
                isDisplayed()
            )
        )
        imageView5.perform(click())

        val imageView6 = onView(
            allOf(
                withContentDescription("Zoom in"),
                isDisplayed()
            )
        )
        imageView6.perform(click())

//        val appCompatButton = onView(
//            allOf(
//                withId(android.R.id.button1), withText("OK")
//            )
//        )
//        appCompatButton.perform(scrollTo(), click())
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
}
