package cn.kevin.openeye.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import cn.kevin.openeye.R
import cn.kevin.openeye.databinding.FragmentHomeBinding
import cn.kevin.openeye.ui.home.daily.DailyFragment
import cn.kevin.openeye.ui.home.discovery.DiscoveryFragment
import cn.kevin.openeye.ui.home.recommend.RecommendFragment
import cn.kevin.openeye.util.GlobalUtil
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var tabLayout: TabLayout

    private val crateTitles = ArrayList<String>().apply {
        add(GlobalUtil.getString(R.string.discovery))
        add(GlobalUtil.getString(R.string.recommend))
        add(GlobalUtil.getString(R.string.daily))
    }

    private val crateFragments = arrayOf(
        DiscoveryFragment.newInstance(),
        RecommendFragment.newInstance(),
        DailyFragment.newInstance()
    )



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        tabLayout = binding.titleBar.tabLayout
        val viewPager = binding.viewPage

        val vpAdapter = VpAdapter(this).apply { addFragments(crateFragments) }
        viewPager.adapter = vpAdapter
        TabLayoutMediator(tabLayout, viewPager, true) {
            tab, position ->tab.text = crateTitles[position]
        }.attach()

        return binding.root
    }

    inner class VpAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        private val fragments = mutableListOf<Fragment>()

        fun addFragments(fragment: Array<Fragment>) = fragments.addAll(fragment)

        override fun getItemCount(): Int {
           return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }

    }
}