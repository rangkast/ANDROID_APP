package com.toadstudio.first.toadproject;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.toadstudio.first.toadproject.BackUp.GoogleSignInActivity;
import com.toadstudio.first.toadproject.CustomDialog.CustomProgressDialog;
import com.toadstudio.first.toadproject.CustomDialog.ListCustomDialog;
import com.toadstudio.first.toadproject.CustomDialog.ListDialogListener;
import com.toadstudio.first.toadproject.GMap.SettingInfo;
import com.toadstudio.first.toadproject.ListView.ListviewAdapter;
import com.toadstudio.first.toadproject.ListView.Listviewitem;
import com.toadstudio.first.toadproject.SQLiteDB.MemoDBHelper;

import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.os.EnvironmentCompat;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toadstudio.first.toadproject.Cam.Camera2BasicFragment;
import com.toadstudio.first.toadproject.Cam.CameraActivity;
import com.toadstudio.first.toadproject.CustomDialog.CustomDialog;
import com.toadstudio.first.toadproject.CustomDialog.CustomDialog_2;
import com.toadstudio.first.toadproject.CustomDialog.DialogListener_2;
import com.toadstudio.first.toadproject.CustomDialog.GridCustomDialog;
import com.toadstudio.first.toadproject.CustomDialog.GridCustomListener;
import com.toadstudio.first.toadproject.CustomDialog.MyDialogListener;
import com.toadstudio.first.toadproject.GMap.CustomInfoWindowGoogleMap;
import com.toadstudio.first.toadproject.GMap.InfoWindowData;
import com.toadstudio.first.toadproject.GMap.MapInfo;
import com.toadstudio.first.toadproject.GMap.MultiDrawable;
import com.toadstudio.first.toadproject.GMap.MySupportMapFragment;
import com.toadstudio.first.toadproject.GMap.PositionItem;
import com.toadstudio.first.toadproject.Image.ImagesView;
import com.toadstudio.first.toadproject.SQLiteDB.SettingDBCtrct;
import com.toadstudio.first.toadproject.SQLiteDB.SettingDBHelper;
import com.toadstudio.first.toadproject.Settings.SettingsActivity;
import com.toadstudio.first.toadproject.Settings.Settings_GridActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.maps.android.clustering.ClusterManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import noman.googleplaces.PlacesListener;

import com.toadstudio.first.toadproject.SQLiteDB.ContactDBCtrct;
import com.toadstudio.first.toadproject.SQLiteDB.ContactDBHelper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;

import android.widget.Button;

import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

import android.os.Vibrator;
import com.google.android.gms.ads.MobileAds;
import com.toadstudio.first.toadproject.Settings.Settings_storage_Activity;

import org.apache.commons.io.FileUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        PlacesListener, View.OnClickListener, View.OnTouchListener,
        View.OnLongClickListener,
        GoogleMap.OnMarkerDragListener {

    private static final String TAG = "ToadPrj_MainActivity";

    private static final String KEY = "AIzaSyDmy4hwiBZtBEdZ5a3AK7N6_dK5ZIl-BGY";

    private GoogleApiClient mGoogleApiClient = null;
    private GoogleMap mGoogleMap = null;
    private Marker currentMarker = null;
    private Marker coordMarker = null;
    private Marker customMarker = null;

    private PolylineOptions polylineOptions_gray;
    private PolylineOptions polylineOptions_blue;

    private static RelativeLayout mLinearLayout;

    private static MySupportMapFragment mapFragment = null;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList hasPermissions = new ArrayList();
    ArrayList permissonsRationale = new ArrayList();

    private List<LatLng> arrayPoints_gray;
    private List<LatLng> arrayPoints_blue;
    private List<Polyline> polyline = new ArrayList<Polyline>();

    ArrayList<String> user_folders_internal = new ArrayList();
    ArrayList<String> user_folders_external = new ArrayList();
    ArrayList<String> fail_folders_reason = new ArrayList();
    ArrayList<String> empty_folder = new ArrayList<>();
    ArrayList<String> no_db_folder = new ArrayList<>();

    private static ArrayList<Integer> custom_marker = new ArrayList<>();

    //To Do : Customize Google Map Activity
    private static ClusterManager<PositionItem> mClusterManager = null;

    private TextView mtextInfo;
    private TextView mcoordInfo;

    private static int db_cnt = 0;
    private static int loading_cnt = 0;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2003;
    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE= 2004;
    private static final int UPDATE_INTERVAL_MS = 1500;
    private static final int FASTEST_UPDATE_INTERVAL_MS = 300;

    static Activity mActivity;
    boolean askPermissionOnceAgain = false;
    boolean mRequestingLocationUpdates = false;
    Location mCurrentLocatiion;
    boolean mMoveMapByUser = true;
    boolean mMoveMapByAPI = true;
    boolean mTrackPosition = true;
    boolean mTouchMarker = false;
    boolean mMakerCustom = false;
    private StringBuilder deleteFolder_str = null;

    LocationRequest locationRequest = new LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL_MS)
            .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

    public static final int DUMMY = 1;
    public static final int MAKE_TOAST = 2;
    public static final int SET_DB_MARKER = 3;
    public static final int NEW_DB_SAVE = 4;
    public static final int ERROR_LOG = 5;
    public static final int NETWORK_CHECK = 6;
    public static final int SHOW_TOAST = 7;
    public static final int RESUME_FUNC = 8;
    public static final int CAMERA_UPDATE = 9;
    public static final int MAKE_FOLDER = 10;
    public static final int CLOSE_APP = 11;
    public static final int PROGRESS = 12;
    public static final int PROGRESS_ALERT = 13;
    public static final int UPDATE_POLYLINE = 14;
    public static final int UPDATE_BTNS = 15;
    public static final int DELETE_FOLDER = 16;
    public static final int FILE_CONTROL = 17;

    private final int START = 0;
    private final int STOP = 1;

    public static final int ERROR_NO_GEOGODER_SERVICE = 1;
    public static final int ERROR_NO_GPS = 2;
    public static final int ERROR_NO_ADDRESS = 3;
    public static final int ERROR_NO_NETWORK = 4;

    public static final int SHOW_REFACTOR = 1;
    public static final int SHOW_DELETE = 2;
    public static final int SHOW_FOLDER_RESULT = 3;

    private static final int UPDATE_NEWLATLON = 1;
    private static final int UPDATE_CURLATLON = 2;

    private static final int SETTING_DB = 0;
    private static final int MAIN_DB = 1;
    private static final int MEMO_DB =2;

    private static int update_status = 0;
    private static int zoom_status = 0;
    public static int marker_changed = 0;

    private static String delete_path;
    private int delete_id;

    private static LatLng update_newLatLng;

    private static LatLng currentPosition;

    List<Marker> previous_marker = null;

    //cluster marker array
    static List<PositionItem> mPosi = null;
    static PositionItem mItem = null;

    //mapinfo db data array
    static ArrayList<MapInfo> mMapInfo = null;
    static MapInfo mMapInfo_class;

    //mapinfo db data array
    static ArrayList<SettingInfo> mSettingInfo = null;
    static SettingInfo mSettingInfo_class;

    DecimalFormat form = new DecimalFormat("#.####");

    //SQL DB Control
    static SQLiteDatabase sqliteDB_data;
    static SQLiteDatabase sqliteDB_setting;
    public static SQLiteDatabase sqliteDB_memo;
    static ContactDBHelper dbHelper = null;
    static SettingDBHelper settingDBHelper = null;
    public static MemoDBHelper memoDBHelper = null;

    static String intent_from = null;
    static int need_to_update = 0;
    static int clear_data = 0;

    public static String[] arrayAddresses;
    public static String[] makeFolder;
    static  String[] cam_data =  new String[5];
    static String Appdir = "/여행을찍다";

    //threads in main activity
    private DBthread mDBthread;
    private ZoomThread mZoomThread;
    public boolean mDBthreadRunning;
    public boolean mNewPathSavethreadRunning;
    private NewPathSavethread mNewPathSavethread;

    private static CustomProgressDialog customProgressDialog;

    private static Settings_GridActivity settings_gridActivity;

    private static AdView mAdView;

    private String force_path = null;

    Vibrator vibrator;

    static int gps_status = 0;

    public static final String CONNECTION_CONFIRM_CLIENT_URL = "http://clients3.google.com/generate_204";

  //  static int folder_level = 3; // TO DO 앞으로 폴더 layer를 결정할 변수

    //setting parameter
    static int marker_choice = 0;  //default
    static int level_choice = 3;  //default
    static int default_cam = 2;  //default
    static float x_coord = 0;
    static float y_coord = 0;

    static int saving_status = 0;
    static int change_path = 0;

    public static boolean mMapIsTouched = false;

    //drag status in main map ui
    //tracking position
    private final static int START_DRAGGING = 1;
    private final static int STOP_DRAGGING = 2;

    private static ImageView drag_button;
    private static ImageView list_button;
    private static ImageButton menu_button;

    private ImageView inter_line_info;
    private ImageView info_btn_info;

    private static int status = STOP_DRAGGING; //default

    private static int maxDisplayWidth;
    private static int maxDisplayHeight;

    private static GroundOverlay imageOverlay;

    static int info_btn = 0;
    static int inter_line = 0;

    private static ListviewAdapter listviewAdapter;

    private LatLng upper_latLng;

    private static String sdcard_path_header = null;
    private static String External_Path;

    private static int storage_status = 0; //default internal storage

    private static int map_status = 1;

    private static ImagesView imagesView;
    private static FileController fileController;
    private static Settings_storage_Activity settings_storage_activity;
    Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            try {


                super.handleMessage(msg);
                switch (msg.what) {
                    case DELETE_FOLDER:
                        break;
                    case DUMMY:
                        switch (msg.arg1) {
                        /*
                        case STATE_TOUCH_DOWN:
                            break;
                            */
                        }
                        break;

                    case MAKE_TOAST:
                        break;

                    case SET_DB_MARKER:
                        if (mGoogleMap != null) {
                            mClusterManager.clearItems(); //clear cursor data
                            mPosi.clear();
                            //DB에 저장 된 위치 marker 표시 부분
                            for (int i = 0; i < mMapInfo.size(); i++) {
                                LatLng newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(i).getlatitude()), Double.parseDouble(mMapInfo.get(i).getlongitude()));
                                if (string_contains_checker(mMapInfo.get(i).getfolder()) == true) {
                                    mItem = new PositionItem(newLatLng, mMapInfo.get(i).getfolder(), mMapInfo.get(i).getreserved() + " 데이터", custom_marker.size() - 1, Integer.parseInt(mMapInfo.get(i).getlevel())); //주소 정보 없음 error마커는 항상 마지막
                                } else {
                                    mItem = new PositionItem(newLatLng, mMapInfo.get(i).getfolder(), mMapInfo.get(i).getreserved() + " 데이터", Integer.parseInt(mMapInfo.get(i).getdata()), Integer.parseInt(mMapInfo.get(i).getlevel()));
                                }
                                mPosi.add(mItem);

                            }
                            addItems(mPosi);
                        } else {
                            //         Log.d(TAG, "GoogleMap not ready");
                        }

                        break;

                    case NEW_DB_SAVE:
                        //     startNewPathSavethread();
                        break;

                    case ERROR_LOG:
                        switch (msg.arg1) {
                            case ERROR_NO_GEOGODER_SERVICE:
                                Toast.makeText(mActivity, "지오코더 서비스 사용이 불가합니다.", Toast.LENGTH_SHORT).show();
                                break;
                            case ERROR_NO_GPS:
                                Toast.makeText(mActivity, "잘못된 GPS 좌표 입니다.", Toast.LENGTH_SHORT).show();
                                break;
                            case ERROR_NO_ADDRESS:
                                Toast.makeText(mActivity, "주소 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                                break;
                            case ERROR_NO_NETWORK:
                                Toast.makeText(mActivity, "인터넷을 사용할 수 없습니다. ", Toast.LENGTH_LONG).show();
                                break;
                        }
                        break;

                    case NETWORK_CHECK:

                        break;

                    case SHOW_TOAST:

                        switch (msg.arg1) {

                            case SHOW_REFACTOR:
                                //      Log.d(TAG, "msg.arg2: " + msg.arg2);
                                final int id = msg.arg2;

                                update_newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(id).getlatitude()), Double.parseDouble(mMapInfo.get(id).getlongitude()));

                                final GridCustomDialog dialog = new GridCustomDialog(mActivity, "폴더 수정", "폴더를 선택 하세요",
                                        "수정 하시겠습니까?",
                                        mMapInfo.get(id).getfolder(),
                                        Integer.parseInt(mSettingInfo.size() > 0 ? mSettingInfo.get(0).getMarker() : Integer.toString(0)),
                                        Integer.parseInt(mMapInfo.get(id).getdata())); //data todo
                                //custom_marker.get(Integer.parseInt(mMapInfo.get(delete_id).getdata()))
                                dialog.setDialogListener(new GridCustomListener() {  // MyDialogListener 를 구현
                                    @Override
                                    public int onPositiveClicked(Integer cursor, String folder) {
                                        //     Log.d(TAG, "ok" + cursor); //여기서 커스텀 다이얼로그가 누른 값 return
                                        final StringBuilder path = new StringBuilder();
                                        int updated = 0;
                                        int detect = 0;
                                        int pos = -1;

                                        if (folder != null) {
                                            if (folder.contains(" ")) {
                                                Toast.makeText(getApplicationContext(), "경로에 공란이 있습니다.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                path.setLength(0); //원래값 초기화
                                                String[] path_check = folder.split("/");
                                                for (int i = 0; i < path_check.length; i++) {
                                                    path.append(path_check[i]);
                                                    if (i < path_check.length - 1)
                                                        path.append("/");
                                                }

                                                for (int i = 0; i < mMapInfo.size(); i++) {
                                                    if (path.toString().equals(mMapInfo.get(i).getfolder())) {
                                                        detect = 1;
                                                    }
                                                }

                                                if (detect == 0) {
                                                    File directoryPath = null;
                                                    File external_directoryPath = null;



                                                    if (Integer.parseInt(mMapInfo.get(id).getStorage()) == imagesView.INTERNAL_STORAGE) {
                                                        directoryPath = new File(Environment.getExternalStorageDirectory() + Appdir, mMapInfo.get(id).getfolder());

                                                        String[] fileList = directoryPath.list();

                                                        for (int i = 0; i < fileList.length; i++) {
                                                            if (fileController.file_format_check(fileList[i]) == 0) {
                                                                detect = 1;
                                                            }
                                                            //             Log.d(TAG, "file list " + fileList[i] + " detect:" + detect);
                                                        }

                                                        if (detect == 0) {
                                                            String prev_path = null;
                                                            //     Log.d(TAG, "path 수정, 기존:" + mMapInfo.get(id).getfolder() + " 수정:" + path.toString());
                                                            prev_path = mMapInfo.get(id).getfolder();

                                                            //folder data 수정
                                                            moveFolder(mMapInfo.get(id).getfolder(), path.toString(), true);
                                                            data_change(id, update_newLatLng, "folder", path.toString());

                                                            if (prev_path != null) {

                                                                File prev_path_check = new File(Environment.getExternalStorageDirectory() + Appdir, prev_path);
                                                                String[] prev_path_result = prev_path_check.list();
                                                                //    Log.d(TAG, "prev_path " + prev_path + " length: " + prev_path_result.length);
                                                                if (prev_path_result.length == 0) {
                                                                    if (delete_folders(prev_path, Integer.parseInt(mMapInfo.get(id).getStorage())) == true) {
                                                                        //      Log.d(TAG, "prev path deleted");
                                                                    }
                                                                }
                                                            }

                                                            if (Integer.parseInt(mMapInfo.get(id).getdata()) != cursor && (cursor != -1)) {
                                                                marker_change(id, Integer.toString(cursor));
                                                            }

                                                            updated = 1;

                                                        } else {

                                                            Toast.makeText(getApplicationContext(), "상위 폴더는 폴더명을 수정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                                        }

                                                        //todo 외장메모리 폴더명 변경 추가
                                                    } else  if (Integer.parseInt(mMapInfo.get(id).getStorage()) == imagesView.EXTERNAL_STORAGE) {
                                                        external_directoryPath = new File(sdcard_path_header + Appdir, mMapInfo.get(id).getfolder());

                                                        String[] fileList = external_directoryPath.list();

                                                        for (int i = 0; i < fileList.length; i++) {
                                                            if (fileController.file_format_check(fileList[i]) == 0) {
                                                                detect = 1;
                                                            }
                                                            //             Log.d(TAG, "file list " + fileList[i] + " detect:" + detect);
                                                        }

                                                        if (detect == 0) {
                                                            List<String> split_str = new ArrayList<>();
                                                            split_str = Arrays.asList(mMapInfo.get(id).getfolder().split("/"));

                                                            List<String> split_str_rename = new ArrayList<>();
                                                            split_str_rename = Arrays.asList(path.toString().split("/"));

                                                            if (split_str.size() == split_str_rename.size()) {
                                                                fileController.file_controller(mActivity, get_main_path(),Appdir + "/" + mMapInfo.get(id).getfolder(), null, Appdir + "/" + path.toString(), fileController.UPDATE_FILE);
                                                            } else {
                                                                try {

                                                                    FileControl task = new FileControl(mActivity, 0, mMapInfo.get(id).getfolder(), path.toString(), fileList);
                                                                    task.execute(mActivity);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                         //       Toast.makeText(getApplicationContext(), "폴더 구조 숫자 변경은 외장에서 아직 지원하지 않습니다.", Toast.LENGTH_SHORT).show();

                                                            }

                                                            data_change(id, update_newLatLng, "folder", path.toString());

                                                            if (Integer.parseInt(mMapInfo.get(id).getdata()) != cursor && (cursor != -1)) {
                                                                marker_change(id, Integer.toString(cursor));
                                                            }

                                                            updated = 1;

                                                        } else {

                                                            Toast.makeText(getApplicationContext(), "상위 폴더는 폴더명을 수정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        directoryPath = new File(Environment.getExternalStorageDirectory() + Appdir, mMapInfo.get(id).getfolder());
                                                        external_directoryPath = new File(sdcard_path_header + Appdir, mMapInfo.get(id).getfolder());

                                                        String[] fileList_internal = directoryPath.list();
                                                        String[] fileList_external = external_directoryPath.list();

                                                        for (int i = 0; i < fileList_internal.length; i++) {
                                                            if (fileController.file_format_check(fileList_internal[i]) == 0) {
                                                                detect = 1;
                                                            }
                                                            //             Log.d(TAG, "file list " + fileList[i] + " detect:" + detect);
                                                        }

                                                        if (detect == 0) {
                                                            String prev_path = null;
                                                            //     Log.d(TAG, "path 수정, 기존:" + mMapInfo.get(id).getfolder() + " 수정:" + path.toString());
                                                            prev_path = mMapInfo.get(id).getfolder();

                                                            //내장
                                                            moveFolder(mMapInfo.get(id).getfolder(), path.toString(), true);


                                                            List<String> split_str = Arrays.asList(mMapInfo.get(id).getfolder().split("/"));

                                                            List<String> split_str_rename = Arrays.asList(path.toString().split("/"));

                                                            if (split_str.size() == split_str_rename.size()) {
                                                                fileController.file_controller(mActivity, get_main_path(),Appdir + "/" + mMapInfo.get(id).getfolder(), null, Appdir + "/" + path.toString(), fileController.UPDATE_FILE);
                                                            } else {
                                                                try {

                                                                    FileControl task = new FileControl(mActivity, 0, mMapInfo.get(id).getfolder(), path.toString(), fileList_external);
                                                                    task.execute(mActivity);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                                //       Toast.makeText(getApplicationContext(), "폴더 구조 숫자 변경은 외장에서 아직 지원하지 않습니다.", Toast.LENGTH_SHORT).show();

                                                            }

                                                            data_change(id, update_newLatLng, "folder", path.toString());

                                                            if (prev_path != null) {

                                                                File prev_path_check = new File(Environment.getExternalStorageDirectory() + Appdir, prev_path);
                                                                String[] prev_path_result = prev_path_check.list();
                                                                //    Log.d(TAG, "prev_path " + prev_path + " length: " + prev_path_result.length);
                                                                if (prev_path_result.length == 0) {
                                                                    if (delete_folders(prev_path, Integer.parseInt(mMapInfo.get(id).getStorage())) == true) {
                                                                        //      Log.d(TAG, "prev path deleted");
                                                                    }
                                                                }
                                                            }

                                                            if (Integer.parseInt(mMapInfo.get(id).getdata()) != cursor && (cursor != -1)) {
                                                                marker_change(id, Integer.toString(cursor));
                                                            }

                                                            updated = 1;


                                                        } else {

                                                            Toast.makeText(getApplicationContext(), "상위 폴더는 폴더명을 수정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                                        }









//
 //                                                       Toast.makeText(getApplicationContext(), "(내/외장) 폴더명 변경 아직 지원하지 않습니다.", Toast.LENGTH_SHORT).show();
                                                    }







                                                } else {
                                                    if (Integer.parseInt(mMapInfo.get(id).getdata()) != cursor && (cursor != -1)) {
                                                        if (path.toString().equals(mMapInfo.get(id).getfolder())) {
                                                            marker_change(id, Integer.toString(cursor));
                                                            updated = 1;
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "같은 경로가 있습니다.", Toast.LENGTH_SHORT).show();
                                                        }

                                                    } else {
                                                        if (!path.toString().equals(mMapInfo.get(id).getfolder()))
                                                            Toast.makeText(getApplicationContext(), "같은 경로가 있습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                if (updated == 1) {
                                                    update_newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(id).getlatitude()), Double.parseDouble(mMapInfo.get(id).getlongitude())); //위치 다시 조정
                                                    mHandler.sendEmptyMessageDelayed(SET_DB_MARKER, 0); //marker 수정
                                                    mHandler.sendMessage(mHandler.obtainMessage(
                                                            MainActivity.CAMERA_UPDATE,
                                                            MainActivity.UPDATE_NEWLATLON, 0));
                                                    Toast.makeText(getApplicationContext(), "수정 되었습니다.", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                } else {
                                                    if (path.toString().equals(mMapInfo.get(id).getfolder()))
                                                        Toast.makeText(getApplicationContext(), "변경사항이 없습니다.", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                        } else {
                                            Toast.makeText(getApplicationContext(), "경로에 공란이 있습니다.", Toast.LENGTH_SHORT).show();
                                        }

                                        return cursor;
                                    }

                                    @Override
                                    public void onNegativeClicked() {
                                        //        Log.d(TAG, "cancel");
                                        //         Toast.makeText(getApplicationContext(), "취소 되었습니다", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialog.show();

                                break;

                            case SHOW_DELETE:
                                //     Log.d(TAG, "msg.arg2: " + msg.arg2);
                                final int num = msg.arg2;

                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                                alertDialog.setTitle(mMapInfo.get(num).getfolder());

                                // Setting Dialog Message
                                alertDialog.setMessage("사진과 폴더 모두 삭제 됩니다." + "\n\n정말 삭제 하시겠습니까?");

                                // Setting Icon to Dialog
                                alertDialog.setIcon(custom_marker.get(Integer.parseInt(mMapInfo.get(num).getdata())));


                                if (Integer.parseInt(mMapInfo.get(num).getStorage()) == imagesView.BOTH_STORAGE) {
                                    // Setting Positive "Yes" Button
                                    alertDialog.setNeutralButton("모두 삭제", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            LatLng newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(num).getlatitude()), Double.parseDouble(mMapInfo.get(num).getlongitude()));

                                            boolean return_status = DeleteDir(num, delete_path, Integer.parseInt(mMapInfo.get(num).getStorage()));

                                            if (return_status ==true) {
                                                update_newLatLng = newLatLng;
                                                mHandler.sendMessage(mHandler.obtainMessage(
                                                        MainActivity.CAMERA_UPDATE,
                                                        MainActivity.UPDATE_NEWLATLON, 0));
                                                //          CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(newLatLng, 12);
                                                //          mGoogleMap.moveCamera(cameraUpdate);

                                                Toast.makeText(getApplicationContext(), "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "삭제 실패.", Toast.LENGTH_SHORT).show();
                                            }
                                            dialog.cancel();

                                        }
                                    });

                                    // Setting Negative "NO" Button
                                    alertDialog.setPositiveButton("내장 삭제", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Write your code here to invoke NO event
                                            //             Toast.makeText(getApplicationContext(), "취소 되었습니다", Toast.LENGTH_SHORT).show();

                                            LatLng newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(num).getlatitude()), Double.parseDouble(mMapInfo.get(num).getlongitude()));

                                            boolean return_status = DeleteDir(num, delete_path, ImagesView.INTERNAL_STORAGE);
                                            if (return_status ==true) {
                                                update_newLatLng = newLatLng;
                                                mHandler.sendMessage(mHandler.obtainMessage(
                                                        MainActivity.CAMERA_UPDATE,
                                                        MainActivity.UPDATE_NEWLATLON, 0));
                                                //          CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(newLatLng, 12);
                                                //          mGoogleMap.moveCamera(cameraUpdate);

                                                Toast.makeText(getApplicationContext(), "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "삭제 실패.", Toast.LENGTH_SHORT).show();
                                            }
                                            dialog.cancel();
                                        }
                                    });
                                    // Setting Negative "NO" Button
                                    alertDialog.setNegativeButton("외장 삭제", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Write your code here to invoke NO event
                                            //             Toast.makeText(getApplicationContext(), "취소 되었습니다", Toast.LENGTH_SHORT).show();
                                            LatLng newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(num).getlatitude()), Double.parseDouble(mMapInfo.get(num).getlongitude()));

                                            boolean return_status = DeleteDir(num, delete_path, ImagesView.EXTERNAL_STORAGE);
                                            if (return_status ==true) {
                                                update_newLatLng = newLatLng;
                                                mHandler.sendMessage(mHandler.obtainMessage(
                                                        MainActivity.CAMERA_UPDATE,
                                                        MainActivity.UPDATE_NEWLATLON, 0));
                                                //          CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(newLatLng, 12);
                                                //          mGoogleMap.moveCamera(cameraUpdate);

                                                Toast.makeText(getApplicationContext(), "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "삭제 실패.", Toast.LENGTH_SHORT).show();
                                            }

                                            dialog.cancel();
                                        }
                                    });
                                } else {
                                    alertDialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            LatLng newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(num).getlatitude()), Double.parseDouble(mMapInfo.get(num).getlongitude()));

                                            boolean return_status = DeleteDir(num, delete_path, Integer.parseInt(mMapInfo.get(num).getStorage()));
                                            if (return_status ==true) {
                                                update_newLatLng = newLatLng;
                                                mHandler.sendMessage(mHandler.obtainMessage(
                                                        MainActivity.CAMERA_UPDATE,
                                                        MainActivity.UPDATE_NEWLATLON, 0));
                                                //          CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(newLatLng, 12);
                                                //          mGoogleMap.moveCamera(cameraUpdate);

                                                Toast.makeText(getApplicationContext(), "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "삭제 실패.", Toast.LENGTH_SHORT).show();
                                            }
                                            dialog.cancel();

                                        }
                                    });

                                    // Setting Negative "NO" Button
                                    alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.cancel();
                                        }

                                    });
                                }


                                alertDialog.show();

                                break;

                            case SHOW_FOLDER_RESULT:
                                Toast toast = Toast.makeText(mActivity, deleteFolder_str.toString(), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                toast.show();
                                deleteFolder_str.setLength(0);
                                mHandler.sendMessage(mHandler.obtainMessage(
                                        MainActivity.CAMERA_UPDATE,
                                        MainActivity.UPDATE_NEWLATLON, 0));

                                break;

                        }

                        break;


                    case RESUME_FUNC:
                        onResumeFunc();
                        break;

                    case CAMERA_UPDATE:

                        float zoom = mGoogleMap.getCameraPosition().zoom;
                        float zoom_config = 0;
                        //       Log.d(TAG, "default zoom" +Float.toString(zoom));
                        if (mMoveMapByAPI) {
                            //      Log.d(TAG, "Do nothing");
                        } else {
                            if (zoom > 14 || zoom < 10) {
                                zoom_config = 12;
                                if (zoom == zoom_config) {
                                    zoom = zoom_config + 0.5f;
                                } else {
                                    zoom = zoom_config;
                                }
                            }
                        }
                        //    Log.d(TAG, "config zoom" + Float.toString(zoom));
                        //    Log.d(TAG, "msg.arg1: " + msg.arg1);

                        switch (msg.arg1) {
                            case UPDATE_NEWLATLON:
                                if (update_newLatLng != null) {
                                    if (mMoveMapByAPI) {
                                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(update_newLatLng, zoom);
                                        mGoogleMap.animateCamera(cameraUpdate);
                                    } else {
                                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(update_newLatLng, zoom + 0.5f);
                                        mGoogleMap.animateCamera(cameraUpdate);
                                    }

                                } else {
                                    setDefaultLocation();
                                }
                                break;
                            case UPDATE_CURLATLON:
                                if (currentPosition != null) {
                                    if (mMoveMapByAPI && early_update_pos == 0) {
                                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentPosition, zoom);
                                        mGoogleMap.animateCamera(cameraUpdate);
                                    } else {
                                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentPosition, zoom + 0.5f);
                                        mGoogleMap.animateCamera(cameraUpdate);
                                        early_update_pos = 0;
                                    }
                                } else {
                                    setDefaultLocation();
                                }
                                break;
                        }

                        break;

                    case MAKE_FOLDER:
                        make_folder_dialog(update_newLatLng);
                        break;
                    case CLOSE_APP:
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                        alertDialog.setTitle("여행을찍다 종료");

                        // Setting Dialog Message
                        alertDialog.setMessage("\n정말 종료 하시겠습니까?");

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.toad_app_icon_v4);

                        // Setting Positive "Yes" Button
                        alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                onStop();
                                finish();
                            }
                        });

                        // Setting Negative "NO" Button
                        alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

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

                    case PROGRESS_ALERT:
