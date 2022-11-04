package com.toadstudio.first.toadproject.Image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.toadstudio.first.toadproject.FileController;
import com.toadstudio.first.toadproject.MainActivity;
import com.toadstudio.first.toadproject.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.security.MessageDigest;

import static com.toadstudio.first.toadproject.Image.ImagesView.fileController;
import static com.toadstudio.first.toadproject.Image.ImagesView.file_path;
import static com.toadstudio.first.toadproject.Image.ImagesView.mainActivity;

/**
 * Created by rangkast.jeong on 2018-03-06.
 */

public class CustomAdapter extends PagerAdapter {
    public static String TAG = "ToadPrj_CustomAdapter";
    private NewViewPager mPager = null;
    private View view = null;
    Context context;
    LayoutInflater inflater;
    String img_array[];
    String path;
    int id;
    int roop_id = 0;
    float rotate = 0f;
    private FileController fileController = new FileController();
    public CustomAdapter(Context context, final NewViewPager pager, String[] img, String path, int id, int rotate) {
        // TODO Auto-generated constructor stub
        super();

        this.img_array = img;
        this.path = path;
        this.context = context;
        this.id = id;
        this.rotate = rotate;
        mPager = pager;
        //전달 받은 LayoutInflater를 멤버변수로 전달
        inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

  //      mPager.setCurrNum(id);


        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

  //             Log.d(TAG, "position:" + position + " offset:" + positionOffset + " pixels:" +positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
  //              Log.d(TAG, "onPageSelected " + position);
                try {
                    FocusViewPager focusViewPager = new FocusViewPager();
                    focusViewPager.set_edit(position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

     //           Log.d(TAG, "onPageScrollStateChanged" + state);

            }
        });

        mPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int pointer_cnt = motionEvent.getActionIndex();
                //Log.d(TAG, Integer.toString(motionEvent.getActionIndex()));

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (pointer_cnt < 1)
                            mPager.scrollBy((int)motionEvent.getX(), (int)motionEvent.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        if (pointer_cnt < 1)
                            mPager.scrollBy((int)motionEvent.getX(), (int)motionEvent.getY());
                        break;
                }

