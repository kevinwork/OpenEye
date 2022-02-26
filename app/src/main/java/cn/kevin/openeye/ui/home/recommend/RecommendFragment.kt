package cn.kevin.openeye.ui.home.recommend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.kevin.openeye.R
import cn.kevin.openeye.databinding.FragmentDiscoveryBinding
import cn.kevin.openeye.extension.logD
import cn.kevin.openeye.ui.common.ui.BaseFragment
import cn.kevin.openeye.ui.home.discovery.DiscoveryFragment
import cn.kevin.openeye.ui.home.discovery.DiscoveryViewModel
import cn.kevin.openeye.util.GlobalUtil
import cn.kevin.openeye.util.ResponseHandler
import com.google.gson.Gson
import com.scwang.smart.refresh.layout.constant.RefreshState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest



@AndroidEntryPoint
class RecommendFragment : BaseFragment() {
    //ui
    private lateinit var binding: FragmentDiscoveryBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecommendAdapter

    //viewModel
    private val viewModel by viewModels<RecommendViewModel>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiscoveryBinding.inflate(layoutInflater, container, false)
        return super.onCreateView(binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = RecommendAdapter(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter

        lifecycleScope.launchWhenCreated {
            viewModel.getPagingData().collectLatest {
                //val ret = Gson().toJson(it)
                //logD("zwz", "response:"+ret)
                adapter.submitData(it)
            }
        }
        binding.refreshLayout.setOnRefreshListener{
            logD("zwz", "refresh....")
            adapter.refresh()

        }
        addLoadStateListener()
    }

    private fun addLoadStateListener() {
        adapter.addLoadStateListener {

            when (it.refresh) {
                is LoadState.NotLoading -> {
                    logD("zwz0", "LoadState.NotLoading")
                    loadFinished()
                    // 到了最后一页，显示 “The End” NoStatusFooter
                    if (it.source.append.endOfPaginationReached) {
                        // 显示
                        binding.refreshLayout.setEnableLoadMore(true)
                        binding.refreshLayout.finishLoadMoreWithNoMoreData()
                    } else {
                        // 不显示
                        binding.refreshLayout.setEnableLoadMore(false)
                    }
                }
                is LoadState.Loading -> {
                    logD("zwz0", "LoadState.Loading")
                    if (binding.refreshLayout.state != RefreshState.Refreshing) {
                        startLoading()
                    }
                }
                is LoadState.Error -> {
                    logD("zwz0", "LoadState.error")
                    val state = it.refresh as LoadState.Error
                    loadFailed(ResponseHandler.getFailureTips(state.error))
                }
            }
        }
    }

    override fun loadFinished() {
        super.loadFinished()
        binding.refreshLayout.finishRefresh()
    }


    override fun loadFailed(msg: String?) {
        super.loadFailed(msg)
        binding.refreshLayout.finishRefresh()
        showLoadErrorView(msg ?: GlobalUtil.getString(R.string.unknown_error)) {
            startLoading()
            adapter.refresh()
        }
    }

    companion object {
        fun newInstance() =
            RecommendFragment()
    }
}