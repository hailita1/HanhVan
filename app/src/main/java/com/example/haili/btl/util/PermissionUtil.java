package com.example.haili.btl.util;

import android.content.Context;

import com.example.haili.btl.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

public final class PermissionUtil {
    public static final void checkPermission(Context context, PermissionListener permissionListener, String[] permissions, String msg) {
        new TedPermission(context)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(msg)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setGotoSettingButtonText(context.getString(R.string.setting))
                .setPermissions(permissions)
                .check();
    }
}
