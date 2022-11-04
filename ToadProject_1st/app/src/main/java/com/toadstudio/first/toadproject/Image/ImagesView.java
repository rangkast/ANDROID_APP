package com.toadstudio.first.toadproject.Image;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.FileProvider;
import android.support.v4.os.EnvironmentCompat;
import android.support.v4.provider.DocumentFile;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.toadstudio.first.toadproject.Cam.Camera2BasicFragment;
import com.toadstudio.first.toadproject.CustomDialog.CustomProgressDialog;
import com.toadstudio.first.toadproject.FileController;
import com.toadstudio.first.toadproject.GMap.MapInfo;
import com.toadstudio.first.toadproject.GMap.SettingInfo;
import com.toadstudio.first.toadproject.ListView.ListviewAdapter;
import com.toadstudio.first.toadproject.ListView.Listviewitem;
import com.toadstudio.first.toadproject.MainActivity;
import com.toadstudio.first.toadproject.R;
//import com.example.rangkastjeong.toadproject.RecycleView.FullMetalAdapter;
//import com.example.rangkastjeong.toadproject.RecycleView.MetalRecyclerViewPager;
import com.toadstudio.first.toadproject.RecycleView.AspectRatioImageView;
import com.toadstudio.first.toadproject.RecycleView.DemoSectionType;
import com.toadstudio.first.toadproject.RecycleView.DemoViewType;
import com.toadstudio.first.toadproject.RecycleView.binder.PageBinder;
import com.toadstudio.first.toadproject.SQLiteDB.MemoDBHelper;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.magiepooh.recycleritemdecoration.ItemDecorations;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.toadstudio.first.toadproject.Settings.SettingsActivity;

import java.io.File;
import java.io.InputStream;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import jp.satorufujiwara.binder.recycler.RecyclerBinder;
import jp.satorufujiwara.binder.recycler.RecyclerBinderAdapter;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by rangkast.jeong on 2018-03-02.
 */

public class ImagesView extends FragmentActivity implements AdapterView.OnItemClickListener, OnClickListener, View.OnTouchListener {

    static String Appdir = "/여행을찍다";

    private static AdView mAdView;
    private static HashMap<Integer, Integer> map = new HashMap<>();

    public final static int COUNT =2; // 표시할 페이지 수
    private ViewPager mPager; //뷰 페이저
    private static CustomView customView;
    public static String TAG = "ToadPrj_ImageView";
    private static String basePath;
    private static String recyclerPath;
    private static String recyclerBasePath;
    private static String sdcard_path_header = null;

    private static int focus_storage;
    private static int main_page_storage;
    private static int recycler_storage;
    private static int  to_recycler_storage = -1;
    private static int  from_recycler_storage = -1;
    static Activity activity;

    static String img[] = {};
    static List<String> mImg = null;
    static List<Integer> selectedPositions = null;
    static List<Integer> selectedPositions_metal = null;

    //listview items for layout2
    static ArrayList<Listviewitem> data;
    static ArrayList<MapInfo> mapInfos;
    static ArrayList<SettingInfo> mSettingInfo;

    static ArrayList<String> path_trace = null;

    private ArrayList<Integer> custom_marker;
    private static ListView listView;
    private static ListviewAdapter listadapter;

    static String file_path = null;

    static int display_width = 0;
    static int display_height = 0;

    static boolean asyncRunning = false;
    static boolean loading_done = false;

    private static GridView mGridView;
    private static ImageView iv;
    private static MyAdapter adapter; //grid adapter
    private static RecyclerView mRecyclerView;
 //   private static FullMetalAdapter fullMetalAdapter;
 //   private static MetalRecyclerViewPager metalviewPager;

    private static List<String> metalImageList;
    private static List<String> metalList;

    private static CheckTh mCheckTh;
    static boolean mCheckThreadRunning = false;

    private static TextView textView_title;
    private static TextView textView_title_2;
    private static TextView textView_list;
    private static TextView textView_list_2;
    private static LinearLayout mlayout;
    private static LinearLayout mlayout_2;

    static int count = 0;
    static int th_count = 0;
    static int kill_thread_call = 0;

    private static int id_from_main = 0;
    private static int delete_enable = 0;
    private static String f_path;
    private static String from = null;

    private  static Vibrator vibrator;

    private  final int KILL_ACTIVITY = 0;
    private  final int UPDATE_VIEW = 1;
    private  final int CLEAR_SELECTED_ITEM = 2;
    private  final int UPDATE_RECYCLER = 3;
    private  final int CTRL_MEMODB = 4;
    private  final int GALLERY_GET = 5;
    private  final int PROGRESS = 6;
    private  final int GET_URI_FROM_PATH = 7;
    private final int SEND_EXTERNAL_STORAGE = 8;

    private final static int SINGLE_FILE_CHOICE_CTRL = 0;
    private final static int PROGRESS_ACTIVITY = 1;
    private final static int MULTI_FILE_DELETE_CTRL = 2;
    private final static int SINGLE_FILE_SHARE_CTRL = 3;
    private final static int MULTI_FILE_SHARE_CTRL = 4;
    private final static int CLEAR_ITEM_CTRL = 5;
    private final static int MULTI_FILE_MOVE_CTRL = 6;
    private final static int UPDATE_CTRL = 7;
    private final static int RECYCLER_VIEW = 8;
    private final static int SET_ADS = 9;
    private final static int MULTI_FILE_MOVE_CTRL_RECYCLER = 10;
    private final static int MULTI_FILE_COPY_CTRL_RECYCLER = 11;
    private final static int UPDATE_CTRL_RECYCLER = 12;
    private final static int MULTI_FILE_COPY_CTRL = 13;



    private final int UPDATE_ID = 0;
    private final int EXTERNAL_MOVE = 0;
    private final int EXTERNAL_COPY = 1;
    private final int EXTERNAL_MOVE_RECYCLER = 2;
    private final int EXTERNAL_COPY_RECYCLER = 3;
    private final int EXTERNAL_DELETE = 4;
    private final int EXTERNAL_GALLERY_MOVE = 5;
    private final int EXTERNAL_DELETE_RECYCLER = 6;


    private  final int START = 0;
    private final int STOP = 1;
    private static int multi_mode = 0;

    private static int item_pos = -1;

    private static int GALLERY_CODE = 1112;

    final static int REQUSET_GALLERY = 1;
    final static int REQUSET_FILE_PATH = 2;

    private static FloatingActionsMenu menuMultipleActions;
    private static FloatingActionButton actionA;
    private static FloatingActionButton actionB;
    private static FloatingActionButton actionC;
    private static FloatingActionButton actionD;

    private static ArrayList<Uri> imageList;
    private static ArrayList<String> imageListPath;

    private static ArrayList<String> imageName;

    private static int single_item_id = -1;

    private static CustomProgressDialog customProgressDialog;

    public static final int INTERNAL_STORAGE = 0;
    public static final int EXTERNAL_STORAGE = 1;

    public static final int BOTH_STORAGE = 2;

    private String to_external_img_path = null;

