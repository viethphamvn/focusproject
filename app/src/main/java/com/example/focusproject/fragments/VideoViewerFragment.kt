package com.example.focusproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull

import com.example.focusproject.R
import com.example.focusproject.StartRoutineActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import kotlinx.android.synthetic.main.fragment_video_viewer.view.videoPlayerView

private const val VIDEO_URL = "param1"

class VideoViewerFragment : Fragment(){
    // TODO: Rename and change types of parameters
    private var vidUrl: String = ""
    private lateinit var mYoutubePlayer : YouTubePlayer

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
        var view = inflater.inflate(R.layout.fragment_video_viewer, container, false)
        view.videoPlayerView.getPlayerUiController().showFullscreenButton(true)
        view.videoPlayerView.addYouTubePlayerListener(object : YouTubePlayerListener {
            override fun onApiChange(youTubePlayer: YouTubePlayer) {
            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
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
                (activity as StartRoutineActivity).playVideo()
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                if (state == PlayerConstants.PlayerState.ENDED){
                    isFinished()
                }
            }

            override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {

            }

            override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {

            }

            override fun onVideoLoadedFraction(
                youTubePlayer: YouTubePlayer,
                loadedFraction: Float
            ) {

            }
        })
        return view
    }

    fun playVideo(){
        if (this::mYoutubePlayer.isInitialized){
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

}
