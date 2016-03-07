package com.example.ant_test.media_player;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;

import com.example.ant_test.R;

/**
 * Created by 3020mt on 2015/10/22.
 */
public class MediaPlayerActivity extends Activity{

    private SurfaceView surfaceView;
    private Button btnPause, btnPlayUrl, btnStop;
    private SeekBar skbProgress;
    private Player player;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView1);
//
//        btnPlayUrl = (Button) this.findViewById(R.id.btnPlayUrl);
//        btnPlayUrl.setOnClickListener(new ClickEvent());
//
//        btnPause = (Button) this.findViewById(R.id.btnPause);
//        btnPause.setOnClickListener(new ClickEvent());
//
//        btnStop = (Button) this.findViewById(R.id.btnStop);
//        btnStop.setOnClickListener(new ClickEvent());
//
//        skbProgress = (SeekBar) this.findViewById(R.id.skbProgress);
//        skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
//        player = new Player(surfaceView, skbProgress);
        ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.setAdapter(new TestAdapter(this));
    }

    class ClickEvent implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            if (arg0 == btnPause) {
                player.pause();
            } else if (arg0 == btnPlayUrl) {
                String url="http://10.21.53.210/test1/video/xiaomaolv_h263.3gp";
                player.playUrl(url);
            } else if (arg0 == btnStop) {
                player.stop();
            }

        }
    }

    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
            this.progress = progress * player.mediaPlayer.getDuration()
                    / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
            player.mediaPlayer.seekTo(progress);
        }
    }
}
