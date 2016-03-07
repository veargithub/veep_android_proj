package com.example.ant_test.picshift;

import com.example.ant_test.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

public class PicShiftActivity extends Activity{
	private ImageView[] ivs;
	private AlphaAnimation fadeInAnimation;
	private AlphaAnimation fadeOutAnimation;
	private AnimationSet as1;
	private AnimationSet as2;
	private int[] pictureList = new int[] {R.drawable.ps1, R.drawable.ps2, R.drawable.ps3/*, R.drawable.ps4, R.drawable.ps5, R.drawable.ps6*/};
	private int[] ivids = new int[] {R.id.iv1, R.id.iv2, R.id.iv3/*, R.id.iv4, R.id.iv5, R.id.iv6*/};
//	private int fadeInIndex = 1;
//	private int fadeOutIndex = 0;
	private int fadeIndex = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picshift);
		ivs = new ImageView[pictureList.length];
		for (int i = 0; i < pictureList.length; i++) {
			ivs[i] = (ImageView) findViewById(ivids[i]);
			ivs[i].setBackgroundResource(pictureList[i]);
			ivs[i].setVisibility(View.INVISIBLE);
		}
		
		fadeOutAnimation = new AlphaAnimation(1, 0);
		fadeOutAnimation.setDuration(5000);
		fadeOutAnimation.setAnimationListener(fadeOutListener);
		
		fadeInAnimation = new AlphaAnimation(0, 1);
		fadeInAnimation.setDuration(5000);
		fadeInAnimation.setAnimationListener(fadeInListener);
		
		ivs[0].setAnimation(fadeOutAnimation);
		ivs[1].setAnimation(fadeInAnimation);
	}
	
	private AnimationListener fadeInListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
			
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			
		}

		@Override
		public void onAnimationRepeat(Animation animation) {}
		
	};
	
	private AnimationListener fadeOutListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
		} 

		@Override
		public void onAnimationEnd(Animation animation) {
			if (fadeIndex >= pictureList.length - 2) {
				//TODO
				return;
			}
			int index = fadeIndex;
			ivs[index].setVisibility(View.INVISIBLE);
			index++;
			if (index >= pictureList.length) index = 0;
			ivs[index].startAnimation(fadeOutAnimation);
			index++;
			if (index >= pictureList.length) index = 0;
			ivs[index].setVisibility(View.VISIBLE);
			ivs[index].startAnimation(fadeInAnimation);
			fadeIndex++;
			//if (fadeIndex >= pictureList.length) fadeIndex = 0;
		}

		@Override
		public void onAnimationRepeat(Animation animation) {}
		
	};
	
}
