package com.example.ant_test.media_player;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ant_test.R;

/**
 * Created by 3020mt on 2015/11/13.
 */
public class TestAdapter extends PagerAdapter {

    ImageView[] imageViews = new ImageView[5];
    Context context;

    public TestAdapter(Context context) {
        this.context = context;
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i] = getImageView();
        }
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imageViews[position]);
        return imageViews[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViews[position]);
    }

    @Override
    public int getCount() {
        return imageViews.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    ImageView getImageView() {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setImageResource(R.drawable.ic_launcher);
        return imageView;
    }
}
