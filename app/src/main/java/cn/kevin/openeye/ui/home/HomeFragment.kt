package cn.kevin.openeye.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import cn.kevin.openeye.MainActivity
import cn.kevin.openeye.R
import cn.kevin.openeye.databinding.FragmentHomeBinding
import cn.kevin.openeye.extension.setOnClickListener
import cn.kevin.openeye.ui.home.daily.DailyFragment
import cn.kevin.openeye.ui.home.discovery.DiscoveryFragment
import cn.kevin.openeye.ui.home.recommend.RecommendFragment
import cn.kevin.openeye.ui.search.SearchActivity
import cn.kevin.openeye.util.GlobalUtil
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var ivSearch: ImageView
    private lateinit var viewPager: ViewPager2

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
        setOnClickListener(binding.titleBar.ivSearch) {
            //(requireActivity() as MainActivity).getNavController().navigate(R.id.searchFragment)
            startActivity(Intent(requireActivity(), SearchActivity::class.java))
            requireActivity().overridePendingTransition(R.anim.anl_push_up_in, 0)
        }
        viewPager = binding.viewPage

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