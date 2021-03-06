package com.example.focusproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.focusproject.R
import com.example.focusproject.StartRoutineActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import kotlinx.android.synthetic.main.fragment_video_viewer.view.*

private const val VIDEO_URL = "param1"

class VideoViewerFragment : Fragment(){
    private var vidUrl: String = ""
    private lateinit var mYoutubePlayer : YouTubePlayer
    private lateinit var theview: View
    var duration: Long = 0
    var currentSecond: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            vidUrl = it.getString(VIDEO_URL).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        theview = inflater.inflate(R.layout.fragment_video_viewer, container, false)
        theview.videoPlayerView.getPlayerUiController().showFullscreenButton(true)
        theview.videoPlayerView.addYouTubePlayerListener(object : YouTubePlayerListener {
            override fun onApiChange(youTubePlayer: YouTubePlayer) {
            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                currentSecond = second.toLong() * 1000
                (activity as StartRoutineActivity).videoIsPlaying(currentSecond, duration)
            }

            override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
            }

            override fun onPlaybackQualityChange(
                youTubePlayer: YouTubePlayer,
                playbackQuality: PlayerConstants.PlaybackQuality
            ) {
            }

            override fun onPlaybackRateChange(
                youTubePlayer: YouTubePlayer,
                playbackRate: PlayerConstants.PlaybackRate
            ) {

            }

            override fun onReady(youTubePlayer: YouTubePlayer) {
                mYoutubePlayer = youTubePlayer
                mYoutubePlayer.cueVideo(vidUrl, 0f)
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                if (state == PlayerConstants.PlayerState.ENDED){
                    isFinished()
                }
                if (state == PlayerConstants.PlayerState.VIDEO_CUED){
                    playVideo()
                    (activity as StartRoutineActivity).playVideo()
                }
                if (state == PlayerConstants.PlayerState.PAUSED){
                    (activity as StartRoutineActivity).pauseVideo()
                }
            }

            override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {
                this@VideoViewerFragment.duration = duration.toLong() * 1000
            }

            override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {

            }

            override fun onVideoLoadedFraction(
                youTubePlayer: YouTubePlayer,
                loadedFraction: Float
            ) {

            }
        })
        return theview
    }

    fun playVideo(){
        if (this::mYoutubePlayer.isInitialized){
            println("Play video $mYoutubePlayer")
            mYoutubePlayer.play()
        }
    }

    fun pauseVideo(){
        mYoutubePlayer.pause()
    }

    fun isFinished() {
        (activity as StartRoutineActivity).nextExcercise()
    }

    companion object {
        @JvmStatic
        fun newInstance(vidUrl: String) =
            VideoViewerFragment().apply {
                arguments = Bundle().apply {
                    putString(VIDEO_URL, vidUrl)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        theview.videoPlayerView.release()
    }

}
