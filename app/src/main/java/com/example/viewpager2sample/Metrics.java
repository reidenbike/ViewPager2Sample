package com.example.viewpager2sample;

import android.content.Context;
import java.util.ArrayList;

class Metrics extends Type
{
    private static final ArrayList<Metrics> metricList = new ArrayList<>();
    static final String DISPLAY_PREFS = "display_config";
    static final String NUMBER_FRAGS = "number_frags";
    static final String DISPLAY = "display_";

    private Metrics(int metricNumber, int label, int units, int units_inline, int units_metric, int units_metric_inline, boolean gpsDependent)
    {
        super(metricNumber, label, units, units_inline, units_metric, units_metric_inline, gpsDependent);
        metricList.add(this);
    }
    static final Metrics SPEED   = new Metrics(0,R.string.current_speed,R.string.mph_stacked,R.string.mph_inline,R.string.kph_stacked,R.string.kph,true);
    static final Metrics SPEED_AVERAGE  = new Metrics(1,R.string.averageSpeed,R.string.mph_stacked,R.string.mph_inline,R.string.kph_stacked,R.string.kph,true);
    static final Metrics DISTANCE = new Metrics(2,R.string.distance,R.string.mi_stacked,R.string.mi_inline,R.string.km_stacked,R.string.km_inline,true);
    static final Metrics ELEVATION_GAIN = new Metrics(3,R.string.elevationGain,R.string.ft_stacked,R.string.ft_inline,R.string.m_inline,R.string.m_inline,true);
    static final Metrics ELEVATION_LOSS = new Metrics(4,R.string.elevationLoss,R.string.ft_stacked,R.string.ft_inline,R.string.m_inline,R.string.m_inline,true);
    static final Metrics ALTITUDE = new Metrics(5,R.string.altitude,R.string.ft_stacked,R.string.ft_inline,R.string.m_inline,R.string.m_inline,true);
    static final Metrics PERCENT_GRADE = new Metrics(6,R.string.percent_grade,R.string.percent_inline,R.string.percent_inline,R.string.percent_inline,R.string.percent_inline,true);
    static final Metrics POWER = new Metrics(7,R.string.current_power,R.string.w_inline,R.string.w_inline,R.string.w_inline,R.string.w_inline,false);
    static final Metrics POWER_AVERAGE = new Metrics(8,R.string.averagePower,R.string.w_inline,R.string.w_inline,R.string.w_inline,R.string.w_inline,true);
    static final Metrics POWER_5_SEC = new Metrics(9,R.string.five_s_power,R.string.w_inline,R.string.w_inline,R.string.w_inline,R.string.w_inline,true);
    static final Metrics POWER_30_SEC = new Metrics(10,R.string.thirty_s_power,R.string.w_inline,R.string.w_inline,R.string.w_inline,R.string.w_inline,true);
    static final Metrics POWER_5_MIN = new Metrics(11,R.string.five_min_power,R.string.w_inline,R.string.w_inline,R.string.w_inline,R.string.w_inline,true);

    static String[] getLabelList(Context context){
        ArrayList<String> labelListString = new ArrayList<>();
        for (Metrics metrics : metricList){
            labelListString.add(metrics.getLabel(context));
        }
        return labelListString.toArray(new String[metricList.size()]);
    }

    static Metrics findByMetricNumber (int metricNumber){
        return metricList.get(metricNumber);
    }
}

class Type implements java.io.Serializable
{
    private int metricNumber;
    private int label;
    private int units;
    private int units_inline;
    private int units_metric;
    private int units_metric_inline;
    private boolean gpsDependent;

    //private static final Hashtable types = new Hashtable();

    Type(int metricNumber, int label, int units, int units_inline, int units_metric, int units_metric_inline, boolean gpsDependent)
    {
        this.metricNumber = metricNumber;
        this.label = label;
        this.units = units;
        this.units_inline = units_inline;
        this.units_metric = units_metric;
        this.units_metric_inline = units_metric_inline;
        this.gpsDependent = gpsDependent;
    }

    int getMetricNumber()
    {
        return metricNumber;
    }

    String getLabel(Context context)
    {
        return context.getString(label);
    }

    String getUnits(Context context, boolean inline, boolean metric)
    {
        if (inline) {
            return context.getString((metric) ? units_metric_inline : units_inline);
        } else {
            return context.getString((metric) ? units_metric : units);
        }
    }

    boolean isGpsDependent() {
        return gpsDependent;
    }
}
