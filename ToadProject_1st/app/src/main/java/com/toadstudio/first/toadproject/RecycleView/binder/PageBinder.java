package com.toadstudio.first.toadproject.RecycleView.binder;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.toadstudio.first.toadproject.Cam.Camera2BasicFragment;
import com.toadstudio.first.toadproject.Cam.CameraActivity;
import com.toadstudio.first.toadproject.FileController;
import com.toadstudio.first.toadproject.Image.ImagesView;
import com.toadstudio.first.toadproject.MainActivity;
import com.toadstudio.first.toadproject.R;
import com.toadstudio.first.toadproject.RecycleView.AspectRatioImageView;
import com.toadstudio.first.toadproject.RecycleView.DemoViewType;
import com.toadstudio.first.toadproject.SQLiteDB.MemoDBHelper;

import java.util.ArrayList;
import java.util.List;

import jp.satorufujiwara.binder.recycler.RecyclerBinder;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class PageBinder extends RecyclerBinder<DemoViewType> {
  public static String TAG = "ToadPrj_PageBinder";
  private final String mResId;
  private Context context;
  private ViewHolder holder;
  private  static Vibrator vibrator;
  private MemoDBHelper memoDBHelper = null;
  private SQLiteDatabase db = null;
  private ImagesView imagesView;
  private Camera2BasicFragment camera2BasicFragment;
  private CameraActivity cameraActivity;
  private String path = null;
  private MainActivity mainActivity;
  static List<Integer> selectedPositions = null;
  private FileController fileController;

  public PageBinder(Activity activity, String resId) {
    super(activity, DemoViewType.PAGE);
    mResId = resId;
    memoDBHelper = new MemoDBHelper(getContext());

    imagesView = new ImagesView();
    camera2BasicFragment = new Camera2BasicFragment();
    cameraActivity = new CameraActivity();
    mainActivity = new MainActivity();
    fileController = new FileController();

    selectedPositions = new ArrayList<>();
    String[] back_folder = mResId.split("/");
    StringBuilder backward_fpath = new StringBuilder();

    for (int i = 0; i < back_folder.length - 1; i++) {
      if (i != 0)
        backward_fpath.append("/");
      backward_fpath.append(back_folder[i]);
    }
//    Log.d(TAG, "upper layer " + backward_fpath.toString());

    this.path = backward_fpath.toString();
  }

  //아이템 클릭시 실행 함수
  private ItemClick itemClick;
  public interface ItemClick {
    void onClick(View view,int position);
  }

  //아이템 클릭시 실행 함수 등록 함수
  public void setItemClick(ItemClick itemClick) {
    this.itemClick = itemClick;
  }

  @Override
  public int layoutResId() {
    return R.layout.row_page;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(View v) {
    return new ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
    final int Position = position;
    context = getContext();
    holder = (ViewHolder) viewHolder;
    Cursor c = null;

 //   holder.mImageView.setImageResource(mResId);
    try {
      if (camera2BasicFragment.check_cam_status() == false) {

        try {

          String new_id = mainActivity.memoID_translation(path, imagesView.check_metal_list(position));
          if (new_id != null) {
            db = memoDBHelper.getWritableDatabase();
            c = db.query("memo", new String[]{"id", "info"}, "id='" + new_id + "'", null, null, null, null);
            if (c.getCount() != 0)
              holder.textView.setImageResource(R.drawable.pen);
            else
              holder.textView.setImageResource(R.color.transparent);
            c.close();
            db.close();
          }
        } catch (Exception e) {
          e.printStackTrace();
          c.close();
          db.close();
        }

        if (fileController.file_format_check(imagesView.check_metal_list(position)) == 2) {
          holder.textView_0.setTextColor(Color.WHITE);
          holder.textView_0.setText("mov");
        } else {
          holder.textView_0.setText("");
        }
      }

        int selectedIndex = selectedPositions.indexOf(Position);
   //     Log.d(TAG, "data: " + mResId + " pos: " + position + " selected:" + selectedIndex);
        if (selectedIndex > -1) {
          draw_data(true, mResId);
        } else {
          draw_data(false, mResId);
        }

    } catch (Exception e) {
      e.printStackTrace();
    }
      holder.mImageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          int ret = 0;
       //   Log.d(TAG, "position: " + Position);
          try {
            ret = imagesView.recycler_item_click(Position, 8); //recycler_view

            if (ret == 1) {
       //         Log.d(TAG, "try cam path for recyclerview");
              try {
                String cam_path = camera2BasicFragment.check_cam_focus_path();
       //         Log.d(TAG, "recyclerPath(Camera): " + cam_path);
                if (cam_path != null) {
                  cameraActivity.show_recycle_view_by_cam(cam_path, Position);
                } else {
         //         Log.d(TAG, "file path is null");
                }
              } catch (Exception ex) {
                ex.printStackTrace();
              }
            } else {
        //      Log.d(TAG, "path not found error");
            }

          } catch (Exception e) {
            e.printStackTrace();
          }
            /*
            if(itemClick != null){
               itemClick.onClick(v, Position);
            }
            */
        }
      });

      holder.mImageView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
          vibrator.vibrate(100);

          try {
            int selectedIndex = selectedPositions.indexOf(Position);
            if (selectedIndex > -1) {
              selectedPositions.remove(selectedIndex);
              draw_data(false, mResId);

            } else {
              selectedPositions.add(Position);
              draw_data(true, mResId);
            }

    //        Log.d(TAG, "mResID: " + mResId + " selectedIndex: " + selectedIndex + " pos: " + Position);

            imagesView.set_metal_list(selectedPositions);

          } catch (Exception e) {
            e.printStackTrace();
          }

          return true;
        }
      });
  }

  private static final class ViewHolder extends RecyclerView.ViewHolder {
    View view;
    private final AspectRatioImageView mImageView;
    private final ImageView textView;
    private final TextView textView_0;
    private final TextView textView_check;

    public ViewHolder(View view) {
      super(view);
      this.view = view;
      mImageView = (AspectRatioImageView) view.findViewById(R.id.img_page);
      textView = (ImageView)view.findViewById(R.id.textview_row_page);
      textView_0 = (TextView)view.findViewById(R.id.textview_row_page_0);
      textView_check = (TextView)view.findViewById(R.id.textview_check);
    }
  }

  private void draw_data(Boolean status, String data) {
    Glide.get(context).clearMemory();
    if (status) {
          holder.textView_check.setText("V");
          Glide   .with(context)
                  .load(data)
                  .apply(RequestOptions.bitmapTransform(new MultiTransformation<>(new BlurTransformation(), new CenterCrop()))
                          .priority(Priority.HIGH))
                  .into(holder.mImageView);
    } else {
          holder.textView_check.setText("");
          Glide
                  .with(context)
                  .load(data)
                  .into(holder.mImageView);
    }

  }

}
