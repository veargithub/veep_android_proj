package com.example.ant_test.keyboard_height;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.ant_test.R;

/**
 * Created by 3020mt on 2016/6/3.
 */
public class KeyBoardHeightActivity extends Activity implements View.OnClickListener {

    LinearLayout ll1;
    Window mRootWindow;
    View mRootView;
    int originHeight;
    int currentHeight;
    TextView layer1;
    TranslateAnimation tr;
    AlphaAnimation aa;
    AnimationSet as;
    int currentKeyBoardHeight;
    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_one_edittext);
        layer1 = (TextView) findViewById(R.id.layer1);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv1.setOnClickListener(this);
        tv1.setText("fix444");
        mRootWindow = getWindow();
        mRootView = mRootWindow.getDecorView().findViewById(android.R.id.content);
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        View view = mRootWindow.getDecorView();
                        view.getWindowVisibleDisplayFrame(r);
                        if (originHeight == 0) {
                            originHeight = r.bottom;
                        }
                        currentHeight = r.bottom;
                        currentKeyBoardHeight = originHeight - currentHeight;
                        if (currentKeyBoardHeight > 0) {//键盘显示了
                            if (layer1.getVisibility() != View.VISIBLE) {
                                layer1.setVisibility(View.VISIBLE);
                                setLayerMarginBottom(0);
                                if (hasBrotherScrollView(layer1)) {
                                    tr = new TranslateAnimation(0, 0, layer1.getHeight(), 0);
                                } else {
                                    tr = new TranslateAnimation(0, 0, 0, -layer1.getHeight());
                                }

                                aa = new AlphaAnimation(0, 100);
                                as = new AnimationSet(true);
                                as.addAnimation(aa);
                                as.addAnimation(tr);

                                //tr = new TranslateAnimation(0, 0, 0, currentKeyBoardHeight);
                                as.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        layer1.clearAnimation();
                                        if (hasBrotherScrollView(layer1)) {
                                            if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN) {
                                                setLayerMarginBottom(currentKeyBoardHeight);
                                            } else {
                                                setLayerMarginBottom(0);
                                            }
                                        } else {
                                            setLayerMarginBottom(currentKeyBoardHeight);
                                        }
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                                as.setDuration(500);

                                //as.setFillAfter(true);
                                layer1.startAnimation(as);
                            }
                        } else {
                            if (as != null && !as.hasEnded()) {
                                as.cancel();
                            }
                            layer1.setVisibility(View.INVISIBLE);
                        }
                        Log.d(">>>>", r.left + " " + r.top + " " + r.right + " " + r.bottom);
                        // r.left, r.top, r.right, r.bottom
                    }
                });
    }

    private boolean hasBrotherScrollView(View view) {
        if (view == null) return false;
        ViewParent parent = view.getParent();
        if (parent == null) return false;
        ViewGroup viewGroup = (ViewGroup) parent;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof ScrollView) {
                Log.d(">>>>", "hasBrotherScrollView true");
                return true;
            }
        }
        Log.d(">>>>", "hasBrotherScrollView false");
        return false;
    }

    private void setLayerMarginBottom(int bottom) {
        if (layer1.getLayoutParams() instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) layer1.getLayoutParams();
            lp.setMargins(0, 0, 0, bottom);
            layer1.setLayoutParams(lp);
        } else if (layer1.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) layer1.getLayoutParams();
            lp.setMargins(0, 0, 0, bottom);
            layer1.setLayoutParams(lp);
        }
        //lp.setMargins(0, 0, 0, currentKeyBoardHeight - layer1.getHeight());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv1:
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                //if (!imm.isActive()) {//如果未开启，则开启软键盘
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                //}
            break;
        }
    }
}
