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
    private lateinit var SwipeViewAdapter: SwipeViewAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_swipe_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        SwipeViewAdapter = SwipeViewAdapter(this)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = SwipeViewAdapter
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)

        //populate the tabs
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "OBJECT ${(position + 1)}"
        }.attach()

        //Disable user input while on the map fragment Very important that position lines up with the position the map fragment is in.
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewPager.isUserInputEnabled = position != 1
                super.onPageSelected(position)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }
}

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
