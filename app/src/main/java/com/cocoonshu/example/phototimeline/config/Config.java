package com.cocoonshu.example.phototimeline.config;

import android.app.Activity;
import android.content.Context;

import com.cocoonshu.example.phototimeline.R;

public class Config {

    public Config(Context context) {

    }

    public Theme getTheme() {
        throw new UnsupportedOperationException();
    }

    public static class Theme {

        private static final int R_StatusBarColor   = R.color.statusBarColor;
        private static final int R_LayoutBackground = R.color.layout_background;
        private static final int R_DividerLine      = R.color.divider_line;
        private static final int R_TextDark         = R.color.text_dark;
        private static final int R_TextLight        = R.color.text_light;
        private static final int R_TextSubLight     = R.color.text_sub_light;
        private static final int R_IconDark         = R.color.icon_dark;
        private static final int R_IconLight        = R.color.icon_light;

        public Theme(Activity activity) {
            throw new UnsupportedOperationException();
        }

        public int getStatusBarColor() {
            throw new UnsupportedOperationException();
        }

        public int getLayoutBackgroundColor() {
            throw new UnsupportedOperationException();
        }

        public int getDividerLineColor() {
            throw new UnsupportedOperationException();
        }

        public int getMainTextColor(boolean reverseColor) {
            throw new UnsupportedOperationException();
        }

        public int getSubTextColor(boolean reverseColor) {
            throw new UnsupportedOperationException();
        }

        public int getCommentTextColor(boolean reverseColor) {
            throw new UnsupportedOperationException();
        }

        public int getMainIconColor(boolean reverseColor) {
            throw new UnsupportedOperationException();
        }

        public int getSeconderyIconColor(boolean reverseColor) {
            throw new UnsupportedOperationException();
        }

        public int getHintColor() {
            throw new UnsupportedOperationException();
        }
    }
}
