package cn.kevin.openeye.ui.home.discovery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.kevin.openeye.R
import cn.kevin.openeye.databinding.FragmentDiscoveryBinding
import cn.kevin.openeye.extension.logD
import cn.kevin.openeye.ui.common.callback.RequestLifecycle
import cn.kevin.openeye.ui.common.ui.BaseFragment
import cn.kevin.openeye.util.GlobalUtil
import cn.kevin.openeye.util.ResponseHandler
import com.google.gson.Gson
import com.scwang.smart.refresh.layout.constant.RefreshState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


/**
 * A simple [Fragment] subclass.
 * Use the [DiscoveryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class DiscoveryFragment : BaseFragment(){

    //ui
    private lateinit var binding: FragmentDiscoveryBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var discoveryAdapter: DiscoveryAdapter

    //viewModel
    private val viewModel by viewModels<DiscoveryViewModel>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDiscoveryBinding.inflate(layoutInflater, container, false)
        return super.onCreateView(binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDiscoveryBinding.inflate(layoutInflater)
        recyclerView = binding.recyclerView
        discoveryAdapter = DiscoveryAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = discoveryAdapter

        lifecycleScope.launchWhenCreated {
            viewModel.getPagingData().collectLatest {
                logD("zwz", "response:"+Gson().toJson(it))
                discoveryAdapter.submitData(it)
            }
        }
        binding.refreshLayout.setOnClickListener{
            discoveryAdapter.refresh()
        }
    }

    private fun addLoadStateListener() {
        discoveryAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
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
                    if (binding.refreshLayout.state != RefreshState.Refreshing) {
                        startLoading()
                    }
                }
                is LoadState.Error -> {
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
            discoveryAdapter.refresh()
        }
    }

    companion object {
        fun newInstance() =
            DiscoveryFragment()
    }

}