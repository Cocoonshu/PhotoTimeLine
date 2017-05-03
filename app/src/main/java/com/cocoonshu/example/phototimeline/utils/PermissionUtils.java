package com.cocoonshu.example.phototimeline.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import com.cocoonshu.example.phototimeline.config.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Cocoonshu
 * @Date   2017-05-03
 */
public class PermissionUtils {

    public static boolean checkSelfPermissions(Context context) {
        for (String permission : Config.Permission.Permissions) {
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static void requestPermissions(Activity activity) {
        List<String> noGrantedPermissions = new ArrayList<>();
        for (String permission : Config.Permission.Permissions) {
            if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                noGrantedPermissions.add(permission);
            }
        }
        if (!noGrantedPermissions.isEmpty()) {
            activity.requestPermissions(noGrantedPermissions.toArray(new String[0]), Config.Permission.REQUEST_CODE_PERMISSIONS);
        }
    }

    public static void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Config.Permission.REQUEST_CODE_PERMISSIONS) {
            // Ignore for application necessary permissions,
            // just waiting for onResume invoking.
        }
    }
}
