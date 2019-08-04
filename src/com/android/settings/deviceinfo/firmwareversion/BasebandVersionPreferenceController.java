/*
 * Copyright (C) 2017 The Android Open Source Project
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
import android.os.SystemProperties;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.Utils;

public class BasebandVersionPreferenceController extends BasePreferenceController {

    static final String BASEBAND_PROPERTY = "gsm.version.baseband";

    public BasebandVersionPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return !Utils.isWifiOnly(this.mContext) ? 0 : 3;
    }

    public CharSequence getSummary() {
        String baseband = SystemProperties.get(BASEBAND_PROPERTY,
                this.mContext.getString(R.string.device_info_default));
        if (baseband.contains(",")){
          String[] baseband_parts = baseband.split(",");
          if (baseband_parts.length > 0 && baseband_parts[0].equals(baseband_parts[1])){
            baseband = baseband_parts[0];
          }
        }
        return baseband;
    }
}
