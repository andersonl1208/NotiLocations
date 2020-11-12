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

class SwipeViewFragment : Fragment() {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
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
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "OBJECT ${(position + 1)}"
        }.attach()
    }
}


class SwipeViewAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)

        val fragment1 = Fragment1()
        val fragment2 = Fragment2()
        val fragment3 = MapsFragment()

        fragment1.arguments = Bundle().apply {
            // Our object is just an integer
            putInt(ARG_OBJECT, position + 1)
        }
        fragment2.arguments = Bundle().apply {
            // Our object is just an integer
            putInt(ARG_OBJECT, position + 1)
        }
        fragment3.arguments = Bundle().apply {
            // Our object is just an integer
            putInt(ARG_OBJECT, position + 1)
        }
        //determine where the fragments go, I.E. obje
        when (position) {
            0 -> return fragment1
            1 -> return fragment2
            2 -> return fragment3
            else -> return fragment1
        }
    }
}

private const val ARG_OBJECT = "object"

// Instances of this class are fragments representing a single
// object in our collection.
class Fragment1 : Fragment() {

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
class Fragment2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //the return statement is what determines what fragment appears
        return inflater.inflate(R.layout.fragment_create_task, container, false)
    }


    /*not really needed right now, but will probably be needed at some point to access data in these fragments.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            //val textView: TextView = view.findViewById(android.R.id.text1)
            //textView.text = getInt(ARG_OBJECT).toString()
        }
    }*/
}
class Fragment3 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //the return statement is what determines what fragment appears
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }


    /*not really needed right now, but will probably be needed at some point to access data in these fragments.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            //val textView: TextView = view.findViewById(android.R.id.text1)
            //textView.text = getInt(ARG_OBJECT).toString()
        }
    }*/
}