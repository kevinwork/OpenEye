package cn.kevin.openeye.ui.search

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cn.kevin.openeye.MainActivity
import cn.kevin.openeye.R
import cn.kevin.openeye.databinding.ActivitySearchBinding
import cn.kevin.openeye.databinding.FragmentSearchBinding
import cn.kevin.openeye.extension.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * 搜索页面，从主页面跳转过来
 * 一进入，默认聚焦在输入框，弹出软件盘
 * 输入文本，以列表显示查询记录
 * 点击搜索后(键盘上的搜索按键)，根据已输入信息进行搜索。搜索结果以列表展示
 * 安吉取消，退出当前页面，返回主页面
 */
@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    companion object {
        val TAG = "SearchActivity"
    }

    private lateinit var binding : ActivitySearchBinding
    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var adapter: HotSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.llSearch.visibleAlphaAnimation(500)
        binding.etQuery.setOnEditorActionListener(EditorActionListener())
        setOnClickListener(binding.tvCancel) {
            hideSoftKeyboard()
            finish()
            overridePendingTransition(0, R.anim.anl_push_top_out)
            //(requireActivity() as MainActivity).getNavController().navigate(R.id.homeFragment)
        }
        //recycleView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HotSearchAdapter(viewModel.dataList)
        binding.recyclerView.adapter = adapter

        //默认主线程 但是retrofit会默认后台线程运行，所以整个流程不影响
        lifecycleScope.launchWhenCreated {
            viewModel.data
                .catch {
                        e->e.printStackTrace()
                    logV(TAG, "catch exception....")
                }.collect {
                    //主线程
                    viewModel.dataList.clear()
                    viewModel.dataList.addAll(it)
                    adapter.notifyDataSetChanged()

                    binding.etQuery.showSoftKeyboard()
                }
        }

    }

    override fun onBackPressed() {
        //已经调用了finish
        super.onBackPressed()
        overridePendingTransition(0, R.anim.anl_push_top_out)
    }

    override fun onDestroy() {
        super.onDestroy()
        overridePendingTransition(0, R.anim.anl_push_top_out)
    }

    fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        logD(TAG, "onCreateAnimation....")

        return if (enter) {
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                logD(TAG, "showSoftKeyboard....")
                binding.etQuery.showSoftKeyboard() },
                2000)
            AnimationUtils.loadAnimation(this, R.anim.anl_push_up_in)
        } else {
            AnimationUtils.loadAnimation(this, R.anim.anl_push_top_out)
        }

    }


    /**
     * 隐藏软键盘
     */
    private fun hideSoftKeyboard() {
        try {
            this.currentFocus?.run {
                val imm =
                    this@SearchActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(this.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        } catch (e: Exception) {
            logW(TAG, e.message, e)
        }
    }

    /**
     * 拉起软键盘
     */
    private fun View.showSoftKeyboard() {
        try {
            this.isFocusable = true
            this.isFocusableInTouchMode = true
            this.requestFocus()
            val manager =
                this@SearchActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.showSoftInput(this, 0)
        } catch (e: Exception) {
            logW(TAG, e.message, e)
        }
    }

    inner class EditorActionListener : TextView.OnEditorActionListener {

        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (binding.etQuery.text.toString().isEmpty()) {
                    R.string.input_keywords_tips.showToast()
                    return false
                }
                R.string.currently_not_supported.showToast()
                return true
            }
            return true
        }

    }



}