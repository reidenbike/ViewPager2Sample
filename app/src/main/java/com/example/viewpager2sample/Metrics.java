package com.example.viewpager2sample;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Hashtable;

public class Metrics extends Type
{
    private static final ArrayList<Metrics> metricList = new ArrayList<>();
    static final String DISPLAY_PREFS = "display_config";
    static final String NUMBER_FRAGS = "number_frags";
    static final String DISPLAY = "display_";

    protected Metrics(int metricNumber, int label, int units, int units_inline, int units_metric, int units_metric_inline, boolean gpsDependent)
    {
        super(metricNumber, label, units, units_inline, units_metric, units_metric_inline, gpsDependent);
        metricList.add(this);
    }
    public static final Metrics SPEED   = new Metrics(0,R.string.current_speed,R.string.mph_stacked,R.string.mph_inline,R.string.kph_stacked,R.string.kph,true);
    public static final Metrics SPEED_AVERAGE  = new Metrics(1,R.string.averageSpeed,R.string.mph_stacked,R.string.mph_inline,R.string.kph_stacked,R.string.kph,true);
    public static final Metrics DISTANCE = new Metrics(2,R.string.distance,R.string.mi_stacked,R.string.mi_inline,R.string.km_stacked,R.string.km_inline,true);
    public static final Metrics ELEVATION_GAIN = new Metrics(3,R.string.elevationGain,R.string.ft_stacked,R.string.ft_inline,R.string.m_inline,R.string.m_inline,true);
    public static final Metrics ELEVATION_LOSS = new Metrics(4,R.string.elevationLoss,R.string.ft_stacked,R.string.ft_inline,R.string.m_inline,R.string.m_inline,true);
    public static final Metrics ALTITUDE = new Metrics(5,R.string.altitude,R.string.ft_stacked,R.string.ft_inline,R.string.m_inline,R.string.m_inline,true);
    public static final Metrics PERCENT_GRADE = new Metrics(6,R.string.percent_grade,R.string.percent_inline,R.string.percent_inline,R.string.percent_inline,R.string.percent_inline,true);
    public static final Metrics POWER = new Metrics(7,R.string.current_power,R.string.w_inline,R.string.w_inline,R.string.w_inline,R.string.w_inline,false);
    public static final Metrics POWER_AVERAGE = new Metrics(8,R.string.averagePower,R.string.w_inline,R.string.w_inline,R.string.w_inline,R.string.w_inline,true);
    public static final Metrics POWER_5_SEC = new Metrics(9,R.string.five_s_power,R.string.w_inline,R.string.w_inline,R.string.w_inline,R.string.w_inline,true);
    public static final Metrics POWER_30_SEC = new Metrics(10,R.string.thirty_s_power,R.string.w_inline,R.string.w_inline,R.string.w_inline,R.string.w_inline,true);
    public static final Metrics POWER_5_MIN = new Metrics(11,R.string.five_min_power,R.string.w_inline,R.string.w_inline,R.string.w_inline,R.string.w_inline,true);

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

    protected Type(int metricNumber, int label, int units, int units_inline, int units_metric, int units_metric_inline, boolean gpsDependent)
    {
        this.metricNumber = metricNumber;
        this.label = label;
        this.units = units;
        this.units_inline = units_inline;
        this.units_metric = units_metric;
        this.units_metric_inline = units_metric_inline;
        this.gpsDependent = gpsDependent;

        //storeType(this);
        //types.put(metricNumber, this );

        //Log.i("HashTable",types.toString());
    }

    public int getMetricNumber()
    {
        return metricNumber;
    }

    public String getLabel(Context context)
    {
        return context.getString(label);
    }

    public String getUnits(Context context, boolean inline, boolean metric)
    {
        if (inline) {
            return context.getString((metric) ? units_metric_inline : units_inline);
        } else {
            return context.getString((metric) ? units_metric : units);
        }
    }

