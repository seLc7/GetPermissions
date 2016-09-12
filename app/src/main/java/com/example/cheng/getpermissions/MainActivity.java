package com.example.cheng.getpermissions;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            getPermissionsList();
        } catch (PackageManager.NameNotFoundException e) {
            System.out.print("Packagename is not founded!!");
        }
    }

    protected void getPermissionsList() throws PackageManager.NameNotFoundException {
        /*final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List pkgAppsList = getPackageManager().queryIntentActivities(mainIntent, 0);*/


        PackageManager packageManager = this.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        List packageName = new ArrayList();

        for (PackageInfo packageInfo : packageInfoList) {
            packageName.add(packageInfo.applicationInfo.packageName); // 获取包名
        }
        Log.i("name:", packageName.toString());

        String pName;
        String[] permissions = null;

        for (Object pkgName : packageName) { //遍历包名
            String permissionString = ""; //输出的权限字符串
            pName = pkgName.toString();
            // Log.i("name", pName);
            //根据包名获取该包的权限集
            permissions = packageManager.getPackageInfo(pName, packageManager.GET_PERMISSIONS).requestedPermissions;
            if (permissions == null) { //有可能某些包并没有申请权限，主要是系统包，需要进行判断
                continue;
            }
            for (String permission : permissions) { //遍历权限
                //permissionList.add(permission);
                permissionString += permission;
            }
            String appPermission = pName + " has permission: " + permissionString + "\n";
            Log.i("msg", appPermission);
        }
       /*for (int i = 0; i < packageName.size(); i++) {
            String permissionString = "";
            pName = packageName.get(i).toString();
            permissions = packageManager.getPackageInfo(pName, packageManager.GET_PERMISSIONS).requestedPermissions;
            Log.i("length:", permissions.length + "");

            for (int j = 0; j < permissions.length; j++) {
                Log.i("j:", j + "");
                permissionString += permissions[j];
            }
            List permissionList = new ArrayList();
            for (String permission : permissions) {
                //permissionList.add(permission);
                permissionString += permission;
            }

            String appPermission = pName + " has permission: " + permissionString + "\n";
            Log.i("msg", appPermission);
        }*/
    }
}