package com.toadstudio.first.toadproject.RecycleView.binder;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.toadstudio.first.toadproject.R;
import com.toadstudio.first.toadproject.RecycleView.DemoViewType;

import jp.satorufujiwara.binder.recycler.RecyclerBinder;

//todo
public class TitleBinder extends RecyclerBinder<DemoViewType> {

  private final String mTitle;

  public TitleBinder(Activity activity, String title) {
    super(activity, DemoViewType.TITLE);
    mTitle = title;
  }

  @Override public int layoutResId() {
    return R.layout.row_title;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(View v) {
    return new ViewHolder(v);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
    ViewHolder holder = (ViewHolder) viewHolder;
    holder.mTextView.setText(mTitle);
  }

  private static final class ViewHolder extends RecyclerView.ViewHolder {

    private final TextView mTextView;

    public ViewHolder(View itemView) {
      super(itemView);
      mTextView = (TextView) itemView.findViewById(R.id.txt_row_title);
    }
  }
}
