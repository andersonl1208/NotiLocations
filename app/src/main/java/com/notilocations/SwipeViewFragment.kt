package com.notilocations
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                viewPager.isUserInputEnabled = position != 1
                super.onPageSelected(position)
            }
        })
    }
}


class SwipeViewAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)

        val taskList = TaskList()
        val mapFragment = Map()

        taskList.arguments = Bundle().apply {
            // Our object is just an integer
            putInt(ARG_OBJECT, position + 1)
        }
        mapFragment.arguments = Bundle().apply {
            // Our object is just an integer
            putInt(ARG_OBJECT, position + 1)
        }
        //determine where the fragments go, I.E. pos 0 is the first fragment, pos 1 is the second fragmnet
        when (position) {
            0 -> return taskList
            1 -> return mapFragment
            else -> return taskList
        }
    }
}

private const val ARG_OBJECT = "object"

// Instances of this class are fragments representing a single
// object in our collection.
class TaskList : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //the return statement is what determines what fragment appears
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }
    /*not really needed right now, but will probably be needed at some point to access data in these fragments.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            //val textView: TextView = view.findViewById(android.R.id.text1)
            //textView.text = getInt(ARG_OBJECT).toString()
        }
    }*/
}

class Map : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //the return statement is what determines what fragment appears
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }
}