/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.exoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import com.google.ads.interactivemedia.v3.api.player.ContentProgressProvider;
import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;

import com.google.ads.interactivemedia.v3.api.AdDisplayContainer;
import com.google.ads.interactivemedia.v3.api.AdErrorEvent;
import com.google.ads.interactivemedia.v3.api.AdErrorEvent.AdErrorListener;
import com.google.ads.interactivemedia.v3.api.AdEvent;
import com.google.ads.interactivemedia.v3.api.AdEvent.AdEventListener;
import com.google.ads.interactivemedia.v3.api.AdsLoader;
import com.google.ads.interactivemedia.v3.api.AdsLoader.AdsLoadedListener;
import com.google.ads.interactivemedia.v3.api.AdsManager;
import com.google.ads.interactivemedia.v3.api.AdsManagerLoadedEvent;
import com.google.ads.interactivemedia.v3.api.AdsRequest;
import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
import com.google.ads.interactivemedia.v3.api.ImaSdkSettings;

/**
 * A fullscreen activity to play audio or video streams.
 */
public class PlayerActivity extends AppCompatActivity implements AdEventListener, AdErrorListener {
    private static final String TAG = "PlayerActivity";

    private PlayerView playerView;
    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;


    private static String LOGTAG = "ImaExample";
    private AdsLoader mAdsLoader;
    private AdsManager mAdsManager;
    private ViewGroup mAdUiContainer;
    private boolean mIsAdDisplayed;
    // Factory class for creating SDK objects.
    private ImaSdkFactory mSdkFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        mAdUiContainer = (ViewGroup) findViewById(R.id.videoPlayerWithAdPlayback);