/*
                        String[] array = fail_folders_reason.toArray(new String[fail_folders_reason.size()]);

                        final CustomDialog_2 dialog = new CustomDialog_2(mActivity, array, "적용 실패", "");
                        dialog.setDialogListener(new DialogListener_2() {  // MyDialogListener 를 구현
                            @Override
                            public void onPositiveClicked() {

                                dialog.cancel();
                            }

                            @Override
                            public void onNegativeClicked() {

                                dialog.cancel();
                            }


                        });
                        dialog.show();

*/

                        String[] array = fail_folders_reason.toArray(new String[fail_folders_reason.size()]);

                        CustomDialog dialog = new CustomDialog(mActivity, array, "저장 실패", "");
                        dialog.setDialogListener(new MyDialogListener() {  // MyDialogListener 를 구현
                            @Override
                            public void onPositiveClicked() {
                                no_db_folder.clear();

                            }

                            @Override
                            public void onDeleteClicked() {
                                try {
                                    StringBuilder str = new StringBuilder("");

                                    for (int i = 0; i < fail_folders_reason.size(); i++) {
                                        List<String> split_str = new ArrayList<>();
                                        split_str = Arrays.asList(fail_folders_reason.get(i).split(":"));
                                        String path = null;
                                        if (split_str.get(2).equals("내장")) {
                                            path = split_str.get(0);
                                            if (delete_folders(path, imagesView.INTERNAL_STORAGE) == true) {
                                                str.append("삭제 성공: " + fail_folders_reason.get(i) + "\n");
                                            } else {
                                                str.append("삭제 실패: " + fail_folders_reason.get(i) + "\n");
                                            }
                                        } else if (split_str.get(2).equals("외장")) {
                                            path = split_str.get(0);
                                            if (delete_folders(path, imagesView.EXTERNAL_STORAGE) == true) {
                                                str.append("삭제 성공: " + fail_folders_reason.get(i) + "\n");
                                            } else {
                                                str.append("삭제 실패: " + fail_folders_reason.get(i) + "\n");
                                            }
                                        }


                                    }

                                    //          no_db_folder.clear();


                                    Toast.makeText(MainActivity.this, str.toString(), Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {

                                }
                                if (customProgressDialog.isShowing()) {
                                    mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                            PROGRESS,
                                            STOP, 0), 0);
                                }
                            }
                        });

             //           no_db_folder.clear();
              //          fail_folders_reason.clear();
              //          empty_folder.clear();
                        dialog.show();

                        break;

                    case UPDATE_POLYLINE:

                        polyline.add(mGoogleMap.addPolyline(polylineOptions_gray));
                        polyline.add(mGoogleMap.addPolyline(polylineOptions_blue));
                        break;
                    case UPDATE_BTNS:
                        set_btns();
                        break;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            }
    };

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.drag_btn:
          //      Log.d(TAG, "button long click");

                int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    vibrator.vibrate(100);
                    status = START_DRAGGING;
                }


                break;


        }
        return false;
    }

    public class DeleteFolderTask extends AsyncTask {

        private Context mContext;

        public DeleteFolderTask(Context context) {
            mContext = context;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
      //      Log.d(TAG, "DeleteFolderTask doInBackground");
            deleteFolder_str = new StringBuilder("");


            while (true) {
            //    Log.d(TAG, "deleteFolder while loop start");
                /*
                for (int i = 0; i < mMapInfo.size(); i++) {
                    if (Integer.parseInt(mMapInfo.get(i).getreserved()) == 0) {
                        empty_folder.add(mMapInfo.get(i).getfolder()); //arraylist의 주소를  배열로 저장
                    }
                }
*/
                String[] array = empty_folder.toArray(new String[empty_folder.size()]);

                if (array.length > 0) {
/*
                    for (int i = 0; i < mMapInfo.size(); i++) {
                        if (Integer.parseInt(mMapInfo.get(i).getreserved()) == 0) {
             //               empty_folder.add(mMapInfo.get(i).getfolder()); //arraylist의 주소를  배열로 저장
                            update_newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(i).getlatitude()), Double.parseDouble(mMapInfo.get(i).getlongitude()));
                        }
                    }
*/
         //           Log.d(TAG, "empty_folder length " + empty_folder.size());


                    for (int i = 0; i < empty_folder.size(); i++) {
                        List<String> split_str = Arrays.asList(empty_folder.get(i).split(":"));
                        if (delete_folders_auto(empty_folder.get(i)) == true) {
                            for (int j = 0; j < mMapInfo.size(); j++) {
                                if (mMapInfo.get(j).getfolder().equals(split_str.get(0))) {
                                    if (Integer.parseInt(split_str.get(1)) == ImagesView.BOTH_STORAGE) {
                                        data_remove(j);
                                    } else if (Integer.parseInt(split_str.get(1)) == ImagesView.INTERNAL_STORAGE) {
                                        if (Integer.parseInt(mMapInfo.get(j).getStorage()) == ImagesView.BOTH_STORAGE) {
                                            mMapInfo_class = new MapInfo(
                                                    mMapInfo.get(j).getid(),
                                                    mMapInfo.get(j).getfolder(),
                                                    mMapInfo.get(j).getlatitude(),
                                                    mMapInfo.get(j).getlongitude(),
                                                    mMapInfo.get(j).getreserved(),
                                                    mMapInfo.get(j).getFav(),
                                                    mMapInfo.get(j).getdata(),
                                                    mMapInfo.get(j).getlevel(),
                                                    "1");
                                            mMapInfo.set(j, mMapInfo_class);
                                            save_db(0);
                                        } else {
                                            data_remove(j);
                                        }

                                    } else {
                                        if (Integer.parseInt(mMapInfo.get(j).getStorage()) == ImagesView.BOTH_STORAGE) {
                                            mMapInfo_class = new MapInfo(
                                                    mMapInfo.get(j).getid(),
                                                    mMapInfo.get(j).getfolder(),
                                                    mMapInfo.get(j).getlatitude(),
                                                    mMapInfo.get(j).getlongitude(),
                                                    mMapInfo.get(j).getreserved(),
                                                    mMapInfo.get(j).getFav(),
                                                    mMapInfo.get(j).getdata(),
                                                    mMapInfo.get(j).getlevel(),
                                                    "0");
                                            mMapInfo.set(j, mMapInfo_class);
                                            save_db(0);
                                        } else {
                                            data_remove(j);
                                        }
                                    }
                                    deleteFolder_str.append("삭제 성공: " + split_str.get(0) +

                                            (Integer.parseInt(split_str.get(1)) == imagesView.BOTH_STORAGE ? "(내/외장)" : (Integer.parseInt(split_str.get(1)) == ImagesView.INTERNAL_STORAGE ? "(내장)" : "(외장)")) +

                                            "\n");
                                }
                            }
                        } else {
                            deleteFolder_str.append("삭제 실패: " + split_str.get(0) +

                                    (Integer.parseInt(split_str.get(1)) == imagesView.BOTH_STORAGE ? "(내/외장)" : (Integer.parseInt(split_str.get(1)) == ImagesView.INTERNAL_STORAGE ? "(내장)" : "(외장)")) +

                                    "\n");
                        }
                    }



           //         CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(update_newLatLng, 10);
          //          mGoogleMap.moveCamera(cameraUpdate);


             //       Log.d(TAG, "str.toString " + deleteFolder_str.toString());

                    if (deleteFolder_str.toString().contains("삭제 성공")) {

                 //       Log.d(TAG, " 추가 작업 진행");
                        try {
                            user_folders_internal = check_child_path(imagesView.INTERNAL_STORAGE);
                            user_folders_external = check_child_path(imagesView.EXTERNAL_STORAGE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (user_folders_internal.size() == 0 && user_folders_external.size() == 0) {  //폴더가 더이상 없을 경우

     //                       Toast.makeText(MainActivity.this, str.toString(), Toast.LENGTH_SHORT).show();

                        } else {

                            if (user_folders_internal.size() > 0 || user_folders_external.size() > 0) {
                                if ((check_netWork() == true) && (isOnline() == true)) {
                                    update_status = 0;
                                    String[] new_path = {"0", "", "", ""};
                                    update_path(user_folders_internal, user_folders_external, new_path);
                                    break;
       //                             Toast.makeText(MainActivity.this, str.toString(), Toast.LENGTH_SHORT).show();
                                } else {
              //                      Log.d(TAG, "network malfunction occured");
                                    deleteFolder_str.setLength(0);
                                    deleteFolder_str.append("처리중 문제가 발생했습니다. 네트워크를 확인해 주세요");
           //                         Toast.makeText(getApplicationContext(), "처리 중 문제가 발생 했습니다. \n네트워크를 연결해 주세요", Toast.LENGTH_SHORT).show();

                                    break;
                                }

                            } else { //추가할 폴더가 없을 경우

                    //            Toast.makeText(MainActivity.this, str.toString(), Toast.LENGTH_SHORT).show();

                            }


                        }

                    } else {

                    }

                    empty_folder.clear();

                } else {
            //        Log.d(TAG, "deleteFolder while loop break");
         //           Toast.makeText(MainActivity.this, "빈 폴더가 없습니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
            }

            mHandler.sendMessageDelayed(mHandler.obtainMessage(
                    PROGRESS,
                    STOP, 0), 0);

            mHandler.sendEmptyMessageDelayed(SET_DB_MARKER, 0); //update new marker
            mHandler.sendMessage(mHandler.obtainMessage(
                    MainActivity.SHOW_TOAST,
                    MainActivity.SHOW_FOLDER_RESULT, 0));

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }

    public class LoadingTask extends AsyncTask {
        private int mLength;
        private Context mContext;

        public LoadingTask(Context context) {
            mContext = context;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
     //       Log.d(TAG, "LoadingTask doInBackground");

            if (check_netWork() == true && isOnline() == true) {
                save_db(0);
            } else {
                mHandler.sendMessage(mHandler.obtainMessage(
                        MainActivity.ERROR_LOG,
                        MainActivity.ERROR_NO_NETWORK, 0));

                mHandler.sendEmptyMessageDelayed(SET_DB_MARKER, 0);

                update_status = 1;
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }

    public class FileControl extends AsyncTask {
        private Context mContext;
        private int mControl;
        private String[] lists;
        private String mfrom_path;
        private String mto_path;


        public FileControl(Context context, int control, String from_path, String to_path, String[] file_list) {
            mContext = context;
            mControl = control;
            lists = file_list;
            mfrom_path = from_path;
            mto_path = to_path;
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            switch (mControl) {
                case 0:
                    if (fileController.folder_checker(mContext, get_main_path(), Appdir + File.separator + mto_path, fileController.MAKE_DIR) != null) {

                        for (int i = 0; i < lists.length; i++) {
                            if (fileController.file_controller(mContext,get_main_path(), sdcard_path_header + Appdir + File.separator + mfrom_path + File.separator + lists[i], Appdir + File.separator + mto_path, lists[i], fileController.COPY_FILE) == 0) {
                      //          Log.d(TAG, "from " + mfrom_path  + "(" + lists[i] + ") copied to " + mto_path);
                            }
                            if (fileController.file_controller(mContext, get_main_path(),Appdir + File.separator + mfrom_path, null, lists[i], fileController.DELETE_FILE) == 0) {
                      //          Log.d(TAG, lists[i] + " deleted");
                            }
                        }

                    }
                    break;
            }






            return null;
        }

        @Override
        protected void onPreExecute() {

            mHandler.sendMessageDelayed(mHandler.obtainMessage(
                    PROGRESS,
                    START, 0), 0);

            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Object o) {
            mHandler.sendMessageDelayed(mHandler.obtainMessage(
                    PROGRESS,
                    STOP, 0), 0);
            super.onPostExecute(o);
        }
    }




    public class AddFolderTask extends AsyncTask {
        private int mLength;
        private Context mContext;

        public AddFolderTask(Context context) {
            mContext = context;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
       //     Log.d(TAG, "AddFolder doInBackground");

            try {
                update_status = 0;
                String[] new_path = {"0", "", "", ""};
                update_path(user_folders_internal, user_folders_external, new_path);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }

    public class FileCntTask extends AsyncTask {

        private Context mContext;

        public FileCntTask(Context context) {
            mContext = context;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
   //         Log.d(TAG, "FileCntTask doInBackground");

            for (int i = 0; i < mMapInfo.size(); i++) {
                int length = check_files(mMapInfo.get(i).getfolder());
                if (length >= 0)
                    pics_sync(i, length, 0);
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }

    public class CheckConnect extends Thread{
        private boolean success;
        private String host;

        public CheckConnect(String host){
            this.host = host;
        }

        @Override
        public void run() {
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection)new URL(host).openConnection();
                conn.setRequestProperty("User-Agent","Android");
                conn.setConnectTimeout(1000);
                conn.connect();
                int responseCode = conn.getResponseCode();
                if(responseCode == 204) success = true;
                else success = false;
            }
            catch (Exception e) {
                e.printStackTrace();
                success = false;
            }
            if(conn != null){
                conn.disconnect();
            }
        }

        public boolean isSuccess(){
            return success;
        }

    }

    public boolean isOnline() {
        CheckConnect cc = new CheckConnect(CONNECTION_CONFIRM_CLIENT_URL);
        cc.start();
        try{
            cc.join();
            return cc.isSuccess();
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d(TAG, "isOnline false");
        return false;
    }

    private boolean check_netWork(){
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ninfo = cm.getActiveNetworkInfo();

        if(ninfo == null) {
            Log.d(TAG, "check network false");
            return false;
        } else {
            return true;
        }
    }


    public class DBthread extends Thread {
        int i = 0;
        public void run() {
            mHandler.post(new Runnable() {
                public void run() {
                    update_status = 0;

                    if (intent_from.contains("LogoActivity")) {
                        Intent intent_loading = new Intent(mActivity, LogoTwoActivity.class);
                        mActivity.startActivity(intent_loading);
                    }

                    if (init_database() == 0) {
                        init_tables();
                        if (load_values() < 0) {

                            Toast.makeText(getApplicationContext(), "로딩 중 문제가 발생 되었습니다. \n" +
                                    "3초후 앱이 종료 됩니다. 다시 시도해 주세요.", Toast.LENGTH_LONG).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    android.os.Process.killProcess(android.os.Process.myPid());

                                }
                            }, 3000);

                        }
                        final LoadingTask task = new LoadingTask(mActivity);
                        task.execute(mActivity);
                    } else {
          //              Log.d(TAG, "DB setting failed");
                        mDBthreadRunning = false;
                    }

                    //show logo
                //    readDirectoryPath();
                }
            });
            while (mDBthreadRunning) {
                mHandler.post(new Runnable() {
                    public void run() {
                     if (update_status == 1)
                         killDBthread();
                    }
                });
                try {
                     Thread.sleep(100);   //sleep func이 있는 상태에서 interrupt를 발생시키면 Exception 발생
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void startDBthread() {
   //     Log.d(TAG, "DB loading thread start");
        mDBthreadRunning = true;
        mDBthread = new DBthread();
        mDBthread.start();
    }

    public void killDBthread() {
        if (mDBthread != null) {
        //    Log.d(TAG, "DB Loading thread kill");
            mDBthreadRunning = false;
            mDBthread.interrupt();
            mDBthread = null;
        }
    }

    public class NewPathSavethread extends Thread {
        public void run() {
            mHandler.post(new Runnable() {
                public void run() {
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(
                            PROGRESS,
                            START, 0), 0);
                    update_status = 0;
                    String[] new_path = {"0", "", "", ""};
                    update_path(user_folders_internal, user_folders_external, new_path);
                }
            });
            while (mDBthreadRunning) {
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

    public void startNewPathSavethread() {
   //     Log.d(TAG, "Path save thread start");
        mNewPathSavethreadRunning = true;
        mNewPathSavethread = new NewPathSavethread();
        mNewPathSavethread.start();
    }

    public void killNewPathSavethread() {
        if (mNewPathSavethread != null) {
       //     Log.d(TAG, "Path save thread kill");
            mNewPathSavethreadRunning = false;
            mNewPathSavethread.interrupt();
            mNewPathSavethread = null;
        }
    }

    static Double prev_lat;
    static Double prev_lng;

    private List<LatLng> separates_points(List<LatLng> latLngs) {
        List<LatLng> list_latlng = latLngs;
        List<LatLng> difference_latlng = new ArrayList<>();
        LatLng latLng;
        int cnt = 0;
        int separates_cnt = 100;

        try {
            for (int i = 0; i < list_latlng.size(); i++) {

                Double lat = list_latlng.get(i).latitude;
                Double lng = list_latlng.get(i).longitude;

                if (i % 2 != 0) { //dest
                    latLng = new LatLng((lat - prev_lat) / (separates_cnt), (lng - prev_lng) / (separates_cnt ));
                    difference_latlng.add(latLng);
                } else {
                    prev_lat = lat; //source
                    prev_lng = lng;
                }
/*
                Log.d(TAG, cnt + ". lat: " + lat
                + " prev_lat: " + prev_lat + "  lng: " + lng + " prev_lng: " + prev_lng + "\n");
                */
                cnt++;
            }
/*
            for (int i = 0; i < difference_latlng.size(); i++) {
                Log.d(TAG, i + " d lat: " + difference_latlng.get(i).latitude + " d lng: " + difference_latlng.get(i).longitude);
            }
            */
            int length = list_latlng.size();

            for (int i = length - 1; i >= 0; i--) {

                Double lat_list = list_latlng.get(i).latitude;
                Double lng_list = list_latlng.get(i).longitude;

                if (i % 2 != 0) {
                    for (int j = 0; j < separates_cnt; j ++) {
                        Double lat_loop = lat_list - (difference_latlng.get((i - 1) / 2).latitude) * j;
                        Double lng_loop = lng_list - (difference_latlng.get((i - 1) / 2).longitude) * j;

                        if ((lat_list > lat_loop ? (lat_list - lat_loop) : (lat_loop - lat_list)) > 0
                                && (lng_list > lng_loop ? (lng_list - lng_loop) : (lng_loop - lng_list)) > 0) {
                            latLng = new LatLng(lat_loop, lng_loop);
                            list_latlng.add(i, latLng);
                        }

                    }
                }
            }
/*
            for (int k = 0; k < list_latlng.size(); k++) {
                Log.d(TAG, k + ". lat: " + list_latlng.get(k).latitude + " lng: " + list_latlng.get(k).longitude);
            }
*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list_latlng;
    }


    public class AnimatorTask extends AsyncTask {
        private Context mContext;


        public AnimatorTask(Context context) {
            mContext = context;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
      //      Log.d(TAG, "AnimatorTask doInBackground");

            try {
                arrayPoints_gray = separates_points(arrayPoints_gray);
                polylineOptions_gray.addAll(arrayPoints_gray);

                mHandler.sendEmptyMessageDelayed(UPDATE_POLYLINE, 0);

                // Get a handler that can be used to post to the main thread
                Handler mainHandler = new Handler(Looper.getMainLooper());

                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        animatePolyLine();
                    } // This is your code
                };
                mainHandler.post(myRunnable);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }

    private List<LatLng> listLatLng = new ArrayList<>();
    private  ValueAnimator animator = ValueAnimator.ofInt(0, 100);

    private void animatePolyLine() {
        animator.setDuration(3000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
    //            Log.d(TAG, "onAnimationUpdate");
                try {
                    if (polyline.size() > 0) {
                        List<LatLng> latLngList = polyline.get(1).getPoints(); //maybe blue line
                        int initialPointSize = latLngList.size();
                        int animatedValue = (int) animator.getAnimatedValue();
                        int newPoints = (animatedValue * arrayPoints_gray.size()) / 100;
 //                       Log.d(TAG, "" + initialPointSize +"." + animatedValue + "." + newPoints);
                        if (initialPointSize < newPoints) {
                            latLngList.addAll(arrayPoints_gray.subList(initialPointSize, newPoints));
                            polyline.get(1).setPoints(latLngList);
                        }
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        animator.addListener(polyLineAnimationListener);
        animator.start();

    }

    Animator.AnimatorListener polyLineAnimationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {

     //       Log.d(TAG, "onAnimationStart");

        }

        @Override
        public void onAnimationEnd(Animator animator) {
            try {
                if (polyline.size() > 0) {
                    List<LatLng> greyLatLng = polyline.get(0).getPoints();
                    List<LatLng> blueLatLng = polyline.get(1).getPoints();

         //           Log.d(TAG, "onAnimatonEnd");

                    blueLatLng.clear();
        //            greyLatLng.clear();

        //            polyline.get(0).setPoints(greyLatLng);
                    polyline.get(1).setPoints(blueLatLng);

                    polyline.get(1).setZIndex(2);

                    animator.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAnimationCancel(Animator animator) {
   //         Log.d(TAG, "animationCancel");
        }

        @Override
        public void onAnimationRepeat(Animator animator) {


        }
    };
/*
    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier("takepicturetotrip");
        Crashlytics.setUserEmail("rangkast@gmail.com");
        Crashlytics.setUserName("Test user");
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d(TAG, "onCreate");

        mActivity = this;

        try {
      //      Fabric.with(mActivity, new Crashlytics());
       //     logUser();


        final Intent intent = getIntent();
        imagesView = new ImagesView();
        fileController = new FileController();
            settings_storage_activity = new Settings_storage_Activity();

        intent_from = intent.getStringExtra("from");
        //   Log.d(TAG, "intent from: " + intent_from);

        MobileAds.initialize(mActivity, "ca-app-pub-4387339919511881~4433795886");

        //show logo
        //     Intent intent = new Intent(getBaseContext(), LogoActivity.class);
        //     startActivity(intent);

        previous_marker = new ArrayList<Marker>();
        mPosi = new ArrayList<PositionItem>();
        mMapInfo = new ArrayList<MapInfo>();
        mSettingInfo = new ArrayList<SettingInfo>();
        arrayPoints_gray = new ArrayList<LatLng>();

        //add Permission to ArrayList
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        checkPermissions();

        init_main_ui();

        //userFolders.add(""); //init
        settings_gridActivity = new Settings_GridActivity();
        //이부분이 오래 걸림........................
        startDBthread();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mapFragment = (MySupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_google);
        mapFragment.getMapAsync(this);

        customProgressDialog = new CustomProgressDialog(mActivity, "처리 중 입니다.");

        } catch (Exception e) {
            e.printStackTrace();
        }



    }


    public boolean hasRealRemovableSdCard(Context context) {

        boolean status = false;
        int cnt = 0;
        File[] external_files = ContextCompat.getExternalFilesDirs(context, null);
        for (int i = 0; i < external_files.length; i++) {
            if (external_files[i] != null) {
                cnt++;
            }
         //   Log.d(TAG, "dirs: " + external_files[i]);
        }

        if (cnt >= 2)
            status = true;

        return status;
    }

    public void init_main_ui() {
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Point displaySize = new Point();
        mActivity.getWindowManager().getDefaultDisplay().getSize(displaySize);
        maxDisplayWidth = displaySize.x;
        maxDisplayHeight = displaySize.y;

   //     Log.d(TAG, "Display size X:" + maxDisplayWidth + " Y:" + maxDisplayHeight);

        mLinearLayout = (RelativeLayout) findViewById(R.id.drag_layout);

        drag_button = (ImageView) findViewById(R.id.drag_btn);
        drag_button.setDrawingCacheEnabled(true);
        drag_button.setOnClickListener(this);
        drag_button.setOnLongClickListener(this);
        list_button = (ImageView)findViewById(R.id.listview_btn);
        list_button.setOnClickListener(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                mActivity.getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(com.google.android.gms.location.places.Place place) {
                Location location = new Location("");
            //    location.setLatitude(place.getLatLng().latitude);
             //   location.setLongitude(place.getLatLng().longitude);


                update_newLatLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(update_newLatLng, 12);
                mGoogleMap.animateCamera(cameraUpdate);

   //             setCurrentLocation(location, place.getName().toString(), place.getAddress().toString());




                String positionAddress = getPositionAddress(place.getLatLng().latitude, place.getLatLng().longitude);

                String positionSnippet = " 위도:" + String.valueOf(form.format(place.getLatLng().latitude))
                        + " 경도:" + String.valueOf(form.format(place.getLatLng().longitude));

                StringBuilder strPosition = new StringBuilder("");
                strPosition.append(positionAddress);
                strPosition.append(positionSnippet);

                if (customMarker != null)
                    customMarker.remove();

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(update_newLatLng);
                markerOptions.title(positionAddress);
                markerOptions.snippet(positionSnippet);
                markerOptions.draggable(false);
                markerOptions.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                customMarker = mGoogleMap.addMarker(markerOptions);
                customMarker.showInfoWindow();

           }


            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
/*
        FloatingActionButton action1 = (FloatingActionButton)findViewById(R.id.action_1);
        action1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ListviewActivity.class);
                intent.putExtra("mMapInfo", mMapInfo);
                startActivity(intent);
            }
        });
        FloatingActionButton action2 = (FloatingActionButton)findViewById(R.id.action_2);
        action2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_share = new Intent(android.content.Intent.ACTION_SEND);
                intent_share.setType("image/*");
                Intent chooser = Intent.createChooser(intent_share, "친구에게 공유하기");
                startActivity(chooser);
            }
        });
        FloatingActionButton action3 = (FloatingActionButton)findViewById(R.id.action_3);
        action3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_setting = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(intent_setting);
            }
        });
        FloatingActionButton action4 = (FloatingActionButton)findViewById(R.id.action_4);
        action4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_folders = check_child_path();

                if (user_folders == null) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("새 폴더 확인");
                    alertDialog.setIcon(R.drawable.toad_app_icon);

                    // Setting Dialog Message
                    alertDialog.setMessage("\n경로 정보가 없습니다.");

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();
                } else {

                    if (user_folders.size() > 0) {

                        String[] array = user_folders.toArray(new String[user_folders.size()]);
                        final CustomDialog_2 dialog = new CustomDialog_2(mActivity, array, "새 폴더 리스트", "DB에 저장되지 않은 경로가 있습니다. 적용 하시겠습니까?");
                        dialog.setDialogListener(new DialogListener_2() {  // MyDialogListener 를 구현
                            @Override
                            public void onPositiveClicked() {
                                if ((check_netWork() == true) && (isOnline() == true)) {
                                    mProgressDialog = ProgressDialog.show(
                                            MainActivity.this, "처리중",
                                            "잠시만 기다려주세요..");
                                    mHandler.sendEmptyMessageDelayed(NEW_DB_SAVE, 0);
                                } else {
                                    Toast.makeText(getApplicationContext(), "네트워크를 연결해 주세요", Toast.LENGTH_SHORT).show();
                                }
                                dialog.cancel();
                            }

                            @Override
                            public void onNegativeClicked() {
                                // Write your code here to invoke NO event
                                Toast.makeText(getApplicationContext(), "취소 되었습니다", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }


                        });
                        dialog.show();

                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                        alertDialog.setTitle("새 폴더 확인");
                        alertDialog.setIcon(R.drawable.toad_app_icon);
                        // Setting Dialog Message
                        alertDialog.setMessage("\n최신 경로 입니다.");
                        // Setting Positive "Yes" Button
                        alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        // Showing Alert Message
                        alertDialog.show();
                    }
                }
            }
        });
        FloatingActionButton action5 = (FloatingActionButton)findViewById(R.id.action_5);
        action5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //            empty_folder.clear();

                for (int i = 0; i < mMapInfo.size(); i++) {
                    if (Integer.parseInt(mMapInfo.get(i).getreserved()) == 0) {
                        empty_folder.add(mMapInfo.get(i).getfolder()); //arraylist의 주소를  배열로 저장
                    }
                }

                String[] array = empty_folder.toArray(new String[empty_folder.size()]);

                if (array.length > 0) {
                    final CustomDialog_2 dialog = new CustomDialog_2(mActivity, array, "빈 폴더 정리", "정리 하시겠습니까?");

                    dialog.setDialogListener(new DialogListener_2() {  // MyDialogListener 를 구현
                        @Override
                        public void onPositiveClicked() {
                            boolean status = true;
                            Log.d(TAG, "custom dialog ok");
                            mProgressDialog = ProgressDialog.show(
                                    MainActivity.this, "처리중",
                                    "잠시만 기다려주세요..");

                            for (int i = 0; i < mMapInfo.size(); i++) {
                                if (Integer.parseInt(mMapInfo.get(i).getreserved()) == 0) {
                                    empty_folder.add(mMapInfo.get(i).getfolder()); //arraylist의 주소를  배열로 저장
                                    update_newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(i).getlatitude()), Double.parseDouble(mMapInfo.get(i).getlongitude()));
                                }
                            }

                            StringBuilder str = new StringBuilder("");

                            Log.d(TAG, "empty_folder length " + empty_folder.size());

                            for (int i = 0; i < empty_folder.size(); i++) {
                                if (deleteDir_looper(empty_folder.get(i)) == true) {
                                    for (int j = 0; j < mMapInfo.size(); j++) {
                                        if (mMapInfo.get(j).getfolder().contains(empty_folder.get(i))) {
                                            data_remove(j);
                                            str.append("삭제 성공: " + empty_folder.get(i) + "\n");
                                        }
                                    }
                                } else {
                                    str.append("삭제 실패: " + empty_folder.get(i) + "\n");
                                }
                            }

                            mHandler.sendEmptyMessageDelayed(SET_DB_MARKER, 0); //update new marker

                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(update_newLatLng, 10);
                            mGoogleMap.moveCamera(cameraUpdate);

                            empty_folder.clear();

                            mProgressDialog.dismiss();

                            Toast.makeText(MainActivity.this, str.toString(), Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onNegativeClicked() {
                            Log.d(TAG, "custom dialog cancel");
                            empty_folder.clear();
                            Toast.makeText(MainActivity.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

                        }

                    });
                    empty_folder.clear();
                    dialog.show();

                } else {
                    Toast.makeText(MainActivity.this, "빈 폴더가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        FloatingActionButton action6 = (FloatingActionButton)findViewById(R.id.action_6);
        action6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info_btn == 0) {
                    Toast.makeText(mActivity, "주소 알림 켜짐", Toast.LENGTH_LONG).show();
                    info_btn = 1;
                } else {
                    Toast.makeText(mActivity, "주소 알림 꺼짐", Toast.LENGTH_LONG).show();
                    mtextInfo.setText("");
        //            mcoordInfo.setText("");
                    info_btn = 0;
                }
            }
        });
*/
/*
        FloatingActionButton fab_1 = (FloatingActionButton) findViewById(R.id.fab_1);
        fab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_folders = check_child_path();

                if (user_folders == null) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("새 폴더 확인");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.home_key);
                    // Setting Dialog Message
                    alertDialog.setMessage("\n경로 정보가 없습니다.");

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();
                } else {

                    if (user_folders.size() > 0) {

                        String[] array = user_folders.toArray(new String[user_folders.size()]);
                        final CustomDialog_2 dialog = new CustomDialog_2(mActivity, array, "새 폴더 리스트", "DB에 저장되지 않은 경로가 있습니다. 적용 하시겠습니까?");
                        dialog.setDialogListener(new DialogListener_2() {  // MyDialogListener 를 구현
                            @Override
                            public void onPositiveClicked() {
                                if ((check_netWork() == true) && (isOnline() == true)) {
                                    mProgressDialog = ProgressDialog.show(
                                            MainActivity.this, "처리중",
                                            "잠시만 기다려주세요..");
                                    mHandler.sendEmptyMessageDelayed(NEW_DB_SAVE, 0);
                                } else {
                                    Toast.makeText(getApplicationContext(), "네트워크를 연결해 주세요", Toast.LENGTH_SHORT).show();
                                }
                                dialog.cancel();
                            }

                            @Override
                            public void onNegativeClicked() {
                                // Write your code here to invoke NO event
                                Toast.makeText(getApplicationContext(), "취소 되었습니다", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }


                        });
                        dialog.show();

                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                        alertDialog.setTitle("새 폴더 확인");

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.home_key);
                        // Setting Dialog Message
                        alertDialog.setMessage("\n최신 경로 입니다.");
                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.common_google_signin_btn_icon_dark);
                        // Setting Positive "Yes" Button
                        alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        // Showing Alert Message
                        alertDialog.show();
                    }
                }

            }
        });

        FloatingActionButton fab_2 = (FloatingActionButton) findViewById(R.id.fab_2);
        fab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        //            empty_folder.clear();

                for (int i = 0; i < mMapInfo.size(); i++) {
                    if (Integer.parseInt(mMapInfo.get(i).getreserved()) == 0) {
                        empty_folder.add(mMapInfo.get(i).getfolder()); //arraylist의 주소를  배열로 저장
                    }
                }

                String[] array = empty_folder.toArray(new String[empty_folder.size()]);

                if (array.length > 0) {
                    final CustomDialog_2 dialog = new CustomDialog_2(mActivity, array, "빈 폴더 정리", "정리 하시겠습니까?");

                    dialog.setDialogListener(new DialogListener_2() {  // MyDialogListener 를 구현
                        @Override
                        public void onPositiveClicked() {
                            boolean status = true;
                            Log.d(TAG, "custom dialog ok");
                            mProgressDialog = ProgressDialog.show(
                                    MainActivity.this, "처리중",
                                    "잠시만 기다려주세요..");

                            for (int i = 0; i < mMapInfo.size(); i++) {
                                if (Integer.parseInt(mMapInfo.get(i).getreserved()) == 0) {
                                    empty_folder.add(mMapInfo.get(i).getfolder()); //arraylist의 주소를  배열로 저장
                                    update_newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(i).getlatitude()), Double.parseDouble(mMapInfo.get(i).getlongitude()));
                                }
                            }

                            StringBuilder str = new StringBuilder("");

                            Log.d(TAG, "empty_folder length " + empty_folder.size());

                            for (int i = 0; i < empty_folder.size(); i++) {
                                if (deleteDir_looper(empty_folder.get(i)) == true) {
                                    for (int j = 0; j < mMapInfo.size(); j++) {
                                        if (mMapInfo.get(j).getfolder().contains(empty_folder.get(i))) {
                                            data_remove(j);
                                            str.append("삭제 성공: " + empty_folder.get(i) + "\n");
                                        }
                                    }
                                } else {
                                    str.append("삭제 실패: " + empty_folder.get(i) + "\n");
                                }
                            }

                            mHandler.sendEmptyMessageDelayed(SET_DB_MARKER, 0); //update new marker

                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(update_newLatLng, 10);
                            mGoogleMap.moveCamera(cameraUpdate);

                            empty_folder.clear();

                            mProgressDialog.dismiss();

                            Toast toast = Toast.makeText(mActivity, str.toString(), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                            toast.show();
                        }

                        @Override
                        public void onNegativeClicked() {
                            Log.d(TAG, "custom dialog cancel");
                            empty_folder.clear();
                            Toast toast = Toast.makeText(mActivity, "취소", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                            toast.show();
                        }

                    });
                    empty_folder.clear();
                    dialog.show();

                } else {
                    Toast toast = Toast.makeText(mActivity, "빈 폴더가 없습니다.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                    toast.show();
                }
            }
        });


        FloatingActionButton fab_3 = (FloatingActionButton) findViewById(R.id.fab_3);
        fab_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (info_btn == 0) {
                    Toast.makeText(mActivity, "주소 알림 켜짐", Toast.LENGTH_LONG).show();
                    info_btn = 1;
                } else {
                    Toast.makeText(mActivity, "주소 알림 꺼짐", Toast.LENGTH_LONG).show();
                    mtextInfo.setText("");
                    mcoordInfo.setText("");
                    info_btn = 0;
                }
            }
        });
*/

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
          ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                  this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
          drawer.addDrawerListener(toggle);
          toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


     //   mtextInfo = (TextView)findViewById(R.id.position_info);

    //    mcoordInfo = (TextView)findViewById(R.id.coord_info) ;

        inter_line_info = (ImageView) findViewById(R.id.inter_line_info);
        info_btn_info = (ImageView) findViewById(R.id.info_btn_info);

/*
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
*/
        setAds();

    }
    private void set_btns() {


        Glide.get(mActivity).clearMemory();
        if (default_cam == 1) {
            Glide.with(mActivity)
                    //       .load(R.drawable.camera_180901)
                    .load(R.drawable.camera_key)
                    //       .apply(RequestOptions.bitmapTransform(new CenterCrop()))
                    .into(drag_button);
        } else {
            Glide.with(mActivity)
                    .load(R.drawable.camera)
                    //       .apply(RequestOptions.bitmapTransform(new CenterCrop()))
                    .into(drag_button);
        }

        Glide.with(mActivity)
                .load(R.drawable.listbutton)
                .into(list_button);

        //      menu_button = (ImageButton) findViewById(R.id.menu_btn);
        //      menu_button.setOnClickListener(this);

        /*

        Button button = (Button)findViewById(R.id.button_search);
        button.setOnClickListener(this);

        Button button_track = (Button)findViewById(R.id.button_track);
        button_track.setOnClickListener(this);

        Button button_trans = (Button)findViewById(R.id.button_trans);
        button_trans.setOnClickListener(this);

        Button button_custom = (Button)findViewById(R.id.button_custom);
        button_custom.setOnClickListener(this);

        Button button_db = (Button)findViewById(R.id.button_db);
        button_db.setOnClickListener(this);

        Button button_db_delete = (Button)findViewById(R.id.button_db_delete);
        button_db_delete.setOnClickListener(this);
        */

    }
    private void setAds() {
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
 //        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
 //        .addTestDevice("86AFEB741F059FAD6147AAA3A2B25BD4")
         .build();
        mAdView.loadAd(adRequest);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {

            case R.id.menu1:
                ImageView imageView_menu1 = (ImageView)item.getActionView();

                if (inter_line == 0) {

                    imageView_menu1.setImageResource(R.drawable.ic_action_info);
                    inter_line_info.setImageResource(R.drawable.inter_line);
                    inter_line = 1;
            //        Toast.makeText(mActivity, "관계선 보기 켜짐", Toast.LENGTH_LONG).show();
                } else {

                    imageView_menu1.setImageResource(R.color.transparent);
                    inter_line_info.setImageResource(R.color.transparent);
            //        Toast.makeText(mActivity, "관계선 보기 꺼짐", Toast.LENGTH_LONG).show();
                    inter_line = 0;
                    if (arrayPoints_gray.size() > 0) {
                        if (animator.isRunning())
                            animator.cancel();
                        arrayPoints_gray.clear();

                        for (Polyline line : polyline) {
                            line.remove();
                        }
                        polyline.clear();

                    }
                }
                break;

            case R.id.menu3:
                ImageView imageView_menu3 = (ImageView)item.getActionView();

                if (info_btn == 0) {
                    imageView_menu3.setImageResource(R.drawable.ic_action_info);
                    info_btn_info.setImageResource(R.drawable.show_pos);
              //      Toast.makeText(mActivity, "주소 알림 켜짐", Toast.LENGTH_LONG).show();
                    info_btn = 1;
                } else {
                    imageView_menu3.setImageResource(R.color.transparent);
                    info_btn_info.setImageResource(R.color.transparent);
            //        Toast.makeText(mActivity, "주소 알림 꺼짐", Toast.LENGTH_LONG).show();
          //          mtextInfo.setText("");
                    //          mcoordInfo.setText("");
                    info_btn = 0;
                    if (coordMarker != null)
                        coordMarker.remove();
                }
                break;
                /*
            case R.id.menu4:
                Intent intent_share = new Intent(android.content.Intent.ACTION_SEND);
                intent_share.setType("image/*");
                Intent chooser = Intent.createChooser(intent_share, "친구에게 공유하기");
                startActivity(chooser);
                break;
                */
            case R.id.menu5:
                Intent intent_setting = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(intent_setting);
                break;
                /*
            case R.id.menu6:

                Intent intent_backup = new Intent(getBaseContext(), GoogleSignInActivity.class);
                intent_backup.putExtra("data", "photomap_data.db");
                intent_backup.putExtra("setting", "photomap_setting.db");
                intent_backup.putExtra("memo", "photomap_memo.db");
                startActivity(intent_backup);

                break;
                */
            case R.id.menu2_1:

                user_folders_internal = check_child_path(imagesView.INTERNAL_STORAGE);
                user_folders_external = check_child_path(imagesView.EXTERNAL_STORAGE);

                if (user_folders_internal.size() == 0 && user_folders_external.size() == 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("새 폴더 확인");
                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.toad_app_icon_v4);
                    // Setting Dialog Message
                    alertDialog.setMessage("\n경로 정보가 없습니다.");

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();
                } else {

                    if (user_folders_internal.size() > 0 || user_folders_external.size() > 0) {
                        ArrayList<String> combined_array = combine_storage_path(user_folders_internal, user_folders_external);

                        String[] array = combined_array.toArray(new String[combined_array.size()]);

                        if (array.length != 0) {

                            final CustomDialog_2 dialog = new CustomDialog_2(mActivity, array, "새 폴더 리스트", "DB에 저장되지 않은 경로가 있습니다. 적용 하시겠습니까?");
                            dialog.setDialogListener(new DialogListener_2() {  // MyDialogListener 를 구현
                                @Override
                                public void onPositiveClicked() {
                                    if ((check_netWork() == true) && (isOnline() == true)) {
                                        mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                                PROGRESS,
                                                START, 0), 0);
                                        AddFolderTask addFolderTask = new AddFolderTask(mActivity);
                                        addFolderTask.execute();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "네트워크를 연결해 주세요", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.cancel();
                                }

                                @Override
                                public void onNegativeClicked() {
                                    // Write your code here to invoke NO event
                                    Toast.makeText(getApplicationContext(), "취소 되었습니다", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });
                            dialog.show();
                        } else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                            alertDialog.setTitle("새 폴더 확인");

                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.drawable.toad_app_icon_v4);
                            // Setting Dialog Message
                            alertDialog.setMessage("\n     최신 경로 입니다.");

                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            // Showing Alert Message
                            alertDialog.show();
                        }
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                        alertDialog.setTitle("새 폴더 확인");

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.toad_app_icon_v4);
                        // Setting Dialog Message
                        alertDialog.setMessage("\n     최신 경로 입니다.");

                        // Setting Positive "Yes" Button
                        alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        // Showing Alert Message
                        alertDialog.show();
                    }
                }

                break;
            case R.id.menu2_2:

                empty_folder.clear();
                ArrayList<String> delete_list = new ArrayList<>();
                try {
                    String[][] return_strings = check_empty_folder();
              //      Log.d(TAG, "return_string: " + return_strings.length);

                    for (int i = 0; i < return_strings.length; i++) {
                   //     Log.d(TAG, "emptyh_folders: " + return_strings[i][0] + " storage: " + return_strings[i][1]);
                        if (return_strings[i][0] != null) {
                            String storage = null;
                            Integer storage_value = Integer.parseInt(return_strings[i][1]);
                            storage = (storage_value == imagesView.BOTH_STORAGE ? "내/외장" : (storage_value == ImagesView.INTERNAL_STORAGE ? "내장" : "외장"));
                            delete_list.add(return_strings[i][0] + " <" + storage + ">");
                            empty_folder.add(return_strings[i][0] + ":" + storage_value);
                        }
                    }







/*
                for (int i = 0; i < mMapInfo.size(); i++) {
                    if (Integer.parseInt(mMapInfo.get(i).getreserved()) == 0) {
                        empty_folder.add(mMapInfo.get(i).getfolder()); //arraylist의 주소를  배열로 저장
                    }
                }
*/


                String[] array = delete_list.toArray(new String[delete_list.size()]);

                if (array.length > 0) {
                    final CustomDialog_2 dialog = new CustomDialog_2(mActivity, array, "빈 폴더 정리", "정리 하시겠습니까?");

                    dialog.setDialogListener(new DialogListener_2() {  // MyDialogListener 를 구현
                        @Override
                        public void onPositiveClicked() {

                            try {
                                mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                        PROGRESS,
                                        START, 0), 0);

              //                  Log.d(TAG, "custom dialog ok");
                                DeleteFolderTask deleteFolderTask = new DeleteFolderTask(mActivity);
                                deleteFolderTask.execute();
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, "삭제 실패 했습니다.", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onNegativeClicked() {
           //                 Log.d(TAG, "custom dialog cancel");
                            empty_folder.clear();
          //                  Toast.makeText(MainActivity.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

                        }

                    });
            //        empty_folder.clear();
                    dialog.show();

                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("빈 폴더 확인");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.toad_app_icon_v4);
                    // Setting Dialog Message
                    alertDialog.setMessage("\n     빈 폴더가 없습니다.");

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();

                }







                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }

        if (id != R.id.menu1 && id != R.id.menu3) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }


    public String[][] check_empty_folder () {

        String[][] strings = new String[mMapInfo.size()][2];
        for (int i = 0; i < mMapInfo.size(); i++) {

            if (Integer.parseInt(mMapInfo.get(i).getStorage()) != imagesView.BOTH_STORAGE) {
                if (Integer.parseInt(mMapInfo.get(i).getreserved()) == 0) {
                    strings[i][0] = mMapInfo.get(i).getfolder();
                    strings[i][1] = mMapInfo.get(i).getStorage();
                }
            } else {

                File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + Appdir, mMapInfo.get(i).getfolder());
                File[] files = mediaStorageDir.listFiles();

                File external_directoryPath = new File(sdcard_path_header + Appdir, mMapInfo.get(i).getfolder());
                File[] external_files = external_directoryPath.listFiles();

                if (files != null && external_files != null) {
                    if (files.length ==0 && external_files.length == 0) {
                        strings[i][0] = mMapInfo.get(i).getfolder();
                        strings[i][1] = Integer.toString(ImagesView.BOTH_STORAGE);
                    } else {
                        if (files.length == 0) {
                            strings[i][0] = mMapInfo.get(i).getfolder();
                            strings[i][1] = Integer.toString(ImagesView.INTERNAL_STORAGE);
                        }
                        if (external_files.length == 0) {
                            strings[i][0] = mMapInfo.get(i).getfolder();
                            strings[i][1] = Integer.toString(ImagesView.EXTERNAL_STORAGE);
                        }
                    }

                } else {
                    if (files != null) {
                        if (files.length == 0) {
                            strings[i][0] = mMapInfo.get(i).getfolder();
                            strings[i][1] = Integer.toString(ImagesView.INTERNAL_STORAGE);
                        }
                    }
                    if (external_files != null) {
                        if (external_files.length == 0) {
                            strings[i][0] = mMapInfo.get(i).getfolder();
                            strings[i][1] = Integer.toString(ImagesView.EXTERNAL_STORAGE);
                        }
                    }
                }

            }

        }

        return strings;

    }


    public static int check_files (String path) {


        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + Appdir, path);
        File[] files = mediaStorageDir.listFiles();

        File external_directoryPath = new File(sdcard_path_header + Appdir, path);
        File[] external_files = external_directoryPath.listFiles();

   //     Log.d(TAG, path + " : " + String.valueOf(files.length) + "pics");

        if (files == null && external_files == null)
            return -1;

        if (files != null && external_files != null) {
            return files.length + external_files.length;
        } else if (external_files == null) {
            return files.length;
        } else if (files == null) {
            return external_files.length;
        }

        return -1;
    }

    public static File[] check_files_list (String path, int focus_storage) {

        File mediaStorageDir;
        if (focus_storage == imagesView.INTERNAL_STORAGE)
            mediaStorageDir = new File(Environment.getExternalStorageDirectory() + Appdir, path);
        else
            mediaStorageDir = new File(sdcard_path_header + Appdir, path);

        File[] files = mediaStorageDir.listFiles();

        //     Log.d(TAG, path + " : " + String.valueOf(files.length) + "pics");

        return files;
    }



/*


    public static String delete_folders_external(Context context, String path) {
        DocumentFile pickedDir = null;
        String root_path = get_main_path();

        try {
            Log.d(TAG, "root_path: " + root_path + " path: " + path);
            if (root_path != null) {
                Uri folder_uri = Uri.parse(root_path);
                pickedDir = DocumentFile.fromTreeUri(context, folder_uri); //find root
                List<String> pathParts = Arrays.asList(path.split("/")); //split folder layer
                for (int i = 1; i < pathParts.size(); i++) {
                    DocumentFile nextDoc = null;
                    if (fileController.file_format_check(pathParts.get(i)) == 0) {
                        nextDoc = pickedDir.findFile(pathParts.get(i));

                        if (nextDoc != null) {
                            pickedDir = nextDoc;

                                if (i == pathParts.size() - 1) {
                                    pickedDir.delete();
                                    Log.d(TAG, "deleted: " + pickedDir.getUri().getPath());
                                }


                        } else {
                        }
                    } else {

                            nextDoc = pickedDir.findFile(pathParts.get(i));
                            if (nextDoc != null) {
                                pickedDir = nextDoc;
                            }
                            if (i == pathParts.size() - 1) {
                                pickedDir.delete();
                                Log.d(TAG, "deleted: " + pickedDir.getUri().getPath());
                            }

                    }

                    Log.d(TAG, "path(" + i + ")" + pathParts.get(i) + " nextDoc" + nextDoc.getName()
                            + "nextDoc.canWrite:" + nextDoc.canWrite());

                }

            }

            return pickedDir.toString();
        } catch (Exception e) {
            return null;
        }
    }
*/


    public static boolean delete_folders_auto(String path) {
        Log.d(TAG, " delete_folders_auto: " + path);
        boolean value = false;
        DocumentFile return_status = null;
        File mediaStorageDir;
        List<String> split_str = Arrays.asList(path.split(":"));

        if (Integer.parseInt(split_str.get(1)) == ImagesView.BOTH_STORAGE) {
            mediaStorageDir = new File(Environment.getExternalStorageDirectory() + Appdir, split_str.get(0));
            value = mediaStorageDir.delete();
            return_status = fileController.folder_checker(mActivity, get_main_path(), Appdir + File.separator + split_str.get(0), fileController.DELETE_DIR);


        } else {
            if (Integer.parseInt(split_str.get(1)) == ImagesView.INTERNAL_STORAGE) {
                mediaStorageDir = new File(Environment.getExternalStorageDirectory() + Appdir, split_str.get(0));
                value = mediaStorageDir.delete();
            } else {
                return_status = fileController.folder_checker(mActivity, get_main_path(), Appdir + File.separator + split_str.get(0), fileController.DELETE_DIR);            }
        }


        if (value == true || return_status != null)
            value = true;
        else
            value = false;

        return value;
    }



    public static boolean delete_folders(String path, int storage_status) {
   //     Log.d(TAG, " delete_folders: " + path);
        boolean value = false;
        DocumentFile return_status = null;

        if (storage_status == imagesView.INTERNAL_STORAGE) {
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + Appdir, path);
            value = mediaStorageDir.delete();
        }
    //    String root_path = get_main_path();
        if (storage_status == imagesView.EXTERNAL_STORAGE) {
   //         return_status = delete_folders_external(mActivity, Appdir + File.separator + path);
            return_status = fileController.folder_checker(mActivity, get_main_path(), Appdir + File.separator + path, fileController.DELETE_DIR);
            //   return_status = fileController.file_controller(mActivity, Appdir, null, path, fileController.DELETE_FILE);
        }

        if (storage_status == imagesView.BOTH_STORAGE) {
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + Appdir, path);
            value = mediaStorageDir.delete();
            return_status = fileController.folder_checker(mActivity, get_main_path(), Appdir + File.separator + path, fileController.DELETE_DIR);

//            return_status = delete_folders_external(mActivity, Appdir + File.separator + path);
        }

        if (value == true || return_status != null)
            value = true;
        else
            value = false;

        return value;
    }

    public boolean moveFile(Context context, String file_path, String destFolder, int from_storage, int to_storage) {
    //    Log.d(TAG, "from: " + file_path + " to: " + destFolder + "<" + from_storage + "/" + to_storage +">");
        File mediaStorageDir;
        File mFile = null;
        File mDestFolder = null;

        mFile = new File(file_path);

        if (to_storage == imagesView.INTERNAL_STORAGE) {
            mDestFolder = new File(Environment.getExternalStorageDirectory() + Appdir, destFolder);
        } else {
            mDestFolder = new File(sdcard_path_header + Appdir, destFolder);
        }

        if (to_storage == imagesView.INTERNAL_STORAGE) {
            try {

                if (from_storage == imagesView.EXTERNAL_STORAGE) {
                    copyFile(file_path, destFolder);
                    deleteFileFromMediaStore(context, mFile);
                } else {
                    FileUtils.moveFileToDirectory(mFile, mDestFolder, true);
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        } else {

        }
        return true;
    }

    public boolean moveFileDelete(Context context, String file_path, String destFolder) {
   //     Log.d(TAG, "from: " + file_path + " to: " + destFolder);
        File mFile = new File(file_path);
        File mDestFolder = new File(Environment.getExternalStorageDirectory() + Appdir, destFolder);
        try {
            FileUtils.copyFileToDirectory(mFile, mDestFolder, true);
            deleteFileFromMediaStore(context, mFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public void deleteFileFromMediaStore(Context context, File file) {
 //       Log.d(TAG, "deleteFileFromMediaStore" + file.toString());
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            String canonicalPath;
            try {
                canonicalPath = file.getCanonicalPath();
            } catch (IOException e) {
                canonicalPath = file.getAbsolutePath();
            }
     //       Log.d(TAG, "canonicalPath: " +canonicalPath);
            final Uri uri = MediaStore.Files.getContentUri("external");
            try {
                final int result = context.getContentResolver().delete(uri,
                        MediaStore.Files.FileColumns.DATA + "=?", new String[]{canonicalPath});
                if (result == 0) {
                    final String absolutePath = file.getAbsolutePath();
                    if (!absolutePath.equals(canonicalPath)) {
                        context.getContentResolver().delete(uri,
                                MediaStore.Files.FileColumns.DATA + "=?", new String[]{absolutePath});
                    }
                }
        //        context.getContentResolver().notifyChange(uri, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean copyFile(String file_path, String destFolder) {
    //    Log.d(TAG, "from: " + file_path + " to: " + destFolder);
        File mFile = new File(file_path);
        File mDestFolder = new File(Environment.getExternalStorageDirectory() + Appdir, destFolder);
        try {
            FileUtils.copyFileToDirectory(mFile, mDestFolder, true);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static void copyFileToMediaStore(Context context, File file) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            String canonicalPath;
            try {
                canonicalPath = file.getCanonicalPath();
            } catch (IOException e) {
                canonicalPath = file.getAbsolutePath();
            }
    //        Log.d(TAG, "canonicalPath: " +canonicalPath);
            final Uri uri = MediaStore.Files.getContentUri("external");
            try {
                final int result = context.getContentResolver().delete(uri,
                        MediaStore.Files.FileColumns.DATA + "=?", new String[]{canonicalPath});
                if (result == 0) {
                    final String absolutePath = file.getAbsolutePath();
                    if (!absolutePath.equals(canonicalPath)) {
                        context.getContentResolver().delete(uri,
                                MediaStore.Files.FileColumns.DATA + "=?", new String[]{absolutePath});
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean moveFolder(String srcPath, String destPath, boolean isMoveRoot) {
//        Log.d(TAG, "srcPath:" + srcPath + " destPath:" + destPath);
    //    File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + Appdir, srcPath);
    //    File[] files = mediaStorageDir.listFiles();

        File srcFolder = new File(Environment.getExternalStorageDirectory() + Appdir, srcPath);
        File destFolder = new File(Environment.getExternalStorageDirectory() + Appdir, destPath);

  //      Log.d(TAG, "srcPath:" + srcFolder + " destPath:" + destFolder);

        File[] files = srcFolder.listFiles();

        try {
            if(isMoveRoot) {
                for (int i = 0; i < files.length; i++) {
                    FileUtils.moveFileToDirectory(files[i], destFolder, true);
                }
            //    FileUtils.moveDirectoryToDirectory(new File(Environment.getExternalStorageDirectory() + Appdir, srcPath), new File(Environment.getExternalStorageDirectory() + Appdir, destPath), true);
            } else {
                FileUtils.moveDirectory(new File(Environment.getExternalStorageDirectory() + Appdir, srcPath), new File(Environment.getExternalStorageDirectory() + Appdir, destPath));
            }

        //    FileUtils.moveFileToDirectory(files, new File(Environment.getExternalStorageDirectory() + Appdir, destPath), true);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
//            Log.d(TAG, e.toString());
            return false;
        }

    }


    public int init_database() {
        int ret = 0;

        File file = new File(getFilesDir(), "photomap_data.db") ;
     //   Log.d(TAG, "PATH: " +file.toString());
        try {
            sqliteDB_data = SQLiteDatabase.openOrCreateDatabase(file, null) ;
        } catch (SQLiteException e) {
            ret = -1;
            e.printStackTrace();
        }

        if (sqliteDB_data == null) {
     //       Log.d(TAG, "DB create failed: " +file.getAbsolutePath());
            ret = -1;
        } else {
     //       Log.d(TAG, "DB load success: " +file.getAbsolutePath());
        }

        file = new File(getFilesDir(), "photomap_setting.db");
 //       Log.d(TAG, "PATH: " +file.toString());
        try {
            sqliteDB_setting = SQLiteDatabase.openOrCreateDatabase(file, null) ;
        } catch (SQLiteException e) {
            ret = -1;
            e.printStackTrace() ;
        }

        if (sqliteDB_setting == null) {
   //         Log.d(TAG, "DB create failed: " +file.getAbsolutePath());
            ret = -1;
        } else {
     //       Log.d(TAG, "DB load success: " +file.getAbsolutePath());
        }

        file = new File(getFilesDir(), "photomap_memo.db") ;
    //    Log.d(TAG, "PATH: " +file.toString());
        try {
            sqliteDB_memo = SQLiteDatabase.openOrCreateDatabase(file, null) ;
        } catch (SQLiteException e) {
            e.printStackTrace() ;
            ret = -1;
        }

        if (sqliteDB_memo == null) {
   //         Log.d(TAG, "DB create failed: " +file.getAbsolutePath());
            ret = -1;
        } else {
   //        Log.d(TAG, "DB load success: " +file.getAbsolutePath());
        }

        return ret ;
    }

    public void init_tables() {
  //      Log.d(TAG, "init_tables");
        dbHelper = new ContactDBHelper(this);
        settingDBHelper = new SettingDBHelper(this);
        memoDBHelper = new MemoDBHelper(this);
    }
    private static File mediaStorageDir;
    public int load_values() {

            sqliteDB_data = dbHelper.getWritableDatabase();
            sqliteDB_setting = settingDBHelper.getWritableDatabase();
            Cursor cursor = sqliteDB_data.rawQuery(ContactDBCtrct.SQL_SELECT, null);
            Cursor setting_cursor = sqliteDB_setting.rawQuery(SettingDBCtrct.SQL_SELECT, null);

        if (setting_cursor.moveToFirst()) {
     //       Log.d(TAG, "Setting DB loading");

            Log.d(TAG,"Setting DB: " + setting_cursor.getString(setting_cursor.getColumnIndex("_id")) +
                    " PATH: " + setting_cursor.getString(setting_cursor.getColumnIndex("PATH")) +
                            " LATITUDE: " + setting_cursor.getString(setting_cursor.getColumnIndex("LATITUDE")) +
                            " LONGITUDE: " + setting_cursor.getString(setting_cursor.getColumnIndex("LONGITUDE")) +
                            " XCORD: " + setting_cursor.getString(setting_cursor.getColumnIndex("XCORD")) +
                            " YCORD: " + setting_cursor.getString(setting_cursor.getColumnIndex("YCORD")) +
                            " MARKER: " + setting_cursor.getString(setting_cursor.getColumnIndex("MARKER")) +
                            " LEVEL: " + setting_cursor.getString(setting_cursor.getColumnIndex("LEVEL")) +
                    " STORAGE: " + setting_cursor.getString(setting_cursor.getColumnIndex("STORAGE_STATUS")) +
                    " MAP: " + setting_cursor.getString(setting_cursor.getColumnIndex("MAP_STATUS")));

            /* setting db에서 로딩 */
   //         marker_choice = Integer.parseInt(setting_cursor.getString(setting_cursor.getColumnIndex("MARKER")));
            default_cam = Integer.parseInt(setting_cursor.getString(setting_cursor.getColumnIndex("LEVEL")));
            storage_status = Integer.parseInt(setting_cursor.getString(setting_cursor.getColumnIndex("STORAGE_STATUS")));
            x_coord = Float.parseFloat(setting_cursor.getString(setting_cursor.getColumnIndex("XCORD")));
            y_coord = Float.parseFloat(setting_cursor.getString(setting_cursor.getColumnIndex("YCORD")));

            if ((x_coord == 0) && (y_coord == 0)) {
    //            Log.d(TAG, "drag_btn set default position");
                x_coord = maxDisplayWidth / 2;
                y_coord = maxDisplayHeight - 400;
            }

            if (!((setting_cursor.getString(setting_cursor.getColumnIndex("LATITUDE")).contains("null")) ||
            setting_cursor.getString(setting_cursor.getColumnIndex("LONGITUDE")).contains("null"))) {
                if (!((setting_cursor.getString(setting_cursor.getColumnIndex("LATITUDE")).contains("latitude")) ||
                        setting_cursor.getString(setting_cursor.getColumnIndex("LONGITUDE")).contains("longitude"))) {
           //         Log.d(TAG, "last LatLng loaded");
                    try {
                        currentPosition
                                = new LatLng(Double.parseDouble(setting_cursor.getString(setting_cursor.getColumnIndex("LATITUDE"))),
                                Double.parseDouble(setting_cursor.getString(setting_cursor.getColumnIndex("LONGITUDE"))));
                    } catch (Exception e) {
             //           Log.d(TAG, "Failed to load currentPosition");
                        e.printStackTrace();
                    }
                } else {
              //      Log.d(TAG, "contains latitude or longitude string");
                }
            } else {
          //      Log.d(TAG, "contains null string");
            }

//            Log.d(TAG, "x: " + x_coord + " y: " +y_coord);
            int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        200, 200); //이값이 왜 200인지 모르겠음
                //       drag_button.setPadding((int) me.getRawX(), (int) me.getRawY(), 0,0); //this is not working fine.
                layoutParams.setMargins((int) x_coord - 100,
                        (int) y_coord - 200, 0, 0);
                mLinearLayout.removeView(drag_button);
                mLinearLayout.addView(drag_button, layoutParams);
                //drag_button.setImageResource(R.drawable.camera_key);
                drag_button.invalidate();
            }

            mSettingInfo.clear(); //array 초기화
            mSettingInfo_class = new SettingInfo(
                    setting_cursor.getString(setting_cursor.getColumnIndex("_id")),
                    setting_cursor.getString(setting_cursor.getColumnIndex("PATH")),
                    setting_cursor.getString(setting_cursor.getColumnIndex("LATITUDE")),
                    setting_cursor.getString(setting_cursor.getColumnIndex("LONGITUDE")),
                    Float.toString(x_coord),
                    Float.toString(y_coord),
                    setting_cursor.getString(setting_cursor.getColumnIndex("MARKER")),
                    setting_cursor.getString(setting_cursor.getColumnIndex("LEVEL")),
                    setting_cursor.getString(setting_cursor.getColumnIndex("STORAGE_STATUS")),
                    setting_cursor.getString(setting_cursor.getColumnIndex("MAP_STATUS"))
            );
            mSettingInfo.add(mSettingInfo_class); //array list에 data 전달

            for (int i = 0; i < mSettingInfo.size(); i++) {
                Log.d(TAG, "Setting DB: " + mSettingInfo.get(i).getid() +
                ", path " + mSettingInfo.get(i).getPath() +
                ", Lat " + mSettingInfo.get(i).getLatitude() +
                ", Lon " + mSettingInfo.get(i).getLongitude() +
                ", Xcord " + mSettingInfo.get(i).getXcord() +
                ", YCord " + mSettingInfo.get(i).getYcord() +
                ", marker " + mSettingInfo.get(i).getMarker() +
                ", level " + mSettingInfo.get(i).getLevel() +
                ", storage_status " + mSettingInfo.get(i).getStorage_status());

            }


        } else {
       //     Log.d(TAG, "setting db count: " + setting_cursor.getCount());
        }



        /* Folder Main data */
        //    Log.d(TAG, "columm index " + cursor.getColumnName(cursor.getColumnIndex("PATH")));
        //     int mcursor = 0;
        db_cnt = cursor.getCount();
        mMapInfo.clear(); //array 초기화
     //   Log.d(TAG,"load_values: db count " + Integer.toString(db_cnt));

        try {
            if (cursor.moveToFirst()) {
         //       Log.d(TAG, "Main DB loading");
                //          Log.d(TAG, "getPosition" + cursor.getPosition() + "ColummCount" + cursor.getColumnCount() + " getdata:" + cursor.getString(1));
                mMapInfo_class = new MapInfo(
                        cursor.getString(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("PATH")),
                        cursor.getString(cursor.getColumnIndex("LATITUDE")),
                        cursor.getString(cursor.getColumnIndex("LONGITUDE")),
                        cursor.getString(cursor.getColumnIndex("INFO")),
                        cursor.getString(cursor.getColumnIndex("FAV")),
                        cursor.getString(cursor.getColumnIndex("DATA")),
                        cursor.getString(cursor.getColumnIndex("LEVEL")),
                        cursor.getString(cursor.getColumnIndex("STORAGE"))
                );
                mMapInfo.add(mMapInfo_class); //array list에 data 전달

                while (cursor.moveToNext()) {
                    //            Log.d(TAG, "getPosition" + cursor.getPosition() + "ColummCount" + cursor.getColumnCount() + " getdata:" + cursor.getString(1));
                    mMapInfo_class = new MapInfo(
                            cursor.getString(cursor.getColumnIndex("_id")),
                            cursor.getString(cursor.getColumnIndex("PATH")),
                            cursor.getString(cursor.getColumnIndex("LATITUDE")),
                            cursor.getString(cursor.getColumnIndex("LONGITUDE")),
                            cursor.getString(cursor.getColumnIndex("INFO")),
                            cursor.getString(cursor.getColumnIndex("FAV")),
                            cursor.getString(cursor.getColumnIndex("DATA")),
                            cursor.getString(cursor.getColumnIndex("LEVEL")),
                            cursor.getString(cursor.getColumnIndex("STORAGE"))
                    );
                    mMapInfo.add(mMapInfo_class); //array list에 data 전달
                }
            }


            try {
                boolean external_status = hasRealRemovableSdCard(mActivity);
                if (external_status == true) {


                    FileController fileController = new FileController();
                    String root_path = get_main_path();
                    if (root_path != null) {
                        String path = Uri.parse(root_path).getPath();
                        //           Log.d(TAG, "isExternalStorageWritable: " + fileController.isExternalStorageWritable() + " root_path: " + path);

                        List<String> exist_path = new ArrayList<>();
                        External_Path = fileController.return_External_path(mActivity);
                        if (root_path.equals("")) {

                        } else {
                            List<String> split_str = new ArrayList<>();
                            split_str = Arrays.asList(path.split(":"));

                            sdcard_path_header = External_Path + File.separator + split_str.get(split_str.size() - 1);

                            //              Log.d(TAG, "sdcard_path_header: " + sdcard_path_header);
                        }
                    }
                }
     //           Log.d(TAG, "External dirs status: " + external_status);
            } catch (Exception e) {
                e.printStackTrace();
            }



            if (mSettingInfo.size() > 0) {
                if (Integer.parseInt(mSettingInfo.get(0).getStorage_status()) >= imagesView.EXTERNAL_STORAGE) {
                    if (settings_storage_activity.check_authority_external(mActivity) == false) {
                        Toast.makeText(mActivity, "SD카드 접근 권한이 없습니다. 설정에서 저장소 위치 설정 후 \n 다시 시도해 주세요.", Toast.LENGTH_LONG).show();
                        setting_cursor.close();
                        cursor.close();
                        return -1;
                    }
                }
            }


            for (int i = 0; i < mMapInfo.size(); i++) {
/*
                Log.d(TAG, "Main DB: "
                        + mMapInfo.get(i).getid()
                        + ", " + mMapInfo.get(i).getfolder()
                        + " (위도:" + mMapInfo.get(i).getlatitude()
                        + " 경도:" + mMapInfo.get(i).getlongitude()
                        + ") " + mMapInfo.get(i).getreserved()
                        + " pics" +
                        " fav:" + mMapInfo.get(i).getFav()
                        + " data:" + mMapInfo.get(i).getdata()
                        + " level:" + mMapInfo.get(i).getlevel()
                        + " storage:" + mMapInfo.get(i).getStorage());
*/

                    try {
                        if (Integer.parseInt(mMapInfo.get(i).getStorage()) == imagesView.EXTERNAL_STORAGE) {
                            fileController.folder_checker(MainActivity.this, get_main_path(), Appdir + File.separator + mMapInfo.get(i).getfolder(), fileController.MAKE_DIR);
                        } else if (Integer.parseInt(mMapInfo.get(i).getStorage()) == imagesView.INTERNAL_STORAGE) {
                            mediaStorageDir = new File(Environment.getExternalStorageDirectory() + Appdir, mMapInfo.get(i).getfolder());
                            if (!mediaStorageDir.exists()) {
                                if (!mediaStorageDir.mkdirs()) {
                                //    Log.d(TAG, "failed to create directory");
                                }
                            }
                        } else {
                            fileController.folder_checker(MainActivity.this, get_main_path(), Appdir + File.separator + mMapInfo.get(i).getfolder(), fileController.MAKE_DIR);
                            mediaStorageDir = new File(Environment.getExternalStorageDirectory() + Appdir, mMapInfo.get(i).getfolder());
                            if (!mediaStorageDir.exists()) {
                                if (!mediaStorageDir.mkdirs()) {
                               //     Log.d(TAG, "failed to create directory");
                                }
                            }
                        }
                    }catch (Exception e) {

                    }
                }

            setting_cursor.close();
            cursor.close();



        } catch (Exception e) {
            e.printStackTrace();
            setting_cursor.close();
            cursor.close();
            delete_values();
            return -1;
        }

        custom_marker = settings_gridActivity.get_data(mSettingInfo.size() > 0 ? Integer.parseInt(mSettingInfo.get(0).getMarker()) : 0); //onCreate init
        mHandler.sendEmptyMessageDelayed(UPDATE_BTNS, 0); //update new marker
        return 0;

   //     save_db(1); //todo 속도가 느림.........

    }


    public void save_arraylist_db(int choice, int pos, String[] data) {
  //      Log.d(TAG, "save_arraylist_db: choice" + choice + " pos " + pos);
        /*
        for (int i = 0; i < data.length; i++) {
            if (data[i] != null)
                Log.d(TAG, data[i]);
        }
*/
        switch (choice) {
            case SETTING_DB:
        //        Log.d(TAG, "save setting DB");
                mSettingInfo_class = new SettingInfo(
                        (data[0] != null) ? data[0] : mSettingInfo.size() > 0 ? mSettingInfo.get(pos).getid() : Integer.toString(1)  ,
                        (data[1] != null) ? data[1] : mSettingInfo.size() > 0 ? mSettingInfo.get(pos).getPath() : "" ,
                        (data[2] != null) ? data[2] : mSettingInfo.size() > 0 ? mSettingInfo.get(pos).getLatitude() : "" ,
                        (data[3] != null) ? data[3] : mSettingInfo.size() > 0 ? mSettingInfo.get(pos).getLongitude() : "",
                        (data[4] != null) ? data[4] : mSettingInfo.size() > 0 ? mSettingInfo.get(pos).getXcord() : "0",
                        (data[5] != null) ? data[5] : mSettingInfo.size() > 0 ? mSettingInfo.get(pos).getYcord() : "0",
                        (data[6] != null) ? data[6] : mSettingInfo.size() > 0 ? mSettingInfo.get(pos).getMarker() : Integer.toString(marker_choice),
                        (data[7] != null) ? data[7] : mSettingInfo.size() > 0 ? mSettingInfo.get(pos).getLevel() : Integer.toString(default_cam),
                        (data[8] != null) ? data[8] : mSettingInfo.size() > 0 ? mSettingInfo.get(pos).getStorage_status() : Integer.toString(storage_status),
                        (data[9] != null) ? data[9] : mSettingInfo.size() > 0 ? mSettingInfo.get(pos).getMap_status() : Integer.toString(map_status)
                );
                if (mSettingInfo.size() > 0)
                    mSettingInfo.set(pos, mSettingInfo_class);
                else
                    mSettingInfo.add(mSettingInfo_class); //array list에 data 전달
                sqliteDB_setting = settingDBHelper.getWritableDatabase();
                String sqlInsert_setting_db = SettingDBCtrct.SQL_INSERT +
                        " ("
                        + "1" + ", " +   //setting 1
                        "'" + mSettingInfo.get(0).getPath() + "', " +
                        "'" + mSettingInfo.get(0).getLatitude() + "', " +
                        "'" + mSettingInfo.get(0).getLongitude() + "', " +
                        "'" + mSettingInfo.get(0).getXcord() + "', " +
                        "'" + mSettingInfo.get(0).getYcord() + "', " +
                        "'" + mSettingInfo.get(0).getMarker() + "', " +
                        "'" + mSettingInfo.get(0).getLevel() + "', " +
                        "'" + mSettingInfo.get(0).getStorage_status() + "', " +
                        "'" + mSettingInfo.get(0).getMap_status() + "' " +
                        ")";
                sqliteDB_setting.execSQL(sqlInsert_setting_db);
                break;
            case MAIN_DB:
     //           Log.d(TAG, "save main DB");

                mMapInfo_class = new MapInfo(
                        (data[0] != null) ? data[0] : mMapInfo.get(pos).getid(),
                        (data[1] != null) ? data[1] : mMapInfo.get(pos).getfolder(),
                        (data[2] != null) ? data[2] : mMapInfo.get(pos).getlatitude(),
                        (data[3] != null) ? data[3] : mMapInfo.get(pos).getlongitude(),
                        (data[4] != null) ? data[4] : mMapInfo.get(pos).getreserved(),
                        (data[5] != null) ? data[5] : mMapInfo.get(pos).getFav(),
                        (data[6] != null) ? data[6] : mMapInfo.get(pos).getdata(),
                        (data[7] != null) ? data[7] : mMapInfo.get(pos).getlevel(),
                        (data[8] != null) ? data[8] : mMapInfo.get(pos).getStorage()
                        );
                mMapInfo.set(pos, mMapInfo_class);

                sqliteDB_data = dbHelper.getWritableDatabase();
                String sqlInsert_main_db = ContactDBCtrct.SQL_INSERT +
                        " ("
                        + mMapInfo.get(pos).getid() + ", " +
                        "'" + mMapInfo.get(pos).getfolder() + "', " +
                        "'" + mMapInfo.get(pos).getlatitude() + "', " +
                        "'" + mMapInfo.get(pos).getlongitude() + "', " +
                        "'" + mMapInfo.get(pos).getreserved() + "', " +
                        "'" + mMapInfo.get(pos).getFav() + "', " +
                        "'" + mMapInfo.get(pos).getdata() + "', " +
                        "'" + mMapInfo.get(pos).getlevel() + "', " +
                        "'" + mMapInfo.get(pos).getStorage() + "' " +
                        ")";

                sqliteDB_data.execSQL(sqlInsert_main_db);
                break;
            case MEMO_DB:
                break;
        }


        if (choice == 0) {


        } else if (choice == 1) {

        }

    }

    public void save_db(int control) {
        if (control == 3) { //setting DB 저장

        } else {
            sqliteDB_data = dbHelper.getWritableDatabase();
            //get Latitude and Longitude from DB address for marker counting
            for (int i = 0; i < mMapInfo.size(); i++) {
/*
            Log.d(TAG, "DB: "
                    + mMapInfo.get(i).getid()
                    + ", " + mMapInfo.get(i).getfolder()
                    + " (위도:" + mMapInfo.get(i).getlatitude()
                    + " 경도:" + mMapInfo.get(i).getlongitude()
                    + ") " + mMapInfo.get(i).getreserved()
                    + "pics");
*/
                if (control == 1) {
                    if (string_contains_checker(mMapInfo.get(i).getfolder()) == false) {
                        String getAddress = addressParser(mMapInfo.get(i).getfolder(), 0);

                        String[] LatLong = getCurrentLatLong(getAddress).split(",");

                        String sqlInsert = ContactDBCtrct.SQL_INSERT +
                                " ("
                                + mMapInfo.get(i).getid() + ", " +
                                "'" + mMapInfo.get(i).getfolder() + "', " +
                                "'" + LatLong[0] + "', " +
                                "'" + LatLong[1] + "', " +
                                "'" + mMapInfo.get(i).getreserved() + "', " +
                                "'" + mMapInfo.get(i).getFav() + "', " +
                                "'" + mMapInfo.get(i).getdata() + "', " +
                                "'" + mMapInfo.get(i).getlevel() + "', " +
                                "'" + mMapInfo.get(i).getStorage() + "' " +
                                ")";
                        sqliteDB_data.execSQL(sqlInsert);
                    } else {  //주소정보없음으로 DB에 있으면 getCurrentLatLong 타지 않음
                        String sqlInsert = ContactDBCtrct.SQL_INSERT +
                                " ("
                                + mMapInfo.get(i).getid() + ", " +
                                "'" + mMapInfo.get(i).getfolder() + "', " +
                                "'" + mMapInfo.get(i).getlatitude() + "', " +
                                "'" + mMapInfo.get(i).getlongitude() + "', " +
                                "'" + mMapInfo.get(i).getreserved() + "', " +
                                "'" + mMapInfo.get(i).getFav() + "', " +
                                "'" + mMapInfo.get(i).getdata() + "', " +
                                "'" + mMapInfo.get(i).getlevel() + "', " +
                                "'" + mMapInfo.get(i).getStorage() + "' " +
                                ")";
                        sqliteDB_data.execSQL(sqlInsert);
                    }

                } else if (control == 0) {

                    String sqlInsert = ContactDBCtrct.SQL_INSERT +
                            " ("
                            + mMapInfo.get(i).getid() + ", " +
                            "'" + mMapInfo.get(i).getfolder() + "', " +
                            "'" + mMapInfo.get(i).getlatitude() + "', " +
                            "'" + mMapInfo.get(i).getlongitude() + "', " +
                            "'" + mMapInfo.get(i).getreserved() + "', " +
                            "'" + mMapInfo.get(i).getFav() + "', " +
                            "'" + mMapInfo.get(i).getdata() + "', " +
                            "'" + mMapInfo.get(i).getlevel() + "', " +
                            "'" + mMapInfo.get(i).getStorage() + "' " +
                            ")";

                    sqliteDB_data.execSQL(sqlInsert);

                }
                loading_cnt = i;

            }

            mHandler.sendEmptyMessageDelayed(SET_DB_MARKER, 0); //update new marker

            update_status = 1;
            loading_cnt = 0;
        }
    }

    public String getCurrentLatLong(String path) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(path,1);
        } catch (IOException ioException) {
            //네트워크 문제
            mHandler.sendMessage(mHandler.obtainMessage(
                    MainActivity.ERROR_LOG,
                    MainActivity.ERROR_NO_GEOGODER_SERVICE, 0));

            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            mHandler.sendMessage(mHandler.obtainMessage(
                    MainActivity.ERROR_LOG,
                    MainActivity.ERROR_NO_GPS, 0));
  //          Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_SHORT).show();
            return "잘못된 GPS 좌표";

        }
        if (addresses == null || addresses.size() == 0) {
            mHandler.sendMessage(mHandler.obtainMessage(
                    MainActivity.ERROR_LOG,
                    MainActivity.ERROR_NO_ADDRESS, 0));
    //       Toast.makeText(this, "주소 미발견", Toast.LENGTH_SHORT).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            String[] arr = new String[2];
            arr[0] = String.valueOf(form.format(address.getLatitude()));
            arr[1] = String.valueOf(form.format(address.getLongitude()));

            //          Log.d(TAG, "11" + arr[0] + " : " + arr[1]);

            StringBuilder result = new StringBuilder("");

            result.append(arr[0]);
            result.append(",");
            result.append(arr[1]);

            //           String result = String.format("%f,%f", address.getLatitude(), address.getLongitude());
            return result.toString();
        }
    }

    public void nonStatic_caller (Context context, int data) {
//        Log.d(TAG, "nonStatic_caller");
        if (data == 1) {
            /*
            setCoordLocation(Double.parseDouble(cam_data[2]),
                    Double.parseDouble(cam_data[3]),
                    cam_data[1], cam_data[4] + " pictures");
                    */

          //  need_to_update = 1;

        }
    }

    public static void save_values() {
        sqliteDB_data = dbHelper.getWritableDatabase() ;
        Cursor cursor = sqliteDB_data.rawQuery(ContactDBCtrct.SQL_SELECT, null);
        saving_status = 1;
        int value = 0;
        int index = 0;
        int cnt = 0;
        int count = 0;
        int check_data = 0;
        String marker_data;
        String fav_data;
        String folder_level;
        String storage;

        if (string_contains_checker(cam_data[1]) == true) {
            check_data =1;
        }

        String data = null;

  //      Log.d(TAG, "save values" + mMapInfo.size());

        for (int i =0; i < mMapInfo.size(); i++) {
            if (cam_data[1].equals(mMapInfo.get(i).getfolder())) {
        //        Log.d(TAG, "Same path in DB: "  + mMapInfo.get(i).getfolder());
                index = Integer.parseInt(mMapInfo.get(i).getid());
                value = 1;
                cnt = i;
                break;
            } else {
             //        Log.d(TAG, "New path in DB: "  + mMapInfo.get(i).getfolder() + "cam_data[1]" + cam_data[1]);
            }
        }


        //    cam_data[4] = cam_data[0];     //수정, 사진수를 카메라에서 계산해서 넘겨줌
        /*
        if (Integer.parseInt(cam_data[0]) == 0) {
            cam_data[4] = Integer.toString(check_files(cam_data[1]));
        } else {
            cam_data[4] = Integer.toString(check_files(cam_data[1]) + 1);
        }
*/

        MainActivity mainActivity = new MainActivity();
        int base_storage = mainActivity.run_activiy(mActivity, 1, null);

        if (value == 1) { //path가 있을 경우
            data = Integer.toString(index); //DB에는 id로 저장
            cam_data[2] = mMapInfo.get(cnt).getlatitude();  //
            cam_data[3] = mMapInfo.get(cnt).getlongitude();  //
            marker_data = mMapInfo.get(cnt).getdata();
            fav_data = mMapInfo.get(cnt).getFav();
            folder_level = mMapInfo.get(cnt).getlevel();

            if (base_storage > -1) {
                if (base_storage != Integer.parseInt(mMapInfo.get(cnt).getStorage())) {
                    storage = Integer.toString(imagesView.BOTH_STORAGE);
                } else {
                    storage = mMapInfo.get(cnt).getStorage();
                }
            } else {
                storage = mMapInfo.get(cnt).getStorage();
            }

        } else {  //path가 없을 경우
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
        //        Log.d(TAG, "cursor to last in DB" + cursor.getString(0));

                count = Integer.parseInt(cursor.getString(0));
                count++;
            } else {
                count = 1;
            }

         //   db_cnt++;
            data = Integer.toString(count);
            cam_data[2] = cam_data[2].substring(3);
            cam_data[3] = cam_data[3].substring(3);
            marker_data = "0";    //기본값 맨처음 마커가 선택됨
            fav_data = "0";
            folder_level = Integer.toString(level_choice); //기본으로 설정 된 폴더 레벨 세팅
            if (base_storage > -1) {
                storage = Integer.toString(base_storage);
            } else {
                storage = Integer.toString(storage_status);
            }
      //      Log.d(TAG, "New path in DB: ");

        }

        //DB에 저장 공통 부
        String sqlInsert = ContactDBCtrct.SQL_INSERT +
                " ("
                + data + ", " +                //DB id
                "'" + cam_data[1] + "', " +    // 주소
                "'" + cam_data[2] + "', " +    // Latitude
                "'" + cam_data[3] + "', " +    // Logitude
                "'" + cam_data[4] + "', " +     // 사진 수
                "'" + fav_data + "', " +
                "'" + marker_data + "', " +
                "'" + folder_level + "', " +
                "'" + storage + "' " +
                ")";

        sqliteDB_data.execSQL(sqlInsert);

        mMapInfo_class = new MapInfo(
                data,
                cam_data[1],
                cam_data[2],
                cam_data[3],
                cam_data[4],
                fav_data,
                marker_data,
                folder_level,
                storage); //default

        LatLng newLatLng = new LatLng(Double.parseDouble(cam_data[2]), Double.parseDouble(cam_data[3]));
        if (check_data == 1) {
            mItem = new PositionItem(newLatLng, cam_data[1], cam_data[4] + " 데이터", custom_marker.size() - 1, Integer.parseInt(folder_level)); //error 마커는 맨 마지막
        } else {
            mItem = new PositionItem(newLatLng, cam_data[1], cam_data[4] + " 데이터", Integer.parseInt(marker_data), Integer.parseInt(folder_level)); //
        }
        zoom_status = 1;

        if (value == 0) { //path가 없을 경우
            mMapInfo.add(mMapInfo_class); //array list에 data 추가
            need_to_update = 1;

            mPosi.add(mItem);
            addItems(mPosi);

            update_newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(mMapInfo.size() - 1).getlatitude()), Double.parseDouble(mMapInfo.get(mMapInfo.size() - 1).getlongitude())); //마지막 위치

        } else {
            mMapInfo.set(cnt, mMapInfo_class); //값수정
            mPosi.set(cnt, mItem);

            update_newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(cnt).getlatitude()), Double.parseDouble(mMapInfo.get(cnt).getlongitude()));
        }



/*
        for (int i =0; i < mMapInfo.size(); i++) {
            Log.d(TAG, "Array: "
                    + mMapInfo.get(i).getid()
                    + ", " + mMapInfo.get(i).getfolder()
                    + ", " + mMapInfo.get(i).getlatitude()
                    + "" + mMapInfo.get(i).getlongitude()
                    + " (" + mMapInfo.get(i).getreserved()
                    + ") pics");
        }
  */

        saving_status = 0;
   //     Log.d(TAG, "saved: " + saving_status);
        cursor.close();
    }

    public static int set_data(String[] data, int count) {

        StringBuilder dirPath = new StringBuilder("");
        for (int i = 0; i < level_choice; i++) {
            dirPath.append(data[i]);
            if (i < level_choice - 1)
                dirPath.append("/");
        }

        //저장 값
    //    Log.d(TAG, "" + count + ": "+ dirPath.toString() + ", " + data[3] + ", " + data[4] + ", " + data[5]);

        cam_data[1] = dirPath.toString(); // folder address
        cam_data[2] = data[3]; //latitude 현재 찍는 곳
        cam_data[3] = data[4]; //longitude 현재 찍는 곳
        cam_data[4] = Integer.toString(count); //count camera 로 부터 호출 분기

        if (isStringDouble(cam_data[2].substring(3)) && isStringDouble(cam_data[3].substring(3))) {
            save_values();
            return 0;
        } else {
            return -1;
        }
    }
    public static boolean isStringDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean string_contains_checker(String string) {
        boolean value = false;
        List<String> string_list = new ArrayList<>();
        string_list.add("잘못된");
        string_list.add("지오코더");
        string_list.add("주소");
        string_list.add("주소정보없음");

        for (int i =0; i < string_list.size(); i++) {
            if (string.contains(string_list.get(i))) {
                value = true;
                break;
            }
        }

    //    Log.d(TAG, "contains_checker" + value);

        return value;
    }


    public void delete_values() {
    //    Log.d(TAG, "delete_valuese and DB");
        sqliteDB_data = dbHelper.getWritableDatabase();
        sqliteDB_data.execSQL(ContactDBCtrct.SQL_DELETE);
        mMapInfo.clear();
        mPosi.clear();

        sqliteDB_setting = settingDBHelper.getWritableDatabase();
        sqliteDB_setting.execSQL(SettingDBCtrct.SQL_DELETE);
        mSettingInfo.clear();

        custom_marker.clear();

        clear_data = 1;

    }
    private checkIsConnect mCheckIsConnect;

    public class checkIsConnect extends Thread {
        public void run() {
            mHandler.post(new Runnable() {
                public void run() {
                }
            });
            while (true) {
                if (mGoogleApiClient.isConnected()) {
                    mHandler.sendEmptyMessageDelayed(RESUME_FUNC, 0);

                    if (intent_from.contains("WidgetMain")) {
                        if (Integer.parseInt(mSettingInfo.size() > 0 ? mSettingInfo.get(0).getLevel() : Integer.toString(default_cam)) == 2) {
                            update_newLatLng = null;
                            Intent intent_camtest = new Intent(getBaseContext(), CameraActivity.class);
                            intent_camtest.putExtra("from", "WidgetMain");
                            startActivity(intent_camtest);
                        } else {
                            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            try {
                                PackageManager pm = getPackageManager();

                                final ResolveInfo mInfo = pm.resolveActivity(i, 0);

                                Intent intent = new Intent();
                                intent.setComponent(new ComponentName(mInfo.activityInfo.packageName, mInfo.activityInfo.name));
                                intent.setAction(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_LAUNCHER);

                                startActivity(intent);
                            } catch (Exception e){
                            //    Log.d("TAG", "Unable to launch camera: " + e);
                            }
                        }
                    }

                    break;
                } else {
        //            Log.d(TAG, "try connect...");
                }
                try {
                    Thread.sleep(200);   //sleep func이 있는 상태에서 interrupt를 발생시키면 Exception 발생
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onResumeFunc() {

        if (mMapInfo.size() > 0) {
            final FileCntTask task = new FileCntTask(mActivity);
            task.execute(mActivity);
        }

        if (marker_changed == 1) {
       //     Log.d(TAG, "change marker");
            mGoogleMap.clear();
            mClusterManager.clearItems();
            mHandler.sendEmptyMessageDelayed(SET_DB_MARKER, 0); //update new marker
            mHandler.sendMessage(mHandler.obtainMessage(
                    MainActivity.CAMERA_UPDATE,
                    MainActivity.UPDATE_CURLATLON, 0));
    //        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentPosition, 12);
    //        mGoogleMap.moveCamera(cameraUpdate);
            marker_changed = 0;
        }

        if (zoom_status == 1) {
            //         mGoogleMap.clear();
            mHandler.sendMessage(mHandler.obtainMessage(
                    MainActivity.CAMERA_UPDATE,
                    MainActivity.UPDATE_NEWLATLON, 0));
      //      CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(update_newLatLng, 12);
            mClusterManager.clearItems();
            addItems(mPosi);

       //     mGoogleMap.moveCamera(cameraUpdate);
            zoom_status = 0;
        }

        if (clear_data == 1) {
            mClusterManager.clearItems();
            mHandler.sendMessage(mHandler.obtainMessage(
                    MainActivity.CAMERA_UPDATE,
                    MainActivity.UPDATE_CURLATLON, 0));
   //         CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentPosition, 12);
     //       mGoogleMap.moveCamera(cameraUpdate);
        }

  //      Log.d(TAG, "onResume : call startLocationUpdates");
        if (!mRequestingLocationUpdates) startLocationUpdates();
        if (need_to_update == 1) {
     //       Log.d(TAG, "need to update");
            /*
                setCoordLocation(Double.parseDouble(cam_data[2]),
                        Double.parseDouble(cam_data[3]),
                        cam_data[1], cam_data[4] + " pictures");
                        */
            need_to_update = 0;


        }


        mGoogleMap.setMapType(mSettingInfo.size() > 0 ? Integer.parseInt(mSettingInfo.get(0).getMap_status()) : map_status);


    }



    @Override
    public void onResume() {
        super.onResume();

    //    Log.d(TAG, "onResume");

        if (mGoogleApiClient.isConnected()) {
            mHandler.sendEmptyMessageDelayed(RESUME_FUNC, 0);
        } else {
     //       Log.d(TAG, "mGoogleApiClient is not connected");
            mCheckIsConnect = new checkIsConnect();
            mCheckIsConnect.start();
        }

        //앱 정보에서 퍼미션을 허가했는지를 다시 검사해봐야 한다.
        if (askPermissionOnceAgain) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                askPermissionOnceAgain = false;
                checkPermissions();
            }
        }


    }

    static int early_update_pos = 0;
    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {

    //        Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        } else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        //        Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }


     //       Log.d(TAG, "startLocationUpdates : call FusedLocationApi.requestLocationUpdates");
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
                mRequestingLocationUpdates = true;

                mGoogleMap.setMyLocationEnabled(true);

                    //update last position to gmap
                    try {
                        if (currentPosition != null) {
              //              Log.d(TAG, "Early update Position in map");
                            early_update_pos = 1;
                            if (update_newLatLng != null) {
                                mHandler.sendMessage(mHandler.obtainMessage(
                                        MainActivity.CAMERA_UPDATE,
                                        MainActivity.UPDATE_NEWLATLON, 0));
                            } else {
                                mHandler.sendMessage(mHandler.obtainMessage(
                                        MainActivity.CAMERA_UPDATE,
                                        MainActivity.UPDATE_CURLATLON, 0));
                            }
                //            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentPosition, 12);
                //           mGoogleMap.moveCamera(cameraUpdate);
                        }  else {
                            setDefaultLocation();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            } else {
       //         Log.d(TAG, "mGoogleApiClient is not connected");
            }

        }

    }



    private void stopLocationUpdates() {

   //     Log.d(TAG,"stopLocationUpdates : LocationServices.FusedLocationApi.removeLocationUpdates");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mRequestingLocationUpdates = false;
    }

    private void setCluster() {
        mClusterManager = new ClusterManager<PositionItem>(this, mGoogleMap);
        mGoogleMap.setOnCameraIdleListener(mClusterManager);

        addItems(mPosi);         // 클러스터 Marker 추가

        mClusterManager.setRenderer(new CustomIconRenderer(getBaseContext(), mGoogleMap, mClusterManager));

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<PositionItem>() {
            @Override
            public boolean onClusterClick(Cluster<PositionItem> cluster) {
           //     Log.d(TAG, "cluster click");
                return false;
            }
        });

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<PositionItem>() {
            @Override
            public boolean onClusterItemClick(PositionItem positionItem) {
       //         Log.d(TAG, "cluster item click");
                return false;
            }
        });



        // 클러스터 클릭시 펼치기
        /*
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<PositionItem>() {
            @Override
            public boolean onClusterClick(Cluster<PositionItem> cluster) {

                Log.d(TAG, "cluster click");

                LatLngBounds.Builder builder_c = LatLngBounds.builder();
                for (ClusterItem item : cluster.getItems()) {
                    builder_c.include(item.getPosition());
                }
                LatLngBounds bounds_c = builder_c.build();
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds_c, 10));
                float zoom = mGoogleMap.getCameraPosition().zoom - 0.5f;
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));

                return false;
            }
        });
*/
    }

    // 마커 추가
    private static void addItems(List<PositionItem> mPosi) {
        for (PositionItem item : mPosi) {
            mClusterManager.addItem(item);
        }
    }

    // 마커 커스텀 class
    private  class CustomIconRenderer extends DefaultClusterRenderer<PositionItem> {
        private final int mDimension;
        private final ImageView mClusterImageView;
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());

        public CustomIconRenderer(Context context, GoogleMap map, ClusterManager<PositionItem> clusterManager) {
            super(context, map, clusterManager);
            View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image_cluster);
            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);

        }
        @Override
        protected void onBeforeClusterItemRendered(PositionItem item, MarkerOptions markerOptions) {

            if (clear_data == 1) {
                custom_marker = settings_gridActivity.get_data(mSettingInfo.size() > 0 ? Integer.parseInt(mSettingInfo.get(0).getMarker()) : 0); //onCreate init
                clear_data = 0;
            }

            int id = 0;
            /*
            int passBg = 1051206553;

            if (item.bg == passBg) {
                markerOptions.position(item.getPosition());
                markerOptions.snippet(item.getSnippet());
                markerOptions.title(item.getTitle());
                markerOptions.draggable(false);
                markerOptions.icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_action_info));
            } else {
                id = custom_marker.get(item.bg);
                markerOptions.position(item.getPosition());
                markerOptions.snippet(item.getSnippet());
                markerOptions.title(item.getTitle());
                markerOptions.draggable(true);
                markerOptions.icon(BitmapDescriptorFactory
                        .fromResource(id));

            }
            */
/*
            Log.d(TAG,"좌표: 위도(" + String.valueOf(item.getPosition().latitude) + "), 경도("
                    + String.valueOf(item.getPosition().longitude) + ")");
            Log.d(TAG, "주소: " + item.getTitle() + ": " + item.getSnippet());
*/
            id = custom_marker.get(item.bg);
            markerOptions.position(item.getPosition());
            markerOptions.snippet(item.getSnippet());
            markerOptions.title(item.getTitle());
            markerOptions.draggable(true);
            markerOptions.icon(BitmapDescriptorFactory
                    .fromBitmap(resizeMapIcons(id, item.getFolder_level())));

            super.onBeforeClusterItemRendered(item, markerOptions);
        }

        public Bitmap resizeMapIcons(int id, int level){
            int width = 0;
            int height = 0;
            BitmapDrawable bitmapdraw= (BitmapDrawable)getResources().getDrawable(id, null);
            Bitmap imageBitmap = bitmapdraw.getBitmap();

            if (level <= level_choice + 1) { //4단계까지 scale 조정
                if (level == 1) //1단계는 너무 크다
                    level = 2;
                if (level == 3 || level == 4) {
                    width = imageBitmap.getWidth() * level_choice / level;
                    height = imageBitmap.getHeight() * level_choice / level;
                } else {
                    width = imageBitmap.getWidth() * level_choice / level - 30;
                    height = imageBitmap.getHeight() * level_choice / level - 30;
                }
            } else {
                width = imageBitmap.getWidth();
                height = imageBitmap.getHeight();
            }

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);

//            Log.d(TAG, "level_choice " + level_choice+ " level:" + level + " width:" + width + " height:" + height);

            return resizedBitmap;
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<PositionItem> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (PositionItem item : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4)
                    break;
                Drawable drawable = getResources().getDrawable(custom_marker.get(item.bg), null);
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 2;
        }

    }

    public class ZoomThread extends Thread {
        private Context mContext;
        private LatLng mLatLng;
        private int mCount;
        private int mZoomLv;
        private int zoom_lv_goal;

        public ZoomThread(Context context, LatLng latLng, int zoom_lv, int count) {
            mContext = context;
            mLatLng = latLng;
            mCount = count;
            mZoomLv = zoom_lv;
        }

        public void run() {
            mHandler.post(new Runnable() {
                public void run() {
                    zoom_lv_goal = mZoomLv + mCount;
           //         Log.d(TAG, "zoomlv:" + mZoomLv + " count:" + mCount);

                    while (true) {
                        if (mZoomLv < zoom_lv_goal) {
                     //       Log.d(TAG, "mZoomLV:" + mZoomLv);
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mLatLng, mZoomLv);
                            mGoogleMap.animateCamera(cameraUpdate);
                            mZoomLv++;
                        } else {
                  //          Log.d(TAG, "zoom thread stop");
                            if (need_to_update == 1) {
                                mHandler.sendEmptyMessageDelayed(MAKE_FOLDER, 300);
                            }
                            break;
                        }

                        try {
                            Thread.sleep(50);   //sleep func이 있는 상태에서 interrupt를 발생시키면 Exception 발생
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public void data_change(int i, LatLng latLng, String cmd, String data) {
    //    Log.d(TAG, "data_change cmd: " + cmd);

        try {
            sqliteDB_data = dbHelper.getWritableDatabase();

            if (cmd == "Lat") {
           //     Log.d(TAG, "saving position:" + i + " lat:" + latLng.latitude + " lon:" + latLng.longitude);
                LatLng newLatLng = new LatLng(latLng.latitude, latLng.longitude); //새로운 위치수정
                mItem = new PositionItem(newLatLng, mMapInfo.get(i).getfolder(), mMapInfo.get(i).getreserved() + " 데이터", Integer.parseInt(mMapInfo.get(i).getdata()), Integer.parseInt(mMapInfo.get(i).getlevel()));
                mMapInfo_class = new MapInfo(
                        mMapInfo.get(i).getid(),
                        mMapInfo.get(i).getfolder(),
                        String.valueOf(form.format(latLng.latitude)),
                        String.valueOf(form.format(latLng.longitude)),
                        mMapInfo.get(i).getreserved(),
                        mMapInfo.get(i).getFav(),
                        mMapInfo.get(i).getdata(),
                        mMapInfo.get(i).getlevel(),
                        mMapInfo.get(i).getStorage());
                mMapInfo.set(i, mMapInfo_class); //mapinfo 수정

                mPosi.set(i, mItem); //posi 수정

                //DB save
                String sqlInsert = ContactDBCtrct.SQL_INSERT +
                        " ("
                        + mMapInfo.get(i).getid() + ", " +
                        "'" + mMapInfo.get(i).getfolder() + "', " +
                        "'" + String.valueOf(form.format(latLng.latitude)) + "', " +
                        "'" + String.valueOf(form.format(latLng.longitude)) + "', " +
                        "'" + mMapInfo.get(i).getreserved() + "', " +
                        "'" + mMapInfo.get(i).getFav() + "', " +
                        "'" + mMapInfo.get(i).getdata() + "', " +
                        "'" + mMapInfo.get(i).getlevel() + "', " +
                        "'" + mMapInfo.get(i).getStorage() + "' " +
                        ")";
                sqliteDB_data.execSQL(sqlInsert);

                mHandler.sendEmptyMessageDelayed(SET_DB_MARKER, 10);
            } else if (cmd == "folder") {

           //     Log.d(TAG, "saving position:" + i + " old path:" + mMapInfo.get(i).getfolder() + " new path:" + data);
                LatLng newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(i).getlatitude()), Double.parseDouble(mMapInfo.get(i).getlongitude())); //기존 위치
                mItem = new PositionItem(newLatLng, data, mMapInfo.get(i).getreserved() + " 데이터", Integer.parseInt(mMapInfo.get(i).getdata()), Integer.parseInt(mMapInfo.get(i).getlevel()));
                mMapInfo_class = new MapInfo(
                        mMapInfo.get(i).getid(),
                        data,
                        mMapInfo.get(i).getlatitude(),
                        mMapInfo.get(i).getlongitude(),
                        mMapInfo.get(i).getreserved(),
                        mMapInfo.get(i).getFav(),
                        mMapInfo.get(i).getdata(),
                        mMapInfo.get(i).getlevel(),
                        mMapInfo.get(i).getStorage());
                mMapInfo.set(i, mMapInfo_class); //mapinfo 수정

                mPosi.set(i, mItem); //posi 수정

                //DB save
                String sqlInsert = ContactDBCtrct.SQL_INSERT +
                        " ("
                        + mMapInfo.get(i).getid() + ", " +
                        "'" + data + "', " +
                        "'" + mMapInfo.get(i).getlatitude() + "', " +
                        "'" + mMapInfo.get(i).getlongitude() + "', " +
                        "'" + mMapInfo.get(i).getreserved() + "', " +
                        "'" + mMapInfo.get(i).getFav() + "', " +
                        "'" + mMapInfo.get(i).getdata() + "', " +
                        "'" + mMapInfo.get(i).getlevel() + "', " +
                        "'" + mMapInfo.get(i).getStorage() + "' " +
                        ")";
                sqliteDB_data.execSQL(sqlInsert);

            } else if (cmd == "add_folder") {
                int f_length = check_files(mMapInfo.get(i).getfolder());

                if (f_length < 0)
                    return;

                mItem = new PositionItem(latLng, mMapInfo.get(i).getfolder(), Integer.toString(f_length) + " 데이터", Integer.parseInt(mMapInfo.get(i).getdata()), Integer.parseInt(mMapInfo.get(i).getlevel()));

                mMapInfo_class = new MapInfo(
                        mMapInfo.get(i).getid(),
                        mMapInfo.get(i).getfolder(),
                        mMapInfo.get(i).getlatitude(),
                        mMapInfo.get(i).getlongitude(),
                        Integer.toString(f_length),
                        mMapInfo.get(i).getFav(),
                        mMapInfo.get(i).getdata(),
                        mMapInfo.get(i).getlevel(),
                        mMapInfo.get(i).getStorage());
                mMapInfo.set(i, mMapInfo_class); //mapinfo 수정

                mPosi.set(i, mItem); //posi 수정

                //DB save
                String sqlInsert = ContactDBCtrct.SQL_INSERT +
                        " ("
                        + mMapInfo.get(i).getid() + ", " +
                        "'" + mMapInfo.get(i).getfolder() + "', " +
                        "'" + mMapInfo.get(i).getlatitude() + "', " +
                        "'" + mMapInfo.get(i).getlongitude() + "', " +
                        "'" + Integer.toString(f_length) + "', " +
                        "'" + mMapInfo.get(i).getFav() + "', " +
                        "'" + mMapInfo.get(i).getdata() + "', " +
                        "'" + mMapInfo.get(i).getlevel() + "', " +
                        "'" + mMapInfo.get(i).getStorage() + "' " +
                        ")";
                sqliteDB_data.execSQL(sqlInsert); //DB 수정

            } else if (cmd == "file_cnt") {

            } else if (cmd == "memo") {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    //    Log.d(TAG, "data changed");
    }

    public static String getTagString(String tag, ExifInterface exif) {
        return (exif.getAttribute(tag) + "," + exif.getAttribute(ExifInterface.TAG_MAKE) + "<photoMap>");
    }

    public static String showExif(ExifInterface exif) {

        String myAttribute;

        myAttribute = getTagString(ExifInterface.TAG_DATETIME, exif);

        return myAttribute;
    }

    public static String memoID_translation(String path, String data) {
        String id = null;

        try {
            File fileDir = new File(path + File.separator + data);
            String file_path = fileDir.getPath();

            ExifInterface exif = new ExifInterface(file_path);

            id = showExif(exif);

            if (id.contains("null")) {
                id = data + "," + "<photoMap>";
            }

  //          Log.d(TAG, id);

        } catch (Exception e) {
         //   e.printStackTrace();
        }

        return id;
    }

    public static void deleteMemoDB(String path, String id) {
        sqliteDB_memo = memoDBHelper.getWritableDatabase();

        String new_id = memoID_translation(path, id);

        if (new_id.contains("null"))
            return;

        try {
            sqliteDB_memo.delete("memo", "id='" + new_id + "'", null);
      //      Log.d(TAG, "memoDB delete success: " + new_id);
        } catch (Exception e) {
            e.printStackTrace();
       //     Log.d(TAG, "memoDB delete failed: " + new_id);
        }

    }

    public static void writeMemoDB(String id, String path, String info) throws Exception {
        ContentValues values = new ContentValues();
        sqliteDB_memo = memoDBHelper.getWritableDatabase() ;

        String new_id = memoID_translation(path, id);

        if (new_id.contains("null")) {
            return;
        }
        values.put("id", new_id);
        values.put("info", info);
   //     int colNum = db.update("memo", values, null, null);
   //     Log.d(TAG, "update return: " + colNum);
    //    if (colNum == 0) {
   //     Log.d(TAG, "wrtieMemoDB new_id " + new_id);
        try {
            sqliteDB_memo.insertOrThrow("memo", "", values);
     //       Log.d(TAG, "insert memoDB");
        } catch (Exception e) {
            try {
                sqliteDB_memo.replaceOrThrow("memo", "", values);
      //          Log.d(TAG, "replace memoDB");
            } catch (Exception ex) {
        //        Log.d(TAG, "replace memoDB failed");
                ex.printStackTrace();
            }
        }
      //  }
    }

    public static String readMemoDB(String path, String id) throws Exception {
        sqliteDB_memo = memoDBHelper.getWritableDatabase() ;

        String new_id = memoID_translation(path, id);
        Cursor c = null;
        if (new_id.contains("null"))
            return null;

        try {
            c = sqliteDB_memo.query("memo", new String[]{"id", "info"}, "id='" + new_id + "'", null, null, null, null);
            int cnt = c.getCount();
    //        Log.d(TAG, "memoDB cnt: " + cnt);

            if (cnt == 0) {
                return null;
            }

            c.moveToFirst();
            String str = c.getString(1);
            c.close();
/*
            Cursor cursor = db.rawQuery("SELECT * FROM " + "memo", null);
            int total_cnt = cursor.getCount();
            Log.d(TAG, "memoDB total cnt: " + total_cnt);


        if (total_cnt > 0) {
            cursor.moveToFirst();

            for (int i = 0; i < total_cnt; i++) {
                Log.d(TAG, "id: " + cursor.getString(0) + "\ninfo: " + cursor.getString(1));
                cursor.moveToNext();
            }
        }

            cursor.close();
            */
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            c.close();
            return null;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

  //      Log.d(TAG, "onMapReady : start");

        mGoogleMap = googleMap;

        mGoogleMap.clear();

        try {
            MapsInitializer.initialize(getApplicationContext());

            //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
            //지도의 초기위치를 서울로 이동
            setDefaultLocation();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        //      mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.setPadding(0,100,0,0);
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        setCluster();

        mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
     //           Log.d(TAG, "onMarkerDragStart");
            }

            @Override
            public void onMarkerDrag(Marker marker) {
        //        Log.d(TAG, "onMarkerDrag");
            }

            @Override
            public void onMarkerDragEnd(final Marker marker) {
                change_path = 0;

                final LatLng latLng = marker.getPosition();

         //       Log.d(TAG, "onMarkerDragEnd(" + latLng.latitude + ":"+ latLng.longitude + ")");

                final String positionAddress = getPositionAddress(latLng.latitude, latLng.longitude);

                addressParser(positionAddress, 1);

                final StringBuilder path = new StringBuilder();
                for (int i =0; i < makeFolder.length; i++) {
           //         Log.d(TAG, makeFolder[i] + "\n");
                    if (i < level_choice) {
                        path.append(makeFolder[i]);
                        if (i < level_choice - 1)
                            path.append("/");
                    }
                }


                ArrayList<Integer> simular_path = new ArrayList<>();

                final ArrayList<MapInfo> new_mMapinfo = new ArrayList<>();
                MapInfo new_mMapinfo_class;

                for (int i = 0; i < mMapInfo.size(); i++) {
                    if (mMapInfo.get(i).getfolder().contains(path.toString())) {
                        if (!mMapInfo.get(i).getfolder().equals(marker.getTitle())) {
                            simular_path.add(i);
                            new_mMapinfo_class = new MapInfo(
                                    mMapInfo.get(i).getid(),
                                    mMapInfo.get(i).getfolder(),
                                    mMapInfo.get(i).getlatitude(),
                                    mMapInfo.get(i).getlongitude(),
                                    mMapInfo.get(i).getreserved(),
                                    mMapInfo.get(i).getFav(),
                                    mMapInfo.get(i).getdata(),
                                    mMapInfo.get(i).getlevel(),
                                    mMapInfo.get(i).getStorage());
                            new_mMapinfo.add(new_mMapinfo_class);
                        }
                    }
                }


                for (int i = 0; i < mMapInfo.size(); i++) {
                    if (mMapInfo.get(i).getfolder().equals(path.toString())) {
              //          Log.d(TAG, "mMapinfo " +mMapInfo.get(i).getfolder() + " path.toString " + path.toString());
              //          Log.d(TAG, "marker " + marker.getTitle());
                        if (path.toString().equals(marker.getTitle())) {
                            change_path = 1;
                        } else {
                            //todo 비슷한 경로 merge 기능 삭제
                            change_path = 1;


                            final int position = i;
                     //       change_path = 0;
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                            // Setting Dialog Title
                            alertDialog.setTitle("비슷한 경로가 있습니다");

                            StringBuilder stringBuilder = new StringBuilder();

                            if (simular_path.size() > 0) {
                                for (int k = 0; k < simular_path.size(); k++) {
                                    stringBuilder.append("" + mMapInfo.get(simular_path.get(k)).getfolder() + "\n");
                                }
                            }

                            // Setting Dialog Message
                            alertDialog.setMessage("\n " + stringBuilder.toString() + "\n 폴더를 합치겠습니까?");

                            // Setting Icon to Dialog
                            alertDialog.setIcon(custom_marker.get(Integer.parseInt(mMapInfo.get(position).getdata())));

                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                    final ListCustomDialog listCustomDialog = new ListCustomDialog(mActivity, "폴더 합치기","현재 폴더: " + marker.getTitle() +
                                            "\n\n폴더 목록",
                                            "",
                                            new_mMapinfo, mSettingInfo.size() > 0 ? mSettingInfo.get(0).getMarker() : Integer.toString(0));

                                    listCustomDialog.setDialogListener(new ListDialogListener() {  // MyDialogListener 를 구현
                                        @Override
                                        public int onPositiveClicked(Integer status, String folder) {
                         //                   Log.d(TAG, "ok " + status + "path " + folder); //여기서 커스텀 다이얼로그가 누른 값 return

                                            if (status < 0) {
                                                Toast.makeText(getApplicationContext(), "폴더를 선택해 주세요.\n", Toast.LENGTH_LONG).show();
                                            } else {
                                                for (int i = 0; i < mMapInfo.size(); i++ ) {
                                                    if (new_mMapinfo.get(status).getfolder().equals(mMapInfo.get(i).getfolder())) {
                                   //                     Log.d(TAG, "여기를 " + marker.getTitle() + "을 이곳에 " + mMapInfo.get(i).getfolder());

                                                        moveFolder(marker.getTitle(), mMapInfo.get(i).getfolder(), true);
                                                        for (int p = 0; p < mMapInfo.size(); p++) {
                                                            if (marker.getTitle().equals(mMapInfo.get(p).getfolder())) {
                                                      //          Log.d(TAG, "id: " + p + " 삭제: " + mMapInfo.get(p).getfolder());

                                                                LatLng newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(i).getlatitude()), Double.parseDouble(mMapInfo.get(i).getlongitude())); //원래 위치로

                                                                data_change(i, newLatLng, "add_folder", null);

                                                                deleteDir_looper(mMapInfo.get(p).getfolder()); //파일 디렉토리 삭제

                                                                data_remove(p); //기존 data 삭제, 구조체 DB


                                                                mHandler.sendEmptyMessageDelayed(SET_DB_MARKER, 0);

                                                                if (mZoomThread != null) {
                                                                    mZoomThread.interrupt();
                                                                    mZoomThread = null;

                                                                }
                                                                float zoom_lv = mGoogleMap.getCameraPosition().zoom;
                                                                mZoomThread = new ZoomThread(mActivity, newLatLng, (int)zoom_lv, 2);
                                                                mZoomThread.start();

                                                            }
                                                        }


                                                    }
                                                }

                                                listCustomDialog.dismiss();

                                            }

                                            return status;
                                        }

                                        @Override
                                        public void onNegativeClicked(Integer status) {
                                 //           Log.d(TAG, "cancel");
                 //                           Toast.makeText(getApplicationContext(), "취소 되었습니다", Toast.LENGTH_SHORT).show();

                                            for (int j = 0; j < mMapInfo.size(); j++) {
                                                if (mMapInfo.get(j).getfolder().equals(marker.getTitle())) {
                                                    LatLng newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(j).getlatitude()), Double.parseDouble(mMapInfo.get(j).getlongitude())); //원래 위치로

                                                    mHandler.sendEmptyMessageDelayed(SET_DB_MARKER, 0);

                                                    if (mZoomThread != null) {
                                                        mZoomThread.interrupt();
                                                        mZoomThread = null;

                                                    }
                                                    float zoom_lv = mGoogleMap.getCameraPosition().zoom;
                                                    mZoomThread = new ZoomThread(mActivity, newLatLng, (int)zoom_lv, 2);
                                                    mZoomThread.start();
                                                }
                                            }



                                        }
                                    });
                                    listCustomDialog.show();

                                }
                            });

                            alertDialog.setNeutralButton("이동", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String title = marker.getTitle();
                                    if (title != null) {

                           //             Log.d(TAG, "title:" + marker.getTitle() + " path: " + path.toString());

                                        for (int i = 0; i < mMapInfo.size(); i++) {
                                            if (mMapInfo.get(i).getfolder().equals(title)) {
                                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                                                final int position = i;
                                                // Setting Dialog Title
                                                alertDialog.setTitle("폴더 위치 변경");

                                                // Setting Dialog Message
                                                alertDialog.setMessage("\n 폴더 위치를 수정 하시겠습니까?" +
                                                        "\n\n 위도:" + String.valueOf(form.format(latLng.latitude)) + " 경도:" + String.valueOf(form.format(latLng.longitude)));

                                                // Setting Icon to Dialog
                                                alertDialog.setIcon(custom_marker.get(Integer.parseInt(mMapInfo.get(i).getdata())));

                                                // Setting Positive "Yes" Button
                                                alertDialog.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        data_change(position, latLng, "Lat", null);

                                                        if (inter_line == 1) {
                                                            if (animator.isRunning())
                                                                animator.cancel();
                                                            arrayPoints_gray.clear();

                                                            for (Polyline line : polyline) {
                                                                line.remove();
                                                            }
                                                            polyline.clear();

                                                        }

                                                    }
                                                });

                                                // Setting Negative "NO" Button
                                                alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        LatLng newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(position).getlatitude()), Double.parseDouble(mMapInfo.get(position).getlongitude())); //원래 위치로
                                                        //        mItem = new PositionItem(newLatLng, mMapInfo.get(position).getfolder(), mMapInfo.get(position).getreserved() + " pictures", Integer.parseInt(mMapInfo.get(position).getdata()));
                                                        //        mPosi.set(position, mItem); //posi 수정

                                                        mHandler.sendEmptyMessageDelayed(SET_DB_MARKER, 0);

                     //                                   Toast.makeText(getApplicationContext(), "취소 되었습니다.", Toast.LENGTH_SHORT).show();

                                                        if (mZoomThread != null) {
                                                            mZoomThread.interrupt();
                                                            mZoomThread = null;

                                                        }
                                                        float zoom_lv = mGoogleMap.getCameraPosition().zoom;
                                                        mZoomThread = new ZoomThread(mActivity, newLatLng, (int)zoom_lv, 2);
                                                        mZoomThread.start();
                                                    }
                                                });
                                                marker.hideInfoWindow();
                                                // Showing Alert Message
                                                alertDialog.show();
                                            }
                                        }
                                    } else {
                              //          Log.d(TAG, "title is null");
                                    }

                                }
                            });

                            // Setting Positive "Yes" Button
                            alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                      //              Toast.makeText(getApplicationContext(), "같은 경로가 있습니다.\n" + path.toString(), Toast.LENGTH_LONG).show();

                   //                 change_path = 0;

                                    for (int j = 0; j < mMapInfo.size(); j++) {
                                        if (mMapInfo.get(j).getfolder().equals(marker.getTitle())) {
                                            LatLng newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(j).getlatitude()), Double.parseDouble(mMapInfo.get(j).getlongitude())); //원래 위치로

                                            mHandler.sendEmptyMessageDelayed(SET_DB_MARKER, 0);

                                            if (mZoomThread != null) {
                                                mZoomThread.interrupt();
                                                mZoomThread = null;

                                            }
                                            float zoom_lv = mGoogleMap.getCameraPosition().zoom;
                                            mZoomThread = new ZoomThread(mActivity, newLatLng, (int)zoom_lv, 2);
                                            mZoomThread.start();
                                        }
                                    }

                                }
                            });
                    //todo 비슷한 경로 merge 기능 삭제
                    //        alertDialog.show();
                        }
                        break;
                    } else {
                        change_path = 1;
                    }
                }

                String title = marker.getTitle();

                if (change_path == 1) {
                    if (title != null) {

        //                Log.d(TAG, "title:" + marker.getTitle() + " path: " + path.toString());

                        for (int i = 0; i < mMapInfo.size(); i++) {
                            if (mMapInfo.get(i).getfolder().equals(title)) {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                                final int position = i;
                                // Setting Dialog Title
                                alertDialog.setTitle("폴더 위치 변경");

                                // Setting Dialog Message
                                alertDialog.setMessage("\n 폴더 위치를 수정 하시겠습니까?" +
                                                       "\n\n 위도:" + String.valueOf(form.format(latLng.latitude)) + " 경도:" + String.valueOf(form.format(latLng.longitude)));

                                // Setting Icon to Dialog
                                alertDialog.setIcon(custom_marker.get(Integer.parseInt(mMapInfo.get(i).getdata())));

                                // Setting Positive "Yes" Button
                                alertDialog.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        data_change(position, latLng, "Lat", null);

                                        if (inter_line == 1) {
                                            if (animator.isRunning())
                                                animator.cancel();
                                            arrayPoints_gray.clear();

                                            for (Polyline line : polyline) {
                                                line.remove();
                                            }

                                            polyline.clear();

                                        }
                                    }
                                });

                                // Setting Negative "NO" Button
                                alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        LatLng newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(position).getlatitude()), Double.parseDouble(mMapInfo.get(position).getlongitude())); //원래 위치로
                                //        mItem = new PositionItem(newLatLng, mMapInfo.get(position).getfolder(), mMapInfo.get(position).getreserved() + " pictures", Integer.parseInt(mMapInfo.get(position).getdata()));
                                //        mPosi.set(position, mItem); //posi 수정

                                        mHandler.sendEmptyMessageDelayed(SET_DB_MARKER, 0);

                   //                     Toast.makeText(getApplicationContext(), "취소 되었습니다.", Toast.LENGTH_SHORT).show();

                                        if (mZoomThread != null) {
                                            mZoomThread.interrupt();
                                            mZoomThread = null;

                                        }
                                        float zoom_lv = mGoogleMap.getCameraPosition().zoom;
                                        mZoomThread = new ZoomThread(mActivity, newLatLng, (int)zoom_lv, 2);
                                        mZoomThread.start();
                                    }
                                });
                                marker.hideInfoWindow();
                                // Showing Alert Message
                                alertDialog.show();
                            }
                        }
                    } else {
              //          Log.d(TAG, "title is null");
                    }
                }

            }
        });

        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){

            @Override
            public boolean onMyLocationButtonClick() {
                if (gps_status == 0) {
                    showDialogForLocationServiceSetting();
                } else {
         //           Log.d(TAG, "onMyLocationButtonClick : 위치에 따른 카메라 이동 활성화");
                    mMoveMapByAPI = true;
                }
                return true;
            }
        });

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {

                try {

                    if (customMarker != null)
                        customMarker.remove();

                    if (inter_line == 1) {
                        if (animator.isRunning())
                            animator.cancel();

                        arrayPoints_gray.clear();

                        for (Polyline line : polyline) {
                            line.remove();
                        }
                        polyline.clear();


                    }
                    if (info_btn == 1) {

           //             Log.d(TAG, "OnMapClick");

                        // 현재 위도와 경도에서 화면 포인트를 알려준다
                        Point screenPt = mGoogleMap.getProjection().toScreenLocation(point);

                        // 현재 화면에 찍힌 포인트로 부터 위도와 경도를 알려준다.
                        //      LatLng latLng = mGoogleMap.getProjection().fromScreenLocation(screenPt);

                        String positionAddress = getPositionAddress(point.latitude, point.longitude);

                        String positionSnippet = " 위도:" + String.valueOf(form.format(point.latitude))
                                + " 경도:" + String.valueOf(form.format(point.longitude));

                        String positionCoord = " X:" + String.valueOf(screenPt.x)
                                + " Y:" + String.valueOf(screenPt.y);

                        StringBuilder strPosition = new StringBuilder("");
                        strPosition.append(positionAddress);
                        strPosition.append(positionSnippet);
                        //            strPosition.append(positionCoord);
                        //        mcoordInfo.setText("선택위치 주소: " + strPosition);

                        addressParser(strPosition.toString(), 0);
/*
                    if (imageOverlay != null)
                        imageOverlay.remove();
*/
                        LatLng NEWARK = new LatLng(point.latitude, point.longitude);
/*
                    GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                            .image(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_info))
                            .position(NEWARK, 500f, 500f);

                    imageOverlay = mGoogleMap.addGroundOverlay(newarkMap);
*/
                        mTouchMarker = false;
/*
                    int tracking_check = 0;
                    int pos = 0;
                    int passBg = 1051206553;

                    for (int i =0; i < mPosi.size(); i++) {
                        if (mPosi.get(i).getBg() == passBg) {
                            Log.d(TAG, "ic_action_info detect");
                            tracking_check = 1;
                            pos = i;
                            break;
                        }
                    }

                    mItem = new PositionItem(NEWARK, positionAddress, positionSnippet, passBg);

                    if (tracking_check == 1) {
                        mPosi.set(pos, mItem);
                    } else {
                        mPosi.add(mItem);
                        addItems(mPosi);
                    }
*/
                        if (coordMarker != null)
                            coordMarker.remove();

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(NEWARK);
                        markerOptions.title(positionAddress);
                        markerOptions.snippet(positionSnippet);
                        markerOptions.draggable(false);
                        markerOptions.icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        coordMarker = mGoogleMap.addMarker(markerOptions);
                        coordMarker.showInfoWindow();


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mGoogleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                if (mMoveMapByUser == true && mRequestingLocationUpdates){
         //           Log.d(TAG, "onCameraMove : 위치에 따른 카메라 이동 비활성화");
                    mMoveMapByAPI = false;
                }
                mMoveMapByUser = true;
            }
        });

        mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
         //       Log.d(TAG, "onMapLongClick");

                if (customMarker != null)
                    customMarker.remove();

                if (force_path != null)
                    force_path = null;
                update_newLatLng = latLng;
                mHandler.sendEmptyMessageDelayed(MAKE_FOLDER, 0);
            }
        });

        mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
            }
        });

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {

                    if (marker.getSnippet().contains("위도:") &&
                            marker.getSnippet().contains("경도:"))
                     return;

                String title;
                int id = 0;
       //         Log.d(TAG, "OnInfoWindowClick: markerID (" + marker.getId() + ") : (" + marker.getTitle() + ")");
                marker.hideInfoWindow();
                update_newLatLng = marker.getPosition();
                title = marker.getTitle();
                if (title != null) {
                    for (int i = 0; i < mMapInfo.size(); i++) {
                        if (mMapInfo.get(i).getfolder().equals(title)) {
                            id = i;
                        }
                    }
                }

                Intent intent_toad_project = new Intent(getBaseContext(), ImagesView.class);
                intent_toad_project.putExtra("mMapInfo", mMapInfo);
                intent_toad_project.putExtra("mSetting", mSettingInfo);
                intent_toad_project.putExtra("f_path", marker.getTitle());
                intent_toad_project.putExtra("id", id);
                intent_toad_project.putExtra("enable", 1);
                intent_toad_project.putExtra("from", "MainActivity");
                intent_toad_project.putExtra("sdcard_path", sdcard_path_header);
                startActivity(intent_toad_project);

            }
        });

        mGoogleMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                try {
                    if (info_btn == 1) {
                        if (marker.getSnippet().contains("위도") ||
                                marker.getSnippet().contains("경도"))
                            return;
                    }
                    vibrator.vibrate(100);

                    delete_path = marker.getTitle();
                    if (delete_path != null) {
                        for (int i = 0; i < mMapInfo.size(); i++) {
                            if (mMapInfo.get(i).getfolder().equals(delete_path)) {
                                delete_id = i;
                            }
                        }
                    }

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    //             Log.d(TAG, "onMapLongClick");
                    // Setting Dialog Title
                    alertDialog.setTitle(mMapInfo.get(delete_id).getfolder());

                    // Setting Dialog Message
                    alertDialog.setMessage("\n 폴더를 수정 하시겠습니까?");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(custom_marker.get(Integer.parseInt(mMapInfo.get(delete_id).getdata())));

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //         Log.d(TAG, "수정 id: " + delete_id);
                            mHandler.sendMessage(mHandler.obtainMessage(
                                    MainActivity.SHOW_TOAST,
                                    MainActivity.SHOW_REFACTOR, delete_id));
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            //                Toast.makeText(getApplicationContext(), "취소 되었습니다", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                    alertDialog.setNeutralButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            mHandler.sendMessage(mHandler.obtainMessage(
                                    MainActivity.SHOW_TOAST,
                                    MainActivity.SHOW_DELETE, delete_id));

                            // Write your code here to invoke YES event
                            //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                        }
                    });
                    marker.hideInfoWindow();
                    // Showing Alert Message
                    alertDialog.show();
                } catch (Exception e) {

                }
            }
        });


        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
       //         int id = 0;
                String title = null;
                String pics = null;
                LatLng latLng;
                StringBuilder backward_fpath = new StringBuilder();
                int detect = 0;
                title = marker.getTitle();

                if (inter_line == 1) {
                    if (arrayPoints_gray.size() != 0) {

                        if (animator.isRunning())
                            animator.cancel();

                        arrayPoints_gray.clear();


                        for (Polyline line : polyline) {
                            line.remove();
                        }
                        polyline.clear();

                    }

                    //polyline 세팅
                    polylineOptions_gray = new PolylineOptions();
                    polylineOptions_gray.color(getResources().getColor(R.color.transparent_gray));
                    polylineOptions_gray.startCap(new SquareCap());
                    polylineOptions_gray.endCap(new SquareCap());
              //To Do cannot find the ROUND value
                    polylineOptions_gray.jointType(JointType.ROUND);
                    polylineOptions_gray.width(10);


                    polylineOptions_blue = new PolylineOptions();
                    polylineOptions_blue.color(getResources().getColor(R.color.transparent_magenta));
                    polylineOptions_blue.startCap(new SquareCap());
                    polylineOptions_blue.endCap(new SquareCap());
                    //To Do cannot find the ROUND value
                    polylineOptions_blue.jointType(JointType.ROUND);

                    polylineOptions_blue.width(10);

                    arrayPoints_gray.add(marker.getPosition());
        //            Log.d(TAG , "start layer" + marker.getPosition().latitude + "," + marker.getPosition().longitude);


                }
                if (title != null) {
                    String[] back_folder = title.split("/");

                    for (int i = 0; i < back_folder.length - 1; i++) {
                        if (i != 0)
                            backward_fpath.append("/");
                        backward_fpath.append(back_folder[i]);
                    }
            //        Log.d(TAG, "upper layer " + backward_fpath.toString());

                    if (inter_line == 1) {
                        for (int i = 0; i < mMapInfo.size(); i++) {
                            if (mMapInfo.get(i).getfolder().equals(backward_fpath.toString())) {
                                upper_latLng = new LatLng(Double.parseDouble(mMapInfo.get(i).getlatitude()), Double.parseDouble(mMapInfo.get(i).getlongitude()));
                                arrayPoints_gray.add(upper_latLng);
                         //       Log.d(TAG , "upper_latLng " + upper_latLng.latitude + "," + upper_latLng.longitude);
                                detect = 1;
                            }
                        }
                    }
                    for (int i = 0; i < mMapInfo.size(); i++) {
                        if (mMapInfo.get(i).getfolder().equals(title)) {
                            pics = mMapInfo.get(i).getreserved();
                            StringBuilder mPics = new StringBuilder("");
                            mPics.append(pics);
                            mPics.append(" 데이터");
                            marker.setSnippet(mPics.toString());
                    //        Log.d(TAG, "OnMarkerClick: (id " + i + ", pics " + mPics + ")");
                        }

                        if (inter_line == 1) {
                            if (detect == 1) {
                                if (mMapInfo.get(i).getfolder().contains(backward_fpath.toString())) {

                                    latLng = new LatLng(Double.parseDouble(mMapInfo.get(i).getlatitude()), Double.parseDouble(mMapInfo.get(i).getlongitude()));
                              //      Log.d(TAG, "inter folder " + mMapInfo.get(i).getfolder());
                                    String[] inter_folder_path = mMapInfo.get(i).getfolder().split("/");
                                    String[] upper_folder_path = backward_fpath.toString().split("/");

                                    if (inter_folder_path.length > upper_folder_path.length + 1) {
                                  //      Log.d(TAG, "2layer over");
                                    } else {
                                        if (!mMapInfo.get(i).getfolder().equals(backward_fpath.toString())
                                                && !mMapInfo.get(i).getfolder().equals(marker.getTitle())) {
                                            arrayPoints_gray.add(upper_latLng);
                                         //   Log.d(TAG, i + ". upper_latLng " + upper_latLng.latitude + "," + upper_latLng.longitude);
                                            arrayPoints_gray.add(latLng);
                                     //       Log.d(TAG, i + ". latLng " + latLng.latitude + "," + latLng.longitude);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (detect == 1 && inter_line == 1) {
                        AnimatorTask animatorTask = new AnimatorTask(mActivity);
                        animatorTask.execute();
                    }
                }
                mTouchMarker = true;

                return false;
            }
        });

        try {
         //   boolean success = mGoogleMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));

                mGoogleMap.setMapType(mSettingInfo.size() > 0 ? Integer.parseInt(mSettingInfo.get(0).getMap_status()) : map_status);


            /*
            if (!success) {
                Log.d(TAG, "Style parsing failed.");
            }
            */
        } catch (Exception e) {
            e.printStackTrace();
        }

    //    Log.d(TAG, "onMapReady : done");
    }


    boolean deleteDir_looper(String path) {

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + Appdir, path);
        File[] childFileList = mediaStorageDir.listFiles();

        if (childFileList == null) {
     //       Log.d(TAG, "list is null");
            return true;
        }

        for(File childFile : childFileList)
        {
            if(childFile.isDirectory()) {
                deleteDir_looper(childFile.getAbsolutePath());     //하위 디렉토리 루프
            }
            else {
                childFile.delete();    //하위 파일삭제
            }
        }
        mediaStorageDir.delete();    //root 삭제

        return true;

    }

    public boolean DeleteDir(int id, String path, int storage_status)
    {
        try {
   //         Log.d(TAG, "deleteDir: " + path + " storage_status: " + storage_status);
            //내장

             int external_folder_check = 0;
             int internal_folder_check = 0;


                File directoryPath_internal = new File(Environment.getExternalStorageDirectory() + Appdir, path);

                File directoryPath_external = new File(sdcard_path_header + Appdir, path);

                String[] fileList_internal = directoryPath_internal.list();
                String[] fileList_external = directoryPath_external.list();


                if (fileList_internal != null) {
                    for (int i = 0; i < fileList_internal.length; i++) {
                        if (fileController.file_format_check(fileList_internal[i]) == 0) {
                            internal_folder_check = 1;
                            break;
                        }
                    }
                }

                if (fileList_external != null) {
                    for (int i = 0; i < fileList_external.length; i++) {
                        if (fileController.file_format_check(fileList_external[i]) == 0) {
                            external_folder_check = 1;
                            break;
                        }
                    }

                }

                if (storage_status == ImagesView.BOTH_STORAGE) {
                    if (internal_folder_check == 0) {
                        deleteDir_looper(path);

                    } else {

                    }
                    if (external_folder_check == 0) {
                        if (fileController.folder_checker(mActivity, get_main_path(), Appdir + File.separator + path, fileController.DELETE_DIR) != null) {
                        }
                    } else {
                        for (int i = 0; i < fileList_external.length; i++) {
                            if (fileController.file_format_check(fileList_internal[i]) != 0) {
                                if (fileController.file_controller(mActivity, get_main_path(), Appdir + File.separator + path, null, fileList_external[i], fileController.DELETE_FILE) == 0) {

                                }
                            }
                        }

                    }
                  //      delete_folders_external(mActivity, Appdir + File.separator + path);
                    data_remove(id);
                } else if (storage_status == ImagesView.INTERNAL_STORAGE) {

                    if (deleteDir_looper(path) == true) {
                        if (Integer.parseInt(mMapInfo.get(id).getStorage()) == ImagesView.BOTH_STORAGE) {
                            int pics = check_files(path);

                            mMapInfo_class = new MapInfo(
                                    mMapInfo.get(id).getid(),
                                    mMapInfo.get(id).getfolder(),
                                    mMapInfo.get(id).getlatitude(),
                                    mMapInfo.get(id).getlongitude(),
                                    Integer.toString(pics),
                                    mMapInfo.get(id).getFav(),
                                    mMapInfo.get(id).getdata(),
                                    mMapInfo.get(id).getlevel(),
                                    "1");
                            mMapInfo.set(id, mMapInfo_class);
                            save_db(0);
                        } else {
                            data_remove(id);
                        }
                    }

                } else {

                    if (external_folder_check == 0) {
                        if (fileController.folder_checker(mActivity, get_main_path(), Appdir + File.separator + path, fileController.DELETE_DIR) != null) {
                        }
                    } else {
                        for (int i = 0; i < fileList_external.length; i++) {
                            if (fileController.file_format_check(fileList_external[i]) != 0) {
                                if (fileController.file_controller(mActivity, get_main_path(), Appdir + File.separator + path, null, fileList_external[i], fileController.DELETE_FILE) == 0) {

                                }
                            }
                        }

                    }



                        if (Integer.parseInt(mMapInfo.get(id).getStorage()) == ImagesView.BOTH_STORAGE) {

                            int pics = check_files(path);

                            mMapInfo_class = new MapInfo(
                                    mMapInfo.get(id).getid(),
                                    mMapInfo.get(id).getfolder(),
                                    mMapInfo.get(id).getlatitude(),
                                    mMapInfo.get(id).getlongitude(),
                                    Integer.toString(pics),
                                    mMapInfo.get(id).getFav(),
                                    mMapInfo.get(id).getdata(),
                                    mMapInfo.get(id).getlevel(),
                                    "0");
                            mMapInfo.set(id, mMapInfo_class);
                            save_db(0);
                        } else {
                            data_remove(id);
                        }

                }

            mClusterManager.clearItems();
            addItems(mPosi);

            mClusterManager.onCameraIdle();
            return  true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
    public int pics_delete(int id) {
        String path;
        int pics;
        path = mMapInfo.get(id).getfolder();
        pics = check_files(path);

     //   Log.d(TAG, "path:" + path + " pics:" + pics);

        if (pics< -1)
            return -1;
        if (pics > 0) {
            pics_sync(id, pics, 0);
        } else {
            if (delete_folders(path) == true) {
                zoom_status = 1;
                pics_sync(id, pics, 1);
            }
        }
        return pics;
    }
*/
    public void marker_change(int i, String data) {
  //      Log.d(TAG, "marker_change");
        sqliteDB_data = dbHelper.getWritableDatabase() ;
        LatLng newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(i).getlatitude()), Double.parseDouble(mMapInfo.get(i).getlongitude()));

            mItem = new PositionItem(newLatLng, mMapInfo.get(i).getfolder(), mMapInfo.get(i).getreserved() + " 데이터", Integer.parseInt(data), Integer.parseInt(mMapInfo.get(i).getlevel()));

            mMapInfo_class = new MapInfo(
                    mMapInfo.get(i).getid(),
                    mMapInfo.get(i).getfolder(),
                    mMapInfo.get(i).getlatitude(),
                    mMapInfo.get(i).getlongitude(),
                    mMapInfo.get(i).getreserved(),
                    mMapInfo.get(i).getFav(),
                    data,
                    mMapInfo.get(i).getlevel(),
                    mMapInfo.get(i).getStorage());
            mMapInfo.set(i, mMapInfo_class);

            mPosi.set(i, mItem);

            //DB save
            String sqlInsert = ContactDBCtrct.SQL_INSERT +
                    " ("
                    + mMapInfo.get(i).getid() + ", " +
                    "'" + mMapInfo.get(i).getfolder() + "', " +
                    "'" + mMapInfo.get(i).getlatitude() + "', " +
                    "'" + mMapInfo.get(i).getlongitude() + "', " +
                    "'" + mMapInfo.get(i).getreserved() + "', " +
                    "'" + mMapInfo.get(i).getFav() + "', " +
                    "'" + data + "', " +
                    "'" + mMapInfo.get(i).getlevel() + "', " +
                    "'" + mMapInfo.get(i).getStorage() + "' " +
                    ")";

        sqliteDB_data.execSQL(sqlInsert);
    }

    public void pics_sync(int i, int pics, int replace) {
   //     Log.d(TAG, "pics_sync");
        try {
            sqliteDB_data = dbHelper.getWritableDatabase();
            LatLng newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(i).getlatitude()), Double.parseDouble(mMapInfo.get(i).getlongitude()));
            if (string_contains_checker(mMapInfo.get(i).getfolder()) == true) {
                mItem = new PositionItem(newLatLng, mMapInfo.get(i).getfolder(), Integer.toString(pics) + " 데이터", custom_marker.size() - 1, Integer.parseInt(mMapInfo.get(i).getlevel()));
            } else {
                mItem = new PositionItem(newLatLng, mMapInfo.get(i).getfolder(), Integer.toString(pics) + " 데이터", Integer.parseInt(mMapInfo.get(i).getdata()), Integer.parseInt(mMapInfo.get(i).getlevel()));
            }
            if (replace == 0) {
                mMapInfo_class = new MapInfo(
                        mMapInfo.get(i).getid(),
                        mMapInfo.get(i).getfolder(),
                        mMapInfo.get(i).getlatitude(),
                        mMapInfo.get(i).getlongitude(),
                        Integer.toString(pics),
                        mMapInfo.get(i).getFav(),
                        mMapInfo.get(i).getdata(),
                        mMapInfo.get(i).getlevel(),
                        mMapInfo.get(i).getStorage());
                mMapInfo.set(i, mMapInfo_class);

                if (mPosi.size() != 0)
                    mPosi.set(i, mItem);

                //DB save
                String sqlInsert = ContactDBCtrct.SQL_INSERT +
                        " ("
                        + mMapInfo.get(i).getid() + ", " +
                        "'" + mMapInfo.get(i).getfolder() + "', " +
                        "'" + mMapInfo.get(i).getlatitude() + "', " +
                        "'" + mMapInfo.get(i).getlongitude() + "', " +
                        "'" + Integer.toString(pics) + "', " +
                        "'" + mMapInfo.get(i).getFav() + "', " +
                        "'" + mMapInfo.get(i).getdata() + "', " +
                        "'" + mMapInfo.get(i).getlevel() + "', " +
                        "'" + mMapInfo.get(i).getStorage() + "' " +
                        ")";

                sqliteDB_data.execSQL(sqlInsert);

            } else if (replace == 1) {
          //      Log.d(TAG, "DB delete id:" + mMapInfo.get(i).getid() + " path:" + mMapInfo.get(i).getfolder());
                update_newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(i).getlatitude()), Double.parseDouble(mMapInfo.get(i).getlongitude()));
                data_remove(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void data_remove(int id) {
    //    Log.d(TAG, "data_remove");
        try {
            sqliteDB_data = dbHelper.getWritableDatabase();
            sqliteDB_data.delete(ContactDBCtrct.TBL_CONTACT, "_id" + "=" + mMapInfo.get(id).getid(), null);

            mMapInfo.remove(id);
            mPosi.remove(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update_path(ArrayList<String> path, ArrayList<String> path_1, String[] path_new) {
        Cursor cursor = null;
        try {

         //   Log.d(TAG, "update_path");
            sqliteDB_data = dbHelper.getWritableDatabase();
            cursor = sqliteDB_data.rawQuery(ContactDBCtrct.SQL_SELECT, null);
            fail_folders_reason.clear();
            no_db_folder.clear();


            int db_start;
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                db_start = Integer.parseInt(cursor.getString(0));
                db_start += 1;
            } else {
                db_start = 1;
            }

            for (int i = 0; i < path.size(); i++) {
                if (path_new[0].equals("0")) {
                    String getAddress = addressParser(path.get(i), 0);
                    String getLatLong = getCurrentLatLong(getAddress);

                    if (getLatLong.contains(",")) {
                        String[] LatLong = getLatLong.split(",");

                        if (check_files(path.get(i)) >= 0) {

                            String[] path_length_check = path.get(i).split("/");
                            //          Log.d(TAG, "folder_level: " + path_length_check.length);
                            int detect = -1;
                            for (int k = 0; k < mMapInfo.size(); k++) {
                                if (path.get(i).equals(mMapInfo.get(k).getfolder())) {
                                    detect = k;
                                }
                            }

                            if (detect > -1) {
                                mMapInfo_class = new MapInfo(
                                        mMapInfo.get(detect).getid(),
                                        mMapInfo.get(detect).getfolder(),
                                        mMapInfo.get(detect).getlatitude(),
                                        mMapInfo.get(detect).getlongitude(),
                                        mMapInfo.get(detect).getreserved(),
                                        mMapInfo.get(detect).getFav(),
                                        mMapInfo.get(detect).getdata(),
                                        mMapInfo.get(detect).getlevel(),
                                        "2");
                                mMapInfo.set(detect, mMapInfo_class);
                            } else {

                                mMapInfo_class = new MapInfo(
                                        Integer.toString(db_start),
                                        path.get(i),
                                        LatLong[0],
                                        LatLong[1],
                                        Integer.toString(check_files(path.get(i))),
                                        "0", //default
                                        "0",   //default
                                        Integer.toString(path_length_check.length),
                                        "0"
                                );
                                mMapInfo.add(mMapInfo_class); //array list에 data 전달
                                db_start++;
                            }
                        } else {
                            //               fail_folders_reason.add(path.get(i) + "(사진이 없습니다)");
                        }

                    } else {
                        fail_folders_reason.add(path.get(i) + ":(" + getLatLong + ")"+":내장");
                    }


                }
            }
            if (path_1 != null) {
                for (int i = 0; i < path_1.size(); i++) {
                    if (path_new[0].equals("0")) {
                        String getAddress = addressParser(path_1.get(i), 0);
                        String getLatLong = getCurrentLatLong(getAddress);

                        if (getLatLong.contains(",")) {
                            String[] LatLong = getLatLong.split(",");

                            if (check_files(path_1.get(i)) >= 0) {
                                String[] path_length_check = path_1.get(i).split("/");
                                //          Log.d(TAG, "folder_level: " + path_length_check.length);
                                int detect = -1;
                                for (int k = 0; k < mMapInfo.size(); k++) {
                                    if (path_1.get(i).equals(mMapInfo.get(k).getfolder())) {
                                        detect = k;
                                    }
                                }

                                if (detect > -1) {
                                    mMapInfo_class = new MapInfo(
                                            mMapInfo.get(detect).getid(),
                                            mMapInfo.get(detect).getfolder(),
                                            mMapInfo.get(detect).getlatitude(),
                                            mMapInfo.get(detect).getlongitude(),
                                            mMapInfo.get(detect).getreserved(),
                                            mMapInfo.get(detect).getFav(),
                                            mMapInfo.get(detect).getdata(),
                                            mMapInfo.get(detect).getlevel(),
                                            "2");
                                    mMapInfo.set(detect, mMapInfo_class);
                                } else {
                                    mMapInfo_class = new MapInfo(
                                            Integer.toString(db_start),
                                            path_1.get(i),
                                            LatLong[0],
                                            LatLong[1],
                                            Integer.toString(check_files(path_1.get(i))),
                                            "0", //default
                                            "0",   //default
                                            Integer.toString(path_length_check.length),
                                            "1"
                                    );
                                    mMapInfo.add(mMapInfo_class); //array list에 data 전달
                                    db_start++;
                                }
                            } else {
                                //               fail_folders_reason.add(path.get(i) + "(사진이 없습니다)");
                            }

                        } else {
                            fail_folders_reason.add(path.get(i) + ":(" + getLatLong + ")"+":외장");
                        }


                    }
                }
            }

            for (int i = 0; i < path.size(); i++) {
                if (path_new[0].equals("1")) { //새폴더 생성

                    String[] path_length_check = path.get(i).split("/");
                    //             Log.d(TAG, "folder_level: " + path_length_check.length);

                    mMapInfo_class = new MapInfo(
                            Integer.toString(db_start),
                            path.get(i),
                            path_new[1],
                            path_new[2],
                            "0",  //새폴더 생성이기 때문에 사진이 없음
                            "0",
                            path_new[3],
                            Integer.toString(path_length_check.length),
                            Integer.toString(get_storage_choice())

                    );
                    mMapInfo.add(mMapInfo_class); //array list에 data 전달
                    db_start++;
                }
            }
/*
            for (int i = 0; i < fail_folders_reason.size(); i++) {
                Log.d(TAG, "fail reason:" + fail_folders_reason.get(i));
            }
*/
            save_db(0);
// to do
// to do
            if (fail_folders_reason.size() > 0) {
  //              no_db_folder.clear();
                int add_data = 0;

                for (int i = 0; i < user_folders_internal.size(); i++) {
                    for (int j = 0; j < mMapInfo.size(); j++) {
                        if (user_folders_internal.get(i).equals(mMapInfo.get(j).getfolder())) {
               //             Log.d(TAG, "user_folders:" + user_folders.get(i) + " attached to mMapinfo");
                            add_data = 0;
                            break;
                        } else {
                            add_data = 1;
                        }
                    }
                    if (add_data == 1) {
                        no_db_folder.add(user_folders_internal.get(i) + ":내장"); //arraylist의 주소를  배열로 저장
          //              Log.d(TAG, "user_folders:" + user_folders.get(i));
                        add_data = 0;
                    }
                }

                for (int i = 0; i < user_folders_external.size(); i++) {
                    for (int j = 0; j < mMapInfo.size(); j++) {
                        if (user_folders_external.get(i).equals(mMapInfo.get(j).getfolder())) {
                            //             Log.d(TAG, "user_folders:" + user_folders.get(i) + " attached to mMapinfo");
                            add_data = 0;
                            break;
                        } else {
                            add_data = 1;
                        }
                    }
                    if (add_data == 1) {
                        no_db_folder.add(user_folders_external.get(i) + ":외장"); //arraylist의 주소를  배열로 저장
                        //              Log.d(TAG, "user_folders:" + user_folders.get(i));
                        add_data = 0;
                    }
                }
                if (customProgressDialog.isShowing()) {
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(
                            PROGRESS,
                            STOP, 0), 0);
                }
                mHandler.sendEmptyMessageDelayed(PROGRESS_ALERT, 20);

            }

            if (customProgressDialog.isShowing()) {
                mHandler.sendMessageDelayed(mHandler.obtainMessage(
                        PROGRESS,
                        STOP, 0), 0);
            }
            mHandler.sendMessage(mHandler.obtainMessage(
                    MainActivity.CAMERA_UPDATE,
                    MainActivity.UPDATE_CURLATLON, 0));

            //init array lists
            user_folders_internal.clear();
            user_folders_external.clear();
    //        fail_folders_reason.clear();
            cursor.close();
        } catch (Exception e) {
            if (customProgressDialog.isShowing()) {
                mHandler.sendMessageDelayed(mHandler.obtainMessage(
                        PROGRESS,
                        STOP, 0), 0);
            }
            cursor.close();
            e.printStackTrace();
        }
    }

    public void make_folder_dialog(LatLng latLng) {
        vibrator.vibrate(100);
        /*
        mHandler.sendMessageDelayed(mHandler.obtainMessage(
                PROGRESS,
                START, 0), 0);
*/
        final String positionAddress = getPositionAddress(latLng.latitude, latLng.longitude);

        final String positionSnippet = " 위도:" + String.valueOf(form.format(latLng.latitude))
                + " 경도:" + String.valueOf(form.format(latLng.longitude));

        final String lat = String.valueOf(form.format(latLng.latitude));
        final String lon = String.valueOf(form.format(latLng.longitude));
        final StringBuilder strPosition = new StringBuilder("");
        final LatLng new_LatLng;

        strPosition.append("주소:");
        if (string_contains_checker(positionAddress) == false) {
            strPosition.append(positionAddress);
        } else {
            strPosition.append("주소정보없음");
        }
        strPosition.append("\n");
        strPosition.append(positionSnippet);

        new_LatLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));

        //        custom_marker = settings_gridActivity.get_data(marker_choice);

        if (force_path != null) {
            strPosition.setLength(0);
            strPosition.append(force_path);
        }

        StringBuilder makeDir = new StringBuilder("");
        makeDir.append(positionAddress);
        makeDir.append(positionSnippet);

        addressParser(makeDir.toString(), 1);

        final StringBuilder path = new StringBuilder();
        if (force_path == null) {
            for (int i = 0; i < makeFolder.length; i++) {
                //                          Log.d(TAG, makeFolder[i] + "\n");
                if (i < level_choice) {
                    path.append(makeFolder[i]);
                    if (i < level_choice - 1)
                        path.append("/");
                }
            }
        } else {
            path.append(force_path);
        }

        final GridCustomDialog dialog = new GridCustomDialog(mActivity, "폴더 생성","폴더 "
                + level_choice + " 단계 적용 중입니다.\n\n"
                + strPosition.toString(), "여기에 폴더를 만드시겠습니까?",
                path.toString(), Integer.parseInt(mSettingInfo.size() > 0 ? mSettingInfo.get(0).getMarker() : Integer.toString(1)),
                -1); //data todo
        dialog.setDialogListener(new GridCustomListener() {  // MyDialogListener 를 구현
            @Override
            public int onPositiveClicked(Integer cursor, String folder) {
                int status = 1;
      //          Log.d(TAG, "ok : cursor: " + cursor); //여기서 커스텀 다이얼로그가 누른 값 return

                if (cursor == -1) {
                    Toast.makeText(getApplicationContext(), "폴더를 선택하세요", Toast.LENGTH_SHORT).show();
                    status = 0;
                } else {
                    int mkdir = 0;

                    if (folder != null) {
                        if (folder.contains(" ")) {
                            Toast.makeText(getApplicationContext(), "경로에 공란이 있습니다.", Toast.LENGTH_SHORT).show();
                            status = 0;
                        } else if (folder.contains(".")) {
                            Toast.makeText(getApplicationContext(), "경로에 '.'이 있습니다.", Toast.LENGTH_SHORT).show();
                            status = 0;
                        } else {
                            path.setLength(0); //원래값 초기화

                            String[] path_check = folder.split("/");

                            for (int i = 0; i < path_check.length; i++) {
                                path.append(path_check[i]);

                                if (i < path_check.length - 1)
                                    path.append("/");
                            }
                        }
                    }

             //       Log.d(TAG, "path: " + path.toString() + " folder: " + folder + "status " + status);

                    if (status == 1) {

                        if (string_contains_checker(path.toString()) == true) {
                    //        Log.d(TAG, "path:주소정보없음");
                            path.setLength(0);
                            path.append("주소정보없음");
                        }

                        //아무 db가 없는지 check
                        if (mMapInfo.size() == 0) {
                            mkdir = 1;
                        }

                        for (int i = 0; i < mMapInfo.size(); i++) {
                            if (path.toString().equals(mMapInfo.get(i).getfolder())) {
                                Toast.makeText(getApplicationContext(), "같은 경로가 있습니다.\n" + mMapInfo.get(i).getfolder(), Toast.LENGTH_LONG).show();
                                status = 0;
                                mkdir = 0;
                                break;
                            } else {
                                mkdir = 1;
                            }
                        }

                        if (mkdir == 1) {

                            ArrayList<String> add_path = new ArrayList<>();
                            add_path.add(path.toString());
                            String[] new_path = new String[4]; //여기 size
                            new_path[0] = "1"; //1이면 새로 생성이다
                            new_path[1] = lat;
                            new_path[2] = lon;
                            new_path[3] = Integer.toString(cursor); //유저가 선택한 커스텀 마커 모양 저장

                            Camera2BasicFragment cam_activity = new Camera2BasicFragment();
                            if (cam_activity.setDirectory(3, Appdir, add_path.toArray(new String[add_path.size()]), level_choice) == true) { //폴더 생성 성공
                                update_path(add_path, null, new_path);
                        //        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new_LatLng, 12);
                       //         mGoogleMap.moveCamera(cameraUpdate);
                                update_newLatLng = new_LatLng;
                                mHandler.sendMessage(mHandler.obtainMessage(
                                        MainActivity.CAMERA_UPDATE,
                                        MainActivity.UPDATE_NEWLATLON, 0));
                                Toast.makeText(getApplicationContext(), "폴더 만들기를 성공 하였습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "폴더 만들기를 실패 하였습니다.", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }

                }

                if (force_path != null)
                    force_path = null;

                if (status == 1)
                    dialog.dismiss();

                return status;
            }

            @Override
            public void onNegativeClicked() {
           //     Log.d(TAG, "cancel");
      //          Toast.makeText(getBaseContext(), "취소 되었습니다", Toast.LENGTH_SHORT).show();

                if (force_path != null)
                    force_path = null;
            }
        });
    //    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
   //     customProgressDialog.dismiss();

    }

    public int return_update_status() {
        return update_status;
    }

    public int return_mapinfo_size(int control) {
        int value = 0;
        if (control == 0) {
            value = mMapInfo.size();
        } else {
            value = loading_cnt;

        }

        return value;
    }

    public ArrayList<String> combine_storage_path(ArrayList<String> internal, ArrayList<String> external) {
        ArrayList<String> combined_array = new ArrayList<>();

        if (internal.size() == 0 && external.size() == 0) {
            return combined_array;
        }


        for (int i = 0; i < internal.size(); i++) {
            if (!internal.get(i).equals(""))
                combined_array.add(internal.get(i) + "(내장)");
        }

        for (int i = 0; i < external.size(); i++) {
            if (!external.get(i).equals(""))
                combined_array.add(external.get(i)+ "(외장)");
        }

        return combined_array;
    }

    //todo external path에 대한 고려
    public ArrayList<String> check_child_path(int storage_status) {
        ArrayList<String> saved_path_1 = new ArrayList<>();
        ArrayList<String> saved_path_2 = new ArrayList<>();
        ArrayList<String> saved_path_3 = new ArrayList<>();

        //폴더내 모든 경로 가져오기
        saved_path_1 = readDirectoryPath("", storage_status);

        if (saved_path_1 == null || saved_path_1.size() == 0) {
            return saved_path_2;
        }

        //1단계
        for (int i = 0; i < saved_path_1.size(); i++) {
            saved_path_2.add(saved_path_1.get(i));
        }
        saved_path_1.clear();

        int path_size = 0;

        while (true) {

            //+단계
            for (int i =0; i < saved_path_2.size(); i++) {
                saved_path_1 = readDirectoryPath(saved_path_2.get(i), storage_status);
                for (int j = 0; j < saved_path_1.size(); j++) {
                    saved_path_3.add(saved_path_1.get(j));
                }
                saved_path_1.clear();
            }
            if (path_size == saved_path_3.size()) {
                int detect = 0;
                for (int i = 0; i < path_size; i++) {
                    if (saved_path_3.get(i).equals(saved_path_2.get(i))) {
                    } else {
                        detect = 1;
                    }
   //                 Log.d(TAG, "3 " + saved_path_3.get(i) + " 2 " + saved_path_2.get(i));
                }
                if (detect == 0) {
                    saved_path_2.clear();
                    break;
                }
            } else {
                path_size = saved_path_3.size();
            }

            saved_path_2.clear();


            //++단계
            for (int i =0; i < saved_path_3.size(); i++) {
                saved_path_1 = readDirectoryPath(saved_path_3.get(i), storage_status);
                for (int j = 0; j < saved_path_1.size(); j++) {
                    saved_path_2.add(saved_path_1.get(j));
                }
                saved_path_1.clear();
            }
              if (path_size == saved_path_2.size()) {
                  int detect = 0;
                  for (int i = 0; i < path_size; i++) {
                      if (saved_path_2.get(i).equals(saved_path_3.get(i))) {
                      } else {
                          detect = 1;
                      }
           //           Log.d(TAG, "2 " + saved_path_2.get(i) + " 3 " + saved_path_3.get(i));

                  }
                  if (detect == 0) {
                      saved_path_3.clear();
                      break;
                  }

              } else {
                  path_size = saved_path_2.size();
              }

            saved_path_3.clear();

        }

        //while 끝

        if (saved_path_2.size() > 0) {
            for (int i = 0; i < saved_path_2.size(); i++) {
    //            Log.d(TAG, "folders in storage: " + saved_path_2.get(i));
            }
            saved_path_1 = containers_check(last_folder_path, storage_status);
        } else if (saved_path_3.size() > 0) {
            for (int i = 0; i < saved_path_3.size(); i++) {
    //            Log.d(TAG, "folders in storage: " + saved_path_3.get(i));
            }
            saved_path_1 = containers_check(last_folder_path, storage_status);
        }


/*
        for (int i =0; i < saved_path_1.size(); i++) {
            StringBuilder cnt_info = new StringBuilder("");
            cnt_info.append(saved_path_1.get(i) + "/" + check_files(saved_path_1.get(i)));
            saved_path_1.set(i, cnt_info.toString());
        }
*/
/*
        for (int i =0; i < saved_path_1.size(); i++) {
            String getAddress = addressParser(saved_path_1.get(i));
            String[] LatLong = getCurrentLatLong(getAddress).split(",");
            Log.d(TAG, "containers : " + getAddress + " (" + LatLong[0] + " : " + LatLong[1] + ") ");

        }
*/


       for (int i = 0; i < saved_path_1.size(); i++) {
            if (saved_path_1.get(i).equals("")) {
                saved_path_1.remove(i);
            }
        }

        //정리
        last_folder_path.clear();

        return saved_path_1;
    }

    public ArrayList<String> containers_check (ArrayList<String> path, int storage_status) {
        ArrayList<String> no_containers = new ArrayList<>();
        int detect = 0;

        for (int i = 0; i < path.size(); i++) {
            for (int j = 0; j < mMapInfo.size(); j++) {
                if (mMapInfo.get(j).getfolder().equals(path.get(i))) {
                    if (storage_status == Integer.parseInt(mMapInfo.get(j).getStorage())
                            || Integer.parseInt(mMapInfo.get(j).getStorage()) == imagesView.BOTH_STORAGE) {
                        detect = 1;
                    } else  {

                    }
                }
            }
/*
            if (path.get(i).contains(".") | path.get(i).contains(",")) {
                detect = 1;
            }
*/
            if (detect == 0) {
                no_containers.add(path.get(i));
            } else {
                detect = 0;
            }
        }
/*
        for (int i = 0; i < mMapInfo.size(); i++) {
            Log.d(TAG, "mMapInfo path: " + mMapInfo.get(i).getfolder());
        }
        for (int i = 0; i < no_containers.size(); i++) {
            Log.d(TAG, "need to add: " + no_containers.get(i));
        }
*/
        return no_containers;
    }


    static ArrayList<String> last_folder_path = new ArrayList<>();
    //폴더에서 주소를 파싱해서 붙여줌



    public ArrayList<String> readDirectoryPath(String child_path, int storage_status) {
        ArrayList<String> FolderPath = new ArrayList<>();
        ArrayList<String> userFolders = new ArrayList<>();
        File directoryPath;
        if (storage_status ==  imagesView.INTERNAL_STORAGE) {
            directoryPath = new File(Environment.getExternalStorageDirectory() + Appdir, child_path);
        } else {
            directoryPath = new File(sdcard_path_header + Appdir, child_path);
        }
//        Log.d(TAG, "child_path:" + child_path);
        String[] fileList = directoryPath.list();
        if (fileList == null) {
      //      Log.d(TAG, "no Lists");
            return null;
        } else {
            //사진이 없는 경우도 폴더에 포함
            if (fileList.length == 0) {
    //            Log.d(TAG, "last folder:" + child_path);
                userFolders.add(userFolders.size(), child_path);

                int detect = 0;

                for (int j = 0; j < last_folder_path.size(); j ++) {
                    if (child_path.equals(last_folder_path.get(j)))
                        detect = 1;
                }

                if (detect == 0)
                    last_folder_path.add(last_folder_path.size(), child_path);

            } else {
/*
                for (int i = 0; i < fileList.length; i++) {
                   Log.d(TAG, "list: " + fileList[i]);
                }
*/

//확장자 정리

         int check_files_status = 0;

                for (int i = 0; i < fileList.length; i++) {
                    if (fileController.file_format_check(fileList[i]) == 0) {
                        FolderPath.add(fileList[i]);
                    } else {
                        check_files_status = 1;
                    }
             /*
                    if (!fileList[i].contains(".txt")
                            && !fileList[i].contains(".jpg")
                            && !fileList[i].contains(".JPG")
                            && !fileList[i].contains(".png")
                            && !fileList[i].contains(".gif")
                            && !fileList[i].contains(".jpeg")
                            && !fileList[i].contains(".bmp")
                            && !fileList[i].contains(".tif")
                            && !fileList[i].contains(".tiff")
                            && !fileList[i].contains(".raw")
                            && !fileList[i].contains(".psd")) {
                        FolderPath.add(fileList[i]);
                    } else {
                        check_files_status = 1;
                    }
                    */
                }

                int detect = 0;

                for (int j = 0; j < last_folder_path.size(); j ++) {
                    if (child_path.equals(last_folder_path.get(j)))
                        detect = 1;
                }

                if (FolderPath.size() == 0) {
        //            Log.d(TAG, "last folder:" + child_path);
                    userFolders.add(userFolders.size(), child_path);
                    if (detect == 0)
                        last_folder_path.add(last_folder_path.size(), child_path);
                } else {
                    for (int i = 0; i < FolderPath.size(); i++) {
                        StringBuilder userFolderPath = new StringBuilder("");
                        if (child_path != "") {
                            userFolderPath.append(child_path + "/");
                        }
                        userFolderPath.append(FolderPath.get(i));
                        userFolders.add(i, userFolderPath.toString());
                    }

                    if (check_files_status == 1) {

                        if (detect == 0)
                            last_folder_path.add(last_folder_path.size(), child_path);
                    }

                }

            }
/*
            for (int i = 0; i < userFolders.size(); i++) {
                Log.d(TAG, "forders: " + userFolders.get(i));
            }

            for (int i =0; i < last_folder_path.size(); i++) {
                Log.d(TAG, "last_folder_path: " + last_folder_path.get(i));
            }
*/
        }

        return userFolders;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mTrackPosition == true) {
            currentPosition
                    = new LatLng(location.getLatitude(), location.getLongitude());

            update_newLatLng = new LatLng(location.getLatitude(), location.getLongitude());

            String markerTitle = getCurrentAddress(location);
            String markerSnippet = " 위도:" + String.valueOf(form.format(location.getLatitude()))
                    + " 경도:" + String.valueOf(form.format(location.getLongitude()));

//                Log.d(TAG, markerTitle + markerSnippet);

            StringBuilder strPosition = new StringBuilder("");
            strPosition.append(markerTitle);
            strPosition.append(markerSnippet);
      //      if (info_btn == 1) {
       //         mtextInfo.setText("현재위치 주소: " + strPosition);
      //      }
            //현재 위치에 마커 생성하고 이동
            setCurrentLocation(location, markerTitle, markerSnippet);

            mCurrentLocatiion = location;

            addressParser(strPosition.toString(), 0);

     //       Log.d(TAG, "onLocationChanged: " + strPosition.toString());


            try {
                String[] data = new String[10]; //여기 size
                data[2] = Double.toString(mCurrentLocatiion.getLatitude());
                data[3] = Double.toString(mCurrentLocatiion.getLongitude());
                save_arraylist_db(SETTING_DB, 0, data);
    //            save_db(3);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    protected void onStart() {

        if(mGoogleApiClient != null && mGoogleApiClient.isConnected() == false){

  //          Log.d(TAG, "onStart: mGoogleApiClient connect");
            mGoogleApiClient.connect();
        }

        super.onStart();
    }

    @Override
    protected void onStop() {

        if (mRequestingLocationUpdates) {

   //         Log.d(TAG, "onStop : call stopLocationUpdates");
            stopLocationUpdates();
        }

        if ( mGoogleApiClient.isConnected()) {

    //        Log.d(TAG, "onStop : mGoogleApiClient disconnect");
            mGoogleApiClient.disconnect();
        }

        if (intent_from.contains("WidgetMain")) {
            finish();
        }

        //kill connect check thread
        if (mCheckIsConnect != null) {
            mCheckIsConnect.interrupt();
            mCheckIsConnect = null;
        }
        //kill zoom thread
        if (mZoomThread != null) {
            mZoomThread.interrupt();
            mZoomThread = null;

        }

        if (inter_line == 1) {
            if (arrayPoints_gray.size() != 0) {

                if (animator.isRunning())
                    animator.cancel();

                arrayPoints_gray.clear();


                for (Polyline line : polyline) {
                    line.remove();
                }
                polyline.clear();

            }
        }

        super.onStop();
    }


    @Override
    public void onConnected(Bundle connectionHint) {


        if ( mRequestingLocationUpdates == false ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

                if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED) {

                    ActivityCompat.requestPermissions(mActivity,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                } else {

           //         Log.d(TAG, "onConnected : 퍼미션 가지고 있음");
           //         Log.d(TAG, "onConnected : call startLocationUpdates");
                    startLocationUpdates();
                    mGoogleMap.setMyLocationEnabled(true);

                }

            } else {

          //      Log.d(TAG, "onConnected : call startLocationUpdates");
                startLocationUpdates();
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
        gps_status = 1;
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

      //  Log.d(TAG, "onConnectionFailed");
        try {
            MapsInitializer.initialize(getApplicationContext());

            //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
            //지도의 초기위치를 서울로 이동
            setDefaultLocation();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Override
    public void onConnectionSuspended(int cause) {

    //    Log.d(TAG, "onConnectionSuspended");
        if (cause == CAUSE_NETWORK_LOST)
            Log.e(TAG, "onConnectionSuspended(): Google Play services " +
                    "connection lost.  Cause: network lost.");
        else if (cause == CAUSE_SERVICE_DISCONNECTED)
            Log.e(TAG, "onConnectionSuspended():  Google Play services " +
                    "connection lost.  Cause: service disconnected");
    }


    public String getCurrentAddress(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            mHandler.sendMessage(mHandler.obtainMessage(
                    MainActivity.ERROR_LOG,
                    MainActivity.ERROR_NO_GEOGODER_SERVICE, 0));
   //         Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_SHORT).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            mHandler.sendMessage(mHandler.obtainMessage(
                    MainActivity.ERROR_LOG,
                    MainActivity.ERROR_NO_GPS, 0));
       //     Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_SHORT).show();
            return "잘못된 GPS 좌표";

        }
        if (addresses == null || addresses.size() == 0) {
            mHandler.sendMessage(mHandler.obtainMessage(
                    MainActivity.ERROR_LOG,
                    MainActivity.ERROR_NO_ADDRESS, 0));
  //          Toast.makeText(this, "주소 미발견", Toast.LENGTH_SHORT).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }
    }

    public String getPositionAddress(double Latitude, double Longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {

            addresses = geocoder.getFromLocation(
                    Latitude,
                    Longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
   //         Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            mHandler.sendMessage(mHandler.obtainMessage(
                    MainActivity.ERROR_LOG,
                    MainActivity.ERROR_NO_GEOGODER_SERVICE, 0));
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            mHandler.sendMessage(mHandler.obtainMessage(
                    MainActivity.ERROR_LOG,
                    MainActivity.ERROR_NO_GPS, 0));
 //           Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }
        if (addresses == null || addresses.size() == 0) {
            mHandler.sendMessage(mHandler.obtainMessage(
                    MainActivity.ERROR_LOG,
                    MainActivity.ERROR_NO_ADDRESS, 0));
  //          Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {

        mMoveMapByUser = false;


        if (currentMarker != null) currentMarker.remove();


        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        //구글맵의 디폴트 현재 위치는 파란색 동그라미로 표시
        //마커를 원하는 이미지로 변경하여 현재 위치 표시하도록 수정해야함.
        /*
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentMarker = mGoogleMap.addMarker(markerOptions);
*/

        if ( mMoveMapByAPI ) {

   //         Log.d( TAG, "setCurrentLocation :  mGoogleMap moveCamera "
     //               + location.getLatitude() + " " + location.getLongitude() ) ;
            // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15);
            mHandler.sendMessage(mHandler.obtainMessage(
                    MainActivity.CAMERA_UPDATE,
                    MainActivity.UPDATE_CURLATLON, 0));
      //      CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 12);
      //      mGoogleMap.animateCamera(cameraUpdate);
        }
    }

    public void setCoordLocation(double Latitude, double Longitude, String markerTitle, String markerSnippet) {

        //     mMoveMapByUser = false;


//        if (coordMarker != null) coordMarker.remove();

        LatLng currentLatLng = new LatLng(Latitude, Longitude);

        //구글맵의 디폴트 현재 위치는 파란색 동그라미로 표시
        //마커를 원하는 이미지로 변경하여 현재 위치 표시하도록 수정해야함.
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(false);
        markerOptions.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        coordMarker = mGoogleMap.addMarker(markerOptions);
/*
            PositionItem item = new PositionItem(Latitude,Longitude, 1);
            mPosi.add(item);



            addItems(mPosi);
*/

/*
            if (toggle == 0) {
                markerOptions.icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_map_start));
                coordMarker = mGoogleMap.addMarker(markerOptions);
                toggle = 1;
            } else {
                markerOptions.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                coordMarker = mGoogleMap.addMarker(markerOptions);
                toggle = 0;
            }
*/
/*
        if ( mMoveMapByAPI ) {

            Log.d( TAG, "setCurrentLocation :  mGoogleMap moveCamera "
                    + Latitude + " " + Longitude) ;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
            mGoogleMap.moveCamera(cameraUpdate);
        }
        */
    }

    public void setCustomWindow(double Latitude, double Longitude, String markerTitle, String markerSnippet) {

        LatLng currentLatLng = new LatLng(Latitude, Longitude);

        MarkerOptions markerOptions = new MarkerOptions();
/*
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

*/
        markerOptions.position(currentLatLng)
                .title(markerTitle)
                .snippet(markerSnippet)
                .icon(BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_BLUE));

        //string으로 data 던질 수 있음
        InfoWindowData info = new InfoWindowData();
        info.setImage("ic_pin_01");
        info.setHotel("Hotel : excellent hotels available");
        info.setFood("Food : all types of restaurants available");
        info.setTransport("Reach the site by bus, car and train.");

        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
        mGoogleMap.setInfoWindowAdapter(customInfoWindow);

        customMarker = mGoogleMap.addMarker(markerOptions);
        customMarker.setTag(info);
        customMarker.showInfoWindow();

    }

    public void setDefaultLocation() {

        mMoveMapByUser = false;


        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";


        if (currentMarker != null) currentMarker.remove();
/*
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(false);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = mGoogleMap.addMarker(markerOptions);
*/
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 12);
        mGoogleMap.animateCamera(cameraUpdate);

    }


    //return the dynamic address insert ArrayList,
    //and camera background thread will check address
    public static String addressParser(String address, int control) {
        //   Log.d(TAG,"addressParser");

        String subReplaceAddr = null;
        int addressSize;
        List<String> arrayAddress = new ArrayList<>();

        if (address.contains(",") == true) {
            subReplaceAddr = address.replace(",", "");
            arrayAddress = Arrays.asList(subReplaceAddr.split(" "));
        } else if (address.contains("/") == true) {
            subReplaceAddr = address.replace("/", " ");
        } else {
            arrayAddress = Arrays.asList(address.split(" "));
        }

        addressSize = arrayAddress.size();
        //init the address before store new one
        arrayAddresses = null;
        arrayAddresses = arrayAddress.toArray(new String[addressSize]);

        if (control == 1) {
            makeFolder = null;
            makeFolder = arrayAddress.toArray(new String[addressSize]);
        }
        //test log
/*
        for (int i = 0; i < addressSize; i++) {
            Object obj = arrayAddress.get(i);
            if (obj instanceof String) {
                String str = (String) obj;
                Log.d(TAG,  str);
            }
        }

        for (int i = 0; i < arrayAddresses.length; i++) {
                Log.d(TAG,  arrayAddresses[i]);
        }
*/

        return subReplaceAddr;
    }

    //get data from camera thread
    public String[] returnAddress() {
        //       Log.d(TAG, "returnAddress");

        return arrayAddresses;
    }


    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        int permissonCnt = 0;
        Boolean needPermissionRequest = false;
        Boolean needRationaleRequest = false;

        permissonCnt = permissions.size();

        //arraylist to string array
        String[] arrayPermissions = permissions.toArray(new String[permissonCnt]);

        for (int i = 0; i < permissonCnt; i++) {
            Object obj = permissions.get(i);
            if (obj instanceof String) {
                String str = (String) obj;
                permissonsRationale.add(new Boolean(ActivityCompat.shouldShowRequestPermissionRationale(this, str)));
                hasPermissions.add(new Integer(ContextCompat.checkSelfPermission(this, str)));
          //      Log.d(TAG, " " + str + ": " + hasPermissions.get(i) + ", " + permissonsRationale.get(i));

                if ((Integer) hasPermissions.get(i) == PackageManager.PERMISSION_DENIED) {
                    needPermissionRequest = true;
                }

                if ((Boolean) permissonsRationale.get(i) == false) {
                    needRationaleRequest = true;
                }
            }
       //    Log.d(TAG,"requestPermissionString: " + arrayPermissions[i]);

        }

        if (needPermissionRequest) {
            ActivityCompat.requestPermissions(this, arrayPermissions, 0);
       //     Log.d(TAG,"requesting...");
        }

        //           if (needRationaleRequest) {
        //              showDialogForPermissionSetting("퍼미션 거부 + Don't ask again(다시 묻지 않음) " +
        //                      "체크 박스를 설정한 경우로 설정에서 퍼미션 허가해야합니다.");
        //          }

        if ((Integer) hasPermissions.get(0) == PackageManager.PERMISSION_GRANTED) {
            if ( mGoogleApiClient.isConnected() == false) {

            //    Log.d(TAG, "checkPermissions : 퍼미션 가지고 있음");
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
   //     Log.d(TAG, "permissionResult");

        if ((permsRequestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION && grantResults.length > 0) ||
                (permsRequestCode == PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE && grantResults.length > 0) ||
                (permsRequestCode == PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE && grantResults.length > 0)) {

            boolean permissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (permissionAccepted) {

                if ( mGoogleApiClient.isConnected() == false) {

              //      Log.d(TAG, "onRequestPermissionsResult : mGoogleApiClient connect");
                    mGoogleApiClient.connect();
                }

            } else {
                checkPermissions();
            }
        }
    }

    /*
            @TargetApi(Build.VERSION_CODES.M)
            private void showDialogForPermission(String msg, int count) {

                for (int i =0; i < count; i++) {
                    Object obj = permissions.get(i);
                    final String str = (String)obj;

                    AlertDialog.Builder builder = new AlertDialog.Builder(GoogleMapActivity.this);
                    builder.setTitle("알림");
                    builder.setMessage(msg);
                    builder.setCancelable(false);
                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            int cnt = 0;
                            ActivityCompat.requestPermissions(mActivity,
                                    new String[]{str}, cnt++);

                            ActivityCompat.requestPermissions(mActivity,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            ActivityCompat.requestPermissions(mActivity,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                        }
                    });

                    builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
                    builder.create().show();
                }
            }
    */
    private void showDialogForPermissionSetting(String msg) {

    //    Log.d(TAG,"showDialogForPermissionSetting");

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(true);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                askPermissionOnceAgain = true;

                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + mActivity.getPackageName()));
                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(myAppSettings);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create().show();
    }

    static int service_setting_toggle = 0;
    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        if (service_setting_toggle == 1)
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "GPS를 설정 하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
                dialog.cancel();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                gps_status = 0;
                service_setting_toggle = 0;
                dialog.cancel();
            }
        });
        builder.create().show();
        service_setting_toggle = 1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                 //       Log.d(TAG, "onActivityResult : 퍼미션 가지고 있음");

                        if ( mGoogleApiClient.isConnected() == false ) {
                       //     Log.d( TAG, "onActivityResult : mGoogleApiClient connect ");
                            mGoogleApiClient.connect();

                            gps_status = 1;
                        }
                        return;
                    }
                }

                break;
        }
    }

    public void showPlaceInformation(LatLng location)
    {
        mGoogleMap.clear();//지도 클리어

        if (previous_marker != null)
            previous_marker.clear();//지역정보 마커 클리어

        new NRPlaces.Builder()
                .listener(MainActivity.this)
                .key(KEY)
                .latlng(location.latitude, location.longitude)//현재 위치
                .radius(1000) //1000 미터 내에서 검색
                //      .type(PlaceType.RESTAURANT) //음식점
                .type(PlaceType.BAKERY) //빵집
                .build()
                .execute();
    }

    public void onPlacesFailure(PlacesException e) {

    }

    @Override
    public void onPlacesStart() {

    }

    @Override
    public void onPlacesSuccess(final List<Place> places) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (noman.googleplaces.Place place : places) {

                    LatLng latLng
                            = new LatLng(place.getLatitude()
                            , place.getLongitude());

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(place.getName());
                    markerOptions.snippet(place.getVicinity());
                    Marker item = mGoogleMap.addMarker(markerOptions);
                    previous_marker.add(item);

                }

                //중복 마커 제거
                HashSet<Marker> hashSet = new HashSet<Marker>();
                hashSet.addAll(previous_marker);
                previous_marker.clear();
                previous_marker.addAll(hashSet);

            }
        });
    }

    @Override
    public void onPlacesFinished() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*
            case R.id.button_search:
                Log.d(TAG, "button_search click");
                showPlaceInformation(currentPosition);
                break;

            case R.id.button_track:
                Log.d(TAG, "button_track click");

                if (mTrackPosition == false)
                    mTrackPosition = true;
                else
                    mTrackPosition = false;

                Toast.makeText(this, "tracking: " + mTrackPosition, Toast.LENGTH_SHORT).show();

                break;

            case R.id.button_trans:
                Log.d(TAG, "button_trans click");
                mcoordInfo.setText("");
                mtextInfo.setText("");

                break;

            case R.id.button_custom:
                if (mMakerCustom == false)
                    mMakerCustom = true;
                else
                    mMakerCustom = false;
                Toast.makeText(this, "custom marker: " + mMakerCustom, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "button_custom click");

                break;

            case R.id.button_db:

                load_values();

                break;
            case R.id.button_db_delete:

                delete_values();

                break;
                */
            /*
            case R.id.menu_btn:

                PopupMenu p = new PopupMenu(this, // 현재 화면의 제어권자
                        v); // anchor : 팝업을 띄울 기준될 위젯
                getMenuInflater().inflate(R.menu.main_menu, p.getMenu());
                // 이벤트 처리

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    Boolean status = false;
                    if (info_btn == 1)
                        status = true;
                    p.getMenu().findItem(R.id.menu3).setChecked(status);
                    invalidateOptionsMenu();
                }

                p.setOnMenuItemClickListener(listener);
                p.show();//Popup Menu 보이기

                break;
*/
            case R.id.drag_btn:
           //     Log.d(TAG, "Draggable CAM btn click");

                try {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    //      currentMarker.hideInfoWindow();
                    //      coordMarker.hideInfoWindow();
                    if (arrayAddresses == null) {
                        if (Integer.parseInt(mSettingInfo.size() > 0 ? mSettingInfo.get(0).getLevel() : Integer.toString(default_cam)) == 1) {
                            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            try {
                                PackageManager pm = getPackageManager();

                                final ResolveInfo mInfo = pm.resolveActivity(i, 0);

                                Intent intent = new Intent();
                                intent.setComponent(new ComponentName(mInfo.activityInfo.packageName, mInfo.activityInfo.name));
                                intent.setAction(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_LAUNCHER);

                                startActivity(intent);
                            } catch (Exception e) {
                      //          Log.d("TAG", "Unable to launch camera: " + e);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "현재 위치를 찾을 수 없습니다(1).", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    if (arrayAddresses.length > 0) {
                        if (inter_line == 1) {
                            if (arrayPoints_gray.size() != 0) {

                                if (animator.isRunning())
                                    animator.cancel();

                                arrayPoints_gray.clear();


                                for (Polyline line : polyline) {
                                    line.remove();
                                }
                                polyline.clear();

                            }
                        }

                        if (Integer.parseInt(mSettingInfo.size() > 0 ? mSettingInfo.get(0).getLevel() : Integer.toString(default_cam)) == 2) {
                            update_newLatLng = null;
                            Intent intent_camtest = new Intent(getBaseContext(), CameraActivity.class);
                            intent_camtest.putExtra("from", "WidgetMain");
                            startActivity(intent_camtest);
                        } else {
                            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            try {
                                PackageManager pm = getPackageManager();

                                final ResolveInfo mInfo = pm.resolveActivity(i, 0);

                                Intent intent = new Intent();
                                intent.setComponent(new ComponentName(mInfo.activityInfo.packageName, mInfo.activityInfo.name));
                                intent.setAction(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_LAUNCHER);

                                startActivity(intent);
                            } catch (Exception e) {
                        //        Log.d("TAG", "Unable to launch camera: " + e);
                            }
                        }
                    } else {
                        //           alertDialog.setTitle("새 폴더 주소 확인");

                        // Setting Dialog Message:
                        alertDialog.setMessage("현재 위치를 찾을 수 없습니다(2).");

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.toad_app_icon_v4);

                        // Setting Positive "Yes" Button
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "카메라가 동작하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.listview_btn:
                v = mActivity.getLayoutInflater().inflate(R.layout.listview_btn_layout, null);

                final ListView listView=(ListView)v.findViewById(R.id.listviewbtn);
                Listviewitem items;
                final ArrayList<Listviewitem> data= new ArrayList<>();
                final Button button = (Button)v.findViewById(R.id.listview_btn_1);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
              //          Log.d(TAG, "Button click");
                        if (button.getText().toString().contains("즐겨찾기")) {
                            data.clear();
                            Listviewitem items;
                            for (int i = 0; i < mMapInfo.size(); i++) {
                                if (Integer.parseInt(mMapInfo.get(i).getFav()) > 0) {
                                    items = new Listviewitem(mSettingInfo.size() > 0 ? mSettingInfo.get(0).getMarker() : Integer.toString(1),
                                            mMapInfo.get(i).getdata(),
                                            mMapInfo.get(i).getfolder(),
                                            mMapInfo.get(i).getreserved(),
                                            Integer.parseInt(mMapInfo.get(i).getFav()),
                                            Integer.parseInt(mMapInfo.get(i).getStorage()));
                                    data.add(items);
                                }
                            }
                            listviewAdapter = new ListviewAdapter(getBaseContext(), R.layout.listview_item, data);
                            listView.setAdapter(listviewAdapter);

                            listviewAdapter.notifyDataSetChanged();

                            button.setText("모두 보기");

                        } else if (button.getText().toString().contains("모두")) {
                            Listviewitem items;
                            data.clear();
                            for (int i = 0; i < mMapInfo.size(); i++) {
                                    items = new Listviewitem(mSettingInfo.size() > 0 ? mSettingInfo.get(0).getMarker() : Integer.toString(1),
                                            mMapInfo.get(i).getdata(),
                                            mMapInfo.get(i).getfolder(),
                                            mMapInfo.get(i).getreserved(),
                                            Integer.parseInt(mMapInfo.get(i).getFav()),
                                            Integer.parseInt(mMapInfo.get(i).getStorage()));
                                    data.add(items);
                            }
                            listviewAdapter = new ListviewAdapter(getBaseContext(), R.layout.listview_item, data);
                            listView.setAdapter(listviewAdapter);

                            listviewAdapter.notifyDataSetChanged();

                            button.setText("즐겨찾기 보기");
                        }
                    }
                });

                data.clear();

                for (int i = 0; i < mMapInfo.size(); i++) {
                    items = new Listviewitem(mSettingInfo.size() > 0 ? mSettingInfo.get(0).getMarker() : Integer.toString(1),
                            mMapInfo.get(i).getdata(),
                            mMapInfo.get(i).getfolder(),
                            mMapInfo.get(i).getreserved(),
                            Integer.parseInt(mMapInfo.get(i).getFav()),
                            Integer.parseInt(mMapInfo.get(i).getStorage()));
                    data.add(items);
                }
                listviewAdapter = new ListviewAdapter(this, R.layout.listview_item, data);
                listView.setAdapter(listviewAdapter);

                listviewAdapter.notifyDataSetChanged();

                final AlertDialog listDialog = new AlertDialog.Builder(MainActivity.this).create();
                listDialog.setView(v);
                listDialog.setTitle("폴더 바로가기");
                listDialog.setIcon(R.drawable.toad_app_icon_v4);

                listDialog.getWindow().setLayout(maxDisplayWidth  * 3/4, maxDisplayHeight * 3/4);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        int id = -1;
                        for (int j = 0; j < mMapInfo.size(); j++) {
                            if (mMapInfo.get(j).getfolder().equals(listviewAdapter.getItem(i))) {
                                id = j;
                            }
                        }
                  //      Log.d(TAG, "onItemClick " + i + " id " + id);
                        if (id > -1) {
                            LatLng newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(id).getlatitude()), Double.parseDouble(mMapInfo.get(id).getlongitude()));

                            update_newLatLng = newLatLng;
                            mHandler.sendMessage(mHandler.obtainMessage(
                                    MainActivity.CAMERA_UPDATE,
                                    MainActivity.UPDATE_NEWLATLON, 0));
                        } else {
                            Toast.makeText(getApplicationContext(), "경로를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        listDialog.dismiss();
                    }
                });

                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                        Settings_GridActivity settings_gridActivity = new Settings_GridActivity();
                        String text_title = "";
                        String text_make_folder = "";
                        vibrator.vibrate(100);

                        int id = -1;
                        for (int j = 0; j < mMapInfo.size(); j++) {
                            if (mMapInfo.get(j).getfolder().equals(listviewAdapter.getItem(i))) {
                                id = j;
                            }
                        }
                  //      Log.d(TAG, "onItemClick " + i + " id " + id);

                        if (id > -1) {
                            // Setting Dialog Title
                            alertDialog.setTitle("폴더 정보");

                            StringBuilder str = new StringBuilder("");
                            str.append("폴더경로:" + mMapInfo.get(id).getfolder() + "\n");
                            str.append("경도:" + mMapInfo.get(id).getlatitude() + " 위도:" + mMapInfo.get(id).getlongitude() + "\n\n");
                            str.append("사진 수:" + mMapInfo.get(id).getreserved() + "장");

                            // Setting Dialog Message
                            alertDialog.setMessage(str.toString());

                            // Setting Icon to Dialog
                            alertDialog.setIcon(custom_marker.get(Integer.parseInt(mMapInfo.get(id).getdata())));

                            if (Integer.parseInt(mMapInfo.get(id).getFav()) > 0) {
                                text_title = "즐겨찾기 취소";
                            } else {
                                text_title = "즐겨찾기 추가";
                            }

                                if (Integer.parseInt(mMapInfo.get(id).getStorage()) == imagesView.INTERNAL_STORAGE) {
                                    text_make_folder = "폴더 만들기(외장)";
                                } else if (Integer.parseInt(mMapInfo.get(id).getStorage()) == imagesView.EXTERNAL_STORAGE) {
                                    text_make_folder = "폴더 만들기(내장)";
                                }

                        }
                        // Setting Positive "Yes" Button
                        alertDialog.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
