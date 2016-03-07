package com.example.ant_test.drawer;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.example.ant_test.R;

/**
 * Created by Administrator on 2015/2/26.
 */
public class Vdrawer extends FrameLayout implements View.OnClickListener{

    private static final String TAG = "V_drawer";
    private static final int PANEL_NUMBER = 4;
    private static final int DEFAULT_TRANSLATION_DURATION = 1000;

    private View mDrawer;
    private FrameLayout mPanel;
    private LayoutInflater inflater;
    //private View topPanel, rightPanel, bottomPanel, leftPanel;
    private View[] drawers = new View[PANEL_NUMBER];//top, right, bottom, left;
    private int[] drawersLayout = new int[PANEL_NUMBER];
    private int[] drawersAttr = {R.styleable.drawer_top_layout, R.styleable.drawer_right_layout,
            R.styleable.drawer_bottom_layout, R.styleable.drawer_left_layout};
    private int[] drawersLayoutGravity = new int[] {Gravity.TOP, Gravity.RIGHT, Gravity.BOTTOM, Gravity.LEFT};
    private View currentShownView = null;

    private int animationDuration = DEFAULT_TRANSLATION_DURATION;
    private Interpolator animationInterpolator = new DecelerateInterpolator();

    public void setAnimationInterpolator(Interpolator animationInterpolator) {
        this.animationInterpolator = animationInterpolator;
    }

//    private boolean translate = true;
//
//    public void setTranslate(boolean translate) {
//        this.translate = translate;
//    }

    private boolean alpha = true;

    public void setAlpha(boolean alpha) {
        this.alpha = alpha;
    }

    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
    }

    public Vdrawer(Context context) {
        super(context);
        init(context, null);
    }

    public Vdrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Vdrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflater = LayoutInflater.from(getContext());
        mPanel = createPanel();
        this.addView(mPanel, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        mPanel.setVisibility(View.INVISIBLE);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.drawer);
        for (int i = 0; i < drawersAttr.length; i++) {
            if (typedArray.hasValue(drawersAttr[i])) {
                Log.d(TAG, "find attr:" + drawersAttr[i]);
                drawersLayout[i] = typedArray.getResourceId(drawersAttr[i], 0);
                drawers[i] = inflater.inflate(drawersLayout[i], null);
                FrameLayout.LayoutParams lp = null;
                if (drawersAttr[i] == R.styleable.drawer_top_layout || drawersAttr[i] == R.styleable.drawer_bottom_layout) {
                    lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
                } else if (drawersAttr[i] == R.styleable.drawer_left_layout || drawersAttr[i] == R.styleable.drawer_right_layout) {
                    lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
                } else {
                    Log.e(TAG, "unknown attrs");
                }
                drawers[i].setVisibility(View.INVISIBLE);//TODO if uses View.GONE, it will not obtain the panel's height.
                lp.gravity = drawersLayoutGravity[i];
                mPanel.addView(drawers[i], lp);
                drawers[i].setOnClickListener(this);
                //mPanel.setOnTouchListener(this);
            }
        }
        //this.setOnClickListener(this);
    }

    private FrameLayout createPanel() {
        FrameLayout fl = new FrameLayout(getContext());
        fl.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        return fl;
    }

    public void popDown() {//从上往下
        final View panel = drawers[0];
        if (panel != null) {
            Log.d(TAG, "pop up to down");
            int fromX = 0, toX = 0, fromY = -panel.getHeight(), toY = 0;
            pop(panel, fromX, toX, fromY, toY);
        }
    }

    public void popLeft() {//从右往左
        final View panel = drawers[1];
        if (panel != null) {
            Log.d(TAG, "pop right to left");
            int panelWidth = panel.getWidth();
            int frameWidth = this.getWidth();
            int fromX = frameWidth, toX = frameWidth - panelWidth, fromY = 0, toY = 0;
            pop(panel, fromX, toX, fromY, toY);
        }
    }

    public void popUp() {//从下往上
        final View panel = drawers[2];
        if (panel != null) {
            Log.d(TAG, "pop down to up");
            int panelHeight = panel.getHeight();
            int fromX = 0, toX = 0, fromY = panelHeight, toY = 0;
            pop(panel, fromX, toX, fromY, toY);
        }
    }

    public void popRight() {//从左往右
        final View panel = drawers[3];
        if (panel != null) {
            Log.d(TAG, "pop left to right");
            int fromX = -panel.getWidth(), toX = 0, fromY = 0, toY = 0;
            pop(panel, fromX, toX, fromY, toY);
        }
    }


    /** 'from and to' is relative to their initial position if they has gravity property*/
    public void pop(View view, final int fromX, final int toX, final int fromY, final int toY) {
        hide();
        mPanel.setVisibility(View.VISIBLE);
        currentShownView = view;
        currentShownView.setVisibility(View.VISIBLE);
        AnimationSet as = makeAnimation(fromX, toX, fromY, toY);
        currentShownView.startAnimation(as);
    }

    public void hide() {
        if (currentShownView != null) {
            currentShownView.clearAnimation();
            currentShownView.setVisibility(View.INVISIBLE);
            currentShownView = null;
            mPanel.setVisibility(View.INVISIBLE);
        }
    }

    public void test() {
        mDrawer = drawers[2];
        int frameHeight = this.getHeight() <= 0 ? this.getMeasuredHeight() : getHeight();
        int panelHeight = mDrawer.getHeight() <= 0 ? mDrawer.getMeasuredHeight() : mDrawer.getHeight();
//        Log.d(TAG, "getHeight:" + getHeight() + ", mDrawer.getHeight():" + mDrawer.getHeight());
//        TranslateAnimation tr = new TranslateAnimation(0, 0, frameHeight, frameHeight - panelHeight);
//        tr.setDuration(1000);
//        tr.setFillAfter(true);
//        Log.d(TAG, "frameHeight:" + frameHeight + ", panelHeight:" + panelHeight);
//        mDrawer.startAnimation(tr);
        AnimationSet as = makeAnimation(0, 0, frameHeight, frameHeight - panelHeight);
        mDrawer.startAnimation(as);
    }

    private TranslateAnimation makeTranslateAnimation(int fromX, int toX, int fromY, int toY) {
//        if (!translate) return null;
        TranslateAnimation tr = new TranslateAnimation(fromX, toX, fromY, toY);
        return tr;
    }

    private AlphaAnimation makeAlphaAnimation() {
        if (!alpha) return null;
        AlphaAnimation aa = new AlphaAnimation(0f, 1.0f);
        return aa;
    }

    private AnimationSet makeAnimation(final int fromX, final int toX, final int fromY, final int toY) {
        AnimationSet set = new AnimationSet(true);
        TranslateAnimation ta = makeTranslateAnimation(fromX, toX, fromY, toY);
        AlphaAnimation aa = makeAlphaAnimation();
        if (ta != null) {
            set.addAnimation(ta);
        }
        if (aa != null) {
            set.addAnimation(aa);
        }
        set.setInterpolator(animationInterpolator);
        set.setDuration(animationDuration);
        set.setFillAfter(true);
        return set;
    }

    @Override
    public void onClick(View v) {
        for (View view : drawers) {
            if (v == view) {
                Log.d(TAG, "onclick drawer");
                return;
            }
        }
        if (v == mPanel) {
            hide();
            Log.d(TAG, "onclick panel");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (currentShownView == null) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent ACTION_DOWN HIDE");
                hide();
                break;

        }
        return super.onTouchEvent(event);
    }


}
