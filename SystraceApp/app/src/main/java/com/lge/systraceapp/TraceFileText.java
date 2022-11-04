package com.lge.systraceapp;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.lge.systraceapp.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;

public class TraceFileText extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {


    TextView textView_log;
    static String intent_path = null;
    BufferedReader bufferedReader;
    StringBuffer stringBuffer;
    String str,line;

    EditText editText;
    TextView textView, scrollableText;
    Button button;
    ScrollView textViewWrapper;

    private static final String TAG = "SystraceApp:Log";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trace_file_text);


        final Intent intent = getIntent();
        intent_path = intent.getStringExtra("path");
        textView_log = (TextView) findViewById(R.id.textview_trace);
    //    textView_log.setMovementMethod(new ScrollingMovementMethod());

    //    scrollableText = (TextView) findViewById(R.id.scrollableText);
        editText = (EditText) findViewById(R.id.editText);
        textViewWrapper = (ScrollView) findViewById(R.id.textViewWrapper);
        button = (Button) findViewById(R.id.button);

/*
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                Log.d(TAG, "onTouch" + event.getToolType(0));
                return false;
            }
        });
*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String criteria = editText.getText().toString();
                String fullText = textView_log.getText().toString();
                if (criteria.length() > 0) {
                    Log.d(TAG, "[" + criteria + "]");
                    if (fullText.contains(criteria)) {
                        int indexOfCriteria = fullText.indexOf(criteria);
                        int lineNumber = textView_log.getLayout().getLineForOffset(indexOfCriteria);
                        String highlighted = "<font color='red'>" + criteria + "</font>";
                        fullText = fullText.replace(criteria, highlighted);
                        textView_log.setText(Html.fromHtml(fullText));

                        textViewWrapper.scrollTo(0, textView_log.getLayout().getLineTop(lineNumber));
                    } else {
                        showToast(getApplication(), "cannnot find");
                    }
                } else {
                    Log.d(TAG, "length is 0");
                    showToast(getApplication(), "Set the data");
                }
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    if (scrollableText != null) {
                        String fullText = scrollableText.getText().toString();
                        fullText = fullText.replace("<font color='red'>", "");
                        fullText = fullText.replace("</font>", "");
                        scrollableText.setText(fullText);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        stringBuffer = new StringBuffer();
        try {
            Log.d(TAG, "try to read: " + intent_path);

            bufferedReader = new BufferedReader(new FileReader(intent_path));

            while (true) {
                line = bufferedReader.readLine();

                if (line == null) {
                    Log.d(TAG, "Read break");
                    break;
                }

        //        Log.d(TAG, line);

                stringBuffer = stringBuffer.append(line).append("\n");


            }
            str = stringBuffer.toString();
            textView_log.setText(str);



            str = null;
            stringBuffer.setLength(0);
            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showToast(final Application application, final String msg) {
        Handler h = new Handler(application.getMainLooper());
        h.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(application, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

        Log.d(TAG, "onClick");



    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(TAG, "Motion event");
        return false;
    }
}
