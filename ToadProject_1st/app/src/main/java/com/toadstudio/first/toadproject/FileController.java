package com.toadstudio.first.toadproject;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.os.EnvironmentCompat;
import android.support.v4.provider.DocumentFile;
import android.util.Log;

import com.toadstudio.first.toadproject.CustomDialog.ListCustomDialog_2;
import com.toadstudio.first.toadproject.CustomDialog.ListDialogListener;
import com.toadstudio.first.toadproject.Settings.PathInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileController extends Activity {
    public static String TAG = "ToadPrj_FileControl";
    private List<String> exist_path;
    static String Appdir = "/여행을찍다";


    // 아래로 test function 임
    public ArrayList<PathInfo> queryFiles(Context context, String external_path, String keyword) {
   //     Log.d(TAG, "queryFiles start");

        try {
            ArrayList<PathInfo> mPathInfo = new ArrayList<>();
            PathInfo mPathInfo_class;

            //      List<String> folder_path = new ArrayList<>();
            // External 스토리지의 URI 획득
            final Uri uri = MediaStore.Files.getContentUri("external");
            //ID, 파일명, mimeType, 파일크기 을 가져오도록 설정
            final String[] projection = new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DISPLAY_NAME, MediaStore.Files.FileColumns.MIME_TYPE, MediaStore.Files.FileColumns.SIZE};
            //파일명에 Keyword 가 있으면 쿼리
            //     String keyword = context.getString(R.string.app_name);
            final String selection = MediaStore.Files.FileColumns.DATA + " LIKE '%" + external_path + "%'" + " AND " + MediaStore.Files.FileColumns.DATA + " LIKE '%" + keyword + "'";
            final String[] selectionArgs = new String[]{keyword, "NULL"};

            // 쿼리 수행 후, 컬럼명, 값 출력
            Cursor cursor = context.getContentResolver().query(uri, projection, selection, null, null);

            while (cursor != null && cursor.moveToNext()) {
                int columnCount = cursor.getColumnCount();
                Uri cursor_uri = null;
                String path_from_uri = "";
                for (int i = 0; i < columnCount; i++) {
                    //         Log.d(TAG, cursor.getColumnName(i) + " : " + cursor.getString(i));
                    if (cursor.getColumnName(i).equals("_id")) {
                        cursor_uri = ContentUris.withAppendedId(MediaStore.Files.getContentUri("external"),
                                Integer.parseInt(cursor.getString(i)));

                        path_from_uri = getPathFromUri(context, cursor_uri);
                        if (path_from_uri == null) {
                            return null;
                        }
//                    folder_path.add(path_from_uri);


              //          Log.d(TAG, "Add mPathInfo: " + cursor_uri + " / " + path_from_uri);

                        mPathInfo_class = new PathInfo(
                                cursor_uri,
                                path_from_uri);
                        mPathInfo.add(mPathInfo_class);

                    }

                }
                //        Log.d(TAG, "----------------------------");
            }

            if (cursor != null)
                cursor.close();


            return mPathInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    public int run_activiy(Context context, int control) {
        int ret = 0;
        try {
            switch (control) {
                case 0:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }


    public final int MAKE_DIR = 0;
    public final int DELETE_DIR = 1;
    public final int SEARCH_DIR = 2;
    public final int UPDATE = 3;

    public final int COPY_FILE = 0;
    public final int MOVE_FILE =1;
    public final int DELETE_FILE = 2;
    public final int UPDATE_FILE = 3;


    //Todo
    public class FileTask extends AsyncTask {

        private Context mContext;

        public FileTask(Context context) {
            mContext = context;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {

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

    public int file_format_check (String data) {
        int detect = 0;
        if (data.contains(".jpg")
                || data.contains(".JPG")
                || data.contains(".png")
                || data.contains(".gif")
                || data.contains(".swf")
                || data.contains(".jpeg")
                || data.contains(".bmp")
                || data.contains(".tif")
                || data.contains(".tiff")
                || data.contains(".raw")
                || data.contains(".psd")
                || data.contains(".webp")) {
            detect = 1;
        } else if (data.contains(".mp4")
                || data.contains(".wmv")
                || data.contains(".avi")
                || data.contains(".MPG")
                || data.contains(".MPEG")
                || data.contains(".MOV")
                || data.contains(".DAT")
                || data.contains(".FLV")
                || data.contains(".ASF")
                || data.contains(".ASX")) {
            detect = 2;
        } else if (!data.contains(".")) {
            detect = 0;
        } else {
            detect = -1;
        }

        return detect;
    }

    public DocumentFile folder_checker(Context context, String root_path, String path, int data) {
        DocumentFile pickedDir = null;
 //     MainActivity mainActivity = new MainActivity(); //이게 문제임
 //       String root_path = mainActivity.get_main_path();

        try {
       //     Log.d(TAG, "root_path: " + root_path + " path: " + path);



            if (root_path != null) {
                Uri folder_uri = Uri.parse(root_path);
                pickedDir = DocumentFile.fromTreeUri(context, folder_uri); //find root
                List<String> pathParts;
                List<String> pathParts_rename_to = null;

                if (data == UPDATE) {
                    List<String> update_parts = Arrays.asList(path.split(":")); //split folder layer
                    pathParts = Arrays.asList(update_parts.get(0).split("/")); //split folder layer
                    pathParts_rename_to = Arrays.asList(update_parts.get(1).split("/")); //split folder layer

              //      Log.d(TAG, "pathParts: " + update_parts.get(0) + " rename_parts: " + update_parts.get(1));

                } else {
                    pathParts = Arrays.asList(path.split("/")); //split folder layer
                }


                for (int i = 1; i < pathParts.size(); i++) {
                    DocumentFile nextDoc = null;
                    if (file_format_check(pathParts.get(i)) == 0) { //폴더 일 경우
                        nextDoc = pickedDir.findFile(pathParts.get(i));

                        if (nextDoc != null) {
                            pickedDir = nextDoc;

                            if (data == DELETE_DIR) {
                                if (i == pathParts.size() - 1) {
                                    pickedDir.delete();
                            //        Log.d(TAG, "deleted: " + pickedDir.getUri().getPath());
                                }
                            } else if (data == SEARCH_DIR) {
                            //    Log.d(TAG, "search: " + pickedDir.getUri().getPath());
                            } else if (data == UPDATE) {
                                if (pathParts_rename_to != null) {
                                    if (!pathParts.get(i).equals(pathParts_rename_to.get(i))) {
                                   //    Log.d(TAG, "rename : " + pathParts.get(i) + " to " + pathParts_rename_to.get(i));
                                        pickedDir.renameTo(pathParts_rename_to.get(i));
                                    }
                                }
                            }

                        } else { //파일일 경우
                            if (data == MAKE_DIR) {
                                pickedDir.createDirectory(pathParts.get(i));
                                nextDoc = pickedDir.findFile(pathParts.get(i));
                                pickedDir = nextDoc;
                            } else if (data == SEARCH_DIR) {
                                pickedDir = nextDoc;
                            } else {
                                pickedDir = null;
                            }
                        }
                    } else {
                        if (data == DELETE_DIR) {
                            nextDoc = pickedDir.findFile(pathParts.get(i));
                            if (nextDoc != null) {
                                pickedDir = nextDoc;
                            }
                            if (i == pathParts.size() - 1) {
                                    pickedDir.delete();
                                //    Log.d(TAG, "deleted: " + pickedDir.getUri().getPath());
                            }
                        }
                    }
/*
                    Log.d(TAG, "path(" + i + ")" + pathParts.get(i) + " nextDoc" + nextDoc.getName()
                            + "nextDoc.canWrite:" + nextDoc.canWrite());
*/
                }
/*
                if (pickedDir == null) {
                    Log.d(TAG, "pickedDir is null");
                } else {
                    Log.d(TAG, "pickedDir info: " + pickedDir.getUri()
                            + "/" + pickedDir.isDirectory() + "(" + pickedDir.canWrite()
                            + "/" + pickedDir.canRead() + ")" + pickedDir.getName());
                }
                */
            }

            return pickedDir;
        } catch (Exception e) {
            return null;
        }
    }


    public int file_controller(Context context, String root_path, String inputPath, String outputPath, String Filename, int control) {

        try {
            DocumentFile pickedDir = null;
/*
            Log.d(TAG, "inputPath:" + inputPath + "\noutputPath: " + outputPath
                    + "\nFilename: " + Filename + "\ncontrol: " + control);
                    */
     //       MainActivity mainActivity = new MainActivity(); //이게 문제임
     //       String root_path = mainActivity.get_main_path();


            switch (control) {
                case COPY_FILE:

                        pickedDir = folder_checker(context,root_path, outputPath, MAKE_DIR);

                        InputStream in = null;
                        OutputStream out = null;

                        in = new FileInputStream(inputPath);

/*
                String id = DocumentsContract.getTreeDocumentId(folder_uri);

                Log.d(TAG, "id: " + id);
                id = id + "/여행을찍다/서울시/강서구";

                Uri childrenUri = DocumentsContract.buildDocumentUriUsingTree(folder_uri, id);
                DocumentFile chhildfile = DocumentFile.fromSingleUri(context,childrenUri);


                Log.d(TAG, "chhildfile: " + chhildfile.getUri().toString() + " path: "+ chhildfile.getUri().getPath());
*/

                        DocumentFile file = pickedDir.createFile("application/octet-stream", Filename);
                        out = context.getContentResolver().openOutputStream(file.getUri());

//                        DocumentFile[] list_files = pickedDir.listFiles();
/*

                        for (int i = 0; i < list_files.length; i++) {
                            Log.d(TAG, "list_files:" + list_files[i].getName()
                                    + " isfile:" + list_files[i].isFile() + " isDir:" + list_files[i].isDirectory());
                        }
*/

                        byte[] buffer = new byte[1024];
                        int read;
                        while ((read = in.read(buffer)) != -1) {
                            out.write(buffer, 0, read);
                        }
                        in.close();

                        // write the output file (You have now copied the file)
                        out.flush();
                        out.close();



                    break;
                case MOVE_FILE:
                    break;
                case DELETE_FILE:

                        pickedDir = folder_checker(context, root_path,inputPath + File.separator + Filename, DELETE_DIR);

                    break;
                case UPDATE_FILE:
                    pickedDir = folder_checker(context, root_path,inputPath + ":" + Filename, UPDATE);
                    break;
            }

            return 0;
        } catch (FileNotFoundException fnfe1) {
            fnfe1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void find_uri_by_name(Context context, String name) {
        Uri parcialUri = Uri.parse("content://media/external/dir"); // also can be "content://media/internal/audio/media", depends on your needs
        Uri finalSuccessfulUri = null;

        Cursor cursor = context.getContentResolver().query( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,

                null, "_data = '" + name + "'", null, null );


        String path = cursor.getString(cursor.getColumnIndex("_data"));

  //      Log.d(TAG, " <path:" + path + ">");

        cursor.close();

        /*
        while(!cursor.isAfterLast())
        {
            if (name.compareToIgnoreCase(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))) == 0) {
                int ID = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                finalSuccessfulUri = Uri.withAppendedPath(parcialUri, "" + ID);
                break;
            }
            cursor.moveToNext();
        }
        Log.d(TAG, "find_uri:" + finalSuccessfulUri.toString());
        */
    }

    public String getPathFromUri(Context context, Uri uri){
        try {

            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

            cursor.moveToNext();

            String path = cursor.getString(cursor.getColumnIndex("_data"));

            cursor.close();

      //      Log.d(TAG, "uri:" + uri.toString() + " <path:" + path + ">");
            return path;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public Uri getUriFromPath(Context context, String path, String data_path) {

        Log.d(TAG, "path_to_uri " +  path);
        String filePath = null;
        if (path != null) {
            Uri fileUri = Uri.parse(path);

            filePath = fileUri.getPath();
        }

        if (data_path != null) {
            filePath = data_path;
        }

        int format = file_format_check(path);
        Cursor c = null;
        Uri uri = null;
        if (format == 1) {
            c = context.getContentResolver().query( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, "_data = '" + filePath + "'", null, null );
        } else if (format == 2) {
            c = context.getContentResolver().query( MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    null, "_data = '" + filePath + "'", null, null );
        } else {
            return null;
        }

        if (c == null)
            return null;

        c.moveToNext();

        int id = c.getInt( c.getColumnIndex( "_id" ) );
        if (format == 1) {
            uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
        } else if (format == 2) {
            uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
        }
        c.close();

        return uri;
    }

    /*
    private ContentObserver makeFolderObserver = new ContentObserver(new Handler()) {

        @Override public void onChange(boolean selfChange){

            Log.d(TAG, "oncanged:" + selfChange);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {

            Log.d(TAG, "oncanged:" + selfChange + " uri:" + uri.toString());


        }
    };
*/
    @Override
    protected void onDestroy() {
        super.onDestroy();

  //      getBaseContext().getContentResolver().unregisterContentObserver(makeFolderObserver);
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public String return_External_path(Context context) {
     //   Log.d(TAG, "getExternalStorageDirectories start");
        String[] external_path = getExternalStorageDirectories(context);
        String return_path = null;
        if (external_path.length > 0) {
            for (int k = 0; k < external_path.length; k++) {
         //       Log.d(TAG, "external_path: " + external_path[k] + "\n");
                return_path = external_path[k];
            }
        }
        return return_path;
    }

    /* returns external storage paths (directory of external memory card) as array of Strings */
    public String[] getExternalStorageDirectories(Context context) {
        List<String> results = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//Method 1 for KitKat & above
            File[] externalDirs = context.getExternalFilesDirs(null);

            for (File file : externalDirs) {
                String path = file.getPath().split("/Android")[0];
                boolean addPath = false;

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    addPath = Environment.isExternalStorageRemovable(file);
                } else {
                    addPath = Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(file));
                }

                if(addPath){ results.add(path); }
            }
        }

        if(results.isEmpty()) { //Method 2 for all versions
// better variation of: http://stackoverflow.com/a/40123073/5002496
            String output = "";
            try {
                final Process process = new ProcessBuilder().command("mount | grep /dev/block/vold") .redirectErrorStream(true).start();
                process.waitFor();
                final InputStream is = process.getInputStream();
                final byte[] buffer = new byte[1024];
                while (is.read(buffer) != -1) {
                    output = output + new String(buffer);
                }
                is.close();
            } catch (final Exception e) {
                e.printStackTrace();
            }
            if(!output.trim().isEmpty()) {
                String devicePoints[] = output.split("\n"); for(String voldPoint: devicePoints) { results.add(voldPoint.split(" ")[2]);
                }
            }
        }

        //Below few lines is to remove paths which may not be external memory card, like OTG (feel free to comment them out)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < results.size(); i++) {
                if (!results.get(i).toLowerCase().matches(".*[0-9a-f]{4}[-][0-9a-f]{4}")) {
                //    Log.d(TAG, results.get(i) + " might not be extSDcard");
                    results.remove(i--);
                }
            }
        } else {
            for (int i = 0; i < results.size(); i++) {
                if (!results.get(i).toLowerCase().contains("ext") && !results.get(i).toLowerCase().contains("sdcard")) {
              //     Log.d(TAG, results.get(i)+" might not be extSDcard"); results.remove(i--);
                }
            }
        }
        String[] storageDirectories = new String[results.size()];
        for(int i=0; i<results.size(); ++i)
            storageDirectories[i] = results.get(i);
        return storageDirectories;
    }



    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
