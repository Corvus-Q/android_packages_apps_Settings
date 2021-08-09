/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.deviceinfo.firmwareversion;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.preference.Preference;

import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;

public class CrvsVersionPreferenceController extends BasePreferenceController {

    private static final Uri INTENT_URI_DATA = Uri.parse("https://github.com/Corvus-Q/");
    private static final String TAG = "crvsDialogCtrl";
    private static final String ROM_VERSION_PROP = "ro.du.build.version";
    private static final String ROM_RELEASETYPE_PROP = "ro.du.build.type";
    private static final String ROM_CODENAME_PROP = "ro.corvus.codename";
    private final PackageManager mPackageManager = this.mContext.getPackageManager();

    public CrvsVersionPreferenceController(Context context, String preferenceKey) {
        super(context, preferenceKey);
    }

    public int getAvailabilityStatus() {
        return AVAILABLE;
    }

    public CharSequence getSummary() {
        String crvsVersion = SystemProperties.get(ROM_VERSION_PROP,
                mContext.getString(R.string.device_info_default));
        String crvsReleasetype =  SystemProperties.get(ROM_RELEASETYPE_PROP,
                this.mContext.getString(R.string.device_info_default));
        String crvsCodename = SystemProperties.get(ROM_CODENAME_PROP,
                mContext.getString(R.string.device_info_default));
        if (!crvsVersion.isEmpty() && !crvsReleasetype.isEmpty())
            return crvsVersion + " | " + crvsCodename + " | " + crvsReleasetype;
        else
            return mContext.getString(R.string.crvs_version_default);
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            return false;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(INTENT_URI_DATA);
        if (this.mPackageManager.queryIntentActivities(intent, 0).isEmpty()) {
            Log.w(TAG, "queryIntentActivities() returns empty");
            return true;
        }
        this.mContext.startActivity(intent);
        return true;
    }
}
