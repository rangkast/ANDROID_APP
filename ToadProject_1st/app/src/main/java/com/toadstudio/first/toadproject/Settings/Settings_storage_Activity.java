package com.toadstudio.first.toadproject.Settings;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;
import com.toadstudio.first.toadproject.CustomDialog.ListCustomDialog_2;
import com.toadstudio.first.toadproject.CustomDialog.ListDialogListener;
import com.toadstudio.first.toadproject.FileController;
import com.toadstudio.first.toadproject.Image.ImagesView;
import com.toadstudio.first.toadproject.MainActivity;
import com.toadstudio.first.toadproject.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by rangkast.jeong on 2018-03-19.
 */

public class Settings_storage_Activity extends AppCompatActivity implements View.OnClickListener {
    public static String TAG = "ToadPrj_Settingsmarker";
    ArrayList<SettingsMarkerItem> data = new ArrayList<>();
    SettingsMarkerItem items;
    private ListView listView;
    private Settings_marker_Adapter adapter;
    private int selected_position = -1;
    private int checked_item;
    private int selected_storage = 0;
    static ListCustomDialog_2 listCustomDialog;
    private ArrayList<PathInfo> exist_path;
    private static String External_Path = null;
    Button back;
    Button store;
    MainActivity mainActivity;
    private  AlertDialog.Builder alertDialog;
    ImagesView imagesView;
    FileController fileController;
    static String Appdir = "/여행을찍다";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_marker_activity);
        listView = (ListView) findViewById(R.id.settings_markerlist);
        final View header = getLayoutInflater().inflate(R.layout.settings_header, null, false);
        listView.addHeaderView(header);
        mainActivity = new MainActivity();
        Intent intent = getIntent();
        imagesView = new ImagesView();
        fileController = new FileController();

        selected_storage = intent.getIntExtra("storage_choice", -1);
 //       Log.d(TAG, "selected_storage: " + selected_storage);
        //마커 설정에서 intent 날라 왔을때 처리
        if (selected_storage > -1) {
            selected_position = selected_storage;
            //        Log.d(TAG, "selected_position" + selected_position);
            //need update
            items = new SettingsMarkerItem(R.drawable.internal850, "내장 메모리", false);
            data.add(items);
            items = new SettingsMarkerItem(R.drawable.external850, "외장 메모리", false);
            data.add(items);

            adapter = new Settings_marker_Adapter(this, R.layout.settings_marker_item, data);
            listView.setAdapter(adapter);

        }


        back = (Button) findViewById(R.id.button_marker_1);
        store = (Button) findViewById(R.id.button_marker_2);
        back.setOnClickListener(this);
        store.setOnClickListener(this);

        exist_path = new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
          //      Log.d(TAG, "onItemClick: " + i);
                if (i > 0) {
                    if (selected_storage > -1) {

                        if (i == 1) {
                   //         Intent intent = new Intent(getBaseContext(), InfoActivity.class);
                    //        intent.putExtra("from", i + 1);
                     //       getBaseContext().startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
                        } else if (i == 2) {
                            if (mainActivity.hasRealRemovableSdCard(mainActivity.return_mainActivity_context()) == true) {
                                final LoadingTask task = new LoadingTask(Settings_storage_Activity.this);
                                task.execute(Settings_storage_Activity.this);
                            } else {
                                mHandler.sendEmptyMessageDelayed(NO_SDCARD, 0); //marker 수정
                            }
                        }

                    }
                }
            }
        });

    }//end onCreate


    public class Settings_marker_Adapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<SettingsMarkerItem> data;
        private int layout;


        public Settings_marker_Adapter(Context context, int layout, ArrayList<SettingsMarkerItem> data) {
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.data = data;
            this.layout = layout;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public String getItem(int position) {
            return data.get(position).getName();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = inflater.inflate(layout, parent, false);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.check.setTag(position);

            if (selected_position == position) {
                holder.check.setChecked(true);
                checked_item = position;
           //     Log.d(TAG, "checked_item" + checked_item);
            } else {
                holder.check.setChecked(false);
            }

            SettingsMarkerItem listviewitem = data.get(position);
            holder.image.setImageResource(listviewitem.getIcon());
            holder.name.setText(listviewitem.getName());

            holder.check.setOnClickListener(onStateChangedListener(holder.check, position));

            return convertView;
        }

    }

    private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selected_position = position;
                } else {
                    selected_position = -1;
                }
                adapter.notifyDataSetChanged();
            }
        };
    }

    private static class ViewHolder {
        TextView name;
        ImageView image;
        CheckBox check;

        public ViewHolder(View v) {
            check = (CheckBox) v.findViewById(R.id.checkBox_marker);
            name = (TextView) v.findViewById(R.id.textview_marker);
            image = (ImageView) v.findViewById(R.id.imageview_marker);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_marker_1:
                //   Log.d(TAG, "canceled");
                Intent intent_cancel = getIntent();
                //       intent.putExtra("data", Integer.toString(checked_item));
                setResult(RESULT_CANCELED, intent_cancel);
                finish();
                break;
            case R.id.button_marker_2:
                //    Log.d(TAG, "stored");

                try {
                    Intent intent = getIntent();


     //               Log.d(TAG, "checked_item: " + checked_item);

                    if (checked_item == imagesView.EXTERNAL_STORAGE) {
                        if (mainActivity.hasRealRemovableSdCard(mainActivity.return_mainActivity_context()) == true) {
                            if (mainActivity.get_main_path().equals("")) {
                                Toast.makeText(this, "SD카드 권한 및 경로가 없습니다.", Toast.LENGTH_SHORT).show();
                                final LoadingTask task = new LoadingTask(Settings_storage_Activity.this);
                                task.execute(Settings_storage_Activity.this);
                            } else {
                                DocumentFile documentFile = fileController.folder_checker(mainActivity.return_mainActivity_context(), mainActivity.get_main_path(), Appdir, fileController.SEARCH_DIR);
                                if (documentFile != null) {
                                    if (documentFile.canWrite() == true) {
                             //           Log.d(TAG, "documentFile: " + documentFile.getUri() + " auth: " + documentFile.canWrite());
                          //              mainActivity.set_default_storage(imagesView.EXTERNAL_STORAGE);
                                        intent.putExtra("data", Integer.toString(checked_item));
                                        setResult(RESULT_OK, intent);
                                        Toast.makeText(this, "저장 되었습니다.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                } else {
                                    final LoadingTask task = new LoadingTask(Settings_storage_Activity.this);
                                    task.execute(Settings_storage_Activity.this);
                                    Toast.makeText(this, "권한이 없습니다. 새 경로 설정을 해주세요.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        } else {
                            Toast.makeText(this, "SD카드가 없습니다.", Toast.LENGTH_SHORT).show();
                       }
                    } else if (checked_item == imagesView.INTERNAL_STORAGE) {
              //          mainActivity.set_default_storage(imagesView.INTERNAL_STORAGE);
                        intent.putExtra("data", Integer.toString(checked_item));
                        setResult(RESULT_OK, intent);
                        Toast.makeText(this, "저장 되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "잠시후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public boolean check_authority_external(Context context) {
        try {
            boolean status = true;
            String root_path = mainActivity.get_main_path();
            FileController fileController = new FileController();
            DocumentFile documentFile = fileController.folder_checker(context, mainActivity.get_main_path(), Appdir, fileController.SEARCH_DIR);
    //        Log.d(TAG, "check_authority_external: " + root_path + " : " + documentFile);

                if (root_path == null || documentFile == null) {
                status = false;
            } else {
                if (documentFile.canWrite() == false) {
                    status = false;
                }
            }

            return status;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public class LoadingTask extends AsyncTask {

        private Context mContext;

        public LoadingTask(Context context) {
            mContext = context;
        }

        FileController fileController = new FileController();

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String root_path = mainActivity.get_main_path();
      //          Log.d(TAG, "isExternalStorageWritable: " + fileController.isExternalStorageWritable() + " root_path: " + root_path);
                External_Path = fileController.return_External_path(mContext);
                exist_path.clear();
                exist_path = fileController.queryFiles(mContext, External_Path, mContext.getString(R.string.app_name));

                    if (exist_path != null) {
                        if (exist_path.size() > 0) {
                            mHandler.sendMessage(mHandler.obtainMessage(
                                    FOLDER_CHECK,
                                    EXIST_LIST, 0));
                        } else {
                            mHandler.sendMessage(mHandler.obtainMessage(
                                    FOLDER_CHECK,
                                    NO_LIST, 0));
                        }

                    } else {

                            mHandler.sendMessage(mHandler.obtainMessage(
                                    FOLDER_CHECK,
                                    NO_LIST, 0));

                    }

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


    public static final int FOLDER_CONTROL = 0;
    public static final int FOLDER_CHECK = 1;
    public static final int GET_URI = 2;
    public static final int NO_SDCARD = 3;

    public static final int NO_LIST = 0;
    public static final int EXIST_LIST= 1;
    private static DocumentFile documentFile;

    private void getUri(File file) {

        FileController fileController = new FileController();
        //       Log.d(TAG, "getUriFromPath: " + fileController.getUriFromPath(Settings_storage_Activity.this, null, folder));
        documentFile = DocumentFile.fromFile(file);
/*
        Log.d(TAG, "pickDir uri: " + documentFile.getUri()
                + "\nparent: " + documentFile.getParentFile().getUri());
                */
    }
    private static String return_folder_path = null;
    Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            try {
                super.handleMessage(msg);
                switch (msg.what) {
                    case FOLDER_CONTROL:

                        //      Toast.makeText(getApplicationContext(), "이미 경로가 존재합니다.", Toast.LENGTH_SHORT).show();

                        ArrayList<String> path_list = new ArrayList();
                        for (int i = 0; i < exist_path.size(); i++) {
                            path_list.add(exist_path.get(i).getFolderPath());
                        }

                        String path_header = mainActivity.get_sdcard_path_header() + File.separator + "여행을찍다";

                        if (path_header == null)
                            path_header = "없음";

                        listCustomDialog = new ListCustomDialog_2(Settings_storage_Activity.this, "폴더 경로 확인", "현재 폴더: " + path_header +
                                "\n\n폴더 목록",
                                "",
                                path_list, null);

                        listCustomDialog.setDialogListener(new ListDialogListener() {  // MyDialogListener 를 구현
                            @Override
                            public int onPositiveClicked(Integer status, String folder) {
                         //       Log.d(TAG, "ok " + status + " path " + folder); //여기서 커스텀 다이얼로그가 누른 값 return
                                return_folder_path = folder;
                                mHandler.sendEmptyMessageDelayed(GET_URI, 0);

                                listCustomDialog.dismiss();

                                return status;
                            }

                            @Override
                            public void onNegativeClicked(Integer status) {
                                //           Log.d(TAG, "cancel");
                            }
                        });
                        listCustomDialog.show();

                        break;

                    case FOLDER_CHECK:
                        alertDialog = new AlertDialog.Builder(Settings_storage_Activity.this);
                        alertDialog.setTitle("새 폴더 확인");
                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.toad_app_icon_v4);
                        // Setting Dialog Message
                        alertDialog.setMessage("\n경로 정보가 없습니다.");

                        // Setting Positive "Yes" Button
                        alertDialog.setPositiveButton("새경로설정", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    FileController fileController = new FileController();
                                    External_Path = fileController.return_External_path(Settings_storage_Activity.this);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Intent chooseFile = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                                startActivityForResult(chooseFile, 1);


                                dialog.cancel();
                            }
                        });
                        alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        if (msg.arg1 == EXIST_LIST) {
                            alertDialog.setNeutralButton("기존경로설정", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mHandler.sendEmptyMessageDelayed(FOLDER_CONTROL, 0); //marker 수정
                                }
                            });
                        }
                        // Showing Alert Message
                        alertDialog.show();
                        break;
                    case GET_URI:
                        try {
                            for (int i = 0; i < exist_path.size(); i++) {
                                String get_exist_path = exist_path.get(i).getFolderPath();
                                if (get_exist_path.equals(return_folder_path)) {

                                    String[] split = External_Path.split("/");
/*
                                    for (int j = 0; j < split.length; j++) {
                                        Log.d(TAG, "split: " + split[j]);
                                    }
*/
                                    String[] split_path = get_exist_path.split(split[2] + "/");
/*
                                    for (int j = 0; j < split_path.length; j++) {
                                        Log.d(TAG, "split_path: " + split_path[j]);
                                    }
                                    */

                                    String[] split_path_dir = split_path[1].split("/");
                                    StringBuilder stringBuilder = new StringBuilder();
                                    for (int j = 0; j < split_path_dir.length -1; j++) {
                                        stringBuilder.append(split_path_dir[j]);
                                        if (j < split_path_dir.length - 2)
                                            stringBuilder.append("/");

                                    }

                                    String root_path = "/tree/" + split[2] + ":" + stringBuilder.toString() + "/document/" + split[2] + ":"
                                            + stringBuilder.toString();

                                    String uri_encode = Uri.encode(root_path, "euc-kr").toString();


                                    String[] split_root_path = uri_encode.split(split[2]);
/*
                                    for (int j = 0; j < split_root_path.length; j++) {
                                        Log.d(TAG, "split_root_path: " + split_root_path[j]);
                                    }
*/
                                    String path_to_treeURI = "content://com.android.externalstorage.documents/tree/" + split[2] + split_root_path[split_root_path.length -1]
                                            + "/document/" +split[2] + split_root_path[split_root_path.length - 1];

                     //               Log.d(TAG, "remake root_path: " + root_path + "<" + uri_encode + ">" + "<<" + path_to_treeURI + ">>") ;

                                   // Uri folder_uri =  Uri.parse("content://com.android.externalstorage.documents/tree/9016-4EF8%3A/document/9016-4EF8%3A");
                                    DocumentFile documentFile = DocumentFile.fromTreeUri(mainActivity.return_mainActivity_context(), Uri.parse(path_to_treeURI)); //find root

                      //             Log.d(TAG, "path_to_treeURI: " + documentFile.canWrite());
/*
                                    DocumentFile[] list_files = documentFile.listFiles();

                                    for (int j = 0; j < list_files.length; j++) {
                                        Log.d(TAG, "list_files:" + list_files[j].getName()
                                                + " isfile:" + list_files[j].isFile() + " isDir:" + list_files[j].isDirectory());
                                    }
*/
                              //      FileController fileController = new FileController();

                                    if (documentFile.canWrite() == true) {
                                        ImagesView imagesView = new ImagesView();
                                        //      fileController.folder_checker(mainActivity.return_mainActivity_context(), split_path[1], fileController.SEARCH_DIR);
                                        mainActivity.set_main_path(documentFile.getUri().toString());
                                        mainActivity.set_sdcard_path_header(External_Path + "/" + stringBuilder.toString());
                                        mainActivity.set_default_storage(imagesView.EXTERNAL_STORAGE); //External storage = 1
                                        //Internal storage = 0
                                        Toast.makeText(Settings_storage_Activity.this, "권한 획득하였습니다", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(Settings_storage_Activity.this, "권한 획득을 실패 했습니다.", Toast.LENGTH_SHORT).show();
                                        try {
                                            FileController fileController = new FileController();
                                            External_Path = fileController.return_External_path(Settings_storage_Activity.this);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        Intent chooseFile = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                                        startActivityForResult(chooseFile, 1);
                                    }
                                        /*


                                    Uri folder_uri =  MediaStore.Files.getContentUri("external");

                                    DocumentFile documentFile = DocumentFile.fromSingleUri(mainActivity.return_mainActivity_context(), exist_path.get(i).getUri()); //find root

                              //      DocumentFile[] list_files = documentFile.listFiles();
                                    String documentsContract = DocumentsContract.getTreeDocumentId(documentFile.getUri());

                                    Log.d(TAG, "docContract: " + documentsContract);

                                    for (int j = 0; j < list_files.length; j++) {
                                        Log.d(TAG, "list_files:" + list_files[i].getName()
                                                + " isfile:" + list_files[i].isFile() + " isDir:" + list_files[i].isDirectory());
                                    }


                                    Log.d(TAG, "get URI form mPathInfo: " + exist_path.get(i).getUri()
                                    + "\n docu.getUri: " + documentFile.getUri()
                                    + "(" + documentFile.canRead()
                                    + "," + documentFile.canWrite() +")");
*/
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case NO_SDCARD:
                        Toast.makeText(Settings_storage_Activity.this, "SD카드가 없습니다.", Toast.LENGTH_SHORT).show();
                        break;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 1:
           //     Log.d(TAG, "Request_file_path");
                try {

                    Context context = mainActivity.return_mainActivity_context();

                    Uri treeUri = data.getData();
                    DocumentFile pickedDir = DocumentFile.fromTreeUri(context, treeUri);
                    context.grantUriPermission(context.getPackageName(), treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    final int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    context.getContentResolver().takePersistableUriPermission(treeUri, takeFlags);
/*
                    Log.d(TAG, "URI: " + treeUri + " <Path: " + treeUri.getPath() + ">"
                            + "\npickedDir.getName: " + pickedDir.getName()
                            + "\npickedDir.canWrite: " + pickedDir.canWrite()
                            + "\npickedDir.canRead: " + pickedDir.canRead()
                            + "\npickedDir.toString: " + pickedDir.toString());
*/

                    DocumentFile[] list_files = pickedDir.listFiles();

                    int detect = 0;
                    for (int j = 0; j < list_files.length; j++) {
                        /*
                        Log.d(TAG, "list_files:" + list_files[j].getName()
                                + " isfile:" + list_files[j].isFile() + " isDir:" + list_files[j].isDirectory());
                                */
                        if (list_files[j].getName().equals("여행을찍다")) {
                            detect = 1;
                        }
                    }

                    if (detect == 0)
                        pickedDir.createDirectory("여행을찍다");
                    else {
                        Toast.makeText(this, "경로가 존재합니다.", Toast.LENGTH_SHORT).show();
                    }

                    mainActivity.set_main_path(pickedDir.getUri().toString());

                    List<String> split_str = Arrays.asList(pickedDir.getUri().getPath().split(":"));
                    mainActivity.set_sdcard_path_header(External_Path + File.separator + split_str.get(split_str.size() - 1));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }
}
