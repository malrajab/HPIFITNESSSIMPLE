package com.example.m_alrajab.hpi_simple_fitness.ui.graph_vis;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by m_alrajab on 1/17/17.
 */

public class VisualizationUtils {
    public static final String DATE_FORMAT_HPI = "MM-dd-yyyy";

    public static String getCurrentDateStamp() {
        return (new SimpleDateFormat(DATE_FORMAT_HPI)).format(new Date());
    }

    public static String getDateFormated(long end, int shift) {
        return new SimpleDateFormat(DATE_FORMAT_HPI).format(end - (shift - 1) * (24L * 3_600_000L));
    }
}
