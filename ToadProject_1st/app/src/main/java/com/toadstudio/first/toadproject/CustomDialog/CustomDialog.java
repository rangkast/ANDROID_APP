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

public class CustomDialog extends Dialog implements View.OnClickListener{

    private MyDialogListener dialogListener;
    private static final int LAYOUT = R.layout.custom_dialog_category;
    private Context context;

  //  private TextView cancelTv;
    private TextView searchTv;
    private TextView delete;
    private TextView mFail;

    private TextView mtextTitle;
    private TextView mtextQuestion;

    private String text_title;
    private String text_question;
    private String[] fail_reason;

    public CustomDialog(Context context,String[] fail_reason, String text_title, String text_question){
        super(context);
        this.context = context;
        this.text_title = text_title;
        this.text_question = text_question;
        this.fail_reason =fail_reason;
    }

    public void setDialogListener(MyDialogListener dialogListener){
        this.dialogListener = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        mFail = (TextView)findViewById(R.id.fail_reasons);
        searchTv = (TextView) findViewById(R.id.findPwDialogFindTv);
//        cancelTv = (TextView) findViewById(R.id.findPwDialogCancelTv);
        delete = (TextView) findViewById(R.id.delete);

        mtextTitle = (TextView)findViewById(R.id.text_title);
        mtextQuestion = (TextView)findViewById(R.id.text_question);

        StringBuilder info = new StringBuilder("");
        for (int i =0; i < fail_reason.length; i++) {
            info.append(fail_reason[i] + "\n");
        }
        mFail.setText(info.toString());

        mtextTitle.setText(text_title);
        mtextQuestion.setText(text_question);
//        cancelTv.setOnClickListener(this);
        searchTv.setOnClickListener(this);
        delete.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*
            case R.id.findPwDialogCancelTv:
                dialogListener.onNegativeClicked();
                dismiss();
                break;
                */

            case R.id.findPwDialogFindTv:
                dialogListener.onPositiveClicked();
                dismiss();
                break;

            case R.id.delete:
                dialogListener.onDeleteClicked();
                dismiss();
                break;
        }
    }

}