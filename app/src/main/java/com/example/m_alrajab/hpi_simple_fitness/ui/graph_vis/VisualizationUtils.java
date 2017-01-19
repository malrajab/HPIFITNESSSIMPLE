package com.example.m_alrajab.hpi_simple_fitness.ui.graph_vis;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for preparing the visualization
 * Created by m_alrajab on 1/17/17.
 */

public class VisualizationUtils {
    public static final String DATE_FORMAT_HPI = "MM-dd-yyyy";

    public static String getCurrentDateStamp() {
        return (new SimpleDateFormat(DATE_FORMAT_HPI)).format(new Date());
    }

    public static String getDateFormated(long end, int shift) {
        return new SimpleDateFormat(DATE_FORMAT_HPI).format(end
                - (shift - 1) * (24L * 3_600_000L));
    }

    public static final Uri getUriToResource(@NonNull Context context, @AnyRes int resId)
            throws Resources.NotFoundException {
        Resources res = context.getResources();
        Uri resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(resId)
                + '/' + res.getResourceTypeName(resId)
                + '/' + res.getResourceEntryName(resId));

        return resUri;
    }
}
