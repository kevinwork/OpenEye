package cn.kevin.openeye.ui.newdetail


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.kevin.openeye.extension.logV
import cn.kevin.openeye.logic.api.VideoService
import cn.kevin.openeye.logic.model.VideoDetail
import cn.kevin.openeye.logic.model.VideoRelated
import cn.kevin.openeye.logic.model.VideoReplies
import cn.kevin.openeye.logic.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 视频详情界面绑定的ViewModel。
 */
@HiltViewModel
class NewDetailViewModel @Inject constructor(
    val repository: VideoRepository
) : ViewModel() {

    var relatedDataList = ArrayList<VideoRelated.Item>()

    var repliesDataList = ArrayList<VideoReplies.Item>()

    var videoInfoData: NewDetailActivity.VideoInfo? = null

    var videoId: Long = 0L

    // 用于控制获取下一页评论
    private val repliesState: MutableStateFlow<String?> = MutableStateFlow(null)

    // 用于控制获取视频详细信息
    private val videoDetailState: MutableStateFlow<RequestParam?> = MutableStateFlow(null)

    // 用于控制获取推荐视频列表和评论信息
    private val relatedAndRepliesState: MutableStateFlow<RequestParam?> = MutableStateFlow(null)

    // 获取下一页评论的访问路径
    var nextPageUrl: String? = null

    // 视频详细信息，评论信息和推荐视频列表
    val videoDetail: MutableStateFlow<VideoDetail?> = MutableStateFlow(null)

    // 获取视频评论信息和推荐视频列表
    val relatedAndReplies: MutableStateFlow<VideoDetail?> = MutableStateFlow(null)

    // 获取更多的视频评论信息（翻页）
    var replies: MutableStateFlow<VideoReplies?> = MutableStateFlow(null)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            // 视频信息+相关推荐+评论
            videoDetailState.collectLatest {
                it?.run {
                    videoDetail.value =
                        repository.getVideoDetail(videoId, repliesUrl)
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            // 相关推荐+评论
            relatedAndRepliesState.collectLatest {
                it?.run {
                    relatedAndReplies.value =
                        repository.getVideoRelatedAndVideoReplies(videoId, repliesUrl)
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            // 下一页评论
            repliesState.collectLatest {
                it?.run {
                    replies.value = repository.getVideoReplies(it)
                }
            }
        }
    }

    /**
     * 请求获取视频详情数据
     * 当在 NewDetailActivity 中打开推荐列表中的视频时，videoInfoData 已经有值了，
     * 所以不需要重新获取视频详细信息，只需要获取该视频的相关推荐视频和评论就行了，这样可以减少一次网络请求
     */
    fun onRefresh() {
        // videoInfoData 为空
        if (videoInfoData == null) {
            videoDetailState.value =
                RequestParam(videoId, "${VideoService.VIDEO_REPLIES_URL}$videoId")
        } else {
            // videoInfoData 不为空
            relatedAndRepliesState.value = RequestParam(
                videoInfoData?.videoId ?: 0L,
                "${VideoService.VIDEO_REPLIES_URL}${videoInfoData?.videoId ?: 0L}"
            )
        }
    }

    /**
     * 视频评论翻页
     */
    fun onLoadMore() {
        repliesState.value = nextPageUrl
    }

}

data class RequestParam(val videoId: Long, val repliesUrl: String)