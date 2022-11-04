package com.toadstudio.first.toadproject.RecycleView;

/**
 * Created by rangkast.jeong on 2018-03-07.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.toadstudio.first.toadproject.Image.GlideApp;
import com.toadstudio.first.toadproject.R;

import java.util.List;

public class FullMetalAdapter extends MetalRecyclerViewPager.MetalAdapter<FullMetalAdapter.FullMetalViewHolder> {

    private static List<String> metalList;
    private static List<String> metalImageList;
    Context context;

    public static String TAG = "Toad_FullMetalAdapter";

    public FullMetalAdapter(Context context, @NonNull DisplayMetrics metrics, @NonNull List<String> metalList, List<String> metalImageList) {
        super(metrics);
        this.metalList = metalList;
        this.metalImageList = metalImageList;
        this.context = context;
    }

    @Override
    public FullMetalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pager_item, parent, false);
        return new FullMetalViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(FullMetalViewHolder holder, int position) {
        // don't forget about calling supper.onBindViewHolder!
        super.onBindViewHolder(holder, position);

    //    ViewTask task = new ViewTask(context, position, holder);
    //    task.execute(context, position, holder);



//        Log.d(TAG, "pos: " + position + " text: " + metalList.get(position) + " data: " + metalImageList.get(position));
//        holder.metalText.setText(metalList.get(position));

        try{
            /*
            if (holder.metalImage != null) {
                ((BitmapDrawable)holder.metalImage.getDrawable()).getBitmap().recycle();
            }
            holder.metalImage.setImageURI(Uri.fromFile(new File(metalImageList.get(position))));
            */

            GlideApp
                    .with(context)
                    .load(metalImageList.get(position))
                    .fitCenter()
                    .centerCrop()
                    //      .skipMemoryCache(true)
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.black_color)
                    .into(holder.metalImage);

            Thread.sleep(100);
        } catch(Exception e) {

        } finally {
            Log.d(TAG, "loaded");
        }

    }

    @Override
    public int getItemCount() {
        return metalList.size();
    }

    static class FullMetalViewHolder extends MetalRecyclerViewPager.MetalViewHolder {

  //      TextView metalText;
        ImageView metalImage;

        public FullMetalViewHolder(View itemView) {
            super(itemView);
  //          metalText = (TextView) itemView.findViewById(R.id.metal_text_1);
            metalImage = (ImageView) itemView.findViewById(R.id.metal_image);
        }
    }

    private static class ViewTask extends AsyncTask {
        private int pos;
        FullMetalViewHolder mHolder;
        private Context mContext;

        public ViewTask(Context context, int position, FullMetalViewHolder holder) {
            pos = position;
            mHolder = holder;
            mContext = context;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            Log.d(TAG, "pos: " + pos + " text: " + metalList.get(pos) + " data: " + metalImageList.get(pos));
     //      mHolder.metalText.setText(metalList.get(pos));

            GlideApp
                    .with(mContext)
                    .asBitmap()
                    .load(metalImageList.get(pos))
                    .centerCrop()
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.black_color)
                    .into(new BitmapImageViewTarget(mHolder.metalImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            //Play with bitmap
                            super.setResource(resource);
                        }
                    });

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }
}
