package com.notilocations
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

//collection of fragments
class SwipeViewFragment : Fragment() {
    // When requested, this adapter returns a fragment of our choosing
    private lateinit var swipeViewAdapter: SwipeViewAdapter
    private lateinit var viewPager: ViewPager2

    /**
     * creates the SwipeView
     * @param inflater the layout inflater
     * @param container The container being used
     * @param savedInstanceState the bundle that is being passed through
     * @return the inflater that is being used
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_swipe_view, container, false)
    }

    /**
     * Override the onviewCreated function
     * @param view the view being used
     * @param savedInstanceState the bundle being passe
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        swipeViewAdapter = SwipeViewAdapter(this)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = swipeViewAdapter
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)

        //populate the tabs
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when(position){
                0-> tab.text = "Task list"
                1-> tab.text = "Task locations"
            }

        }.attach()

        //Disable user input while on the map fragment Very important that position lines up with the position the map fragment is in.
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewPager.isUserInputEnabled = position != 1
                super.onPageSelected(position)
            }
        })
    }

    /**
     * Ovveride the onCreateOption menu to inflate our menu
     * @param menu the menu that is wanted to be inflated
     * @param inflater the inflater being used
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }


    /**
     * Used to return the item that the user selected on the tab view or just swiped to.
     * @param item the menu item
     * @return the item selection on the tab view
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)

    }
}


/**
 * Inflates the fragment depending on which position the user is at on the swipe view
 * @param fragment The fragment that is being inflated
 * @return the fragment that is to be inflated
 */
class SwipeViewAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)

        val taskList = TaskListFragment()
        val mapFragment = MapsFragment()

        //determine where the fragments go, I.E. pos 0 is the first fragment, pos 1 is the second fragmnet
        when (position) {
            0 -> return taskList
            1 -> return mapFragment
            else -> return taskList
        }
    }
}