    public boolean isGpsDependent() {
        return gpsDependent;
    }

    /*private void storeType( Type type )
    {
        String metricsTable = "metricsTable";

        Hashtable values;

        synchronized( types ) // avoid race condition for creating inner table
        {
            values = (Hashtable) types.get( metricsTable );

            if( values == null )
            {
                values = new Hashtable();
                types.put( metricsTable, values );
            }
        }

        values.put(type.getMetricNumber(), type );
    }*/

    /*public static Type getByMetricNumber(int metricNumber )
    {
        *//*Type type = null;

        String metricsTable = "metricsTable";

        Hashtable values = (Hashtable) types.get( metricsTable );

        if( values != null )
        {
            type = (Type) values.get(metricNumber);
        }
        return( type );*//*
        return (Type) types.get(metricNumber);
    }*/
}

/*public enum Metrics {
    SPEED           (0,     R.string.current_speed,    R.string.mph_stacked,       R.string.mph_inline,        R.string.kph_stacked,       R.string.kph,           true),
    SPEED_AVERAGE   (1,     R.string.averageSpeed,     R.string.mph_stacked,       R.string.mph_inline,        R.string.kph_stacked,       R.string.kph,           true),
    DISTANCE        (2,     R.string.distance,         R.string.mi_stacked,        R.string.mi_inline,         R.string.km_stacked,        R.string.km_inline,     true),
    ELEVATION_GAIN  (3,     R.string.elevationGain,    R.string.ft_stacked,        R.string.ft_inline,         R.string.m_inline,          R.string.m_inline,      true),
    ELEVATION_LOSS  (4,     R.string.elevationLoss,    R.string.ft_stacked,        R.string.ft_inline,         R.string.m_inline,          R.string.m_inline,      true),
    ALTITUDE        (5,     R.string.altitude,         R.string.ft_stacked,        R.string.ft_inline,         R.string.m_inline,          R.string.m_inline,      true),
    PERCENT_GRADE   (6,     R.string.percent_grade,    R.string.percent_inline,    R.string.percent_inline,    R.string.percent_inline,    R.string.percent_inline,true),
    POWER           (7,     R.string.current_power,    R.string.w_inline,          R.string.w_inline,          R.string.w_inline,          R.string.w_inline,      false),
    POWER_AVERAGE   (8,     R.string.averagePower,     R.string.w_inline,          R.string.w_inline,          R.string.w_inline,          R.string.w_inline,      true),
    POWER_5_SEC     (9,     R.string.five_s_power,     R.string.w_inline,          R.string.w_inline,          R.string.w_inline,          R.string.w_inline,      true),
    POWER_30_SEC    (10,    R.string.thirty_s_power,   R.string.w_inline,          R.string.w_inline,          R.string.w_inline,          R.string.w_inline,      true),
    POWER_5_MIN     (11,    R.string.five_min_power,   R.string.w_inline,          R.string.w_inline,          R.string.w_inline,          R.string.w_inline,      true)
    ;

    private final int metricNumber;
    private final int label;
    private final int units;
    private final int units_inline;
    private final int units_metric;
    private final int units_metric_inline;
    private final boolean gpsDependent;

    Metrics(int metricNumber, int label, int units, int units_inline, int units_metric, int units_metric_inline, boolean gpsDependent) {
        this.metricNumber = metricNumber;
        this.label = label;
        this.units = units;
        this.units_inline = units_inline;
        this.units_metric = units_metric;
        this.units_metric_inline = units_metric_inline;
        this.gpsDependent = gpsDependent;
    }

    public Integer getMetricNumber(){
        return metricNumber;
    }

    public Integer getLabel() {
        return label;
    }

    public Integer getUnits(boolean metric) {
        return (metric) ? units_metric : units;
    }

    public Integer getInlineUnits(boolean metric){
        return (metric) ? units_metric_inline : units_inline;
    }

    public boolean isGpsDependent(){
        return gpsDependent;
    }
}*/
