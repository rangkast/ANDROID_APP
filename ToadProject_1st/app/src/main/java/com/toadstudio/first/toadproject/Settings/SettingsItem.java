package com.toadstudio.first.toadproject.Settings;

/**
 * Created by rangkast.jeong on 2018-03-16.
 */

public class SettingsItem {
    private String text_1;
    private String text_2;

    public String getText_1() {
        return text_1;
    }
    public String getText_2() {
        return text_2;
    }

    public SettingsItem(String text_1, String text_2) {
        this.text_1 = text_1;
        this.text_2 = text_2;
    }
}
