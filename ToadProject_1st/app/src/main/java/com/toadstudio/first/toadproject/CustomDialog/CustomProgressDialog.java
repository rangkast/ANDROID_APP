package com.toadstudio.first.toadproject.CustomDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.toadstudio.first.toadproject.R;

public class CustomProgressDialog extends Dialog
{
    private TextView textView;
    private TextView textView_percent;
    private ImageView imageView;
//    private static ProgressBar progressBar;
    private String text;
    private Context context;
    private int progress_max;
    public CustomProgressDialog(Context context, String text)
    {
        super(context);
        this.text = text;
        this.context = context;
    //    this.progress_max = progressBar_max;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // No Title
        setContentView(R.layout.custom_progress);

        imageView = (ImageView) findViewById(R.id.custom_progressBar_img);
        textView = (TextView) findViewById(R.id.custom_progressBar_text);
  //      textView_percent = (TextView) findViewById(R.id.custom_progressBar_percent_text);
   //     progressBar = (ProgressBar) findViewById(R.id.custom_progressBar);
   //     progressBar.setMax(100);
    //    progressBar.setMin(0);
    //    progressBar.setBackgroundResource(R.color.transparent);
        textView.setText(text);
    //    textView_percent.setText("");

                Glide   .with(context)
                        .load(R.drawable.camera)
                        .into(imageView);



    }

    public void setProgressBar(int set_progress) {

  //      progressBar.setProgress(set_progress);

    }

    public void setPercentage(int percentage) {
    //    textView_percent.setText(" " + percentage + "% ");
    }
}
