package com.example.lifecut.graphitems;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import com.example.lifecut.R;
import com.example.lifecut.RadarMarkerView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class RadialChartItem extends ChartItem {

    private final String[] mEmotions = new String[]{"Happiness", "Sadness", "Surprise", "Fear", "Neutral", "Contempt","Anger","Disgust" };

    public RadialChartItem(ChartData<?> cd, Context c) {
        super(cd);
    }



    @Override
    public int getItemType() {
        return TYPE_RADARCHART;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, Context c) {


        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_radarchart, null);
            holder.chart = convertView.findViewById(R.id.chart);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.chart.setBackgroundColor(Color.));

        holder.chart.getDescription().setEnabled(false);

        holder.chart.setWebLineWidth(1f);
        holder.chart.setWebColor(Color.LTGRAY);
        holder.chart.setWebLineWidthInner(1f);
        holder.chart.setWebColorInner(Color.LTGRAY);
        holder.chart.setWebAlpha(100);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MarkerView mv = new RadarMarkerView(c, R.layout.radar_markerview);
        mv.setChartView(holder.chart); // For bounds control
        holder.chart.setMarker(mv); // Set the marker to the chart

        holder.chart.setData((RadarData) mChartData);
        holder.chart.invalidate();



        holder.chart.animateXY(750, 750, Easing.EaseInOutQuad);

        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new MyXAxisValueFormatter(mEmotions));
        xAxis.setTextColor(Color.BLACK);

        YAxis yAxis = holder.chart.getYAxis();
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);
        yAxis.setDrawLabels(false);

        Legend l = holder.chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.BLACK);

        return convertView;
    }


    private static class ViewHolder {
        RadarChart chart;
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            //return mValues[(int) value];
            return mValues[(int) value % mValues.length];
        }

    }
}


