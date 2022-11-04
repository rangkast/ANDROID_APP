package com.toadstudio.first.toadproject.BackUp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.toadstudio.first.toadproject.CustomDialog.CustomProgressDialog;
import com.toadstudio.first.toadproject.MainActivity;
import com.toadstudio.first.toadproject.R;
import com.toadstudio.first.toadproject.SQLiteDB.ContactDBHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
public class GoogleSignInActivity extends BaseActivity implements
        View.OnClickListener {

    private static final String TAG = "Toad_SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;
    private TextView mStatusTextView;
  //  private TextView mDetailTextView;

    private static int server_status = 1;
    private static List<String> db_paths = new ArrayList<>();
    private checkServerStatus checkServerStatus;

    private static CustomProgressDialog customProgressDialog;

    private final int PROGRESS = 0;

    private final int START = 0;
    private final int STOP = 1;

    private int up_down_status = 0;
    private int start_progress_cnt = 0;

    private static int already_sign = 0;

    static ContactDBHelper dbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);

   //     Log.d(TAG, " onCreate");
        // Views
        mStatusTextView = findViewById(R.id.status);
    //    mDetailTextView = findViewById(R.id.detail);

        // Button listeners
        findViewById(R.id.signInButton).setOnClickListener(this);
        findViewById(R.id.signOutButton).setOnClickListener(this);
   //     findViewById(R.id.disconnectButton).setOnClickListener(this);
        findViewById(R.id.uploadButton).setOnClickListener(this);
        findViewById(R.id.downloadButton).setOnClickListener(this);

        Intent intent = getIntent();

        db_paths.add(intent.getStringExtra("data"));
        db_paths.add(intent.getStringExtra("setting"));
        db_paths.add(intent.getStringExtra("memo"));

