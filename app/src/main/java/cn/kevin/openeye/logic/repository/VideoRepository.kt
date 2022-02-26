package cn.kevin.openeye.logic.repository

import cn.kevin.openeye.logic.model.VideoDetail
import cn.kevin.openeye.logic.model.VideoReplies

interface VideoRepository {

    suspend fun getVideoReplies(url: String): VideoReplies

    suspend fun getVideoRelatedAndVideoReplies(videoId: Long, repliesUrl: String): VideoDetail

    suspend fun getVideoDetail(videoId: Long, repliesUrl: String): VideoDetail
}