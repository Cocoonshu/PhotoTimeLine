package com.cocoonshu.example.phototimeline.config;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;

import com.cocoonshu.example.phototimeline.R;
import com.cocoonshu.example.phototimeline.utils.Debugger;

/**
 * @Author Cocoonshu
 * @Date   2017-05-02
 */
public class Config {

    private Theme mTheme = new Theme();

    public Config(Context context) {

    }

    public void updateApplicationConfig(Context context) {
        // TODO
    }

    public void updateActivityConfig(Activity activity) {
        mTheme.update(activity);
    }

    public Theme getTheme() {
        throw new UnsupportedOperationException();
    }

    public boolean isSame(Activity activity) {
        return mTheme != null ? mTheme.isSame(activity) : false;
    }

    public static class Theme {
        private static final String TAG = "Theme";

        private static final int R_StatusBarColor   = R.color.statusBarColor;
        private static final int R_LayoutBackground = R.color.layout_background;
        private static final int R_DividerLine      = R.color.divider_line;
        private static final int R_TextDark         = R.color.text_dark;
        private static final int R_TextLight        = R.color.text_light;
        private static final int R_TextSubLight     = R.color.text_sub_light;
        private static final int R_IconDark         = R.color.icon_dark;
        private static final int R_IconLight        = R.color.icon_light;

        private ColorStateList mStatusBarColor   = null;
        private ColorStateList mLayoutBackground = null;
        private ColorStateList mDividerLine      = null;
        private ColorStateList mTextDark         = null;
        private ColorStateList mTextLight        = null;
        private ColorStateList mTextSubLight     = null;
        private ColorStateList mIconDark         = null;
        private ColorStateList mIconLight        = null;
        private int            mActivityHash     = 0;

        public Theme() {

        }

        public void update(Activity activity) {
            Debugger.i(TAG, "[update] update activity theme configurations");
            Resources resources = activity.getResources();
            mStatusBarColor   = resources.getColorStateList(R_StatusBarColor);
            mLayoutBackground = resources.getColorStateList(R_LayoutBackground);
            mDividerLine      = resources.getColorStateList(R_DividerLine);
            mTextDark         = resources.getColorStateList(R_TextDark);
            mTextLight        = resources.getColorStateList(R_TextLight);
            mTextSubLight     = resources.getColorStateList(R_TextSubLight);
            mIconDark         = resources.getColorStateList(R_IconDark);
            mIconLight        = resources.getColorStateList(R_IconLight);
            mActivityHash     = activity.hashCode();
        }

        public boolean isSame(Activity activity) {
            return mActivityHash == activity.hashCode();
        }

        public ColorStateList getStatusBarColor() {
            return mStatusBarColor;
        }

        public ColorStateList getLayoutBackgroundColor() {
            return mLayoutBackground;
        }

        public ColorStateList getDividerLineColor() {
            return mDividerLine;
        }

        public ColorStateList getMainTextColor(boolean reverseColor) {
            return mTextDark;
        }

        public ColorStateList getSubTextColor(boolean reverseColor) {
            return mTextSubLight;
        }

        public ColorStateList getCommentTextColor(boolean reverseColor) {
            return mTextLight;
        }

        public ColorStateList getMainIconColor(boolean reverseColor) {
            return mIconDark;
        }

        public ColorStateList getSeconderyIconColor(boolean reverseColor) {
            return mIconLight;
        }

        public ColorStateList getHintColor() {
            throw new UnsupportedOperationException();
        }
    }
}