/*
        for (int i = 0; i < db_paths.size(); i++ ) {
            Log.d(TAG, "db_paths: " + db_paths.get(i));
        }
*/
        updateUI(null);

        customProgressDialog = new CustomProgressDialog(this, "처리 중 입니다.");

    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (already_sign == 1) {

            set_signclient();

            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
        }
    }
    // [END on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
   //     Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
               //             Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            already_sign = 1;
                        } else {
                            // If sign in fails, display a message to the user.
                    //        Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                            already_sign = 0;
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        set_signclient();

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);


    }
    // [END signin]

    private void set_signclient() {
        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("12658829674-k9vievmfc3to7urf8vs0gj7vhch64pta.apps.googleusercontent.com")
                .requestEmail()
                .requestScopes(Drive.SCOPE_APPFOLDER)
                .requestScopes(Drive.SCOPE_FILE)
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    @Override
    public void onStop() {
        db_paths.clear();
        server_status = 1;
        start_progress_cnt = 0;
        if (checkServerStatus != null) {
            checkServerStatus.interrupt();
            checkServerStatus = null;
        }
        super.onStop();
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
   //        mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.signInButton).setVisibility(View.GONE);
            findViewById(R.id.signOutAndDisconnect).setVisibility(View.VISIBLE);
            findViewById(R.id.uploadanddownload).setVisibility(View.VISIBLE);
        } else {
            if (already_sign == 0) {
           //     mStatusTextView.setText(R.string.signed_out);
            }
    //       mDetailTextView.setText(null);

            findViewById(R.id.signInButton).setVisibility(View.VISIBLE);
            findViewById(R.id.signOutAndDisconnect).setVisibility(View.GONE);
            findViewById(R.id.uploadanddownload).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        switch (i) {
            case R.id.signInButton:
                signIn();
                break;
            case R.id.signOutButton:
                signOut();
                already_sign = 0;
                mStatusTextView.setText("로그아웃");
                break;
                /*
            case R.id.disconnectButton:
                revokeAccess();
                already_sign = 0;
                mStatusTextView.setText("로그아웃");
                break;

                */
            case R.id.uploadButton:
                if (checkServerStatus == null) {
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(
                            PROGRESS,
                            START, 0), 0);
                    up_down_status = 0;
                    start_progress_cnt = 0;
                    checkServerStatus = new checkServerStatus();
                    checkServerStatus.start();
                } else {
                    Toast.makeText(this, "업로드 중입니다.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.downloadButton:
                if (checkServerStatus == null) {
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(
                            PROGRESS,
                            START, 0), 0);
                    up_down_status = 1;
                    start_progress_cnt = 0;
                    checkServerStatus = new checkServerStatus();
                    checkServerStatus.start();
                } else {
                    Toast.makeText(this, "다운로드 중입니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS:
                    switch (msg.arg1) {
                        case START:
                            customProgressDialog.setCanceledOnTouchOutside(false);
                            customProgressDialog.show();
                            break;
                        case STOP:
                            customProgressDialog.setCanceledOnTouchOutside(true);
                            customProgressDialog.dismiss();
                            start_progress_cnt = 0;
                            if (checkServerStatus != null) {
                                checkServerStatus.interrupt();
                                checkServerStatus = null;
                            }

                            if (up_down_status == 1) {
                                try {
                                    //load database in MainActivity
                                    MainActivity mainActivity = new MainActivity();
                                    if (mainActivity.load_values() == 0) {
                                        mainActivity.marker_changed = 1;
                                        Toast.makeText(getApplicationContext(), "완료 되었습니다.", Toast.LENGTH_SHORT).show();
                                    } else {

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            break;
                    }
                    break;
            }
        }
    };

    public class checkServerStatus extends Thread {
        public void run() {
            mHandler.post(new Runnable() {
                public void run() {
                }
            });
            while (true) {
                if (server_status == 1) {
                    if (start_progress_cnt > db_paths.size() - 1) {
                    //    Log.d(TAG, "file transfer finish");
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                PROGRESS,
                                STOP, 0), 0);

                        break;
                    }
                    server_status = 0;

                    if (up_down_status == 0) {
                        upload_data(db_paths.get(start_progress_cnt));
                    } else {
                        download_data(db_paths.get(start_progress_cnt));
                    }
                    start_progress_cnt++;

                } else {
            //        Log.d(TAG, "try...");
                }
                try {
                    Thread.sleep(200);   //sleep func이 있는 상태에서 interrupt를 발생시키면 Exception 발생
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public int download_data(String db_name) {
        int ret = 0;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference downloadRef = storageRef.child("database/" + db_name);

  //      Log.d(TAG, "getLastPathSegment: " + db_name);

        File local_file = new File(getApplicationContext().getDatabasePath(db_name).toString());

        Date lastModDate = new Date(local_file.lastModified());
  //      Log.d(TAG, "file path " + local_file.toString() + " update time " + lastModDate.toString());
        downloadRef.getFile(local_file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
     //           Log.d(TAG, "success to download db");
                server_status = 1;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
    //            Log.d(TAG, " fail to create local db " + exception.toString());
                server_status = 1;
            }
        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
    //            Log.d(TAG, "Download is " + progress + "% done");
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
     //           Log.d(TAG, "download canceled");
                server_status = 1;

            }
        }).addOnPausedListener(new OnPausedListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onPaused(FileDownloadTask.TaskSnapshot taskSnapshot) {
        //        Log.d(TAG, "download paused");
            }
        });

        return ret;
    }


    public int upload_data(String db_name) {
        int ret = 0;
        try {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

       //     Log.d(TAG, "getLastPathSegment: " + db_name);

            Uri upload_file_path = Uri.fromFile(getApplicationContext().getDatabasePath(db_name));

       //     Log.d(TAG, " new_path: " + upload_file_path);

            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("application/octet-stream")
                    .build();

 //           UploadTask uploadTask = uploadRef.putStream(stream);

            UploadTask uploadTask = storageRef.child("database/"+db_name).putFile(upload_file_path, metadata);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
             //       Log.d(TAG, "Fail to upload files");
                    server_status = 1;
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
              //      Log.d(TAG, "Success to upload files");
                    server_status = 1;
                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
              //      Log.d(TAG, "Canceled to upload files");
                    server_status = 1;
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                //    Log.d(TAG, "Paused to upload files");
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
             //       Log.d(TAG, "Upload is " + progress + "% done");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;

    }
}
