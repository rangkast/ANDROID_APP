package com.toadstudio.first.toadproject.CustomDialog;

/**
 * Created by rangkast.jeong on 2018-03-13.
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.toadstudio.first.toadproject.R;

/**
 * Created by charlie on 2017. 8. 18..
 */

public class CustomDialog_2 extends Dialog implements View.OnClickListener{

    private DialogListener_2 dialogListener;
    private static final int LAYOUT = R.layout.custom_dialog_2;
    private Context context;

    private TextView cancelTv;
    private TextView searchTv;
    private TextView mFail;

    private TextView mtextTitle;
    private TextView mtextQuestion;

    private String text_title;
    private String text_question;
    private String[] fail_reason;

    public CustomDialog_2(Context context,String[] fail_reason, String text_title, String text_question){
        super(context);
        this.context = context;
        this.text_title = text_title;
        this.text_question = text_question;
        this.fail_reason =fail_reason;
    }

    public void setDialogListener(DialogListener_2 dialogListener){
        this.dialogListener = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        mFail = (TextView)findViewById(R.id.custom_2_fail_reasons);
        searchTv = (TextView) findViewById(R.id.custom_2_ok);
        cancelTv = (TextView) findViewById(R.id.custom_2_cancel);

        mtextTitle = (TextView)findViewById(R.id.custom_2_text_title);
        mtextQuestion = (TextView)findViewById(R.id.custom_2_text_question);

        StringBuilder info = new StringBuilder("");
        for (int i =0; i < fail_reason.length; i++) {
            info.append(fail_reason[i] + "\n");
        }

        mFail.setText(info.toString());


        mtextTitle.setText(text_title);
        mtextQuestion.setText(text_question);
        cancelTv.setOnClickListener(this);
        searchTv.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.custom_2_cancel:
                dialogListener.onNegativeClicked();
                dismiss();
                break;

            case R.id.custom_2_ok:
                dialogListener.onPositiveClicked();
                dismiss();
                break;

        }
    }

}