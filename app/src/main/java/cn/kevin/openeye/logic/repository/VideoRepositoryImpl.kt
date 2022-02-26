package cn.kevin.openeye.logic.repository

import android.util.Log
import cn.kevin.openeye.logic.api.VideoService
import cn.kevin.openeye.logic.model.VideoDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

/**
 * 视频相关，对应的仓库数据管理。
 */
class VideoRepositoryImpl(
    private val videoService: VideoService
) : VideoRepository {

    override suspend fun getVideoReplies(url: String) = withContext(Dispatchers.IO) {
        coroutineScope {
            val deferredVideoReplies = async { videoService.getVideoReplies(url) }
            val videoReplies = deferredVideoReplies.await()
            videoReplies
        }
    }

    override suspend fun getVideoRelatedAndVideoReplies(
        videoId: Long,
        repliesUrl: String
    ): VideoDetail =
        withContext(Dispatchers.IO) {
            coroutineScope {
                val deferredVideoRelated = async { videoService.getVideoRelated(videoId) }
                val deferredVideoReplies = async { videoService.getVideoReplies(repliesUrl) }
                val videoRelated = deferredVideoRelated.await()
                val videoReplies = deferredVideoReplies.await()
                val videoDetail = VideoDetail(null, videoRelated, videoReplies)
                videoDetail
            }
        }

    override suspend fun getVideoDetail(videoId: Long, repliesUrl: String): VideoDetail =
        withContext(Dispatchers.IO) {
            Log.d("ning","requestVideoDetail")
            coroutineScope {
                val deferredVideoRelated = async { videoService.getVideoRelated(videoId) }
                val deferredVideoReplies = async { videoService.getVideoReplies(repliesUrl) }
                val deferredVideoBeanForClient =
                    async { videoService.getVideoBeanForClient(videoId) }
                val videoBeanForClient = deferredVideoBeanForClient.await()
                val videoRelated = deferredVideoRelated.await()
                val videoReplies = deferredVideoReplies.await()
                val videoDetail = VideoDetail(videoBeanForClient, videoRelated, videoReplies)

                /*if (videoDetail.videoBeanForClient != null && videoDetail.videoRelated?.count ?: 0 > 0 && videoDetail.videoReplies.count > 0) {
                    dao.cacheVideoDetail(videoDetail)
                }*/
                videoDetail
            }
        }

}