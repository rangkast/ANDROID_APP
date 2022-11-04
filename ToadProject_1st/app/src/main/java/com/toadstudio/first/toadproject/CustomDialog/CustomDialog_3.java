package com.toadstudio.first.toadproject.CustomDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.TextView;

import com.toadstudio.first.toadproject.R;

public class CustomDialog_3 extends Dialog implements View.OnClickListener{

    private DialogListener_3 dialogListener;

    private static final int LAYOUT = R.layout.custom_dialog_3;

    private Context context;

    private TextInputEditText nameEt;
    private TextInputEditText emailEt;

    private TextView cancelTv;
    private TextView searchTv;
    private TextView search;

    public CustomDialog_3(Context context) {
        super(context);
        this.context = context;
    }

    public void setDialogListener(DialogListener_3 dialogListener){
        this.dialogListener = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        emailEt = (TextInputEditText) findViewById(R.id.findPwDialogEmailEt);
        cancelTv = (TextView) findViewById(R.id.findPwDialogCancelTv);
        searchTv = (TextView) findViewById(R.id.findPwDialogFindTv);
        search = (TextView) findViewById(R.id.search);

        cancelTv.setOnClickListener(this);
        searchTv.setOnClickListener(this);
        search.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.findPwDialogCancelTv:
                cancel();
                break;
            case R.id.findPwDialogFindTv:
                String path = emailEt.getText().toString();
                dialogListener.onPositiveClicked(path);
                dismiss();
                break;

            case R.id.search:
                String path_for_search = emailEt.getText().toString();
                dialogListener.onSearchClicked(path_for_search);
                dismiss();
                break;
        }
    }

}
