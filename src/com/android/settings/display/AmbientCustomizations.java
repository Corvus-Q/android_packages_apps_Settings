/**
 * Copyright (C) 2014 The Android Open Source Project
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
package com.android.settings.display;

import android.app.Activity;
import android.content.Context;
import android.content.ContentResolver;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.preference.SwitchPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import android.provider.SearchIndexableResource;
import android.provider.Settings;

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.android.settings.preference.CustomSeekBarPreference;
import com.android.settings.preference.SystemSettingListPreference;
import com.android.settings.preference.SystemSettingEditTextPreference;
import net.margaritov.preference.colorpicker.ColorPickerPreference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class AmbientCustomizations extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String AMBIENT_TEXT_STRING = "ambient_text_string";
    private static final String AMBIENT_TEXT_ALIGNMENT = "ambient_text_alignment";
    private static final String AMBIENT_TEXT_FONT = "ambient_text_font";
    private static final String AMBIENT_TEXT_TYPE_COLOR = "ambient_text_type_color";
    private static final String AMBIENT_TEXT_COLOR = "ambient_text_color";
    private static final String FILE_AMBIENT_SELECT = "file_ambient_select";

    private static final int REQUEST_PICK_IMAGE = 0;

    private SystemSettingEditTextPreference mAmbientText;
    private ListPreference mAmbientTextAlign;
    private ListPreference mAmbientTextFonts;
    private ListPreference mAmbientTextTypeColor;
    private ColorPickerPreference mAmbientTextColor;

    private Preference mAmbientImage;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.du_ambient_customization);
        final PreferenceScreen prefScreen = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();
        Resources resources = getResources();

        // set ambient text alignment
        mAmbientTextAlign = (ListPreference) findPreference(AMBIENT_TEXT_ALIGNMENT);
        int align = Settings.System.getInt(resolver,
                Settings.System.AMBIENT_TEXT_ALIGNMENT, 3);
        mAmbientTextAlign.setValue(String.valueOf(align));
        mAmbientTextAlign.setSummary(mAmbientTextAlign.getEntry());
        mAmbientTextAlign.setOnPreferenceChangeListener(this);

        // ambient text Fonts
        mAmbientTextFonts = (ListPreference) findPreference(AMBIENT_TEXT_FONT);
        mAmbientTextFonts.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.AMBIENT_TEXT_FONT, 8)));
        mAmbientTextFonts.setSummary(mAmbientTextFonts.getEntry());
        mAmbientTextFonts.setOnPreferenceChangeListener(this);

        // ambient text color type
        mAmbientTextTypeColor = (ListPreference) findPreference(AMBIENT_TEXT_TYPE_COLOR);
        mAmbientTextTypeColor.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.AMBIENT_TEXT_TYPE_COLOR, 0)));
        mAmbientTextTypeColor.setSummary(mAmbientTextTypeColor.getEntry());
        mAmbientTextTypeColor.setOnPreferenceChangeListener(this);

        mAmbientTextColor = (ColorPickerPreference) findPreference(AMBIENT_TEXT_COLOR);
        mAmbientTextColor.setOnPreferenceChangeListener(this);
        int ambientTextColor = Settings.System.getInt(getContentResolver(),
                Settings.System.AMBIENT_TEXT_COLOR, 0xFF3980FF);
        String ambientTextColorHex = String.format("#%08x", (0xFF3980FF & ambientTextColor));
        if (ambientTextColorHex.equals("#ff3980ff")) {
            mAmbientTextColor.setSummary(R.string.default_string);
        } else {
            mAmbientTextColor.setSummary(ambientTextColorHex);
        }
        mAmbientTextColor.setNewPreviewColor(ambientTextColor);

        mAmbientImage = findPreference(FILE_AMBIENT_SELECT);

    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference == mAmbientImage) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_PICK_IMAGE);
            return true;
        }
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mAmbientText) {
            String value = (String) newValue;
            Settings.System.putString(resolver,
                    Settings.System.AMBIENT_TEXT_STRING, value);
            return true;
        } else if (preference == mAmbientTextAlign) {
            int align = Integer.valueOf((String) newValue);
            int index = mAmbientTextAlign.findIndexOfValue((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.AMBIENT_TEXT_ALIGNMENT, align);
            mAmbientTextAlign.setSummary(mAmbientTextAlign.getEntries()[index]);
            return true;
        } else if (preference == mAmbientTextFonts) {
            Settings.System.putInt(getContentResolver(), Settings.System.AMBIENT_TEXT_FONT,
                    Integer.valueOf((String) newValue));
            mAmbientTextFonts.setValue(String.valueOf(newValue));
            mAmbientTextFonts.setSummary(mAmbientTextFonts.getEntry());
            return true;
        } else if (preference == mAmbientTextTypeColor) {
            Settings.System.putInt(getContentResolver(), Settings.System.AMBIENT_TEXT_TYPE_COLOR,
                    Integer.valueOf((String) newValue));
            mAmbientTextTypeColor.setValue(String.valueOf(newValue));
            mAmbientTextTypeColor.setSummary(mAmbientTextTypeColor.getEntry());
            return true;
        } else if (preference == mAmbientTextColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            if (hex.equals("#ff3980ff")) {
                preference.setSummary(R.string.default_string);
            } else {
                preference.setSummary(hex);
            }
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.AMBIENT_TEXT_COLOR, intHex);
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == REQUEST_PICK_IMAGE) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            final Uri imageUri = result.getData();
            Settings.System.putString(getContentResolver(), Settings.System.AMBIENT_CUSTOM_IMAGE, imageUri.toString());
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.DIRTYTWEAKS;
    }
}
