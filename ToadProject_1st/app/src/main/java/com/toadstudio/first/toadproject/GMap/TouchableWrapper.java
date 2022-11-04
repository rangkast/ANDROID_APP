package com.toadstudio.first.toadproject.GMap;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.toadstudio.first.toadproject.MainActivity;

public class TouchableWrapper extends FrameLayout {
    private static final String TAG = "ToadPrj_Touchable";

    public TouchableWrapper(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                MainActivity.mMapIsTouched = true;
   //             Log.d(TAG, "X:" + Float.toString(event.getX()) + " Y:" + Float.toString(event.getY()));
                break;

            case MotionEvent.ACTION_UP:
                MainActivity.mMapIsTouched = false;
     //           Log.d(TAG, "X:" + Float.toString(event.getX()) + " Y:" + Float.toString(event.getY()));
                break;

            case MotionEvent.ACTION_MOVE:
     //           Log.d(TAG, "X:" + Float.toString(event.getX()) + " Y:" + Float.toString(event.getY()));
                break;
        }

        MainActivity.onDragTouch(event);
        return super.dispatchTouchEvent(event);
    }
}
