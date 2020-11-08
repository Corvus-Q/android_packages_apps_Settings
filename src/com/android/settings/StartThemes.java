/*
 * Copyright (C) 2020 CorvusROM
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

package com.android.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;

import com.android.settings.R;
import com.android.settings.dashboard.SummaryLoader;
import com.android.settings.SettingsPreferenceFragment;

public class StartThemes extends SettingsPreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startThemes();
        finish();
    }

    private void startThemes(){
        Intent themesExtrasIntent = new Intent();
        themesExtrasIntent.setClassName(
                "com.corvus.themes", "com.corvus.themes.MainActivity");
        startActivity(themesExtrasIntent);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.CORVUS;
    }

    private static class SummaryProvider implements SummaryLoader.SummaryProvider {

        public SummaryProvider(Context context, SummaryLoader summaryLoader) {
        }

        @Override
        public void setListening(boolean listening) {
            if (listening) {
            }
        }
    }

    public static final SummaryLoader.SummaryProviderFactory SUMMARY_PROVIDER_FACTORY
            = new SummaryLoader.SummaryProviderFactory() {
        @Override
        public SummaryLoader.SummaryProvider createSummaryProvider(Activity activity,
                                                                   SummaryLoader summaryLoader) {
            return new SummaryProvider(activity, summaryLoader);
        }
    };
}
