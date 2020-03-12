package com.dlanuara.ejercicioc.utilities;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;

public final class JsonUtility {
    public static String[] jsonArrayParse(final @NonNull JSONArray array) throws JSONException {
        String[] ret = new String[array.length()];
        for(int i=0; i<ret.length; ++i)
            ret[i] = array.getString(0);
        return ret;
    }
}