                return false;
            }
        });


    }

    //PagerAdapter가 가지고 잇는 View의 개수를 리턴
    //보통 보여줘야하는 이미지 배열 데이터의 길이를 리턴
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
 //       Log.d(TAG, "img.length: " + img_array.length);

        return img_array.length; //이미지 개수 리턴
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
 //       Log.d(TAG, "itemposition" + super.getItemPosition(object));
        return super.getItemPosition(object);
    }


    private static ImageButton playBtn;
    private static ImageButton rotateBtn;
    private PhotoView photoView;
    //ViewPager가 현재 보여질 Item(View객체)를 생성할 필요가 있는 때 자동으로 호출
    //쉽게 말해, 스크롤을 통해 현재 보여져야 하는 View를 만들어냄.
    //첫번째 파라미터 : ViewPager
    //두번째 파라미터 : ViewPager가 보여줄 View의 위치(가장 처음부터 0,1,2,3...)
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // TODO Auto-generated method stub
        //새로운 View 객체를 Layoutinflater를 이용해서 생성
        //만들어질 View의 설계는 res폴더>>layout폴더>>viewpater_childview.xml 레이아웃 파일 사용
        view= inflater.inflate(R.layout.focus_image, null);
        try {
            ;
            //만들어진 View안에 있는 ImageView 객체 참조
            //위에서 inflated 되어 만들어진 view로부터 findViewById()를 해야 하는 것에 주의.

            photoView = (PhotoView) view.findViewById(R.id.focus_image_id);
            MainActivity mainActivity = new MainActivity();

            playBtn = (ImageButton) view.findViewById(R.id.play_btn);
            playBtn.setOnClickListener(mOnClickListener);
            playBtn.setTag(position);

/*
            rotateBtn = (ImageButton) view.findViewById(R.id.rotate_btn);
            rotateBtn.setOnClickListener(mOnClickListener);
            rotateBtn.setTag(position);
*/

            //ImageView에 현재 position 번째에 해당하는 이미지를 보여주기 위한 작업
            //현재 position에 해당하는 이미지를 setting
            //   img.setImageResource(R.drawable.gametitle_01+position);

            //    Log.d(TAG, "position:" + position + " id:" +id);
            if (context != null) {

                if ((position + id) < img_array.length) {
                    roop_id = position + id;
                } else {
                    roop_id = position + id - img_array.length;

                }

                if (position == id) {
                    GlideApp
                            .with(context)
                            .load(path + File.separator + img_array[position])
                            .transform(new RotateTransformation(rotate))
                            .thumbnail(0.1f)
                            .into(photoView);

                } else {
                    GlideApp
                            .with(context)
                            .load(path + File.separator + img_array[position])
                            .thumbnail(0.1f)
                            .into(photoView);
                }
                if (fileController.file_format_check(img_array[position]) == 2) {
                    playBtn.setVisibility(View.VISIBLE);
                } else {
                    playBtn.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //ViewPager에 만들어 낸 View 추가
        container.addView(view);

        //Image가 세팅된 View를 리턴
        return view;
    }


    ImageButton.OnClickListener mOnClickListener = new  ImageButton.OnClickListener() {

        @Override
        public void onClick(View v) {
            int position = Integer.parseInt( (v.getTag().toString()) );
       //     Log.d(TAG, "position " + position);

            switch (v.getId()) {
                case R.id.play_btn:
                    try {

                        Intent intent_video = new Intent(Intent.ACTION_VIEW);
                        File file = new File(path + File.separator + img_array[position]);
              //          Log.d(TAG, "file: " + file.getPath());
                        Uri contentUri = null;
                        //update 1
                        if (mainActivity.get_sdcard_path_header() != null) {
                            if (file.getPath().contains(mainActivity.get_sdcard_path_header())) {
                                contentUri = fileController.getUriFromPath(context, file.getPath(), null);
                            } else {
                                contentUri = FileProvider.getUriForFile(context, "com.toadstudio.first.toadproject" + ".fileprovider", file);
                            }
                        } else {
                            contentUri = FileProvider.getUriForFile(context, "com.toadstudio.first.toadproject" + ".fileprovider", file);
                        }

                        intent_video.setDataAndType(contentUri, "video/*");
                        intent_video.addFlags(intent_video.FLAG_GRANT_READ_URI_PERMISSION);
                        intent_video.addFlags(intent_video.FLAG_GRANT_WRITE_URI_PERMISSION);

                        context.startActivity(intent_video);

                    } catch (Exception e) {
                        Toast.makeText(context, "동영상을 실행할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    break;
                    /*
                case R.id.rotate_btn:
                    Log.d(TAG, "rotateBtn clicked");
                    try {
                 //       photoView = (PhotoView) view.findViewById(R.id.focus_image_id);
                        GlideApp
                                .with(context)
                                .load(path + File.separator + img_array[position])
                                .transform(new RotateTransformation(90f))
                                .thumbnail(0.1f)
                                .into(photoView);

       //                 photoView.notify();

                        if (fileController.file_format_check(img_array[position]) == 2) {
                            playBtn.setVisibility(View.VISIBLE);
                        } else {
                            playBtn.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

*/
            }



        }
    };



    //화면에 보이지 않은 View는파쾨를 해서 메모리를 관리함.
    //첫번째 파라미터 : ViewPager
    //두번째 파라미터 : 파괴될 View의 인덱스(가장 처음부터 0,1,2,3...)
    //세번째 파라미터 : 파괴될 객체(더 이상 보이지 않은 View 객체)
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        // TODO Auto-generated method stub
        //ViewPager에서 보이지 않는 View는 제거
        //세번째 파라미터가 View 객체 이지만 데이터 타입이 Object여서 형변환 실시
        container.removeView((View)object);
    }
    //instantiateItem() 메소드에서 리턴된 Ojbect가 View가  맞는지 확인하는 메소드

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        // TODO Auto-generated method stub
        return v==obj;
    }


    public class RotateTransformation extends BitmapTransformation {

        private float rotateRotationAngle = 0f;

        public RotateTransformation(float rotateRotationAngle) {
       //     super(context);

            this.rotateRotationAngle = rotateRotationAngle;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            Matrix matrix = new Matrix();

            matrix.postRotate(rotateRotationAngle);

            return Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true);
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {
            messageDigest.update(("rotate" + rotateRotationAngle).getBytes());
        }
    }


}
