package com.toadstudio.first.toadproject.Cam;

import android.Manifest;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CaptureFailure;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;


import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


import com.toadstudio.first.toadproject.FileController;
import com.toadstudio.first.toadproject.Image.ImagesView;
import com.toadstudio.first.toadproject.MainActivity;
import com.toadstudio.first.toadproject.R;

import com.toadstudio.first.toadproject.RecycleView.AspectRatioImageView;
import com.toadstudio.first.toadproject.RecycleView.DemoSectionType;
import com.toadstudio.first.toadproject.RecycleView.DemoViewType;
//import com.example.rangkastjeong.toadproject.RecycleView.FullMetalAdapter;
//import com.example.rangkastjeong.toadproject.RecycleView.MetalRecyclerViewPager;
import com.toadstudio.first.toadproject.RecycleView.binder.PageBinder;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.magiepooh.recycleritemdecoration.ItemDecorations;

import jp.satorufujiwara.binder.recycler.RecyclerBinder;
import jp.satorufujiwara.binder.recycler.RecyclerBinderAdapter;

/**
 * Created by rangkast.jeong on 2018-02-25.
 */

public class Camera2BasicFragment extends Fragment
        implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback, View.OnKeyListener {

    //default
    String Appdir = "/여행을찍다";
    //test string array
    public static String[] info = {"한국", "서울", "강서구", "위도:xx", "경도:xx", "XXXX.jpg"};
    static List<String> mImg = null;

    private MediaPlayer mp;


 //   private static SeekBar seekBar;

    private static File mediaStorageDir;
    private static String last_file_name;
    private static String basePath;
 //   private static FullMetalAdapter fullMetalAdapter;
 //   private static MetalRecyclerViewPager metalviewPager;
    private static int load_status = 0;

    private RecyclerBinderAdapter<DemoSectionType, DemoViewType> adapter;
    private List<RecyclerBinder<DemoViewType>> demoBinderList;
    private RecyclerView.ItemDecoration decoration;
    private RecyclerView recyclerView;


    private RecyclerBinderAdapter<DemoSectionType, DemoViewType> effect_adapter;
    private static List<RecyclerBinder<DemoViewType>> effect_demoBinderList;
    private RecyclerView.ItemDecoration effect_decoration;
    private RecyclerView effect_recyclerView;
    private static List<String> effect_image_list;

    private static List<String> metalImageList;
    private static List<String> metalList;

 //   public static String[] info_data = new String[info.length];
    Vibrator vibrator;
    public static final int MSG_MAP_DATA = 0;
    public static final int UPDATE_VIEW = 1;
    public static final int UPDATE_NOTIFY = 2;
    public static final int UPDATE_EFFECT_RECYCLER =3;
    public static final int STORE_PICTURES = 4;
    public static final int ERROR_REPORT = 5;

    public String[] address;
    static String picture_name = "default.jpg";

    private static final int ENABLE = 1;
    private static final int DISABLE = 0;
    private static final int NO_NETWORK = 2;
    private static final int EMPTY_FOLDER = 3;
    private static final int SET_PATH = 4;
    private static final int ADD_ADAPTAR = 5;

    private static String camID[];

    final static int BACK_CAM = 0;
    final static int FRONT_CAM = 1;
    final static int WIDE_CAM = 2;

    static int capturing = 0;
    static int camID_length = 0;
    static int camera_status = 2;

    final static int CAM_OPEN = 0;
    final static int CAM_CLOSE = 1;
    final static int CAM_INIT = 2;

    private static int file_cnt = 0;

    static int cam_status = BACK_CAM; //기본 후면 카메라

//    static int viewCreated = 0;

    static int effect_mode = 0;


    static int effect_mode_length = 0;
    private static int effectMode[];
 //   private static int seekbar_position;
    private static int mode_num = 0;
    private static int filter_mode = 0;

    private static FloatingActionsMenu menuMultipleActions;
    private static FloatingActionButton actionA;
    private static FloatingActionButton actionB;
    private static FloatingActionButton actionC;

    /**
     * Conversion from screen rotation to JPEG orientation.
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final String FRAGMENT_DIALOG = "dialog";
    private MapDataHookThread mMapDataHookThread;
    private static boolean mMapDataHookThreadRunning = false;

    private static Activity activity;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    /**
     * Tag for the {@link Log}.
     */
    private static final String TAG = "ToadPrj_Camera2Basic";

    /**
     * Camera state: Showing camera preview.
     */
    private static final int STATE_PREVIEW = 0;

    /**
     * Camera state: Waiting for the focus to be locked.
     */
    private static final int STATE_WAITING_LOCK = 1;

    /**
     * Camera state: Waiting for the exposure to be precapture state.
     */
    private static final int STATE_WAITING_PRECAPTURE = 2;

    /**
     * Camera state: Waiting for the exposure state to be something other than precapture.
     */
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;

    /**
     * Camera state: Picture was taken.
     */
    private static final int STATE_PICTURE_TAKEN = 4;

    /**
     * Max preview width that is guaranteed by Camera2 API
     */
    private static final int MAX_PREVIEW_WIDTH = 1920;

    /**
     * Max preview height that is guaranteed by Camera2 API
     */
    private static final int MAX_PREVIEW_HEIGHT = 1080;

    /**
     * {@link TextureView.SurfaceTextureListener} handles several lifecycle events on a
     * {@link TextureView}.
     */
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }

    };

    private MainActivity mGoogleMapActivity;
    private FileController fileController;
    private ImagesView imagesView;
    /**
     * ID of the current {@link CameraDevice}.
     */
    private String mCameraId;

    /**
     * An {@link AutoFitTextureView} for camera preview.
     */
    private AutoFitTextureView mTextureView;

    /**
     * A {@link CameraCaptureSession } for camera preview.
     */
    private CameraCaptureSession mCaptureSession;

    /**
     * A reference to the opened {@link CameraDevice}.
     */
    private CameraDevice mCameraDevice;

    /**
     * The {@link android.util.Size} of camera preview.
     */
    private Size mPreviewSize;

    /**
     * {@link CameraDevice.StateCallback} is called when {@link CameraDevice} changes its state.
     */
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            // This method is called when the camera is opened.  We start camera preview here.
            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            Activity activity = getActivity();
            if (null != activity) {
                activity.finish();
            }
        }

    };

    private Semaphore mButtonLock = new Semaphore(1);

    /**
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private HandlerThread mBackgroundThread;

    /**
     * A {@link Handler} for running tasks in the background.
     */
    private Handler mBackgroundHandler;

    /**
     * An {@link ImageReader} that handles still image capture.
     */
    private ImageReader mImageReader;

    /**
     * This is the output file for our picture.
     */
    private File mFile;

    /**
     * This a callback object for the {@link ImageReader}. "onImageAvailable" will be called when a
     * still image is ready to be saved.
     */
    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener
            = new ImageReader.OnImageAvailableListener() {

        @Override
        public void onImageAvailable(ImageReader reader) {
            try {
              //  if (image_saver_process == 0)

    //            if (!prev_file.equals(mFile.getPath())) {
                    prev_file = mFile.getPath();
                    mBackgroundHandler.post(new ImageSaver(reader.acquireNextImage(), mFile));
      //          } else {
     //               Log.d(TAG, "saving to storage");
      //          }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };

    /**
     * {@link CaptureRequest.Builder} for the camera preview
     */
    private CaptureRequest.Builder mPreviewRequestBuilder;

    /**
     * {@link CaptureRequest} generated by {@link #mPreviewRequestBuilder}
     */
    private CaptureRequest mPreviewRequest;

    /**
     * The current state of camera state for taking pictures.
     *
     * @see #mCaptureCallback
     */
    private int mState = STATE_PREVIEW;

    /**
     * A {@link Semaphore} to prevent the app from exiting before closing the camera.
     */
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);


    /**
     * Whether the current camera device supports Flash or not.
     */
    private boolean mFlashSupported;

    /**
     * Orientation of the camera sensor
     */
    private int mSensorOrientation;

    /**
     * A {@link CameraCaptureSession.CaptureCallback} that handles events related to JPEG capture.
     */
    private CameraCaptureSession.CaptureCallback mCaptureCallback
            = new CameraCaptureSession.CaptureCallback() {

        private void process(CaptureResult result) {
            switch (mState) {
                case STATE_PREVIEW: {
            //        Log.d(TAG, "process: state_preview");
                    // We have nothing to do when the camera preview is working normally.
                    break;
                }
                case STATE_WAITING_LOCK: {
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
              //      Log.d(TAG, "process: state_waiting_lock: " + afState);
                    if (afState == null) {
                        captureStillPicture();
                    } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState ||
                            CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
                        // CONTROL_AE_STATE can be null on some devices
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (aeState == null ||
                                aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            mState = STATE_PICTURE_TAKEN;
                            captureStillPicture();
                        } else {
                            runPrecaptureSequence();
                        }
                    } else if (CaptureResult.CONTROL_AF_STATE_INACTIVE == afState) {
                        //todo ???
                   //     captureStillPicture();
                    }
                    break;
                }
                case STATE_WAITING_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
             //       Log.d(TAG, "process: state_waiting_precapture" + aeState);
                    if (aeState == null ||
                            aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED ||
                            aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                        mState = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                }
                case STATE_WAITING_NON_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
              //      Log.d(TAG, "process: state_waiting_non_precapture" + aeState);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        mState = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
                }
            }
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                        @NonNull CaptureRequest request,
                                        @NonNull CaptureResult partialResult) {
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
            process(result);
        }

    };

    /**
     * Shows a {@link Toast} on the UI thread.
     *
     * @param text The message to show
     */
    private void showToast(final String text) {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
               //     Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Given {@code choices} of {@code Size}s supported by a camera, choose the smallest one that
     * is at least as large as the respective texture view size, and that is at most as large as the
     * respective max size, and whose aspect ratio matches with the specified value. If such size
     * doesn't exist, choose the largest one that is at most as large as the respective max size,
     * and whose aspect ratio matches with the specified value.
     *
     * @param choices           The list of sizes that the camera supports for the intended output
     *                          class
     * @param textureViewWidth  The width of the texture view relative to sensor coordinate
     * @param textureViewHeight The height of the texture view relative to sensor coordinate
     * @param maxWidth          The maximum width that can be chosen
     * @param maxHeight         The maximum height that can be chosen
     * @param aspectRatio       The aspect ratio
     * @return The optimal {@code Size}, or an arbitrary one if none were big enough
     */
    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                                          int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    public static Camera2BasicFragment newInstance() {
        return new Camera2BasicFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera2_basic, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

   //     Log.d(TAG, "onViewCreated");

        activity = getActivity();

        view.findViewById(R.id.picture).setOnClickListener(this);
        view.findViewById(R.id.info).setOnClickListener(this);
        view.findViewById(R.id.wide).setOnClickListener(this);
        mTextureView = (AutoFitTextureView) view.findViewById(R.id.texture);


/*
        metalviewPager = (MetalRecyclerViewPager) view.findViewById(R.id.fragment_camera2_viewPager);

        if (metalviewPager != null) {
            mImg =  new ArrayList<>(); //한번만 호출, list 준비
            metalImageList = new ArrayList<>();
            metalList = new ArrayList<>();
     //       metalviewPager.setAdapter(fullMetalAdapter);
        }
*/

        // normal
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_camera2_viewPager);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        if (recyclerView != null) {
            mImg = new ArrayList<>(); //한번만 호출, list 준비
            metalImageList = new ArrayList<>();
            metalList = new ArrayList<>();

            adapter = new RecyclerBinderAdapter<>();
            demoBinderList = new ArrayList<>();

            decoration = ItemDecorations.horizontal(getContext())
                    .first(R.drawable.shape_decoration_green_w_8)
                    .type(DemoViewType.PAGE.ordinal(), R.drawable.shape_decoration_red_w_8)
                    .last(R.drawable.shape_decoration_flush_orange_w_8)
                    .create();
        }

        /*
        effect_recyclerView = (RecyclerView) view.findViewById(R.id.effect_recycler);
        effect_recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        if (effect_recyclerView != null) {

            effect_image_list = new ArrayList<>();

            for (int i = 1; i < 5; i++) {
                String resName = "@drawable/toad_app_icon_v" + i;

                int resID = getResources().getIdentifier(resName, "drawable", getActivity().getPackageName());

                effect_image_list.add(Integer.toString(resID));

                Log.d(TAG, "get image: " + Integer.toString(resID) + " effect Image list size: " + effect_image_list.size());

            }


            effect_adapter = new RecyclerBinderAdapter<>();
            effect_demoBinderList = new ArrayList<>();

            effect_decoration = ItemDecorations.horizontal(getContext())
                    .first(R.drawable.shape_decoration_green_w_8)
                    .type(DemoViewType.PAGE.ordinal(), R.drawable.shape_decoration_red_w_8)
                    .last(R.drawable.shape_decoration_flush_orange_w_8)
                    .create();

            for (int i = 0; i < effect_image_list.size(); i++) {
                effect_demoBinderList.add(new EffectBinder(getActivity(), effect_image_list.get(i)));
            }

            for (RecyclerBinder<DemoViewType> binder : effect_demoBinderList) {
                effect_adapter.add(DemoSectionType.ITEM, binder);
            }

            if (effect_recyclerView != null) {
                effect_recyclerView.setAdapter(effect_adapter);
                effect_recyclerView.addItemDecoration(effect_decoration);
            }


        }
        */
/*
        seekBar = (SeekBar)view.findViewById(R.id.seek_bar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "seekbar: " + progress);
                seekbar_position = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "seekbar stop tacking");
                if (mState == STATE_PREVIEW)
                    set_effect_mode(seekbar_position + 1);
            }
        });
*/

/*
        seekBar.setVisibility(View.GONE);
        menuMultipleActions = (FloatingActionsMenu) view.findViewById(R.id.multiple_actions_camera);
        menuMultipleActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "menuMultipleActions click");
            }
        });

        actionC = (FloatingActionButton) view.findViewById(R.id.action_3);
        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionA.setVisibility(actionA.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                if ((actionA.getVisibility() == View.VISIBLE) || (actionB.getVisibility() == View.VISIBLE)) {
                    seekBar.setVisibility(View.VISIBLE);
                    filter_mode = 1;
                    Log.d(TAG, "cam filter enabled");
                } else {
                    Log.d(TAG, "cam filter disabled");
                    seekBar.setVisibility(View.GONE);
                    filter_mode = 0;
                    change_camera_mode(2);
                }
            }
        });

        actionB = (FloatingActionButton) view.findViewById(R.id.action_2);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mState == STATE_PREVIEW)
                    change_camera_mode(1);
            }
        });
        actionB.setVisibility(View.GONE);

        actionA = (FloatingActionButton) view.findViewById(R.id.action_1);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mState == STATE_PREVIEW)
                     change_camera_mode(0);
            }
        });
        actionA.setVisibility(View.GONE);
*/
 //       viewCreated = 1;
        load_status = 0;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mGoogleMapActivity = new MainActivity();
        fileController = new FileController();
        imagesView = new ImagesView();

        setDirectory(DISABLE, Appdir, info, 3); //default, level 의미 없음

    }

    public void shutterSound() {
    //    Log.d(TAG, "shutterSound");
        AudioManager audioManager = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);

  //      if (volume != 0) {
            if (mp == null) {
                try {
                    mediaPlayer_create();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (mp != null) {
                try {
                    if (mp.isPlaying()) {
                        mp.stop();
                        mp.release();
                        mediaPlayer_create();
                    }

                    mp.start();
           //         Log.d(TAG, "play shutter sound");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
   //     } else {
  //          Log.d(TAG, "volume 0");
 //       }
    }

    public void mediaPlayer_create() {
        try {
       //     Log.d(TAG, "mediaPlayer_create");
            mp = MediaPlayer.create(getContext(), Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    //code
             //       mp.release();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startBackgroundThread();
    //    Log.d(TAG, "onResume");
        // When the screen is turned off and turned back on, the SurfaceTexture is already
        // available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
        // a camera and start preview from here (otherwise, we wait until the surface is ready in
        // the SurfaceTextureListener).
        if (mTextureView.isAvailable()) {
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    public boolean setDirectory(int control, String Appdir, String[] name, int level) {

     //   Log.d(TAG, "setDirectory " + control);

        try {
            if (control == ENABLE) {
                StringBuilder str = new StringBuilder("");
                for (int i = 0; i < level; i++) {
                    str.append(name[i]);
                    if (i < level - 1)
                        str.append("/");
                }

                if (mGoogleMapActivity.get_storage_choice() == imagesView.INTERNAL_STORAGE) {
            //        Log.d(TAG,"mkdir path: " + str.toString());

                    mediaStorageDir = new File(Environment.getExternalStorageDirectory() + Appdir, str.toString());
                    if (!mediaStorageDir.exists()) {
                        if (!mediaStorageDir.mkdirs()) {
                 //          Log.d(TAG, "failed to create directory");
                            return false;
                        }
                    }
                    mFile = new File(mediaStorageDir, name[5]);
                } else {
                    if (fileController.folder_checker(mGoogleMapActivity.return_mainActivity_context(), mGoogleMapActivity.get_main_path(), Appdir + File.separator + str.toString(), fileController.MAKE_DIR) != null) {
                        //        Toast.makeText(getActivity(), Appdir + "폴더가 생성되었습니다.", Toast.LENGTH_SHORT).show();
                        mediaStorageDir= new File(mGoogleMapActivity.get_sdcard_path_header() + Appdir, str.toString());
                        mFile = new File(Appdir + File.separator + str.toString(), name[5]);
                        //todo
                    }
                }

                load_status = 1;


            } else if (control == DISABLE) {
                if (mGoogleMapActivity.get_storage_choice() == imagesView.INTERNAL_STORAGE) {
                    mediaStorageDir = new File(Environment.getExternalStorageDirectory() + Appdir, "/");
                    if (!mediaStorageDir.exists()) {
                        if (!mediaStorageDir.mkdirs()) {
                            //          Log.d(TAG, "failed to create directory");
                            return false;
                        }
                    }
                    mFile = new File(mediaStorageDir, "readme.txt");
                } else {
                    if (fileController.folder_checker(mGoogleMapActivity.return_mainActivity_context(), mGoogleMapActivity.get_main_path(), Appdir, fileController.MAKE_DIR) != null) {
                //        Toast.makeText(getActivity(), Appdir + "폴더가 생성되었습니다.", Toast.LENGTH_SHORT).show();
                        //todo
                        mFile = new File(Appdir, "readme.txt");
                    }
                }
            } else if (control == NO_NETWORK) {

                if (mGoogleMapActivity.get_storage_choice() == imagesView.INTERNAL_STORAGE) {
                    mediaStorageDir = new File(Environment.getExternalStorageDirectory() + Appdir, name[0]);

                    if (!mediaStorageDir.exists()) {
                        if (!mediaStorageDir.mkdirs()) {
                            //        Log.d(TAG, "failed to create directory");
                            return false;
                        }
                    }
                    mFile = new File(mediaStorageDir, name[5]);
                } else {
                    if (fileController.folder_checker(mGoogleMapActivity.return_mainActivity_context(),mGoogleMapActivity.get_main_path(), Appdir + File.separator + name[0], fileController.MAKE_DIR) != null) {
                        //        Toast.makeText(getActivity(), Appdir + "폴더가 생성되었습니다.", Toast.LENGTH_SHORT).show();
                        mFile = new File(Appdir + File.separator + name[0], name[5]);
                        //todo
                    }
                }

            } else if (control == EMPTY_FOLDER) { //3
                mGoogleMapActivity = new MainActivity();
                imagesView = new ImagesView();
                fileController = new FileController();
                if (mGoogleMapActivity.get_storage_choice() == imagesView.INTERNAL_STORAGE) {
                    mediaStorageDir = new File(Environment.getExternalStorageDirectory() + Appdir, name[0]);
                    if (!mediaStorageDir.exists()) {
                        if (!mediaStorageDir.mkdirs()) {
                    //        Log.d(TAG, "failed to create directory");
                            return false;
                        }
                    }
                } else {
                    try {
                        if (fileController.folder_checker(mGoogleMapActivity.return_mainActivity_context(), mGoogleMapActivity.get_main_path(),Appdir + File.separator + name[0], fileController.MAKE_DIR) != null) {
                            return true;
                        } else {
                            return false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            } else if (control == SET_PATH) {
                StringBuilder str = new StringBuilder("");
                for (int i = 0; i < level; i++) {
                    str.append(name[i]);
                    if (i < level - 1)
                        str.append("/");
                }

                if (mGoogleMapActivity.get_storage_choice() == imagesView.INTERNAL_STORAGE) {
                    mediaStorageDir = new File(Environment.getExternalStorageDirectory() + Appdir, str.toString());
                } else {
                    mediaStorageDir= new File(mGoogleMapActivity.get_sdcard_path_header() + Appdir, str.toString());
                }
                if (mediaStorageDir.exists() && /*(viewCreated == 1) &&*/ (load_status == 0)) {
         //           Log.d(TAG, "loading img start");
                    basePath = mediaStorageDir.getPath();
                    String img[] = {};

                    File file = new File(basePath);
                    img = file.list();

                    mImg.clear();
                    for (int i = 0; i < img.length; i++) {
                        if (img[i].contains("."))
                            mImg.add(img[i]);
                    }

                    //      Log.d(TAG, "mImg length " + mImg.size());
                    Collections.sort(mImg); //sort
                    Collections.reverse(mImg); //거꾸로

                    if (mImg.size() > 0) { //한번만

                        Activity activity = getActivity();
                        DisplayMetrics dm = new DisplayMetrics();
                        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

                        metalList.clear();
                        metalImageList.clear();
                        for (int i = 0; i < mImg.size(); i++) {
                            if (mImg.get(i).contains(".")) {
                                metalList.add(mImg.get(i));
                                metalImageList.add(basePath + File.separator + mImg.get(i));
                            }
                        }

                        //             fullMetalAdapter = new FullMetalAdapter(getActivity(), dm, metalList, metalImageList);
                        //            metalviewPager.setAdapter(fullMetalAdapter);

                        //todo
                        //          Log.d(TAG, "RecyclerBinder");
                        if (metalImageList != null) {
                            demoBinderList.clear();

                            for (int i = 0; i < metalImageList.size(); i++) {
                                demoBinderList.add(new PageBinder(activity, metalImageList.get(i)));
                            }

                            adapter = new RecyclerBinderAdapter<>();
                            for (RecyclerBinder<DemoViewType> binder : demoBinderList) {
                                adapter.add(DemoSectionType.ITEM, binder);
                            }

                            recyclerView.setAdapter(adapter);

                            if (recyclerView != null) {
                                recyclerView.setAdapter(adapter);
                                //       if (viewCreated == 1)
                                //             recyclerView.addItemDecoration(decoration);
                            }

                            UITask uiTask = new UITask(getContext(), 5, 1);
                            uiTask.execute();

                        }

                        load_status = 1;

                    }

                } else {
                 //            Log.d(TAG, "path: " + str.toString() + " is not exist" + " 1." + mediaStorageDir.exists() + " 2." + load_status);
                }

            } else if (control == ADD_ADAPTAR) {

                Activity activity = getActivity();
                DisplayMetrics dm = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(dm);



                int try_connect = 0;
                int firset_mkdir = 0;

                try {
                    mImg.get(mImg.size() - 1).equals(info[5]);
                //    Log.d(TAG, "mImg:" + mImg.get(mImg.size() - 1) + " info:" + info[5]);
                    try_connect = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    if (mImg.size() == 0) {
                        try_connect = 1;
                        firset_mkdir = 1;
                    }
                }
      //          Log.d(TAG, "?? " + mImg.size() + " " + try_connect);

                if (try_connect == 1) { //마지막 파일명이 다를 경우만 저장
                    try {
                        if (firset_mkdir == 1) {
                            basePath = mediaStorageDir.getPath();
                        }
                        Thread.sleep(10); //여기 왜?

                        mImg.add(0, info[5]);
                        metalList.add(0, info[5]);
                        metalImageList.add(0, basePath + File.separator + info[5]);
             //           Log.d(TAG, basePath + File.separator + info[5]);
                        //        fullMetalAdapter = new FullMetalAdapter(getActivity(), dm, metalList, metalImageList);

                        demoBinderList.clear();



                        if (metalImageList != null) {

                            for (int i = 0; i < metalImageList.size(); i++) {
                                demoBinderList.add(new PageBinder(activity, metalImageList.get(i)));
                            }

                            adapter = new RecyclerBinderAdapter<>();
                            for (RecyclerBinder<DemoViewType> binder : demoBinderList) {
                                adapter.add(DemoSectionType.ITEM, binder);
                            }
/*
                            if (firset_mkdir == 1) {
                                recyclerView.setAdapter(adapter);

                                if (recyclerView != null) {
                                    recyclerView.setAdapter(adapter);
                                    //       if (viewCreated == 1)
                                    //             recyclerView.addItemDecoration(decoration);
                                }
                            }
                            */
                            UITask uiTask = new UITask(getContext(), 5, 1);
                            uiTask.execute();
                        }
                        //             mHandler.sendEmptyMessageDelayed(UPDATE_VIEW, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        shutterSound();
                        //           Log.d(TAG, "loaded");
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private class UITask extends AsyncTask {
        Context mContext;
        int mCount = 0;
        int mControl = 0;
        public UITask(Context context, int count, int control) {
   //         Log.d(TAG, "UITask start");
            mContext = context;
            mCount = count;
            mControl = control;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            switch (mControl) {
                case 1:
                    for (int i = 0; i < mCount; i++) {
                        mHandler.sendEmptyMessageDelayed(UPDATE_VIEW, 0);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 2:

                    break;
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }

    @Override
    public void onPause() {
  //      Log.d(TAG, "onPause");
        closeCamera();
        stopBackgroundThread();

        super.onPause();
    }

    @Override
    public void onStop() {
   //     Log.d(TAG, "onStop");
        load_status = 0;
    //    viewCreated = 0;
        capturing = 0;
        prev_addr = "null";
        mImg.clear();
        metalList.clear();
        metalImageList.clear();
        prev_file = "null";
        file_cnt = 0;
        super.onStop();
    }

    public List<String> return_mImage() {
        return mImg;
    }

    private void requestCameraPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            new ConfirmationDialog().show(getChildFragmentManager(), FRAGMENT_DIALOG);
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ErrorDialog.newInstance(getString(R.string.request_permission))
                        .show(getChildFragmentManager(), FRAGMENT_DIALOG);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Sets up member variables related to camera.
     *
     * @param width  The width of available size for camera preview
     * @param height The height of available size for camera preview
     */
    @SuppressWarnings("SuspiciousNameCombination")
    private CameraCharacteristics characteristics;
    private void setUpCameraOutputs(int width, int height, int cam_set) {
        Activity activity = getActivity();
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);

        camera_status = CAM_INIT;

        try {
            for (String cameraId : manager.getCameraIdList()) {
                characteristics
                        = manager.getCameraCharacteristics(cameraId);

                // We don't use a front facing camera in this sample.
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);

                if (filter_mode == 1) {
                    switch (mode_num) {
                        case 0:
                            effect_mode_length = characteristics.get(CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES).length;
                            if (effect_mode_length > 0) {
                                effectMode = new int[effect_mode_length];
                                effectMode = characteristics.get(CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES);
                            }
                            break;
                        case 1:
                            effect_mode_length = characteristics.get(CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS).length;
                            if (effect_mode_length > 0) {
                                effectMode = new int[effect_mode_length];
                                effectMode = characteristics.get(CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS);
                            }
                            break;
                    }

      //              Log.d(TAG, "effect mode length:" + effect_mode_length);
   //                 seekBar.setMax(effect_mode_length - 2);
                } else {
 //                   seekBar.setMax(0);
                }

                camID_length = manager.getCameraIdList().length;
                camID = new String[camID_length];
                camID = manager.getCameraIdList();
/*
                for (int i = 0; i < camID_length; i++) {
                    Log.d(TAG, "CameraID" + camID[i]);
                }
*/
                 if (cam_set == BACK_CAM) {
             //        Log.d(TAG, "Back_cam selected" + cameraId);
                     if (facing != null && (cameraId != camID[0])) {
                         continue;
                     }

                } else if (cam_set == FRONT_CAM) {
                   //      Log.d(TAG, "Front_cam selected" + cameraId);
                         if (facing != null && (cameraId != camID[1])) {
                             continue;
                         }

                } else if (camID_length > 2) {
                    if (cam_set == WIDE_CAM) {
                    //    Log.d(TAG, "Wide_cam selected");
                        if (facing != null && (cameraId != camID[2])) {
                            continue;
                        }
                    }
                }

                StreamConfigurationMap map = characteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }

                // For still image captures, we use the largest available size.
                Size largest = Collections.max(
                        Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                        new CompareSizesByArea());
                mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(),
                        ImageFormat.JPEG, /*maxImages*/2);
                mImageReader.setOnImageAvailableListener(
                        mOnImageAvailableListener, mBackgroundHandler);

                // Find out if we need to swap dimension to get the preview size relative to sensor
                // coordinate.
                int displayRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
                //noinspection ConstantConditions
                mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                boolean swappedDimensions = false;
                switch (displayRotation) {
                    case Surface.ROTATION_0:
                    case Surface.ROTATION_180:
                        if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                            swappedDimensions = true;
                        }
                        break;
                    case Surface.ROTATION_90:
                    case Surface.ROTATION_270:
                        if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                            swappedDimensions = true;
                        }
                        break;
                    default:
                        break;
                        //Log.d(TAG, "Display rotation is invalid: " + displayRotation);
                }

                Point displaySize = new Point();
                activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
                int rotatedPreviewWidth = width;
                int rotatedPreviewHeight = height;
                int maxPreviewWidth = displaySize.x;
                int maxPreviewHeight = displaySize.y;

                if (swappedDimensions) {
                    rotatedPreviewWidth = height;
                    rotatedPreviewHeight = width;
                    maxPreviewWidth = displaySize.y;
                    maxPreviewHeight = displaySize.x;
                }

                if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                    maxPreviewWidth = MAX_PREVIEW_WIDTH;
                }

                if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                    maxPreviewHeight = MAX_PREVIEW_HEIGHT;
                }

                // Danger, W.R.! Attempting to use too large a preview size could  exceed the camera
                // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
                // garbage capture data.
                mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                        rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                        maxPreviewHeight, largest);

                // We fit the aspect ratio of TextureView to the size of preview we picked.
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    mTextureView.setAspectRatio(
                            mPreviewSize.getWidth(), mPreviewSize.getHeight());
                } else {
                    mTextureView.setAspectRatio(
                            mPreviewSize.getHeight(), mPreviewSize.getWidth());
                }

                // Check if the flash is supported.
                Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                mFlashSupported = available == null ? false : available;

                mCameraId = cameraId;
         //       Log.d(TAG, "CamSelected: " + mCameraId);
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.
            ErrorDialog.newInstance(getString(R.string.camera_error))
                    .show(getChildFragmentManager(), FRAGMENT_DIALOG);
        }
    }

    /**
     * Opens the camera specified by {@link Camera2BasicFragment#mCameraId}.
     */
    private void openCamera(int width, int height) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
            return;
        }
     //   Log.d(TAG, "openCamera: X " + width + " Y " + height);
        setUpCameraOutputs(width, height, cam_status);  //기본 후면 카메라
        configureTransform(width, height);
        Activity activity = getActivity();
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);

        if (mCameraId == null) {
      //      Log.d(TAG, "mCameraID is null");
            stopBackgroundThread();
            Toast.makeText(getActivity(), "카메라가 동작하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            manager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
            camera_status = CAM_OPEN;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }

    /**
     * Closes the current {@link CameraDevice}.
     */
    private void closeCamera() {
        try {
      //      Log.d(TAG, "closeCamera");
            mCameraOpenCloseLock.acquire();
            if (null != mCaptureSession) {
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mImageReader) {
                mImageReader.close();
                mImageReader = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            mCameraOpenCloseLock.release();
            camera_status = CAM_CLOSE;
        }
    }

    /**
     * Starts a background thread and its {@link Handler}.
     */
    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());

        mMapDataHookThread = new MapDataHookThread();
        mMapDataHookThread.start();
    }

    /**
     * Stops the background thread and its {@link Handler}.
     */
    private void stopBackgroundThread() {

        if (mMapDataHookThread != null) {
            mMapDataHookThread.interrupt();
            mMapDataHookThread = null;
            mMapDataHookThreadRunning = false;
        }

        if (mBackgroundThread != null) {
            mBackgroundThread.quitSafely();
            try {
                mBackgroundThread.join();
                mBackgroundThread = null;
                mBackgroundHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates a new {@link CameraCaptureSession} for camera preview.
     */
    private void createCameraPreviewSession() {
        try {
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;

            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            // This is the output Surface we need to start preview.
            Surface surface = new Surface(texture);

            // We set up a CaptureRequest.Builder with the output Surface.
            mPreviewRequestBuilder
                    = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);

            // Here, we create a CameraCaptureSession for camera preview.
            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            // The camera is already closed
                            if (null == mCameraDevice) {
               //                Log.d(TAG, "mCameraDevice is null");
                                return;
                            }

                            // When the session is ready, we start displaying the preview.
                            mCaptureSession = cameraCaptureSession;
                            try {
                            //    Log.d(TAG, "createCameraPreviewSesstion-onConfigured");
                                /*
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_USE_SCENE_MODE);
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, CaptureRequest.CONTROL_SCENE_MODE_NIGHT);
*/
                                if (filter_mode == 1) {
                                    switch (mode_num) {
                                        case 0:
                                        //   Log.d(TAG, "preview wb mode" + effect_mode);
                                              mPreviewRequestBuilder.set(CaptureRequest.CONTROL_EFFECT_MODE, CaptureRequest.CONTROL_EFFECT_MODE_OFF);
                                              mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AWB_MODE, effect_mode);
                                            break;
                                        case 1:
                                         //   Log.d(TAG, "preview effect mode" + effect_mode);
                                            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AWB_MODE, CaptureRequest.CONTROL_AWB_MODE_AUTO);
                                            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_EFFECT_MODE, effect_mode);
                                            break;
                                    }
                                } else {
                                    mPreviewRequestBuilder.set(CaptureRequest.CONTROL_EFFECT_MODE, CaptureRequest.CONTROL_EFFECT_MODE_OFF);
                               //     mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AWB_MODE, CaptureRequest.CONTROL_AWB_MODE_AUTO);
                                }
                                // Auto focus should be continuous for camera preview.
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);                                // Flash is automatically enabled when necessary.
                       //         setAutoFlash(mPreviewRequestBuilder);

                                // Finally, we start displaying the camera preview.
                                mPreviewRequest = mPreviewRequestBuilder.build();
                                mCaptureSession.setRepeatingRequest(mPreviewRequest,
                                        mCaptureCallback, mBackgroundHandler);
                            } catch (CameraAccessException | NullPointerException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(
                                @NonNull CameraCaptureSession cameraCaptureSession) {
                            showToast("Failed");
                        }
                    }, null
            );
        } catch (CameraAccessException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Configures the necessary {@link android.graphics.Matrix} transformation to `mTextureView`.
     * This method should be called after the camera preview size is determined in
     * setUpCameraOutputs and also the size of `mTextureView` is fixed.
     *
     * @param viewWidth  The width of `mTextureView`
     * @param viewHeight The height of `mTextureView`
     */
    private void configureTransform(int viewWidth, int viewHeight) {
  //      Log.d(TAG, "configureTransform");
        Activity activity = getActivity();
        if (null == mTextureView || null == mPreviewSize || null == activity) {
            return;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }

    /**
     * Initiate a still image capture.
     */
    private void takePicture() {
        lockFocus();
    }

    /**
     * Lock the focus as the first step for a still image capture.
     */
    private void lockFocus() {
        try {
     //       Log.d(TAG, "lockFocus");
            // This is how to tell the camera to lock focus.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the lock.
            mState = STATE_WAITING_LOCK;
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);

        } catch (CameraAccessException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Run the precapture sequence for capturing a still image. This method should be called when
     * we get a response in {@link #mCaptureCallback} from {@link #lockFocus()}.
     */
    private void runPrecaptureSequence() {
        try {
            // This is how to tell the camera to trigger.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the precapture sequence to be set.
            mState = STATE_WAITING_PRECAPTURE;
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Capture a still picture. This method should be called when we get a response in
     * {@link #mCaptureCallback} from both {@link #lockFocus()}.
     */
    private void captureStillPicture() {
        try {
    //        Log.d(TAG, "captureStillPicture");
            final Activity activity = getActivity();
            if (null == activity || null == mCameraDevice) {
                return;
            }
            if (capturing == 0) {
                capturing = 1;
            } else {
    //            Log.d(TAG, "captureStillPicture: returned");
                return;
            }

            // This is the CaptureRequest.Builder that we use to take a picture.
            final CaptureRequest.Builder captureBuilder =
                    mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());

            if (filter_mode == 1) {
                switch (mode_num) {
                    case 0:
                 //       Log.d(TAG, "capture wb mode " + effect_mode);
                        captureBuilder.set(CaptureRequest.CONTROL_EFFECT_MODE, CaptureRequest.CONTROL_EFFECT_MODE_OFF);
                        captureBuilder.set(CaptureRequest.CONTROL_AWB_MODE, effect_mode);
                        break;
                    case 1:
                   //     Log.d(TAG, "capture effect mode " + effect_mode);
                        captureBuilder.set(CaptureRequest.CONTROL_AWB_MODE, CaptureRequest.CONTROL_AWB_MODE_AUTO);
                        captureBuilder.set(CaptureRequest.CONTROL_EFFECT_MODE, effect_mode);
                        break;
                }
            } else {
                captureBuilder.set(CaptureRequest.CONTROL_EFFECT_MODE, CaptureRequest.CONTROL_EFFECT_MODE_OFF);
                captureBuilder.set(CaptureRequest.CONTROL_AWB_MODE, CaptureRequest.CONTROL_AWB_MODE_AUTO);
            }

            // Use the same AE and AF modes as the preview.
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
//            setAutoFlash(captureBuilder);

            // Orientation
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

    //       Log.d(TAG, "rotation: " + rotation + " / " + getOrientation(rotation));

            CameraCaptureSession.CaptureCallback CaptureCallback
                    = new CameraCaptureSession.CaptureCallback() {

                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                               @NonNull CaptureRequest request,
                                               @NonNull TotalCaptureResult result) {
          //          showToast("Saved: " + mFile);
        //            Log.d(TAG, "CaptureCallback" +  mFile.toString());

                    unlockFocus();
                }

                @Override
                public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
         //           Log.d(TAG, "onCaptureFailed: " + failure.getReason());
                    super.onCaptureFailed(session, request, failure);
                }

                @Override
                public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
            //        Log.d(TAG, "onCaptureStarted");
                    super.onCaptureStarted(session, request, timestamp, frameNumber);
                }

                @Override
                public void onCaptureSequenceAborted(@NonNull CameraCaptureSession session, int sequenceId) {
           //        Log.d(TAG, "onCaptureSequenceAborted");
                    super.onCaptureSequenceAborted(session, sequenceId);
                }

                @Override
                public void onCaptureBufferLost(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull Surface target, long frameNumber) {
             //       Log.d(TAG,"onCaptureBufferLost");
                    super.onCaptureBufferLost(session, request, target, frameNumber);
                }
            };

            //todo
            mCaptureSession.stopRepeating();  //??
            mCaptureSession.abortCaptures();  //??
            mCaptureSession.capture(captureBuilder.build(), CaptureCallback, null);
        } catch (CameraAccessException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the JPEG orientation from the specified screen rotation.
     *
     * @param rotation The screen rotation.
     * @return The JPEG orientation (one of 0, 90, 270, and 360)
     */
    private int getOrientation(int rotation) {
   //    Log.d(TAG, "getOrientation");
        // Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus 5X)
        // We have to take that into account and rotate JPEG properly.
        // For devices with orientation of 90, we simply return our mapping from ORIENTATIONS.
        // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
        return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
    }

    /**
     * Unlock the focus. This method should be called when still image capture sequence is
     * finished.
     */
    private void unlockFocus() {
        try {
       //     Log.d(TAG, "unlockFocus");
            // Reset the auto-focus trigger
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
   //         setAutoFlash(mPreviewRequestBuilder);
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);
            // After this, the camera will go back to the normal state of preview.
            mState = STATE_PREVIEW;
            mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void picture_button_func() {

            int err_state = 0;
            boolean mkdir_status = true;
            // 현재시간을 msec 으로 구한다.
            long now = System.currentTimeMillis();
            // 현재시간을 date 변수에 저장한다.
            Date date = new Date(now);
            // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMdd_HHmmss");
            // nowDate 변수에 값을 저장한다.
            String formatDate = sdfNow.format(date);

            StringBuilder strPicture = new StringBuilder("");
            strPicture.append(formatDate);
            strPicture.append(".jpg");
            info[5] = strPicture.toString();


            if (info[3].contains("xx") || info[4].contains("xx")) {
                mkdir_status = false;
                err_state = 1;
            } else {

                if (mGoogleMapActivity.string_contains_checker(info[0]) == false) { //error의 첫번째 배열 지오코더,잘못된,주소
                    mkdir_status = setDirectory(ENABLE, Appdir, info, mGoogleMapActivity.get_level_choice());
                } else {
                    mkdir_status = setDirectory(NO_NETWORK, Appdir, info, mGoogleMapActivity.get_level_choice());
                }

            }
    //        Log.d(TAG, "picture_button_func: " + mkdir_status);

            if (mkdir_status == true) {
                takePicture();
            } else {
                if (err_state == 1) {
                    Toast.makeText(getActivity(), "위치 정보가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "사진이 저장되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }
    }

    public void info_button_func(int con) {

            Activity activity = getActivity();
            if (null != activity) {

                //close camera
                closeCamera();
                stopBackgroundThread();

                if (con == 0) {
                    if (camID_length > 1) {
                        if (cam_status == FRONT_CAM)
                            cam_status = BACK_CAM;
                        else
                            cam_status = FRONT_CAM;
                    } else {
                        Toast.makeText(getActivity(), "지원하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else if (con == 1) {
                    if (cam_status == WIDE_CAM)
                        cam_status = BACK_CAM;
                    else
                        cam_status = WIDE_CAM;
                }

                startBackgroundThread();
                // When the screen is turned off and turned back on, the SurfaceTexture is already
                // available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
                // a camera and start preview from here (otherwise, we wait until the surface is ready in
                // the SurfaceTextureListener).
                if (mTextureView.isAvailable()) {
               //     Log.d(TAG, "mTextureView is available");
                    openCamera(mTextureView.getWidth(), mTextureView.getHeight());
                } else {
              //      Log.d(TAG, "setSurfaceTextureListener");
                    mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
                }
            }
    }

    public void change_camera_mode(int mode) {
        if (mode_num == mode)
            return;

        try {
            if (null != activity) {
                //close camera
                closeCamera();
                stopBackgroundThread();

                mode_num = mode;

                startBackgroundThread();
                // When the screen is turned off and turned back on, the SurfaceTexture is already
                // available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
                // a camera and start preview from here (otherwise, we wait until the surface is ready in
                // the SurfaceTextureListener).
                if (mTextureView.isAvailable()) {
              //      Log.d(TAG, "mTextureView is available");
                    openCamera(mTextureView.getWidth(), mTextureView.getHeight());
                } else {
              //      Log.d(TAG, "setSurfaceTextureListener");
                    mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
                }
            } else {
            //    Log.d(TAG, "activity is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set_effect_mode(int mode) {
    //    Log.d(TAG, "effect mode: " + mode);

        if (effect_mode == mode)
            return;

        try {
            if (null != activity) {
                //close camera
                closeCamera();
                stopBackgroundThread();

                effect_mode = mode;

                startBackgroundThread();
                // When the screen is turned off and turned back on, the SurfaceTexture is already
                // available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
                // a camera and start preview from here (otherwise, we wait until the surface is ready in
                // the SurfaceTextureListener).
                if (mTextureView.isAvailable()) {
             //       Log.d(TAG, "mTextureView is available");
                    openCamera(mTextureView.getWidth(), mTextureView.getHeight());
                } else {
              //      Log.d(TAG, "setSurfaceTextureListener");
                    mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
                }
            } else {
           //     Log.d(TAG, "activity is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if (capturing == 1) {
      //      Log.d(TAG, "캡쳐 중");
            return;
        }
        if (address == null) {
        //    Log.d(TAG, "address is null");
            Toast.makeText(getActivity(), "위치정보가 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mGoogleMapActivity.get_saving_status() == 1) {
            Toast.makeText(getActivity(), "저장 중 입니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mCameraId == null) {
            Toast.makeText(getActivity(), "카메라가 동작하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            mButtonLock.acquire();

            switch (view.getId()) {
                case R.id.picture: {
                    if (camID != null)
                        picture_button_func();
                    break;
                }
                case R.id.info: {
                    if (camID != null) {
                        if (camera_status == CAM_OPEN) {
                            info_button_func(0);
                        }
                    } else {
                        Toast.makeText(getActivity(), "지원하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }

                    break;
                }

                case R.id.wide: {
                    if (camID_length > 2) {
                        if (camID != null) {
                            if (camera_status == CAM_OPEN) {
                                info_button_func(1);
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), "지원하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            mButtonLock.release();
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
      //      Log.d(TAG, "info button lock released");
            mButtonLock.release();
        }
    }

    private void setAutoFlash(CaptureRequest.Builder requestBuilder) {
        if (mFlashSupported) {
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON);
        }
    }

    /**
     * Saves a JPEG {@link Image} into the specified {@link File}.
     */
    private static String prev_file = "null";
    private static int image_saver_process = 0;
    private class ImageSaver implements Runnable {

        /**
         * The JPEG image
         */
        private final Image mImage;
        /**
         * The file we save the image into.
         */
        private final File mFile;
        ImageSaver(Image image, File file) {
            mImage = image;
            mFile = file;
            image_saver_process = 1;
        }

        @Override
        public void run() {
        //    Log.d(TAG, "image_saver_process: " + image_saver_process);

            if (storage_status == 0) {
                ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                FileOutputStream output = null;
                try {
                    output = new FileOutputStream(mFile);
                    output.write(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    mImage.close();
                    if (null != output) {
                        try {
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            } else {
                try {
                    DocumentFile pickedDir = null;


                        String path = mFile.toString();

                        if (root_path != null) {
                            Uri folder_uri = Uri.parse(root_path);
                            pickedDir = DocumentFile.fromTreeUri(activity, folder_uri); //find root
                            List<String> pathParts = Arrays.asList(path.split("/")); //split folder layer
                            for (int i = 1; i < pathParts.size() - 1; i++) {
                                DocumentFile nextDoc = null;
                                nextDoc = pickedDir.findFile(pathParts.get(i));

                                  if (nextDoc != null) {
                                      pickedDir = nextDoc;
                                  }
                            }

                            /*
                            Log.d(TAG, "pathParts: " + pathParts.get(pathParts.size() - 1) + " prev_file: " + prev_file);

                            if (prev_file == (pathParts.get(pathParts.size() - 1))) {
                                Log.d(TAG, "same path: " + mFile + " prev: " + prev_file);
                                return;

                            } else {
                                prev_file = pathParts.get(pathParts.size() - 1);
                            }

                            prev_file = pathParts.get(pathParts.size() - 1);
                            */

                            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
                            byte[] bytes = new byte[buffer.remaining()];
                            buffer.get(bytes);

                            OutputStream out = null;
                            DocumentFile file = pickedDir.createFile("application/octet-stream", pathParts.get(pathParts.size() - 1));
                   //        Log.d(TAG, "root_path: " + root_path + " path: " + mFile + " file_name:" + pathParts.get(pathParts.size() - 1));
                            try {
                                out = activity.getContentResolver().openOutputStream(file.getUri());
                                out.write(bytes);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                mImage.close();
                                if (null != out) {
                                    try {
                                        out.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }

                        }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }


            mHandler.sendEmptyMessageDelayed(STORE_PICTURES, 0);

        }



    }

    /**
     * Compares two {@code Size}s based on their areas.
     */
    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    /**
     * Shows an error message dialog.
     */
    public static class ErrorDialog extends DialogFragment {

        private static final String ARG_MESSAGE = "message";

        public static ErrorDialog newInstance(String message) {
            ErrorDialog dialog = new ErrorDialog();
            Bundle args = new Bundle();
            args.putString(ARG_MESSAGE, message);
            dialog.setArguments(args);
            return dialog;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Activity activity = getActivity();
            return new AlertDialog.Builder(activity)
                    .setMessage(getArguments().getString(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.finish();
                        }
                    })
                    .create();
        }

    }

    /**
     * Shows OK/Cancel confirmation dialog about camera permission.
     */
    public static class ConfirmationDialog extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Fragment parent = getParentFragment();
            return new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.request_permission)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            parent.requestPermissions(new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA_PERMISSION);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Activity activity = parent.getActivity();
                                    if (activity != null) {
                                        activity.finish();
                                    }
                                }
                            })
                    .create();
        }
    }

    public class MapDataHookThread extends Thread {
        public void run() {
            mMapDataHookThreadRunning = true;
      //      Log.d(TAG, "MapDataHookThread start");
            mHandler.post(new Runnable() {
                public void run() {
                }
            });
            while (mMapDataHookThreadRunning) {
                mHandler.post(new Runnable() {
                    public void run() {
                        mHandler.sendEmptyMessage(MSG_MAP_DATA);
                    }
                });
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
              //      Log.d(TAG, "thread end");
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    private static String prev_addr = "null";
    private static int storage_status = 0;
    private static String root_path;

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
    //        Log.d(TAG, Integer.toString(msg.what));
            switch (msg.what) {
                case MSG_MAP_DATA:
         //           Log.d(TAG,"Handler called in backGround");
                    address = mGoogleMapActivity.returnAddress();
                    storage_status = mGoogleMapActivity.get_storage_choice();
                    root_path = mGoogleMapActivity.get_main_path();
                    if (address != null) {
                        Log.d(TAG, "address[0]" + address[0]);
                        //주소 정보
                        if (mGoogleMapActivity.string_contains_checker(address[0]) == false) { //error의 첫번째 배열 지오코더,잘못된,주소
                            info[0] = address[0];
                            info[1] = address[1];
                            info[2] = address[2];
                        } else {
                            info[0] = "주소정보없음";
                            info[1] = "";
                            info[2] = "";
                        }

                        for (int i = 0; i < address.length; i++) {
                            if (address[i].contains("위도")) {
                                info[3] = address[i];
                            }
                            if (address[i].contains("경도")) {
                                info[4] = address[i];
                            }
                        }
/*
                        Log.d(TAG,"info[0] " + info[0]
                                + " info[1] " + info[1]
                                + " info[2] " + info[2]
                                + " info[3] " + info[3]
                                + " info[4] " + info[4]
                                + " info[5] " + info[5]);
*/

                        String address = info[0] + info[1] + info[2];

                        if (!prev_addr.equals(address)) {
                            prev_addr = address;
                            setDirectory(SET_PATH, Appdir, info, mGoogleMapActivity.get_level_choice()); //한번만???
                        }
                    }

                    break;


                case UPDATE_VIEW:
        //            Log.d(TAG, "UPDATE_VIEW");
          //          metalviewPager.setAdapter(fullMetalAdapter);
                    if (recyclerView != null) {
                        recyclerView.setAdapter(adapter);
                    //    recyclerView.addItemDecoration(decoration);
                    }
                    mHandler.sendEmptyMessageDelayed(UPDATE_NOTIFY, 0);

                    break;
                case UPDATE_NOTIFY:
           //         Log.d(TAG, "UPDATE_NOTIFY");
           //         fullMetalAdapter.notifyDataSetChanged();
                    adapter.notifyDataSetChanged();
                    break;
                case UPDATE_EFFECT_RECYCLER:

                    break;
                case STORE_PICTURES:
                    try {
                        if (capturing == 1) {
                            File[] files = null;
                            if (file_cnt == 0) {
                                files = mediaStorageDir.listFiles();
                                file_cnt = files.length + 1;
                            } else {
                                file_cnt++;
                            }

                 //           Log.d(TAG, "saved" + mFile);


/*
                      //      Bitmap image = BitmapFactory.decodeFile(mFile.getPath());
                            ExifInterface exif = new ExifInterface(mFile.getPath());
                            String exifOrientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                  //          int exifDegree = exifOrientationToDegrees(exifOrientation);


                            int displayRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
                            //noinspection ConstantConditions
                            mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);

                            Log.d(TAG, "orientation: " + exifOrientation + "   " + mSensorOrientation + " " + displayRotation);
*/
                            //todo
                            if (mGoogleMapActivity.set_data(info, file_cnt) == 0) {


                                vibrator.vibrate(50);
                      //          Log.d(TAG, "load_status " + load_status);
                                if (load_status == 1 /*|| viewCreated == 0*/) {
                                    setDirectory(ADD_ADAPTAR, Appdir, info, mGoogleMapActivity.get_level_choice());
                                }

                            } else {
                                Toast.makeText(getActivity(), "주소가 잘못 되었습니다.", Toast.LENGTH_SHORT).show();
                            }

                            capturing = 0;
                        }
                    } catch (Exception e) {

                    }
       //             Log.d(TAG, "ImageSaver Done");
          //          image_saver_process = 0;
                    //       prev_file = null;

                    break;
                case ERROR_REPORT:
                    switch (msg.arg1) {
                        case NO_POSITION_ERROR:
                            break;
                        case NO_READY:
                            break;
                        case NO_CAMERA_WORKS:
                            break;
                    }
                    break;

            }

        };

    };

    private final int NO_POSITION_ERROR = 0;
    private final int NO_READY = 1;
    private final int NO_CAMERA_WORKS = 2;

    public int exifOrientationToDegrees(int exifOrientation)
    {
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
        {
            return 90;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
        {
            return 180;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
        {
            return 270;
        }
        return 0;
    }

    public void OnKeyDown(int keycode) {
        switch (keycode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                /*
                try {
                    if (capturing == 1) {
                        //      Log.d(TAG, "캡쳐 중");
                        return;
                    }
                    if (address == null) {
                        //    Log.d(TAG, "address is null");
                        Toast.makeText(getActivity(), "위치정보가 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (mGoogleMapActivity.get_saving_status() == 1) {
                        Toast.makeText(getActivity(), "저장 중 입니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (mCameraId == null) {
                        Toast.makeText(getActivity(), "카메라가 동작하지 않습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        mButtonLock.acquire();
                        if (camID != null)
                            picture_button_func();

                    } catch (Exception e) {
                        e.printStackTrace();
                        mButtonLock.release();
                        throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
                    } finally {
                        //      Log.d(TAG, "info button lock released");
                        mButtonLock.release();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
*/
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
          //      Log.d(TAG, "volume up");
                break;
            case KeyEvent.KEYCODE_MENU:
            //    Log.d(TAG, "keycode menu");
                break;
            case KeyEvent.KEYCODE_BACK:
           //     Log.d(TAG, "keycode back");
                break;
            case KeyEvent.KEYCODE_HOME:
            //    Log.d(TAG, "keycode home");
                break;

        }
    }
/*
    static int check_delete_status = 0;

    public void check_delete_status (int status) {
        check_delete_status = status;
        Log.d(TAG, "check_delete_status " + check_delete_status);
    }
*/
    public String check_cam_focus_path() {
    //    Log.d(TAG, "check cam focus path");
        return basePath;
    }

    public boolean check_cam_status() {
   //     Log.d(TAG, "check mMapDataHookThreadRunning " + mMapDataHookThreadRunning);
        return mMapDataHookThreadRunning;
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return true;
    }



    public class EffectBinder extends RecyclerBinder<DemoViewType> {
        private final String mResId;
        private Context context;
        private ImagesView imagesView;
        private EffectBinder.ViewHolder holder;

        public EffectBinder(Activity activity, String resId) {
            super(activity, DemoViewType.PAGE);
            mResId = resId;
            imagesView = new ImagesView();
        }


        @Override
        public int layoutResId() {
            return R.layout.row_page;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(View v) {
            return new EffectBinder.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            final int Position = position;
            context = getContext();
            holder = (EffectBinder.ViewHolder) viewHolder;


            //   holder.mImageView.setImageResource(mResId);
            try {

                holder.mImageView.setImageResource(Integer.parseInt(mResId));

            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int ret = 0;
                //    Log.d(TAG, "position: " + Position);
                    try {
                        /*
                        set_effect_mode(Position);
                        */
                        mode_num = Position;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            holder.mImageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(100);
                    return true;
                }
            });
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            View view;
            private final AspectRatioImageView mImageView;
            private final TextView textView;

            public ViewHolder(View view) {
                super(view);
                this.view = view;
                mImageView = (AspectRatioImageView) view.findViewById(R.id.img_page);
                textView = (TextView)view.findViewById(R.id.textview_row_page);

            }
        }
    }


}


