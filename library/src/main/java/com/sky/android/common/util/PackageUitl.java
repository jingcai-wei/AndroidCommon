/*
 * Copyright (c) 2018 The sky Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sky.android.common.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Created by sky on 16-12-1.
 */
public final class PackageUitl {

    private static final String TAG = "PackageUtil";

    public static String getSerialNumber(Context context, String packageName) {

        if (context == null || TextUtils.isEmpty(packageName)) return null;

        try {
            PackageInfo packageInfo = getPackageInfo(
                    context, packageName, PackageManager.GET_SIGNATURES);

            if (packageInfo == null) return null;

            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];

            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate)
                    certFactory.generateCertificate(new ByteArrayInputStream(sign.toByteArray()));

            return certificate.getSerialNumber().toString();
        } catch (Exception e) {
            Alog.e(TAG, "获取包签名异常", e);
        }
        return null;
    }

    /**
     * 获取指定包的版本名
     * @param context
     * @param packageName
     * @return
     */
    public static String getVersionName(Context context, String packageName) {

        PackageInfo packageInfo = getPackageInfo(context, packageName, 0);

        return packageInfo != null ? packageInfo.versionName : null;
    }

    /**
     * 获取指定包的版本号
     * @param context
     * @param packageName
     * @return
     */
    public static int getVersionCode(Context context, String packageName) {

        PackageInfo packageInfo = getPackageInfo(context, packageName, 0);

        return packageInfo != null ? packageInfo.versionCode : 0;
    }

    /**
     * 获取当前应用的版本名
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        return getVersionName(context, context != null ? context.getPackageName() : null);
    }

    /**
     * 获取当前应用的版本号
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        return getVersionCode(context, context != null ? context.getPackageName() : null);
    }

    /**
     * 返回指定的包是否安装
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isInstall(Context context, String packageName) {
        return getPackageInfo(context, packageName, 0) != null;
    }

    /**
     * 获取简单的包信息
     * @param context
     * @param packageName
     * @return
     */
    public static SimplePackageInfo getSimplePackageInfo(Context context, String packageName) {

        PackageInfo packageInfo = getPackageInfo(context, packageName, 0);

        return packageInfo != null ? new SimplePackageInfo(
                packageName, packageInfo.versionName, packageInfo.versionCode) : null;
    }

    /**
     * 获取指定包信息
     * @param context
     * @param packageName
     * @param flag
     * @return
     */
    public static PackageInfo getPackageInfo(Context context, String packageName, int flag) {

        if (context == null || TextUtils.isEmpty(packageName)) return null;

        try {
            PackageManager manager = context.getPackageManager();
            return manager.getPackageInfo(packageName, flag);
        } catch (PackageManager.NameNotFoundException e) {
            Alog.e(TAG, "获取的包不存在", e);
        }
        return null;
    }

    public static void enterAppDetailsSettings(Context context, String packageName) {

        if (context == null || TextUtils.isEmpty(packageName)) return ;

        try {
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
            context.startActivity(intent);
        } catch (Exception e) {
            Alog.e(TAG,"跳转应用详情异常", e);
        }
    }

    public static class SimplePackageInfo implements Serializable {

        public final String packageName;
        public final String versionName;
        public final int versionCode;

        SimplePackageInfo(String packageName, String versionName, int versionCode) {
            this.packageName = packageName;
            this.versionName = versionName;
            this.versionCode = versionCode;
        }
    }
}
