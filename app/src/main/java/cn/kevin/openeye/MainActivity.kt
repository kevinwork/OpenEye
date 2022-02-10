package cn.kevin.openeye

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import cn.kevin.openeye.databinding.ActivityMainBinding
import cn.kevin.openeye.extension.setOnClickListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val navgationBar = binding.navigationBar

        setOnClickListener(
            navgationBar.ivHomePage,
            navgationBar.ivCommunity,
            navgationBar.ivNotification,
            navgationBar.ivMine

        ) {
            when (this) {
                navgationBar.ivHomePage -> {
                    setTabSelection(0)
                    navController.navigate(R.id.homeFragment)
                }
                navgationBar.ivCommunity -> {
                    setTabSelection(1)
                    navController.navigate(R.id.communityFragment)
                }
                navgationBar.ivNotification ->{
                    setTabSelection(2)
                    navController.navigate(R.id.notificationFragment)
                }
                navgationBar.ivMine -> {
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
}