package com.toadstudio.first.toadproject.RecycleView;

/**
 * Created by rangkast.jeong on 2018-03-07.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.toadstudio.first.toadproject.R;

public class MetalRecyclerViewPager extends RecyclerView {

    private int itemMargin;
    public static String TAG = "Toad_MetalRecycler";

    public MetalRecyclerViewPager(Context context) {
        super(context);
        init(context, null);
    }

    public MetalRecyclerViewPager(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MetalRecyclerViewPager(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MetalRecyclerViewPager, 0, 0);
            itemMargin = (int) typedArray.getDimension(R.styleable.MetalRecyclerViewPager_itemMargin, 0f);
            typedArray.recycle();
            Log.d(TAG, "itemMargin " + itemMargin);
        }

        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(this);
    }

    public void setAdapter(Adapter adapter) {
        if (MetalAdapter.class.isInstance(adapter)) {
            MetalAdapter metalAdapter = (MetalAdapter) adapter;
            metalAdapter.setItemMargin(itemMargin);
            metalAdapter.updateDisplayMetrics();
        } else {
            throw new IllegalArgumentException("Only MetalAdapter is allowed here");
        }
        super.setAdapter(adapter);
    }

    public static abstract class MetalAdapter<VH extends MetalViewHolder> extends RecyclerView.Adapter<VH> {

        private DisplayMetrics metrics;
        private int itemMargin;
        private int itemWidth;

        public MetalAdapter(@NonNull DisplayMetrics metrics) {
            this.metrics = metrics;
        }

        void setItemMargin(int itemMargin) {
            this.itemMargin = itemMargin;
        }

        void updateDisplayMetrics() {
            itemWidth = metrics.widthPixels - itemMargin * 2;
        }

        @Override
        public void onBindViewHolder(VH holder, final int position) {
            int currentItemWidth = itemWidth;

            if (position == 0) {
                currentItemWidth += itemMargin;
                holder.rootLayout.setPadding(itemMargin, 0, 0, 0);
            } else if (position == getItemCount() - 1) {
                currentItemWidth += itemMargin;
                holder.rootLayout.setPadding(0, 0, itemMargin, 0);
            }

            int height = holder.rootLayout.getLayoutParams().height;
            holder.rootLayout.setLayoutParams(new ViewGroup.LayoutParams(currentItemWidth, height));
/*
            holder.button_1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: " + position);
                }
            });

            holder.button_2.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: " + position);
                }
            });
            */
        }
    }

    public static abstract class MetalViewHolder extends RecyclerView.ViewHolder {

        ViewGroup rootLayout;
  //      TextView button_1;
  //      TextView button_2;

        public MetalViewHolder(View itemView) {
            super(itemView);
            rootLayout = (ViewGroup) itemView.findViewById(R.id.root_layout);
    //        this.button_1 = (TextView) itemView.findViewById(R.id.metal_text_btn);
    //        this.button_2 = (TextView) itemView.findViewById(R.id.metal_text_btn2);
        }
    }
}