        playerView = findViewById(R.id.video_view);
        initAds();
    }

    private void initAds() {
        // Create an AdsLoader.
        mSdkFactory = ImaSdkFactory.getInstance();
        AdDisplayContainer adDisplayContainer =
                ImaSdkFactory.createAdDisplayContainer(
                        mAdUiContainer,
                        ImaSdkFactory.createSdkOwnedPlayer(this, mAdUiContainer));
        ImaSdkSettings settings = mSdkFactory.createImaSdkSettings();
        mAdsLoader = mSdkFactory.createAdsLoader(this, settings, adDisplayContainer);

        // Add listeners for when ads are loaded and for errors.
        mAdsLoader.addAdErrorListener(this);
        mAdsLoader.addAdsLoadedListener(new AdsLoadedListener() {
            @Override
            public void onAdsManagerLoaded(AdsManagerLoadedEvent adsManagerLoadedEvent) {
                // Ads were successfully loaded, so get the AdsManager instance. AdsManager has
                // events for ad playback and errors.
                mAdsManager = adsManagerLoadedEvent.getAdsManager();

                // Attach event and error event listeners.
                mAdsManager.addAdErrorListener(PlayerActivity.this);
                mAdsManager.addAdEventListener(PlayerActivity.this);
                mAdsManager.init();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if (mAdsManager != null && mIsAdDisplayed) {
            mAdsManager.resume();
        } else {
            if ((Util.SDK_INT <= 23 || player == null)) {
                initializePlayer();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdsManager != null && mIsAdDisplayed) {
            mAdsManager.pause();
        } else {
            if (Util.SDK_INT <= 23) {
                releasePlayer();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(this);

        //listener
        player.addListener(eventListener);

        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        trackSelector.setParameters(
                trackSelector.buildUponParameters().setMaxVideoSizeSd());
        player.addAnalyticsListener(new EventLogger(trackSelector));


        playerView.setPlayer(player);

        Uri uri = Uri.parse(getString(R.string.media_url_mp4_1));
        MediaSource mediaSource = buildMediaSource(uri);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, false, false);
    }

    private void requestAds(String adTagUrl) {
        // Create the ads request.
        AdsRequest request = mSdkFactory.createAdsRequest();
        request.setAdTagUrl(adTagUrl);
        request.setContentProgressProvider(
                new ContentProgressProvider() {
                    @Override
                    public VideoProgressUpdate getContentProgress() {
                        if (mIsAdDisplayed || player == null ||player.getDuration()<=0) {
                            return VideoProgressUpdate.VIDEO_TIME_NOT_READY;
                        }
                        return new VideoProgressUpdate(
                                player.getCurrentPosition(), player.getDuration());
                    }
                });

        // Request the ad. After the ad is loaded, onAdsManagerLoaded() will be called.
        mAdsLoader.requestAds(request);
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, "exoplayer-codelab");
        ProgressiveMediaSource.Factory mediaSourceFactory =
                new ProgressiveMediaSource.Factory(dataSourceFactory);

        // Create a media source using the supplied URI
        MediaSource mediaSource1 = mediaSourceFactory.createMediaSource(uri);

        // Additionally create a media source using an MP3
        Uri audioUri = Uri.parse(getString(R.string.media_url_mp4_2));
        MediaSource mediaSource2 = mediaSourceFactory.createMediaSource(audioUri);

        //创建一个播放列表
        return new ConcatenatingMediaSource(mediaSource1, mediaSource2);
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private Player.EventListener eventListener = new Player.EventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {
                case Player.STATE_IDLE:
                    Log.d(TAG, playbackState + " , " + "STATE_IDLE");
                    break;
                case Player.STATE_BUFFERING:
                    Log.d(TAG, playbackState + " , " + "STATE_BUFFERING");
                    break;
                case Player.STATE_READY:
                    Log.d(TAG, playbackState + " , " + "STATE_READY");
                    break;
                case Player.STATE_ENDED:
                    Log.d(TAG, playbackState + " , " + "STATE_ENDED");
                    requestAds(getString(R.string.ad_tag_url));
                    break;
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            if (error.type == ExoPlaybackException.TYPE_SOURCE) {
                IOException cause = error.getSourceException();
                if (cause instanceof HttpDataSource.HttpDataSourceException) {
                    // An HTTP error occurred.
                    HttpDataSource.HttpDataSourceException httpError = (HttpDataSource.HttpDataSourceException) cause;
                    // This is the request for which the error occurred.
                    DataSpec requestDataSpec = httpError.dataSpec;
                    // It's possible to find out more about the error both by casting and by
                    // querying the cause.
                    if (httpError instanceof HttpDataSource.InvalidResponseCodeException) {
                        // Cast to InvalidResponseCodeException and retrieve the response code,
                        // message and headers.

                        Log.d(TAG, httpError.getMessage());
                    } else {
                        // Try calling httpError.getCause() to retrieve the underlying cause,
                        // although note that it may be null.
                        Log.d(TAG, httpError.getMessage());
                    }
                }
            }
        }

        @Override
        public void onIsPlayingChanged(boolean isPlaying) {

        }
    };

    @Override
    public void onAdError(AdErrorEvent adErrorEvent) {
        Log.e(LOGTAG, "Ad Error: " + adErrorEvent.getError().getMessage());
        initializePlayer();
    }

    @Override
    public void onAdEvent(AdEvent adEvent) {
        Log.i(LOGTAG, "Event: " + adEvent.getType());

        // These are the suggested event types to handle. For full list of all ad event
        // types, see the documentation for AdEvent.AdEventType.
        switch (adEvent.getType()) {
            case LOADED:
                // AdEventType.LOADED will be fired when ads are ready to be played.
                // AdsManager.start() begins ad playback. This method is ignored for VMAP or
                // ad rules playlists, as the SDK will automatically start executing the
                // playlist.
                mAdsManager.start();
                break;
            case CONTENT_PAUSE_REQUESTED:
                // AdEventType.CONTENT_PAUSE_REQUESTED is fired immediately before a video
                // ad is played.
                mIsAdDisplayed = true;
                releasePlayer();
                break;
            case CONTENT_RESUME_REQUESTED:
                // AdEventType.CONTENT_RESUME_REQUESTED is fired when the ad is completed
                // and you should start playing your content.
                mIsAdDisplayed = false;
                initializePlayer();
                break;
            case ALL_ADS_COMPLETED:
                if (mAdsManager != null) {
                    mAdsManager.destroy();
                    mAdsManager = null;
                }
                break;
            default:
                break;
        }
    }
}
