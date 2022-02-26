package cn.kevin.openeye.ui.newdetail

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import cn.kevin.openeye.R
import cn.kevin.openeye.databinding.ActivityNewDetailBinding
import cn.kevin.openeye.extension.*
import cn.kevin.openeye.logic.model.Author
import cn.kevin.openeye.logic.model.Consumption
import cn.kevin.openeye.logic.model.Cover
import cn.kevin.openeye.logic.model.WebUrl
import cn.kevin.openeye.ui.common.ui.BaseActivity
import cn.kevin.openeye.ui.common.view.NoStatusFooter
import cn.kevin.openeye.ui.login.LoginActivity
import cn.kevin.openeye.util.*
import cn.kevin.openeye.ui.newdetail.NewDetailRelatedAdapter
import cn.kevin.openeye.ui.newdetail.NewDetailReplyAdapter
import cn.kevin.openeye.ui.newdetail.NewDetailViewModel
import com.shuyu.gsyvideoplayer.GSYVideoADManager
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.parcelize.Parcelize


/**
 * 视频详情界面。
 */
@AndroidEntryPoint
class NewDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityNewDetailBinding

    private val viewModel: NewDetailViewModel by viewModels()
    // 视频详情-相关推荐列表绑定的适配器
    private lateinit var relatedAdapter: NewDetailRelatedAdapter
    // 视频详情-评论列表绑定的适配器
    private lateinit var replyAdapter: NewDetailReplyAdapter

    private lateinit var mergeAdapter: ConcatAdapter

    // 处理屏幕旋转的的逻辑
    private var orientationUtils: OrientationUtils? = null

    private val globalJob by lazy { Job() }

    private var hideTitleBarJob: Job? = null

    private var hideBottomContainerJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setContentView(layoutView: View) {
        if (checkArguments()) {
            super.setContentView(layoutView)
            setStatusBarBackground(R.color.black)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (checkArguments()) {
            initParams()
            startVideoPlayer()
            viewModel.onRefresh()
        }
    }

    override fun onPause() {
        super.onPause()
        binding.videoPlayer.onVideoPause()
    }

    override fun onResume() {
        super.onResume()
        binding.videoPlayer.onVideoResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoADManager.releaseAllVideos()
        orientationUtils?.releaseListener()
        binding.videoPlayer.release()
        binding.videoPlayer.setVideoAllCallBack(null)
        globalJob.cancel()
    }

    override fun onBackPressed() {
        orientationUtils?.backToProtVideo()
        if (GSYVideoManager.backFromWindowFull(this)) return
        super.onBackPressed()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.anl_push_bottom_out)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.videoPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true)
    }

    override fun setupViews() {
        super.setupViews()
        initParams()
        orientationUtils = OrientationUtils(this, binding.videoPlayer)
        relatedAdapter =
            NewDetailRelatedAdapter(this, viewModel.relatedDataList, viewModel.videoInfoData)
        replyAdapter = NewDetailReplyAdapter(this, viewModel.repliesDataList)
        // 显示完推荐视频列表，再显示评论列表
        mergeAdapter = ConcatAdapter(relatedAdapter, replyAdapter)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = mergeAdapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.itemAnimator = null
        binding.refreshLayout.run {
            // SmartRefreshLayout 的设置，允许上拉刷新
            setDragRate(0.7f)
            setHeaderTriggerRate(0.6f)
            setFooterTriggerRate(0.6f)
            setEnableLoadMoreWhenContentNotFull(true)
            setEnableFooterFollowWhenNoMoreData(true)
            setEnableFooterTranslationContent(true)
            setEnableScrollContentWhenLoaded(true)
            binding.refreshLayout.setEnableNestedScroll(true)
            setFooterHeight(153f)
            setRefreshFooter(NoStatusFooter(this@NewDetailActivity).apply {
                setAccentColorId(R.color.white)
                setTextTitleSize(16f)
            })
            setOnRefreshListener { finish() }
            // 上拉刷新，获取更多评论
            setOnLoadMoreListener { viewModel.onLoadMore() }
        }
        setOnClickListener(
            binding.ivPullDown,
            binding.ivMore,
            binding.ivShare,
            binding.ivCollection,
            binding.ivToWechatFriends,
            binding.ivShareToWechatMemories,
            binding.ivShareToWeibo,
            binding.ivShareToQQ,
            binding.ivShareToQQzone,
            binding.ivAvatar,
            binding.etComment,
            binding.ivReply,
            binding.tvReplyCount,
            listener = ClickListener()
        )
        observe()
        startVideoPlayer()
        viewModel.onRefresh()
    }

    /**
     * 查看是否带有 videoInfo 或者 videoId 参数，如果都没有，提示错误
     */
    private fun checkArguments() =
        if (intent.getParcelableExtra<VideoInfo>(EXTRA_VIDEOINFO) == null && intent.getLongExtra(
                EXTRA_VIDEO_ID, 0L
            ) == 0L
        ) {
            GlobalUtil.getString(R.string.jump_page_unknown_error).showToast()
            finish()
            false
        } else {
            true
        }

    /**
     * 从 Intent 中获取参数，给 viewModel 赋值，videoInfo 或者 videoId
     */
    private fun initParams() {
        if (intent.getParcelableExtra<VideoInfo>(EXTRA_VIDEOINFO) != null) {
            viewModel.videoInfoData = intent.getParcelableExtra(EXTRA_VIDEOINFO)
        }
        if (intent.getLongExtra(EXTRA_VIDEO_ID, 0L) != 0L) {
            viewModel.videoId = intent.getLongExtra(EXTRA_VIDEO_ID, 0L)
        }
    }

    private fun startVideoPlayer() {
        viewModel.videoInfoData?.run {
            // 将视频封面图片，设置为整个视频详情页的模糊背景，一种沉浸式的体验
            binding.ivBlurredBg.load(cover.blurred)
            // 评论数
            binding.tvReplyCount.text = consumption.replyCount.toString()
            binding.videoPlayer.startPlay()
        }
    }

    private fun observe() {
        //刷新，视频信息+相关推荐+评论
        lifecycleScope.launchWhenCreated {
            viewModel.videoDetail.collectLatest {
                it?.run {
                    viewModel.nextPageUrl = videoReplies.nextPageUrl
                    videoBeanForClient?.run {
                        viewModel.videoInfoData = VideoInfo(
                            id,
                            playUrl,
                            title,
                            description,
                            category,
                            library,
                            consumption,
                            cover,
                            author,
                            webUrl
                        )
                        startVideoPlayer()
                        relatedAdapter.bindVideoInfo(viewModel.videoInfoData)
                    }
                    viewModel.relatedDataList.clear()
                    viewModel.repliesDataList.clear()
                    videoRelated?.run { viewModel.relatedDataList.addAll(itemList) }
                    viewModel.repliesDataList.addAll(videoReplies.itemList)
                    relatedAdapter.notifyDataSetChanged()
                    replyAdapter.notifyDataSetChanged()
                    when {
                        viewModel.repliesDataList.isNullOrEmpty() -> binding.refreshLayout.finishLoadMoreWithNoMoreData()
                        videoReplies.nextPageUrl.isNullOrEmpty() -> binding.refreshLayout.finishLoadMoreWithNoMoreData()
                        else -> binding.refreshLayout.closeHeaderOrFooter()
                    }
                }

            }
        }

        //刷新，相关推荐+评论
        lifecycleScope.launchWhenCreated {
            viewModel.relatedAndReplies.collectLatest {
                it?.run {
                    viewModel.nextPageUrl = videoReplies.nextPageUrl
                    viewModel.relatedDataList.clear()
                    viewModel.repliesDataList.clear()
                    videoRelated?.run { viewModel.relatedDataList.addAll(itemList) }
                    viewModel.repliesDataList.addAll(videoReplies.itemList)
                    relatedAdapter.bindVideoInfo(viewModel.videoInfoData)
                    relatedAdapter.notifyDataSetChanged()
                    replyAdapter.notifyDataSetChanged()
                    when {
                        viewModel.repliesDataList.isNullOrEmpty() -> binding.refreshLayout.finishLoadMoreWithNoMoreData()
                        videoReplies.nextPageUrl.isNullOrEmpty() -> binding.refreshLayout.finishLoadMoreWithNoMoreData()
                        else -> binding.refreshLayout.closeHeaderOrFooter()
                    }
                }
            }
        }

        //上拉加载，评论
        lifecycleScope.launchWhenCreated {
            viewModel.replies.collectLatest {
                it?.run {
                    viewModel.nextPageUrl = nextPageUrl
                    val itemCount = replyAdapter.itemCount
                    viewModel.repliesDataList.addAll(itemList)
                    replyAdapter.notifyItemRangeInserted(itemCount, itemList.size)
                    if (nextPageUrl.isNullOrEmpty()) {
                        binding.refreshLayout.finishLoadMoreWithNoMoreData()
                    } else {
                        binding.refreshLayout.closeHeaderOrFooter()
                    }
                }
            }
        }
    }

    private fun GSYVideoPlayer.startPlay() {
        viewModel.videoInfoData?.let {
            //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
            fullscreenButton.setOnClickListener { showFull() }
            //防止错位设置
            playTag = TAG
            //音频焦点冲突时是否释放
            isReleaseWhenLossAudio = false
            //增加封面
            val imageView = ImageView(this@NewDetailActivity)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.load(it.cover.detail)
            thumbImageView = imageView
            thumbImageView.setOnClickListener { switchTitleBarVisible() }
            //是否开启自动旋转
            isRotateViewAuto = false
            //是否需要全屏锁定屏幕功能
            isNeedLockFull = true
            //是否可以滑动调整
            setIsTouchWiget(true)
            //设置触摸显示控制ui的消失时间
            dismissControlTime = 5000
            //设置播放过程中的回调
            setVideoAllCallBack(VideoCallPlayBack())
            //设置播放URL
            setUp(it.playUrl, false, it.title)
            //开始播放
            startPlayLogic()
        }
    }

    /**
     * 控制 flHeader 中的图标隐藏或者显示
     */
    private fun switchTitleBarVisible() {
        // 已经播放完成，直接返回
        if (binding.videoPlayer.currentPlayer.currentState == GSYVideoView.CURRENT_STATE_AUTO_COMPLETE) return
        if (binding.flHeader.visibility == View.VISIBLE) {
            // 隐藏
            hideTitleBar()
        } else {
            // 先慢慢显示，显示一段时间之后，再慢慢隐藏
            binding.flHeader.visibleAlphaAnimation(1000)
            binding.ivPullDown.visibleAlphaAnimation(1000)
            binding.ivCollection.visibleAlphaAnimation(1000)
            binding.ivMore.visibleAlphaAnimation(1000)
            binding.ivShare.visibleAlphaAnimation(1000)
            delayHideTitleBar()
        }
    }

    /**
     * 以动画的形式慢慢隐藏 flHeader 中的图标（分享、收藏等）
     */
    private fun hideTitleBar() {
        binding.flHeader.invisibleAlphaAnimation(1000)
        binding.ivPullDown.goneAlphaAnimation(1000)
        binding.ivCollection.goneAlphaAnimation(1000)
        binding.ivMore.goneAlphaAnimation(1000)
        binding.ivShare.goneAlphaAnimation(1000)
    }

    private fun delayHideTitleBar() {
        // 防止重复执行，将之前启动的协程取消
        hideTitleBarJob?.cancel()
        hideTitleBarJob = CoroutineScope(globalJob).launch(Dispatchers.Main) {
            // 延迟 5 秒钟，再隐藏
            delay(binding.videoPlayer.dismissControlTime.toLong())
            hideTitleBar()
        }
    }

    /**
     * 隐藏底部区域
     */
    private fun delayHideBottomContainer() {
        hideBottomContainerJob?.cancel()
        hideBottomContainerJob = CoroutineScope(globalJob).launch(Dispatchers.Main) {
            delay(binding.videoPlayer.dismissControlTime.toLong())
            binding.videoPlayer.getBottomContainer().gone()
            // 隐藏播放按键
            binding.videoPlayer.startButton.gone()
        }
    }

    private fun showFull() {
        // 点击切换的逻辑，比如竖屏的时候点击了就是切换到横屏不会受屏幕的影响
        orientationUtils?.resolveByClick()
    }

    fun scrollTop() {
        if (relatedAdapter.itemCount != 0) {
            binding.recyclerView.scrollToPosition(0)
            binding.refreshLayout.invisibleAlphaAnimation(2500)
            binding.refreshLayout.visibleAlphaAnimation(1500)
        }
    }

    /**
     * 滚动到评论位置
     */
    private fun scrollRepliesTop() {
        val targetPostion = (relatedAdapter.itemCount - 1) + 2  //+相关推荐最后一项，+1评论标题，+1条评论
        if (targetPostion < mergeAdapter.itemCount - 1) {
            binding.recyclerView.smoothScrollToPosition(targetPostion)
        }
    }

    inner class VideoCallPlayBack : GSYSampleCallBack() {
        // 缓冲完成
        override fun onStartPrepared(url: String?, vararg objects: Any?) {
            super.onStartPrepared(url, *objects)
            binding.flHeader.gone()
            binding.llShares.gone()
        }

        // 点击空白区域
        override fun onClickBlank(url: String?, vararg objects: Any?) {
            super.onClickBlank(url, *objects)
            switchTitleBarVisible()
        }

        // 点击暂停
        override fun onClickStop(url: String?, vararg objects: Any?) {
            super.onClickStop(url, *objects)
            delayHideBottomContainer()
        }

        // 播放完成
        override fun onAutoComplete(url: String?, vararg objects: Any?) {
            super.onAutoComplete(url, *objects)
            binding.flHeader.visible()
            binding.ivPullDown.visible()
            binding.ivCollection.gone()
            binding.ivShare.gone()
            binding.ivMore.gone()
            binding.llShares.visible()
        }
    }

    /**
     * 控件的点击事件处理
     */
    inner class ClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            viewModel.videoInfoData?.let {
                when (v) {
                    // 下拉退出
                    binding.ivPullDown -> finish()
                    binding.ivMore -> {
                    }
                    // 分享
                    binding.ivShare -> showDialogShare(it.webUrl.raw)
                    binding.ivCollection -> LoginActivity.start(this@NewDetailActivity)
                    binding.ivToWechatFriends -> share(it.webUrl.raw, SHARE_WECHAT)
                    binding.ivShareToWechatMemories -> share(it.webUrl.raw, SHARE_WECHAT_MEMORIES)
                    binding.ivShareToWeibo -> share(it.webUrl.forWeibo, SHARE_WEIBO)
                    binding.ivShareToQQ -> share(it.webUrl.raw, SHARE_QQ)
                    binding.ivShareToQQzone -> share(it.webUrl.raw, SHARE_QQZONE)
                    binding.ivAvatar, binding.etComment -> LoginActivity.start(this@NewDetailActivity)
                    binding.ivReply, binding.tvReplyCount -> scrollRepliesTop()
                    else -> {
                    }
                }
            }
        }
    }

    @Parcelize
    data class VideoInfo(
        val videoId: Long,
        val playUrl: String,
        val title: String,
        val description: String,
        val category: String,
        val library: String,
        val consumption: Consumption,
        val cover: Cover,
        val author: Author?,
        val webUrl: WebUrl
    ) : Parcelable

    companion object {
        const val EXTRA_VIDEOINFO = "videoInfo"
        const val EXTRA_VIDEO_ID = "videoId"

        /**
         * 能够构建一个 VideoInfo 对象，使用此方法跳转，这样可以不需要再通过额外的网络请求获取视频信息
         */
        fun start(context: Activity, videoInfo: VideoInfo) {
            val starter = Intent(context, NewDetailActivity::class.java)
            starter.putExtra(EXTRA_VIDEOINFO, videoInfo)
            context.startActivity(starter)
            context.overridePendingTransition(R.anim.anl_push_bottom_in, R.anim.anl_push_up_out)
        }

        /**
         * 只有 videoId，使用此方法跳转
         */
        fun start(context: Activity, videoId: Long) {
            val starter = Intent(context, NewDetailActivity::class.java)
            starter.putExtra(EXTRA_VIDEO_ID, videoId)
            context.startActivity(starter)
            context.overridePendingTransition(R.anim.anl_push_bottom_in, R.anim.anl_push_up_out)
        }
    }
}