/*
                        Toast.makeText(getApplicationContext(), "아직 지원하지 않습니다.", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
*/
                                // Write your code here to invoke YES event
                                //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                            }
                        });


                            if (Integer.parseInt(mMapInfo.get(id).getStorage()) != imagesView.BOTH_STORAGE) {
                                alertDialog.setNeutralButton(text_make_folder, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        int id = -1;

                                        String[] change_data = new String[9]; //여기 size
                                        try {
                                            for (int j = 0; j < mMapInfo.size(); j++) {
                                                if (mMapInfo.get(j).getfolder().equals(listviewAdapter.getItem(i))) {
                                                    id = j;
                                                }
                                            }
                                            if (Integer.parseInt(mMapInfo.get(id).getStorage()) == imagesView.INTERNAL_STORAGE) {
                                                if (settings_storage_activity.check_authority_external(mActivity) == true) {
                                                    String root_path = get_main_path();
                                                    fileController.folder_checker(MainActivity.this, get_main_path(), Appdir + File.separator + mMapInfo.get(id).getfolder(), fileController.MAKE_DIR);
                                                    change_data[8] = "2";
                                                } else {
                                                    Toast.makeText(mActivity, "외장메모리 접근 권한이 없습니다. 설정에서 저장위치 세팅을 하세요", Toast.LENGTH_SHORT).show();
                                                    Intent intent_setting = new Intent(getBaseContext(), SettingsActivity.class);
                                                    startActivity(intent_setting);
                                                    return;
                                                }
                                            } else if (Integer.parseInt(mMapInfo.get(id).getStorage()) == imagesView.EXTERNAL_STORAGE) {
                                                mediaStorageDir = new File(Environment.getExternalStorageDirectory() + Appdir, mMapInfo.get(id).getfolder());
                                                if (!mediaStorageDir.exists()) {
                                                    if (!mediaStorageDir.mkdirs()) {
                                                        Log.d(TAG, "failed to create directory");
                                                    }
                                                }
                                                change_data[8] = "2";
                                            }

                                            save_arraylist_db(MAIN_DB, id, change_data);


                                            Listviewitem items;
                                            items = new Listviewitem(mSettingInfo.size() > 0 ? mSettingInfo.get(0).getMarker() : Integer.toString(1),
                                                    mMapInfo.get(id).getdata(),
                                                    mMapInfo.get(id).getfolder(),
                                                    mMapInfo.get(id).getreserved(),
                                                    Integer.parseInt(mMapInfo.get(id).getFav()),
                                                    Integer.parseInt(mMapInfo.get(id).getStorage()));
                                            data.set(id, items);

                                            listviewAdapter = new ListviewAdapter(getBaseContext(), R.layout.listview_item, data);
                                            listView.setAdapter(listviewAdapter);

                                            listviewAdapter.notifyDataSetChanged();


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton(text_title, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                     //       Log.d(TAG, "fav: " + i + " which: " + which);
                            int id = -1;


                            try {

                                for (int j = 0; j < mMapInfo.size(); j++) {
                                    if (mMapInfo.get(j).getfolder().equals(listviewAdapter.getItem(i))) {
                                        id = j;
                                    }
                                }

//                                Log.d(TAG , "find id " + id);

                                String[] change_data = new String[9]; //여기 size
                                if (Integer.parseInt(mMapInfo.get(id).getFav()) > 0) {
                                    change_data[5] = "0";
                                } else {
                                    change_data[5] = "1";
                                }
                                save_arraylist_db(MAIN_DB, id, change_data);

                                if (button.getText().toString().contains("모두")) {
                                    Listviewitem items;
                                    data.clear();
                                    for (int i = 0; i < mMapInfo.size(); i++) {
                                        if (Integer.parseInt(mMapInfo.get(i).getFav()) > 0) {
                                            items = new Listviewitem(mSettingInfo.size() > 0 ? mSettingInfo.get(0).getMarker() : Integer.toString(1),
                                                    mMapInfo.get(i).getdata(),
                                                    mMapInfo.get(i).getfolder(),
                                                    mMapInfo.get(i).getreserved(),
                                                    Integer.parseInt(mMapInfo.get(i).getFav()),
                                                    Integer.parseInt(mMapInfo.get(i).getStorage()));
                                            data.add(items);
                                        }
                                    }
                                    listviewAdapter = new ListviewAdapter(getBaseContext(), R.layout.listview_item, data);
                                    listView.setAdapter(listviewAdapter);

                                    listviewAdapter.notifyDataSetChanged();
                                } else {
                                    Listviewitem items;
                                    items = new Listviewitem(mSettingInfo.size() > 0 ? mSettingInfo.get(0).getMarker() : Integer.toString(1),
                                            mMapInfo.get(id).getdata(),
                                            mMapInfo.get(id).getfolder(),
                                            mMapInfo.get(id).getreserved(),
                                            Integer.parseInt(mMapInfo.get(id).getFav()),
                                            Integer.parseInt(mMapInfo.get(id).getStorage()));
                                    data.set(id, items);

                                    listviewAdapter = new ListviewAdapter(getBaseContext(), R.layout.listview_item, data);
                                    listView.setAdapter(listviewAdapter);

                                    listviewAdapter.notifyDataSetChanged();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                        // Showing Alert Message
                        alertDialog.show();

                        return true;
                    }
                });

                listDialog.show();

                break;
        }
    }
/*
    public void add_folders() {
        //여기서 폴더 트리 검색
        Log.d(TAG, "add_folders");
        try {
            user_folders_internal = check_child_path(imagesView.INTERNAL_STORAGE);
            user_folders_external = check_child_path(imagesView.EXTERNAL_STORAGE);

            if (user_folders_internal.size() == 0 && user_folders_external.size() == 0) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("새 폴더 확인");
                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.toad_app_icon_v4);
                // Setting Dialog Message
                alertDialog.setMessage("\n경로 정보가 없습니다.");

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                // Showing Alert Message
                alertDialog.show();
            } else {

                if (user_folders_internal.size() > 0 || user_folders_external.size() > 0) {
                    ArrayList<String> combined_array = new ArrayList<>();
                    combined_array = combine_storage_path(user_folders_internal, user_folders_external);

                    String[] array = combined_array.toArray(new String[combined_array.size()]);
                    final CustomDialog_2 dialog = new CustomDialog_2(mActivity, array, "새 폴더 리스트", "DB에 저장되지 않은 경로가 있습니다. 적용 하시겠습니까?");
                    dialog.setDialogListener(new DialogListener_2() {  // MyDialogListener 를 구현
                        @Override
                        public void onPositiveClicked() {
                            if ((check_netWork() == true) && (isOnline() == true)) {

                                mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                        PROGRESS,
                                        START, 0), 0);
                                AddFolderTask addFolderTask = new AddFolderTask(mActivity);
                                addFolderTask.execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "네트워크를 연결해 주세요", Toast.LENGTH_SHORT).show();
                            }
                            dialog.cancel();
                        }

                        @Override
                        public void onNegativeClicked() {
                            // Write your code here to invoke NO event
         //                   Toast.makeText(getApplicationContext(), "취소 되었습니다", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });
                    dialog.show();

                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
                    alertDialog.setTitle("새 폴더 확인");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.toad_app_icon_v4);
                    // Setting Dialog Message
                    alertDialog.setMessage("\n     최신 경로 입니다.");

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

*/

/*
    public void delete_folders() {
        //            empty_folder.clear();

        for (int i = 0; i < mMapInfo.size(); i++) {
            if (Integer.parseInt(mMapInfo.get(i).getreserved()) == 0) {
                empty_folder.add(mMapInfo.get(i).getfolder()); //arraylist의 주소를  배열로 저장
            }
        }

        String[] array = empty_folder.toArray(new String[empty_folder.size()]);

        if (array.length > 0) {
            final CustomDialog_2 dialog = new CustomDialog_2(mActivity, array, "빈 폴더 정리", "정리 하시겠습니까?");

            dialog.setDialogListener(new DialogListener_2() {  // MyDialogListener 를 구현
                @Override
                public void onPositiveClicked() {
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(
                            PROGRESS,
                            START, 0), 0);

              //      Log.d(TAG, "custom dialog ok");
                    DeleteFolderTask deleteFolderTask = new DeleteFolderTask(mActivity);
                    deleteFolderTask.execute();
                }

                @Override
                public void onNegativeClicked() {
              //      Log.d(TAG, "custom dialog cancel");
                    empty_folder.clear();
        //            Toast.makeText(MainActivity.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

                }

            });

            dialog.show();

        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("빈 폴더 확인");

            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.toad_app_icon_v4);
            // Setting Dialog Message
            alertDialog.setMessage("\n     빈 폴더가 없습니다.");

            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            // Showing Alert Message
            alertDialog.show();

        }
    }
    */
/*
    PopupMenu.OnMenuItemClickListener listener= new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            // TODO Auto-generated method stub
            Log.d(TAG,"onMenuItemClick: " + item.getItemId());
            switch(item.getItemId()) {//눌러진 MenuItem의 Item Id를 얻어와 식별
                case R.id.menu1:
                    Intent intent = new Intent(getBaseContext(), ListviewActivity.class);
                    intent.putExtra("mMapInfo", mMapInfo);
                    intent.putExtra("mSetting", mSettingInfo);
                    startActivity(intent);
                    break;
                case R.id.menu2:
                    break;
                case R.id.menu3:
                    if (info_btn == 0) {
                        Toast.makeText(mActivity, "주소 알림 켜짐", Toast.LENGTH_LONG).show();
                        info_btn = 1;
                    } else {
                        Toast.makeText(mActivity, "주소 알림 꺼짐", Toast.LENGTH_LONG).show();
                        mtextInfo.setText("");
              //          mcoordInfo.setText("");
                        info_btn = 0;
                        if (coordMarker != null)
                            coordMarker.remove();
                    }
                    break;
                case R.id.menu4:
                    Intent intent_share = new Intent(android.content.Intent.ACTION_SEND);
                    intent_share.setType("image/*");
                    Intent chooser = Intent.createChooser(intent_share, "친구에게 공유하기");
                    startActivity(chooser);
                    break;
                case R.id.menu5:
                    Intent intent_setting = new Intent(getBaseContext(), SettingsActivity.class);
                    startActivity(intent_setting);
                    break;
                case R.id.menu2_1:
                    user_folders = check_child_path();

                    if (user_folders == null) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                        alertDialog.setTitle("새 폴더 확인");
                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.toad_eye_nose);
                        // Setting Dialog Message
                        alertDialog.setMessage("\n경로 정보가 없습니다.");

                        // Setting Positive "Yes" Button
                        alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        // Showing Alert Message
                        alertDialog.show();
                    } else {

                        if (user_folders.size() > 0) {

                            String[] array = user_folders.toArray(new String[user_folders.size()]);
                            final CustomDialog_2 dialog = new CustomDialog_2(mActivity, array, "새 폴더 리스트", "DB에 저장되지 않은 경로가 있습니다. 적용 하시겠습니까?");
                            dialog.setDialogListener(new DialogListener_2() {  // MyDialogListener 를 구현
                                @Override
                                public void onPositiveClicked() {
                                    if ((check_netWork() == true) && (isOnline() == true)) {

                                        mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                                PROGRESS,
                                                START, 0), 0);

                                        AddFolderTask addFolderTask = new AddFolderTask(mActivity);
                                        addFolderTask.execute();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "네트워크를 연결해 주세요", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.cancel();
                                }

                                @Override
                                public void onNegativeClicked() {
                                    // Write your code here to invoke NO event
                                    Toast.makeText(getApplicationContext(), "취소 되었습니다", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });
                            dialog.show();

                        } else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                            alertDialog.setTitle("새 폴더 확인");

                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.drawable.toad_eye_nose);
                            // Setting Dialog Message
                            alertDialog.setMessage("\n최신 경로 입니다.");

                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            // Showing Alert Message
                            alertDialog.show();
                        }
                    }
                    break;
                case R.id.menu2_2:
                    //            empty_folder.clear();

                    for (int i = 0; i < mMapInfo.size(); i++) {
                        if (Integer.parseInt(mMapInfo.get(i).getreserved()) == 0) {
                            empty_folder.add(mMapInfo.get(i).getfolder()); //arraylist의 주소를  배열로 저장
                        }
                    }

                    String[] array = empty_folder.toArray(new String[empty_folder.size()]);

                    if (array.length > 0) {
                        final CustomDialog_2 dialog = new CustomDialog_2(mActivity, array, "빈 폴더 정리", "정리 하시겠습니까?");

                        dialog.setDialogListener(new DialogListener_2() {  // MyDialogListener 를 구현
                            @Override
                            public void onPositiveClicked() {
                                mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                        PROGRESS,
                                        START, 0), 0);

                                Log.d(TAG, "custom dialog ok");
                                DeleteFolderTask deleteFolderTask = new DeleteFolderTask(mActivity);
                                deleteFolderTask.execute();
                            }

                            @Override
                            public void onNegativeClicked() {
                                Log.d(TAG, "custom dialog cancel");
                                empty_folder.clear();
                                Toast.makeText(MainActivity.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

                            }

                        });

                        dialog.show();

                    } else {
                        Toast.makeText(MainActivity.this, "빈 폴더가 없습니다.", Toast.LENGTH_SHORT).show();

                    }
                    break;
            }
            return true;
        }
    };
*/
    private void invalidateMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            invalidateOptionsMenu();
        }
    }
    /**
     * Invoked during init to give the Activity a chance to set up its Menu.
     *
     * @param menu the Menu to which entries may be added
     * @return true
     */

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        Boolean status = false;

        if (info_btn == 1) {
            status = true;
            menu.findItem(R.id.menu3).setChecked(status);
        }
        if (inter_line == 1) {
            status = true;
            menu.findItem(R.id.menu1).setChecked(status);
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu3) {

            invalidateMenu();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        super.onTouchEvent(event);
//        Log.d(TAG, "X:" + event.getX() + " Y:" + event.getY());
        return false;
    }

    public static void onDragTouch(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {
       //     status = START_DRAGGING;
    //        Log.d(TAG, "Action down");
        }
        if (me.getAction() == MotionEvent.ACTION_UP) {
      //      Log.d(TAG, "Action up");
            if (status == START_DRAGGING) {
                if ((((int) x_coord > 100) && ((int) x_coord < maxDisplayWidth - 100))
                        && (((int) y_coord > 350) && ((int) y_coord < maxDisplayHeight - 300))) {
         //           Log.d(TAG, "on the border");
                } else {
          //          Log.d(TAG, "out of the border");
                    if ((int) x_coord <= 100) {
                        x_coord = 100;
                    }
                    if ((int) x_coord >= maxDisplayWidth - 100) {
                        x_coord = maxDisplayWidth - 100;
                    }
                    if ((int) y_coord <= 350) {
                        y_coord = 350;
                    }
                    if ((int) y_coord >= maxDisplayHeight - 400) {
                        y_coord = maxDisplayHeight - 400;
                    }
                }

                try {
        //            Log.d(TAG, "position saving (x:" + x_coord + " y:" + y_coord + ")");
                    MainActivity run = new MainActivity();
                    String[] data = new String[10]; //여기 size
                    data[4] = Float.toString(x_coord);
                    data[5] = Float.toString(y_coord);
                    run.save_arraylist_db(SETTING_DB, 0, data);


            //        run.save_db(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            status = STOP_DRAGGING;

        } else if (me.getAction() == MotionEvent.ACTION_MOVE) {
            if (status == START_DRAGGING) {
                x_coord = me.getRawX();
                y_coord = me.getRawY();

                if ((((int)x_coord > 100) && ((int)x_coord < maxDisplayWidth - 100))
                            && (((int)y_coord > 350) && ((int)y_coord < maxDisplayHeight - 300))) {
                 //   Log.d(TAG, "dragging");
                 //   System.out.println("Dragging");
/*
                    Log.d(TAG,
                            "me.getRawX and Y = " + me.getRawX() + " "
                                    + me.getRawY());
*/
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            200, 200); //이값이 왜 200인지 모르겠음
                    //       drag_button.setPadding((int) me.getRawX(), (int) me.getRawY(), 0,0); //this is not working fine.
                    layoutParams.setMargins((int)x_coord - 100,
                            (int)y_coord - 200, 0, 0);
                    mLinearLayout.removeView(drag_button);
                    mLinearLayout.addView(drag_button, layoutParams);
                    //drag_button.setImageResource(R.drawable.camera_key);
                    drag_button.invalidate();

                } else {
             //       Log.d(TAG, "out of border");
                }
            }
        }
    }

    public int run_activiy(Context context, int control, String path) {
        int ret = -1;
        try {
            switch (control) {
                case 0:
              //      Log.d(TAG, "set_main_path: " + path);
                    String[] data = new String[10]; //여기 size
                    data[1] = path;
                    save_arraylist_db(SETTING_DB, 0, data);
                    break;
                case 1:
                    return get_storage_choice();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static void set_main_path(String path) {

        try {
            MainActivity run = new MainActivity();
            run.run_activiy(mActivity, 0, path);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get_main_path() {
        String return_path = null;
        try {
            return_path = mSettingInfo.get(0).getPath();
            return return_path;
        } catch (Exception e) {
            return return_path;
        }
    }


//마커 설정 관련 func
    public void set_marker_type(int type) {
        if (Integer.parseInt(mSettingInfo.size() > 0 ? mSettingInfo.get(0).getMarker() : Integer.toString(0)) != type) {
            //      marker_choice = type;
            //        Log.d(TAG, "marker_choice:" + type);
            custom_marker = settings_gridActivity.get_data(type); //세팅에서 바로 설정할 경우 settings_gridActivity의 list값을 바로 세팅하여 db에 저장
            try {
                String[] data = new String[10]; //여기 size
                data[6] = Integer.toString(type);
                save_arraylist_db(SETTING_DB, 0, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < mMapInfo.size(); i++) {

                mMapInfo_class = new MapInfo(
                        mMapInfo.get(i).getid(),
                        mMapInfo.get(i).getfolder(),
                        mMapInfo.get(i).getlatitude(),
                        mMapInfo.get(i).getlongitude(),
                        mMapInfo.get(i).getreserved(),
                        mMapInfo.get(i).getFav(),
                        "0",
                        mMapInfo.get(i).getlevel(),
                        mMapInfo.get(i).getStorage());
                mMapInfo.set(i, mMapInfo_class);

                LatLng newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(i).getlatitude()), Double.parseDouble(mMapInfo.get(i).getlongitude()));
                mItem = new PositionItem(newLatLng, mMapInfo.get(i).getfolder(), mMapInfo.get(i).getreserved() + " 데이터", 0, Integer.parseInt(mMapInfo.get(i).getlevel()));
                mPosi.set(i, mItem);
            }

            //     save_db(2);

            // zoom_status = 1;
            marker_changed = 1;
        }
    }


    public void set_storage_info(String path, int storage) {
        sqliteDB_data = dbHelper.getWritableDatabase() ;
      //  Log.d(TAG, "set_storage_info: " + path + " <" + storage + ">");
            for (int i = 0; i < mMapInfo.size(); i++) {

                if (path.equals(mMapInfo.get(i).getfolder())) {

                    mMapInfo_class = new MapInfo(
                            mMapInfo.get(i).getid(),
                            mMapInfo.get(i).getfolder(),
                            mMapInfo.get(i).getlatitude(),
                            mMapInfo.get(i).getlongitude(),
                            mMapInfo.get(i).getreserved(),
                            mMapInfo.get(i).getFav(),
                            mMapInfo.get(i).getdata(),
                            mMapInfo.get(i).getlevel(),
                            Integer.toString(storage));
                    mMapInfo.set(i, mMapInfo_class);

                    LatLng newLatLng = new LatLng(Double.parseDouble(mMapInfo.get(i).getlatitude()), Double.parseDouble(mMapInfo.get(i).getlongitude())); //기존 위치
                    mItem = new PositionItem(newLatLng, mMapInfo.get(i).getfolder(), mMapInfo.get(i).getreserved() + " 데이터", Integer.parseInt(mMapInfo.get(i).getdata()), Integer.parseInt(mMapInfo.get(i).getlevel()));


                    mPosi.set(i, mItem);

                    //DB save
                    String sqlInsert = ContactDBCtrct.SQL_INSERT +
                            " ("
                            + mMapInfo.get(i).getid() + ", " +
                            "'" + mMapInfo.get(i).getfolder() + "', " +
                            "'" + mMapInfo.get(i).getlatitude() + "', " +
                            "'" + mMapInfo.get(i).getlongitude() + "', " +
                            "'" + mMapInfo.get(i).getreserved() + "', " +
                            "'" + mMapInfo.get(i).getFav() + "', " +
                            "'" + mMapInfo.get(i).getdata() + "', " +
                            "'" + mMapInfo.get(i).getlevel() + "', " +
                            "'" + Integer.toString(storage) + "' " +
                            ")";

                    sqliteDB_data.execSQL(sqlInsert);



                }
            }

            marker_changed = 1;
    }

    public void set_level(int level) {
   //     Log.d(TAG, "level_choice:" + level);
        if (level_choice != level) {
            level_choice = level;
        }
    }

    public void set_default_camera(int type) {
           //  Log.d(TAG, "default_cam:" + type);

        if (Integer.parseInt(mSettingInfo.size() > 0 ? mSettingInfo.get(0).getLevel() : Integer.toString(default_cam)) != type) {
            try {
                String[] data = new String[10]; //여기 size
                data[7] = Integer.toString(type);
                save_arraylist_db(SETTING_DB, 0, data);

                Glide.get(mActivity).clearMemory();
                if (Integer.parseInt(mSettingInfo.size() > 0 ? mSettingInfo.get(0).getLevel() : Integer.toString(default_cam)) == 2) {

                    Glide.with(mActivity)
                            //       .load(R.drawable.camera_180901)
                            .load(R.drawable.camera)
                            //       .apply(RequestOptions.bitmapTransform(new CenterCrop()))
                            .into(drag_button);
                } else {
                    Glide.with(mActivity)
                            //       .load(R.drawable.camera_180901)
                            .load(R.drawable.camera_key)
                            //       .apply(RequestOptions.bitmapTransform(new CenterCrop()))
                            .into(drag_button);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void set_default_storage(int type) {
 //       Log.d(TAG, "default_storage:" + type);

        if (Integer.parseInt(mSettingInfo.size() > 0 ? mSettingInfo.get(0).getStorage_status() : Integer.toString(storage_status)) != type) {
            try {
                String[] data = new String[10]; //여기 size
                data[8] = Integer.toString(type);
                save_arraylist_db(SETTING_DB, 0, data);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void set_default_map(int type) {
        //       Log.d(TAG, "default_storage:" + type);

        if (Integer.parseInt(mSettingInfo.size() > 0 ? mSettingInfo.get(0).getMap_status() : Integer.toString(map_status)) != type) {
            try {
                String[] data = new String[10]; //여기 size
                data[9] = Integer.toString(type);
                save_arraylist_db(SETTING_DB, 0, data);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int get_marker_choice() {
        try {
            return Integer.parseInt(mSettingInfo.size() > 0 ? mSettingInfo.get(0).getMarker() : Integer.toString(marker_choice));
        } catch (Exception e) {
            return marker_choice;
        }
    }
    public int get_level_choice() { return level_choice; }
    public int get_saving_status() { return saving_status; }
    public int get_cam_choice() {
        return Integer.parseInt(mSettingInfo.size() > 0 ? mSettingInfo.get(0).getLevel() : Integer.toString(default_cam));
    }

    public int get_storage_choice() {
        return Integer.parseInt(mSettingInfo.size() > 0 ? mSettingInfo.get(0).getStorage_status() : Integer.toString(storage_status));
    }

    public int get_map_choice() {
        return Integer.parseInt(mSettingInfo.size() > 0 ? mSettingInfo.get(0).getMap_status() : Integer.toString(map_status));
    }


    private int back_key_pressed = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
     //   Log.d(TAG, "keyevent " + event.getAction() + " keycode" + keyCode);

        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                break;
            case KeyEvent.KEYCODE_HOME:
                break;
            case KeyEvent.KEYCODE_BACK:
                back_key_pressed = 1;
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
     //   Log.d(TAG, "onBackPressed");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (back_key_pressed == 1) {
                mHandler.sendEmptyMessageDelayed(CLOSE_APP, 0);
            } else {
                super.onBackPressed();
            }
        }
    }

    public Context return_mainActivity_context()
    {
        return mActivity;
    }

    public void set_sdcard_path_header(String data) {
        sdcard_path_header = data;
 //       Log.d(TAG, "sdcard_path_header: " + sdcard_path_header);
    }
    public String get_sdcard_path_header() {
        return sdcard_path_header;
    }

    private String return_mainActivity_external_path() {return External_Path;}

}
