package com.notilocations


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
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
class MainAppTest2 {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainAppTest2() {
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
        appCompatEditText.perform(replaceText("CVS"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.description_input),

                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("Pick up prescription"), closeSoftKeyboard())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.distanceInput),

                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("2.5"), closeSoftKeyboard())

        val appCompatButton = onView(
            allOf(
                withId(R.id.addLocation), withText("Add a location"),

                isDisplayed()
            )
        )
        appCompatButton.perform(click())


        val recyclerView = onView(
            allOf(
                withId(R.id.taskRecycler)
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))


        val button = onView(
            allOf(
                withId(R.id.addLocation), withText("UPDATE LOCATION"),

                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val floatingActionButton2 = onView(
            allOf(
                withId(R.id.submitTask),
                isDisplayed()
            )
        )
        floatingActionButton2.perform(click())

        val appCompatEditText6 = onView(
            allOf(
                withId(R.id.title_input), withText("CVS"),

                isDisplayed()
            )
        )
        appCompatEditText6.perform(pressImeActionButton())

        val appCompatEditText7 = onView(
            allOf(
                withId(R.id.description_input), withText("Pick up prescription"),

                isDisplayed()
            )
        )
        appCompatEditText7.perform(pressImeActionButton())

        val tabView = onView(
            allOf(
                withContentDescription("Task locations"),

                isDisplayed()
            )
        )
        tabView.perform(click())

        val tabView2 = onView(
            allOf(
                withContentDescription("Task list"),

                isDisplayed()
            )
        )
        tabView2.perform(click())

        val floatingActionButton3 = onView(
            allOf(
                withId(R.id.settingButton), withContentDescription("Go to settings"),

                isDisplayed()
            )
        )
        floatingActionButton3.perform(click())

        val recyclerView2 = onView(
            allOf(
                withId(R.id.recycler_view)
            )
        )
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(1, click()))

        val recyclerView3 = onView(
            allOf(
                withId(R.id.recycler_view)
            )
        )
        recyclerView3.perform(actionOnItemAtPosition<ViewHolder>(1, click()))

        val recyclerView4 = onView(
            allOf(
                withId(R.id.recycler_view)
            )
        )
        recyclerView4.perform(actionOnItemAtPosition<ViewHolder>(2, click()))

        val recyclerView5 = onView(
            allOf(
                withId(R.id.recycler_view)
            )
        )
        recyclerView5.perform(actionOnItemAtPosition<ViewHolder>(2, click()))

        val recyclerView6 = onView(
            allOf(
                withId(R.id.recycler_view)
            )
        )
        recyclerView6.perform(actionOnItemAtPosition<ViewHolder>(5, click()))

        val recyclerView7 = onView(
            allOf(
                withId(R.id.recycler_view)
            )
        )
        recyclerView7.perform(actionOnItemAtPosition<ViewHolder>(5, click()))
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