    private static AdapterView.OnItemClickListener mItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView adapterView, View view, int i, long l) {
    //        Log.d(TAG, "item click: " +i);
            if (multi_mode == 0) {
                try {

                    if (single_item_id == i) {
      //                  Log.d(TAG, "same id clicked.");
                        return;
                    }
                    single_item_id = i;

                    ImagesView run = new ImagesView();
                    run.run_activiy(activity, i, SINGLE_FILE_CHOICE_CTRL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                int selectedIndex = selectedPositions.indexOf(i);
                if (selectedIndex > -1) {
                    selectedPositions.remove(selectedIndex);
                    ((CustomView)view).display(false, i);
                } else {

                    if (mImg.get(i).toString().contains(".")) {
                        selectedPositions.add(i);
                        ((CustomView) view).display(true, i);
                    }
                }
            }
        }
    };

    private static AdapterView.OnItemLongClickListener mItemLongClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
            vibrator.vibrate(100);
            int count = 0;

            if (multi_mode == 1) {
                StringBuilder str = new StringBuilder("");
                for (int cnt = 0; cnt < selectedPositions.size(); cnt++) {
                    //           Log.d(TAG, "selected item:" + adapter.selectedPositions.get(cnt));
                    //            str.append(mImg.get(adapter.selectedPositions.get(cnt)) + "\n");
                    count++;
                }
                str.append("\n" + count + "장을 선택 하였습니다.");
                alertDialog.setTitle(str.toString());
                alertDialog.setIcon(R.drawable.toad_app_icon_v4);
            } else {
                //      if (multi_mode == 0) {
                alertDialog.setTitle(mImg.get(i));
                alertDialog.setIcon(R.drawable.toad_app_icon_v4);
            }


            if (!mImg.get(i).toString().contains(".")) {
       //         Toast.makeText(activity, "지원하지 않습니다.", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialog_delete = new AlertDialog.Builder(activity);

                // Setting Positive "Yes" Button
                alertDialog_delete.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //               Log.d(TAG, "item click: " + i);

                        if (focus_storage == INTERNAL_STORAGE) {
                            try {
                                File mFile = new File(basePath + File.separator + mImg.get(i));
                                mFile.delete();
                                mImg.remove(i);
                                ImagesView run = new ImagesView();
                                run.run_activiy(activity, i, UPDATE_CTRL);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                fileController.file_controller(mainActivity.return_mainActivity_context(), mainActivity.get_main_path(),Appdir + File.separator + f_path, null, mImg.get(i), fileController.DELETE_FILE);

                                mImg.remove(i);
                                ImagesView run = new ImagesView();
                                run.run_activiy(activity, i, UPDATE_CTRL);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        // Write your code here to invoke YES event
               //      Toast.makeText(activity, "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                alertDialog_delete.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        //      Toast.makeText(activity, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                alertDialog_delete.show();

                return true;
            }

                if (delete_enable == 1) {
                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
             //               Log.d(TAG, "item click: " + i);
                            try {
                                ImagesView run = new ImagesView();
                                run.run_activiy(activity, i, MULTI_FILE_DELETE_CTRL);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            // Write your code here to invoke YES event
                    //        Toast.makeText(activity, "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });
                }
                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                  //      Toast.makeText(activity, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                alertDialog.setNeutralButton("공유", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            try {
                                ImagesView run = new ImagesView();
                                if (selectedPositions.size() > 1) {
              //                      Log.d(TAG, "여러장 공유");

                                    for(int cnt = 0; cnt < selectedPositions.size(); cnt++) {
                                        if (fileController.file_format_check(mImg.get(selectedPositions.get(cnt))) == 2) {
                                            Toast.makeText(activity, "여러장 보내기는 사진만 가능 합니다.", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                    run.run_activiy(activity, i, MULTI_FILE_SHARE_CTRL);
                                } else {
               //                     Log.d(TAG, "한장 공유");
                                    run.run_activiy(activity, i, SINGLE_FILE_SHARE_CTRL);
                                 //   run.run_activiy(activity, i, SEND_EXTERNAL_STORAGE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dialog.cancel();
                    }
                });

        //    }
            /*else {
                StringBuilder str = new StringBuilder("");
                int count = 0;
                for(int cnt = 0; cnt < adapter.selectedPositions.size(); cnt++) {
                        Log.d(TAG, "selected item:" + adapter.selectedPositions.get(cnt));
                        str.append(mImg.get(adapter.selectedPositions.get(cnt)) + "\n");
                        count++;
                }

                str.append("\n" + count + "장을 공유 하시겠습니까?");
                alertDialog.setMessage(str.toString());

                alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        Toast.makeText(activity, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                alertDialog.setNeutralButton("공유", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            ImagesView run = new ImagesView();
                            run.run_activiy(activity, i, MULTI_FILE_SHARE_CTRL);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        dialog.cancel();
                    }
                });

            }
*/
                // Showing Alert Message

            if (multi_mode == 1 && count == 0) {
                Toast.makeText(activity,"사진을 선택 하세요", Toast.LENGTH_SHORT).show();
            } else {
                alertDialog.show();
            }

            return true;
        }
    };



    static String folder_path = null;
    static int folder_depth = 0;
    static int focus_view_pager_intent_status = 0;

    //static 에서 non-static func 호출 방법
     public int run_activiy(Context context, final int id, int control) {
     //    Log.d(TAG, "run_activity: " + id + " control: " + control);
         int ret = 0;

         if (prev_id != -1)
             prev_id = -1;

         try {
             if (control != RECYCLER_VIEW) {
                 if (control != MULTI_FILE_MOVE_CTRL &&
                         control != MULTI_FILE_COPY_CTRL &&
                 control != MULTI_FILE_MOVE_CTRL_RECYCLER &&
                 control != MULTI_FILE_COPY_CTRL_RECYCLER) {
                     if (mImg.size() != 0) {
                         if (focus_storage == INTERNAL_STORAGE)
                             file_path = basePath + File.separator + mImg.get(id);
                         else
                             file_path = sdcard_path_header + Appdir + File.separator + f_path + File.separator + mImg.get(id);
                     }
                 }
             }
 //            Log.d(TAG, "check path" + file_path);
             switch (control) {
                 case SINGLE_FILE_CHOICE_CTRL:
                     if (basePath != null) {

                         if (mImg.get(id).contains(".")) {

                             List<String> r_mImg = null;
                             r_mImg = new ArrayList();

                             int cnt = 0;
                             for (int i = 0; i < mImg.size(); i++) {
                                 if (fileController.file_format_check(mImg.get(i)) > 0)
                                     r_mImg.add(mImg.get(i));
                                 else
                                     cnt++;
                             }

                             String[] new_img = r_mImg.toArray(new String[r_mImg.size()]);
                             focus_view_pager_intent_status = 1;
                             selectedPositions_metal.clear();
                             Intent intent_focus_image = new Intent(context, FocusViewPager.class);
                             if (focus_storage == INTERNAL_STORAGE)
                                intent_focus_image.putExtra("focus_path", basePath);
                             else
                                 intent_focus_image.putExtra("focus_path", sdcard_path_header + Appdir + File.separator + f_path);
                             intent_focus_image.putExtra("imgs", new_img);
                             intent_focus_image.putExtra("id", id - cnt);
                             intent_focus_image.putExtra("focus_storage", focus_storage);
                             if (new_img.length != 0)
                                context.startActivity(intent_focus_image); //context.startActivity 해야함...............ㄷㄷㄷ
                         } else {
                             //ToDo
                             File underFolderDir;
                             folder_path = f_path;

                             StringBuilder path = new StringBuilder(folder_path);
                             path.append("/");
                             path.append(mImg.get(id));

                             folder_path = path.toString();

                             String local_path = null;
                             local_path = mImg.get(id);

                             if (focus_storage == INTERNAL_STORAGE) {
                                 underFolderDir = new File(Environment.getExternalStorageDirectory() + Appdir, folder_path);
                             } else {
                                 underFolderDir = new File(sdcard_path_header + Appdir, folder_path);
                             }
                             if (check_imgs(underFolderDir) < 0) {
               //                  Log.d(TAG, "no images in underFolder");
                                 mImg.clear();
                                 //        break;
                             }


                             //check img 다음에 해야함?????
                             if (focus_storage == INTERNAL_STORAGE)
                                file_path = basePath + File.separator + local_path;
                             else
                                 file_path = sdcard_path_header + Appdir + File.separator + folder_path + File.separator + local_path;
                             mediaStorageDir = underFolderDir;

            //                 Log.d(TAG, "underFolderDir" + underFolderDir.toString());
                             textView_title.setText(folder_path);
                             folder_depth++;

                             single_item_id = -1;

                             f_path = folder_path;
                             path_trace.add(path_trace.size(), f_path);

                             mHandler.sendEmptyMessageDelayed(UPDATE_VIEW, 0);
                             mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                     UPDATE_RECYCLER,
                                     UPDATE_ID, id), 10);
                         }

                     } else {
            //             Log.d(TAG, "file path is null");
                     }
                     break;
                 case PROGRESS_ACTIVITY:

                     break;
                 case MULTI_FILE_DELETE_CTRL:
                     if (multi_mode == 1) {
                         SendTask task = new SendTask(context, file_path, 2);
                         task.execute();
                     } else {
                         if (activity != null) {

                                 int posFrom = -1;
                                 for (int i = 0; i < mapInfos.size(); i++) {
                                     if (f_path.equals(mapInfos.get(i).getfolder())) {
                                         posFrom = i;
                                         //                     Log.d(TAG, "mapinfo posFrom: " + posFrom);
                                     }
                                 }

                                 if (focus_storage == INTERNAL_STORAGE) {
                                     File mFile = new File(file_path);
                                     mFile.delete();
                                 } else {
                                     fileController.file_controller(mainActivity.return_mainActivity_context(), mainActivity.get_main_path(),Appdir + File.separator + f_path, null, mImg.get(id), fileController.DELETE_FILE);
                                 }

                                 if (posFrom > -1)
                                     change_mapinfo(posFrom, f_path);
                                 //todo
                                 if (files_counter(f_path) == 0) {
                                     //      data.remove(posFrom);
                                 }

                                 mHandler.sendMessage(mHandler.obtainMessage(
                                         CTRL_MEMODB,
                                         UPDATE_ID, id));

                                 activity.runOnUiThread(new Runnable() {
                                     @Override
                                     public void run() {
                                         //             Log.d(TAG, "id : " + id + "deleted");
                                         mImg.remove(id);
                                         //           Log.d(TAG, "mImg.size" + mImg.size());
                                         mHandler.sendEmptyMessageDelayed(UPDATE_VIEW, 20);
                                     }
                                 });

                                 mHandler.sendMessage(mHandler.obtainMessage(
                                         UPDATE_RECYCLER,
                                         UPDATE_ID, posFrom));

                         }
                     }
                     break;
                 case SINGLE_FILE_SHARE_CTRL:
                     //공유 기능 AsyncTask
                     SendTask task_single_share = new SendTask(context, file_path, 0);
                     task_single_share.execute();
                     break;
                 case MULTI_FILE_SHARE_CTRL:
                     SendTask task_multi_share = new SendTask(context, file_path, 1);
                     task_multi_share.execute();
                     break;
                 case CLEAR_ITEM_CTRL:
                     mHandler.sendEmptyMessageDelayed(CLEAR_SELECTED_ITEM, 0);
                     break;
                 case MULTI_FILE_MOVE_CTRL:
         //            Log.d(TAG, "run_activity path: " + mapInfos.get(id).getfolder());
                     SendTask task_multi_move = new SendTask(context, mapInfos.get(id).getfolder(), 3);
                     task_multi_move.execute();
                     break;
                 case UPDATE_CTRL:
                     int pos = 0;
                     mHandler.sendEmptyMessageDelayed(UPDATE_VIEW, 0);
                     /*
                     mHandler.sendMessageDelayed(mHandler.obtainMessage(
                             UPDATE_RECYCLER,
                             UPDATE_ID, 0), 10);
                             */
                     break;
                 case UPDATE_CTRL_RECYCLER:
                     mHandler.sendMessageDelayed(mHandler.obtainMessage(
                             UPDATE_RECYCLER,
                             UPDATE_ID, 0), 10);

                     break;
                 case RECYCLER_VIEW:
                     boolean cam_status = false;
                     Camera2BasicFragment camera2BasicFragment = new Camera2BasicFragment();
                     cam_status = camera2BasicFragment.check_cam_status();
         //            Log.d(TAG, "cam_status " + cam_status);
                     if (cam_status == false) {
                         if (recyclerPath != null) {
           //                  Log.d(TAG, "recyclerPath(imagesView): " + recyclerPath);
                             show_recycle_view(context, recyclerPath, id);
                         } else {
                             ret = -1;
                         }
                     } else {
                         ret = 1;
                     }
                     break;
                 case SET_ADS:
         //            Log.d(TAG, "set ads");
                     MobileAds.initialize(activity, "ca-app-pub-4387339919511881~4433795886");
                     setAds();
                     break;

                 case MULTI_FILE_MOVE_CTRL_RECYCLER:
                     SendTask task_multi_move_recycle = new SendTask(context, mapInfos.get(id).getfolder(), 7);
                     task_multi_move_recycle.execute();
                     break;
                 case MULTI_FILE_COPY_CTRL_RECYCLER:
                     SendTask task_multi_copy_recycler = new SendTask(context, mapInfos.get(id).getfolder(), 9);
                     task_multi_copy_recycler.execute();
                     break;

                 case MULTI_FILE_COPY_CTRL:
                     SendTask task_multi_copy = new SendTask(context, mapInfos.get(id).getfolder(), 8);
                     task_multi_copy.execute();
                     break;

             }


         } catch (Exception e) {
             e.printStackTrace();

         }

         return ret;
     }

     public void show_recycle_view(Context context, String path, int id) {

         String r_img[] = {};
         File file = new File(path);
         r_img = file.list();

         List<String> r_mImg = null;
         r_mImg = new ArrayList();

         for (int i = 0; i < r_img.length; i++) {
             if (fileController.file_format_check(r_img[i]) > 0)
                r_mImg.add(r_img[i]);
         }


         //여기서 왜이래 놨지????
         Collections.sort(r_mImg);
         Collections.reverse(r_mImg);
         selectedPositions_metal.clear();
         String[] recycle_img = metalList.toArray(new String[metalList.size()]);
         Intent intent_focus_image = new Intent(context, FocusViewPager.class);
         intent_focus_image.putExtra("focus_path", path);
         intent_focus_image.putExtra("imgs", recycle_img);
         intent_focus_image.putExtra("id", id);
         intent_focus_image.putExtra("focus_storage", recycler_storage);
         if (recycle_img.length != 0)
            context.startActivity(intent_focus_image);
     }

    public int recycler_item_click(int pos, int ctrl) {
        int ret = 0;
        boolean cam_status = false;

        try {
            Camera2BasicFragment  camera2BasicFragment = new Camera2BasicFragment();
            cam_status = camera2BasicFragment.check_cam_status();
      //      Log.d(TAG, "cam_status " + cam_status);

            if (single_item_id == pos && cam_status == false) {
        //       Log.d(TAG, "same id clicked.");
                return ret;
            }
            single_item_id = pos;
            ImagesView run = new ImagesView();
            ret = run.run_activiy(activity, pos, ctrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }
    static MainActivity mainActivity = new MainActivity();
    static FileController fileController = new FileController();

     public int files_counter (String path) {

         try {
             int internal_cnt = 0;
             int external_cnt = 0;

             File mediaStorageDir_ct;
             File mediaStorageDir_ct_external;

             mediaStorageDir_ct = new File(Environment.getExternalStorageDirectory() + Appdir, path);
             mediaStorageDir_ct_external = new File(sdcard_path_header + Appdir, path);

             File[] files = mediaStorageDir_ct.listFiles();
             File[] files_external = mediaStorageDir_ct_external.listFiles();

   //          Log.d(TAG, path + " : " + String.valueOf(files.length) + "pics (internal) " + String.valueOf(files_external.length) + "pics (external)");

             int cnt = 0;
             int e_cnt = 0;

             if (files == null) {
//            Log.d(TAG, "files null");
             } else {
                 for (int j = 0; j < files.length; j++) {
                     if (!files[j].getName().contains(".")) {
                         cnt++;
                     }
                 }
                 internal_cnt = files.length - cnt;
    //             Log.d(TAG, path + " : " + internal_cnt + "pics (internal)");

             }

             if (files_external == null) {
//            Log.d(TAG, "files_external null");
             } else {
                 for (int j = 0; j < files_external.length; j++) {
                     if (!files_external[j].getName().contains(".")) {
                         e_cnt++;
                     }
                 }
                 external_cnt = files_external.length - e_cnt;
    //             Log.d(TAG, path + " : " + external_cnt + "pics (external)");
             }

             return internal_cnt + external_cnt;

         } catch (Exception e) {
             e.printStackTrace();
         }

        return 0;
    }
    private String external_file_name = null;
    private static int id_cnt = -1;
    private static int gallery_status = 0;

    private class SendTask extends AsyncTask {
        private String mfile_path;
        private Context mContext;
        private int mMode;
        private String Update_recycler_from = "null";
        private String Update_recycler_to = "null";
        private int posFrom = -1;
        private int posTo = -1;


        CustomProgressDialog customProgressDialog = new CustomProgressDialog(activity, "처리 중 입니다.");



        public SendTask(Context context, String file_path, int mode) {
            mfile_path = file_path;
            mContext = context;
            mMode = mode;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            Log.d(TAG, "file_path: " + mfile_path);
            //        Uri uri = Uri.fromFile(new File(file_path));
            try {
                if (mMode == 0) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);

                    if (fileController.file_format_check(mfile_path) == 1) {
                        intent.setType("image/*");
                    } else if (fileController.file_format_check(mfile_path) == 2) {
                        intent.setType("video/*");
                    }
                    File file = new File(mfile_path);
                    Uri contentUri = null;
                    if (focus_storage == INTERNAL_STORAGE)
                        contentUri = FileProvider.getUriForFile(mContext, "com.toadstudio.first.toadproject" + ".fileprovider", file);
                    else {
                        contentUri = fileController.getUriFromPath(activity, mfile_path, null);
                    }
                    if (contentUri != null) {
                        intent.putExtra(Intent.EXTRA_STREAM, contentUri);

                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                        Intent chooser = Intent.createChooser(intent, "친구에게 공유하기");
                        mContext.startActivity(chooser);
                    } else {
                        Toast.makeText(activity, "실행 중 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }

                } else if (mMode == 1) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    intent.setType("image/*");
                    //         intent.setType("video/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    ArrayList<Uri> arrUri = new ArrayList<>();
                    Uri contentUri = null;
                    File file;
                    for (int cnt = 0; cnt < selectedPositions.size(); cnt++) {
                        if (focus_storage == INTERNAL_STORAGE) {
                            file = new File(basePath + File.separator + mImg.get(selectedPositions.get(cnt)));
                            contentUri = FileProvider.getUriForFile(mContext, "com.toadstudio.first.toadproject" + ".fileprovider", file);
                            arrUri.add(contentUri);
                        } else {
                            String path = SDCARD_DIR + File.separator + mImg.get(selectedPositions.get(cnt));
                            contentUri = fileController.getUriFromPath(activity, path, null);
                            arrUri.add(contentUri);
                        }
                    }




                    if (contentUri != null) {
                        intent.putParcelableArrayListExtra(intent.EXTRA_STREAM, arrUri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                        Intent chooser = Intent.createChooser(intent, "친구에게 공유하기");
                        mContext.startActivity(chooser);
                    } else {
                        Toast.makeText(activity, "실행 중 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }

                } else if (mMode == 2) { //삭제
                    int cnt = 0;
                    Collections.sort(selectedPositions);
                    Collections.reverse(selectedPositions);
/*
                for (int i = 0; i < selectedPositions.size(); i++) {
                    Log.d(TAG, "selectedPos: " + selectedPositions.get(i));
                }
*/
                    for (int i = 0; i < mapInfos.size(); i++) {
                        if (f_path.equals(mapInfos.get(i).getfolder())) {
                            posFrom = i;
                            //                     Log.d(TAG, "mapinfo posFrom: " + posFrom);
                        }
                    }

                    Update_recycler_from = f_path;

                    if (posFrom > -1) {
                        while (true) {
                            int size;

                            size = selectedPositions.size();

                            try {
                                Thread.sleep(10);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            //         Log.d(TAG, "while loop: " + size);
                            if (size == 0) {
                                prev_id = -1;
                                break;
                            } else {
                                int id = selectedPositions.get(cnt);

                                if (focus_storage == INTERNAL_STORAGE) {
                                    File file = new File(basePath + File.separator + mImg.get(id));
                                    file.delete();

                                    mHandler.sendMessage(mHandler.obtainMessage(
                                            CTRL_MEMODB,
                                            UPDATE_ID, id));

                                    mImg.remove(id);
                                    //               Log.d(TAG, "mImg.size" + mImg.size());

                                    int selectedIndex = selectedPositions.indexOf(id);
                                    if (selectedIndex > -1) {
                                        //       Log.d(TAG, "selectedPositions: " + selectedIndex);
                                        selectedPositions.remove(selectedIndex);
                                    }
                                    //     Log.d(TAG, "selectedPositions.size" + selectedPositions.size());
                                    //        cnt++; //cnt 증가

                                } else {
                          //          fileController.file_controller(mainActivity.return_mainActivity_context(), Appdir + File.separator + f_path + File.separator + mImg.get(id), null, mImg.get(id), fileController.DELETE_FILE);

                                    if (prev_id == -1) {
                                        prev_id = 1;

                                        to_external_img_path = Appdir + File.separator + f_path;
                                        external_file_name = mImg.get(id);
                                        mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                                SEND_EXTERNAL_STORAGE,
                                                EXTERNAL_DELETE, id), 0);


                                        mHandler.sendMessage(mHandler.obtainMessage(
                                                CTRL_MEMODB,
                                                UPDATE_ID, id));
/*
                                        mImg.remove(id);
                                        //               Log.d(TAG, "mImg.size" + mImg.size());

                                        int selectedIndex = selectedPositions.indexOf(id);
                                        if (selectedIndex > -1) {
                                            //       Log.d(TAG, "selectedPositions: " + selectedIndex);
                                            selectedPositions.remove(selectedIndex);
                                        }
                                        //     Log.d(TAG, "selectedPositions.size" + selectedPositions.size());
                                        //        cnt++; //cnt 증가
*/
                                    }

                                }

               //                 Log.d(TAG, "id : " + id + " deleted" + " selectedPositions.size : " + selectedPositions.size());


                            }
                        }
                    }

                } else if (mMode == 3) { //파일 이동(main에서 recycler로)
                    int from_storage_status = -1;
                    int to_storage_status = -1;
                    //이동
                    //정렬


                    try {

                        Collections.sort(selectedPositions);
                        Collections.reverse(selectedPositions);
/*
                for (int i = 0; i < selectedPositions.size(); i++) {
                    Log.d(TAG, "selectedPos: " + selectedPositions.get(i));
                }
*/
/*
                        if (f_path.equals(mfile_path)) {
                            //
                            //            Toast.makeText(activity, "경로가 같습니다.", Toast.LENGTH_LONG).show();
                            return 0;
                        }
*/
                        for (int i = 0; i < mapInfos.size(); i++) {
                            if (mfile_path.equals(mapInfos.get(i).getfolder())) {
                                to_storage_status = Integer.parseInt(mapInfos.get(i).getStorage());
                                posTo = i;
                                //                     Log.d(TAG, "mapinfo posTo: " + posTo);
                            }

                            if (f_path.equals(mapInfos.get(i).getfolder())) {
                                from_storage_status = focus_storage;
                                posFrom = i;
                                //                     Log.d(TAG, "mapinfo posFrom: " + posFrom);
                            }
                        }


                        if (to_storage_status == BOTH_STORAGE) {
                            to_storage_status = to_recycler_storage;
                        }

                //        Log.d(TAG, "MOVE from(M): " + f_path + " to(R): " + mfile_path + " <" + from_storage_status + " -> " + to_storage_status + ">");

                        Update_recycler_from = f_path;
                        Update_recycler_to = mfile_path;

                        if (posFrom > -1 && posTo > -1) {
                            while (true) {
                                int size;
                                int cnt = 0;

                                size = selectedPositions.size();

                                try {
                                    Thread.sleep(10);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                             //                   Log.d(TAG, "while loop: " + size);
                                if (size == 0) {

                                    prev_id = -1;
                                    to_recycler_storage = -1;
                                    break;
                                } else {

                                    //            File file = new File(basePath + File.separator + mImg.get(id));
                                    if (prev_id == -1) {
                                        prev_id = 1;
                                        int id = selectedPositions.get(cnt);

                                        if (from_storage_status == INTERNAL_STORAGE) {
                                            file_path = basePath + File.separator + mImg.get(id);
                                        } else if (from_storage_status == EXTERNAL_STORAGE) {
                                            file_path = sdcard_path_header + Appdir + File.separator + f_path + File.separator + mImg.get(id);
                                        } else {
                                            //todo

                                        }
                                        //from은 어차피 정해져 있다
                                        if (to_storage_status == INTERNAL_STORAGE) {

                                            if (from_storage_status == INTERNAL_STORAGE) {
                                                mainActivity.moveFile(activity, file_path, mfile_path, from_storage_status, to_storage_status);
                                                //           file.delete();
                                                //                Log.d(TAG, "id : " + id + " moved" + " selectedPositions.size : " + selectedPositions.size());
                                                prev_id = -1;
                                                //         Log.d(TAG, "selectedPositions.size" + selectedPositions.size());
                                                //        cnt++; //cnt 증가
                                            } else {
                                                //todo from이 외장인 경우
                                                mainActivity.copyFile(file_path, mfile_path);
                                                to_external_img_path = Appdir + File.separator + f_path;
                                                external_file_name = mImg.get(id);
                                                //todo
                                                mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                                        SEND_EXTERNAL_STORAGE,
                                                        EXTERNAL_DELETE, id), 0);


                                            }
/*
                                            mImg.remove(id);

                                            //             Log.d(TAG, "mImg.size" + mImg.size());

                                            int selectedIndex = selectedPositions.indexOf(id);

                                            if (selectedIndex > -1) {
                                                //             Log.d(TAG, "selectedPositions: " + selectedIndex);
                                                selectedPositions.remove(selectedIndex);
                                            }
*/
                                        } else if (to_storage_status == EXTERNAL_STORAGE) {
                                        /*
                                        String path = mainActivity.get_main_path();
                                        fileController.file_controller(mainActivity.return_mainActivity_context(), path, file_path,Appdir + File.separator + f_path, mImg.get(id), fileController.COPY_FILE);    */


                                            to_external_img_path = mfile_path;
                                            external_file_name = mImg.get(id);
                                            mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                                    SEND_EXTERNAL_STORAGE,
                                                    EXTERNAL_MOVE, id), 0);

/*
                                            mImg.remove(id);

                                            //             Log.d(TAG, "mImg.size" + mImg.size());

                                            int selectedIndex = selectedPositions.indexOf(id);

                                            if (selectedIndex > -1) {
                                                //             Log.d(TAG, "selectedPositions: " + selectedIndex);
                                                selectedPositions.remove(selectedIndex);
                                            }

                                            //         Log.d(TAG, "selectedPositions.size" + selectedPositions.size());
                                            //        cnt++; //cnt 증가
*/

                                        } else {
                                            //todo
                                        }

                                    }
                                }
                            }
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                } else if (mMode == 4) { //갤러리에서 사진 가져오기
                    //    Log.d(TAG, "Start gallery get");
                    try {
                    /*
                    String flag = null;
                    flag = imageList.get(0).substring(0,7);

                    for (int i = 0; i < imageList.size(); i++) {
                        if (flag.contains("content")) {
                            Cursor cursor = null;
                            try {
                                cursor = getContentResolver().query(Uri.parse(imageList.get(i)), null, null, null, null);
                                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                                cursor.moveToFirst();
                                imageListPath.add(cursor.getString(column_index));
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (cursor != null) {
                                    cursor.close();
                                }
                            }
                        } else {
                            String path = imageList.get(i).replace("file://", "");
                            imageListPath.add(path);
                        }
                    }
*/

                        for (int i = 0; i < imageList.size(); i++) {
                            String path = fileController.getPath(mContext, imageList.get(i));
                            imageListPath.add(path);
                 //           Log.d(TAG, "image absolute path:" + path);
                        }
                        for (int i = 0; i < imageListPath.size(); i++) {
                            //             Log.d(TAG, "imageListPath " + imageListPath.get(i));
                            List<String> str = Arrays.asList(imageListPath.get(i).toString().split("/"));
                            for (int j = 0; j < str.size(); j++) {
                                if (fileController.file_format_check(str.get(j)) > 0) {
                                    imageName.add(str.get(j));
                                }
                            }
                        }

                    /*
                    for (int i = 0; i < imageName.size(); i++) {
                        Log.d(TAG, "imageName: " + imageName.get(i));
                    }
*/
                        mHandler.sendEmptyMessageDelayed(GALLERY_GET, 0);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (mMode == 5) { //갤러리에서 사진 이동
                    //todo 갤러리 사진 이동 아직 안됨
                    try {

                        if (focus_storage == INTERNAL_STORAGE) {
                            for (int i = 0; i < imageListPath.size(); i++) {
                                if (mainActivity.moveFileDelete(mContext, imageListPath.get(i), f_path) == true) {
                                    gallery_status = 1;
                                } else {
                                    //               Log.d(TAG, "file move fail");
                                }
                            }
                        } else {
                      //      int percent = 0;
                      //      int id_cnt = 0;
                            if (imageListPath.size() > 0) {
                                while (true) {

                            //        if (id_cnt != -1) {
                            //            percent =(100 * id_cnt) / imageListPath.size();
                            //        }


                                    if (id_cnt == imageListPath.size()) {
                                 //       if (customProgressDialog.isShowing())
                                 //           customProgressDialog.setPercentage(100);
                                        break;
                                    }

                                    if (prev_id == -1) {
                                        prev_id = 1;

                                        file_path = imageListPath.get(id_cnt);

                                        List<String> split_str_path = new ArrayList<>();
                                        split_str_path = Arrays.asList(file_path.split("/"));

                                        for (int i = 0 ; i < split_str_path.size(); i++) {
                                            if (split_str_path.get(i).contains(".")) {
                                                if (fileController.file_format_check(split_str_path.get(i)) > 0) {

                                                    to_external_img_path = f_path;
                                                    external_file_name = split_str_path.get(i);
                                                    mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                                            SEND_EXTERNAL_STORAGE,
                                                            EXTERNAL_GALLERY_MOVE, id_cnt), 0);

                                                    gallery_status = 1;
                                                }
                                            }
                                        }

                                    }
                             //       if (customProgressDialog.isShowing())
                             //           customProgressDialog.setPercentage(percent);
                                }
                            }
                        }

                        if (gallery_status == 1) {
                            int pos = 0;

                            while (true) {
                                int size = imageName.size();

                                //            Log.d(TAG, "loop pos: " + pos + " size: " + size);

                                if (size == 0 || pos > size - 1) {
                                    //               Log.d(TAG, "loop break");
                                    break;
                                }

                                if (check_images_Img(imageName.get(pos).toString()) == true) {
                                    //               Log.d(TAG, imageName.get(pos) + " removed");
                                    imageName.remove(pos);
                                    //    pos = 0;
                                } else {
                                    pos++;
                                }
                            }

                            mImg.addAll(0, imageName);

                            Update_recycler_from = f_path;
                            posFrom = id_from_main;

              //              change_mapinfo(posFrom, Update_recycler_from);
          //                  check_imgs(mediaStorageDir);
/*
                            mHandler.sendEmptyMessageDelayed(UPDATE_VIEW, 10);
                            mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                    UPDATE_RECYCLER,
                                    UPDATE_ID, id_from_main), 30);
                                    */

                        }

                    } catch (Exception e) {


                        e.printStackTrace();
                    }
                } else if (mMode == 6) { //갤러리에서 사진 복사
                    try {
                        //     Log.d(TAG, "start copy pic from gallery");

                        if (focus_storage == INTERNAL_STORAGE) {
                            for (int i = 0; i < imageListPath.size(); i++) {
                                if (mainActivity.copyFile(imageListPath.get(i), f_path) == true) {
                                    gallery_status = 1;
                                } else {
                                    //        Log.d(TAG, "file copy fail");
                                }
                            }
                        } else {

                  //          int id_cnt = 0;
                      //      int percent = 0;
                            if (imageListPath.size() > 0) {
                                while (true) {

                               //     if (id_cnt != -1) {
                             //        percent =(100 * id_cnt) / imageListPath.size();
                             //       }
                    //                Log.d(TAG, "id_cnt " + id_cnt);

                                    if (id_cnt == imageListPath.size()) {
                                  //      if (customProgressDialog.isShowing())
                                //            customProgressDialog.setPercentage(100);
                               //         customProgressDialog.setProgressBar(100);
                                        break;
                                    }

                                    if (prev_id == -1) {
                                        prev_id = 1;

                                        file_path = imageListPath.get(id_cnt);

                                        List<String> split_str_path = new ArrayList<>();
                                        split_str_path = Arrays.asList(file_path.split("/"));

                                        for (int i = 0 ; i < split_str_path.size(); i++) {
                                            if (split_str_path.get(i).contains(".")) {
                                                if (fileController.file_format_check(split_str_path.get(i)) > 0) {
                                             //       if (customProgressDialog.isShowing())
                                           //             customProgressDialog.setPercentage(percent);
                                                    to_external_img_path = f_path;
                                                    external_file_name = split_str_path.get(i);
                                                    mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                                            SEND_EXTERNAL_STORAGE,
                                                            EXTERNAL_COPY, id_cnt), 0);

                                                    gallery_status = 1;
                                                }
                                            }
                                        }

                                    }

                                }
                            }
                        }
                        if (gallery_status == 1) {
                            int pos = 0;

                            while (true) {
                                int size = imageName.size();

                      //          Log.d(TAG, "loop pos: " + pos + " size: " + size);

                                if (size == 0 || pos > size - 1) {
                                    //                   Log.d(TAG, "loop break");
                                    break;
                                }

                                if (check_images_Img(imageName.get(pos).toString()) == true) {
                                    Log.d(TAG, imageName.get(pos) + " removed");
                                    imageName.remove(pos);
                                    //    pos = 0;
                                } else {
                                    pos++;
                                }
                            }

                            mImg.addAll(0, imageName); //맨처음에 붙이기

                            Update_recycler_from = f_path;
                            posFrom = id_from_main;

            //                change_mapinfo(posFrom, Update_recycler_from);
                        /*
                        if (check_imgs(mediaStorageDir) < 0) {
                            Log.d(TAG, "no images error");
                        }

                        */
                            //         Log.d(TAG, "end copy pic from gallery");

               //             check_imgs(mediaStorageDir);

/*

                            mHandler.sendEmptyMessageDelayed(UPDATE_VIEW, 100);
                            mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                    UPDATE_RECYCLER,
                                    UPDATE_ID, id_from_main), 100);
                                    */

                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                } else if (mMode == 7) { //recyler로 이동 하는 경우

                    int from_storage_status = -1;
                    int to_storage_status = -1;


                    for (int i = 0; i < mapInfos.size(); i++) {
                        if (mfile_path.equals(mapInfos.get(i).getfolder())) {
                            posTo = i;
                            to_storage_status = Integer.parseInt(mapInfos.get(i).getStorage());
                        }
                        if (recyclerBasePath.equals(mapInfos.get(i).getfolder())) {
                 //           from_storage_status = Integer.parseInt(mapInfos.get(i).getStorage());

                            posFrom = i;
                            //                     Log.d(TAG, "mapinfo posFrom: " + posFrom);
                        }
                    }

                    if (to_storage_status == BOTH_STORAGE) {
                        to_storage_status = to_recycler_storage;
                    }

          //          Log.d(TAG, "Move from(R): " + recyclerBasePath + " to(R): " + mfile_path + "<" + focus_storage + " -> " + to_storage_status + ">");
                    try {
/*
                        if (recyclerBasePath.equals(mfile_path)) {
                            Log.d(TAG, "recyclerBasePath equal ");
                        } else {
                        */
                            int id = 0;


                            Collections.sort(selectedPositions_metal);
                            Collections.reverse(selectedPositions_metal);

                            if (item_pos > -1) {
                                id = item_pos;
                            } else {
                                id = id_from_main;
                            }

                            //init
                            int k = 0;
                            prev_id = -1;

                            while (true) {
                                if (prev_id == -1) {
                                    prev_id = 1;

                                    if (k == selectedPositions_metal.size()) {
                                        to_recycler_storage = -1;

                                        break;
                                    }

                                    try {
                                        Thread.sleep(10);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
/*
                                    Log.d(TAG, "selectedPos_metal data : " + selectedPositions_metal.get(k)
                                            + " metalist: " + metalImageList.get(selectedPositions_metal.get(k)));
                                            */
                                    if (to_storage_status == INTERNAL_STORAGE) {
                                        if (metalImageList.get(selectedPositions_metal.get(k)).contains(sdcard_path_header)) {
                                            mainActivity.copyFile(metalImageList.get(selectedPositions_metal.get(k)), mfile_path);

                                            to_external_img_path = Appdir + File.separator + recyclerBasePath;
                                            external_file_name = metalList.get(selectedPositions_metal.get(k));
                                            mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                                    SEND_EXTERNAL_STORAGE,
                                                    EXTERNAL_DELETE_RECYCLER, id), 0);

                                            metalImageList.remove(selectedPositions_metal.get(k));
                                            metalList.remove(selectedPositions_metal.get(k));

                                        } else {
                                            if (mainActivity.moveFile(activity, metalImageList.get(selectedPositions_metal.get(k)), mfile_path, focus_storage, to_storage_status) == true) {
                                                metalImageList.remove(selectedPositions_metal.get(k));

                                            } else {

                                            }
                                            prev_id = -1;

                                        }
                                        k++;

                                    } else if (to_storage_status == EXTERNAL_STORAGE) {
                                        file_path = metalImageList.get(selectedPositions_metal.get(k)); //from
                                        to_external_img_path = mfile_path; //to
                                        external_file_name = metalList.get(selectedPositions_metal.get(k)); //file name
                                        mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                                SEND_EXTERNAL_STORAGE,
                                                EXTERNAL_MOVE_RECYCLER, k), 0);


                                        metalImageList.remove(selectedPositions_metal.get(k));
                                        metalList.remove(selectedPositions_metal.get(k));

                                        k++;
                                    } else {
                                        //todo
                                    }

                                }

                            }
/*
                            for (int k = 0; k < selectedPositions_metal.size(); k++) {

                                Log.d(TAG, "selectedPos_metal data : " + selectedPositions_metal.get(k)
                                        + " metalist: " + metalImageList.get(selectedPositions_metal.get(k)));

                                if (to_storage_status == INTERNAL_STORAGE) {
                                    mainActivity.moveFile(metalImageList.get(selectedPositions_metal.get(k)), mfile_path, focus_storage, to_storage_status);
                                    metalImageList.remove(selectedPositions_metal.get(k));
                                } else {
                                    file_path = metalImageList.get(selectedPositions_metal.get(k)); //from
                                    to_external_img_path = mfile_path; //to
                                    external_file_name = metalList.get(k); //file name
                                    mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                            SEND_EXTERNAL_STORAGE,
                                            EXTERNAL_MOVE_RECYCLER, k), 0);
                                }
                            }
*/
                            selectedPositions_metal.clear();

                            posFrom = id;
                            Update_recycler_from = recyclerBasePath;

                    //        change_mapinfo(id, recyclerBasePath);
                            if (posTo > -1)
                                Update_recycler_to = mfile_path;
                                //change_mapinfo(posTo, mfile_path);

          //                  check_imgs(mediaStorageDir);
/*
                            mHandler.sendEmptyMessageDelayed(UPDATE_VIEW, 10);

                            mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                    UPDATE_RECYCLER,
                                    UPDATE_ID, id), 30);
*/


                //        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                } else if (mMode == 8) {
                    int from_storage_status = -1;
                    int to_storage_status = -1;
                    //이동
                    //정렬
                    Collections.sort(selectedPositions);
                    Collections.reverse(selectedPositions);
/*
                for (int i = 0; i < selectedPositions.size(); i++) {
                    Log.d(TAG, "selectedPos: " + selectedPositions.get(i));
                }
*/


                try {
                    /*
                    if (f_path.equals(mfile_path)) {
                        //
                        //            Toast.makeText(activity, "경로가 같습니다.", Toast.LENGTH_LONG).show();
                        return 0;
                    }
*/
                    for (int i = 0; i < mapInfos.size(); i++) {
                        if (mfile_path.equals(mapInfos.get(i).getfolder())) {
                            to_storage_status = Integer.parseInt(mapInfos.get(i).getStorage());
                            posTo = i;
                            //                     Log.d(TAG, "mapinfo posTo: " + posTo);
                        }

                        if (f_path.equals(mapInfos.get(i).getfolder())) {
                            from_storage_status = focus_storage;
                            posFrom = i;
                            //                     Log.d(TAG, "mapinfo posFrom: " + posFrom);
                        }
                    }

                    if (to_storage_status == BOTH_STORAGE) {
                        to_storage_status = to_recycler_storage;
                    }

       //             Log.d(TAG, "COPY from(M): " + f_path + " to(R): " + mfile_path + "<" + from_storage_status + " -> " + to_storage_status + ">");

                    Update_recycler_from = f_path;
                    Update_recycler_to = mfile_path;

                    if (posFrom > -1 && posTo > -1) {
                        while (true) {
                            int size;
                            int cnt = 0;

                            size = selectedPositions.size();
                 //           Log.d(TAG, "while loop: " + size);

                            try {
                                Thread.sleep(10);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (size == 0) {

                      //          change_mapinfo(posFrom, f_path);
                      //          change_mapinfo(posTo, mfile_path);


                                //internal/external 상관 없음
                                //todo
                                /*
                                if (files_counter(f_path) == 0) {
                                    //            data.remove(posFrom);
                                }
*/
                                /*
                               mHandler.sendMessage(mHandler.obtainMessage(
                                    UPDATE_RECYCLER,
                                    UPDATE_ID, posTo));

                                mHandler.sendEmptyMessageDelayed(UPDATE_VIEW, 30);
                                */
                                prev_id = -1;
                                to_recycler_storage = -1;

                                break;
                            } else {

                                //            File file = new File(basePath + File.separator + mImg.get(id));
                                if (prev_id == -1) {
                                    prev_id = 1;
                                    int id = selectedPositions.get(cnt);
                                    if (from_storage_status == INTERNAL_STORAGE) {
                                        file_path = basePath + File.separator + mImg.get(id);
                                    } else if (from_storage_status == EXTERNAL_STORAGE) {
                                        file_path = sdcard_path_header + Appdir + File.separator + f_path + File.separator + mImg.get(id);
                                    } else {

                                    }
                                    //from은 어차피 정해져 있다
                                    if (to_storage_status == INTERNAL_STORAGE) {

                                        mainActivity.copyFile(file_path, mfile_path);
                                        //           file.delete();
                                        //                Log.d(TAG, "id : " + id + " moved" + " selectedPositions.size : " + selectedPositions.size());
                                        //       mImg.remove(id);

                                        //             Log.d(TAG, "mImg.size" + mImg.size());

                                        int selectedIndex = selectedPositions.indexOf(id);

                                        if (selectedIndex > -1) {
                                            //             Log.d(TAG, "selectedPositions: " + selectedIndex);
                                            selectedPositions.remove(selectedIndex);
                                        }
                                        prev_id = -1;
                                        //         Log.d(TAG, "selectedPositions.size" + selectedPositions.size());
                                        //        cnt++; //cnt 증가
                                    } else if (to_storage_status == EXTERNAL_STORAGE) {
                                    /*
                                    String path = mainActivity.get_main_path();
                                    fileController.file_controller(mainActivity.return_mainActivity_context(), path, file_path,Appdir + File.separator + f_path, mImg.get(id), fileController.COPY_FILE);    */


                                        to_external_img_path = mfile_path;
                                        external_file_name = mImg.get(id);
                                        mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                                SEND_EXTERNAL_STORAGE,
                                                EXTERNAL_COPY, id), 0);

                                        int selectedIndex = selectedPositions.indexOf(id);

                                        if (selectedIndex > -1) {
                                            //             Log.d(TAG, "selectedPositions: " + selectedIndex);
                                            selectedPositions.remove(selectedIndex);
                                        }

                                          //  Log.d(TAG, "selectedPositions.size" + selectedPositions.size());
                                        //        cnt++; //cnt 증가

                                    } else {

                                    }

                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                } else if (mMode == 9) {

                    int from_storage_status = -1;
                    int to_storage_status = -1;


                    for (int i = 0; i < mapInfos.size(); i++) {
                        if (mfile_path.equals(mapInfos.get(i).getfolder())) {
                            posTo = i;
                            to_storage_status = Integer.parseInt(mapInfos.get(i).getStorage());
                        }
                        if (recyclerBasePath.equals(mapInfos.get(i).getfolder())) {
                            //           from_storage_status = Integer.parseInt(mapInfos.get(i).getStorage());
                            posFrom = i;
                            //                     Log.d(TAG, "mapinfo posFrom: " + posFrom);
                        }
                    }

                    if (to_storage_status == BOTH_STORAGE) {
                        to_storage_status = to_recycler_storage;
                    }

                    //todo from이 외장인 경우는
            //        Log.d(TAG, "COPY from(R): " + recyclerBasePath + " to(R): " + mfile_path + "<" + focus_storage + " -> " + to_storage_status + ">");

                    try {


                            int id = 0;


                            Collections.sort(selectedPositions_metal);
                            Collections.reverse(selectedPositions_metal);

                            if (item_pos > -1) {
                                id = item_pos;
                            } else {
                                id = id_from_main;
                            }

                            //init
                            int k = 0;
                            prev_id = -1;

                            while (true) {
                                if (prev_id == -1) {
                                    prev_id = 1;

                                    if (k == selectedPositions_metal.size()) {
                                        to_recycler_storage = -1;
                                        break;
                                    }
/*

                                    Log.d(TAG, "selectedPos_metal data : " + selectedPositions_metal.get(k)
                                            + " metalist: " + metalImageList.get(selectedPositions_metal.get(k)));
                                            */

                                    try {
                                        Thread.sleep(10);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    if (to_storage_status == INTERNAL_STORAGE) {

                                        if (mainActivity.copyFile(metalImageList.get(selectedPositions_metal.get(k)), mfile_path) == true) {

                                        }
                                        /*
                                        if (mainActivity.moveFile(activity, metalImageList.get(selectedPositions_metal.get(k)), mfile_path, focus_storage, to_storage_status) == true) {
                                            metalImageList.remove(selectedPositions_metal.get(k));

                                        } else {

                                        }
                                        */
                                        prev_id = -1;
                                        k++;

                                    } else if (to_storage_status == EXTERNAL_STORAGE) {
                                        /*
                                        file_path = metalImageList.get(selectedPositions_metal.get(k)); //from
                                        to_external_img_path = mfile_path; //to
                                        external_file_name = metalList.get(k); //file name
                                        mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                                SEND_EXTERNAL_STORAGE,
                                                EXTERNAL_MOVE_RECYCLER, k), 0);
                                        k++;
                                        */
                                        file_path = metalImageList.get(selectedPositions_metal.get(k)); //from
                                        to_external_img_path = mfile_path;
                                        external_file_name = metalList.get(selectedPositions_metal.get(k));
                                        mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                                SEND_EXTERNAL_STORAGE,
                                                EXTERNAL_COPY_RECYCLER, k), 0);

                                        k++;

                                    } else {
                                        //todo
                                    }

                                }

                            }
/*
                            for (int k = 0; k < selectedPositions_metal.size(); k++) {

                                Log.d(TAG, "selectedPos_metal data : " + selectedPositions_metal.get(k)
                                        + " metalist: " + metalImageList.get(selectedPositions_metal.get(k)));

                                if (to_storage_status == INTERNAL_STORAGE) {
                                    mainActivity.moveFile(metalImageList.get(selectedPositions_metal.get(k)), mfile_path, focus_storage, to_storage_status);
                                    metalImageList.remove(selectedPositions_metal.get(k));
                                } else {
                                    file_path = metalImageList.get(selectedPositions_metal.get(k)); //from
                                    to_external_img_path = mfile_path; //to
                                    external_file_name = metalList.get(k); //file name
                                    mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                            SEND_EXTERNAL_STORAGE,
                                            EXTERNAL_MOVE_RECYCLER, k), 0);
                                }
                            }
*/
                            selectedPositions_metal.clear();

                            posFrom = id;
                            Update_recycler_from = recyclerBasePath;


              //              change_mapinfo(id, recyclerBasePath);

                            if (posTo > -1)
                                Update_recycler_to = mfile_path;

                                //  change_mapinfo(posTo, mfile_path);

        //                    check_imgs(mediaStorageDir);
/*
                            mHandler.sendEmptyMessageDelayed(UPDATE_VIEW, 10);

                            mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                    UPDATE_RECYCLER,
                                    UPDATE_ID, id), 30);

*/


                    } catch (Exception e) {


                        e.printStackTrace();
                    }



                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {

      //      Log.d(TAG, "sendTask PreExecute");
            id_cnt = 0;
            gallery_status = 0;
            customProgressDialog.show();

            try {
                Thread.sleep(50);
            } catch (Exception e) {
                e.printStackTrace();
            }

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {

      //      Log.d(TAG, "sendTask PostExecute");

            check_imgs(mediaStorageDir);

            if (posTo > -1 && !Update_recycler_to.equals("null")) {
                change_mapinfo(posTo, Update_recycler_to);
                mHandler.sendMessage(mHandler.obtainMessage(
                        UPDATE_RECYCLER,
                        UPDATE_ID, posTo));
            }

            if (posFrom > -1 && !Update_recycler_from.equals("null")) {
                change_mapinfo(posFrom, Update_recycler_from);
                mHandler.sendMessageDelayed(mHandler.obtainMessage(
                        UPDATE_RECYCLER,
                        UPDATE_ID, posFrom), 30);
            }

            mHandler.sendEmptyMessageDelayed(UPDATE_VIEW, 20);
            gallery_status = 0;
            customProgressDialog.dismiss();

            super.onPostExecute(o);
        }
    }

    public boolean check_images_Img(String imageName) {
         boolean check = false;
         for (int i = 0; i < mImg.size(); i++ ) {
             if (imageName.contains(mImg.get(i).toString())) {
                 check = true;
                 break;
             }
         }
         return check;
    }

    public void change_mapinfo(int id, String path) {
         try {
             int pics = files_counter(path);
      //       Log.d(TAG, "path: " + path + " pics:" + pics);

             MapInfo mMapInfo_class = new MapInfo(
                     mapInfos.get(id).getid(),
                     mapInfos.get(id).getfolder(),
                     mapInfos.get(id).getlatitude(),
                     mapInfos.get(id).getlongitude(),
                     Integer.toString(pics),
                     mapInfos.get(id).getFav(),
                     mapInfos.get(id).getdata(),
                     mapInfos.get(id).getlevel(),
                     mapInfos.get(id).getStorage());
             mapInfos.set(id, mMapInfo_class);

             Listviewitem items = new Listviewitem(mSettingInfo.get(0).getMarker(), mapInfos.get(id).getdata(), mapInfos.get(id).getfolder(), mapInfos.get(id).getreserved(),
                     Integer.parseInt(mapInfos.get(id).getFav()), Integer.parseInt(mapInfos.get(id).getStorage()));

        //     Log.d(TAG, "data set: " + id);
             if (data.size() != 0)
                 data.set(id, items);
         } catch (Exception e) {
             e.printStackTrace();
         }

    }
     static File mediaStorageDir;
     static File SDCARD_DIR;
     static File INTERNAL_DIR;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Log.d(TAG, "onCreate");
        activity = ImagesView.this;

        //main 사진
        mImg =  new ArrayList<>();
        selectedPositions = new ArrayList<>();
        selectedPositions_metal = new ArrayList<>();

        metalImageList = new ArrayList<>();
        metalList = new ArrayList<>();

        //   gv.setAdapter(adapter);  // 커스텀 아답타를 GridView 에 적용


        // Intent 가져오기.
        Intent intent = getIntent();

        id_from_main = intent.getIntExtra("id", 0);
        delete_enable = intent.getIntExtra("enable", 0);
        f_path = intent.getStringExtra("f_path");
        from = intent.getStringExtra("from");

        mapInfos = (ArrayList<MapInfo>)getIntent().getSerializableExtra("mMapInfo");
        mSettingInfo = (ArrayList<SettingInfo>)getIntent().getSerializableExtra("mSetting");


        for (int i = 0; i < mapInfos.size(); i++) {
            if (f_path.equals(mapInfos.get(i).getfolder())) {
                if (Integer.parseInt(mapInfos.get(i).getStorage()) == EXTERNAL_STORAGE) {
                    focus_storage = EXTERNAL_STORAGE;
                    main_page_storage = EXTERNAL_STORAGE;
                } else if(Integer.parseInt(mapInfos.get(i).getStorage()) == INTERNAL_STORAGE) {
                    focus_storage = INTERNAL_STORAGE;
                    main_page_storage = INTERNAL_STORAGE;
                } else {
                    focus_storage = INTERNAL_STORAGE;
                    main_page_storage = BOTH_STORAGE;
                }
            }
        }
        sdcard_path_header = intent.getStringExtra("sdcard_path");

        SDCARD_DIR = new File(sdcard_path_header + Appdir, intent.getStringExtra("f_path"));
        INTERNAL_DIR = new File(Environment.getExternalStorageDirectory() + Appdir, intent.getStringExtra("f_path"));

        if (focus_storage == EXTERNAL_STORAGE)
            mediaStorageDir = SDCARD_DIR;
        else
            mediaStorageDir = INTERNAL_DIR;

        data= new ArrayList<>();

        folder_path = f_path;
        basePath = f_path;

        path_trace = new ArrayList<>();
        path_trace.add(path_trace.size(), f_path);

   //     Log.d(TAG, "get id: " +id_from_main + " path:" + f_path);


   //     customProgressDialog = new CustomProgressDialog(activity, "처리 중 입니다.");

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

//        check_imgs(mediaStorageDir);

        setContentView(R.layout.viewpager);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new ImagesViewAdapter(getSupportFragmentManager()));

      //  check_imgs();
    }

    private static int check_imgs(File mediaStorageDir) {
   //     Log.d(TAG, "mediaStorageDir " + mediaStorageDir.toString());
        String temp_path = null;
        String recylcer_temp_path = null;
        ArrayList<File> fileArrayList = new ArrayList<>();
        try {
            if (!mediaStorageDir.exists()) {
      //          Log.d(TAG, "directory is not exist " + mediaStorageDir.toString());
            } else {
                temp_path = mediaStorageDir.getPath();
                recylcer_temp_path = basePath;


        //        Log.d(TAG, "check_imgs basePath: " + temp_path);

//                File file = new File(temp_path);

                File lists = mediaStorageDir;
                File[] files = lists.listFiles();
/*
                if (img.length <= 0) {
                    Toast.makeText(activity, "사진이 없습니다.", Toast.LENGTH_LONG).show();
                    single_item_id = -1;

                    return -1;
                }

                mImg.clear();


                for (int i = 0; i < img.length; i++) {
                    mImg.add(img[i]);
                //    Log.d(TAG, mImg.get(i));
                }

                Collections.sort(mImg); //sort
                Collections.reverse(mImg); //거꾸로

*/


                if (files.length <= 0) {
    //                Toast.makeText(activity, "사진이 없습니다.", Toast.LENGTH_LONG).show();
                    single_item_id = -1;

                    return -1;
                }

                mImg.clear();

                    for (int j = 0; j < files.length; j++) {
                            fileArrayList.add(files[j]);
                //            Log.d(TAG,"fileLists: " + fileArrayList.get(j));
                    }

                    Collections.sort(fileArrayList, comparable);
          //          Collections.reverse(fileArrayList);

                    for (int k = 0; k < fileArrayList.size(); k++) {
                        mImg.add(fileArrayList.get(k).getName());
                        /*
                        if (!fileArrayList.get(k).getName().contains(".")) {
                            if (mImg.size() == 0) {
                                mImg.add(fileArrayList.get(k).getName());
                            } else {
                                mImg.add(0, fileArrayList.get(k).getName());
                            }
                        } else {
                            mImg.add(fileArrayList.get(k).getName());
                        }
*/
                    }

                for (int k = 0; k < fileArrayList.size(); k++) {
                    if (!fileArrayList.get(k).getName().contains(".")) {
                        mImg.add(0, fileArrayList.get(k).getName());
                        mImg.remove(k + 1);
                    }
                }


                basePath = temp_path;
                recyclerPath = recylcer_temp_path;

                DisplayMetrics dm = activity.getResources().getDisplayMetrics();
                display_width = dm.widthPixels;
                display_height = dm.heightPixels;
           //     Log.d(TAG, "Display  (width:" + display_width + " height:" + display_height + ")");

                asyncRunning = false;

                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    private void setAds() {
        AdRequest adRequest = new AdRequest.Builder()
                //        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //         .addTestDevice("4AA0ED293726F23BB7F6456FE3094FAF")
                .build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
     //   Log.d(TAG, "onItemClick"+ adapterView.getId());
    }

    @Override
    public void onClick(View view) {
     //   Log.d(TAG, "onClick");
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
    //    Log.d(TAG, "onTouch");
        return false;
    }

    private class ImagesViewAdapter extends FragmentStatePagerAdapter {

        public ImagesViewAdapter(FragmentManager fm) {
            super(fm);

        }

        /**
         * 실제 뷰페이저에서 보여질 fragment를 반환.
         * * 일반 아답터(갤러리, 리스트뷰 등)의 getView와 같은 역할
         * * @param position - 뷰페이저에서 보여저야할 페이지 값( 0부터 )
         * * @return 보여질 fragment
         */

        @Override
        public Fragment getItem(int position) {

            return ArrayFragment.newInstance(position); //todo
        }

        @Override
        public int getCount() {
            return COUNT;
        } //todo

    }

    private static View v;
    public static class ArrayFragment extends Fragment {
        int mPosition;    //뷰 페이저의 페이지 값

        // fragment 생성하는 static 메소드 뷰페이저의 position을 값을 받는다.
        static ArrayFragment newInstance(int position) {

            ArrayFragment f = new ArrayFragment();

            //객체 생성
            Bundle args = new Bundle();

            //해당 fragment에서 사용될 정보 담을 번들 객체
            args.putInt("position", position); //포지션 값을 저장
            f.setArguments(args); //fragment에 정보 전달.
            return f; //fragment 반환
        }

        //fragment가 만들어질 때
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mPosition = getArguments() != null ? getArguments().getInt("position") : 0;
            // 뷰페이저의 position값을 넘겨 받음

        }

        //fragment의 UI 생성
        @Override
        public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            v = inflater.inflate(R.layout.layout1 + mPosition, container, false);

      //      Log.d(TAG, "onCreateView: " + mPosition);
            //미리 알고 있는 레이아웃을 inflate 한다.
            if (mPosition == 0) { //첫번째 layout
                mGridView = (GridView) v.findViewById(R.id.gridView1);
      //          mGridView.getExpanded(true);

                mlayout = (LinearLayout) v.findViewById(R.id.title_text);
                textView_title = v.findViewById(R.id.layout1_textview);
                textView_title.setText(f_path);

                textView_title_2 = v.findViewById(R.id.layout1_textview_2);

                if (focus_storage == INTERNAL_STORAGE) {
                    textView_title_2.setText("내장");
                } else if (focus_storage == EXTERNAL_STORAGE) {
                    textView_title_2.setText("외장");
                }
                final Activity activity;
                activity = getActivity();

                menuMultipleActions = (FloatingActionsMenu) v.findViewById(R.id.multiple_actions);
/*
                menuMultipleActions.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "menu clicked");
                    }
                });
                */
/*
                actionC = (FloatingActionButton) v.findViewById(R.id.action_c);
                actionC.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            ImagesView run = new ImagesView();
                            run.run_activiy(activity, 0, UPDATE_CTRL);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                actionC.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return false;
                    }
                });
*/

                actionD = (FloatingActionButton) v.findViewById(R.id.action_d);
                actionD.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent_gallery = new Intent();
                        intent_gallery.setAction(Intent.ACTION_PICK);
                        //           intent_gallery.setType("image/*");
                        intent_gallery.setType("video/*");
                        //intent_gallery.setType("video/*");
                        intent_gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent_gallery.setData(MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        Intent chooser = intent_gallery.createChooser(intent_gallery, "동영상 가져오기");

                        activity.startActivityForResult(chooser, REQUSET_GALLERY);

                        menuMultipleActions.collapse();
                    }
                });

                actionB = (FloatingActionButton) v.findViewById(R.id.action_b);
                actionB.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent_gallery = new Intent();
                        intent_gallery.setAction(Intent.ACTION_PICK);
             //           intent_gallery.setType("image/*");
                        intent_gallery.setType("image/*");
                        //intent_gallery.setType("video/*");
                        intent_gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent_gallery.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        Intent chooser = intent_gallery.createChooser(intent_gallery, "사진 가져오기");

                        activity.startActivityForResult(chooser, REQUSET_GALLERY);

                        menuMultipleActions.collapse();
                    }
                });

                actionC = (FloatingActionButton) v.findViewById(R.id.action_c);
                if (main_page_storage != BOTH_STORAGE) {
                            actionC.setVisibility(View.INVISIBLE);
                }
                actionC.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (folder_depth > 0) {
                                return;
                            }
                            if (main_page_storage != BOTH_STORAGE) {
                        //        actionC.setVisibility(View.INVISIBLE);
                                return;
                            }

                            ArrayList<File> fileArrayList = new ArrayList<>();

                            if (focus_storage == INTERNAL_STORAGE) {
                                focus_storage = EXTERNAL_STORAGE;
                                mlayout.setBackgroundResource(R.color.cornflower_lilac);
                                mlayout_2.setBackgroundResource(R.color.cornflower_lilac);
                        //        textView_list.setBackgroundResource(R.color.cornflower_lilac);
                                actionC.setTitle("내장메모리 사진 보기");
                                textView_title_2.setText("외장");
                            } else {
                                focus_storage = INTERNAL_STORAGE;
                                mlayout.setBackgroundResource(R.color.photo_map_color);
                                mlayout_2.setBackgroundResource(R.color.photo_map_color);
                           //     textView_list.setBackgroundResource(R.color.photo_map_color);
                                actionC.setTitle("외장메모리 사진보기");
                                textView_title_2.setText("내장");
                            }

                            if (focus_storage == EXTERNAL_STORAGE) {
                                /*
                                FileController fileController = new FileController();
                                DocumentFile pickedDir = fileController.folder_checker(activity, Appdir + File.separator + f_path, fileController.SEARCH_DIR);

                                String path = pickedDir.getUri().getPath();
                                String storage_path = fileController.return_External_path(activity);
                                Log.d(TAG, "path: " + path
                                        + "\nexternalDir: " + storage_path);

                                List<String> split_str = new ArrayList<>();
                                split_str = Arrays.asList(path.split(":"));

                                String sdcard_full_path = storage_path + File.separator + split_str.get(split_str.size() - 1);

                                List<String> split_str_path = new ArrayList<>();
                                split_str_path = Arrays.asList(sdcard_full_path.split(Appdir));

                                for (int i = 0 ; i < split_str_path.size(); i++) {
                                    Log.d(TAG, "split_str: " + split_str_path.get(i));
                                }

                                if (split_str_path.size() > 0)
                                    sdcard_path_header = split_str_path.get(0);
*/
              //                  Log.d(TAG, "file_path: " + SDCARD_DIR.getPath());

                                File lists = SDCARD_DIR;
                                File[] files = lists.listFiles();

                                mediaStorageDir = SDCARD_DIR;
                                basePath = mediaStorageDir.getPath();
/*
                                if (files != null) {
                                    for (int i = 0; i < files.length; i++) {
                                        Log.d(TAG, "files: " + files[i].getName());
                                    }
                                }
*/

                                check_imgs(mediaStorageDir);
                                /*
                                mImg.clear();


                                for (int j = 0; j < files.length; j++) {
                                    fileArrayList.add(files[j]);
                                }

                                Collections.sort(fileArrayList, comparable);
                        //        Collections.reverse(fileArrayList);

                                for (int k = 0; k < fileArrayList.size(); k++) {
                                    if (!fileArrayList.get(k).getName().contains(".")) {
                                        if (mImg.size() == 0) {
                                            mImg.add(fileArrayList.get(k).getName());
                                        } else {
                                            mImg.add(0, fileArrayList.get(k).getName());
                                        }
                                    } else {
                                        mImg.add(fileArrayList.get(k).getName());
                                    }

                                }
                                */
                                /*

                                for (int i = 0; i < files.length; i++) {
                                    mImg.add(files[i].getName());
                                    //    Log.d(TAG, mImg.get(i));
                                }

                                Collections.sort(mImg); //sort
                                Collections.reverse(mImg); //거꾸로
*/
                                for (int i = 0; i < mImg.size(); i++) {
                                    if (mImg.get(i).contains(".")) {
                                        metalList.add(mImg.get(i));
                                        metalImageList.add(sdcard_path_header + Appdir  + File.separator + f_path + File.separator + mImg.get(i));
                                    }
                                }






                            } else if (focus_storage == INTERNAL_STORAGE) {
                        //        Log.d(TAG, "file_path: " + INTERNAL_DIR.getPath());
                                File lists = INTERNAL_DIR;
                                File[] files = lists.listFiles();

                                mediaStorageDir = INTERNAL_DIR;
                                basePath = INTERNAL_DIR.getPath();
/*
                                if (files != null) {
                                    for (int i = 0; i < files.length; i++) {
                                        Log.d(TAG, "files: " + files[i].getName());
                                    }
                                }
*/

                            check_imgs(mediaStorageDir);
                            /*
                                mImg.clear();


                                for (int j = 0; j < files.length; j++) {
                                    fileArrayList.add(files[j]);
                                }

                                Collections.sort(fileArrayList, comparable);
                        //        Collections.reverse(fileArrayList);

                                for (int k = 0; k < fileArrayList.size(); k++) {
                                    if (!fileArrayList.get(k).getName().contains(".")) {
                                        if (mImg.size() == 0) {
                                            mImg.add(fileArrayList.get(k).getName());
                                        } else {
                                            mImg.add(0, fileArrayList.get(k).getName());
                                        }
                                    } else {
                                        mImg.add(fileArrayList.get(k).getName());
                                    }

                                }
                                */
/*
                                for (int i = 0; i < files.length; i++) {
                                    mImg.add(files[i].getName());
                                    //    Log.d(TAG, mImg.get(i));
                                }

                                Collections.sort(mImg); //sort
                                Collections.reverse(mImg); //거꾸로
*/
                                for (int i = 0; i < mImg.size(); i++) {
                                    if (mImg.get(i).contains(".")) {
                                        metalList.add(mImg.get(i));
                                        if (focus_storage == INTERNAL_STORAGE)
                                            metalImageList.add(basePath + File.separator + mImg.get(i));
                                    }
                                }


                            }

                            //공통부
                                ImagesView run = new ImagesView();
                                run.run_activiy(activity, 0, UPDATE_CTRL);
                                run.run_activiy(activity, 0, UPDATE_CTRL_RECYCLER);
                                run.run_activiy(activity, 0, CLEAR_ITEM_CTRL);


                        } catch (Exception e) {
                            e.printStackTrace();
                            mImg.clear();
                            metalList.clear();
                            metalImageList.clear();

                            Toast.makeText(getActivity(), "경로가 없습니다.", Toast.LENGTH_LONG).show();

                            //공통부
                            ImagesView run = new ImagesView();
                            run.run_activiy(activity, 0, UPDATE_CTRL);
                            run.run_activiy(activity, 0, UPDATE_CTRL_RECYCLER);
                            run.run_activiy(activity, 0, CLEAR_ITEM_CTRL);

                            if (focus_storage == EXTERNAL_STORAGE) {
                                mainActivity.set_storage_info(f_path, INTERNAL_STORAGE);
                            } else {
                                mainActivity.set_storage_info(f_path, EXTERNAL_STORAGE);
                            }

                        }

                        menuMultipleActions.collapse();

                    }
                });

/*
                actionC = new FloatingActionButton(getContext());
                actionC.setTitle("숨기기");
                actionC.setSize(FloatingActionButton.SIZE_MINI);
                actionC.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actionB.setVisibility(actionA.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                    }
                });


                menuMultipleActions.addButton(actionC);
*/

                actionA = (FloatingActionButton) v.findViewById(R.id.action_a);
                actionA.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (multi_mode == 0) {
                    //        Toast.makeText(getActivity(), "여러장 선택하기", Toast.LENGTH_LONG).show();

                            if (selectedPositions_metal.size() > 0) {
                                Toast.makeText(getActivity(), "동시에 진행 할 수 없습니다.", Toast.LENGTH_LONG).show();
                                return;
                            }

                            multi_mode = 1;
                            v.setBackgroundColor(Color.WHITE);

                            mlayout.setBackgroundResource(R.color.transparent_blue);
                            mlayout_2.setBackgroundResource(R.color.transparent_blue);
                         //   textView_list.setBackgroundResource(R.color.transparent_blue);
                            actionA.setTitle("여러장 선택하기 취소");
                        } else {
                            /*
                            Toast.makeText(getActivity(), "한장 공유하기", Toast.LENGTH_LONG).show();
                            multi_mode = 0;
                            */
                    //        Toast.makeText(getActivity(), "여러장 선택하기 취소", Toast.LENGTH_LONG).show();
                            multi_mode = 0;
                            v.setBackgroundColor(Color.WHITE);
                            if (focus_storage == INTERNAL_STORAGE) {
                                mlayout.setBackgroundResource(R.color.photo_map_color);
                                mlayout_2.setBackgroundResource(R.color.photo_map_color);
                             //   textView_list.setBackgroundResource(R.color.photo_map_color);
                            } else {
                                mlayout.setBackgroundResource(R.color.cornflower_lilac);
                                mlayout_2.setBackgroundResource(R.color.cornflower_lilac);
                         //       textView_list.setBackgroundResource(R.color.cornflower_lilac);
                            }
                            actionA.setTitle("여러장 선택하기");

                            try {
                                ImagesView run = new ImagesView();
                                run.run_activiy(activity, 0, CLEAR_ITEM_CTRL);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                            menuMultipleActions.collapse();
                    }
                });

                mAdView = (AdView) v.findViewById(R.id.adView_2);

                try {
                    ImagesView run = new ImagesView();
                    run.run_activiy(activity, 0, SET_ADS);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (mPosition == 1) { //두번째 layout
                //버튼 setting
              //  v.findViewById(R.id.btn3).setOnClickListener(mButtonClick);
             //   v.findViewById(R.id.btn4).setOnClickListener(mButtonClick);

                //cardview setting
//                metalviewPager = (MetalRecyclerViewPager) v.findViewById(R.id.layout2_viewPager);
                DisplayMetrics dm = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
 //               metalList = Arrays.asList("item1", "item2", "item3", "item4"); //todo
                textView_list = v.findViewById(R.id.layout2_textview);
                textView_list_2 = v.findViewById(R.id.layout2_textview_2);
                mlayout_2 = (LinearLayout) v.findViewById(R.id.textview_layout2_main);
                for (int i = 0; i < mImg.size(); i++) {
                    if (mImg.get(i).contains(".")) {
                        metalList.add(mImg.get(i));
                        if (focus_storage == INTERNAL_STORAGE) {
                            metalImageList.add(basePath + File.separator + mImg.get(i));
                            textView_list_2.setText("내장");
                            recycler_storage = INTERNAL_STORAGE;
                        } else {
                            metalImageList.add(sdcard_path_header + Appdir + File.separator + f_path + File.separator + mImg.get(i));
                            textView_list_2.setText("외장");
                            recycler_storage = EXTERNAL_STORAGE;
                        }
                    }
                }

                RecyclerView.ItemDecoration decoration = ItemDecorations.horizontal(getContext())
                        .first(R.drawable.shape_decoration_green_w_8)
                        .type(DemoViewType.PAGE.ordinal(), R.drawable.shape_decoration_red_w_8)
                        .last(R.drawable.shape_decoration_flush_orange_w_8)
                        .create();

                // normal
                mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_horizontal);
                mRecyclerView.setLayoutManager(
                        new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

                if (mRecyclerView != null) {
                    RecyclerBinderAdapter<DemoSectionType, DemoViewType> normalAdapter = initAdapter(1);
                    mRecyclerView.setAdapter(normalAdapter);
                    mRecyclerView.addItemDecoration(decoration);
                }

         //       Log.d(TAG, " metalList size " + metalList.size());
     //           fullMetalAdapter = new FullMetalAdapter(activity, dm, metalList, metalImageList);


                int marker_choice = 0;
                marker_choice = mainActivity.get_marker_choice();

                //listview setting
       //         Log.d(TAG, " mMapinfo size: " + mapInfos.size());


                listView= (ListView) v.findViewById(R.id.layout2_listview);
                try {
                    for (int i = 0; i < mapInfos.size(); i++) {

                        File[] files;
                        if (focus_storage == INTERNAL_STORAGE)
                            files = mainActivity.check_files_list(mapInfos.get(i).getfolder(), INTERNAL_STORAGE);
                        else
                            files = mainActivity.check_files_list(mapInfos.get(i).getfolder(), EXTERNAL_STORAGE);
                        int cnt = 0;

                        if (files == null) {
                  //          Log.d(TAG, "files null");
                        } else {
                            for (int j = 0; j < files.length; j++) {
                                if (!files[j].getName().contains(".")) {
                                    cnt++;
                                }
                            }
                        }

                        Listviewitem items = new Listviewitem(Integer.toString(marker_choice), mapInfos.get(i).getdata(), mapInfos.get(i).getfolder(), Integer.toString(Integer.parseInt(mapInfos.get(i).getreserved()) - cnt),
                                Integer.parseInt(mapInfos.get(i).getFav()), Integer.parseInt(mapInfos.get(i).getStorage()));
                        data.add(items);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
            }
            return v;
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            if (activity != null) {

          //       Log.d(TAG,"onActivityCreate");
                // 커스텀 아답타 생성



                adapter = new MyAdapter(activity,
                        R.layout.row,       // GridView 항목의 레이아웃 row.xml
                        mImg);    // 데이터

                if (mGridView != null) {
        //            Log.d(TAG, "mGridView is not null");
                    mGridView.setAdapter(adapter);
                    mGridView.setOnItemClickListener(mItemClick);
                    mGridView.setOnItemLongClickListener(mItemLongClick);
                }
//                if (metalviewPager != null) {
                    /*
                    metalviewPager.setAdapter(fullMetalAdapter);

                    try {
                        ImagesView run = new ImagesView();
                        run.run_activiy(activity, 0, 7);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
*/
   //             }
                if (listView != null) {
                    listadapter = new ListviewAdapter(getContext(), R.layout.listview_item, data);
                    listView.setAdapter(listadapter);

                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                            if (multi_mode == 0 || (selectedPositions.size() == 0)) {
                                /* To Do */
                                if (selectedPositions_metal.size() > 0) {

                                    vibrator.vibrate(100);

                //                    Log.d(TAG, "longClick path: " + mapInfos.get(i).getfolder());

                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                    alertDialog.setTitle("이 폴더로 사진을 이동하겠습니까?");
                                    alertDialog.setMessage("'" + mapInfos.get(i).getfolder() + "'");

                                    alertDialog.setPositiveButton("복사", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {

                                                //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);

                                     //          ImagesView run = new ImagesView();
                                      //          run.run_activiy(activity, i, MULTI_FILE_COPY_CTRL_RECYCLER);


                                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                                alertDialog.setTitle("저장소 확인");
                                                alertDialog.setMessage("'" + mapInfos.get(i).getfolder() + "'");


                                                if (mapInfos.get(i).getfolder().equals(recyclerBasePath) && Integer.parseInt(mapInfos.get(i).getStorage()) == BOTH_STORAGE) {


                                                    if (recycler_storage == INTERNAL_STORAGE) {
                                                        alertDialog.setPositiveButton("외장", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                try {

                                                                    //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);
                                                                    to_recycler_storage = EXTERNAL_STORAGE;
                                                                    ImagesView run = new ImagesView();
                                                                    run.run_activiy(activity, i, MULTI_FILE_COPY_CTRL_RECYCLER);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }
                                                        });
                                                    } else {
                                                        alertDialog.setNegativeButton("내장", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                try {

                                                                    //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);
                                                                    to_recycler_storage = INTERNAL_STORAGE;
                                                                    ImagesView run = new ImagesView();
                                                                    run.run_activiy(activity, i, MULTI_FILE_COPY_CTRL_RECYCLER);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }
                                                        });
                                                    }
                                                } else {


                                                    alertDialog.setPositiveButton("내장", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            try {

                                                                //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);
                                                                to_recycler_storage = INTERNAL_STORAGE;
                                                                ImagesView run = new ImagesView();
                                                                run.run_activiy(activity, i, MULTI_FILE_COPY_CTRL_RECYCLER);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    });

                                                    alertDialog.setNegativeButton("외장", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            try {

                                                                //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);
                                                                to_recycler_storage = EXTERNAL_STORAGE;
                                                                ImagesView run = new ImagesView();
                                                                run.run_activiy(activity, i, MULTI_FILE_COPY_CTRL_RECYCLER);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    });

                                                }



                                                // Showing Alert Message
                                                if (Integer.parseInt(mapInfos.get(i).getStorage()) == BOTH_STORAGE) {
                                                    alertDialog.show();
                                                } else {
                                                              ImagesView run = new ImagesView();
                                                              run.run_activiy(activity, i, MULTI_FILE_COPY_CTRL_RECYCLER);
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });

                                    alertDialog.setNegativeButton("이동", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {

                            //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);

                  //                              ImagesView run = new ImagesView();
                   //                             run.run_activiy(activity, i, MULTI_FILE_MOVE_CTRL_RECYCLER);

                                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                                alertDialog.setTitle("저장소 확인");
                                                alertDialog.setMessage("'" + mapInfos.get(i).getfolder() + "'");





                                                if (mapInfos.get(i).getfolder().equals(recyclerBasePath) && Integer.parseInt(mapInfos.get(i).getStorage()) == BOTH_STORAGE) {


                                                    if (recycler_storage == INTERNAL_STORAGE) {
                                                        alertDialog.setPositiveButton("외장", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                try {

                                                                    //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);
                                                                    to_recycler_storage = EXTERNAL_STORAGE;
                                                                    ImagesView run = new ImagesView();
                                                                    run.run_activiy(activity, i, MULTI_FILE_MOVE_CTRL_RECYCLER);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }
                                                        });
                                                    } else {
                                                        alertDialog.setNegativeButton("내장", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                try {

                                                                    //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);
                                                                    to_recycler_storage = INTERNAL_STORAGE;
                                                                    ImagesView run = new ImagesView();
                                                                    run.run_activiy(activity, i, MULTI_FILE_MOVE_CTRL_RECYCLER);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }
                                                        });
                                                    }
                                                } else {


                                                    alertDialog.setPositiveButton("내장", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            try {

                                                                //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);
                                                                to_recycler_storage = INTERNAL_STORAGE;
                                                                ImagesView run = new ImagesView();
                                                                run.run_activiy(activity, i, MULTI_FILE_MOVE_CTRL_RECYCLER);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    });

                                                    alertDialog.setNegativeButton("외장", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            try {

                                                                //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);
                                                                to_recycler_storage = EXTERNAL_STORAGE;
                                                                ImagesView run = new ImagesView();
                                                                run.run_activiy(activity, i, MULTI_FILE_MOVE_CTRL_RECYCLER);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    });

                                                }



                                     /*

                                                alertDialog.setPositiveButton("내장", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        try {

                                                            //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);
                                                            to_recycler_storage = INTERNAL_STORAGE;
                                                            ImagesView run = new ImagesView();
                                                            run.run_activiy(activity, i, MULTI_FILE_MOVE_CTRL_RECYCLER);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                });

                                                alertDialog.setNegativeButton("외장", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        try {

                                                            //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);
                                                            to_recycler_storage = EXTERNAL_STORAGE;
                                                            ImagesView run = new ImagesView();
                                                            run.run_activiy(activity, i, MULTI_FILE_MOVE_CTRL_RECYCLER);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                });
*/
                                                // Showing Alert Message
                                                if (Integer.parseInt(mapInfos.get(i).getStorage()) == BOTH_STORAGE) {
                                                    alertDialog.show();
                                                } else {
                                                    ImagesView run = new ImagesView();
                                                    run.run_activiy(activity, i, MULTI_FILE_MOVE_CTRL_RECYCLER);
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });

                                    alertDialog.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });

                                    // Showing Alert Message
                                    if (Integer.parseInt(mapInfos.get(i).getStorage()) != BOTH_STORAGE && recyclerBasePath.equals(mapInfos.get(i).getfolder())) {
                                        //not show
                                    } else {
                                        alertDialog.show();
                                    }


                                }

                            } else {
                                if (selectedPositions_metal.size() > 0) {
                                    Toast.makeText(getContext(), "동시에 진행 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (selectedPositions.size() == 0) {
                                        Toast.makeText(getContext(), "잘못된 접근 입니다.", Toast.LENGTH_SHORT).show();
                                        return true;
                                    }

                                    //정렬
                                    //             Collections.sort(selectedPositions);
                                    //             Collections.reverse(selectedPositions);

                                    vibrator.vibrate(100);

                     //               Log.d(TAG, "longClick path: " + mapInfos.get(i).getfolder());

                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                    alertDialog.setTitle("이 폴더로 사진을 이동하겠습니까?");
                                    alertDialog.setMessage("'" + mapInfos.get(i).getfolder() + "'");

                                    alertDialog.setPositiveButton("복사", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            try {
                                     //          ImagesView run = new ImagesView();
                                      //          run.run_activiy(activity, i, MULTI_FILE_COPY_CTRL);






                                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                                alertDialog.setTitle("저장소 확인");
                                                alertDialog.setMessage("'" + mapInfos.get(i).getfolder() + "'");



                                                if (mapInfos.get(i).getfolder().equals(recyclerBasePath) && Integer.parseInt(mapInfos.get(i).getStorage()) == BOTH_STORAGE) {

                                                    if (focus_storage == INTERNAL_STORAGE) {
                                                        alertDialog.setPositiveButton("외장", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                try {

                                                                    //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);
                                                                    to_recycler_storage = EXTERNAL_STORAGE;
                                                                    ImagesView run = new ImagesView();
                                                                    run.run_activiy(activity, i, MULTI_FILE_COPY_CTRL);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }
                                                        });
                                                    } else {
                                                        alertDialog.setNegativeButton("내장", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                try {

                                                                    //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);
                                                                    to_recycler_storage = INTERNAL_STORAGE;
                                                                    ImagesView run = new ImagesView();
                                                                    run.run_activiy(activity, i, MULTI_FILE_COPY_CTRL);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }
                                                        });
                                                    }
                                                } else {


                                                    alertDialog.setPositiveButton("내장", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            try {

                                                                //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);
                                                                to_recycler_storage = INTERNAL_STORAGE;
                                                                ImagesView run = new ImagesView();
                                                                run.run_activiy(activity, i, MULTI_FILE_COPY_CTRL);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    });

                                                    alertDialog.setNegativeButton("외장", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            try {

                                                                //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);
                                                                to_recycler_storage = EXTERNAL_STORAGE;
                                                                ImagesView run = new ImagesView();
                                                                run.run_activiy(activity, i, MULTI_FILE_COPY_CTRL);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    });

                                                }










                                                      /*



                                                alertDialog.setPositiveButton("내장", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        try {

                                                            //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);
                                                            to_recycler_storage = INTERNAL_STORAGE;
                                                            ImagesView run = new ImagesView();
                                                            run.run_activiy(activity, i, MULTI_FILE_COPY_CTRL);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                });

                                                alertDialog.setNegativeButton("외장", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        try {

                                                            //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);
                                                            to_recycler_storage = EXTERNAL_STORAGE;
                                                            ImagesView run = new ImagesView();
                                                            run.run_activiy(activity, i, MULTI_FILE_COPY_CTRL);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                });
*/
                                                // Showing Alert Message
                                                if (Integer.parseInt(mapInfos.get(i).getStorage()) == BOTH_STORAGE) {
                                                    alertDialog.show();
                                                } else {
                                                    ImagesView run = new ImagesView();
                                                    run.run_activiy(activity, i, MULTI_FILE_COPY_CTRL);
                                                }










                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                                    alertDialog.setNegativeButton("이동", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            try {
                                 //               ImagesView run = new ImagesView();
                                  //              run.run_activiy(activity, i, MULTI_FILE_MOVE_CTRL);





                                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                                alertDialog.setTitle("저장소 확인");
                                                alertDialog.setMessage("'" + mapInfos.get(i).getfolder() + "'");








                                                if (mapInfos.get(i).getfolder().equals(recyclerBasePath) && Integer.parseInt(mapInfos.get(i).getStorage()) == BOTH_STORAGE) {


                                                    if (focus_storage == INTERNAL_STORAGE) {
                                                        alertDialog.setPositiveButton("외장", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                try {

                                                                    //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);
                                                                    to_recycler_storage = EXTERNAL_STORAGE;
                                                                    ImagesView run = new ImagesView();
                                                                    run.run_activiy(activity, i, MULTI_FILE_MOVE_CTRL);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }
                                                        });
                                                    } else {
                                                        alertDialog.setNegativeButton("내장", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                try {

                                                                    //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);
                                                                    to_recycler_storage = INTERNAL_STORAGE;
                                                                    ImagesView run = new ImagesView();
                                                                    run.run_activiy(activity, i, MULTI_FILE_MOVE_CTRL);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }
                                                        });
                                                    }
                                                } else {


                                                    alertDialog.setPositiveButton("내장", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            try {

                                                                //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);
                                                                to_recycler_storage = INTERNAL_STORAGE;
                                                                ImagesView run = new ImagesView();
                                                                run.run_activiy(activity, i, MULTI_FILE_MOVE_CTRL);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    });

                                                    alertDialog.setNegativeButton("외장", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            try {

                                                                //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);
                                                                to_recycler_storage = EXTERNAL_STORAGE;
                                                                ImagesView run = new ImagesView();
                                                                run.run_activiy(activity, i, MULTI_FILE_MOVE_CTRL);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    });

                                                }













/*




                                                alertDialog.setPositiveButton("내장", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        try {

                                                            //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);
                                                            to_recycler_storage = INTERNAL_STORAGE;
                                                            ImagesView run = new ImagesView();
                                                            run.run_activiy(activity, i, MULTI_FILE_MOVE_CTRL);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                });

                                                alertDialog.setNegativeButton("외장", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        try {

                                                            //                    Log.d(TAG, "recyclerBasePath: " + recyclerBasePath);
                                                            to_recycler_storage = EXTERNAL_STORAGE;
                                                            ImagesView run = new ImagesView();
                                                            run.run_activiy(activity, i, MULTI_FILE_MOVE_CTRL);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                });
*/
                                                // Showing Alert Message
                                                if (Integer.parseInt(mapInfos.get(i).getStorage()) == BOTH_STORAGE) {
                                                    alertDialog.show();
                                                } else {
                                                    ImagesView run = new ImagesView();
                                                    run.run_activiy(activity, i, MULTI_FILE_MOVE_CTRL);
                                                }














                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });

                                    alertDialog.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });

                                    // Showing Alert Message
                                    if (Integer.parseInt(mapInfos.get(i).getStorage()) != BOTH_STORAGE && recyclerBasePath.equals(mapInfos.get(i).getfolder())) {
                                        //not show
                                    } else {
                                        alertDialog.show();
                                    }
                                }
                            }

                            return true;
                        }
                    });

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                            update_recycler_view(getContext(), i);



                                if (item_pos != i) {
                                    toggler_recycler_item = 0;
                                }

                            item_pos = i;

                            if (selectedPositions_metal.size() != 0)
                                selectedPositions_metal.clear();
                        }
                    });
                }

                if (mRecyclerView != null) {

                }

                adapter.notifyDataSetChanged();

            }
        }
    }
    static int toggler_recycler_item = 0;
    static void update_recycler_view (Context context, int i) {


        if (v == null)
            return;

        ArrayList<File> fileArrayList = new ArrayList<>();

        try {
     //       Log.d(TAG, "onItemClick path: " + mapInfos.get(i).getfolder());
            File fileDir = null;

            if (Integer.parseInt(mapInfos.get(i).getStorage()) == EXTERNAL_STORAGE) {
                fileDir = new File(sdcard_path_header + Appdir, mapInfos.get(i).getfolder());
            } else if (Integer.parseInt(mapInfos.get(i).getStorage()) == INTERNAL_STORAGE) {
                fileDir = new File(Environment.getExternalStorageDirectory() + Appdir, mapInfos.get(i).getfolder());
            } else {
                if (toggler_recycler_item == 0) {
                    fileDir = new File(Environment.getExternalStorageDirectory() + Appdir, mapInfos.get(i).getfolder());
                } else {
                    fileDir = new File(sdcard_path_header + Appdir, mapInfos.get(i).getfolder());
                }

            }
            recyclerPath = fileDir.getPath();
            recyclerBasePath = mapInfos.get(i).getfolder();


            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

            File[] files;
            if (Integer.parseInt(mapInfos.get(i).getStorage()) == EXTERNAL_STORAGE) {
                files = mainActivity.check_files_list(mapInfos.get(i).getfolder(), EXTERNAL_STORAGE);
                recycler_storage = EXTERNAL_STORAGE;
            } else if (Integer.parseInt(mapInfos.get(i).getStorage()) == INTERNAL_STORAGE) {
                files = mainActivity.check_files_list(mapInfos.get(i).getfolder(), INTERNAL_STORAGE);
                recycler_storage = INTERNAL_STORAGE;
            } else {
                if (toggler_recycler_item == 0) {
                    files = mainActivity.check_files_list(mapInfos.get(i).getfolder(), INTERNAL_STORAGE);
                    recycler_storage = INTERNAL_STORAGE;
                    toggler_recycler_item = 1;
                } else {
                    files = mainActivity.check_files_list(mapInfos.get(i).getfolder(), EXTERNAL_STORAGE);
                    recycler_storage = EXTERNAL_STORAGE;
                    toggler_recycler_item = 0;
                }
            }

            metalImageList.clear();
            metalList.clear();

            if (files == null) {
    //            Log.d(TAG, "files null");
            } else {


                for (int j = 0; j < files.length; j++) {
                    if (files[j].getName().contains(".")) {
                        fileArrayList.add(files[j]);
                    }
                }

                Collections.sort(fileArrayList, comparable);
     //           Collections.reverse(fileArrayList);

                for (int k = 0; k < fileArrayList.size(); k++) {
                    metalList.add(fileArrayList.get(k).getName());
                    metalImageList.add(fileDir.getPath() + File.separator + fileArrayList.get(k).getName());
      //              Log.d(TAG, "after "+fileArrayList.get(k).getName() + ": " + Long.toString(fileArrayList.get(k).lastModified()));
                }
            }


            textView_list.setText(recyclerBasePath);
            if (recycler_storage == INTERNAL_STORAGE)
                textView_list_2.setText("내장");
            else
                textView_list_2.setText("외장");
/*
            Collections.sort(metalImageList);
            Collections.reverse(metalImageList);
            Collections.sort(metalList);
            Collections.reverse(metalList);
*/
            //                    fullMetalAdapter = new FullMetalAdapter(activity, dm, metalList, metalImageList);
            //                   metalviewPager.setAdapter(fullMetalAdapter);

/*
        RecyclerView.ItemDecoration decoration = ItemDecorations.horizontal(context)
                //       .first(R.drawable.shape_decoration_green_w_8)
                .type(DemoViewType.PAGE.ordinal(), R.drawable.shape_decoration_red_w_8)
                //        .last(R.drawable.shape_decoration_flush_orange_w_8)
                .create();
*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        // normal
        try {
            mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_horizontal);
            if (mRecyclerView != null) {
                mRecyclerView.setLayoutManager(
                        new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                RecyclerBinderAdapter<DemoSectionType, DemoViewType> normalAdapter = initAdapter(2);
                mRecyclerView.setAdapter(normalAdapter);
                //               recyclerView.addItemDecoration(decoration);
            }
        } catch (Exception e) {
            Toast.makeText(context, "카드 사진 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }
        try {
            ImagesView run = new ImagesView();
            run.run_activiy(activity, 0, UPDATE_CTRL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Comparator<File> comparable = new Comparator<File>() {
        @Override
        public int compare(File s1, File s2) {

            if (s1.lastModified() < s2.lastModified()) {
                return 1;
            }

            return -1;
        }
    };

    @NonNull
    private static RecyclerBinderAdapter<DemoSectionType, DemoViewType> initAdapter(int control) {
        RecyclerBinderAdapter<DemoSectionType, DemoViewType> adapter = new RecyclerBinderAdapter<>();
        List<RecyclerBinder<DemoViewType>> demoBinderList = new ArrayList<>();

        //todo
    //    Log.d(TAG, "RecyclerBinder");
        if (metalImageList != null) {
            for (int i = 0; i < metalImageList.size(); i++) {
                demoBinderList.add(new PageBinder(activity, metalImageList.get(i)));
            }
        }

     //   Collections.sort(metalImageList);
     //   Collections.reverse(metalImageList);

        if (control == 1) {
            for (RecyclerBinder<DemoViewType> binder : demoBinderList) {
                adapter.add(DemoSectionType.ITEM, binder);
            }
        } else if (control == 2) {
     //       Log.d(TAG, "removeAll");
            adapter.removeAll(DemoSectionType.ITEM);
            for (RecyclerBinder<DemoViewType> binder : demoBinderList) {
                adapter.add(DemoSectionType.ITEM, binder);
            }
        }

        return adapter;
    }

//todo mImage
    //custom view and convert view 문제 확인

    static class MyAdapter extends BaseAdapter {
        Context context;
        int layout;
        List<String> mImg;
        LayoutInflater inf;

        public MyAdapter(Context context, int layout, List<String> mImg) {
            this.context = context;
            this.layout = layout;
            this.mImg = mImg;
            inf = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mImg.size();
        }

        @Override
        public Object getItem(int position) {
            return mImg.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
/*

            if (convertView == null) {
                convertView = inf.inflate(layout, parent, false);
            }

            ImageView iv = (ImageView) convertView.findViewById(R.id.imageView1);

            if (asyncRunning == false) {
                final WorkerTask task = new WorkerTask(this.context, mImg.size(), iv);
                task.execute(this.context, position, iv);
                asyncRunning = true;
            }

            count++;

            GlideApp
                    .with(this.context)
                    .load(basePath + File.separator + mImg.get(position))
                    .centerCrop()
                    .override(display_width / 4, display_width / 4)
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.black_color)
                    .into(iv);


            if (mCheckThreadRunning == false) {
                mCheckThreadRunning = true;
                startThread();
            }
            //      Log.d(TAG, "position: " + position);
            return convertView;
*/

            customView = (convertView == null) ?
                    new CustomView(context, layout) : (CustomView) convertView;

            customView.display(selectedPositions.contains(position), position);

            if (asyncRunning == false) {
       //         final WorkerTask task = new WorkerTask(this.context, mImg.size(), iv);
       //         task.execute(this.context, position, iv);
                asyncRunning = true;
            }

            count++;

            if (mCheckThreadRunning == false) {
                mCheckThreadRunning = true;
                startThread();
            }

       //     Log.d(TAG, "position: " + position);

            return customView;

        }
    }

    public static class CustomView extends FrameLayout {
        MemoDBHelper memoDBHelper = new MemoDBHelper(getContext());
        SQLiteDatabase db = null;
        AspectRatioImageView iv;
        TextView textView0;
        ImageView textView1;
        TextView textView2;
        TextView textView3;
        Context context;
        View view;
        public CustomView(Context context, int layout) {
            super(context);
            this.context = context;

            LayoutInflater.from(context).inflate(layout, this);
            view = findViewById(R.id.row_id);
            iv = (AspectRatioImageView)view.findViewById(R.id.imageView1);
            textView0 = (TextView)view.findViewById(R.id.textview0);
            textView1 = (ImageView) view.findViewById(R.id.textview1);
            textView2 = (TextView)view.findViewById(R.id.textview2);
            textView3 = (TextView)view.findViewById(R.id.textview3);


            Glide.get(context).setMemoryCategory(MemoryCategory.HIGH); //performance를 위한 memory 할당 관련
            db = memoDBHelper.getWritableDatabase();

     //       check_imgs(mediaStorageDir);
        }

        public void display(boolean isSelected, int position) {
   //         Log.d(TAG, "isSelected:" + isSelected + " position:" + position);

            String glide_path = null;
            if (focus_storage == INTERNAL_STORAGE) {
                glide_path = basePath;
            } else if (focus_storage == EXTERNAL_STORAGE) {
                glide_path = sdcard_path_header + Appdir + File.separator + f_path;
            }

            try {
                //ToDo 메모가 이상함 Exif
                try {
                    String id = mainActivity.memoID_translation(glide_path, mImg.get(position));

                    if (id != null) {
                        Cursor c = db.query("memo", new String[]{"id", "info"}, "id='" + id + "'", null, null, null, null);
                        if (c.getCount() != 0) {
                            textView1.setImageResource(R.drawable.pen);
                  //          textView1.setTextColor(Color.WHITE);
                  //          textView1.setText("memo");
                        } else {
                            textView1.setImageResource(R.color.transparent);
                  //          textView1.setText("");
                        }

                        c.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

        //        Glide.get(context).clearMemory();
                if (mImg.get(position).contains(".")) {

                    if (isSelected) {
                        Glide   .with(context)
                                .load(glide_path + File.separator + mImg.get(position))
                                .apply(RequestOptions.bitmapTransform(new MultiTransformation<>(new BlurTransformation(), new CenterCrop()))
                                .priority(Priority.HIGH))
                                .into(iv);
                        if (fileController.file_format_check(mImg.get(position)) == 2) {
                 //           textView0.setImageResource(R.drawable.ic_menu_send);
                           textView0.setTextColor(Color.WHITE);
                            textView0.setText("mov");
                        } else {
                 //           textView0.setImageResource(R.color.transparent);
                            textView0.setTextColor(Color.WHITE);
                            textView0.setText("");
                        }
                    } else {
                        Glide
                                .with(context)
                                .load(glide_path + File.separator + mImg.get(position))
                                .apply(new RequestOptions()
                                .bitmapTransform(new CenterCrop()))
                                .into(iv);


                        if (fileController.file_format_check(mImg.get(position)) == 2) {
                    //        textView0.setImageResource(R.drawable.ic_menu_send);
                            textView0.setTextColor(Color.WHITE);
                            textView0.setText("mov");
                        } else {
                    //        textView0.setImageResource(R.color.transparent);
                           textView0.setTextColor(Color.WHITE);
                            textView0.setText("");
                        }
                    }
                    textView3.setTextColor(Color.BLACK);
                    textView3.setText("");
                } else {
                    Glide
                            .with(context)
                            .load(R.drawable.folder3)
                            .into(iv);
                    textView3.setTextColor(Color.BLACK);
                    textView3.setText(mImg.get(position));
                }

                //         display(isSelected);


                if (isSelected) {
                    //    iv.setBackgroundColor(isSelected? Color.RED : Color.LTGRAY);
                    //     iv.setColorFilter(Color.RED, PorterDuff.Mode.SRC_OVER);
                    //               view.setBackgroundColor(Color.BLUE);
                    textView2.setText("V");

                } else {
                    //          view.setBackgroundColor(Color.WHITE);
                    textView2.setText("");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        //    Log.d(TAG, "pos: " + position);
        }
/*
        public void display(boolean isSelected) {
     //       Log.d(TAG, "isSelected:" + isSelected);

            iv.setBackgroundColor(isSelected? Color.RED : Color.LTGRAY);
        }
        */
    }

    private static class WorkerTask extends AsyncTask {
        private int mLength;
        private ImageView mView;
        private Context mContext;

        public WorkerTask(Context context, int length, ImageView view) {
            mLength = length;
            mView = view;
            mContext = context;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            Intent intent_progress_image = new Intent(mContext, ProgressActivity.class);
            intent_progress_image.putExtra("from", "ImagesView");
            mContext.startActivity(intent_progress_image);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }

    @Override
    protected void onPause() {
      //  Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
   //     Log.d(TAG, "onStop");
        from = "stop";
    //    mImg.clear();
    //    metalList.clear();
    //    metalImageList.clear();
        single_item_id = -1;
        item_pos = -1;
    //    focus_storage = 0;
     //   main_page_storage = 0;
      //  toggler_recycler_item = 0;
      //  recycler_storage = 0;
        super.onStop();
    }

    public boolean imgPosition() {
        return loading_done;
    }

    public int imgLength() {
        return mImg.size();
    }

    public void setEndProgress(int status) {
        if (status == 1) {
      //      Log.d(TAG, "force kill thread");
            kill_thread_call = 1;
        }
    }

    @Override
    protected void onResume() {
  //      Log.d(TAG, "onResume");
        int id = 0;
        if (item_pos > -1)
            id = item_pos;
        else
            id = id_from_main;

        loading_done = false;
        killThread();

            if (check_imgs(mediaStorageDir) < 0) {
                if (focus_view_pager_intent_status == 1) {
                    for (int i = 0; i < mapInfos.size(); i++) {
                        if (mapInfos.get(i).getfolder().equals(f_path)) {
                            mImg.clear();
                            //            Log.d(TAG, "f_path " + f_path);
                            change_mapinfo(i, f_path);
                        }
                    }
                    focus_view_pager_intent_status = 0;
                }
            }


        if (textView_title != null)
            textView_title.setText(f_path);


        mHandler.sendEmptyMessageDelayed(UPDATE_VIEW, 0);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(
                UPDATE_RECYCLER,
                UPDATE_ID, id), 10);

        super.onResume();
    }

    Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
      //      Log.d(TAG, "mHandler " + msg.what);
            switch (msg.what) {
                case KILL_ACTIVITY:
       //             Log.d(TAG, "finish");
                    finish();
                    break;

                case UPDATE_RECYCLER:
                    switch (msg.arg1) {
                        case UPDATE_ID:
                            //todo
                            update_recycler_view(getBaseContext(), msg.arg2);  //update recycler view in asynctask
                            break;
                    }
                    break;

                case UPDATE_VIEW:
        //            Log.d(TAG, "update view");
                    if (adapter != null && listadapter != null) {
                        adapter.notifyDataSetChanged();
                        listadapter.notifyDataSetChanged();
                    }
      //              fullMetalAdapter.notifyDataSetChanged();

                    if (from != "MainActivity") {
                        /*
                        MainActivity mMain = new MainActivity();
                        if (mMain.pics_delete(id_from_main) == 0) {
                            Log.d(TAG, "id_from_main " + id_from_main);
                            mHandler.sendEmptyMessageDelayed(KILL_ACTIVITY, 20);
                        }
                        */
                    }
                    break;

                case CLEAR_SELECTED_ITEM:
                    //arraylist 순서 조정 필요
                    Collections.sort(selectedPositions);
                    Collections.reverse(selectedPositions);
/*
                    for (int i = 0; i < selectedPositions.size(); i++) {
                        Log.d(TAG, "selectedPos: " + selectedPositions.get(i));
                    }
*/
                    while (true) {
                        int size;

                        size = selectedPositions.size();

                        if (size == 0) {
                            adapter.notifyDataSetChanged();
                            break;
                        } else {
        //                    Log.d(TAG, "selectedPositions: " + selectedPositions.get(0));
                            customView.display(false, selectedPositions.get(0));
                            selectedPositions.remove(0);
                        }
                    }
/*
                    for (int i = selectedPositions.size() - 1; i > 0; i--) {
                        Log.d(TAG, "selectedPositions: " + selectedPositions.get(i));
                        customView.display(false, selectedPositions.get(i));
                        selectedPositions.remove(i);
                    }

                    adapter.notifyDataSetChanged();
                    */
                break;

                case CTRL_MEMODB:
                    switch (msg.arg1) {
                        case UPDATE_ID:
                            FocusViewPager focusViewPager = new FocusViewPager();
                            focusViewPager.extern_memoDB_ctrl(3, msg.arg2); //3, memoDB delete
                            break;
                    }

                    break;

                case GALLERY_GET:

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ImagesView.this);
                    alertDialog.setTitle("이 폴더로 사진을 가져 오시겠습니까?\n");
                    alertDialog.setIcon(R.drawable.toad_app_icon_v4);
                    alertDialog.setMessage("     " + imageList.size() + "장 선택 되었습니다.");
                    alertDialog.setPositiveButton("복사", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SendTask get_gallery = new SendTask(getBaseContext(), null, 6);
                            get_gallery.execute();
                        }
                    });

                    alertDialog.setNegativeButton("이동", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SendTask get_gallery = new SendTask(getBaseContext(), null, 5);
                            get_gallery.execute();
                        }
                    });

                    alertDialog.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
         //                   Toast.makeText(getBaseContext(), "취소 되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                    break;


                case PROGRESS:

                    switch (msg.arg1) {
                        case START:
                             customProgressDialog.setCanceledOnTouchOutside(false);
                             customProgressDialog.show();
                            break;
                        case STOP:
                            customProgressDialog.setCanceledOnTouchOutside(true);
                            customProgressDialog.dismiss();
                            break;
                    }
                    break;
                case GET_URI_FROM_PATH:
                    break;
                case SEND_EXTERNAL_STORAGE:
                    switch (msg.arg1) {
                        case EXTERNAL_MOVE:
                            if (msg.arg2 >= 0) {
                                try {
                                    int id = msg.arg2;

                                        if (fileController.file_controller(mainActivity.return_mainActivity_context(),mainActivity.get_main_path(), file_path, Appdir + File.separator + to_external_img_path, external_file_name, fileController.COPY_FILE) == 0) {

                                            if (focus_storage == INTERNAL_STORAGE) {
                                                File file = new File(file_path);
                                                file.delete();
                                            } else {

                                                fileController.file_controller(mainActivity.return_mainActivity_context(), mainActivity.get_main_path(),Appdir + File.separator + f_path, null, mImg.get(id), fileController.DELETE_FILE);

                                            }


                                            mImg.remove(id);

                                            //             Log.d(TAG, "mImg.size" + mImg.size());

                                            int selectedIndex = selectedPositions.indexOf(id);

                                            if (selectedIndex > -1) {
                                                //             Log.d(TAG, "selectedPositions: " + selectedIndex);
                                                selectedPositions.remove(selectedIndex);
                                            }


                                            prev_id = -1;
                                        } else {
                                            //todo copy fail
                                            prev_id = -1;
                                        }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case EXTERNAL_COPY:
                            if (msg.arg2 >= 0) {
                                try {

                                    int id = msg.arg2;


                                    if (fileController.file_controller(mainActivity.return_mainActivity_context(),mainActivity.get_main_path(),file_path, Appdir + File.separator + to_external_img_path, external_file_name, fileController.COPY_FILE) == 0) {
/*
                                        if (focus_storage == INTERNAL_STORAGE) {
                                            File file = new File(file_path);
                                            file.delete();
                                        }
*/
                          //              mImg.remove(id);

                                        //             Log.d(TAG, "mImg.size" + mImg.size());

                                        id_cnt++;
                                        prev_id = -1;
                                    } else {
                                        //todo copy fail
                                        prev_id = -1;
                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case EXTERNAL_COPY_RECYCLER:
                            if (msg.arg2 >= 0) {
                                try {
                                    int id = msg.arg2;


                                    if (fileController.file_controller(mainActivity.return_mainActivity_context(), mainActivity.get_main_path(),file_path, Appdir + File.separator + to_external_img_path, external_file_name, fileController.COPY_FILE) == 0) {
/*
                                        if (focus_storage == INTERNAL_STORAGE) {
                                            File file = new File(file_path);
                                            file.delete();
                                        }
*/
                                        //              mImg.remove(id);

                                        //             Log.d(TAG, "mImg.size" + mImg.size());
/*
                                        int selectedIndex = selectedPositions.indexOf(id);

                                        if (selectedIndex > -1) {
                                            //             Log.d(TAG, "selectedPositions: " + selectedIndex);
                                            selectedPositions.remove(selectedIndex);
                                        }

                                        //         Log.d(TAG, "selectedPositions.size" + selectedPositions.size());
                                        //        cnt++; //cnt 증가
                                        */
                                        prev_id = -1;
                                    } else {
                                        //todo copy fail
                                        prev_id = -1;
                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            break;
                        case EXTERNAL_MOVE_RECYCLER:
                            if (msg.arg2 >= 0) {
                                try {
                                    int id = msg.arg2;


                                    if (fileController.file_controller(mainActivity.return_mainActivity_context(), mainActivity.get_main_path(),file_path, Appdir + File.separator + to_external_img_path, external_file_name, fileController.COPY_FILE) == 0) {

                                        if (recycler_storage == INTERNAL_STORAGE) {
                                            File file = new File(file_path);
                                            file.delete();
                                        } else {
                                            fileController.file_controller(mainActivity.return_mainActivity_context(),mainActivity.get_main_path(),Appdir + File.separator + recyclerBasePath, null, external_file_name, fileController.DELETE_FILE);
                                        }

                                        prev_id = -1;

                                    } else {
                                        //todo move fail
                                        prev_id = -1;
                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            break;

                        case EXTERNAL_DELETE:
                            if (msg.arg2 >= 0) {
                                try {
                                    int id = msg.arg2;

                                    if (fileController.file_controller(mainActivity.return_mainActivity_context(),mainActivity.get_main_path(), to_external_img_path, null, external_file_name, fileController.DELETE_FILE) == 0) {
                                        mImg.remove(id);

                                        //             Log.d(TAG, "mImg.size" + mImg.size());

                                        int selectedIndex = selectedPositions.indexOf(id);

                                        if (selectedIndex > -1) {
                                            //             Log.d(TAG, "selectedPositions: " + selectedIndex);
                                            selectedPositions.remove(selectedIndex);
                                        }
                                            prev_id = -1;
                                    } else {
                                        prev_id = -1;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            break;

                        case EXTERNAL_DELETE_RECYCLER:
                            if (msg.arg2 >= 0) {
                                try {
                                    int id = msg.arg2;

                                    if (fileController.file_controller(mainActivity.return_mainActivity_context(),mainActivity.get_main_path(), to_external_img_path, null, external_file_name, fileController.DELETE_FILE) == 0) {
                                        prev_id = -1;
                                    } else {
                                        prev_id = -1;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            break;

                        case EXTERNAL_GALLERY_MOVE:
                            if (msg.arg2 >= 0) {
                                try {

                                    int id = msg.arg2;


                                    if (fileController.file_controller(mainActivity.return_mainActivity_context(),mainActivity.get_main_path(), file_path, Appdir + File.separator + to_external_img_path, external_file_name, fileController.COPY_FILE) == 0) {

                                        mainActivity.deleteFileFromMediaStore(mainActivity.return_mainActivity_context(), new File(imageListPath.get(id)));
                                        id_cnt++;
                                        prev_id = -1;
                                    } else {
                                        //todo copy fail
                                        prev_id = -1;
                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            break;

                    }
                    break;

            }

            super.handleMessage(msg);
        }
    };

    static int prev_id = -1;

    private static class CheckTh extends Thread {
        public void run() {
            /*
            if (mCheckThreadRunning == false) {
                try {
                    ImagesView run = new ImagesView();
                    run.run_activiy(activity, 0, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCheckThreadRunning = true;
            }
*/
            while (mCheckThreadRunning) {
                if (th_count == count) {
                    loading_done = true;
         //           Log.d(TAG, "DONE!!");
                    killThread();
                    if (kill_thread_call == 1) {
                        break;
                    }
                } else {
                    th_count = count;
                }
                try {
                    //          while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(200);   //sleep func이 있는 상태에서 interrupt를 발생시키면 Exception 발생
                    //        }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                } finally {
                    //    Log.d(TAG, "thread finally dead");
                }
            }
        }
    }

    private static void startThread() {
//        Log.d(TAG, "check thread start");
        mCheckTh = new CheckTh();
        mCheckTh.start();
    }

    private static void killThread() {
        if (mCheckTh != null) {
     //       Log.d(TAG, "check thread kill");
            mCheckTh.interrupt();
            mCheckTh = null;
            count = 0;
            th_count = 0;
            kill_thread_call = 0;
        }
    }

    @Override
    protected void onDestroy() {
        mCheckThreadRunning = false;
        asyncRunning = false;
  //      Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
 //       Log.d(TAG, "keyevent " + event.getAction() + " keycode" + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                if (multi_mode == 1) {
                    multi_mode = 0;
         //           Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
                  //  return true;
                } else {
                //    return false;

                }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
  //      Log.d(TAG, "onBackPressed");

        if (folder_depth > 0) {
            try {
                if (path_trace.size() > 0) {
                    String path_to_back = path_trace.get(path_trace.size() - 2);
                    File underFolderDir;
                    if (focus_storage == INTERNAL_STORAGE)
                         underFolderDir = new File(Environment.getExternalStorageDirectory() + Appdir,
                            path_to_back);
                    else
                        underFolderDir = new File(sdcard_path_header + Appdir,
                            path_to_back);

                    check_imgs(underFolderDir);

                    mediaStorageDir = underFolderDir;

    //                Log.d(TAG, "backward path" + underFolderDir.toString());
                    textView_title.setText(path_to_back);
                    int pos = 0;
                    for (int i = 0; i < mapInfos.size(); i++) {
                        if (path_to_back.equals(mapInfos.get(i).getfolder())) {
                            pos = i;
                            //                     Log.d(TAG, "mapinfo posFrom: " + posFrom);
                        }
                    }

                    mHandler.sendEmptyMessageDelayed(UPDATE_VIEW, 0);
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(
                            UPDATE_RECYCLER,
                            UPDATE_ID, pos), 10);

                    folder_depth = folder_depth - 1;
                    f_path = null;

                    f_path = path_to_back;

                    folder_path = null;
                    single_item_id = -1;

                    path_trace.remove(path_trace.size() - 1);

           //         Toast.makeText(this, " (" + f_path + ") "+ "폴더로 돌아갑니다.", Toast.LENGTH_SHORT).show();

                } else {
                    super.onBackPressed();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            super.onBackPressed();
        }
    }

    public String check_metal_list(int id) {
        return metalList.get(id);
    }

    public void set_metal_list(List<Integer> isSelected) {
        selectedPositions_metal.clear();
        for (int i = 0; i < isSelected.size(); i++) {
        //    Log.d(TAG, " isSlected pos: " + isSelected.get(i));
            selectedPositions_metal.add(isSelected.get(i));
        }
    }

    /*
    private void getGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                intent_gallery = new Intent();
                intent_gallery.setAction(Intent.ACTION_PICK);
                intent_gallery.setType("image/*");
                intent_gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent_gallery.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent_gallery, REQUSET_GALLERY);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "build version error");
        }
    }
*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     //   Log.d(TAG, "onActivityResult: Request " + requestCode + " Result " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUSET_GALLERY:
                if (resultCode == Activity.RESULT_OK) {
           //         Log.d(TAG, "Result ok");

                    imageList = new ArrayList();
                    imageListPath = new ArrayList<>();
                    imageName = new ArrayList<>();

                    if (data.getClipData() == null) {
                        imageList.add(data.getData());
                    } else {
                        ClipData clipData = data.getClipData();
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            imageList.add(clipData.getItemAt(i).getUri());
                        }
                    }
/*
                    for (int i = 0; i < imageList.size(); i++) {
                        Log.d(TAG, "imagelist: " + imageList.get(i).getPath());
                    }
                    */

  //                  Log.d(TAG, "get gallery");
                    SendTask get_gallery = new SendTask(getBaseContext(), null, 4);
                    get_gallery.execute();

                } else {
         //           Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUSET_FILE_PATH:
           //     Log.d(TAG, "Request_file_path");
                try {
                    ArrayList<Uri> LIST = new ArrayList<>();
                    if (data.getClipData() == null) {
                        LIST.add(data.getData());
                    } else {
                        ClipData clipData = data.getClipData();
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            LIST.add(clipData.getItemAt(i).getUri());
                        }
                    }
                    /*
                    for (int i = 0; i < LIST.size(); i++) {
                        Log.d(TAG, "dir list " + LIST.get(i));
                    }
                    */
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;

        }
    }





}
