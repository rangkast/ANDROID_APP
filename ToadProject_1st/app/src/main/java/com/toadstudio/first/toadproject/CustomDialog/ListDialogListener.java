package com.toadstudio.first.toadproject.CustomDialog;

public interface ListDialogListener {
    int onPositiveClicked(Integer status, String path);
    void onNegativeClicked(Integer status);
}