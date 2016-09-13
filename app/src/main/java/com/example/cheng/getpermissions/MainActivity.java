package com.example.cheng.getpermissions;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            getPermissions();
        } catch (PackageManager.NameNotFoundException e) {
            System.out.print("Packagename is not founded!!");
        }
    }

    /**
     * 获取权限
     *
     * @throws PackageManager.NameNotFoundException
     */
    protected void getPermissions() throws PackageManager.NameNotFoundException {
        /*final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List pkgAppsList = getPackageManager().queryIntentActivities(mainIntent, 0);*/


        PackageManager packageManager = this.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        List allPackageNameList = new ArrayList(); // 所有的包名
        List systemPackageNameList = new ArrayList(); //系统自带app的包名
        List userPackageNameList = new ArrayList();   //用户安装app的包名


        for (PackageInfo packageInfo : packageInfoList) {
            ApplicationInfo appInfo = packageInfo.applicationInfo;

            allPackageNameList.add(appInfo.packageName); // 获取全部包名
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
                systemPackageNameList.add(appInfo.packageName); // 系统自带程序包名
            } else {
                userPackageNameList.add(appInfo.packageName); // 用户安装程序包名
            }
        }
        //Log.i("name:", allPackageNameList.toString());

        String pName;
        String[] permissions;

        for (Object pkgName : allPackageNameList) { //遍历包名
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
                permissionString += permission + "; ";
            }
            String appPermission = pName + " has permission: " + permissionString + "\n";
            //Log.i("msg", appPermission);
        }
        Map allAppsPermissionsMap = getAppsPermissionsMap(allPackageNameList, packageManager);
        Map systemAppsPermissionsMap = getAppsPermissionsMap(systemPackageNameList, packageManager); //系统应用的权限
        Map userAppsPermissionsMap = getAppsPermissionsMap(userPackageNameList, packageManager); // 用户安装应用的权限

        for (Object obj : userAppsPermissionsMap.keySet()) { // 输出权限
            String key = (String) obj;
            String value = (String) userAppsPermissionsMap.get(key);
            Log.i("userAP",key+"has permissions:"+value);
        }


        /*
       for (int i = 0; i < allPackageNameList.size(); i++) {
            String permissionString = "";
            pName = allPackageNameList.get(i).toString();
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
        }
        */
    }

    /**
     * 根据不同的packageName列表获取不同的app权限，由getPermissions()调用。
     * 为了以后写控件调用函数使用。
     */
    protected Map getAppsPermissionsMap(List packageName, PackageManager packageManager) {
        String pName;
        String[] permissions = null;
        String appPermissionStr = "";
        Map permissionsMap = new HashMap();
        for (Object pkgName : packageName) { //遍历包名
            String permissionStr = ""; //输出的权限字符串
            pName = pkgName.toString();
            // Log.i("name", pName);
            //根据包名获取该包的权限集
            try {
                permissions = packageManager.getPackageInfo(pName, packageManager.GET_PERMISSIONS).requestedPermissions;
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("name error", "Name not found!!!");
            }
            if (permissions == null) { //有可能某些包并没有申请权限，主要是系统包，需要进行判断
                continue;
            }
            for (String permission : permissions) { //遍历权限
                //permissionList.add(permission);
                permissionStr += permission + "; ";
            }
            appPermissionStr = pName + " has permission: " + permissionStr + "\n";
            //Log.i("permissions", appPermissionStr);
            permissionsMap.put(pName, permissionStr);
        }
        return permissionsMap;
    }
}