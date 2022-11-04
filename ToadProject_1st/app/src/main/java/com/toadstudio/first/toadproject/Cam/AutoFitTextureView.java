package com.toadstudio.first.toadproject.Cam;

/**
 * Created by rangkast.jeong on 2018-02-25.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.View;

/**
 * A {@link TextureView} that can be adjusted to a specified aspect ratio.
 */
public class AutoFitTextureView extends TextureView {

    private int mRatioWidth = 0;
    private int mRatioHeight = 0;
    public static String TAG = "ToadPrj_AutoFitTex";
    public AutoFitTextureView(Context context) {
        this(context, null);
    }

    public AutoFitTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoFitTextureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Sets the aspect ratio for this view. The size of the view will be measured based on the ratio
     * calculated from the parameters. Note that the actual sizes of parameters don't matter, that
     * is, calling setAspectRatio(2, 3) and setAspectRatio(4, 6) make the same result.
     *
     * @param width  Relative horizontal size
     * @param height Relative vertical size
     */
    public void setAspectRatio(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);
     //   Log.d(TAG, "onMesure: (W " + width + "/ H " + height + ")");
     //   Log.d(TAG, "mRatioWidth: " + mRatioWidth + " mRatioHeight: " + mRatioHeight);
        /*
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        } else {
        */

            //To Do
            //이 부분이 preview 를 resize함
            /*
            if (width < height * mRatioWidth / mRatioHeight) {
                Log.d(TAG, "width: " + width + " height: " + width * mRatioHeight / mRatioWidth);
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else {
                Log.d(TAG, "width: " + height * mRatioWidth / mRatioHeight + " height: " + height);
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
            */
            setMeasuredDimension(width, width *  4 / 3); // W : H = 2 : 3
     //   }
    }

}
