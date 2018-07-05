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

package com.sky.android.common.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.sky.android.common.util.Alog;

/**
 * Created by sky on 16-12-2.
 */
public class ReceiverHelper {

    private Context mContext;
    private ReceiverCallback mCallback;
    private IntentFilter mIntentFilter;

    private HelperBroadcastReceiver mHelperBroadcastReceiver;

    public ReceiverHelper(Context context, ReceiverCallback callback, String... actions) {
        this(context, callback, buildIntentFilter(actions));
    }

    public ReceiverHelper(Context context, ReceiverCallback callback, IntentFilter intentFilter) {
        mContext = context.getApplicationContext();
        mCallback = callback;
        mIntentFilter = intentFilter;
    }

    public void registerReceiver() {

        if (mHelperBroadcastReceiver != null) return ;

        try {
            mHelperBroadcastReceiver = new HelperBroadcastReceiver();
            mContext.registerReceiver(mHelperBroadcastReceiver, mIntentFilter);
        } catch (Exception e) {
            Alog.e("Exception", e);
        }
    }

    public void unregisterReceiver() {

        if (mHelperBroadcastReceiver == null) return ;

        try {
            mContext.unregisterReceiver(mHelperBroadcastReceiver);
            mHelperBroadcastReceiver = null;
        } catch (Exception e) {
            Alog.e("Exception", e);
        }
    }

    public static IntentFilter buildIntentFilter(String... actions) {

        IntentFilter filter = new IntentFilter();

        if (actions == null || actions.length <= 0) {
            // 暂无
            return filter;
        }

        for (String action : actions) {
            // 添加Action
            filter.addAction(action);
        }

        return filter;
    }

    private final class HelperBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 直接回调出去就可以了
            if (mCallback != null) mCallback.onReceive(intent.getAction(), intent);
        }
    }

    public interface ReceiverCallback {

        void onReceive(String action, Intent intent);
    }
}
