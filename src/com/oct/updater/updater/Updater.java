/*
 * Copyright (C) 2013 OTAPlatform
 * Copyright (C) 2014 ProBAM ROM
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

package com.oct.updater.updater;

import java.io.Serializable;

import android.content.Context;

import com.oct.updater.updater.RomUpdater.RomUpdaterListener;
import com.oct.updater.updater.impl.GooUpdater;
import com.oct.updater.updater.impl.OUCUpdater;
import com.oct.updater.util.Constants;
import com.oct.updater.util.HttpStringReader.HttpStringReaderListener;
import com.oct.updater.util.URLStringReader.URLStringReaderListener;

public abstract class Updater implements URLStringReaderListener, HttpStringReaderListener {

    public interface PackageInfo extends Serializable {

        public String getMd5();
        public String getFilename();
        public String getPath();
        public String getFolder();
        public String getMessage(Context context);
        public long getVersion();
        public boolean isDelta();
        public String getDeltaFilename();
        public String getDeltaPath();
        public String getDeltaMd5();
        public boolean isGapps();
    }

    public static final String PROPERTY_DEVICE = "ro.cm.device";

    public static final int UPDATER_GOO = 0;
    public static final int UPDATER_OUC = 1;

    public static interface UpdaterListener {

        public void versionFound(PackageInfo info);

        public void versionError(String error);
    }

    public static int getRomType() {
        if (Constants.getProperty(GooUpdater.PROPERTY_GOO_DEVELOPER) != null
                && Constants.getProperty(GooUpdater.PROPERTY_GOO_ROM) != null
                && Constants.getProperty(GooUpdater.PROPERTY_GOO_VERSION) != null) {
            return UPDATER_GOO;
        }
        if (Constants.getProperty(OUCUpdater.PROPERTY_OTA_ID) != null
                && Constants.getProperty(OUCUpdater.PROPERTY_OTA_TIME) != null
                && Constants.getProperty(OUCUpdater.PROPERTY_OTA_VER) != null) {
            return UPDATER_OUC;
        }
        return -1;
    }
    
    public static RomUpdater getRomUpdater(Context context, RomUpdaterListener listener, boolean fromAlarm) {
        switch (getRomType()) {
            case UPDATER_GOO :
            case UPDATER_OUC :
                return new RomUpdater(context, listener, fromAlarm);
            default :
                return null;
        }
    }
    
    protected static Updater getRomUpdater(RomUpdater updater) {
        switch (getRomType()) {
            case UPDATER_GOO :
                return new GooUpdater(updater);
            case UPDATER_OUC :
                return new OUCUpdater(updater);
            default :
                return null;
        }
    }

    public abstract String getDeveloperId();

    public abstract String getName();

    public abstract int getVersion();

    public abstract void searchVersion();

    public abstract boolean isScanning();
}
