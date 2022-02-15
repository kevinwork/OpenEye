package cn.kevin.openeye

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import cn.kevin.openeye.databinding.ActivityMainBinding
import cn.kevin.openeye.extension.setOnClickListener
import cn.kevin.openeye.ui.common.ui.FixFragmentNavigator
import cn.kevin.openeye.ui.community.CommunityFragment
import cn.kevin.openeye.ui.home.HomeFragment
import cn.kevin.openeye.ui.mine.MineFragment
import cn.kevin.openeye.ui.notification.NotificationFragment
import cn.kevin.openeye.ui.search.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //fragment复用
        //获取页面容器NavHostFragment
        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        //获取导航控制器
        navController = NavHostFragment.findNavController(fragment!!)
        //创建自定义的Fragment导航器
        val fragmentNavigator =
            FixFragmentNavigator(
                this,
                fragment.childFragmentManager,
                fragment.id
            )
        //获取导航器提供者
        val provider = navController.navigatorProvider
        //把自定义的Fragment导航器添加进去
        provider.addNavigator(fragmentNavigator)
        //手动创建导航图
        val navGraph = initNavGraph(provider, fragmentNavigator)
        //设置导航图
        navController.graph = navGraph

        val navigationBar = binding.navigationBar
        setOnClickListener(
            navigationBar.ivHomePage,
            navigationBar.ivCommunity,
            navigationBar.ivNotification,
            navigationBar.ivMine

        ) {
            when (this) {
                navigationBar.ivHomePage -> {
                    setTabSelection(0)
                    navController.navigate(R.id.homeFragment)
                }
                navigationBar.ivCommunity -> {
                    setTabSelection(1)
                    navController.navigate(R.id.communityFragment)
                }
                navigationBar.ivNotification ->{
                    setTabSelection(2)
                    navController.navigate(R.id.notificationFragment)
                }
                navigationBar.ivMine -> {
                    setTabSelection(3)
                    navController.navigate(R.id.mineFragment)
                }
            }
        }

        setTabSelection(0)
        navController.navigate(R.id.homeFragment)
    }


    private fun setTabSelection(index: Int) {
        clearAllSelected()
        when (index) {
            0 -> {
                binding.navigationBar.ivHomePage.isSelected = true
                binding.navigationBar.tvHomePage.isSelected = true
            }
            1 -> {
                binding.navigationBar.ivCommunity.isSelected = true
                binding.navigationBar.tvCommunity.isSelected = true
            }
            2 -> {
                binding.navigationBar.ivNotification.isSelected = true
                binding.navigationBar.tvNotification.isSelected = true
            }
            3 -> {
                binding.navigationBar.ivMine.isSelected = true
                binding.navigationBar.tvMine.isSelected = true
            }
            else -> {
                binding.navigationBar.ivHomePage.isSelected = true
                binding.navigationBar.tvHomePage.isSelected = true
            }
        }
    }

    private fun clearAllSelected() {
        binding.navigationBar.ivHomePage.isSelected = false
        binding.navigationBar.tvHomePage.isSelected = false
        binding.navigationBar.ivCommunity.isSelected = false
        binding.navigationBar.tvCommunity.isSelected = false
        binding.navigationBar.ivNotification.isSelected = false
        binding.navigationBar.tvNotification.isSelected = false
        binding.navigationBar.ivMine.isSelected = false
        binding.navigationBar.tvMine.isSelected = false
    }

    //手动创建导航图，把目的地添加进来
    private fun initNavGraph(
        provider: NavigatorProvider,
        fragmentNavigator: FixFragmentNavigator
    ): NavGraph {
        val navGraph = NavGraph(NavGraphNavigator(provider))

        //用自定义的导航器来创建目的地
        val destination1 = fragmentNavigator.createDestination()
        destination1.id = R.id.homeFragment
        destination1.className = HomeFragment::class.java.canonicalName
        destination1.label = resources.getString(R.string.homepage)
        navGraph.addDestination(destination1)

        val destination2 = fragmentNavigator.createDestination()
        destination2.id = R.id.communityFragment
        destination2.className = CommunityFragment::class.java.canonicalName
        destination2.label = resources.getString(R.string.community)
        navGraph.addDestination(destination2)

        val destination3 = fragmentNavigator.createDestination()
        destination3.id = R.id.notificationFragment
        destination3.className = NotificationFragment::class.java.canonicalName
        destination3.label = resources.getString(R.string.notification)
        navGraph.addDestination(destination3)

        val destination4 = fragmentNavigator.createDestination()
        destination4.id = R.id.mineFragment
        destination4.className = MineFragment::class.java.canonicalName
        destination4.label = resources.getString(R.string.mine)
        navGraph.addDestination(destination4)

        val destination5 = fragmentNavigator.createDestination()
        destination5.id = R.id.searchFragment
        destination5.className = SearchFragment::class.java.canonicalName
        destination5.label = resources.getString(R.string.search)
        navGraph.addDestination(destination5)

        navGraph.startDestination = R.id.homeFragment
        return navGraph
    }

    fun getNavController(): NavController {
        return navController
    }
}